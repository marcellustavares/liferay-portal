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

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;
import com.liferay.portlet.dynamicdatamapping.storage.Field;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class DDMFieldsToDDMFormValuesConverterImpl
	implements DDMFieldsToDDMFormValuesConverter {

	public DDMFormValues convert(DDMStructure ddmStructure, Fields ddmFields)
		throws Exception {

		DDMFormValues ddmFormValues = new DDMFormValues();

		DDMForm ddmForm = ddmStructure.getDDMForm();

		ddmFormValues.setAvailableLocales(ddmFields.getAvailableLocales());
		ddmFormValues.setDDMForm(ddmForm);
		ddmFormValues.setDefaultLocale(ddmFields.getDefaultLocale());

		DDMFieldsCounter ddmFieldsCounter = new DDMFieldsCounter();

		for (String fieldName : getParentDDMFormFieldNames(ddmForm)) {
			int repetitions = countFieldRepetition(
				ddmFields, fieldName, null, -1);

			for (int i = 0; i < repetitions; i++) {
				DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

				ddmFormFieldValue.setName(fieldName);

				setDDMFormFieldValueValues(
					ddmFormFieldValue, ddmForm, ddmFields, ddmFieldsCounter);

				ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);
			}
		}

		return ddmFormValues;
	}

	public List<String> getParentDDMFormFieldNames(DDMForm ddmForm) {
		List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

		return getDDMFormFieldNames(ddmFormFields);
	}

	protected int countFieldRepetition(
			Fields ddmFields, String fieldName, String parentFieldName,
			int parentOffset)
		throws Exception {

		Field fieldsDisplayField = ddmFields.get(DDMImpl.FIELDS_DISPLAY_NAME);

		String[] fieldsDisplayValues = DDMUtil.getFieldsDisplayValues(
			fieldsDisplayField);

		int offset = -1;

		int repetitions = 0;

		for (int i = 0; i < fieldsDisplayValues.length; i++) {
			String fieldDisplayName = fieldsDisplayValues[i];

			if (offset > parentOffset) {
				break;
			}

			if (fieldDisplayName.equals(parentFieldName)) {
				offset++;
			}

			if (fieldDisplayName.equals(fieldName) &&
				(offset == parentOffset)) {

				repetitions++;
			}
		}

		return repetitions;
	}

	protected DDMFormField getDDMFormField(DDMForm ddmForm, String fieldName) {
		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(true);

		return ddmFormFieldsMap.get(fieldName);
	}

	protected List<String> getDDMFormFieldNames(
		List<DDMFormField> ddmFormFields) {

		List<String> fieldNames = new ArrayList<String>();

		for (DDMFormField ddmFormField : ddmFormFields) {
			fieldNames.add(ddmFormField.getName());
		}

		return fieldNames;
	}

	protected List<String> getNestedDDMFormFieldNames(
		DDMForm ddmForm, String parentFieldName) {

		DDMFormField ddmFormField = getDDMFormField(ddmForm, parentFieldName);

		return getDDMFormFieldNames(ddmFormField.getNestedDDMFormFields());
	}

	protected boolean hasDDMFieldValues(
		DDMFormField ddmFormField, Field ddmField) {

		if (Validator.isNull(ddmFormField.getDataType()) ||
			(ddmField == null)) {

			return false;
		}

		return true;
	}

	protected void setDDMFormFieldValueLocalizedValue(
		DDMFormFieldValue ddmFormFieldValue, Field ddmField, int count) {

		Value value = new LocalizedValue(ddmField.getDefaultLocale());

		for (Locale locale : ddmField.getAvailableLocales()) {
			Serializable fieldValue = ddmField.getValue(locale, count);

			if (fieldValue instanceof Date) {
				Date valueDate = (Date)fieldValue;

				fieldValue = valueDate.getTime();
			}

			value.addValue(locale, String.valueOf(fieldValue));
		}

		ddmFormFieldValue.setValue(value);
	}

	protected void setDDMFormFieldValueUnlocalizedValue(
		DDMFormFieldValue ddmFormFieldValue, Field ddmField, int count) {

		Locale defaultLocale = ddmField.getDefaultLocale();

		Serializable fieldValue = ddmField.getValue(defaultLocale, count);

		if (fieldValue instanceof Date) {
			Date valueDate = (Date)fieldValue;

			fieldValue = valueDate.getTime();
		}

		Value value = new UnlocalizedValue(String.valueOf(fieldValue));

		ddmFormFieldValue.setValue(value);
	}

	protected void setDDMFormFieldValueValues(
			DDMFormFieldValue ddmFormFieldValue, DDMForm ddmForm,
			Fields ddmFields, DDMFieldsCounter ddmFieldsCounter)
		throws Exception {

		String fieldName = ddmFormFieldValue.getName();

		setNestedDDMFormFieldValues(
			ddmFormFieldValue, ddmForm, ddmFields, fieldName, ddmFieldsCounter);

		setDDMFormFieldValueValues(
			ddmFormFieldValue, ddmForm, ddmFields, fieldName, ddmFieldsCounter);
	}

	protected void setDDMFormFieldValueValues(
			DDMFormFieldValue ddmFormFieldValue, DDMForm ddmForm,
			Fields ddmFields, String fieldName,
			DDMFieldsCounter ddmFieldsCounter)
		throws Exception {

		DDMFormField ddmFormField = getDDMFormField(ddmForm, fieldName);

		int count = ddmFieldsCounter.get(fieldName);

		Field ddmField = ddmFields.get(fieldName);

		if (hasDDMFieldValues(ddmFormField, ddmField)) {
			if (ddmFormField.isLocalizable()) {
				setDDMFormFieldValueLocalizedValue(
					ddmFormFieldValue, ddmField, count);
			}
			else {
				setDDMFormFieldValueUnlocalizedValue(
					ddmFormFieldValue, ddmField, count);
			}
		}

		ddmFieldsCounter.incrementKey(fieldName);
	}

	protected void setNestedDDMFormFieldValues(
			DDMFormFieldValue ddmFormFieldValue, DDMForm ddmForm,
			Fields ddmFields, String fieldName,
			DDMFieldsCounter ddmFieldsCounter)
		throws Exception {

		for (String nestedFieldName :
				getNestedDDMFormFieldNames(ddmForm, fieldName)) {

			int count = ddmFieldsCounter.get(fieldName);

			int repetitions = countFieldRepetition(
				ddmFields, nestedFieldName, fieldName, count);

			for (int i = 0; i < repetitions; i++) {
				DDMFormFieldValue nestedDDMFormFieldValue =
					new DDMFormFieldValue();

				nestedDDMFormFieldValue.setName(nestedFieldName);

				setDDMFormFieldValueValues(
					nestedDDMFormFieldValue, ddmForm, ddmFields,
					ddmFieldsCounter);

				ddmFormFieldValue.addNestedDDMFormField(
					nestedDDMFormFieldValue);
			}
		}
	}

}