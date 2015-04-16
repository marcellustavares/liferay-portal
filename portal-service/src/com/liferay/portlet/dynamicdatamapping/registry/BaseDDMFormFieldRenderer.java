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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.registry.settings.DDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;

import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * @author Marcellus Tavares
 */
public abstract class BaseDDMFormFieldRenderer implements DDMFormFieldRenderer {

	@Override
	public String render(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws PortalException {

		Template template = TemplateManagerUtil.getTemplate(
			getTemplateLanguage(), getTemplateResource(), false);

		template.put(TemplateConstants.NAMESPACE, getTemplateNamespace());

		populateRequiredContext(
			template, ddmFormField, ddmFormFieldRenderingContext);

		populateOptionalContext(
			template, ddmFormField, ddmFormFieldRenderingContext);

		return render(template);
	}

	protected TemplateResource getTemplateResource(String templatePath) {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		URL templateURL = classLoader.getResource(templatePath);

		return new URLTemplateResource(templateURL.getPath(), templateURL);
	}

	protected void populateOptionalContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		DDMFormFieldType ddmFormFieldType =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldType(
				ddmFormField.getType());

		List<DDMFormFieldTypeSetting> optionalSettings =
			ddmFormFieldType.getOptionalSettings();

		for (DDMFormFieldTypeSetting setting : optionalSettings) {
			Object value = ddmFormField.getProperty(setting.getName());

			if (setting.isLocalizable()) {
				value = ((LocalizedValue)value).getString(locale);
			}

			template.put(setting.getName(), value);
		}
	}

	protected void populateRequiredContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		template.put(
			"childElementsHTML",
			ddmFormFieldRenderingContext.getChildElementsHTML());
		template.put("dir", LanguageUtil.get(locale, "lang.dir"));

		LocalizedValue label = ddmFormField.getLabel();

		String labelString = StringPool.BLANK;

		if (Validator.isNotNull(label.getString(locale))) {
			labelString = label.getString(locale);
		}

		template.put("label", labelString);
		template.put("name", ddmFormFieldRenderingContext.getName());
		template.put("value", ddmFormFieldRenderingContext.getValue());
	}

	protected String render(Template template) throws PortalException {
		Writer writer = new UnsyncStringWriter();

		template.processTemplate(writer);

		return writer.toString();
	}

}