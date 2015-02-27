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
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.registry.BaseDDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Renato Rego
 */
@Component(
	immediate = true, property = {"templatePath=/META-INF/resources/choice.soy"},
	service = {
		ChoiceDDMFormFieldRenderer.class, DDMFormFieldRenderer.class
	}
)
public class ChoiceDDMFormFieldRenderer extends BaseDDMFormFieldRenderer {

	@Activate
	protected void activate(Map<String, Object> properties) {
		String templatePath = MapUtil.getString(properties, "templatePath");

		TemplateResource templateResource = getTemplateResource(templatePath);

		this.templateResource = templateResource;

		setTemplatesNamespaces();
	}

	protected void populateRadioAndSelectCommonContext(
		Template template, DDMFormField ddmFormField, Locale locale,
		String fieldQualifiedName) {

		List<String> fieldChoicesLabels = new ArrayList<>();
		List<String> fieldChoicesValues = new ArrayList<>();
		int numberOfFieldChoices = 0;

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormField.getDDMFormFieldOptions();

		for (String optionValue : ddmFormFieldOptions.getOptionsValues()) {
			LocalizedValue optionLabel = ddmFormFieldOptions.getOptionLabels(
				optionValue);

			fieldChoicesLabels.add(optionLabel.getString(locale));
			fieldChoicesValues.add(optionValue);

			numberOfFieldChoices++;
		}

		template.put("fieldChoicesLabels", fieldChoicesLabels);
		template.put("fieldChoicesValues", fieldChoicesValues);
		template.put("numberOfFieldChoices", numberOfFieldChoices);
	}

	protected void populateRadioContext(
		Template template, DDMFormField ddmFormField, Locale locale,
		String fieldQualifiedName) {

		populateRadioAndSelectCommonContext(
			template, ddmFormField, locale, fieldQualifiedName);

		List<String> fieldChoicesIds = new ArrayList<>();
		int numberOfFieldChoices = (int) template.get("numberOfFieldChoices");

		for (int i = 0; i < numberOfFieldChoices; i++) {
			fieldChoicesIds.add(fieldQualifiedName + StringPool.UNDERLINE + i);
		}

		template.put("fieldChoicesIds", fieldChoicesIds);
	}

	protected void populateRequiredContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		String fieldName = ddmFormField.getName();

		String instanceId = StringUtil.randomString();

		String fieldNameSuffix = getFieldNameSuffix(instanceId);

		String fieldQualifiedName = getFieldQualifiedName(
			fieldName, instanceId);

		LocalizedValue predefinedValue = ddmFormField.getPredefinedValue();

		LocalizedValue label = ddmFormField.getLabel();

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		template.put("fieldName", fieldName);
		template.put("fieldNameSuffix", fieldNameSuffix);
		template.put("fieldQualifiedName", fieldQualifiedName);
		template.put("fieldValue", predefinedValue.getString(locale));
		template.put("fieldLabel", label.getString(locale));

		String ddmFormFieldType = ddmFormField.getType();

		if (ddmFormFieldType.equals("select")) {
			populateSelectContext(template, ddmFormField, locale,
				fieldQualifiedName);
		}
		else if (ddmFormFieldType.equals("radio")) {
			populateRadioContext(template, ddmFormField, locale,
				fieldQualifiedName);
		}
	}

	protected void populateSelectContext(
		Template template, DDMFormField ddmFormField, Locale locale,
		String fieldQualifiedName) {

		populateRadioAndSelectCommonContext(
			template, ddmFormField, locale, fieldQualifiedName);
	}

	protected void setTemplatesNamespaces() {
		this.templatesNamespaces = new HashMap<>();

		this.templatesNamespaces.put("checkbox", "ddm.checkbox");
		this.templatesNamespaces.put("radio", "ddm.radio");
		this.templatesNamespaces.put("select", "ddm.select");
	}

}