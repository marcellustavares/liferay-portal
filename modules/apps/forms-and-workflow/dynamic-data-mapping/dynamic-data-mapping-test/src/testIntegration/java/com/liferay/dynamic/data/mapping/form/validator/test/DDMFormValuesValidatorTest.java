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

package com.liferay.dynamic.data.mapping.form.validator.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.service.test.BaseDDMServiceTestCase;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.validator.DDMFormValuesValidationException.MustSetValidValues;
import com.liferay.dynamic.data.mapping.validator.DDMFormValuesValidator;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.List;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Leonardo Barros
 */
@RunWith(Arquillian.class)
public class DDMFormValuesValidatorTest extends BaseDDMServiceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		setUpDDMFormValuesValidator();
	}

	@Test(expected = MustSetValidValues.class)
	public void testValidationWithMissingNestedRequiredFieldValue()
		throws Exception {

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = new DDMFormField("name", "text");

		List<DDMFormField> nestedDDMFormFields =
			ddmFormField.getNestedDDMFormFields();

		nestedDDMFormFields.add(
			DDMFormTestUtil.createTextDDMFormField(
				"contact", "", false, false, true));

		DDMFormTestUtil.addDDMFormFields(ddmForm, ddmFormField);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue("name", null);

		List<DDMFormFieldValue> nestedDDMFormFieldValues =
			ddmFormFieldValue.getNestedDDMFormFieldValues();

		nestedDDMFormFieldValues.add(
			DDMFormValuesTestUtil.createDDMFormFieldValue("contact", null));

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		_ddmFormValuesValidator.validate(ddmFormValues);
	}

	@Test(expected = MustSetValidValues.class)
	public void testValidationWithMissingRequiredField() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField =
			DDMFormTestUtil.createLocalizableTextDDMFormField("name");

		ddmFormField.setRequired(true);

		DDMFormTestUtil.addDDMFormFields(ddmForm, ddmFormField);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		_ddmFormValuesValidator.validate(ddmFormValues);
	}

	@Test(expected = MustSetValidValues.class)
	public void testValidationWithMissingRequiredFieldValue() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField =
			DDMFormTestUtil.createLocalizableTextDDMFormField("name");

		ddmFormField.setRequired(true);

		DDMFormTestUtil.addDDMFormFields(ddmForm, ddmFormField);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue("name", null));

		_ddmFormValuesValidator.validate(ddmFormValues);
	}

	@Test(expected = MustSetValidValues.class)
	public void testValidationWithRequiredFieldAndEmptyDefaultLocaleValue()
		throws Exception {

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			DDMFormTestUtil.createAvailableLocales(LocaleUtil.US),
			LocaleUtil.US);

		DDMFormField ddmFormField = DDMFormTestUtil.createTextDDMFormField(
			"name", "Name", true, false, true);

		DDMFormTestUtil.addDDMFormFields(ddmForm, ddmFormField);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		LocalizedValue localizedValue = new LocalizedValue(LocaleUtil.US);

		localizedValue.addString(LocaleUtil.US, StringPool.BLANK);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"name", localizedValue);

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		_ddmFormValuesValidator.validate(ddmFormValues);
	}

	@Test(expected = MustSetValidValues.class)
	public void testValidationWithRequiredFieldAndEmptyTranslatedValue()
		throws Exception {

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			DDMFormTestUtil.createAvailableLocales(
				LocaleUtil.US, LocaleUtil.BRAZIL),
			LocaleUtil.US);

		DDMFormField ddmFormField = DDMFormTestUtil.createTextDDMFormField(
			"name", "Name", true, false, true);

		DDMFormTestUtil.addDDMFormFields(ddmForm, ddmFormField);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm,
			DDMFormTestUtil.createAvailableLocales(
				LocaleUtil.US, LocaleUtil.BRAZIL),
			LocaleUtil.US);

		LocalizedValue localizedValue = new LocalizedValue(LocaleUtil.US);

		localizedValue.addString(LocaleUtil.US, StringUtil.randomString());
		localizedValue.addString(LocaleUtil.BRAZIL, StringPool.BLANK);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"name", localizedValue);

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		_ddmFormValuesValidator.validate(ddmFormValues);
	}

	@Test(expected = MustSetValidValues.class)
	public void testValidationWithRequiredFieldAndWithNoValue()
		throws Exception {

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			DDMFormTestUtil.createAvailableLocales(LocaleUtil.US),
			LocaleUtil.US);

		DDMFormField ddmFormField = DDMFormTestUtil.createTextDDMFormField(
			"name", "Name", true, false, true);

		DDMFormTestUtil.addDDMFormFields(ddmForm, ddmFormField);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		_ddmFormValuesValidator.validate(ddmFormValues);
	}

	protected void setUpDDMFormValuesValidator() {
		Registry registry = RegistryUtil.getRegistry();

		_ddmFormValuesValidator = registry.getService(
			DDMFormValuesValidator.class);
	}

	private DDMFormValuesValidator _ddmFormValuesValidator;

}