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
import com.liferay.portlet.dynamicdatamapping.registry.BaseDDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueAccessor;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueRendererAccessor;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Renato Rego
 */
@Component(immediate = true, service = DDMFormFieldType.class)
public class SelectDDMFormFieldType extends BaseDDMFormFieldType {

	@Override
	public DDMFormFieldRenderer getDDMFormFieldRenderer() {
		return _ddmFormFieldRenderer;
	}

	@Override
	public DDMFormFieldValueAccessor<JSONArray> getDDMFormFieldValueAccessor(
		Locale locale) {

		return new SelectDDMFormFieldValueAccessor(locale);
	}

	@Override
	public DDMFormFieldValueRendererAccessor
		getDDMFormFieldValueRendererAccessor(Locale locale) {

		return new SelectDDMFormFieldValueRendererAccessor(
			getDDMFormFieldValueAccessor(locale));
	}

	@Override
	public String getLabel() {
		return "Select";
	}

	@Override
	public String getName() {
		return "select";
	}

	@Override
	public List<DDMFormFieldTypeSetting> getOptionalSettings() {
		return Collections.emptyList();
	}

	@Reference(service = SelectDDMFormFieldRenderer.class, unbind = "-")
	protected void setDDMFormFieldRenderer(
		DDMFormFieldRenderer ddmFormFieldRenderer) {

		_ddmFormFieldRenderer = ddmFormFieldRenderer;
	}

	private DDMFormFieldRenderer _ddmFormFieldRenderer;

}