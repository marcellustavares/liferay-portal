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

import java.util.ArrayList;
import java.util.List;

import com.liferay.portlet.dynamicdatamapping.registry.settings.DDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.DataTypeDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.IndexTypeDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.LabelDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.LocalizableDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.NameDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.OptionsDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.PredefinedValueDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.ReadOnlyDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.RepeatableDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.RequiredDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.ShowLabelDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.TipDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.settings.TypeDDMFormFieldTypeSetting;
public abstract class BaseDDMFormFieldType implements DDMFormFieldType {

	@Override
	public String getFieldJavaScriptClass() {
		return "Liferay.Forms.FieldBase.getFieldClass()";
	}

	@Override
	public String getIcon() {
		return "my-icon";
	}

	@Override
	public List<DDMFormFieldTypeSetting> getRequiredSettings() {
		List<DDMFormFieldTypeSetting> requiredSettings = new ArrayList<>();

		requiredSettings.add(new DataTypeDDMFormFieldTypeSetting());
		requiredSettings.add(new IndexTypeDDMFormFieldTypeSetting());
		requiredSettings.add(new PredefinedValueDDMFormFieldTypeSetting());
		requiredSettings.add(new RepeatableDDMFormFieldTypeSetting());
		requiredSettings.add(new NameDDMFormFieldTypeSetting());
		requiredSettings.add(new LabelDDMFormFieldTypeSetting());
		requiredSettings.add(new TipDDMFormFieldTypeSetting());
		requiredSettings.add(new RequiredDDMFormFieldTypeSetting());
		requiredSettings.add(new ShowLabelDDMFormFieldTypeSetting());
		requiredSettings.add(new OptionsDDMFormFieldTypeSetting());
		requiredSettings.add(new TypeDDMFormFieldTypeSetting());
		requiredSettings.add(new LocalizableDDMFormFieldTypeSetting());
		requiredSettings.add(new ReadOnlyDDMFormFieldTypeSetting());

		return requiredSettings;
	}

}