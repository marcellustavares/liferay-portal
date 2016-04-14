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

package com.liferay.dynamic.data.mapping.form.field.type;

import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	service = {
		DefaultDDMFormFieldTemplateContextFactory.class,
		DDMFormFieldTemplateContextFactory.class
	}
)
public class DefaultDDMFormFieldTemplateContextFactory
	implements DDMFormFieldTemplateContextFactory {

	@Override
	public JSONObject create(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		JSONArray childTemplateContext =
			ddmFormFieldRenderingContext.getChildTemplateContext();

		if (childTemplateContext != null) {
			jsonObject.put("childTemplateContext", childTemplateContext);
		}

		jsonObject.put("dir", LanguageUtil.get(locale, "lang.dir"));
		jsonObject.put("label", ddmFormFieldRenderingContext.getLabel());
		jsonObject.put("name", ddmFormFieldRenderingContext.getName());
		jsonObject.put(
			"readOnly",
			isReadOnly(ddmFormField, ddmFormFieldRenderingContext));
		jsonObject.put("required", ddmFormFieldRenderingContext.isRequired());
		jsonObject.put("showLabel", ddmFormField.isShowLabel());
		jsonObject.put("tip", ddmFormFieldRenderingContext.getTip());
		jsonObject.put("value", ddmFormFieldRenderingContext.getValue());
		jsonObject.put("visible", ddmFormFieldRenderingContext.isVisible());

		return jsonObject;
	}

	protected boolean isReadOnly(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		if (ddmFormFieldRenderingContext.isReadOnly() ||
			ddmFormField.isReadOnly()) {

			return true;
		}

		return false;
	}

	@Reference
	private JSONFactory _jsonFactory;

}