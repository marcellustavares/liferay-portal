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
import com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONSerializerImpl;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONSerializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import org.junit.Before;
import org.junit.Test;

import org.powermock.core.classloader.annotations.PrepareForTest;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Marcellus Tavares
 */
@PrepareForTest({LocaleUtil.class})
public class DDMFormValuesFactoryTest extends BaseDDMTestCase {

	@Before
	public void setUp() {
		setUpDDMFormValuesFactoryUtil();
		setUpDDMFormValuesJSONSerializerUtil();
		setUpLocaleUtil();
		setUpJSONFactoryUtil();
	}

	@Test
	public void testCreateWithComplexForm() throws Exception {
		DDMForm ddmForm = createDDMForm();

		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		nameDDMFormField.setDataType("string");
		nameDDMFormField.setRepeatable(true);

		DDMFormField phoneDDMFormField = new DDMFormField(
			"Phone", DDMFormFieldType.TEXT);

		phoneDDMFormField.setDataType("string");
		phoneDDMFormField.setRepeatable(true);

		DDMFormField extDDMFormField = new DDMFormField(
			"Ext", DDMFormFieldType.TEXT);

		extDDMFormField.setDataType("string");
		extDDMFormField.setRepeatable(true);

		phoneDDMFormField.addNestedDDMFormField(extDDMFormField);

		nameDDMFormField.addNestedDDMFormField(phoneDDMFormField);

		ddmForm.addDDMFormField(nameDDMFormField);

		DDMFormValues expectedDDMFormValues = createDDMFormValues(ddmForm);

		DDMFormFieldValue paulDDMFormFieldValue = createDDMFormFieldValue(
			"wqer", "Name", new UnlocalizedValue("Paul"));

		DDMFormFieldValue paulPhone1DDMFormFieldValue = createDDMFormFieldValue(
			"gatu", "Phone", new UnlocalizedValue("1"));

		paulPhone1DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"jkau", "Ext", new UnlocalizedValue("1.1")));

		paulPhone1DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"amat", "Ext", new UnlocalizedValue("1.2")));

		paulDDMFormFieldValue.addNestedDDMFormFieldValue(
			paulPhone1DDMFormFieldValue);

		DDMFormFieldValue paulPhone2DDMFormFieldValue = createDDMFormFieldValue(
			"hato", "Phone", new UnlocalizedValue("2"));

		paulPhone2DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"hamp", "Ext", new UnlocalizedValue("2.1")));

		paulPhone2DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"xzal", "Ext", new UnlocalizedValue("2.2")));

		paulPhone2DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"kaly", "Ext", new UnlocalizedValue("2.3")));

		paulDDMFormFieldValue.addNestedDDMFormFieldValue(
			paulPhone2DDMFormFieldValue);

		expectedDDMFormValues.addDDMFormFieldValue(paulDDMFormFieldValue);

		DDMFormFieldValue joeDDMFormFieldValue = createDDMFormFieldValue(
			"fahu", "Name", new UnlocalizedValue("Joe"));

		DDMFormFieldValue joePhone1DDMFormFieldValue = createDDMFormFieldValue(
			"jakl", "Phone", new UnlocalizedValue("3"));

		joePhone1DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"bagt", "Ext", new UnlocalizedValue("3.1")));

		joeDDMFormFieldValue.addNestedDDMFormFieldValue(
			joePhone1DDMFormFieldValue);

		expectedDDMFormValues.addDDMFormFieldValue(joeDDMFormFieldValue);

		MockHttpServletRequest httpServletRequest =
			new MockHttpServletRequest();

		httpServletRequest.addParameter("availableLanguageIds", "en_US");
		httpServletRequest.addParameter(
			"defaultLanguageId", LocaleUtil.toLanguageId(LocaleUtil.US));

		// Names

		httpServletRequest.addParameter("Name_INSTANCE_wqer_0_en_US", "Paul");
		httpServletRequest.addParameter("Name_INSTANCE_fahu_1_en_US", "Joe");

		// Phones

		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_gatu_0_en_US", "1");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_hato_1_en_US", "2");
		httpServletRequest.addParameter(
			"Name_INSTANCE_fahu_1__Phone_INSTANCE_jakl_0_en_US", "3");

		// Ext

		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_gatu_0__Ext_INSTANCE_jkau_0_en_US",
			"1.1");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_gatu_0__Ext_INSTANCE_amat_1_en_US",
			"1.2");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_hato_1__Ext_INSTANCE_hamp_0_en_US",
			"2.1");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_hato_1__Ext_INSTANCE_xzal_1_en_US",
			"2.2");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_hato_1__Ext_INSTANCE_kaly_2_en_US",
			"2.3");
		httpServletRequest.addParameter(
			"Name_INSTANCE_fahu_1__Phone_INSTANCE_jakl_0__Ext_INSTANCE_bagt_0_en_US",
			"3.1");

		DDMFormValues actualDDMFormValues = DDMFormValuesFactoryUtil.create(
			ddmForm, httpServletRequest);

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testCreateWithLocalizableFields() throws Exception {
		DDMForm ddmForm = createDDMForm("Title", "Content");

		DDMFormValues expectedDDMFormValues = createDDMFormValues(
			ddmForm, createAvailableLocales(LocaleUtil.BRAZIL, LocaleUtil.US),
			LocaleUtil.US);

		expectedDDMFormValues.addDDMFormFieldValue(
			createDDMFormFieldValue(
				"wqer", "Title",
				createLocalizedValue("Title", "Titulo", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(
			createDDMFormFieldValue(
				"thsy", "Content",
				createLocalizedValue("Content", "Conteudo", LocaleUtil.US)));

		MockHttpServletRequest httpServletRequest =
			new MockHttpServletRequest();

		httpServletRequest.addParameter("availableLanguageIds", "en_US,pt_BR");
		httpServletRequest.addParameter(
			"defaultLanguageId", LocaleUtil.toLanguageId(LocaleUtil.US));

		httpServletRequest.addParameter("Title_INSTANCE_wqer_0_en_US", "Title");
		httpServletRequest.addParameter(
			"Title_INSTANCE_wqer_0_pt_BR", "Titulo");
		httpServletRequest.addParameter(
			"Content_INSTANCE_thsy_0_en_US", "Content");
		httpServletRequest.addParameter(
			"Content_INSTANCE_thsy_0_pt_BR", "Conteudo");

		DDMFormValues actualDDMFormValues = DDMFormValuesFactoryUtil.create(
			ddmForm, httpServletRequest);

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testCreateWithParentAndChildRepeatable() throws Exception {
		DDMForm ddmForm = createDDMForm();

		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		nameDDMFormField.setDataType("string");
		nameDDMFormField.setLocalizable(true);
		nameDDMFormField.setRepeatable(true);

		DDMFormField phoneDDMFormField = new DDMFormField(
			"Phone", DDMFormFieldType.TEXT);

		phoneDDMFormField.setDataType("string");
		phoneDDMFormField.setLocalizable(true);
		phoneDDMFormField.setRepeatable(true);

		nameDDMFormField.addNestedDDMFormField(phoneDDMFormField);

		ddmForm.addDDMFormField(nameDDMFormField);

		DDMFormValues expectedDDMFormValues = createDDMFormValues(
			ddmForm, createAvailableLocales(LocaleUtil.BRAZIL, LocaleUtil.US),
			LocaleUtil.US);

		DDMFormFieldValue paulDDMFormFieldValue = createDDMFormFieldValue(
			"wqer", "Name",
			createLocalizedValue("Paul", "Paulo", LocaleUtil.US));

		paulDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"gatu", "Phone",
				createLocalizedValue("12", "34", LocaleUtil.US)));

		paulDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"hato", "Phone",
				createLocalizedValue("56", "78", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(paulDDMFormFieldValue);

		DDMFormFieldValue joeDDMFormFieldValue = createDDMFormFieldValue(
			"fahu", "Name",
			createLocalizedValue("Joe", "Joao", LocaleUtil.US));

		joeDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"jamh", "Phone",
				createLocalizedValue("90", "01", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(joeDDMFormFieldValue);

		MockHttpServletRequest httpServletRequest =
			new MockHttpServletRequest();

		httpServletRequest.addParameter("availableLanguageIds", "en_US,pt_BR");
		httpServletRequest.addParameter(
			"defaultLanguageId", LocaleUtil.toLanguageId(LocaleUtil.US));

		httpServletRequest.addParameter("Name_INSTANCE_wqer_0_en_US", "Paul");
		httpServletRequest.addParameter("Name_INSTANCE_wqer_0_pt_BR", "Paulo");

		httpServletRequest.addParameter("Name_INSTANCE_fahu_1_en_US", "Joe");
		httpServletRequest.addParameter("Name_INSTANCE_fahu_1_pt_BR", "Joao");

		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_gatu_0_en_US", "12");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_gatu_0_pt_BR", "34");

		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_hato_1_en_US", "56");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Phone_INSTANCE_hato_1_pt_BR", "78");

		httpServletRequest.addParameter(
			"Name_INSTANCE_fahu_1__Phone_INSTANCE_jamh_0_en_US", "90");
		httpServletRequest.addParameter(
			"Name_INSTANCE_fahu_1__Phone_INSTANCE_jamh_0_pt_BR", "01");

		DDMFormValues actualDDMFormValues = DDMFormValuesFactoryUtil.create(
			ddmForm, httpServletRequest);

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testCreateWithParentAndTwoChildRepeatables() throws Exception {
		DDMForm ddmForm = createDDMForm();

		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		nameDDMFormField.setDataType("string");
		nameDDMFormField.setLocalizable(true);
		nameDDMFormField.setRepeatable(true);

		DDMFormField text1DDMFormField = new DDMFormField(
			"Text1", DDMFormFieldType.TEXT);

		text1DDMFormField.setDataType("string");
		text1DDMFormField.setLocalizable(true);
		text1DDMFormField.setRepeatable(true);

		nameDDMFormField.addNestedDDMFormField(text1DDMFormField);

		DDMFormField text2DDMFormField = new DDMFormField(
			"Text2", DDMFormFieldType.TEXT);

		text2DDMFormField.setDataType("string");
		text2DDMFormField.setLocalizable(true);
		text2DDMFormField.setRepeatable(true);

		nameDDMFormField.addNestedDDMFormField(text2DDMFormField);

		ddmForm.addDDMFormField(nameDDMFormField);

		DDMFormValues expectedDDMFormValues = createDDMFormValues(
			ddmForm, createAvailableLocales(LocaleUtil.BRAZIL, LocaleUtil.US),
			LocaleUtil.US);

		DDMFormFieldValue paulDDMFormFieldValue = createDDMFormFieldValue(
			"wqer", "Name",
			createLocalizedValue("Paul", "Paulo", LocaleUtil.US));

		paulDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"gatu", "Text1",
				createLocalizedValue(
					"Text1 Paul One", "Text1 Paulo Um", LocaleUtil.US)));

		paulDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"hayt", "Text1",
				createLocalizedValue(
					"Text1 Paul Two", "Text1 Paulo Dois", LocaleUtil.US)));

		paulDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"haby", "Text2",
				createLocalizedValue(
					"Text2 Paul One", "Text2 Paulo Um", LocaleUtil.US)));

		paulDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"makp", "Text2",
				createLocalizedValue(
					"Text2 Paul Two", "Text2 Paulo Dois", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(paulDDMFormFieldValue);

		DDMFormFieldValue joeDDMFormFieldValue = createDDMFormFieldValue(
			"fahu", "Name",
			createLocalizedValue("Joe", "Joao", LocaleUtil.US));

		joeDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"banm", "Text1",
				createLocalizedValue(
					"Text1 Joe One", "Text1 Joao Um", LocaleUtil.US)));

		joeDDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"bagj", "Text2",
				createLocalizedValue(
					"Text2 Joe One", "Text2 Joao Um", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(joeDDMFormFieldValue);

		MockHttpServletRequest httpServletRequest =
			new MockHttpServletRequest();

		httpServletRequest.addParameter("availableLanguageIds", "en_US,pt_BR");
		httpServletRequest.addParameter(
			"defaultLanguageId", LocaleUtil.toLanguageId(LocaleUtil.US));

		httpServletRequest.addParameter("Name_INSTANCE_wqer_0_en_US", "Paul");
		httpServletRequest.addParameter("Name_INSTANCE_wqer_0_pt_BR", "Paulo");

		httpServletRequest.addParameter("Name_INSTANCE_fahu_1_en_US", "Joe");
		httpServletRequest.addParameter("Name_INSTANCE_fahu_1_pt_BR", "Joao");

		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Text1_INSTANCE_gatu_0_en_US",
			"Text1 Paul One");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Text1_INSTANCE_gatu_0_pt_BR",
			"Text1 Paulo Um");

		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Text1_INSTANCE_hayt_1_en_US",
			"Text1 Paul Two");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Text1_INSTANCE_hayt_1_pt_BR",
			"Text1 Paulo Dois");

		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Text2_INSTANCE_haby_0_en_US",
			"Text2 Paul One");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Text2_INSTANCE_haby_0_pt_BR",
			"Text2 Paulo Um");

		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Text2_INSTANCE_makp_1_en_US",
			"Text2 Paul Two");
		httpServletRequest.addParameter(
			"Name_INSTANCE_wqer_0__Text2_INSTANCE_makp_1_pt_BR",
			"Text2 Paulo Dois");

		httpServletRequest.addParameter(
			"Name_INSTANCE_fahu_1__Text1_INSTANCE_banm_0_en_US",
			"Text1 Joe One");
		httpServletRequest.addParameter(
			"Name_INSTANCE_fahu_1__Text1_INSTANCE_banm_0_pt_BR",
			"Text1 Joao Um");

		httpServletRequest.addParameter(
			"Name_INSTANCE_fahu_1__Text2_INSTANCE_bagj_0_en_US",
			"Text2 Joe One");
		httpServletRequest.addParameter(
			"Name_INSTANCE_fahu_1__Text2_INSTANCE_bagj_0_pt_BR",
			"Text2 Joao Um");

		DDMFormValues actualDDMFormValues = DDMFormValuesFactoryUtil.create(
			ddmForm, httpServletRequest);

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testCreateWithRepeatableAndLocalizableFields()
		throws Exception {

		DDMForm ddmForm = createDDMForm();

		DDMFormField ddmFormField = new DDMFormField(
			"Title", DDMFormFieldType.TEXT);

		ddmFormField.setDataType("string");
		ddmFormField.setLocalizable(true);
		ddmFormField.setRepeatable(true);

		ddmForm.addDDMFormField(ddmFormField);

		DDMFormValues expectedDDMFormValues = createDDMFormValues(
			ddmForm, createAvailableLocales(LocaleUtil.BRAZIL, LocaleUtil.US),
			LocaleUtil.US);

		expectedDDMFormValues.addDDMFormFieldValue(
			createDDMFormFieldValue(
				"wqer", "Title",
				createLocalizedValue("Title 1", "Titulo 1", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(
			createDDMFormFieldValue(
				"fahu", "Title",
				createLocalizedValue("Title 2", "Titulo 2", LocaleUtil.US)));

		MockHttpServletRequest httpServletRequest =
			new MockHttpServletRequest();

		httpServletRequest.addParameter("availableLanguageIds", "en_US,pt_BR");
		httpServletRequest.addParameter(
			"defaultLanguageId", LocaleUtil.toLanguageId(LocaleUtil.US));

		httpServletRequest.addParameter(
			"Title_INSTANCE_wqer_0_en_US", "Title 1");
		httpServletRequest.addParameter(
			"Title_INSTANCE_wqer_0_pt_BR", "Titulo 1");

		httpServletRequest.addParameter(
			"Title_INSTANCE_fahu_1_en_US", "Title 2");
		httpServletRequest.addParameter(
			"Title_INSTANCE_fahu_1_pt_BR", "Titulo 2");

		DDMFormValues actualDDMFormValues = DDMFormValuesFactoryUtil.create(
			ddmForm, httpServletRequest);

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testCreateWithTransientParentRepeatable() throws Exception {
		DDMForm ddmForm = createDDMForm();

		DDMFormField separatorDDMFormField = new DDMFormField(
			"Separator", DDMFormFieldType.SEPARATOR);

		separatorDDMFormField.setRepeatable(true);

		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		nameDDMFormField.setDataType("string");
		nameDDMFormField.setLocalizable(true);

		separatorDDMFormField.addNestedDDMFormField(nameDDMFormField);

		ddmForm.addDDMFormField(separatorDDMFormField);

		DDMFormValues expectedDDMFormValues = createDDMFormValues(
			ddmForm, createAvailableLocales(LocaleUtil.BRAZIL, LocaleUtil.US),
			LocaleUtil.US);

		DDMFormFieldValue separator1DDMFormFieldValue = createDDMFormFieldValue(
			"wqer", "Separator", null);

		separator1DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"gatu", "Name",
				createLocalizedValue("Joe", "Joao", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(separator1DDMFormFieldValue);

		DDMFormFieldValue separator2DDMFormFieldValue = createDDMFormFieldValue(
			"haby", "Separator", null);

		separator2DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"hato", "Name",
				createLocalizedValue("Paul", "Paulo", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(separator2DDMFormFieldValue);

		DDMFormFieldValue separator3DDMFormFieldValue = createDDMFormFieldValue(
			"bajk", "Separator", null);

		separator3DDMFormFieldValue.addNestedDDMFormFieldValue(
			createDDMFormFieldValue(
				"fahu", "Name",
				createLocalizedValue("Claude", "Claudio", LocaleUtil.US)));

		expectedDDMFormValues.addDDMFormFieldValue(separator3DDMFormFieldValue);

		MockHttpServletRequest httpServletRequest =
			new MockHttpServletRequest();

		httpServletRequest.addParameter("availableLanguageIds", "en_US,pt_BR");
			httpServletRequest.addParameter(
				"defaultLanguageId", LocaleUtil.toLanguageId(LocaleUtil.US));

		httpServletRequest.addParameter(
			"Separator_INSTANCE_wqer_0__Name_INSTANCE_gatu_0_en_US", "Joe");
		httpServletRequest.addParameter(
			"Separator_INSTANCE_wqer_0__Name_INSTANCE_gatu_0_pt_BR", "Joao");
		httpServletRequest.addParameter(
			"Separator_INSTANCE_haby_1__Name_INSTANCE_hato_0_en_US", "Paul");
		httpServletRequest.addParameter(
			"Separator_INSTANCE_haby_1__Name_INSTANCE_hato_0_pt_BR", "Paulo");
		httpServletRequest.addParameter(
			"Separator_INSTANCE_bajk_2__Name_INSTANCE_fahu_0_en_US", "Claude");
		httpServletRequest.addParameter(
			"Separator_INSTANCE_bajk_2__Name_INSTANCE_fahu_0_pt_BR", "Claudio");

		DDMFormValues actualDDMFormValues = DDMFormValuesFactoryUtil.create(
			ddmForm, httpServletRequest);

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	protected void assertEquals(
			DDMFormValues expectedDDMFormValues,
			DDMFormValues actualDDMFormValues)
		throws Exception {

		String serializedExpectedDDMFormValues =
			DDMFormValuesJSONSerializerUtil.serialize(expectedDDMFormValues);

		System.out.println(serializedExpectedDDMFormValues);

		String serializedActualDDMFormValues =
			DDMFormValuesJSONSerializerUtil.serialize(actualDDMFormValues);

		System.out.println(serializedActualDDMFormValues);

		JSONAssert.assertEquals(
			serializedExpectedDDMFormValues, serializedActualDDMFormValues,
			false);
	}

	protected void setUpDDMFormValuesFactoryUtil() {
		DDMFormValuesFactoryUtil ddmFormValuesFactoryUtil =
			new DDMFormValuesFactoryUtil();

		ddmFormValuesFactoryUtil.setDDMFormValuesFactory(
			new DDMFormValuesFactoryImpl());
	}

	protected void setUpDDMFormValuesJSONSerializerUtil() {
		DDMFormValuesJSONSerializerUtil ddmFormValuesJSONSerializerUtil =
			new DDMFormValuesJSONSerializerUtil();

		ddmFormValuesJSONSerializerUtil.setDDMFormValuesJSONSerializer(
			new DDMFormValuesJSONSerializerImpl());
	}

}