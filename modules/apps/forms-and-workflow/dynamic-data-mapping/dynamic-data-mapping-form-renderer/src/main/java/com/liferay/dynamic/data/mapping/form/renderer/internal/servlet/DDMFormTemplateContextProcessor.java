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

package com.liferay.dynamic.data.mapping.form.renderer.internal.servlet;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author Marcellus Tavares
 */
public class DDMFormTemplateContextProcessor {

	public DDMFormTemplateContextProcessor(
		JSONObject ddmFormTemplateContextJSONObject) {

		_ddmFormTemplateContextJSONObject = ddmFormTemplateContextJSONObject;

		_ddmForm = new DDMForm();
		_ddmFormLayout = new DDMFormLayout();
		_ddmFormValues = new DDMFormValues(_ddmForm);

		_ddmFormValues.setDefaultLocale(
			LocaleThreadLocal.getSiteDefaultLocale());
		_ddmFormValues.addAvailableLocale(
			LocaleThreadLocal.getSiteDefaultLocale());

		process();
	}

	public DDMForm getDDMForm() {
		return _ddmForm;
	}

	public DDMFormLayout getDDMFormLayout() {
		return _ddmFormLayout;
	}

	public DDMFormValues getDDMFormValues() {
		return _ddmFormValues;
	}

	protected DDMFormField createDDMFormField(JSONObject fieldJSONObject) {
		String fieldName = fieldJSONObject.getString("fieldName");
		String type = fieldJSONObject.getString("type");

		DDMFormField ddmFormField = new DDMFormField(fieldName, type);

		boolean localizable = fieldJSONObject.getBoolean("localizable");
		boolean repeatable = fieldJSONObject.getBoolean("repeatable");
		String visibilityExpression = fieldJSONObject.getString(
			"visibilityExpression", "");

		ddmFormField.setLocalizable(localizable);
		ddmFormField.setRepeatable(repeatable);
		ddmFormField.setVisibilityExpression(visibilityExpression);

		long ddmDataProviderInstanceId = fieldJSONObject.getLong(
			"ddmDataProviderInstanceId");
		String ddmDataProviderInstanceOutput = fieldJSONObject.getString(
			"ddmDataProviderInstanceOutput");

		ddmFormField.setProperty(
			"ddmDataProviderInstanceId", ddmDataProviderInstanceId);
		ddmFormField.setProperty(
			"ddmDataProviderInstanceOutput", ddmDataProviderInstanceOutput);

		JSONObject validationJSONObject = fieldJSONObject.getJSONObject(
			"validation");

		if (validationJSONObject != null) {
			DDMFormFieldValidation ddmFormFieldValidation =
				new DDMFormFieldValidation();

			ddmFormFieldValidation.setErrorMessage(
				validationJSONObject.getString("errorMessage"));
			ddmFormFieldValidation.setExpression(
				validationJSONObject.getString("expression"));

			ddmFormField.setDDMFormFieldValidation(ddmFormFieldValidation);
		}

		JSONArray optionsJSONArray = fieldJSONObject.getJSONArray("options");

		if (optionsJSONArray != null) {
			DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

			for (int j = 0; j < optionsJSONArray.length(); j++) {
				JSONObject jsonObject = optionsJSONArray.getJSONObject(j);

				String value = jsonObject.getString("value");

				Iterator<String> itr = jsonObject.keys();

				while (itr.hasNext()) {
					String languageId = itr.next();

					ddmFormFieldOptions.addOptionLabel(
						value, LocaleUtil.fromLanguageId(languageId),
						jsonObject.getString(languageId));
				}
			}

			ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);
		}

		return ddmFormField;
	}

	protected DDMFormFieldValue createDDMFormFieldValue(
		JSONObject fieldJSONObject, boolean localizable) {

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName(fieldJSONObject.getString("fieldName"));
		ddmFormFieldValue.setInstanceId(
			fieldJSONObject.getString("instanceId"));

		if (localizable) {
			LocalizedValue localizedValue = new LocalizedValue(
				LocaleThreadLocal.getSiteDefaultLocale());

			localizedValue.addString(
				LocaleThreadLocal.getSiteDefaultLocale(),
				fieldJSONObject.getString("value"));
		}
		else {
			ddmFormFieldValue.setValue(
				new UnlocalizedValue(fieldJSONObject.getString("value")));
		}

		JSONArray nestedFieldsJSONArray = fieldJSONObject.getJSONArray(
			"nestedFields");

		if (nestedFieldsJSONArray != null) {
			for (int i = 0; i < nestedFieldsJSONArray.length(); i++) {
				JSONObject nestedFieldJSONObject =
					nestedFieldsJSONArray.getJSONObject(i);

				ddmFormFieldValue.addNestedDDMFormFieldValue(
					createDDMFormFieldValue(
						nestedFieldJSONObject,
						nestedFieldJSONObject.getBoolean("localizable")));
			}
		}

		return ddmFormFieldValue;
	}

	protected void process() {
		JSONArray jsonArray = _ddmFormTemplateContextJSONObject.getJSONArray(
			"availableLanguageIds");

		for (int i = 0; i < jsonArray.length(); i++) {
			_ddmForm.addAvailableLocale(
				LocaleUtil.fromLanguageId(jsonArray.getString(i)));
		}

		Locale defaultLocale = LocaleUtil.fromLanguageId(
			_ddmFormTemplateContextJSONObject.getString("defaultLanguageId"));

		_ddmForm.setDefaultLocale(defaultLocale);

		traversePages(_ddmFormTemplateContextJSONObject.getJSONArray("pages"));
	}

	protected void traverseColumns(
		JSONArray columnnsJSONArray, DDMFormLayoutRow ddmFormLayoutRow) {

		for (int i = 0; i < columnnsJSONArray.length(); i++) {
			JSONObject columnJSONObject = columnnsJSONArray.getJSONObject(i);

			DDMFormLayoutColumn ddmFormLayoutColumn = new DDMFormLayoutColumn(
				columnJSONObject.getInt("size"));

			traverseFields(
				columnJSONObject.getJSONArray("fields"), ddmFormLayoutColumn);

			ddmFormLayoutRow.addDDMFormLayoutColumn(ddmFormLayoutColumn);
		}
	}

	protected void traverseFields(
		JSONArray fieldsJSONArray, DDMFormLayoutColumn ddmFormLayoutColumn) {

		List<String> ddmFormFieldNames = new ArrayList<>();

		for (int i = 0; i < fieldsJSONArray.length(); i++) {
			JSONObject fieldJSONObject = fieldsJSONArray.getJSONObject(i);

			String fieldName = fieldJSONObject.getString("fieldName");
			boolean localizable = fieldJSONObject.getBoolean("localizable");

			DDMFormField ddmFormField = createDDMFormField(fieldJSONObject);
			DDMFormFieldValue ddmFormFieldValue = createDDMFormFieldValue(
				fieldJSONObject, localizable);

			_ddmForm.addDDMFormField(ddmFormField);
			_ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

			ddmFormFieldNames.add(fieldName);
		}

		ddmFormLayoutColumn.setDDMFormFieldNames(ddmFormFieldNames);
	}

	protected void traversePages(JSONArray pagesJSONArray) {
		for (int i = 0; i < pagesJSONArray.length(); i++) {
			JSONObject pageJSONObject = pagesJSONArray.getJSONObject(i);

			DDMFormLayoutPage ddmFormLayoutPage = new DDMFormLayoutPage();

			traverseRows(
				pageJSONObject.getJSONArray("rows"), ddmFormLayoutPage);

			_ddmFormLayout.addDDMFormLayoutPage(ddmFormLayoutPage);
		}
	}

	protected void traverseRows(
		JSONArray rowsJSONArray, DDMFormLayoutPage ddmFormLayoutPage) {

		for (int i = 0; i < rowsJSONArray.length(); i++) {
			DDMFormLayoutRow ddmFormLayoutRow = new DDMFormLayoutRow();

			JSONObject rowJSONObject = rowsJSONArray.getJSONObject(i);

			traverseColumns(
				rowJSONObject.getJSONArray("columns"), ddmFormLayoutRow);

			ddmFormLayoutPage.addDDMFormLayoutRow(ddmFormLayoutRow);
		}
	}

	private final DDMForm _ddmForm;
	private final DDMFormLayout _ddmFormLayout;
	private final JSONObject _ddmFormTemplateContextJSONObject;
	private final DDMFormValues _ddmFormValues;

}