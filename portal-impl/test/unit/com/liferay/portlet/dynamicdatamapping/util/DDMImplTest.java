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
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.Field;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * @author Marcellus Tavares
 */
@PrepareForTest({DDMStructureLocalServiceUtil.class, LocaleUtil.class})
public class DDMImplTest extends BaseDDMTestCase {

	@Before
	public void setUp() throws Exception {
		setUpDDMStructureLocalServiceUtil();
		setUpDDMFormXSDDeserializerUtil();
		setUpDDMFormXSDSerializerUtil();
		setUpLocaleUtil();
		setUpSAXReaderUtil();
		setUpHtmlUtil();
		setUpPropsUtil();
	}

	@Test
	public void testMergeFieldsAfterNewFieldIsAdded() {
		DDMForm ddmForm = createDDMForm();

		addDDMFormFields(
			ddmForm, createTextDDMFormField("Title", "", true, false, false),
			createTextDDMFormField("Content", "", true, false, false));

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		Field existingTitleField = createField(
			ddmStructure.getStructureId(), "Title",
			createValuesList("Title value"), null);

		Field existingFieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(), "Title_INSTANCE_ovho");

		Fields existingFields = createFields(
			existingTitleField, existingFieldsDisplay);

		Field newContentField = createField(
			ddmStructure.getStructureId(), "Content",
			createValuesList("Content value"), null);

		Field newFieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(),
			"Title_INSTANCE_ovho,Content_INSTANCE_yiek");

		Fields newFields = createFields(
			existingTitleField, newContentField, newFieldsDisplay);

		Fields actualFields = _ddmImpl.mergeFields(newFields, existingFields);

		Field actualContentField = actualFields.get("Content");

		Assert.assertNotNull(actualContentField);
		Assert.assertEquals(
			"Content value", actualContentField.getValue(LocaleUtil.US));
	}

	@Test
	public void testMergeFieldsAfterNewFieldValueInsertedInTheMiddleOfSeries() {
		DDMForm ddmForm = createDDMForm();

		addDDMFormFields(
			ddmForm, createTextDDMFormField("Content", "", true, true, false));

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		Field existingContentField = createField(
			ddmStructure.getStructureId(), "Content",
			createValuesList("Content 1", "Content 3"),
			createValuesList("Conteudo 1", "Conteudo 3"));

		Field existingFieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(),
			"Content_INSTANCE_ovho,Content_INSTANCE_yiek");

		Fields existingFields = createFields(
			existingContentField, existingFieldsDisplay);

		Field newContentField = createField(
			ddmStructure.getStructureId(), "Content",
			createValuesList("Content 1", "Content 2", "Content 3"), null);

		Field newFieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(),
			"Content_INSTANCE_ovho,Content_INSTANCE_zuvh," +
			"Content_INSTANCE_yiek");

		Fields newFields = createFields(newContentField, newFieldsDisplay);

		Fields actualFields = _ddmImpl.mergeFields(newFields, existingFields);

		Field actualContentField = actualFields.get("Content");

		Assert.assertNotNull(actualContentField);

		List<Serializable> values_en_US = actualContentField.getValues(
			LocaleUtil.US);

		Assert.assertEquals(3, values_en_US.size());
		Assert.assertEquals("Content 1", values_en_US.get(0));
		Assert.assertEquals("Content 2", values_en_US.get(1));
		Assert.assertEquals("Content 3", values_en_US.get(2));

		List<Serializable> values_pt_BR = actualContentField.getValues(
			LocaleUtil.BRAZIL);

		Assert.assertEquals(3, values_pt_BR.size());
		Assert.assertEquals("Conteudo 1", values_pt_BR.get(0));
		Assert.assertEquals("Content 2", values_pt_BR.get(1));
		Assert.assertEquals("Conteudo 3", values_pt_BR.get(2));
	}

	@Test
	public void testMergeFieldsAfterNewLocalizedFieldValueIsAdded() {
		DDMForm ddmForm = createDDMForm();

		addDDMFormFields(
			ddmForm, createTextDDMFormField("Title", "", true, false, false));

		DDMStructure ddmStructure = createStructure("Test Structure", ddmForm);

		Field existingTitleField = createField(
			ddmStructure.getStructureId(), "Title",
			createValuesList("Title value"), null);

		Field existingFieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(), "Title_INSTANCE_ovho");

		Fields existingFields = createFields(
			existingTitleField, existingFieldsDisplay);

		Field newTitleField = createField(
			ddmStructure.getStructureId(), "Title",
			createValuesList("Modified title value"),
			createValuesList("Valor do titulo modificado"));

		Field newFieldsDisplay = createdFieldsDisplayField(
			ddmStructure.getStructureId(), "Title_INSTANCE_ovho");

		Fields newFields = createFields(newTitleField, newFieldsDisplay);

		Fields actualFields = _ddmImpl.mergeFields(newFields, existingFields);

		Field actualContentField = actualFields.get("Title");

		Assert.assertNotNull(actualContentField);
		Assert.assertEquals(
			"Modified title value", actualContentField.getValue(LocaleUtil.US));
		Assert.assertEquals(
			"Valor do titulo modificado",
			actualContentField.getValue(LocaleUtil.BRAZIL));
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
		long ddmStructureId, String fieldName, List<Serializable> values_en_US,
		List<Serializable> values_pt_BR) {

		Map<Locale, List<Serializable>> valuesMap = createValuesMap(
			values_en_US, values_pt_BR);

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

	protected List<Serializable> createValuesList(String... valuesString) {
		List<Serializable> values = new ArrayList<Serializable>();

		for (String valueString : valuesString) {
			values.add(valueString);
		}

		return values;
	}

	protected Map<Locale, List<Serializable>> createValuesMap(
		List<Serializable> values_en_US, List<Serializable> values_pt_BR) {

		Map<Locale, List<Serializable>> valuesMap =
			new HashMap<Locale, List<Serializable>>();

		if (values_en_US != null) {
			valuesMap.put(LocaleUtil.US, values_en_US);
		}

		if (values_pt_BR != null) {
			valuesMap.put(LocaleUtil.BRAZIL, values_pt_BR);
		}

		return valuesMap;
	}

	private DDMImpl _ddmImpl = new DDMImpl();

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