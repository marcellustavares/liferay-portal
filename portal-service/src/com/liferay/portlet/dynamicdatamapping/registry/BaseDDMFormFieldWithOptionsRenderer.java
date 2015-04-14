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

package com.liferay.portlet.dynamicdatamapping.registry;

import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Bruno Basto
 */
public abstract class BaseDDMFormFieldWithOptionsRenderer
	extends BaseDDMFormFieldRenderer {

	protected abstract String getActiveOptionText();

	protected String getOptionStatus(
		DDMFormField ddmFormField, String optionValue, Locale locale) {

		String status = StringPool.BLANK;

		LocalizedValue predefinedValue = ddmFormField.getPredefinedValue();

		if (isOptionSelected(optionValue, predefinedValue.getString(locale))) {
			status = getActiveOptionText();
		}

		return status;
	}

	protected boolean isOptionSelected(String value, String predefinedValue) {
		boolean selected = false;

		if (Validator.isNotNull(value)) {
			if (value.equals("true")) {
				selected = true;
			}
		}
		else if (predefinedValue.equals("true")) {
			selected = true;
		}

		return selected;
	}

	protected void populateOptions(
		Template template, DDMFormField ddmFormField, Locale locale,
		String fieldName) {

		List<Map<String, String>> optionsList = new ArrayList<>();

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormField.getDDMFormFieldOptions();

		for (String optionValue : ddmFormFieldOptions.getOptionsValues()) {
			Map<String, String> optionMap = new HashMap<>();

			LocalizedValue optionLabel = ddmFormFieldOptions.getOptionLabels(
				optionValue);

			String optionId =
				fieldName + StringPool.UNDERLINE + optionsList.size();

			optionMap.put("id", optionId);
			optionMap.put("label", optionLabel.getString(locale));
			optionMap.put(
				"status", getOptionStatus(ddmFormField, optionValue, locale));
			optionMap.put("value", optionValue);

			optionsList.add(optionMap);
		}

		template.put("options", optionsList);
	}

	protected void populateRequiredContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		super.populateRequiredContext(
			template, ddmFormField, ddmFormFieldRenderingContext);

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		populateOptions(
			template, ddmFormField, locale,
			ddmFormFieldRenderingContext.getName());
	}

}