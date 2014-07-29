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

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;
import com.liferay.portlet.dynamicdatamapping.storage.Field;
import com.liferay.portlet.dynamicdatamapping.storage.FieldConstants;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;

import java.io.Serializable;

import java.util.Locale;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesToFieldsConverterImpl
	implements DDMFormValuesToFieldsConverter {

	public Fields convert(
			DDMStructure ddmStructure, DDMFormValues ddmFormValues)
		throws Exception {

		Fields ddmFields = new Fields();

		Field fieldsDisplayField = new Field(
			ddmStructure.getStructureId(), DDMImpl.FIELDS_DISPLAY_NAME,
			StringPool.BLANK);

		ddmFields.put(fieldsDisplayField);

		Locale defaultLocale = ddmFormValues.getDefaultLocale();

		for (DDMFormFieldValue ddmFormFieldValue :
				ddmFormValues.getDDMFormFieldValues()) {

			addDDMFields(
				ddmFormFieldValue, ddmStructure, ddmFields, defaultLocale);
		}

		return ddmFields;
	}

	protected void addDDMFields(
			DDMFormFieldValue ddmFormFieldValue, DDMStructure ddmStructure,
			Fields ddmFields, Locale defaultLocale)
		throws Exception {

		String name = ddmFormFieldValue.getName();
		String instanceId = ddmFormFieldValue.getInstanceId();

		if (!ddmStructure.hasField(name)) {
			return;
		}

		if (!ddmStructure.isFieldTransient(name)) {
			Field ddmField = createField(
				ddmFormFieldValue, ddmStructure, defaultLocale);

			String fieldName = ddmField.getName();

			Field existingDDMField = ddmFields.get(fieldName);

			if (existingDDMField != null) {
				for (Locale locale : ddmField.getAvailableLocales()) {
					existingDDMField.addValues(
						locale, ddmField.getValues(locale));
				}
			}
			else {
				ddmFields.put(ddmField);
			}
		}

		updateFieldsDisplayField(ddmFields, name, instanceId);

		for (DDMFormFieldValue nestedDDMFormFieldValue :
				ddmFormFieldValue.getNestedDDMFormFieldValues()) {

			addDDMFields(
				nestedDDMFormFieldValue, ddmStructure, ddmFields,
				defaultLocale);
		}
	}

	protected Field createField(
			DDMFormFieldValue ddmFormFieldValue, DDMStructure ddmStructure,
			Locale defaultLocale)
		throws Exception {

		Field ddmField = new Field();

		ddmField.setDDMStructureId(ddmStructure.getStructureId());
		ddmField.setDefaultLocale(defaultLocale);

		String name = ddmFormFieldValue.getName();

		ddmField.setName(name);

		String type = ddmStructure.getFieldType(name);

		Value value = ddmFormFieldValue.getValue();

		if (value.isLocalized()) {
			for (Locale locale : value.getAvailableLocales()) {
				Serializable serializable = FieldConstants.getSerializable(
					type, value.getValue(locale));

				ddmField.addValue(locale, serializable);
			}
		}
		else {
			Serializable serializable = FieldConstants.getSerializable(
				type, value.getValue(LocaleUtil.ROOT));

			ddmField.addValue(defaultLocale, serializable);
		}

		return ddmField;
	}

	protected void updateFieldsDisplayField(
		Fields ddmFields, String fieldName, String instanceId) {

		String fieldsDisplayValue = fieldName.concat(
			DDMImpl.INSTANCE_SEPARATOR).concat(instanceId);

		Field fieldsDisplayField = ddmFields.get(DDMImpl.FIELDS_DISPLAY_NAME);

		String[] fieldsDisplayValues = StringUtil.split(
			(String)fieldsDisplayField.getValue());

		fieldsDisplayValues = ArrayUtil.append(
			fieldsDisplayValues, fieldsDisplayValue);

		fieldsDisplayField.setValue(StringUtil.merge(fieldsDisplayValues));
	}

}