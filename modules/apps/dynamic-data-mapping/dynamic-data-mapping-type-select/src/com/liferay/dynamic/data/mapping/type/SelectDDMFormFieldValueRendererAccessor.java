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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueAccessor;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueRendererAccessor;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

import java.util.Map;

/**
 * @author Renato Rego
 */
public class SelectDDMFormFieldValueRendererAccessor
	extends DDMFormFieldValueRendererAccessor {

	public SelectDDMFormFieldValueRendererAccessor(
		DDMFormFieldValueAccessor<JSONArray> ddmFormFieldValueAccessor) {

		_ddmFormFieldValueAccessor = ddmFormFieldValueAccessor;
	}

	@Override
	public String get(DDMFormFieldValue ddmFormFieldValue) {
		try {
			JSONArray optionsValues = _ddmFormFieldValueAccessor.get(
				ddmFormFieldValue);

			Map<String, DDMFormField> ddmFormFieldsMap =
				ddmFormFieldValue.getDDMFormValues().getDDMForm().
				getDDMFormFieldsMap(false);

			DDMFormField ddmFormField = ddmFormFieldsMap.get(
				ddmFormFieldValue.getName());

			DDMFormFieldOptions ddmFormFieldOptions =
				ddmFormField.getDDMFormFieldOptions();

			StringBundler sb = new StringBundler();

			for (int i = 0; i < optionsValues.length(); i++) {
				if (i > 0) {
					sb.append(StringPool.COMMA_AND_SPACE);
				}

				LocalizedValue optionLabel =
					ddmFormFieldOptions.getOptionLabels(
						optionsValues.getString(i));

				sb.append(
					optionLabel.getString(
						_ddmFormFieldValueAccessor.getLocale()));
			}

			return sb.toString();
		}
		catch (Exception e) {
			_log.error(e, e);

			return StringPool.BLANK;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SelectDDMFormFieldValueRendererAccessor.class);

	private final DDMFormFieldValueAccessor<JSONArray>
		_ddmFormFieldValueAccessor;

}