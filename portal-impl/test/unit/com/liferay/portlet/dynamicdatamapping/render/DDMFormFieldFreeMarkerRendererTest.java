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

package com.liferay.portlet.dynamicdatamapping.render;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.dynamicdatamapping.BaseDDMTestCase;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.storage.Field;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;
import com.liferay.portlet.dynamicdatamapping.util.DDMImpl;

import org.apache.commons.lang.StringUtils;

import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
public class DDMFormFieldFreeMarkerRendererTest extends BaseDDMTestCase {

	@Before
	public void setUp() throws Exception {
		setUpDDMFormXSDDeserializerUtil();
		setUpDDMFormXSDSerializerUtil();
		setUpSAXReaderUtil();
	}

	@Test
	public void testGetFieldNamespacedNameWithFields() {
		DDMFormFieldFreeMarkerRenderer renderer =
			new DDMFormFieldFreeMarkerRenderer();

		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		MockHttpServletRequest httpServletRequest =
			new MockHttpServletRequest();

		MockHttpServletResponse httpServletResponse =
			new MockHttpServletResponse();

		String instanceId = "0kji";

		httpServletRequest.setParameter(
			DDMImpl.FIELDS_DISPLAY_NAME, "Name_INSTANCE_" + instanceId);

		DDMForm ddmForm = createDDMForm();

		addDDMFormFields(ddmForm, nameDDMFormField);

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		Field nameField = createField(
			ddmStructure.getStructureId(), "Name", createValuesList("Value 01"),
			createValuesList("Value 02"));

		Fields fields = createFields(nameField);

		String fieldDisplayValue = renderer.getFieldsDisplayValue(
			httpServletRequest, httpServletResponse, fields);

		String fieldNamespace = renderer.getFieldNamespace(
			true, fieldDisplayValue, 0);

		String fieldNamespacedName = renderer.getFieldNamespacedName(
			StringPool.BLANK, fieldNamespace, nameDDMFormField, 0);

		System.out.println(fieldNamespacedName);

		String[] pieces = fieldNamespacedName.split("_");

		assertEquals("Name", pieces[0]);
		assertEquals("INSTANCE", pieces[1]);
		assertEquals(instanceId, pieces[2]);
		assertEquals("0", pieces[3]);
	}

	@Test
	public void testGetFieldNamespacedNameWithoutFields() {
		DDMFormFieldFreeMarkerRenderer renderer =
			new DDMFormFieldFreeMarkerRenderer();

		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		String fieldNamespace = renderer.getFieldNamespace(false, null, 0);

		String fieldNamespacedName = renderer.getFieldNamespacedName(
			StringPool.BLANK, fieldNamespace, nameDDMFormField, 0);

		System.out.println(fieldNamespacedName);

		String[] pieces = fieldNamespacedName.split("_");

		assertEquals("Name", pieces[0]);
		assertEquals("INSTANCE", pieces[1]);
		assertTrue(StringUtils.isAlphanumeric(pieces[2]));
		assertEquals("0", pieces[3]);
	}

}