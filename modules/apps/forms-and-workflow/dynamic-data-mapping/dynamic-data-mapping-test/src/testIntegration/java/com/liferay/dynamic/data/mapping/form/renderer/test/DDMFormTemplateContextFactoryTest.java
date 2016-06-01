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

package com.liferay.dynamic.data.mapping.form.renderer.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormTemplateContextFactory;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Marcellus Tavares
 */
@RunWith(Arquillian.class)
public class DDMFormTemplateContextFactoryTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		setUpDDMFormTemplateContextFactory();
	}

	@Test
	public void testFormReadOnlyWithTextField() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createTextDDMFormField(
			"Name", false, false, true);

		ddmForm.addDDMFormField(ddmFormField);

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Name", new UnlocalizedValue("Joe Bloggs")));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setReadOnly(true);
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-form-read-only-text-field.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithCheckboxField() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Agreed", "Terms of use", "checkbox", "boolean", false, false,
			false);

		LocalizedValue tip = new LocalizedValue(LocaleUtil.US);

		tip.addString(
			LocaleUtil.US, "Check if you agree with our terms of use");

		ddmFormField.setTip(tip);

		ddmForm.addDDMFormField(ddmFormField);

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Agreed", new UnlocalizedValue("true")));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-checkbox-field.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithDateField() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createDDMFormField(
				"Date", "Date", "date", "string", false, false, true));

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Date", new UnlocalizedValue("2015-10-21")));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-date-field.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithParagraphField() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Paragraph", "Paragraph", "paragraph", "", false, false, false);

		ddmFormField.setProperty("text", "<p>This is a paragraph.</p>");

		ddmForm.addDDMFormField(ddmFormField);

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Paragraph", null));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-paragraph-field.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithRadioField() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Radio", "Radio", "radio", "string", false, false, true);

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		ddmFormFieldOptions.setDefaultLocale(LocaleUtil.US);

		ddmFormFieldOptions.addOptionLabel("1", LocaleUtil.US, "Option 1");
		ddmFormFieldOptions.addOptionLabel("2", LocaleUtil.US, "Option 2");
		ddmFormFieldOptions.addOptionLabel("3", LocaleUtil.US, "Option 3");

		ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);

		ddmForm.addDDMFormField(ddmFormField);

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Radio", new UnlocalizedValue("[\"2\"]")));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-radio-field.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithSelectField() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createDDMFormField(
			"Select", "Select", "select", "string", false, false, true);

		ddmFormField.setProperty("multiple", true);

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		ddmFormFieldOptions.setDefaultLocale(LocaleUtil.US);

		ddmFormFieldOptions.addOptionLabel("1", LocaleUtil.US, "Option 1");
		ddmFormFieldOptions.addOptionLabel("2", LocaleUtil.US, "Option 2");
		ddmFormFieldOptions.addOptionLabel("3", LocaleUtil.US, "Option 3");

		ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);

		ddmForm.addDDMFormField(ddmFormField);

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Select", new UnlocalizedValue("[\"1\", \"3\"]")));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-select-field.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithTextAreaFieldRepeatable() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createTextDDMFormField(
			"Name", false, true, true);

		ddmFormField.setProperty("displayStyle", "multiline");

		ddmForm.addDDMFormField(ddmFormField);

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Name", new UnlocalizedValue("Neo")));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"naha8", "Name", new UnlocalizedValue("Agent Smith")));
		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"ghnf7", "Name", new UnlocalizedValue("Morpheus")));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-text-field-repeatable.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithTextField() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createTextDDMFormField("Name", false, false, true));

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Name", new UnlocalizedValue("Joe Bloggs")));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-text-field.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithNestedTextField() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField nameDDMFormField = DDMFormTestUtil.createTextDDMFormField(
			"Name", false, false, true);

		nameDDMFormField.addNestedDDMFormField(
			DDMFormTestUtil.createTextDDMFormField(
				"Age", false, false, false));

		ddmForm.addDDMFormField(nameDDMFormField);

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		DDMFormFieldValue nameDDMFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Name", new UnlocalizedValue("Joe Bloggs"));

		nameDDMFormFieldValue.addNestedDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"nvah8", "Age", new UnlocalizedValue("20")));

		ddmFormValues.addDDMFormFieldValue(nameDDMFormFieldValue);

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-text-field.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		System.out.println("Nested " + actualTemplateContextJSONObject);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	@Test
	public void testFormWithTextFieldReadOnly() throws Exception {

		// DDM form

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormField ddmFormField = DDMFormTestUtil.createTextDDMFormField(
			"Name", false, false, true);

		ddmFormField.setReadOnly(true);

		ddmForm.addDDMFormField(ddmFormField);

		// DDM form values

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		ddmFormValues.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"yanu7", "Name", new UnlocalizedValue("Joe Bloggs")));

		// DDM form rendering context

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			new MockHttpServletRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			new MockHttpServletResponse());
		ddmFormRenderingContext.setContainerId("_CONTAINER_ID_");
		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		// Assert

		String expectedDDMFormTemplateContext = read(
			"ddm-form-template-context-text-field-read-only.json");

		JSONObject actualTemplateContextJSONObject =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		JSONAssert.assertEquals(
			expectedDDMFormTemplateContext,
			actualTemplateContextJSONObject.toString(), false);
	}

	protected String read(String fileName) throws Exception {
		Class<?> clazz = getClass();

		return StringUtil.read(
			clazz.getClassLoader(),
			"com/liferay/dynamic/data/mapping/dependencies/" + fileName);
	}

	protected void setUpDDMFormTemplateContextFactory() {
		Registry registry = RegistryUtil.getRegistry();

		_ddmFormTemplateContextFactory = registry.getService(
			DDMFormTemplateContextFactory.class);
	}

	private DDMFormTemplateContextFactory _ddmFormTemplateContextFactory;

}