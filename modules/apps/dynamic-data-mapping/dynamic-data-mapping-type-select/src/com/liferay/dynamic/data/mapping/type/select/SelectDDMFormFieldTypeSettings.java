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

package com.liferay.dynamic.data.mapping.type.select;

import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.registry.DefaultDDMFormFieldTypeSettings;
import com.liferay.dynamic.data.mapping.registry.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.registry.annotations.DDMFormField;
import com.liferay.portal.kernel.json.JSONObject;

/**
 * @author Marcellus Tavares
 */
@DDMForm
public interface SelectDDMFormFieldTypeSettings
	extends DefaultDDMFormFieldTypeSettings {

	@DDMFormField(
		dataType = "string", label = "%datasource",
		properties = {"setting.category=basic", "setting.weight=0"},
		type = "datasource",
		visibilityExpression = "datasourceType.equals(\"datasource\")"
	)
	public JSONObject datasource();

	@DDMFormField(
		dataType = "string", label = "%use-datasource",
		optionLabels = {"%manually", "%datasource"},
		optionValues = {"manually", "datasource"},
		predefinedValue = "manually",
		properties = {"setting.category=basic", "setting.weight=1"},
		type = "radio"
	)
	public String datasourceType();

	@DDMFormField(
		label = "%multiple", properties = {
			"setting.category=advanced", "setting.weight=2",
			"showAsSwitcher=true"
		}
	)
	public boolean multiple();

	@DDMFormField(
		dataType = "ddm-options", label = "%options",
		properties = {"setting.category=basic", "setting.weight=0"},
		type = "options",
		visibilityExpression = "!datasourceType.equals(\"datasource\")"
	)
	public DDMFormFieldOptions options();

}