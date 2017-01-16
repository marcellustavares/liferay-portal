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

package com.liferay.dynamic.data.mapping.data.provider.rest.internal;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import com.liferay.dynamic.data.mapping.annotations.DDMFieldSetOrientation;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@PrepareForTest(ResourceBundleUtil.class)
@RunWith(PowerMockRunner.class)
public class DDMRESTDataProviderSettingsTest {

	@Before
	public void setUp() throws Exception {
		setUpLanguageUtil();
		setUpResourceBundleUtil();
	}

	@Test
	public void testCreateForm() {
		DDMForm ddmForm = DDMFormFactory.create(
			DDMRESTDataProviderSettings.class);

		Map<String, DDMFormField> ddmFormFields = ddmForm.getDDMFormFieldsMap(
			false);

		Assert.assertEquals(10, ddmFormFields.size());

		Assert.assertTrue(ddmFormFields.containsKey("cacheable"));

		assertCacheable(ddmFormFields.get("cacheable"));

		Assert.assertTrue(ddmFormFields.containsKey("filterable"));

		assertFilterable(ddmFormFields.get("filterable"));

		Assert.assertTrue(ddmFormFields.containsKey("filterParameterName"));

		assertFilterParameterName(ddmFormFields.get("filterParameterName"));

		Assert.assertTrue(ddmFormFields.containsKey("inputParameters"));

		assertInputParameters(ddmFormFields.get("inputParameters"));

		Assert.assertTrue(ddmFormFields.containsKey("key"));

		assertKey(ddmFormFields.get("key"));

		Assert.assertTrue(ddmFormFields.containsKey("outputParameters"));

		assertOutputParameters(ddmFormFields.get("outputParameters"));

		Assert.assertTrue(ddmFormFields.containsKey("password"));

		assertPassword(ddmFormFields.get("password"));

		Assert.assertTrue(ddmFormFields.containsKey("url"));

		assertUrl(ddmFormFields.get("url"));

		Assert.assertTrue(ddmFormFields.containsKey("username"));

		assertUsername(ddmFormFields.get("username"));

		Assert.assertTrue(ddmFormFields.containsKey("value"));

		assertValue(ddmFormFields.get("value"));
	}

	protected void assertCacheable(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertEquals("checkbox", ddmFormField.getType());

		Assert.assertEquals("boolean", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("showAsSwitcher"));

		Assert.assertEquals("true", properties.get("showAsSwitcher"));
	}

	protected void assertFilterable(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertEquals("checkbox", ddmFormField.getType());

		Assert.assertEquals("boolean", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("showAsSwitcher"));

		Assert.assertEquals("true", properties.get("showAsSwitcher"));
	}

	protected void assertFilterParameterName(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertEquals("text", ddmFormField.getType());

		Assert.assertEquals("string", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("placeholder"));

		Assert.assertTrue(properties.containsKey("tooltip"));
	}

	protected void assertInputParameters(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertEquals("fieldset", ddmFormField.getType());

		Assert.assertEquals("", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("orientation"));

		Assert.assertEquals(
			DDMFieldSetOrientation.HORIZONTAL, properties.get("orientation"));

		List<DDMFormField> nestedFields = ddmFormField.getNestedDDMFormFields();

		Assert.assertEquals(3, nestedFields.size());

		DDMFormField nestedField0 = nestedFields.get(0);

		Assert.assertEquals("inputParameterName", nestedField0.getName());
		Assert.assertEquals("text", nestedField0.getType());
		Assert.assertEquals("string", nestedField0.getDataType());

		Map<String, Object> nestedFieldProperties =
			nestedField0.getProperties();

		Assert.assertTrue(nestedFieldProperties.containsKey("placeholder"));

		DDMFormField nestedField1 = nestedFields.get(1);

		Assert.assertEquals("inputParameterType", nestedField1.getName());
		Assert.assertEquals("select", nestedField1.getType());
		Assert.assertEquals("string", nestedField1.getDataType());

		Assert.assertNotNull(nestedField1.getDDMFormFieldOptions());

		DDMFormFieldOptions ddmFormFieldOptions =
			nestedField1.getDDMFormFieldOptions();

		Set<String> optionValues = ddmFormFieldOptions.getOptionsValues();

		Assert.assertTrue(optionValues.contains("text"));
		Assert.assertTrue(optionValues.contains("number"));

		DDMFormField nestedField2 = nestedFields.get(2);

		Assert.assertEquals("inputParameterRequired", nestedField2.getName());
		Assert.assertEquals("checkbox", nestedField2.getType());
		Assert.assertEquals("boolean", nestedField2.getDataType());
	}

	protected void assertKey(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertTrue(ddmFormField.isRequired());

		Assert.assertTrue(properties.containsKey("placeholder"));

		Assert.assertTrue(properties.containsKey("tooltip"));
	}

	protected void assertOutputParameters(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertEquals("fieldset", ddmFormField.getType());

		Assert.assertEquals("", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("orientation"));

		Assert.assertEquals(
			DDMFieldSetOrientation.HORIZONTAL, properties.get("orientation"));

		List<DDMFormField> nestedFields = ddmFormField.getNestedDDMFormFields();

		Assert.assertEquals(3, nestedFields.size());

		DDMFormField nestedField0 = nestedFields.get(0);

		Assert.assertEquals("outputParameterName", nestedField0.getName());
		Assert.assertEquals("text", nestedField0.getType());
		Assert.assertEquals("string", nestedField0.getDataType());

		Map<String, Object> nestedFieldProperties =
			nestedField0.getProperties();

		Assert.assertTrue(nestedFieldProperties.containsKey("placeholder"));

		DDMFormField nestedField1 = nestedFields.get(1);

		Assert.assertEquals("outputParameterPath", nestedField1.getName());
		Assert.assertEquals("text", nestedField1.getType());
		Assert.assertEquals("string", nestedField1.getDataType());

		nestedFieldProperties = nestedField1.getProperties();

		Assert.assertTrue(nestedFieldProperties.containsKey("placeholder"));

		DDMFormField nestedField2 = nestedFields.get(2);

		Assert.assertEquals("outputParameterType", nestedField2.getName());
		Assert.assertEquals("select", nestedField2.getType());
		Assert.assertEquals("string", nestedField2.getDataType());

		Assert.assertNotNull(nestedField2.getDDMFormFieldOptions());

		DDMFormFieldOptions ddmFormFieldOptions =
			nestedField2.getDDMFormFieldOptions();

		Set<String> optionValues = ddmFormFieldOptions.getOptionsValues();

		Assert.assertTrue(optionValues.contains("text"));
		Assert.assertTrue(optionValues.contains("number"));
	}

	protected void assertPassword(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertEquals("text", ddmFormField.getType());

		Assert.assertEquals("string", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("placeholder"));

		Assert.assertTrue(properties.containsKey("tooltip"));
	}

	protected void assertUrl(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertTrue(ddmFormField.isRequired());

		Assert.assertEquals("text", ddmFormField.getType());

		Assert.assertEquals("string", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("placeholder"));
	}

	protected void assertUsername(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertEquals("text", ddmFormField.getType());

		Assert.assertEquals("string", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("placeholder"));

		Assert.assertTrue(properties.containsKey("tooltip"));
	}

	protected void assertValue(DDMFormField ddmFormField) {
		Map<String, Object> properties = ddmFormField.getProperties();

		Assert.assertTrue(ddmFormField.isRequired());

		Assert.assertEquals("text", ddmFormField.getType());

		Assert.assertEquals("string", ddmFormField.getDataType());

		Assert.assertTrue(properties.containsKey("placeholder"));

		Assert.assertTrue(properties.containsKey("tooltip"));
	}

	protected void setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		Language language = mock(Language.class);

		languageUtil.setLanguage(language);
	}

	protected void setUpResourceBundleUtil() {
		mockStatic(ResourceBundleUtil.class);

		when(
			ResourceBundleUtil.getBundle(
				Matchers.anyString(), Matchers.any(Locale.class),
				Matchers.any(ClassLoader.class))
		).thenReturn(
			ResourceBundleUtil.EMPTY_RESOURCE_BUNDLE
		);
	}

}