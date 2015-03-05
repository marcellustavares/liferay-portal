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

package com.liferay.dynamic.data.mapping.form.renderer.impl;

import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import aQute.bnd.annotation.component.Deactivate;

import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayoutColumn;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayoutRow;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistryUtil;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;

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
		DDMFormRenderingContext ddmFormRenderingContext) {
		
		try {
			Template template = TemplateManagerUtil.getTemplate(
				TemplateConstants.LANG_TYPE_SOY, _templateResource, false);
			
			template.put(TemplateConstants.NAMESPACE, "ddm.layout");
			
			Map<String, Object> rows = getRows(ddmForm, ddmFormLayout.getDDMFormLayoutRows(), ddmFormRenderingContext);
			
			template.put("rows", rows.get("rows"));
			
			return render(template);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return StringPool.BLANK;
	}
	
	protected  Map<String, Object> getRows(DDMForm ddmForm, List<DDMFormLayoutRow> ddmFormLayoutRows, DDMFormRenderingContext ddmFormRenderingContext) throws PortalException {
		Map<String, Object> rows = new HashMap<>();
		 
		List<Map<String, Object>> rowsList = new ArrayList<>();
		
		for (DDMFormLayoutRow ddmFormLayoutRow : ddmFormLayoutRows) {
			if (!ddmFormLayoutRow.getType().equals("LayoutRow")) {
				continue;
			}

			rowsList.add(getRow(ddmForm, ddmFormLayoutRow, ddmFormRenderingContext));
		}
		
		rows.put("rows", rowsList);
		
		return rows;
	}

	protected Map<String, Object> getRow(DDMForm ddmForm, DDMFormLayoutRow ddFormLayoutRow, DDMFormRenderingContext ddmFormRenderingContext) throws PortalException {
		Map<String, Object> columns = new HashMap<>();
		
		List<Map<String, Object>> columnsList = new ArrayList<>();
		
		for (DDMFormLayoutColumn ddmFormLayoutColumn : 
				ddFormLayoutRow.getDDMFormLayoutColumns()) {
			
			columnsList.add(getColumn(ddmForm, ddmFormLayoutColumn, ddmFormRenderingContext));
			
		}
		
		columns.put("columns", columnsList);
		
		return columns;
	}
	
	protected Map<String, Object> getColumn(DDMForm ddmForm, DDMFormLayoutColumn ddmFormLayoutColumn, DDMFormRenderingContext ddmFormRenderingContext) throws PortalException {
		Map<String, Object> column = new HashMap<>();
		
		column.put("size", ddmFormLayoutColumn.getSize());
		column.put("html", renderDDMFormField(ddmForm, ddmFormLayoutColumn.getDDMFormFieldName(), ddmFormRenderingContext));
		
		return column;
	}
	
	protected String renderDDMFormField(DDMForm ddmForm, String fieldName, DDMFormRenderingContext ddmFormRenderingContext) throws PortalException {
		Map<String, DDMFormField> ddmFormFieldsMap = ddmForm.getDDMFormFieldsMap(true);
		
		DDMFormField ddmFormField = ddmFormFieldsMap.get(fieldName);
		
		DDMFormFieldType ddmFormFieldType = DDMFormFieldTypeRegistryUtil.getDDMFormFieldType(ddmFormField.getType());
		
		DDMFormFieldRenderer ddmFormFieldRenderer = ddmFormFieldType.getDDMFormFieldRenderer();
		
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext = new DDMFormFieldRenderingContext();
		
		ddmFormFieldRenderingContext.setLocale(ddmFormRenderingContext.getLocale());
		
		return ddmFormFieldRenderer.render(ddmFormField, ddmFormFieldRenderingContext);
	}
	
	protected String render(Template template) throws PortalException {
		Writer writer = new UnsyncStringWriter();

		template.processTemplate(writer);

		return writer.toString();
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

	protected TemplateResource getTemplateResource(String templatePath) {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		URL templateURL = classLoader.getResource(templatePath);

		return new URLTemplateResource(templateURL.getPath(), templateURL);
	}
	
	private TemplateResource _templateResource;
	
}