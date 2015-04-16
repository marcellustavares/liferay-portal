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
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Marcellus Tavares
 */
public class DDMFormJSONDeserializerImpl implements DDMFormJSONDeserializer {

	@Override
	public DDMForm deserialize(String serializedDDMForm)
		throws PortalException {

		try {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				serializedDDMForm);

			DDMForm ddmForm = new DDMForm();

			setDDMFormAvailableLocales(
				jsonObject.getJSONArray("availableLanguageIds"), ddmForm);
			setDDMFormDefaultLocale(
				jsonObject.getString("defaultLanguageId"), ddmForm);
			setDDMFormFields(jsonObject.getJSONArray("fields"), ddmForm);
			setDDMFormLocalizedValuesDefaultLocale(ddmForm);

			return ddmForm;
		}
		catch (JSONException jsone) {
			throw new PortalException(jsone);
		}
	}

	protected void addOptionValueLabels(
		JSONObject jsonObject, DDMFormFieldOptions ddmFormFieldOptions,
		String optionValue) {

		Iterator<String> itr = jsonObject.keys();

		while (itr.hasNext()) {
			String languageId = itr.next();

			ddmFormFieldOptions.addOptionLabel(
				optionValue, LocaleUtil.fromLanguageId(languageId),
				jsonObject.getString(languageId));
		}
	}

	protected Set<Locale> getAvailableLocales(JSONArray jsonArray) {
		Set<Locale> availableLocales = new HashSet<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			Locale availableLocale = LocaleUtil.fromLanguageId(
				jsonArray.getString(i));

			availableLocales.add(availableLocale);
		}

		return availableLocales;
	}

	protected DDMFormField getDDMFormField(JSONObject jsonObject) {
		String name = jsonObject.getString("name");
		String type = jsonObject.getString("type");

		DDMFormField ddmFormField = new DDMFormField(name, type);

		DDMFormFieldType ddmFormFieldType =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldType(type);

		if (ddmFormFieldType == null) {
			return ddmFormField;
		}

		List<DDMFormFieldTypeSetting> requiredSettings =
			ddmFormFieldType.getRequiredSettings();

		for (DDMFormFieldTypeSetting setting : requiredSettings) {
			if (setting.getName().equals("ddmFormFieldOptions")) {
				JSONArray optionsJSONArray = jsonObject.getJSONArray("options");

				if (optionsJSONArray != null) {
					DDMFormFieldOptions ddmFormFieldOptions =
						getDDMFormFieldOptions(optionsJSONArray);

					ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);
				}
			}
			else {
				Object value = getDDMFormFieldSettingValue(
					ddmFormField, setting, jsonObject);

				BeanPropertiesUtil.setProperty(
					ddmFormField, setting.getName(), value);
			}
		}

		List<DDMFormFieldTypeSetting> optionalSettings =
			ddmFormFieldType.getOptionalSettings();

		for (DDMFormFieldTypeSetting setting : optionalSettings) {
			Object value = getDDMFormFieldSettingValue(
				ddmFormField, setting, jsonObject);

			ddmFormField.setProperty(setting.getName(), value);
		}

		setNestedDDMFormField(
			jsonObject.getJSONArray("nestedFields"), ddmFormField);

		return ddmFormField;
	}

	protected DDMFormFieldOptions getDDMFormFieldOptions(JSONArray jsonArray) {
		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject optionJSONObject = jsonArray.getJSONObject(i);

			String value = optionJSONObject.getString("value");

			ddmFormFieldOptions.addOption(value);

			JSONObject labelJSONObject = optionJSONObject.getJSONObject(
				"label");

			addOptionValueLabels(labelJSONObject, ddmFormFieldOptions, value);
		}

		return ddmFormFieldOptions;
	}

	protected List<DDMFormField> getDDMFormFields(JSONArray jsonArray) {
		List<DDMFormField> ddmFormFields = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			DDMFormField ddmFormField = getDDMFormField(
				jsonArray.getJSONObject(i));

			ddmFormFields.add(ddmFormField);
		}

		return ddmFormFields;
	}

	protected LocalizedValue getDDMFormFieldSettingLocalizedValue(
		DDMFormField ddmFormField, JSONObject jsonObject) {

		LocalizedValue localizedValue = new LocalizedValue();

		setDDMFormFieldLocalizedValue(jsonObject, localizedValue);

		return localizedValue;
	}

	protected Object getDDMFormFieldSettingValue(
		DDMFormField ddmFormField,
		DDMFormFieldTypeSetting ddmFormFieldTypeSetting,
		JSONObject settingsJSONObject) {

		String settingName = ddmFormFieldTypeSetting.getName();

		Class<?> settingType = BeanPropertiesUtil.getObjectType(
			ddmFormField, settingName);

		Object settingValue = null;

		if (ddmFormFieldTypeSetting.isLocalizable()) {
			settingValue = getDDMFormFieldSettingLocalizedValue(
				ddmFormField, settingsJSONObject.getJSONObject(settingName));
		}
		else if (settingType == null) {
			settingValue = settingsJSONObject.getString(settingName);
		}
		else {
			if (settingType.equals(boolean.class) ||
				settingType.equals(Boolean.class)) {

				settingValue = settingsJSONObject.getBoolean(settingName);
			}
			else if (settingType.equals(String.class)) {
				settingValue = settingsJSONObject.getString(settingName);
			}
		}

		return settingValue;
	}

	protected void setDDMFormAvailableLocales(
		JSONArray jsonArray, DDMForm ddmForm) {

		Set<Locale> availableLocales = getAvailableLocales(jsonArray);

		ddmForm.setAvailableLocales(availableLocales);
	}

	protected void setDDMFormDefaultLocale(
		String defaultLanguageId, DDMForm ddmForm) {

		Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId);

		ddmForm.setDefaultLocale(defaultLocale);
	}

	protected void setDDMFormFieldLocalizedValue(
		JSONObject jsonObject, LocalizedValue localizedValue) {

		if (jsonObject == null) {
			return;
		}

		Iterator<String> itr = jsonObject.keys();

		while (itr.hasNext()) {
			String languageId = itr.next();

			localizedValue.addString(
				LocaleUtil.fromLanguageId(languageId),
				jsonObject.getString(languageId));
		}
	}

	protected void setDDMFormFieldLocalizedValuesDefaultLocale(
		DDMFormField ddmFormField, Locale defaultLocale) {

		LocalizedValue label = ddmFormField.getLabel();

		label.setDefaultLocale(defaultLocale);

		LocalizedValue predefinedValue = ddmFormField.getPredefinedValue();

		predefinedValue.setDefaultLocale(defaultLocale);

		LocalizedValue style = ddmFormField.getStyle();

		style.setDefaultLocale(defaultLocale);

		LocalizedValue tip = ddmFormField.getTip();

		tip.setDefaultLocale(defaultLocale);

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormField.getDDMFormFieldOptions();

		ddmFormFieldOptions.setDefaultLocale(defaultLocale);

		for (DDMFormField nestedDDMFormField :
				ddmFormField.getNestedDDMFormFields()) {

			setDDMFormFieldLocalizedValuesDefaultLocale(
				nestedDDMFormField, defaultLocale);
		}
	}

	protected void setDDMFormFieldOptions(
		JSONArray jsonArray, DDMFormField ddmFormField) {

		DDMFormFieldOptions ddmFormFieldOptions = getDDMFormFieldOptions(
			jsonArray);

		ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);
	}

	protected void setDDMFormFields(JSONArray jsonArray, DDMForm ddmForm) {
		List<DDMFormField> ddmFormFields = getDDMFormFields(jsonArray);

		ddmForm.setDDMFormFields(ddmFormFields);
	}

	protected void setDDMFormLocalizedValuesDefaultLocale(DDMForm ddmForm) {
		for (DDMFormField ddmFormField : ddmForm.getDDMFormFields()) {
			setDDMFormFieldLocalizedValuesDefaultLocale(
				ddmFormField, ddmForm.getDefaultLocale());
		}
	}

	protected void setNestedDDMFormField(
		JSONArray jsonArray, DDMFormField ddmFormField) {

		if ((jsonArray == null) || (jsonArray.length() == 0)) {
			return;
		}

		List<DDMFormField> nestedDDMFormFields = getDDMFormFields(jsonArray);

		ddmFormField.setNestedDDMFormFields(nestedDDMFormFields);
	}

}