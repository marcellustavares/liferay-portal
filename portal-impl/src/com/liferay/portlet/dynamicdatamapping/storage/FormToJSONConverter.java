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
import com.liferay.portlet.dynamicdatamapping.forms.SectionLayout;

import java.util.List;

/**
 * @author Pablo Carvalho
 */
public class FormToJSONConverter {
	public String convert(Form layout) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("pages", convert(layout.getPages()));

		return jsonObject.toString();
	}

	protected JSONObject convert(FormField field) {
		JSONObject fieldJSON = JSONFactoryUtil.createJSONObject();

		return fieldJSON;
	}

	protected JSONObject convert(FormPage page) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("sections", convert(page.getSections()));

		return jsonObject;
	}

	protected JSONObject convert(FormSection section) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("layout", convert(section.getLayout()));
		jsonObject.put("fields", convert(section.getFields()));

		return jsonObject;
	}

	protected <T> JSONArray convert(List<T> list) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (T item : list) {
			jsonArray.put(convert(item));
		}

		return jsonArray;
	}

	protected JSONObject convert(Object object) {
		throw new IllegalArgumentException(
			"Unrecognized type: " + object.getClass().getName());
	}

	protected JSONObject convert(SectionLayout sectionLayout) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		return jsonObject;
	}

}