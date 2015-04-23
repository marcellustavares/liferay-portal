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

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Bruno Basto
 */
public class DDMFormTransformer {

	public DDMFormTransformer(DDMForm ddmForm) {
		_ddmForm = ddmForm;
	}

	public Map<String, Object> getForm() {
		Map<String, Object> form = new HashMap<>();

		form.put("fields", getFields(_ddmForm.getDDMFormFields()));

		return form;
	}

	protected Map<String, Object> getField(DDMFormField ddmFormField) {
		Map<String, Object> field = new HashMap<>();

		field.put("label", getLocalizedValue(ddmFormField.getLabel()));
		field.put("name", ddmFormField.getName());
		field.put("required", ddmFormField.isRequired());

		return field;
	}

	protected List<Object> getFields(List<DDMFormField> ddmFormFields) {
		List<Object> fields = new ArrayList<>();

		for (DDMFormField ddmFormField : ddmFormFields) {
			fields.add(getField(ddmFormField));
		}

		return fields;
	}

	protected Map<String, Object> getLocalizedValue(
		LocalizedValue localizedValue) {

		Map<String, Object> value = new HashMap<>();

		for (Locale locale : localizedValue.getAvailableLocales()) {
			value.put(
				LocaleUtil.toLanguageId(locale),
				localizedValue.getString(locale));
		}

		return value;
	}

	private final DDMForm _ddmForm;

}