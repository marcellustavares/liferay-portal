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

package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portlet.dynamicdatamapping.forms.Form;
import com.liferay.portlet.dynamicdatamapping.forms.FormField;
import com.liferay.portlet.dynamicdatamapping.forms.FormPage;
import com.liferay.portlet.dynamicdatamapping.forms.FormSection;
import com.liferay.portlet.dynamicdatamapping.forms.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.forms.SectionLayout;

import java.util.List;
import java.util.Locale;

/**
 * @author Pablo Carvalho
 */
public class FormToJSONConverter {

	public FormMetadataJSON convert(Form layout) {
		JSONObject formLayout = JSONFactoryUtil.createJSONObject();

		_flatFieldsLayout = JSONFactoryUtil.createJSONObject();
		_flatFieldsStructure = JSONFactoryUtil.createJSONObject();

		formLayout.put("pages", convertFormPages(layout.getPages()));
		formLayout.put("fieldsLayout", _flatFieldsLayout);

		FormMetadataJSON metadataJSON = new FormMetadataJSON(
			formLayout.toString(), _flatFieldsStructure.toString());

		return metadataJSON;
	}

	protected void addFieldToFlatFieldsLayout(FormField field) {
		JSONObject fieldJSON = JSONFactoryUtil.createJSONObject();

		fieldJSON.put("label", convertLocalizedValue(field.getLabel()));
		fieldJSON.put(
			"predefinedValue",
			convertLocalizedValue(field.getPredefinedValue()));
		fieldJSON.put("style", convertLocalizedValue(field.getStyle()));
		fieldJSON.put("type", field.getType());
		fieldJSON.put("visibility", field.getVisibilityExpression());

		_flatFieldsLayout.put(field.getName(), fieldJSON);
	}

	protected void addFieldToFlatFieldsStructure(FormField field) {
		JSONObject fieldJSON = JSONFactoryUtil.createJSONObject();

		fieldJSON.put(
			"calculatedValueExpression",
			convertLocalizedValue(field.getCalculatedValueExpression()));
		fieldJSON.put("dataType", field.getDataType());
		fieldJSON.put("indexType", field.getIndexType());
		fieldJSON.put("multiple", field.isMultiple());
		fieldJSON.put(
			"nestedFields", convertFormFields(field.getNestedFields()));
		fieldJSON.put("repeatable", field.isRepeatable());
		fieldJSON.put("validation", field.getValidationExpression());

		_flatFieldsStructure.put(field.getName(), fieldJSON);
	}

	protected void addFieldToFlatRepresentations(FormField field) {
		addFieldToFlatFieldsLayout(field);
		addFieldToFlatFieldsStructure(field);
	}

	protected JSONArray convertFormFields(List<FormField> list) {
		JSONArray array = JSONFactoryUtil.createJSONArray();

		for (FormField item : list) {
			array.put(item.getName());
			addFieldToFlatRepresentations(item);
		}

		return array;
	}

	protected JSONObject convertFormPage(FormPage page) {
		JSONObject pageJSON = JSONFactoryUtil.createJSONObject();
		pageJSON.put("sections", convertFormSections(page.getSections()));
		return pageJSON;
	}

	protected JSONArray convertFormPages(List<FormPage> list) {
		JSONArray array = JSONFactoryUtil.createJSONArray();

		for (FormPage item : list) {
			array.put(convertFormPage(item));
		}

		return array;
	}

	protected JSONObject convertFormSection(FormSection section) {
		JSONObject sectionJSON = JSONFactoryUtil.createJSONObject();
		sectionJSON.put("layout", convertSectionLayout(section.getLayout()));
		sectionJSON.put("fields", convertFormFields(section.getFields()));
		return sectionJSON;
	}

	protected JSONArray convertFormSections(List<FormSection> list) {
		JSONArray array = JSONFactoryUtil.createJSONArray();

		for (FormSection item : list) {
			array.put(convertFormSection(item));
		}

		return array;
	}

	protected JSONObject convertLocalizedValue(LocalizedValue value) {
		JSONObject valueJSON = JSONFactoryUtil.createJSONObject();

		for (Locale locale : value.getAvailableLocales()) {
			valueJSON.put(locale.getLanguage(), value.getValue(locale));
		}

		return valueJSON;
	}

	protected JSONObject convertSectionLayout(SectionLayout sectionLayout) {
		JSONObject layoutJSON = JSONFactoryUtil.createJSONObject();
		return layoutJSON;
	}

	private JSONObject _flatFieldsLayout;
	private JSONObject _flatFieldsStructure;

}