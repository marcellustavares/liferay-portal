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

import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.registry.BaseDDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

import java.net.URL;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Renato Rego
 */
@Component(
	immediate = true,
	property = {"templatePath=/META-INF/resources/checkbox.soy"},
	service = {
		CheckboxDDMFormFieldRenderer.class, DDMFormFieldRenderer.class
	}
)
public class CheckboxDDMFormFieldRenderer extends BaseDDMFormFieldRenderer {

	@Override
	public String getTemplateLanguage() {
		return TemplateConstants.LANG_TYPE_SOY;
	}

	@Override
	public String getTemplateNamespace() {
		return "ddm.checkbox";
	}

	@Override
	public TemplateResource getTemplateResource() {
		String templatePath = MapUtil.getString(_properties, "templatePath");

		return getTemplateResource(templatePath);
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_properties = properties;
	}

	protected String getCheckboxStatus(Value value, String predefinedValue) {
		String fieldStatus = StringPool.BLANK;

		if (Validator.isNotNull(value)) {
			if (value.equals("true")) {
				fieldStatus = "checked";
			}
		}
		else if (predefinedValue.equals("true")) {
			fieldStatus = "checked";
		}

		return fieldStatus;
	}

	protected TemplateResource getTemplateResource(String templatePath) {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		URL templateURL = classLoader.getResource(templatePath);

		return new URLTemplateResource(templateURL.getPath(), templateURL);
	}

	protected void populateRequiredContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		super.populateRequiredContext(
			template, ddmFormField, ddmFormFieldRenderingContext);

		DDMFormFieldValue ddmFormFieldValue =
			ddmFormFieldRenderingContext.getDDMFormFieldValue();

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		String predefinedValue =
			ddmFormField.getPredefinedValue().getString(locale);

		String status = getCheckboxStatus(
			ddmFormFieldValue.getValue(), predefinedValue);

		template.put("status", status);
	}

	private Map<String, Object> _properties;

}