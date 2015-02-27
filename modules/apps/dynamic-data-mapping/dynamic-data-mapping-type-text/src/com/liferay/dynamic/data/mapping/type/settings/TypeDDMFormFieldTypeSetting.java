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

package com.liferay.dynamic.data.mapping.type.settings;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeSettingEditor;

/**
 * @author Marcellus Tavares
 */
public class TypeDDMFormFieldTypeSetting implements DDMFormFieldTypeSetting {

	@Override
	public DDMFormFieldTypeSettingEditor getDDMFormFieldTypeSettingEditor() {
		return new DDMFormFieldTypeSettingEditor() {

			@Override
			public String getEditorType() {
				return "RadioGroup";
			}

			@Override
			public JSONObject getOptions() {
				JSONObject options = JSONFactoryUtil.createJSONObject();

				options.put("label", "my-field-is");
				options.put("radioLabels", createRadioLabels());

				return options;
			}

			protected JSONArray createRadioLabels() {
				JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

				jsonArray.put("Singleline");
				jsonArray.put("Multiline");

				return jsonArray;
			}

		};
	}

	@Override
	public String getName() {
		return "type";
	}

}