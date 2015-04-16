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

package com.liferay.portlet.dynamicdatamapping.io;

import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistryUtil;
import com.liferay.portlet.dynamicdatamapping.registry.settings.DDMFormFieldTypeSetting;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Marcellus Tavares
 */
public class DDMFormJSONSerializerImpl implements DDMFormJSONSerializer {

	@Override
	public String serialize(DDMForm ddmForm) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		addAvailableLanguageIds(jsonObject, ddmForm.getAvailableLocales());
		addDefaultLanguageId(jsonObject, ddmForm.getDefaultLocale());
		addFields(jsonObject, ddmForm.getDDMFormFields());

		return jsonObject.toString();
	}

	protected void addAvailableLanguageIds(
		JSONObject jsonObject, Set<Locale> availableLocales) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (Locale availableLocale : availableLocales) {
			jsonArray.put(LocaleUtil.toLanguageId(availableLocale));
		}

		jsonObject.put("availableLanguageIds", jsonArray);
	}

	protected void addDefaultLanguageId(
		JSONObject jsonObject, Locale defaultLocale) {

		jsonObject.put(
			"defaultLanguageId", LocaleUtil.toLanguageId(defaultLocale));
	}

	protected void addFields(
		JSONObject jsonObject, List<DDMFormField> ddmFormFields) {

		jsonObject.put("fields", toJSONArray(ddmFormFields));
	}

	protected void addLocalizedProperty(
		JSONObject jsonObject, String propertyName,
		LocalizedValue localizedValue) {

		Map<Locale, String> values = localizedValue.getValues();

		if (values.isEmpty()) {
			return;
		}

		jsonObject.put(propertyName, toJSONObject(localizedValue));
	}

	protected void addNestedFields(
		JSONObject jsonObject, List<DDMFormField> nestedDDMFormFields) {

		if (nestedDDMFormFields.isEmpty()) {
			return;
		}

		jsonObject.put("nestedFields", toJSONArray(nestedDDMFormFields));
	}

	protected void addOptions(
		JSONObject jsonObject, DDMFormFieldOptions ddmFormFieldOptions) {

		Set<String> optionValues = ddmFormFieldOptions.getOptionsValues();

		if (optionValues.isEmpty()) {
			return;
		}

		jsonObject.put("options", toJSONArray(ddmFormFieldOptions));
	}

	protected void addSettings(
		JSONObject jsonObject, DDMFormField ddmFormField,
		List<DDMFormFieldTypeSetting> settings) {

		for (DDMFormFieldTypeSetting setting : settings) {
			String settingName = setting.getName();

			if (setting.getName().equals("ddmFormFieldOptions")) {
				addOptions(jsonObject, ddmFormField.getDDMFormFieldOptions());
			}
			else {
				Object value = BeanPropertiesUtil.getObjectSilent(
					ddmFormField, settingName);

				if (value == null) {
					value = ddmFormField.getProperty(settingName);
				}

				if (setting.isLocalizable()) {
					addLocalizedProperty(
						jsonObject, settingName, (LocalizedValue)value);
				}
				else {
					Class<?> settingType = value.getClass();

					if (settingType.equals(boolean.class) ||
						settingType.equals(Boolean.class)) {

						jsonObject.put(setting.getName(), (boolean)value);
					}
					else {
						jsonObject.put(
							setting.getName(), String.valueOf(value));
					}
				}
			}
		}
	}

	protected JSONArray toJSONArray(DDMFormFieldOptions ddmFormFieldOptions) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (String optionValue : ddmFormFieldOptions.getOptionsValues()) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put("value", optionValue);
			jsonObject.put(
				"label",
				toJSONObject(ddmFormFieldOptions.getOptionLabels(optionValue)));

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	protected JSONArray toJSONArray(List<DDMFormField> ddmFormFields) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (DDMFormField ddmFormField : ddmFormFields) {
			jsonArray.put(toJSONObject(ddmFormField));
		}

		return jsonArray;
	}

	protected JSONObject toJSONObject(DDMFormField ddmFormField) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		DDMFormFieldType ddmFormFieldType =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldType(
				ddmFormField.getType());

		addSettings(
			jsonObject, ddmFormField, ddmFormFieldType.getRequiredSettings());
		addSettings(
			jsonObject, ddmFormField, ddmFormFieldType.getOptionalSettings());
		addNestedFields(jsonObject, ddmFormField.getNestedDDMFormFields());

		return jsonObject;
	}

	protected JSONObject toJSONObject(LocalizedValue localizedValue) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		for (Locale availableLocale : localizedValue.getAvailableLocales()) {
			jsonObject.put(
				LocaleUtil.toLanguageId(availableLocale),
				localizedValue.getString(availableLocale));
		}

		return jsonObject;
	}

}