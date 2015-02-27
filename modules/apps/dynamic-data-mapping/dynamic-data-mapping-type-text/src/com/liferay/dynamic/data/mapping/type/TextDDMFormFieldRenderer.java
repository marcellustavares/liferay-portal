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

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.registry.BaseDDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, property = {"templatePath=/META-INF/resources/text.soy"},
	service = {
		TextDDMFormFieldRenderer.class, DDMFormFieldRenderer.class
	}
)
public class TextDDMFormFieldRenderer extends BaseDDMFormFieldRenderer {

	@Activate
	protected void activate(Map<String, Object> properties) {
		String templatePath = MapUtil.getString(properties, "templatePath");

		TemplateResource templateResource = getTemplateResource(templatePath);

		this.templateResource = templateResource;

		setTemplatesNamespaces();
	}

	protected void populateRequiredContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		LocalizedValue label = ddmFormField.getLabel();

		String fieldName = ddmFormField.getName();

		String instanceId = StringUtil.randomString();

		String fieldNameSuffix = getFieldNameSuffix(instanceId);

		LocalizedValue predefinedValue = ddmFormField.getPredefinedValue();

		String fieldQualifiedName = getFieldQualifiedName(
			fieldName, instanceId);

		template.put("dir", LanguageUtil.get(locale, "lang.dir"));
		template.put("fieldLabel", label.getString(locale));
		template.put("fieldName", fieldName);
		template.put("fieldNameSuffix", fieldNameSuffix);
		template.put("fieldValue", predefinedValue.getString(locale));
		template.put("fieldQualifiedName", fieldQualifiedName);
	}

	protected void setTemplatesNamespaces() {
		this.templatesNamespaces = new HashMap<>();

		this.templatesNamespaces.put("text", "ddm.text");
	}

}