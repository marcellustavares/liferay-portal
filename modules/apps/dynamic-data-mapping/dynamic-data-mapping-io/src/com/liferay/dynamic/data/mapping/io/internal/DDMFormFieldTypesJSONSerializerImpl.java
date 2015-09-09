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

package com.liferay.dynamic.data.mapping.io.internal;

import com.liferay.dynamic.data.mapping.io.DDMFormFieldTypesJSONSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.registry.DDMFormFactory;
import com.liferay.dynamic.data.mapping.registry.DDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.registry.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.registry.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.registry.DDMFormFieldTypeSettings;
import com.liferay.dynamic.data.mapping.registry.annotations.DDMFormField;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(immediate = true)
public class DDMFormFieldTypesJSONSerializerImpl
	implements DDMFormFieldTypesJSONSerializer {

	@Override
	public String serialize(List<DDMFormFieldType> ddmFormFieldTypes)
		throws PortalException {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (DDMFormFieldType ddmFormFieldType : ddmFormFieldTypes) {
			jsonArray.put(toJSONObject(ddmFormFieldType));
		}

		return jsonArray.toString();
	}

	protected void collectDDMFormFieldSetting(
		Class<?> clazz,
		Map<String, List<DDMFormFieldSetting>> ddmFormFieldSettingsMap) {

		for (Class<?> interfaceClass : clazz.getInterfaces()) {
			collectDDMFormFieldSetting(interfaceClass, ddmFormFieldSettingsMap);
		}

		for (Method method : clazz.getDeclaredMethods()) {
			if (!method.isAnnotationPresent(_DDM_FORM_FIELD_ANNOTATION)) {
				continue;
			}

			DDMFormField ddmFormField = method.getAnnotation(
				DDMFormField.class);

			String ddmFormFieldName = method.getName();

			if (Validator.isNotNull(ddmFormField.name())) {
				ddmFormFieldName = ddmFormField.name();
			}

			DDMFormFieldSetting setting = new DDMFormFieldSetting(
				ddmFormFieldName, ddmFormField.properties());

			List<DDMFormFieldSetting> ddmFormFieldSettings =
				ddmFormFieldSettingsMap.get(setting.getCategory());

			ddmFormFieldSettings.add(setting);
		}
	}

	protected DDMFormLayoutPage createSettingPage(
		List<DDMFormFieldSetting> ddmFormFieldSettings) {

		DDMFormLayoutPage ddmFormLayoutPage = new DDMFormLayoutPage();

		DDMFormLayoutRow ddmFormLayoutRow = new DDMFormLayoutRow();

		DDMFormLayoutColumn ddmFormLayoutColumn = new DDMFormLayoutColumn();

		ddmFormLayoutColumn.setSize(DDMFormLayoutColumn.FULL);

		Collections.sort(ddmFormFieldSettings);

		List<String> ddmFormFieldNames = ListUtil.toList(
			ddmFormFieldSettings,
			new Function<DDMFormFieldSetting, String>() {

				@Override
				public String apply(DDMFormFieldSetting ddmFormFieldSetting) {
					return ddmFormFieldSetting.getName();
				}

			});

		ddmFormLayoutColumn.setDDMFormFieldNames(ddmFormFieldNames);

		ddmFormLayoutRow.addDDMFormLayoutColumn(ddmFormLayoutColumn);

		ddmFormLayoutPage.addDDMFormLayoutRow(ddmFormLayoutRow);

		return ddmFormLayoutPage;
	}

	protected DDMFormLayout getDDMFormLayout(
		Class<? extends DDMFormFieldTypeSettings> ddmFormLayoutSettings) {

		Map<String, List<DDMFormFieldSetting>> ddmFormFieldSettingsMap =
			new HashMap<>();

		ddmFormFieldSettingsMap.put(
			"basic", new ArrayList<DDMFormFieldSetting>());
		ddmFormFieldSettingsMap.put(
			"advanced", new ArrayList<DDMFormFieldSetting>());

		collectDDMFormFieldSetting(
			ddmFormLayoutSettings, ddmFormFieldSettingsMap);

		DDMFormLayout ddmFormLayout = new DDMFormLayout();

		ddmFormLayout.addDDMFormLayoutPage(
			createSettingPage(ddmFormFieldSettingsMap.get("basic")));
		ddmFormLayout.addDDMFormLayoutPage(
			createSettingPage(ddmFormFieldSettingsMap.get("advanced")));

		return ddmFormLayout;
	}

	@Reference
	protected void setDDMFormFieldTypeServicesTracker(
		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker) {

		_ddmFormFieldTypeServicesTracker = ddmFormFieldTypeServicesTracker;
	}

	@Reference
	protected void setDDMFormJSONSerializer(
		DDMFormJSONSerializer ddmFormJSONSerializer) {

		_ddmFormJSONSerializer = ddmFormJSONSerializer;
	}

	@Reference
	protected void setDDMFormLayoutJSONSerializer(
		DDMFormLayoutJSONSerializer ddmFormLayoutJSONSerializer) {

		_ddmFormLayoutJSONSerializer = ddmFormLayoutJSONSerializer;
	}

	protected JSONObject toJSONObject(
			Class<? extends DDMFormFieldTypeSettings> ddmFormFieldTypeSettings)
		throws PortalException {

		DDMForm ddmFormFieldTypeSettingsDDMForm = DDMFormFactory.create(
			ddmFormFieldTypeSettings);

		String serializedDDMFormFieldTypeSettings =
			_ddmFormJSONSerializer.serialize(ddmFormFieldTypeSettingsDDMForm);

		return JSONFactoryUtil.createJSONObject(
			serializedDDMFormFieldTypeSettings);
	}

	protected JSONObject toJSONObject(DDMForm ddmForm) throws PortalException {
		String serializedDDMForm = _ddmFormJSONSerializer.serialize(ddmForm);

		return JSONFactoryUtil.createJSONObject(serializedDDMForm);
	}

	protected JSONObject toJSONObject(DDMFormFieldType ddmFormFieldType)
		throws PortalException {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		Map<String, Object> ddmFormFieldTypeProperties =
			_ddmFormFieldTypeServicesTracker.getDDMFormFieldTypeProperties(
				ddmFormFieldType.getName());

		jsonObject.put(
			"icon",
			MapUtil.getString(
				ddmFormFieldTypeProperties, "ddm.form.field.type.icon",
				"icon-ok-circle"));
		jsonObject.put(
			"javaScriptClass",
			MapUtil.getString(
				ddmFormFieldTypeProperties, "ddm.form.field.type.js.class.name",
				"Liferay.DDM.Renderer.Field"));
		jsonObject.put(
			"javaScriptModule",
			MapUtil.getString(
				ddmFormFieldTypeProperties, "ddm.form.field.type.js.module",
				"liferay-ddm-form-renderer-field"));
		jsonObject.put("name", ddmFormFieldType.getName());

		DDMForm ddmForm = DDMFormFactory.create(
			ddmFormFieldType.getDDMFormFieldTypeSettings());

		jsonObject.put("settings", toJSONObject(ddmForm));

		DDMFormLayout ddmFormLayout = getDDMFormLayout(
			ddmFormFieldType.getDDMFormFieldTypeSettings());

		jsonObject.put("settingsLayout", toJSONObject(ddmFormLayout));
		jsonObject.put(
			"system",
			MapUtil.getBoolean(
				ddmFormFieldTypeProperties, "ddm.form.field.type.system"));

		DDMFormFieldRenderer ddmFormFieldRenderer =
			_ddmFormFieldTypeServicesTracker.getDDMFormFieldRenderer(
				ddmFormFieldType.getName());

		jsonObject.put(
			"templateNamespace", ddmFormFieldRenderer.getTemplateNamespace());

		return jsonObject;
	}

	protected JSONObject toJSONObject(DDMFormLayout ddmFormLayout)
		throws PortalException {

		String serializedDDMFormLayout = _ddmFormLayoutJSONSerializer.serialize(
			ddmFormLayout);

		return JSONFactoryUtil.createJSONObject(serializedDDMFormLayout);
	}

	private static final Class<? extends Annotation>
		_DDM_FORM_FIELD_ANNOTATION =
			com.liferay.dynamic.data.mapping.registry.annotations.DDMFormField.
				class;

	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;
	private DDMFormJSONSerializer _ddmFormJSONSerializer;
	private DDMFormLayoutJSONSerializer _ddmFormLayoutJSONSerializer;

	private static class DDMFormFieldSetting
		implements Comparable<DDMFormFieldSetting> {

		public DDMFormFieldSetting(String name, String[] properties) {
			_name = name;
			_category = "advanced";
			_weight = 0;

			for (String property : properties) {
				String propertyName = StringUtil.extractFirst(
					property, StringPool.EQUAL);
				String propertyValue = StringUtil.extractLast(
					property, StringPool.EQUAL);

				if (Validator.equals(propertyName, "setting.category")) {
					_category = propertyValue;
				}

				if (Validator.equals(propertyName, "setting.weight")) {
					_weight = Integer.valueOf(propertyValue);
				}
			}
		}

		@Override
		public int compareTo(DDMFormFieldSetting ddmFormFieldSetting) {
			return -(Integer.compare(_weight, ddmFormFieldSetting._weight));
		}

		public String getCategory() {
			return _category;
		}

		public String getName() {
			return _name;
		}

		private String _category;
		private final String _name;
		private int _weight;

	}

}