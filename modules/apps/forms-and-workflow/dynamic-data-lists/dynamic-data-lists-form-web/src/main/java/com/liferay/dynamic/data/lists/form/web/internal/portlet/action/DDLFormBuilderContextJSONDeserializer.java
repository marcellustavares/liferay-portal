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

package com.liferay.dynamic.data.lists.form.web.internal.portlet.action;

import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
public class DDLFormBuilderContextJSONDeserializer {

	public DDLFormBuilderContextJSONDeserializer(
		String serializedContext, JSONFactory jsonFactory,
		DDMFormLayoutJSONDeserializer ddmFormLayoutJSONDeserializer,
		Locale locale) {

		_serializedContext = serializedContext;
		_jsonFactory = jsonFactory;
		_ddmFormLayoutJSONDeserializer = ddmFormLayoutJSONDeserializer;
		_locale = locale;
	}

	public Tuple deserialize() throws PortalException {
		_ddmForm = new DDMForm();

		Set<Locale> availableLocales = new HashSet<>();
		availableLocales.add(_locale);

		_ddmForm.setAvailableLocales(availableLocales);
		_ddmForm.setDefaultLocale(_locale);

		_ddmFormLayout = new DDMFormLayout();

		_ddmFormLayout.setDefaultLocale(_locale);

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			_serializedContext);

		_ddmFormLayout.setPaginationMode(
			jsonObject.getString("paginationMode"));

		setDDMFormLayoutPages(jsonObject.getJSONArray("pages"), _ddmFormLayout);

		return new Tuple(_ddmForm, _ddmFormLayout);
	}

	protected void addDDMFormField(JSONObject settingsJSONObject) {
		DDMFormField ddmFormField = new DDMFormField();

		Consumer<JSONObject> fieldConsumer = new Consumer<JSONObject>() {

			@Override
			public void accept(JSONObject jsonObject) {
				try {
					Object deserializedDDMFormFieldProperty =
						deserializeDDMFormFieldProperty(
							jsonObject.getString("value"),
							jsonObject.getBoolean("localizable"),
							jsonObject.getString("dataType"),
							jsonObject.getString("type"));

					ddmFormField.setProperty(
						jsonObject.getString("fieldName"),
						deserializedDDMFormFieldProperty);
				}
				catch (PortalException pe) {
					pe.printStackTrace();
				}
			}

		};

		visitFields(settingsJSONObject.getJSONArray("pages"), fieldConsumer);

		_ddmForm.addDDMFormField(ddmFormField);
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

	protected DDMFormFieldOptions deserializeDDMFormFieldOptions(
			String serializedDDMFormFieldProperty)
		throws PortalException {

		if (Validator.isNull(serializedDDMFormFieldProperty)) {
			return new DDMFormFieldOptions();
		}

		JSONArray jsonArray = _jsonFactory.createJSONArray(
			serializedDDMFormFieldProperty);

		return getDDMFormFieldOptions(jsonArray);
	}

	protected Object deserializeDDMFormFieldProperty(
			String serializedDDMFormFieldProperty, boolean localizable,
			String dataType, String type)
		throws PortalException {

		if (localizable) {
			return deserializeLocalizedValue(serializedDDMFormFieldProperty);
		}

		if (Objects.equals(dataType, "boolean")) {
			return Boolean.valueOf(serializedDDMFormFieldProperty);
		}
		else if (Objects.equals(dataType, "ddm-options")) {
			return deserializeDDMFormFieldOptions(
				serializedDDMFormFieldProperty);
		}
		else if (Objects.equals(type, "validation")) {
			return deserializeDDMFormFieldValidation(
				serializedDDMFormFieldProperty);
		}
		else {
			return serializedDDMFormFieldProperty;
		}
	}

	protected DDMFormFieldValidation deserializeDDMFormFieldValidation(
			String serializedDDMFormFieldProperty)
		throws PortalException {

		DDMFormFieldValidation ddmFormFieldValidation =
			new DDMFormFieldValidation();

		if (Validator.isNull(serializedDDMFormFieldProperty)) {
			return ddmFormFieldValidation;
		}

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			serializedDDMFormFieldProperty);

		ddmFormFieldValidation.setExpression(
			jsonObject.getString("expression"));
		ddmFormFieldValidation.setErrorMessage(
			jsonObject.getString("errorMessage"));

		return ddmFormFieldValidation;
	}

	protected LocalizedValue deserializeLocalizedValue(
			String serializedDDMFormFieldProperty)
		throws PortalException {

		LocalizedValue localizedValue = new LocalizedValue(_locale);

		if (Validator.isNull(serializedDDMFormFieldProperty)) {
			return localizedValue;
		}

		localizedValue.addString(_locale, serializedDDMFormFieldProperty);

//		JSONObject jsonObject = _jsonFactory.createJSONObject(
//			serializedDDMFormFieldProperty);

//
//		Iterator<String> itr = jsonObject.keys();
//
//		while (itr.hasNext()) {
//			String languageId = itr.next();
//
//			localizedValue.addString(
//				LocaleUtil.fromLanguageId(languageId),
//				jsonObject.getString(languageId));
//		}

		return localizedValue;
	}

	protected DDMFormFieldOptions getDDMFormFieldOptions(JSONArray jsonArray) {
		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String value = jsonObject.getString("value");

			ddmFormFieldOptions.addOption(value);

			addOptionValueLabels(
				jsonObject.getJSONObject("label"), ddmFormFieldOptions, value);
		}

		return ddmFormFieldOptions;
	}

	protected DDMFormLayoutColumn getDDMFormLayoutColumn(
		JSONObject jsonObject) {

		int size = jsonObject.getInt("size");
		JSONArray jsonArray = jsonObject.getJSONArray("fields");

		List<String> fieldNames = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject fieldJSONObject = jsonArray.getJSONObject(i);

			addDDMFormField(fieldJSONObject.getJSONObject("settingsContext"));
			fieldNames.add(fieldJSONObject.getString("fieldName"));
		}

		return new DDMFormLayoutColumn(
			size, fieldNames.toArray(new String[fieldNames.size()]));
	}

	protected List<DDMFormLayoutColumn> getDDMFormLayoutColumns(
		JSONArray jsonArray) {

		List<DDMFormLayoutColumn> ddmFormLayoutColumns = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			DDMFormLayoutColumn ddmFormLayoutColumn = getDDMFormLayoutColumn(
				jsonArray.getJSONObject(i));

			ddmFormLayoutColumns.add(ddmFormLayoutColumn);
		}

		return ddmFormLayoutColumns;
	}

	protected DDMFormLayoutPage getDDMFormLayoutPage(JSONObject jsonObject) {
		DDMFormLayoutPage ddmFormLayoutPage = new DDMFormLayoutPage();

		setDDMFormLayoutPageDescription(
			jsonObject.getString("description"), ddmFormLayoutPage);
		setDDMFormLayoutPageRows(
			jsonObject.getJSONArray("rows"), ddmFormLayoutPage);
		setDDMFormLayoutPageTitle(
			jsonObject.getString("title"), ddmFormLayoutPage);

		return ddmFormLayoutPage;
	}

	protected List<DDMFormLayoutPage> getDDMFormLayoutPages(
		JSONArray jsonArray) {

		List<DDMFormLayoutPage> ddmFormLayoutPages = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			DDMFormLayoutPage ddmFormLayoutPage = getDDMFormLayoutPage(
				jsonArray.getJSONObject(i));

			ddmFormLayoutPages.add(ddmFormLayoutPage);
		}

		return ddmFormLayoutPages;
	}

	protected DDMFormLayoutRow getDDMFormLayoutRow(JSONObject jsonObject) {
		DDMFormLayoutRow ddmFormLayoutRow = new DDMFormLayoutRow();

		setDDMFormLayoutRowColumns(
			jsonObject.getJSONArray("columns"), ddmFormLayoutRow);

		return ddmFormLayoutRow;
	}

	protected List<DDMFormLayoutRow> getDDMFormLayoutRows(JSONArray jsonArray) {
		List<DDMFormLayoutRow> ddmFormLayoutRows = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			DDMFormLayoutRow ddmFormLayoutRow = getDDMFormLayoutRow(
				jsonArray.getJSONObject(i));

			ddmFormLayoutRows.add(ddmFormLayoutRow);
		}

		return ddmFormLayoutRows;
	}

	protected void setDDMFormLayoutPageDescription(
		String description, DDMFormLayoutPage ddmFormLayoutPage) {

		if (description == null) {
			return;
		}

		LocalizedValue localizedValue = new LocalizedValue();

		localizedValue.addString(_locale, description);

		ddmFormLayoutPage.setDescription(localizedValue);
	}

	protected void setDDMFormLayoutPageRows(
		JSONArray jsonArray, DDMFormLayoutPage ddmFormLayoutPage) {

		List<DDMFormLayoutRow> ddmFormLayoutRows = getDDMFormLayoutRows(
			jsonArray);

		ddmFormLayoutPage.setDDMFormLayoutRows(ddmFormLayoutRows);
	}

	protected void setDDMFormLayoutPages(
		JSONArray jsonArray, DDMFormLayout ddmFormLayout) {

		List<DDMFormLayoutPage> ddmFormLayoutPages = getDDMFormLayoutPages(
			jsonArray);

		ddmFormLayout.setDDMFormLayoutPages(ddmFormLayoutPages);
	}

	protected void setDDMFormLayoutPageTitle(
		String title, DDMFormLayoutPage ddmFormLayoutPage) {

		if (title == null) {
			return;
		}

		LocalizedValue localizedValue = new LocalizedValue();

		localizedValue.addString(_locale, title);

		ddmFormLayoutPage.setTitle(localizedValue);
	}

	protected void setDDMFormLayoutRowColumns(
		JSONArray jsonArray, DDMFormLayoutRow ddmFormLayoutRow) {

		List<DDMFormLayoutColumn> ddmFormLayoutColumns =
			getDDMFormLayoutColumns(jsonArray);

		ddmFormLayoutRow.setDDMFormLayoutColumns(ddmFormLayoutColumns);
	}

	protected void visitFields(
		JSONArray pagesJSONArray, Consumer<JSONObject> fieldConsumer) {

		for (int i = 0; i < pagesJSONArray.length(); i++) {
			JSONObject pageJSObject = pagesJSONArray.getJSONObject(i);

			JSONArray rowsJSONArray = pageJSObject.getJSONArray("rows");

			for (int j = 0; j < rowsJSONArray.length(); j++) {
				JSONObject rowJSObject = rowsJSONArray.getJSONObject(j);

				JSONArray columnsJSONArray = rowJSObject.getJSONArray(
					"columns");

				for (int k = 0; k < columnsJSONArray.length(); k++) {
					JSONObject columnJSObject = columnsJSONArray.getJSONObject(
						k);

					JSONArray fieldsJSONArray = columnJSObject.getJSONArray(
						"fields");

					for (int l = 0; l < fieldsJSONArray.length(); l++) {
						JSONObject fieldJSONObject =
							fieldsJSONArray.getJSONObject(l);

						fieldConsumer.accept(fieldJSONObject);
					}
				}
			}
		}
	}

	private DDMForm _ddmForm;
	private DDMFormLayout _ddmFormLayout;
	private final DDMFormLayoutJSONDeserializer _ddmFormLayoutJSONDeserializer;
	private final JSONFactory _jsonFactory;
	private final Locale _locale;
	private final String _serializedContext;

}