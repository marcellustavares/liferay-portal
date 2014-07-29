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
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.BaseDDMTest;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Pablo Carvalho
 */
public class DDMFormLayoutJSONDeserializerTest extends BaseDDMTest {

	@Test
	public void testAllFieldsTypesDeserialization() throws Exception {
		String json = readJSON(
			"ddm-form-layout-json-deserializer-test-data.json");

		DDMFormLayoutJSONDeserializer deserializer =
			new DDMFormLayoutJSONDeserializerImpl();
		DDMForm ddmForm = deserializer.deserialize(json);

		testAvailableLocales(ddmForm);
		testDefaultLocale(ddmForm);

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(true);

		testNumberDDMFormField(ddmFormFieldsMap.get("Number4325"));
		testNestedDDMFormFields(ddmFormFieldsMap.get("Text5857"));
		testRadioDDMFormField(ddmFormFieldsMap.get("Radio4565"));
	}

	protected String readJSON(String fileName) throws IOException {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + fileName);

		return StringUtil.read(inputStream);
	}

	protected void testAvailableLocales(DDMForm ddmForm) {
		List<Locale> availableLocales = ddmForm.getAvailableLocales();

		Assert.assertEquals(2, availableLocales.size());

		Assert.assertTrue(availableLocales.contains(LocaleUtil.US));
		Assert.assertTrue(availableLocales.contains(LocaleUtil.BRAZIL));
	}

	protected void testDefaultLocale(DDMForm ddmForm) {
		Locale defaultLocale = ddmForm.getDefaultLocale();

		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		Assert.assertEquals("pt_BR", defaultLanguageId);
	}

	protected void testNestedDDMFormFields(DDMFormField ddmFormField) {
		Assert.assertNotNull(ddmFormField);

		List<DDMFormField> nestedDDMFormFields =
			ddmFormField.getNestedDDMFormFields();

		Assert.assertEquals(2, nestedDDMFormFields.size());

		DDMFormField integerDDMFormField = nestedDDMFormFields.get(0);

		Assert.assertEquals("Integer16693", integerDDMFormField.getName());

		DDMFormField separatorDDMFormField = nestedDDMFormFields.get(1);

		Assert.assertEquals("Separator6091", separatorDDMFormField.getName());

		nestedDDMFormFields = separatorDDMFormField.getNestedDDMFormFields();

		Assert.assertEquals(1, nestedDDMFormFields.size());

		DDMFormField textBoxDDMFormField = nestedDDMFormFields.get(0);

		Assert.assertEquals("Text_Box6326", textBoxDDMFormField.getName());
	}

	protected void testNumberDDMFormField(DDMFormField ddmFormField) {
		Assert.assertNotNull(ddmFormField);
		Assert.assertEquals("number", ddmFormField.getDataType());
		Assert.assertEquals("keyword", ddmFormField.getIndexType());

		LocalizedValue label = ddmFormField.getLabel();

		Assert.assertEquals("Number en_US", label.getValue(LocaleUtil.US));
		Assert.assertEquals("Number pt_BR", label.getValue(LocaleUtil.BRAZIL));

		Assert.assertTrue(ddmFormField.isLocalizable());
		Assert.assertEquals("Number4325", ddmFormField.getName());

		LocalizedValue predefinedValue = ddmFormField.getPredefinedValue();

		Assert.assertEquals("1", predefinedValue.getValue(LocaleUtil.US));
		Assert.assertEquals("2", predefinedValue.getValue(LocaleUtil.BRAZIL));

		Assert.assertEquals("ddm-number", ddmFormField.getType());

		Assert.assertFalse(ddmFormField.isRepeatable());
		Assert.assertFalse(ddmFormField.isRequired());

		List<DDMFormField> nestedDDMFormFields =
			ddmFormField.getNestedDDMFormFields();

		Assert.assertEquals(0, nestedDDMFormFields.size());
	}

	protected void testRadioDDMFormField(DDMFormField ddmFormField) {
		Assert.assertNotNull(ddmFormField);
		Assert.assertEquals("string", ddmFormField.getDataType());
		Assert.assertEquals("radio", ddmFormField.getType());

		LocalizedValue predefinedValue = ddmFormField.getPredefinedValue();

		Assert.assertEquals(
			"[\"value 1\"]", predefinedValue.getValue(LocaleUtil.US));

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormField.getDDMFormFieldOptions();

		Set<String> optionsValues = ddmFormFieldOptions.getOptionsValues();

		Assert.assertEquals(3, optionsValues.size());
		Assert.assertTrue(optionsValues.contains("value 1"));
		Assert.assertTrue(optionsValues.contains("value 2"));
		Assert.assertTrue(optionsValues.contains("value 3"));

		LocalizedValue value1Labels = ddmFormFieldOptions.getOptionLabels(
			"value 1");

		Assert.assertEquals("option 1", value1Labels.getValue(LocaleUtil.US));
		Assert.assertEquals(
			"opcao 1", value1Labels.getValue(LocaleUtil.BRAZIL));
	}

}