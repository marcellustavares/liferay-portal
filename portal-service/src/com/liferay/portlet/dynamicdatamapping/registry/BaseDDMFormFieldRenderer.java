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
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;

import java.io.Writer;
import java.util.Locale;
import java.util.Stack;

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
			TemplateConstants.LANG_TYPE_SOY, getTemplateResource(), false);

		template.put(TemplateConstants.NAMESPACE, getTemplateNamespace());

		populateRequiredContext(
			template, ddmFormField, ddmFormFieldRenderingContext);

		populateOptionalContext(
			template, ddmFormField, ddmFormFieldRenderingContext);

		return render(template);
	}

	protected String getFieldNameSuffix(String instanceId) {
		return _INSTANCE_SEPARATOR.concat(instanceId);
	}

	protected String getFieldQualifiedName(
		String fieldName, String instanceId) {

		String fieldNameSuffix = getFieldNameSuffix(instanceId);

		return fieldName.concat(fieldNameSuffix);
	}

	protected void populateOptionalContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {
	}

	protected void populateRequiredContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		String fieldName = ddmFormField.getName();

		String instanceId = StringUtil.randomString();

		template.put("dir", LanguageUtil.get(locale, "lang.dir"));
		template.put("name", getFieldName(ddmFormFieldRenderingContext));
		template.put(
			"fieldQualifiedName", getFieldQualifiedName(fieldName, instanceId));
		template.put("fieldNameSuffix", getFieldNameSuffix(instanceId));

		LocalizedValue label = ddmFormField.getLabel();

		String labelString = StringPool.BLANK;
		
		if (Validator.isNotNull(label.getString(locale))) {
			labelString = label.getString(locale);
		}
		
		template.put("label", labelString);
		template.put("placeholder", StringPool.BLANK);

		template.put("value", StringPool.BLANK);
	}
	
	protected String getFieldName(
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {
		
		StringBundler sb = new StringBundler();
		
		sb.append(ddmFormFieldRenderingContext.getPortletNamespace());
		sb.append(_DDM_FIELD_NAME_PREFIX);
		
		Stack<String> parentNamespace = new Stack<>();
		
		DDMFormFieldRenderingContext parentDDMFormFieldRenderingContext = ddmFormFieldRenderingContext.getParentDDMFormFieldRenderingContext();
		
		while (parentDDMFormFieldRenderingContext != null) {
			StringBundler sb1 = new StringBundler();
			
			sb1.append(parentDDMFormFieldRenderingContext.getDDMFormField().getName());
			sb1.append(StringPool.UNDERLINE);
			sb1.append(parentDDMFormFieldRenderingContext.getDDMFormFieldValue().getInstanceId());
			sb1.append(StringPool.UNDERLINE);
			sb1.append(parentDDMFormFieldRenderingContext.getDDMFormFieldValueIndex());
			sb1.append(StringPool.POUND);
			
			parentNamespace.add(sb1.toString());
			
			parentDDMFormFieldRenderingContext = parentDDMFormFieldRenderingContext.getParentDDMFormFieldRenderingContext();
			
		}
		
		while (!parentNamespace.isEmpty()) {
			sb.append(parentNamespace.pop());
		}
		
		sb.append(ddmFormFieldRenderingContext.getDDMFormField().getName());
		sb.append(StringPool.UNDERLINE);
		sb.append(ddmFormFieldRenderingContext.getDDMFormFieldValue().getInstanceId());
		sb.append(StringPool.UNDERLINE);
		sb.append(ddmFormFieldRenderingContext.getDDMFormFieldValueIndex());
		sb.append(StringPool.DOUBLE_UNDERLINE);
		sb.append(LocaleUtil.toLanguageId(ddmFormFieldRenderingContext.getLocale()));
		
		return sb.toString();
	}

	protected String render(Template template) throws PortalException {
		Writer writer = new UnsyncStringWriter();

		template.processTemplate(writer);

		return writer.toString();
	}

	private static final String _INSTANCE_SEPARATOR = "_INSTANCE_";
	private static final String _DDM_FIELD_NAME_PREFIX = "ddm__";

}