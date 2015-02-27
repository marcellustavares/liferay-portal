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

package com.liferay.portlet.dynamicdatamapping.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayoutRow;

import java.util.List;

/**
 * @author Bruno Basto
 */
@DoPrivileged
public class DDMLayoutHelper {

	public static JSONObject getDDMFormLayoutJSON(DDMFormLayout ddmFormLayout) {
		JSONObject ddmFormLayoutJSONObject = JSONFactoryUtil.createJSONObject();

		JSONArray ddmFormLayoutRowsJSONArray =
			JSONFactoryUtil.createJSONArray();

		List<DDMFormLayoutRow> ddmFormLayoutRows =
			ddmFormLayout.getDDMFormLayoutRows();

		for (DDMFormLayoutRow ddmFormLayoutRow : ddmFormLayoutRows) {
			JSONObject ddmFormLayoutRowJSONObject =
				JSONFactoryUtil.createJSONObject();

			ddmFormLayoutRow.getDDMFormLayoutColumns();

			ddmFormLayoutRowJSONObject.put("type", ddmFormLayoutRow.getType());

			ddmFormLayoutRowsJSONArray.put(ddmFormLayoutRowJSONObject);
		}

		return ddmFormLayoutJSONObject;
	}

}