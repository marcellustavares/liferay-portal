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

package com.liferay.dynamic.data.mapping.type.options.internal;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class OptionsDDMFormFieldContextHelper {

	private DDMForm _ddmForm;
	public OptionsDDMFormFieldContextHelper(
		JSONFactory jsonFactory, DDMFormField ddmFormField, String value) {

		_ddmForm = ddmFormField.getDDMForm();
		_jsonFactory = jsonFactory;
		_value = value;
	}

	protected Map<String, Object> getValue() {
		Map<String, Object> localizedValue = new HashMap<>();

		if (Validator.isNull(_value)) {
			List<Object> list = new ArrayList<>();

			Map<String, String> map = new HashMap<String, String>();

			map.put("value", "Option");
			map.put("label", "Option");

			list.add(map);

			localizedValue.put(LocaleUtil.toLanguageId(_ddmForm.getDefaultLocale()), list);

			return localizedValue;
		}

		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(_value);

			Iterator<String> itr = jsonObject.keys();

			while(itr.hasNext()) {
				String languageId = itr.next();

				JSONArray jsonArray = jsonObject.getJSONArray(languageId);

				List<Object> list = new ArrayList<>();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);

					Map<String, String> optionMap = new HashMap<>();

					optionMap.put("label", obj.getString("label"));
					optionMap.put("value", obj.getString("value"));

					list.add(optionMap);
				}


				localizedValue.put(languageId, list);

			}

			return localizedValue;
		}
		catch (JSONException jsone) {
			_log.error("Unable to parse JSON array", jsone);

			return localizedValue;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OptionsDDMFormFieldContextHelper.class);

	private final JSONFactory _jsonFactory;
	private final String _value;

}