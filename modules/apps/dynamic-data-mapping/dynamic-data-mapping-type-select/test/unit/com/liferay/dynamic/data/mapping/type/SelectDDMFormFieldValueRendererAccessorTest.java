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

package com.liferay.dynamic.data.mapping.type;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMFormTestUtil;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMFormValuesTestUtil;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Renato Rego
 */
public class SelectDDMFormFieldValueRendererAccessorTest {

	@Before
	public void setUp() {
		setUpJSONFactoryUtil();

		availableLocales = new HashSet<>();

		availableLocales.add(LocaleUtil.US);

		defaultLocale = LocaleUtil.US;
	}

	public void setUpJSONFactoryUtil() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

	@Test
	public void testGetSelectMultipleRenderedValue() throws Exception {
		int numberOfOptions = 2;

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			availableLocales, defaultLocale);

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Select", "Select", "select", "string", false, false, false);

		DDMFormFieldOptions ddmFormFieldOptions = createDDMFormFieldOptions(
			numberOfOptions);

		ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);

		ddmForm.addDDMFormField(ddmFormField);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm, availableLocales, defaultLocale);

		JSONArray optionsValues = createOptionsValuesJSONArray(numberOfOptions);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Select", new UnlocalizedValue(optionsValues.toString()));

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		SelectDDMFormFieldValueRendererAccessor
			selectDDMFormFieldValueRendererAccessor =
				createSelectDDMFormFieldValueRendererAccessor(defaultLocale);

		Assert.assertEquals(
			"option 1, option 2",
			selectDDMFormFieldValueRendererAccessor.get(ddmFormFieldValue));
	}

	@Test
	public void testGetSelectSingleRenderedValue() throws Exception {
		int numberOfOptions = 1;

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			availableLocales, defaultLocale);

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Select", "Select", "select", "string", false, false, false);

		DDMFormFieldOptions ddmFormFieldOptions = createDDMFormFieldOptions(
			numberOfOptions);

		ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);

		ddmForm.addDDMFormField(ddmFormField);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm, availableLocales, defaultLocale);

		JSONArray optionsValues = createOptionsValuesJSONArray(numberOfOptions);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Select", new UnlocalizedValue(optionsValues.toString()));

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		SelectDDMFormFieldValueRendererAccessor
			selectDDMFormFieldValueRendererAccessor =
				createSelectDDMFormFieldValueRendererAccessor(defaultLocale);

		Assert.assertEquals(
			"option 1",
			selectDDMFormFieldValueRendererAccessor.get(ddmFormFieldValue));
	}

	protected DDMFormFieldOptions createDDMFormFieldOptions(
		int numberOfOptions) {

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		for (int i = 1; i <= numberOfOptions; i++) {
			ddmFormFieldOptions.addOption("value " + i);
			ddmFormFieldOptions.addOptionLabel(
				"value " + i, defaultLocale, "option " + i);
		}

		return ddmFormFieldOptions;
	}

	protected JSONArray createOptionsValuesJSONArray(
			int numberOfJSONArrayElements)
		throws Exception {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (int i = 1; i <= numberOfJSONArrayElements; i++) {
			jsonArray.put("value " + i);
		}

		return jsonArray;
	}

	protected SelectDDMFormFieldValueRendererAccessor
		createSelectDDMFormFieldValueRendererAccessor(Locale locale) {

		SelectDDMFormFieldValueAccessor selectDDMFormFieldValueAccessor =
			new SelectDDMFormFieldValueAccessor(locale);

		return new SelectDDMFormFieldValueRendererAccessor(
			selectDDMFormFieldValueAccessor);
	}

	protected Set<Locale> availableLocales;
	protected Locale defaultLocale;

}