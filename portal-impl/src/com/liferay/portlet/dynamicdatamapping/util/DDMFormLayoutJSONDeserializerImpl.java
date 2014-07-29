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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author Pablo Carvalho
 */
public class DDMFormLayoutJSONDeserializerImpl
	implements DDMFormLayoutJSONDeserializer {

	@Override
	public DDMForm deserialize(String layoutJSON) throws PortalException {
		try {
			JSONObject rootJSONObject = JSONFactoryUtil.createJSONObject(
				layoutJSON);

			JSONObject formLayoutJSONObject = rootJSONObject.getJSONObject(
				"layout");

			JSONObject formStructureJSONObject = rootJSONObject.getJSONObject(
				"structure");

			return getDDMForm(formLayoutJSONObject, formStructureJSONObject);
		}
		catch (JSONException je) {
			throw new PortalException(je);
		}
	}

	protected void addLayoutProperties(
		JSONObject fieldLayoutJSONObject, DDMFormField ddmFormField) {

		ddmFormField.setLabel(
			getLocalizedValue(fieldLayoutJSONObject.getJSONObject("label")));

		ddmFormField.setDDMFormFieldOptions(
			getDDMFormFieldOptions(
				fieldLayoutJSONObject.getJSONArray("options")));

		ddmFormField.setPredefinedValue(
			getLocalizedValue(
				fieldLayoutJSONObject.getJSONObject("predefinedValue")));

		ddmFormField.setStyle(
			getLocalizedValue(fieldLayoutJSONObject.getJSONObject("style")));

		ddmFormField.setTip(
			getLocalizedValue(fieldLayoutJSONObject.getJSONObject("tip")));

		ddmFormField.setType(fieldLayoutJSONObject.getString("type"));

	}

	protected void addNestedDDMFormFields(
		JSONObject fieldsLayoutJSONObject, JSONObject formStructureJSONObject,
		DDMFormField ddmFormField) {

		List<String> nestedDDMFormFieldsNames = getNestedDDMFormFieldsNames(
			ddmFormField, formStructureJSONObject);

		ddmFormField.setNestedDDMFormFields(
			getDDMFormFields(
				nestedDDMFormFieldsNames, fieldsLayoutJSONObject,
				formStructureJSONObject));
	}

	protected void addStructureProperties(
		JSONObject fieldStructureJSONObject, DDMFormField ddmFormField) {

		ddmFormField.setDataType(
			fieldStructureJSONObject.getString("dataType"));

		ddmFormField.setNamespace(
			fieldStructureJSONObject.getString("fieldNamespace"));

		ddmFormField.setIndexType(
			fieldStructureJSONObject.getString("indexType"));

		ddmFormField.setLocalizable(
			fieldStructureJSONObject.getBoolean("localizable"));

		ddmFormField.setMultiple(
			fieldStructureJSONObject.getBoolean("multiple"));

		ddmFormField.setRepeatable(
			fieldStructureJSONObject.getBoolean("repeatable"));

		ddmFormField.setReadOnly(
			fieldStructureJSONObject.getBoolean("readOnly"));

		ddmFormField.setRequired(
			fieldStructureJSONObject.getBoolean("required"));
	}

	protected List<Locale> getAvailableLocales(JSONObject layoutJSONObject) {
		JSONArray availableLanguagesArray = layoutJSONObject.getJSONArray(
			"availableLanguages");

		List<Locale> availableLocales = new ArrayList<Locale>(
			availableLanguagesArray.length());

		for (int i = 0; i < availableLanguagesArray.length(); i++) {
			String languageId = availableLanguagesArray.getString(i);

			availableLocales.add(LocaleUtil.fromLanguageId(languageId));
		}

		return availableLocales;
	}

	protected DDMForm getDDMForm(
		JSONObject formLayoutJSONObject, JSONObject formStructureJSONObject) {

		DDMForm ddmForm = new DDMForm();

		ddmForm.setAvailableLocales(getAvailableLocales(formLayoutJSONObject));

		ddmForm.setDefaultLocale(getDefaultLocale(formLayoutJSONObject));

		ddmForm.setDDMFormFields(
			getDDMFormFields(formLayoutJSONObject, formStructureJSONObject));

		return ddmForm;
	}

	protected DDMFormField getDDMFormField(
		String ddmFormFieldName, JSONObject fieldsLayoutJSONObject,
		JSONObject formStructureJSONObject) {

		JSONObject fieldLayoutJSONObject = fieldsLayoutJSONObject.getJSONObject(
			ddmFormFieldName);

		JSONObject fieldStructureJSONObject =
			formStructureJSONObject.getJSONObject(ddmFormFieldName);

		String type = fieldLayoutJSONObject.getString("type");

		DDMFormField ddmFormField = new DDMFormField(ddmFormFieldName, type);

		addLayoutProperties(fieldLayoutJSONObject, ddmFormField);

		addStructureProperties(fieldStructureJSONObject, ddmFormField);

		addNestedDDMFormFields(
			fieldsLayoutJSONObject, formStructureJSONObject, ddmFormField);

		return ddmFormField;
	}

	protected DDMFormFieldOptions getDDMFormFieldOptions(
		JSONArray ddmFormFieldOptionsJSONArray) {

		if (ddmFormFieldOptionsJSONArray == null) {
			return null;
		}

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		for (int i = 0; i < ddmFormFieldOptionsJSONArray.length(); i++) {
			JSONObject ddmFormFieldOptionJSONObject =
				ddmFormFieldOptionsJSONArray.getJSONObject(i);

			String optionValue = ddmFormFieldOptionJSONObject.getString(
				"value");

			ddmFormFieldOptions.addOption(optionValue);

			LocalizedValue labels = getLocalizedValue(
				ddmFormFieldOptionJSONObject.getJSONObject("labels"));

			ddmFormFieldOptions.setOptionLabels(optionValue, labels);
		}

		return ddmFormFieldOptions;
	}

	protected List<DDMFormField> getDDMFormFields(
		JSONObject formLayoutJSONObject, JSONObject formStructureJSONObject) {

		List<String> rootDDMFormFieldNames = getRootDDMFormFieldNamesFromPages(
			formLayoutJSONObject.getJSONArray("pages"));

		JSONObject fieldsLayoutJSONObject = formLayoutJSONObject.getJSONObject(
			"fieldsLayout");

		return getDDMFormFields(
			rootDDMFormFieldNames, fieldsLayoutJSONObject,
			formStructureJSONObject);
	}

	protected List<DDMFormField> getDDMFormFields(
		List<String> ddmFormFieldNames, JSONObject fieldsLayoutJSONObject,
		JSONObject formStructureJSONObject) {

		List<DDMFormField> ddmFormFields = new ArrayList<DDMFormField>(
			ddmFormFieldNames.size());

		for (String ddmFormFieldName : ddmFormFieldNames) {
			DDMFormField ddmFormField = getDDMFormField(
				ddmFormFieldName, fieldsLayoutJSONObject,
				formStructureJSONObject);

			ddmFormFields.add(ddmFormField);
		}

		return ddmFormFields;
	}

	protected Locale getDefaultLocale(JSONObject layoutJSONObject) {
		String defaultLanguageId = layoutJSONObject.getString(
			"defaultLanguage");

		return LocaleUtil.fromLanguageId(defaultLanguageId);
	}

	protected LocalizedValue getLocalizedValue(
		JSONObject localizedValueJSONObject) {

		LocalizedValue localizedValue = new LocalizedValue();

		if (localizedValueJSONObject == null) {
			return localizedValue;
		}

		Iterator<String> keysIterator = localizedValueJSONObject.keys();

		while (keysIterator.hasNext()) {
			String languageId = keysIterator.next();

			String value = localizedValueJSONObject.getString(languageId);

			localizedValue.addValue(
				LocaleUtil.fromLanguageId(languageId), value);
		}

		return localizedValue;
	}

	protected List<String> getNestedDDMFormFieldsNames(
		DDMFormField ddmFormField, JSONObject formStructureJSONObject) {

		JSONObject ddmFormFieldJSONObject =
			formStructureJSONObject.getJSONObject(ddmFormField.getName());

		JSONArray nestedDDMFormFieldsJSONArray =
			ddmFormFieldJSONObject.getJSONArray("nestedFields");

		if (nestedDDMFormFieldsJSONArray == null) {
			return new ArrayList<String>();
		}

		return stringJSONArrayToStringList(nestedDDMFormFieldsJSONArray);
	}

	protected List<String> getRootDDMFormFieldNamesFromPages(JSONArray pages) {
		List<String> ddmFormFieldNames = new ArrayList<String>();

		for (int i = 0; i < pages.length(); i++) {
			JSONObject page = pages.getJSONObject(i);

			JSONArray sections = page.getJSONArray("sections");

			ddmFormFieldNames.addAll(
				getRootDDMFormFieldNamesFromSections(sections));
		}

		return ddmFormFieldNames;
	}

	protected List<String> getRootDDMFormFieldNamesFromSections(
		JSONArray sections) {

		List<String> ddmFormFieldNames = new ArrayList<String>();

		for (int i = 0; i < sections.length(); i++) {
			JSONObject section = sections.getJSONObject(i);

			JSONArray fields = section.getJSONArray("fields");

			ddmFormFieldNames.addAll(stringJSONArrayToStringList(fields));
		}

		return ddmFormFieldNames;
	}

	protected List<String> stringJSONArrayToStringList(
		JSONArray stringJSONArray) {

		List<String> stringList = new ArrayList<String>(
			stringJSONArray.length());

		for (int i = 0; i < stringJSONArray.length(); i++) {
			stringList.add(stringJSONArray.getString(i));
		}

		return stringList;
	}

}