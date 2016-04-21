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

package com.liferay.dynamic.data.mapping.type.select;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldRenderingContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, property = "ddm.form.field.type.name=select")
public class SelectDDMFormFieldRenderingContextContributor
	implements DDMFormFieldRenderingContextContributor {

	@Override
	public void addAttributes(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> attributes =
			ddmFormFieldRenderingContext.getAttributes();

		attributes.put(
			"multiple",
			ddmFormField.isMultiple() ? "multiple" : StringPool.BLANK);
		attributes.put(
			"options", getOptions(ddmFormField, ddmFormFieldRenderingContext));

		Map<String, String> stringsMap = new HashMap<>();

		stringsMap.put(
			"chooseAnOption",
			LanguageUtil.get(
				ddmFormFieldRenderingContext.getLocale(), "choose-an-option"));

		attributes.put("strings", stringsMap);
		attributes.put("value", ddmFormFieldRenderingContext.getValue());
	}

	protected DDMFormFieldOptions getDDMFormFieldOptions(
		DDMFormField ddmFormField) {

		String dataSourceType = GetterUtil.getString(
			ddmFormField.getProperty("dataSourceType"), "manual");

		if (Objects.equals(dataSourceType, "manual")) {
			return ddmFormField.getDDMFormFieldOptions();
		}

		return new DDMFormFieldOptions();
	}

	protected List<Object> getOptions(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		SelectDDMFormFieldContextHelper selectDDMFormFieldContextHelper =
			new SelectDDMFormFieldContextHelper(
				jsonFactory, getDDMFormFieldOptions(ddmFormField),
				ddmFormFieldRenderingContext.getValue(),
				ddmFormField.getPredefinedValue(),
				ddmFormFieldRenderingContext.getLocale());

		return selectDDMFormFieldContextHelper.getOptions();
	}

	@Reference
	protected JSONFactory jsonFactory;

}