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

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=options",
	service = {
		OptionsDDMFormFieldTemplateContextContributor.class,
		DDMFormFieldTemplateContextContributor.class
	}
)
public class OptionsDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put(
			"allowEmptyOptions",
			GetterUtil.getBoolean(
				ddmFormField.getProperty("allowEmptyOptions")));
		parameters.put(
			"value", getValue(ddmFormField, ddmFormFieldRenderingContext));

		parameters.put(
			"localizedValue",
			getLocalizedValue(ddmFormField.getDDMForm().getAvailableLocales(), ddmFormField.getDDMFormFieldOptions()));

		return parameters;
	}

	protected Map<String, Object> getLocalizedValue(
		Set<Locale> availableLocales, DDMFormFieldOptions ddmFormFieldOptions) {

		Map<String, Object> localizedValue = new HashMap<>();

		Set<String> optionValues = ddmFormFieldOptions.getOptionsValues();

		for (Locale locale : availableLocales) {
			Map<String, String> map = new HashMap<String, String>();

			for (String optionValue : optionValues) {
				map.put("value", optionValue);
				map.put("label", ddmFormFieldOptions.getOptionLabels(optionValue).getString(locale));
			}

			localizedValue.put(LocaleUtil.toLanguageId(locale), map);
		}


		return localizedValue;
	}


	protected List<Object> getValue(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		OptionsDDMFormFieldContextHelper optionsDDMFormFieldContextHelper =
			new OptionsDDMFormFieldContextHelper(
				jsonFactory, ddmFormFieldRenderingContext.getValue());

		return optionsDDMFormFieldContextHelper.getValue();
	}

	@Reference
	protected JSONFactory jsonFactory;

}