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

import com.liferay.dynamic.data.mapping.form.renderer.DDMFormFieldTypesJSONSerializer;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistryUtil;

import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Bruno Basto
 */
@Component(immediate = true, service = {DDMFormFieldTypesJSONSerializer.class})
@DoPrivileged
public class DDMFormFieldTypesJSONSerializerImpl
	implements DDMFormFieldTypesJSONSerializer {

	public JSONArray serialize() {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		Set<String> ddmFormFieldTypeNames =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldTypeNames();

		for (String ddmFormFieldTypeName : ddmFormFieldTypeNames) {
			JSONObject ddmFormFieldTypeJSONObject =
				JSONFactoryUtil.createJSONObject();

			DDMFormFieldType ddmFormFieldType =
				DDMFormFieldTypeRegistryUtil.getDDMFormFieldType(
					ddmFormFieldTypeName);

			ddmFormFieldTypeJSONObject.put("icon", ddmFormFieldType.getIcon());
			ddmFormFieldTypeJSONObject.put(
				"label", ddmFormFieldType.getLabel());
			ddmFormFieldTypeJSONObject.put("name", ddmFormFieldTypeName);

			DDMFormFieldRenderer ddmFormFieldRenderer =
				ddmFormFieldType.getDDMFormFieldRenderer();

			ddmFormFieldTypeJSONObject.put(
				"templateNamespace",
				ddmFormFieldRenderer.getTemplateNamespace());

			jsonArray.put(ddmFormFieldTypeJSONObject);
		}

		return jsonArray;
	}

}