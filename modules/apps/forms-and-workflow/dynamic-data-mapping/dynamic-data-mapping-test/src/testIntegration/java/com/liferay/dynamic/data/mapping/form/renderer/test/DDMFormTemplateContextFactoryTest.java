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
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
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
	public void testSingleTextFieldForm() throws Exception {

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
			"ddm-form-template-context-single-text-field.json");

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