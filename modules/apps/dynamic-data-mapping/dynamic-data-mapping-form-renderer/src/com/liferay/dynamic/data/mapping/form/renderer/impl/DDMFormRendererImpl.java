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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

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
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
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
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

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
			
			Stack<DDMFormFieldRenderingContext> 
				ddmFormFieldRenderingContextStack = 
					getDDMFormFieldRenderingContextStack(ddmForm, ddmFormRenderingContext.getDDMFormValues(), ddmFormRenderingContext.getPortletNamespace());
			
			Map<String, List<String>> ddmFormFieldMap = getRenderedDDMFormFieldsMap(ddmFormFieldRenderingContextStack, ddmFormRenderingContext.getLocale());
			
			Map<String, Object> rows = getRows(
				ddmForm, ddmFormLayout.getDDMFormLayoutRows(),
				ddmFormFieldMap);
			
			template.put("rows", rows.get("rows"));
			
			return render(template);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return StringPool.BLANK;
	}
	
	protected Map<String, List<String>> getRenderedDDMFormFieldsMap(
		Stack<DDMFormFieldRenderingContext> ddmFormFieldRenderingContextStack,
		Locale locale) throws PortalException {

		Map<String, List<String>> renderedDDMFormFieldsMap = new HashMap<>();
		
		while (!ddmFormFieldRenderingContextStack.isEmpty()) {
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext = 
				ddmFormFieldRenderingContextStack.pop();
			
			ddmFormFieldRenderingContext.setLocale(locale);
			
			DDMFormField ddmFormField = ddmFormFieldRenderingContext.getDDMFormField();
			
			populateRenderedNestedDDMFormFields(
				ddmFormField, ddmFormFieldRenderingContext, 
				renderedDDMFormFieldsMap);
			
			String renderedDDMFormField = renderDDMFormField(ddmFormFieldRenderingContext);
			
			List<String> renderedDDMFormFieldsList = renderedDDMFormFieldsMap.get(ddmFormField.getName());
			
			if (renderedDDMFormFieldsList == null) {
				renderedDDMFormFieldsList = new ArrayList<>();
				
				renderedDDMFormFieldsMap.put(ddmFormField.getName(), renderedDDMFormFieldsList);
			} 
			
			renderedDDMFormFieldsList.add(renderedDDMFormField);
		}
		
		return renderedDDMFormFieldsMap;
	}
	
	private void populateRenderedNestedDDMFormFields(
		DDMFormField ddmFormField, 
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext,
		Map<String, List<String>> renderedDDMFormFieldsMap) {

		for (DDMFormField nestedDDMFormField : ddmFormField.getNestedDDMFormFields()) {
			StringBundler sb = new StringBundler();
			
			for (String nestedRenderedDDMFormField : 
					renderedDDMFormFieldsMap.get(nestedDDMFormField.getName())) {
				
				sb.append(nestedRenderedDDMFormField);
			}
			
			if (!nestedDDMFormField.isRepeatable()) {
				renderedDDMFormFieldsMap.remove(nestedDDMFormField.getName());
			}
			
			ddmFormFieldRenderingContext.setNestedRenderedDDMFormFields(
				sb.toString());
		}
	}

	protected Stack<DDMFormFieldRenderingContext> 
		getDDMFormFieldRenderingContextStack(
			DDMForm ddmForm, DDMFormValues ddmFormValues, String portletNamespace) {
		
		Stack<DDMFormFieldRenderingContext> ddmFormFieldRenderingContextStack = 
			new Stack<>();
			
		populateDDMFormFieldRenderingContextStack(
			ddmForm.getDDMFormFields(), 
			ddmFormValues.getDDMFormFieldValuesMap(), portletNamespace, null,
			ddmFormFieldRenderingContextStack);
		
		return ddmFormFieldRenderingContextStack;
	} 
	
	protected void populateDDMFormFieldRenderingContextStack(
		List<DDMFormField> ddmFormFields,
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap, String portletNamespace, DDMFormFieldRenderingContext parentDDMFormFieldRenderingContext,
		Stack<DDMFormFieldRenderingContext> ddmFormFieldRenderingContextStack) {
	
		if (ddmFormFields.isEmpty()) {
			return;
		}
		
		for (DDMFormField ddmFormField : ddmFormFields) {
			List<DDMFormFieldValue> ddmFormFieldValues = 
				ddmFormFieldValuesMap.get(ddmFormField.getName());
			
			if (ListUtil.isNotEmpty(ddmFormFieldValues)) {
				int index = 0;

				for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
					DDMFormFieldRenderingContext ddmFormFieldRenderingContext = 
						new DDMFormFieldRenderingContext();
					
					ddmFormFieldRenderingContext.setDDMFormField(ddmFormField);
					ddmFormFieldRenderingContext.setDDMFormFieldValue(
						ddmFormFieldValue);
					ddmFormFieldRenderingContext.setDDMFormFieldValueIndex(
						index++);
					ddmFormFieldRenderingContext.setPortletNamespace(portletNamespace);
					
					if (parentDDMFormFieldRenderingContext != null) {
						ddmFormFieldRenderingContext.setParentDDMFormFieldRenderingContext(parentDDMFormFieldRenderingContext);
					}
					
					ddmFormFieldRenderingContextStack.push(
						ddmFormFieldRenderingContext);
					
					populateDDMFormFieldRenderingContextStack(
						ddmFormField.getNestedDDMFormFields(),
						ddmFormFieldValue.getNestedDDMFormFieldValuesMap(), portletNamespace, ddmFormFieldRenderingContext,
						ddmFormFieldRenderingContextStack);
				}
			}
			else {
				DDMFormFieldRenderingContext ddmFormFieldRenderingContext = 
					new DDMFormFieldRenderingContext();
				
				ddmFormFieldRenderingContext.setDDMFormField(ddmFormField);
				ddmFormFieldRenderingContext.setPortletNamespace(portletNamespace);
				
				if (parentDDMFormFieldRenderingContext != null) {
					ddmFormFieldRenderingContext.setParentDDMFormFieldRenderingContext(parentDDMFormFieldRenderingContext);
				}
				ddmFormFieldRenderingContextStack.push(
					ddmFormFieldRenderingContext);
				
				Map<String, List<DDMFormFieldValue>> 
					nestedDDMFormFieldValuesMap = Collections.emptyMap();
				
				populateDDMFormFieldRenderingContextStack(
					ddmFormField.getNestedDDMFormFields(), 
					nestedDDMFormFieldValuesMap, portletNamespace, ddmFormFieldRenderingContext,
					ddmFormFieldRenderingContextStack);
			}
			
		}
	}
	
//	protected void populateRenderedDDMFormFieldsMap(
//		List<DDMFormField> ddmFormFields,
//		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap,
//		Map<String, String> renderedDDMFormFieldsMap) {
//		
//		for (DDMFormField ddmFormField : ddmFormFields) {
//			List<DDMFormFieldValue> ddmFormFieldValues = 
//				ddmFormFieldValuesMap.get(ddmFormField.getName());
//			
//			if (ListUtil.isEmpty(ddmFormFieldValues)) {
//				ddmFormFieldValuesMap.put(ddmFormField.getName(), value)
//			}
//			
//		}
//	}
	
	protected  Map<String, Object> getRows(
			DDMForm ddmForm, List<DDMFormLayoutRow> ddmFormLayoutRows,
			Map<String, List<String>> ddmFormFieldMap)
		throws PortalException {
		
		Map<String, Object> rows = new HashMap<>();
		 
		List<Map<String, Object>> rowsList = new ArrayList<>();
		
		for (DDMFormLayoutRow ddmFormLayoutRow : ddmFormLayoutRows) {
			if (!ddmFormLayoutRow.getType().equals("LayoutRow")) {
				continue;
			}

			rowsList.add(
				getRow(ddmForm, ddmFormLayoutRow, ddmFormFieldMap));
		}
		
		rows.put("rows", rowsList);
		
		return rows;
	}

	protected Map<String, Object> getRow(
			DDMForm ddmForm, DDMFormLayoutRow ddFormLayoutRow,
			Map<String, List<String>> ddmFormFieldMap) 
		throws PortalException {
		
		Map<String, Object> columns = new HashMap<>();
		
		List<Map<String, Object>> columnsList = new ArrayList<>();
		
		for (DDMFormLayoutColumn ddmFormLayoutColumn : 
				ddFormLayoutRow.getDDMFormLayoutColumns()) {
			
			columnsList.add(
				getColumn(
					ddmForm, ddmFormLayoutColumn, ddmFormFieldMap));
			
		}
		
		columns.put("columns", columnsList);
		
		return columns;
	}
	
	protected Map<String, Object> getColumn(
			DDMForm ddmForm, DDMFormLayoutColumn ddmFormLayoutColumn, 
			Map<String, List<String>> ddmFormFieldMap)
		throws PortalException {
		
		Map<String, Object> column = new HashMap<>();
		
		column.put("size", ddmFormLayoutColumn.getSize());
		
		StringBundler sb = new StringBundler();
		
		for (String html : ddmFormFieldMap.get(ddmFormLayoutColumn.getDDMFormFieldName())) {
			sb.append(html);
		}
		column.put("html", sb.toString());
		
		return column;
	}
	
	protected String renderDDMFormField(
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext) 
		throws PortalException {
		
		DDMFormField ddmFormField = ddmFormFieldRenderingContext.getDDMFormField();
		
		DDMFormFieldType ddmFormFieldType = 
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldType(
				ddmFormField.getType());
		
		DDMFormFieldRenderer ddmFormFieldRenderer = 
			ddmFormFieldType.getDDMFormFieldRenderer();
		
		return ddmFormFieldRenderer.render(
			ddmFormField, ddmFormFieldRenderingContext);
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