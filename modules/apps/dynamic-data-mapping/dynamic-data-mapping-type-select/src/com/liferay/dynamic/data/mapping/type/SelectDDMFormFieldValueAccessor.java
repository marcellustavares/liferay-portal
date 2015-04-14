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

package com.liferay.dynamic.data.mapping.type;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueAccessor;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

import java.util.Locale;

/**
 * @author Renato Rego
 */
public class SelectDDMFormFieldValueAccessor
	extends DDMFormFieldValueAccessor<JSONArray> {

	public SelectDDMFormFieldValueAccessor(Locale locale) {
		super(locale);
	}

	@Override
	public JSONArray get(DDMFormFieldValue ddmFormFieldValue) {
		try {
			Value value = ddmFormFieldValue.getValue();

			JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
				value.getString(locale));

			return jsonArray;
		}
		catch (Exception e) {
			_log.error(e, e);

			return JSONFactoryUtil.createJSONArray();
		}
	}

	@Override
	public Class<JSONArray> getAttributeClass() {
		return JSONArray.class;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SelectDDMFormFieldValueAccessor.class);

}