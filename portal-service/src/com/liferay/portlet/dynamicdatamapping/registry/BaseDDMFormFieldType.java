package com.liferay.portlet.dynamicdatamapping.registry;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDDMFormFieldType implements DDMFormFieldType {

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

		return requiredSettings;
	}

}
