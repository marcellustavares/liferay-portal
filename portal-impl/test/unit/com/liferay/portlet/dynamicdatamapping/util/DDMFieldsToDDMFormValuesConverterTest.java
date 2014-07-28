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

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.BaseDDMTestCase;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;
import com.liferay.portlet.dynamicdatamapping.storage.Field;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * @author Marcellus Tavares
 */
@PrepareForTest({DDMStructureLocalServiceUtil.class, LocaleUtil.class})
public class DDMFieldsToDDMFormValuesConverterTest extends BaseDDMTestCase {

	@Before
	public void setUp() throws Exception {
		setUpDDMFieldsToDDMFormValuesConverterUtil();
		setUpDDMFormXSDDeserializerUtil();
		setUpDDMFormXSDSerializerUtil();
		setUpDDMStructureLocalServiceUtil();
		setUpDDMUtil();
		setUpHtmlUtil();
		setUpLocaleUtil();
		setUpPropsUtil();
		setUpSAXReaderUtil();
	}

	@Test
	public void testConversionWithNestedFields() throws Exception {
		DDMForm ddmForm = createDDMForm();

		DDMFormField nameDDMFormField = createTextDDMFormField(
			"Name", "", true, false, false);

		List<DDMFormField> nestedNameDDMFormFields =
			nameDDMFormField.getNestedDDMFormFields();

		nestedNameDDMFormFields.add(
			createTextDDMFormField("Phone", "", true, false, false));

		addDDMFormFields(ddmForm, nameDDMFormField);

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		Field nameField = createField(
			ddmStructure.getStructureId(), "Name",
			createValuesList("Paul", "Joe"),
			createValuesList("Paulo", "Joao"));

		Field phoneField = createField(
			ddmStructure.getStructureId(), "Phone",
			createValuesList(
				"Paul's Phone 1", "Paul's Phone 2", "Joe's Phone 1",
				"Joe's Phone 2", "Joe's Phone 3"),
			createValuesList(
				"Telefone de Paulo 1", "Telefone de Paulo 2",
				"Telefone de Joao 1", "Telefone de Joao 2",
				"Telefone de Joao 3"));

		Field fieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(),
			"Name_INSTANCE_rztm,Phone_INSTANCE_ovho,Phone_INSTANCE_krvx," +
			"Name_INSTANCE_rght,Phone_INSTANCE_latb,Phone_INSTANCE_jewp," +
			"Phone_INSTANCE_mkar");

		Fields fields = createFields(nameField, phoneField, fieldsDisplay);

		DDMFormValues ddmFormValues =
			DDMFieldsToDDMFormValuesConverterUtil.convert(ddmStructure, fields);

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		Assert.assertEquals(2, ddmFormFieldValues.size());

		DDMFormFieldValue nameDDMFormFieldValue0 = ddmFormFieldValues.get(0);

		testLocalizedFieldValues(
			"Paul", "Paulo", nameDDMFormFieldValue0.getValue());

		List<DDMFormFieldValue> nestedDDMFormFieldValues =
			nameDDMFormFieldValue0.getNestedDDMFormFieldValues();

		Assert.assertEquals(2, nestedDDMFormFieldValues.size());

		DDMFormFieldValue phoneDDMFormFieldValue0 =
			nestedDDMFormFieldValues.get(0);

		testLocalizedFieldValues(
			"Paul's Phone 1", "Telefone de Paulo 1",
			phoneDDMFormFieldValue0.getValue());

		DDMFormFieldValue phoneDDMFormFieldValue1 =
			nestedDDMFormFieldValues.get(1);

		testLocalizedFieldValues(
			"Paul's Phone 2", "Telefone de Paulo 2",
			phoneDDMFormFieldValue1.getValue());

		DDMFormFieldValue nameDDMFormFieldValue1 = ddmFormFieldValues.get(1);

		testLocalizedFieldValues(
			"Joe", "Joao", nameDDMFormFieldValue1.getValue());

		nestedDDMFormFieldValues =
			nameDDMFormFieldValue1.getNestedDDMFormFieldValues();

		Assert.assertEquals(3, nestedDDMFormFieldValues.size());

		phoneDDMFormFieldValue0 = nestedDDMFormFieldValues.get(0);

		testLocalizedFieldValues(
			"Joe's Phone 1", "Telefone de Joao 1",
			phoneDDMFormFieldValue0.getValue());

		phoneDDMFormFieldValue1 = nestedDDMFormFieldValues.get(1);

		testLocalizedFieldValues(
			"Joe's Phone 2", "Telefone de Joao 2",
			phoneDDMFormFieldValue1.getValue());

		DDMFormFieldValue phoneDDMFormFieldValue2 =
			nestedDDMFormFieldValues.get(2);

		testLocalizedFieldValues(
			"Joe's Phone 3", "Telefone de Joao 3",
			phoneDDMFormFieldValue2.getValue());
	}

	@Test
	public void testConversionWithRegularField() throws Exception {
		DDMForm ddmForm = createDDMForm();

		addDDMFormFields(
			ddmForm, createTextDDMFormField("Title", "", true, false, false),
			createTextDDMFormField("Content", "", true, false, false));

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		Field titleField = createField(
			ddmStructure.getStructureId(), "Title",
			createValuesList("Title Example"),
			createValuesList("Titulo Exemplo"));

		Field contentField = createField(
			ddmStructure.getStructureId(), "Content",
			createValuesList("Content Example"),
			createValuesList("Conteudo Exemplo"));

		Field fieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(),
			"Title_INSTANCE_rztm,Content_INSTANCE_ovho");

		Fields fields = createFields(titleField, contentField, fieldsDisplay);

		DDMFormValues ddmFormValues =
			DDMFieldsToDDMFormValuesConverterUtil.convert(ddmStructure, fields);

		Map<String, List<Value>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Assert.assertEquals(2, ddmFormFieldValuesMap.size());

		testLocalizedFieldValues(
			titleField, ddmFormFieldValuesMap.get("Title"));

		testLocalizedFieldValues(
			contentField, ddmFormFieldValuesMap.get("Content"));
	}

	@Test
	public void testConversionWithRepeatableField() throws Exception {
		DDMForm ddmForm = createDDMForm();

		addDDMFormFields(
			ddmForm, createTextDDMFormField("Name", "", true, true, false));

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		Field nameField = createField(
			ddmStructure.getStructureId(), "Name",
			createValuesList("Name 1", "Name 2", "Name 3"),
			createValuesList("Nome 1", "Nome 2", "Nome 3"));

		Field fieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(),
			"Name_INSTANCE_rztm,Name_INSTANCE_ovho,Name_INSTANCE_iubr");

		Fields fields = createFields(nameField, fieldsDisplay);

		DDMFormValues ddmFormValues =
			DDMFieldsToDDMFormValuesConverterUtil.convert(ddmStructure, fields);

		Map<String, List<Value>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Assert.assertEquals(1, ddmFormFieldValuesMap.size());

		testLocalizedFieldValues(nameField, ddmFormFieldValuesMap.get("Name"));
	}

	protected Field createdFieldsDisplayField(
		long ddmStructureId, String value) {

		Field fieldsDisplayField = new MockField(
			ddmStructureId, DDMImpl.FIELDS_DISPLAY_NAME,
			createValuesList(value), LocaleUtil.US);

		fieldsDisplayField.setDefaultLocale(LocaleUtil.US);

		return fieldsDisplayField;
	}

	protected Field createField(
		long ddmStructureId, String fieldName, List<Serializable> enValues,
		List<Serializable> ptValues) {

		Map<Locale, List<Serializable>> valuesMap = createValuesMap(
			enValues, ptValues);

		return new MockField(
			ddmStructureId, fieldName, valuesMap, LocaleUtil.US);
	}

	protected Fields createFields(Field... fieldsArray) {
		Fields fields = new Fields();

		for (Field field : fieldsArray) {
			fields.put(field);
		}

		return fields;
	}

	protected List<Serializable> createValuesList(String... valuesString) {
		List<Serializable> values = new ArrayList<Serializable>();

		for (String valueString : valuesString) {
			values.add(valueString);
		}

		return values;
	}

	protected Map<Locale, List<Serializable>> createValuesMap(
		List<Serializable> enValues, List<Serializable> ptValues) {

		Map<Locale, List<Serializable>> valuesMap =
			new HashMap<Locale, List<Serializable>>();

		if (enValues != null) {
			valuesMap.put(LocaleUtil.US, enValues);
		}

		if (ptValues != null) {
			valuesMap.put(LocaleUtil.BRAZIL, ptValues);
		}

		return valuesMap;
	}

	protected void setUpDDMFieldsToDDMFormValuesConverterUtil() {
		DDMFieldsToDDMFormValuesConverterUtil
			ddmFieldsToDDMFormValuesConverterUtil =
				new DDMFieldsToDDMFormValuesConverterUtil();

		ddmFieldsToDDMFormValuesConverterUtil.
			setDDMFieldsToDDMFormValuesConverter(
				new DDMFieldsToDDMFormValuesConverterImpl());
	}

	protected void setUpDDMUtil() {
		DDMUtil ddmUtil = new DDMUtil();

		ddmUtil.setDDM(new DDMImpl());
	}

	protected void testLocalizedFieldValues(
		Field field, List<Value> actualValues) {

		testLocalizedValues(
			field.getValues(LocaleUtil.US), actualValues, LocaleUtil.US);

		testLocalizedValues(
			field.getValues(LocaleUtil.BRAZIL), actualValues,
			LocaleUtil.BRAZIL);
	}

	protected void testLocalizedFieldValues(
		String expectedEnValue, String expectedPtValue, Value actualValue) {

		Assert.assertEquals(
			expectedEnValue, actualValue.getValue(LocaleUtil.US));

		Assert.assertEquals(
			expectedPtValue, actualValue.getValue(LocaleUtil.BRAZIL));
	}

	protected void testLocalizedValues(
		List<Serializable> expectedValues, List<Value> actualValues,
		Locale locale) {

		Assert.assertEquals(expectedValues.size(), actualValues.size());

		for (int i = 0; i < expectedValues.size(); i++) {
			Value actualValue = actualValues.get(i);

			Assert.assertEquals(
				expectedValues.get(i), actualValue.getValue(locale));
		}
	}

	private class MockField extends Field {

		public MockField(
			long ddmStructureId, String name, List<Serializable> values,
			Locale locale) {

			super(ddmStructureId, name, values, locale);
		}

		public MockField(
			long ddmStructureId, String name,
			Map<Locale, List<Serializable>> valuesMap, Locale defaultLocale) {

			super(ddmStructureId, name, valuesMap, defaultLocale);
		}

		@Override
		public DDMStructure getDDMStructure() {
			return structures.get(getDDMStructureId());
		}

		private static final long serialVersionUID = 1L;

	}

}