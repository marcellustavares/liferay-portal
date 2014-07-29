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
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;
import com.liferay.portlet.dynamicdatamapping.storage.Field;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * @author Marcellus Tavares
 */
@PrepareForTest({DDMStructureLocalServiceUtil.class, LocaleUtil.class})
public class DDMFormValuesToFieldsConverterTest extends BaseDDMTestCase {

	@Before
	public void setUp() throws Exception {
		setUpAvailableLocales();
		setUpDDMFormValuesToFieldsConverterUtil();
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

		DDMFormValues ddmFormValues = createDDMFormValues(
			ddmForm, createAvailableLocales(LocaleUtil.BRAZIL, LocaleUtil.US),
			LocaleUtil.US);

		DDMFormFieldValue name1DDMFormFieldValue = createDDMFormFieldValue(
			"rztm", "Name",
			createLocalizedValue("Paul", "Paulo", LocaleUtil.US));

		List<DDMFormFieldValue> name1NestedDDMFormFieldValue =
			name1DDMFormFieldValue.getNestedDDMFormFieldValues();

		DDMFormFieldValue name1phone1DDMFormFieldValue =
			createDDMFormFieldValue(
				"ovho", "Phone",
				createLocalizedValue(
					"Paul's Phone 1", "Telefone de Paulo 1", LocaleUtil.US));

		name1NestedDDMFormFieldValue.add(name1phone1DDMFormFieldValue);

		DDMFormFieldValue name1phone2DDMFormFieldValue =
			createDDMFormFieldValue(
				"krvx", "Phone",
				createLocalizedValue(
					"Paul's Phone 2", "Telefone de Paulo 2", LocaleUtil.US));

		name1NestedDDMFormFieldValue.add(name1phone2DDMFormFieldValue);

		ddmFormValues.addDDMFormFieldValue(name1DDMFormFieldValue);

		DDMFormFieldValue name2DDMFormFieldValue = createDDMFormFieldValue(
			"rght", "Name",
			createLocalizedValue("Joe", "Joao", LocaleUtil.US));

		List<DDMFormFieldValue> name2NestedDDMFormFieldValue =
			name2DDMFormFieldValue.getNestedDDMFormFieldValues();

		DDMFormFieldValue name2phone1DDMFormFieldValue =
			createDDMFormFieldValue(
				"latb", "Phone",
				createLocalizedValue(
					"Joe's Phone 1", "Telefone de Joao 1", LocaleUtil.US));

		name2NestedDDMFormFieldValue.add(name2phone1DDMFormFieldValue);

		DDMFormFieldValue name2phone2DDMFormFieldValue =
			createDDMFormFieldValue(
				"jewp", "Phone",
				createLocalizedValue(
					"Joe's Phone 2", "Telefone de Joao 2", LocaleUtil.US));

		name2NestedDDMFormFieldValue.add(name2phone2DDMFormFieldValue);

		DDMFormFieldValue name2phone3DDMFormFieldValue =
			createDDMFormFieldValue(
				"mkar", "Phone",
				createLocalizedValue(
					"Joe's Phone 3", "Telefone de Joao 3", LocaleUtil.US));

		name2NestedDDMFormFieldValue.add(name2phone3DDMFormFieldValue);

		ddmFormValues.addDDMFormFieldValue(name2DDMFormFieldValue);

		Fields fields = DDMFormValuesToFieldsConverterUtil.convert(
			ddmStructure, ddmFormValues);

		Assert.assertNotNull(fields);

		Field nameField = fields.get("Name");

		testField(
			nameField, createValuesList("Paul", "Joe"),
			createValuesList("Paulo", "Joao"), _availableLocales,
			LocaleUtil.US);

		Field phoneField = fields.get("Phone");

		testField(
			phoneField,
			createValuesList(
				"Paul's Phone 1", "Paul's Phone 2", "Joe's Phone 1",
				"Joe's Phone 2", "Joe's Phone 3"),
			createValuesList(
				"Telefone de Paulo 1", "Telefone de Paulo 2",
				"Telefone de Joao 1", "Telefone de Joao 2",
				"Telefone de Joao 3"),
			_availableLocales, LocaleUtil.US);

		Field fieldsDisplayField = fields.get(DDMImpl.FIELDS_DISPLAY_NAME);

		Assert.assertEquals(
			"Name_INSTANCE_rztm,Phone_INSTANCE_ovho,Phone_INSTANCE_krvx," +
			"Name_INSTANCE_rght,Phone_INSTANCE_latb,Phone_INSTANCE_jewp," +
			"Phone_INSTANCE_mkar", fieldsDisplayField.getValue());
	}

	@Test
	public void testConversionWithRegularField() throws Exception {
		DDMForm ddmForm = createDDMForm();

		addDDMFormFields(
			ddmForm, createTextDDMFormField("Title", "", true, false, false),
			createTextDDMFormField("Content", "", true, false, false));

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		DDMFormValues ddmFormValues = createDDMFormValues(
			ddmForm, createAvailableLocales(LocaleUtil.BRAZIL, LocaleUtil.US),
			LocaleUtil.US);

		DDMFormFieldValue titleDDMFormFieldValue = createDDMFormFieldValue(
			"rztm", "Title",
			createLocalizedValue(
				"Title Example", "Titulo Exemplo", LocaleUtil.US));

		ddmFormValues.addDDMFormFieldValue(titleDDMFormFieldValue);

		DDMFormFieldValue contentDDMFormFieldValue = createDDMFormFieldValue(
			"ovho", "Content",
			createLocalizedValue(
				"Content Example", "Conteudo Exemplo", LocaleUtil.US));

		ddmFormValues.addDDMFormFieldValue(contentDDMFormFieldValue);

		Fields fields = DDMFormValuesToFieldsConverterUtil.convert(
			ddmStructure, ddmFormValues);

		Assert.assertNotNull(fields);

		Field titleField = fields.get("Title");

		testField(
			titleField, createValuesList("Title Example"),
			createValuesList("Titulo Exemplo"), _availableLocales,
			LocaleUtil.US);

		Field contentField = fields.get("Content");

		testField(
			contentField, createValuesList("Content Example"),
			createValuesList("Conteudo Exemplo"), _availableLocales,
			LocaleUtil.US);

		Field fieldsDisplayField = fields.get(DDMImpl.FIELDS_DISPLAY_NAME);

		Assert.assertEquals(
			"Title_INSTANCE_rztm,Content_INSTANCE_ovho",
			fieldsDisplayField.getValue());
	}

	@Test
	public void testConversionWithRepeatableField() throws Exception {
		DDMForm ddmForm = createDDMForm();

		addDDMFormFields(
			ddmForm, createTextDDMFormField("Name", "", true, true, false));

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		DDMFormValues ddmFormValues = createDDMFormValues(
			ddmForm, createAvailableLocales(LocaleUtil.BRAZIL, LocaleUtil.US),
			LocaleUtil.US);

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		DDMFormFieldValue nameDDMFormFieldValue1 = createDDMFormFieldValue(
			"rztm", "Name",
			createLocalizedValue("Name 1", "Nome 1", LocaleUtil.US));

		ddmFormFieldValues.add(nameDDMFormFieldValue1);

		DDMFormFieldValue nameDDMFormFieldValue2 = createDDMFormFieldValue(
			"uayd", "Name",
			createLocalizedValue("Name 2", "Nome 2", LocaleUtil.US));

		ddmFormFieldValues.add(nameDDMFormFieldValue2);

		DDMFormFieldValue nameDDMFormFieldValue3 = createDDMFormFieldValue(
			"pamh", "Name",
			createLocalizedValue("Name 3", "Nome 3", LocaleUtil.US));

		ddmFormFieldValues.add(nameDDMFormFieldValue3);

		Fields fields = DDMFormValuesToFieldsConverterUtil.convert(
			ddmStructure, ddmFormValues);

		Assert.assertNotNull(fields);

		Field nameField = fields.get("Name");

		testField(
			nameField, createValuesList("Name 1", "Name 2", "Name 3"),
			createValuesList("Nome 1", "Nome 2", "Nome 3"), _availableLocales,
			LocaleUtil.US);

		Field fieldsDisplayField = fields.get(DDMImpl.FIELDS_DISPLAY_NAME);

		Assert.assertEquals(
			"Name_INSTANCE_rztm,Name_INSTANCE_uayd,Name_INSTANCE_pamh",
			fieldsDisplayField.getValue());
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

	protected Value createLocalizedValue(
		String enValue, String ptValue, Locale defaultLocale) {

		Value value = new LocalizedValue(defaultLocale);

		value.addValue(LocaleUtil.BRAZIL, ptValue);
		value.addValue(LocaleUtil.US, enValue);

		return value;
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

	protected void setUpAvailableLocales() {
		_availableLocales.add(LocaleUtil.BRAZIL);
		_availableLocales.add(LocaleUtil.US);
	}

	protected void setUpDDMFormValuesToFieldsConverterUtil() {
		DDMFormValuesToFieldsConverterUtil ddmFormValuesToFieldsConverterUtil =
			new DDMFormValuesToFieldsConverterUtil();

		ddmFormValuesToFieldsConverterUtil.setDDMFormValuesToFieldsConverter(
			new DDMFormValuesToFieldsConverterImpl());
	}

	protected void setUpDDMUtil() {
		DDMUtil ddmUtil = new DDMUtil();

		ddmUtil.setDDM(new DDMImpl());
	}

	protected void testField(
		Field field, List<Serializable> expectedEnValues,
		List<Serializable> expectedPtValues,
		Set<Locale> expectedAvailableLocales, Locale expectedDefaultLocale) {

		Assert.assertNotNull(field);
		Assert.assertEquals(
			expectedAvailableLocales, field.getAvailableLocales());
		Assert.assertEquals(expectedEnValues, field.getValues(LocaleUtil.US));
		Assert.assertEquals(
			expectedPtValues, field.getValues(LocaleUtil.BRAZIL));
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

	private Set<Locale> _availableLocales = new HashSet<Locale>();

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