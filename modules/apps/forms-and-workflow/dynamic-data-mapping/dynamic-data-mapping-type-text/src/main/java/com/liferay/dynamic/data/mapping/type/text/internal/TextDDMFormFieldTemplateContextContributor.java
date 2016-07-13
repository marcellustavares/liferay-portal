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

package com.liferay.dynamic.data.mapping.type.text.internal;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.util.ValueStringMapper;
import com.liferay.portal.kernel.util.StringPool;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=text",
	service = {
		TextDDMFormFieldTemplateContextContributor.class,
		DDMFormFieldTemplateContextContributor.class
	}
)
public class TextDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		Optional<String> displayStyleOptional =
			ddmFormField.getOptionalProperty("displayStyle");

		parameters.put(
			"displayStyle", displayStyleOptional.orElse("singleline"));

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		Optional<String> placeholderOptional = ValueStringMapper.apply(
			ddmFormField.<Value>getOptionalProperty("placeholder"), locale);

		parameters.put(
			"placeholder", placeholderOptional.orElse(StringPool.BLANK));

		Optional<String> tooltipOptional = ValueStringMapper.apply(
			ddmFormField.<Value>getOptionalProperty("tooltip"), locale);

		parameters.put("tooltip", tooltipOptional.orElse(StringPool.BLANK));

		return parameters;
	}

}