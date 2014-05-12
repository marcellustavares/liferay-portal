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
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portlet.dynamicdatamapping.forms.Form;
import com.liferay.portlet.dynamicdatamapping.forms.FormField;
import com.liferay.portlet.dynamicdatamapping.forms.FormPage;
import com.liferay.portlet.dynamicdatamapping.forms.FormSection;
import com.liferay.portlet.dynamicdatamapping.forms.LocalizedValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Pablo Carvalho
 */
public class JSONToFormConverter {
	
	public JSONToFormConverter(String layoutJSON, String structureJSON)
		throws JSONException  {
		
		_layoutRoot = JSONFactoryUtil.createJSONObject(layoutJSON);
		
		_fieldsLayoutRoot = _layoutRoot.getJSONObject("fieldsLayout");

		_structureRoot = JSONFactoryUtil.createJSONObject(structureJSON);
	}

	public Form convert() {
		Form form = new Form();
		
		JSONArray pagesArray = _layoutRoot.getJSONArray("pages");
		
		form.setPages(getPages(pagesArray));
		
		return form;
	}

	protected List<FormPage> getPages(JSONArray pagesArray) {
		List<FormPage> pages = new ArrayList<FormPage>();
		
		for (int i = 0; i < pagesArray.length(); ++i) {
			pages.add(getPage(pagesArray.getJSONObject(i)));
		}
		
		return pages;
	}

	protected FormPage getPage(JSONObject pageNode) {
		FormPage page = new FormPage();
		
		JSONArray sectionsArray = pageNode.getJSONArray("sections");
		
		page.setSections(getSections(sectionsArray));
		
		return page;
	}

	protected List<FormSection> getSections(JSONArray sectionsArray) {
		List<FormSection> sections = new ArrayList<FormSection>();
	
		for (int i = 0; i < sectionsArray.length(); ++i) {
			sections.add(getSection(sectionsArray.getJSONObject(i)));
		}
		
		return sections;
	}

	protected FormSection getSection(JSONObject sectionNode) {
		FormSection section = new FormSection();
		
		JSONArray fieldsArray = sectionNode.getJSONArray("fields");
		
		section.setFields(getFields(fieldsArray));
		
		return section;
	}

	protected List<FormField> getFields(JSONArray fieldsArray) {
		List<FormField> fields = new ArrayList<FormField>();
		
		for (int i = 0; i < fieldsArray.length(); ++i) {
			fields.add(getField(fieldsArray.getString(i)));
		}
		
		return fields;
	}

	protected FormField getField(String fieldName) {
		FormField formField = new FormField();

		JSONObject fieldLayout = _fieldsLayoutRoot.getJSONObject(fieldName);
		
		formField.setLabel(fieldLayout.getString("label"));
		formField.setName(fieldName);
		formField.setPredefinedValue(
			getLocalizedString(fieldLayout.getJSONObject("predefinedValue")));
		formField.setStyle(
			getLocalizedString(fieldLayout.getJSONObject("style")));
		formField.setTip(
			getLocalizedString(fieldLayout.getJSONObject("tip")));
		formField.setType(fieldLayout.getString("type"));
		formField.setVisibilityExpression(fieldLayout.getString("visibility"));

		JSONObject fieldStructure = _structureRoot.getJSONObject(fieldName);
		
		formField.setDataType(fieldStructure.getString("dataType"));
		formField.setIndexType(fieldStructure.getString("indexType"));
		formField.setMultiple(fieldStructure.getBoolean("multiple"));
		formField.setNestedFields(
			getFields(fieldStructure.getJSONArray("nestedFields")));
		formField.setRepeatable(fieldStructure.getBoolean("repeatable"));
		formField.setValidationExpression(
			fieldStructure.getString("validation"));

		return formField;
	}

	protected LocalizedValue getLocalizedString (JSONObject localizedNode) {
		LocalizedValue value = new LocalizedValue();
		
		Iterator<String> keys = localizedNode.keys();
		
		while (keys.hasNext()) {
			String language = keys.next();
		
			value.addValue(language, localizedNode.getString(language));
		}
		
		return value;
	}

	private JSONObject _layoutRoot;
	private JSONObject _fieldsLayoutRoot;
	private JSONObject _structureRoot;
}
