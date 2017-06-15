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

package com.liferay.portal.portlet.bridge.soy.internal;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommandCache;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.URL;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Bruno Basto
 */
public class SoyPortletHelper {

	/**
	 * SoyPortletHelper constructor.
	 *
	 * @param bundle The bundle used by this helper.
	 * @param mvcRenderCommandCache The MVCCommandCache used by this helper.
	 */
	public SoyPortletHelper(
			Bundle bundle, MVCCommandCache mvcRenderCommandCache)
		throws Exception {

		_bundle = bundle;
		_mvcRenderCommandCache = mvcRenderCommandCache;
	}

	/**
	 * Returns the JavaScript module name given a path. The returned module definition string can be input to the the
	 * AMD Loader, for example.
	 *
	 * @param mvcCommandName The mvcCommandName of the View being requested.
	 * @throws Exception
	 * @return The JavaScript module name.
	 */
	public String getJavaScriptLoaderModule(String mvcCommandName)
		throws Exception {

		String controllerName = getJavaScriptControllerName(mvcCommandName);

		String packageName = getJavaScriptPackageName(mvcCommandName);

		if (packageName == null) {
			throw new Exception("Could not retrieve package name.");
		}

		if (!controllerName.startsWith(StringPool.SLASH)) {
			packageName = packageName.concat(StringPool.SLASH);
		}

		return packageName.concat(controllerName);
	}

	/**
	 * Returns the template namespace for a given path.
	 *
	 * @param path The path to be used.
	 * @return 	The path concatenated with the string ".render".
	 */
	public String getTemplateNamespace(String path) {
		return path.concat(".render");
	}

	/**
	 * Returns the {@code Bundle} for a given path.
	 *
	 * @param mvcCommandName The {@code MVCCommand} name for which to retreive the {@code Bundle}.
	 * @throws PortletException
	 * @return A {@code Bundle} for the specified {@code MVCCommand}.
	 */
	protected Bundle getMVCCommandBundle(String mvcCommandName)
		throws PortletException {

		MVCCommand mvcRenderCommand;

		if (Validator.isNull(mvcCommandName)) {
			mvcRenderCommand = MVCRenderCommand.EMPTY;
		}
		else {
			mvcRenderCommand = _mvcRenderCommandCache.getMVCCommand(
				mvcCommandName);
		}

		if (mvcRenderCommand == MVCRenderCommand.EMPTY) {
			return _bundle;
		}

		return FrameworkUtil.getBundle(mvcRenderCommand.getClass());
	}

	/**
	 * Returns the JavaScript controller name for given {@code MVCCommand} name.
	 * For example:
	 * If a JavaScript resource matching the controller name of the {@code MVCCommand} is found, this method will return its
	 * name. If not, it will return the name of the Soy resource matching the controller name.
	 *
	 * @param mvcCommandName The {@code MVCCommand} name .
	 * @return The JavaScript controller name.
	 * @throws PortletException
	 */
	protected String getJavaScriptControllerName(String mvcCommandName)
		throws PortletException {

		String controllerName = _controllersMap.get(mvcCommandName);

		if (controllerName != null) {
			return controllerName;
		}

		Bundle bundle = getMVCCommandBundle(mvcCommandName);

		String filePath = getJavaScriptFilePath(bundle, mvcCommandName);

		if (filePath.endsWith(".js")) {
			filePath = StringUtil.replace(filePath, ".js", StringPool.BLANK);
		}

		controllerName = StringUtil.replace(
			filePath, _RESOURCES_PATH, StringPool.BLANK);

		_controllersMap.put(mvcCommandName, controllerName);

		return controllerName;
	}

	/**
	 * Returns the JavaScript controller file path for a given {@code Bundle} MVC Command name.
	 *
	 * @param bundle The {@code Bundle} in which to find the controller file.
	 * @param mvcCommandName The MVC command name.
	 * @return The JavaScript controller file path.
	 * @throws PortletException
	 */
	protected String getJavaScriptFilePath(Bundle bundle, String mvcCommandName)
		throws PortletException {

		String resourcesPath = _RESOURCES_PATH;

		if (!mvcCommandName.startsWith(StringPool.SLASH)) {
			resourcesPath = resourcesPath.concat(StringPool.SLASH);
		}

		String filePath = resourcesPath.concat(mvcCommandName).concat(".js");

		if (bundle.getEntry(filePath) != null) {
			return filePath;
		}

		filePath = resourcesPath.concat(mvcCommandName).concat(".es.js");

		if (bundle.getEntry(filePath) != null) {
			return filePath;
		}

		filePath = resourcesPath.concat(mvcCommandName).concat(".soy");

		if (bundle.getEntry(filePath) != null) {
			return filePath;
		}

		throw new PortletException(
			"Could not find controller for path '" + mvcCommandName + "'");
	}

	/**
	 * Return the JavaScript package name for a given path.
	 *
	 * @param   path The path of the {@code Bundle} to get the JavaScript
	 * 				 package from.
	 * @return       The JavaScript package name found in the given {@Bundle}.
	 *
	 * @throws Exception
	 */
	protected String getJavaScriptPackageName(String path) throws Exception {
		Bundle bundle = getMVCCommandBundle(path);

		URL url = bundle.getEntry("package.json");

		if (url == null) {
			return null;
		}

		String json = StringUtil.read(url.openStream());

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(json);

		String moduleName = jsonObject.getString("name");

		String moduleVersion = jsonObject.getString("version");

		if (Validator.isNull(moduleName)) {
			return null;
		}

		if (Validator.isNull(moduleVersion)) {
			return moduleName;
		}

		return moduleName.concat(StringPool.AT).concat(moduleVersion);
	}

	private static final String _RESOURCES_PATH = "/META-INF/resources";

	private final Bundle _bundle;
	private final Map<String, String> _controllersMap = new HashMap<>();
	private final MVCCommandCache _mvcRenderCommandCache;

}