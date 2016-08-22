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

package com.liferay.dynamic.data.mapping.type.checkbox.multiple;

import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rafael Praxedes
 */
public class CheckboxMultipleDDMFormFieldContextHelperTest {

	@Test
	public void testGetOptionsWithNoCheckedValueAndOnePredefinedValue() {
		List<Object> expectedOptions = new ArrayList<>();

		expectedOptions.add(createUncheckedOption("Label 1", "value 1"));
		expectedOptions.add(createUncheckedOption("Label 2", "value 2"));
		expectedOptions.add(createCheckedOption("Label 3", "value 3"));

		DDMFormFieldOptions ddmFormFieldOptions = createDDMFormFieldOptions();

		List<Object> actualOptions = getActualOptions(
			ddmFormFieldOptions, createValue(),
			createPredefinedValue("value 3"), LocaleUtil.US);

		Assert.assertEquals(expectedOptions, actualOptions);
	}

	@Test
	public void testGetOptionsWithNoCheckedValueAndTwoPredefinedValues() {
		List<Object> expectedOptions = new ArrayList<>();

		expectedOptions.add(createCheckedOption("Label 1", "value 1"));
		expectedOptions.add(createUncheckedOption("Label 2", "value 2"));
		expectedOptions.add(createCheckedOption("Label 3", "value 3"));

		DDMFormFieldOptions ddmFormFieldOptions = createDDMFormFieldOptions();

		List<Object> actualOptions = getActualOptions(
			ddmFormFieldOptions, createValue(),
			createPredefinedValue("value 1", "value 3"), LocaleUtil.US);

		Assert.assertEquals(expectedOptions, actualOptions);
	}

	@Test
	public void testGetOptionsWithOneCheckedValueAndNoPredefinedValue() {
		List<Object> expectedOptions = new ArrayList<>();

		expectedOptions.add(createUncheckedOption("Label 1", "value 1"));
		expectedOptions.add(createCheckedOption("Label 2", "value 2"));
		expectedOptions.add(createUncheckedOption("Label 3", "value 3"));

		DDMFormFieldOptions ddmFormFieldOptions = createDDMFormFieldOptions();

		List<Object> actualOptions = getActualOptions(
			ddmFormFieldOptions, createValue("value 2"),
			createPredefinedValue(), LocaleUtil.US);

		Assert.assertEquals(expectedOptions, actualOptions);
	}

	@Test
	public void testGetOptionsWithOneCheckedValueAndTwoPredefinedValues() {
		List<Object> expectedOptions = new ArrayList<>();

		expectedOptions.add(createUncheckedOption("Label 1", "value 1"));
		expectedOptions.add(createUncheckedOption("Label 2", "value 2"));
		expectedOptions.add(createCheckedOption("Label 3", "value 3"));

		DDMFormFieldOptions ddmFormFieldOptions = createDDMFormFieldOptions();

		List<Object> actualOptions = getActualOptions(
			ddmFormFieldOptions, createValue("value 3"),
			createPredefinedValue("value 1", "value 2"), LocaleUtil.US);

		Assert.assertEquals(expectedOptions, actualOptions);
	}

	@Test
	public void testGetOptionsWithTwoCheckedValueAndNoPredefinedValue() {
		List<Object> expectedOptions = new ArrayList<>();

		expectedOptions.add(createCheckedOption("Label 1", "value 1"));
		expectedOptions.add(createUncheckedOption("Label 2", "value 2"));
		expectedOptions.add(createCheckedOption("Label 3", "value 3"));

		DDMFormFieldOptions ddmFormFieldOptions = createDDMFormFieldOptions();

		List<Object> actualOptions = getActualOptions(
			ddmFormFieldOptions, createValue("value 1", "value 3"),
			createPredefinedValue(), LocaleUtil.US);

		Assert.assertEquals(expectedOptions, actualOptions);
	}

	protected Map<String, String> createCheckedOption(
		String label, String value) {

		return createOption(label, "checked", value);
	}

	protected DDMFormFieldOptions createDDMFormFieldOptions() {
		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		ddmFormFieldOptions.addOptionLabel("value 1", LocaleUtil.US, "Label 1");
		ddmFormFieldOptions.addOptionLabel("value 2", LocaleUtil.US, "Label 2");
		ddmFormFieldOptions.addOptionLabel("value 3", LocaleUtil.US, "Label 3");

		return ddmFormFieldOptions;
	}

	protected Map<String, String> createOption(
		String label, String status, String value) {

		Map<String, String> option = new HashMap<>();

		option.put("label", label);
		option.put("status", status);
		option.put("value", value);

		return option;
	}

	protected LocalizedValue createPredefinedValue(String... strings) {
		LocalizedValue localizedValue = new LocalizedValue();

		JSONArray jsonArray = toJSONArray(strings);

		localizedValue.addString(LocaleUtil.US, jsonArray.toString());

		return localizedValue;
	}

	protected Map<String, String> createUncheckedOption(
		String label, String value) {

		return createOption(label, StringPool.BLANK, value);
	}

	protected String createValue(String... strings) {
		JSONArray jsonArray = toJSONArray(strings);

		return jsonArray.toString();
	}

	protected List<Object> getActualOptions(
		DDMFormFieldOptions ddmFormFieldOptions, String value,
		LocalizedValue predefinedValue, Locale locale) {

		CheckboxMultipleDDMFormFieldContextHelper
			checkboxMultipleDDMFormFieldContextHelper =
				new CheckboxMultipleDDMFormFieldContextHelper(
					new JSONFactoryImpl(), ddmFormFieldOptions, locale);

		return checkboxMultipleDDMFormFieldContextHelper.getOptions();
	}

	protected JSONArray toJSONArray(String... strings) {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (String string : strings) {
			jsonArray.put(string);
		}

		return jsonArray;
	}

	private final JSONFactory _jsonFactory = new JSONFactoryImpl();

}