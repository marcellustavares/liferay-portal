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
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.registry.BaseDDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Renato Rego
 */
@Component(
	immediate = true, property = {"templatePath=/META-INF/resources/radio.soy"},
	service = {
		RadioDDMFormFieldRenderer.class, DDMFormFieldRenderer.class
	}
)
public class RadioDDMFormFieldRenderer extends BaseDDMFormFieldRenderer {

	@Activate
	protected void activate(Map<String, Object> properties) {
		String templatePath = MapUtil.getString(properties, "templatePath");

		TemplateResource templateResource = getTemplateResource(templatePath);

		this.templateNamespace = "ddm.text";
		this.templateResource = templateResource;
	}

	protected String getCheckboxFieldStatus(String predefinedValueString) {
		String fieldStatus = StringPool.BLANK;

		if (predefinedValueString.equals("true")) {
			fieldStatus = "checked";
		}

		return fieldStatus;
	}

	protected String getRadioFieldStatus(
		String predefinedValueString, String optionValue) {

		String fieldStatus = StringPool.BLANK;

		if (predefinedValueString.equals(optionValue)) {
			fieldStatus = "checked";
		}

		return fieldStatus;
	}

	protected String getSelectFieldStatus(
		String predefinedValueString, String optionValue) {

		try {
			String fieldStatus = StringPool.BLANK;

			JSONArray predefinedValues = JSONFactoryUtil.createJSONArray(
				predefinedValueString);

			for (int i = 0; i < predefinedValues.length(); i++) {
				if (predefinedValues.getString(i).equals(optionValue)) {
					fieldStatus = "selected";
					break;
				}
			}

			return fieldStatus;
		} catch (Exception e) {
			_log.error(e, e);
			return StringPool.BLANK;
		}
	}

	protected TemplateResource getTemplateResource(String templatePath) {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		URL templateURL = classLoader.getResource(templatePath);

		return new URLTemplateResource(templateURL.getPath(), templateURL);
	}

	protected void populateRadioAndSelectCommonContext(
		Template template, DDMFormField ddmFormField, Locale locale) {

		List<String> fieldChoicesLabels = new ArrayList<>();
		List<String> fieldChoicesStatus = new ArrayList<>();
		List<String> fieldChoicesValues = new ArrayList<>();

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormField.getDDMFormFieldOptions();

		String predefinedValueString =
			ddmFormField.getPredefinedValue().getString(locale);

		String ddmFormFieldType = ddmFormField.getType();

		for (String optionValue : ddmFormFieldOptions.getOptionsValues()) {
			LocalizedValue optionLabel = ddmFormFieldOptions.getOptionLabels(
				optionValue);

			fieldChoicesLabels.add(optionLabel.getString(locale));

			if (ddmFormFieldType.equals("radio")) {
				fieldChoicesStatus.add(
					getRadioFieldStatus(predefinedValueString, optionValue));
			}
			else if (ddmFormFieldType.equals("select")) {
				fieldChoicesStatus.add(
					getSelectFieldStatus(predefinedValueString, optionValue));
			}

			fieldChoicesValues.add(optionValue);
		}

		template.put("fieldChoicesLabels", fieldChoicesLabels);
		template.put("fieldChoicesStatus", fieldChoicesStatus);
		template.put("fieldChoicesValues", fieldChoicesValues);
	}

	protected void populateRadioContext(
		Template template, DDMFormField ddmFormField, Locale locale,
		String fieldQualifiedName) {

		populateRadioAndSelectCommonContext(template, ddmFormField, locale);

		List<String> fieldChoicesIds = new ArrayList<>();

		int numberOfFieldChoices =
			ddmFormField.getDDMFormFieldOptions().getOptionsValues().size();

		for (int i = 0; i < numberOfFieldChoices; i++) {
			fieldChoicesIds.add(fieldQualifiedName + StringPool.UNDERLINE + i);
		}

		template.put("fieldChoicesIds", fieldChoicesIds);
	}

	protected void populateRequiredContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue label = ddmFormField.getLabel();

		String fieldName = ddmFormField.getName();

		String instanceId = StringUtil.randomString();

		String fieldNameSuffix = getFieldNameSuffix(instanceId);

		String fieldQualifiedName = getFieldQualifiedName(
			fieldName, instanceId);

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		template.put("fieldLabel", label.getString(locale));
		template.put("fieldName", fieldName);
		template.put("fieldNameSuffix", fieldNameSuffix);
		template.put("fieldQualifiedName", fieldQualifiedName);

		String ddmFormFieldType = ddmFormField.getType();

		if (ddmFormFieldType.equals("checkbox")) {
			String predefinedValueString =
				ddmFormField.getPredefinedValue().getString(locale);

			String fieldStatus = getCheckboxFieldStatus(predefinedValueString);

			template.put("fieldStatus", fieldStatus);
		}
		else if (ddmFormFieldType.equals("select")) {
			populateSelectContext(template, ddmFormField, locale);
		}
		else if (ddmFormFieldType.equals("radio")) {
			populateRadioContext(
				template, ddmFormField, locale, fieldQualifiedName);
		}
	}

	protected void populateSelectContext(
		Template template, DDMFormField ddmFormField, Locale locale) {

		populateRadioAndSelectCommonContext(template, ddmFormField, locale);

		template.put("dir", LanguageUtil.get(locale, "lang.dir"));
		template.put(
			"fieldIsMultiple",
			ddmFormField.isMultiple() ? "multiple" : StringPool.BLANK);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RadioDDMFormFieldRenderer.class);

}