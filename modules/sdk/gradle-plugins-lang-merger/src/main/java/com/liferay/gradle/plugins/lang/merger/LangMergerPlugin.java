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

import com.liferay.gradle.util.GradleUtil;

import java.io.File;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.SourceSet;

/**
 * @author Marcellus Tavares
 */
public class LangMergerPlugin implements Plugin<Project> {

	public static final String CONFIGURATION_NAME = "langMerger";

	public static final String MERGE_LANG_TASK_NAME = "mergeLang";

	@Override
	public void apply(Project project) {
		addTaskMergeLang(project);
	}

	protected void addDependenciesLangBuilder(Project project) {
		GradleUtil.addDependency(
			project, CONFIGURATION_NAME, "com.liferay",
			"com.liferay.lang.merger", "latest.release");
	}

	protected MergeLangTask addTaskMergeLang(Project project) {
		final MergeLangTask mergeLangTask = GradleUtil.addTask(
			project, MERGE_LANG_TASK_NAME, MergeLangTask.class);

		mergeLangTask.setDescription(
			"Runs Liferay Lang Merger to merge language property files.");

		PluginContainer pluginContainer = project.getPlugins();

		pluginContainer.withType(
			JavaPlugin.class,
			new Action<JavaPlugin>() {

				@Override
				public void execute(JavaPlugin javaPlugin) {
					configureTaskMergeLangForJavaPlugin(mergeLangTask);
				}

			});

		return mergeLangTask;
	}

	protected void configureTaskMergeLangClasspath(
		MergeLangTask mergeLangTask, FileCollection fileCollection) {

		mergeLangTask.setClasspath(fileCollection);
	}

	protected void configureTaskMergeLangForJavaPlugin(
		final MergeLangTask mergeLangTask) {

		mergeLangTask.setLangDir(
			new Callable<File>() {

				@Override
				public File call() throws Exception {
					return new File(
						getResourcesDir(mergeLangTask.getProject()), "content");
				}

			});
	}

	protected File getResourcesDir(Project project) {
		SourceSet sourceSet = GradleUtil.getSourceSet(
			project, SourceSet.MAIN_SOURCE_SET_NAME);

		return getSrcDir(sourceSet.getResources());
	}

	protected File getSrcDir(SourceDirectorySet sourceDirectorySet) {
		Set<File> srcDirs = sourceDirectorySet.getSrcDirs();

		Iterator<File> iterator = srcDirs.iterator();

		return iterator.next();
	}

}