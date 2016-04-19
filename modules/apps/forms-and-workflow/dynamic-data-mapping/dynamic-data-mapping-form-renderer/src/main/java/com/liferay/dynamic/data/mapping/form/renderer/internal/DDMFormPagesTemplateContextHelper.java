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

package com.liferay.dynamic.data.mapping.form.renderer.internal;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class DDMFormPagesTemplateContextHelper {

	public DDMFormPagesTemplateContextHelper(
		DDMForm ddmForm, DDMFormLayout ddmFormLayout,
		Map<String, JSONArray> ddmFormFieldsTemplateContextMap,
		boolean showRequiredFieldsWarning, Locale locale,
		JSONFactory jsonFactory) {

		_ddmFormLayout = ddmFormLayout;
		_ddmFormFieldsTemplateContextMap = ddmFormFieldsTemplateContextMap;
		_showRequiredFieldsWarning = showRequiredFieldsWarning;
		_locale = locale;
		_jsonFactory = jsonFactory;

		_ddmFormFieldsMap = ddmForm.getDDMFormFieldsMap(true);
	}

	public JSONArray getPagesTemplateContext() {
		return getPages(_ddmFormLayout.getDDMFormLayoutPages());
	}

	protected boolean containsRequiredField(List<String> ddmFormFieldNames) {
		for (String ddmFormFieldName : ddmFormFieldNames) {
			DDMFormField ddmFormField = _ddmFormFieldsMap.get(ddmFormFieldName);

			if (ddmFormField.isRequired()) {
				return true;
			}
		}

		return false;
	}

	protected JSONObject getColumn(DDMFormLayoutColumn ddmFormLayoutColumn) {
		JSONObject column = _jsonFactory.createJSONObject();

		column.put(
			"fields", getFields(ddmFormLayoutColumn.getDDMFormFieldNames()));
		column.put("size", ddmFormLayoutColumn.getSize());

		return column;
	}

	protected JSONArray getColumns(
		List<DDMFormLayoutColumn> ddmFormLayoutColumns) {

		JSONArray columns = _jsonFactory.createJSONArray();

		for (DDMFormLayoutColumn ddmFormLayoutColumn : ddmFormLayoutColumns) {
			columns.put(getColumn(ddmFormLayoutColumn));
		}

		return columns;
	}

	protected List<Object> getField(String ddmFormFieldName) {
		List<Object> ddmFormFieldTemplateContext = new ArrayList<>();

		JSONArray jsonArray = _ddmFormFieldsTemplateContextMap.get(
			ddmFormFieldName);

		for (int i = 0; i < jsonArray.length(); i++) {
			ddmFormFieldTemplateContext.add(
				getFieldContext(jsonArray.getJSONObject(i)));
		}

		return ddmFormFieldTemplateContext;
	}

	protected Map<String, Object> getFieldContext(JSONObject jsonObject) {
		Map<String, Object> context = new HashMap<>();

		Iterator<String> keys = jsonObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();

			context.put(key, jsonObject.get(key));
		}

		return context;
	}

	protected JSONArray getFields(List<String> ddmFormFieldNames) {
		JSONArray fields = _jsonFactory.createJSONArray();

		for (String ddmFormFieldName : ddmFormFieldNames) {
			fields.put(_ddmFormFieldsTemplateContextMap.get(ddmFormFieldName));
		}

		return fields;
	}

	protected JSONObject getPage(DDMFormLayoutPage ddmFormLayoutPage) {
		JSONObject jsonObject = _jsonFactory.createJSONObject();

		LocalizedValue description = ddmFormLayoutPage.getDescription();

		jsonObject.put("description", description.getString(_locale));

		jsonObject.put(
			"rows", getRows(ddmFormLayoutPage.getDDMFormLayoutRows()));

		boolean showRequiredFieldsWarning = isShowRequiredFieldsWarning(
			ddmFormLayoutPage.getDDMFormLayoutRows());

		jsonObject.put("showRequiredFieldsWarning", showRequiredFieldsWarning);

		LocalizedValue title = ddmFormLayoutPage.getTitle();

		jsonObject.put("title", title.getString(_locale));

		return jsonObject;
	}

	protected JSONArray getPages(List<DDMFormLayoutPage> ddmFormLayoutPages) {
		JSONArray pages = _jsonFactory.createJSONArray();

		for (DDMFormLayoutPage ddmFormLayoutPage : ddmFormLayoutPages) {
			pages.put(getPage(ddmFormLayoutPage));
		}

		return pages;
	}

	protected JSONObject getRow(DDMFormLayoutRow ddFormLayoutRow) {
		JSONObject row = _jsonFactory.createJSONObject();

		row.put(
			"columns", getColumns(ddFormLayoutRow.getDDMFormLayoutColumns()));

		return row;
	}

	protected JSONArray getRows(List<DDMFormLayoutRow> ddmFormLayoutRows) {
		JSONArray rows = _jsonFactory.createJSONArray();

		for (DDMFormLayoutRow ddmFormLayoutRow : ddmFormLayoutRows) {
			rows.put(getRow(ddmFormLayoutRow));
		}

		return rows;
	}

	protected boolean isShowRequiredFieldsWarning(
		List<DDMFormLayoutRow> ddmFormLayoutRows) {

		if (!_showRequiredFieldsWarning) {
			return false;
		}

		for (DDMFormLayoutRow ddmFormLayoutRow : ddmFormLayoutRows) {
			for (DDMFormLayoutColumn ddmFormLayoutColumn :
					ddmFormLayoutRow.getDDMFormLayoutColumns()) {

				if (containsRequiredField(
						ddmFormLayoutColumn.getDDMFormFieldNames())) {

					return true;
				}
			}
		}

		return false;
	}

	private final Map<String, DDMFormField> _ddmFormFieldsMap;
	private final Map<String, JSONArray> _ddmFormFieldsTemplateContextMap;
	private final DDMFormLayout _ddmFormLayout;
	private final JSONFactory _jsonFactory;
	private final Locale _locale;
	private final boolean _showRequiredFieldsWarning;

}