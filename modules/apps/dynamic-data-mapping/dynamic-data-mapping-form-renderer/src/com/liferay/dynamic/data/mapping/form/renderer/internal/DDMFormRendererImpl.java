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

import aQute.bnd.annotation.component.Deactivate;

import com.liferay.dynamic.data.mapping.form.renderer.DDMFormFieldTypesJSONSerializer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONSerializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONSerializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import java.io.Writer;

import java.net.URL;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, property = {"templatePath=/META-INF/resources/form.soy"},
	service = {DDMFormRenderer.class}
)
public class DDMFormRendererImpl implements DDMFormRenderer {

	@Override
	public String render(
			DDMForm ddmForm, DDMFormLayout ddmFormLayout,
			DDMFormRenderingContext ddmFormRenderingContext)
		throws PortalException {

		Template template = TemplateManagerUtil.getTemplate(
			TemplateConstants.LANG_TYPE_SOY, _templateResource, false);

		template.put(TemplateConstants.NAMESPACE, "ddm.pages");

		template.put(
			"fieldTypes",
			_ddmFormFieldTypesJSONSerializer.serialize().toString());
		template.put("form", DDMFormJSONSerializerUtil.serialize(ddmForm));

		Map<String, List<String>> renderedDDMFormFieldsMap =
			getRenderedDDMFormFieldsMap(ddmForm, ddmFormRenderingContext);

		List<Object> pages = getPages(
			ddmFormLayout, renderedDDMFormFieldsMap,
			ddmFormRenderingContext.getLocale());

		template.put("pages", pages);
		template.put(
			"portletNamespace", ddmFormRenderingContext.getPortletNamespace());

		DDMFormValues ddmFormValues =
			ddmFormRenderingContext.getDDMFormValues();

		String serializedDDMFormValues = JSONFactoryUtil.getNullJSON();

		if (ddmFormValues != null) {
			serializedDDMFormValues = DDMFormValuesJSONSerializerUtil.serialize(
				ddmFormValues);
		}

		template.put("values", serializedDDMFormValues);

		return render(template);
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		String templatePath = MapUtil.getString(properties, "templatePath");

		_templateResource = getTemplateResource(templatePath);
	}

	@Deactivate
	protected void deactivate() {
		_templateResource = null;
	}

	protected Map<String, Object> getForm(DDMForm ddmForm) {
		DDMFormTransformer ddmFormTransformer = new DDMFormTransformer(ddmForm);

		return ddmFormTransformer.getForm();
	}

	protected List<Object> getPages(
		DDMFormLayout ddmFormLayout,
		Map<String, List<String>> renderedDDMFormFieldsMap, Locale locale) {

		DDMFormLayoutTransformer ddmFormLayoutTransformer =
			new DDMFormLayoutTransformer(
				ddmFormLayout, renderedDDMFormFieldsMap, locale);

		return ddmFormLayoutTransformer.getPages();
	}

	protected Map<String, List<String>> getRenderedDDMFormFieldsMap(
			DDMForm ddmForm, DDMFormRenderingContext ddmFormRenderingContext)
		throws PortalException {

		DDMFormRendererHelper ddmFormRendererHelper = new DDMFormRendererHelper(
			ddmForm, ddmFormRenderingContext);

		return ddmFormRendererHelper.getRenderedDDMFormFieldsMap();
	}

	protected TemplateResource getTemplateResource(String templatePath) {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		URL templateURL = classLoader.getResource(templatePath);

		return new URLTemplateResource(templateURL.getPath(), templateURL);
	}

	protected String render(Template template) throws PortalException {
		Writer writer = new UnsyncStringWriter();

		template.processTemplate(writer);

		return writer.toString();
	}

	@Reference(service = DDMFormFieldTypesJSONSerializer.class, unbind = "-")
	protected void setDDMFormFieldTypesHelper(
		DDMFormFieldTypesJSONSerializer ddmFormFieldTypesJSONSerializer) {

		_ddmFormFieldTypesJSONSerializer = ddmFormFieldTypesJSONSerializer;
	}

	private DDMFormFieldTypesJSONSerializer _ddmFormFieldTypesJSONSerializer;
	private TemplateResource _templateResource;

}