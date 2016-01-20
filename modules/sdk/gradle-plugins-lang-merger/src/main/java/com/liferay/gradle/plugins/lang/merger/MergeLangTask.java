/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.gradle.plugins.lang.merger;

import com.liferay.gradle.util.FileUtil;
import com.liferay.gradle.util.GradleUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.JavaExec;

/**
 * @author Marcellus Tavares
 */
public class MergeLangTask extends JavaExec {

	@Override
	public void exec() {
		setArgs(getCompleteArgs());

		File baseLangDir = getBaseLangDir();

		for (File baseLangPropertiesFile : getPropertiesFiles(baseLangDir)) {
			String baseLangFileName = baseLangPropertiesFile.getName();

			File langPropertiesFile = getLangPropertiesFile(baseLangFileName);

			if (langPropertiesFile == null) {
				langPropertiesFile = new File(getLangDir(), baseLangFileName);

				FileUtil.copy(baseLangPropertiesFile, langPropertiesFile);

				continue;
			}

			Properties baseLangProperties = readProperties(
				baseLangPropertiesFile);
			Properties langProperties = readProperties(langPropertiesFile);

			mergeLangProperties(baseLangProperties, langProperties);

			FileUtil.write(langPropertiesFile, langProperties.toString());
		}
	}

	@Input
	public File getBaseLangDir() {
		return GradleUtil.toFile(getProject(), _baseLangDir);
	}

	@Input
	public File getLangDir() {
		return GradleUtil.toFile(getProject(), _langDir);
	}

	public void setLangDir(Object langDir) {
		_langDir = langDir;
	}

	protected List<String> getCompleteArgs() {
		List<String> args = new ArrayList<>(getArgs());

		args.add(
			"lang.dir=" + FileUtil.relativize(getLangDir(), getWorkingDir()));

		return args;
	}

	protected File getLangPropertiesFile(String fileName) {
		File langDir = getLangDir();

		return GradleUtil.toFile(
			getProject(), langDir.getPath().concat(fileName));
	}

	protected File[] getPropertiesFiles(File parentFile) {
		return parentFile.listFiles(_propertiesFileFilter);
	}

	protected void mergeLangProperties(
		Properties sourceLangProperties, Properties destinationLangProperties) {

		for (Map.Entry<Object, Object> entry :
				sourceLangProperties.entrySet()) {

			destinationLangProperties.put(entry.getKey(), entry.getValue());
		}
	}

	protected Properties readProperties(File langPropertiesFile)
		throws IOException {

		return FileUtil.readProperties(langPropertiesFile);
	}

	private Object _baseLangDir;
	private Object _langDir;
	private final PropertiesFileFilter _propertiesFileFilter =
		new PropertiesFileFilter();

	private static class PropertiesFileFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			String fileName = file.getName();

			if (fileName.endsWith(".properties")) {
				return true;
			}

			return false;
		}

	}

}