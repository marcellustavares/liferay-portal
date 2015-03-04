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

package com.liferay.portlet.dynamicdatamapping.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistryUtil;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeSettingEditor;

/**
 * @author Bruno Basto
 */
@DoPrivileged
public class DDMFormFieldTypesHelper {

	public static JSONArray getFieldTypesJSONArray() {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		Set<String> ddmFormFieldTypeNames =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldTypeNames();

		for (String ddmFormFieldTypeName : ddmFormFieldTypeNames) {
			JSONObject ddmFormFieldTypeJSONObject =
				JSONFactoryUtil.createJSONObject();

			DDMFormFieldType ddmFormFieldType =
				DDMFormFieldTypeRegistryUtil.getDDMFormFieldType(
					ddmFormFieldTypeName);
			
			addSettings(ddmFormFieldType, ddmFormFieldTypeJSONObject);

			ddmFormFieldTypeJSONObject.put("icon", ddmFormFieldType.getIcon());
			ddmFormFieldTypeJSONObject.put(
				"label", ddmFormFieldType.getLabel());
			ddmFormFieldTypeJSONObject.put("name", ddmFormFieldTypeName);

			jsonArray.put(ddmFormFieldTypeJSONObject);
		}

		return jsonArray;
	}

	private static void addSettings(
		DDMFormFieldType ddmFormFieldType,
		JSONObject ddmFormFieldTypeJSONObject) {

		JSONArray advancedSettingsJSONArray = JSONFactoryUtil.createJSONArray();
		JSONArray basicSettingsJSONArray = JSONFactoryUtil.createJSONArray();

		List<DDMFormFieldTypeSetting> settings =
			new ArrayList<DDMFormFieldTypeSetting>(
				ddmFormFieldType.getRequiredSettings());

		settings.addAll(ddmFormFieldType.getOptionalSettings());

		for (DDMFormFieldTypeSetting setting : settings) {
			if (!setting.isVisible()) {
				continue;
			}

			JSONObject settingJSONObject = JSONFactoryUtil.createJSONObject();

			settingJSONObject.put("attrName", setting.getName());

			DDMFormFieldTypeSettingEditor editor =
				setting.getDDMFormFieldTypeSettingEditor();

			JSONObject editorOptions = editor.getOptions();

			if (editorOptions != null) {
				settingJSONObject.put("editorOptions", editor.getOptions());
			}

			settingJSONObject.put("editorType", editor.getEditorType());

			settingJSONObject.put("localizable", setting.isLocalizable());
			settingJSONObject.put("visible", setting.isVisible());

			if (setting.isAdvanced()) {
				advancedSettingsJSONArray.put(settingJSONObject);
			}
			else {
				basicSettingsJSONArray.put(settingJSONObject);
			}
		}

		ddmFormFieldTypeJSONObject.put(
			"advancedSettings", advancedSettingsJSONArray);
		ddmFormFieldTypeJSONObject.put("basicSettings", basicSettingsJSONArray);
	}

}