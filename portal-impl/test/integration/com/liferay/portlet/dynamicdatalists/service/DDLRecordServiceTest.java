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

package com.liferay.portlet.dynamicdatalists.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.test.listeners.MainServletExecutionTestListener;
import com.liferay.portal.test.runners.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.test.SearchContextTestUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONSerializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormXSDDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormXSDSerializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;
import com.liferay.portlet.dynamicdatamapping.storage.StorageType;
import com.liferay.portlet.dynamicdatamapping.model.Value;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author Marcellus Tavares
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public class DDLRecordServiceTest extends BaseDDLServiceTestCase {

	@Override
	public void setUp() throws Exception {
		super.setUp();

		DDMStructure ddmStructure = addStructure(
			PortalUtil.getClassNameId(DDLRecordSet.class), null,
			"Test Structure", readText("test-structure.xsd"),
			StorageType.XML.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		recordSet = addRecordSet(ddmStructure.getStructureId());
		ddmForm = DDMFormXSDDeserializerUtil.deserialize(readText("test-structure.xsd"));
	}

	@Test
	public void testPublishRecordDraftWithoutChanges() throws Exception {
		DDLRecord record = addRecord(
			"Joe Bloggs", "Simple description",
			WorkflowConstants.ACTION_SAVE_DRAFT);

		Assert.assertEquals(WorkflowConstants.STATUS_DRAFT, record.getStatus());

		DDLRecordVersion recordVersion = record.getRecordVersion();

		Assert.assertTrue(recordVersion.isDraft());

		record = updateRecord(record.getRecordId(), record.getDDMFormValues(),
			WorkflowConstants.ACTION_PUBLISH);

		Assert.assertEquals(
			WorkflowConstants.STATUS_APPROVED, record.getStatus());

		recordVersion = record.getRecordVersion();

		Assert.assertTrue(recordVersion.isApproved());
	}

	@Test
	public void testSearchByTextAreaField() throws Exception {
		addSampleRecords();

		SearchContext searchContext = getSearchContext("example");

		Hits hits = DDLRecordLocalServiceUtil.search(searchContext);

		Assert.assertEquals(1, hits.getLength());

		searchContext.setKeywords("description");

		hits = DDLRecordLocalServiceUtil.search(searchContext);

		Assert.assertEquals(2, hits.getLength());
	}

	@Test
	public void testSearchByTextField() throws Exception {
		addSampleRecords();

		SearchContext searchContext = getSearchContext("\"Joe Bloggs\"");

		Hits hits = DDLRecordLocalServiceUtil.search(searchContext);

		Assert.assertEquals(1, hits.getLength());

		searchContext.setKeywords("Bloggs");

		hits = DDLRecordLocalServiceUtil.search(searchContext);

		Assert.assertEquals(2, hits.getLength());
	}

	@Test
	public void testLocalizedTextField() throws Exception {
		
		baseSettingsTest(true, false, false, false);
	}

	@Test
	public void testRepeatableTextField() throws Exception {
		
		baseSettingsTest(false, true, false, false);
	}

	@Test
	public void testUnlocalizedAndUnrepeatableTextField() throws Exception {
		
		baseSettingsTest(false, false, false, false);
	}

	@Test
	public void testNestedFieldsWithoutSeparatorAsParentField() throws Exception {

		baseSettingsTest(false, false, true, false);
	}

	@Test
	public void testNestedFieldsWithSeparatorAsParentField() throws Exception {

		baseSettingsTest(false, false, true, true);
	}
	
	protected void baseSettingsTest(boolean localizedTest, boolean repeatableTest,
			boolean nestedFieldTest, boolean hasSeparatorAsParentField) throws Exception {

		DDMForm ddmForm = createDDMForm();

		DDMFormField ddmFormField;
		
		if (hasSeparatorAsParentField) {
			ddmFormField = createSeparatorDDMFormField("Separator1");
		} else {
			ddmFormField = createTextDDMFormField("Name1", localizedTest, repeatableTest);
		}

		if (nestedFieldTest) {
			DDMFormField nestedDDMFormField = createTextDDMFormField("Name2", localizedTest, repeatableTest);

			ddmFormField.addNestedDDMFormField(nestedDDMFormField);
		}

		ddmForm.addDDMFormField(ddmFormField);

		DDMStructure ddmStructure = addStructure(
			PortalUtil.getClassNameId(DDLRecordSet.class), null,
			"Test Structure", DDMFormXSDSerializerUtil.serialize(ddmForm),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDLRecordSet recordSet = addRecordSet(ddmStructure.getStructureId());

		DDMFormValues expectedDDMFormValues = createDDMFormValues(ddmForm);

		DDMFormFieldValue ddmFormFieldValue;

		if (hasSeparatorAsParentField) {
			ddmFormFieldValue = createSeparatorDDMFormFieldValue("Separator1");
		} else {
			ddmFormFieldValue = createTextDDMFormFieldValue("Name1", "Joe Bloggs 1", localizedTest);
		}

		if (nestedFieldTest) {
			DDMFormFieldValue nestedDDMFormFieldValue = createTextDDMFormFieldValue("Name2", "Joe Bloggs 2", localizedTest);
			
			ddmFormFieldValue.addNestedDDMFormFieldValue(nestedDDMFormFieldValue);
		}
		
		expectedDDMFormValues.addDDMFormFieldValue(ddmFormFieldValue);
		
		DDLRecord record = addRecord(recordSet.getRecordSetId(), expectedDDMFormValues, WorkflowConstants.ACTION_PUBLISH);
		
		DDLRecord actualRecord = DDLRecordLocalServiceUtil.getRecord(record.getRecordId());
		
		DDMFormValues actualDDMFormValues = actualRecord.getDDMFormValues();
		
		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}
	
	protected void assertEquals(DDMFormValues expectedDDMFormValues, DDMFormValues actualDDMFormValues) throws Exception {
		String expectedSerializedDDMFormValues = DDMFormValuesJSONSerializerUtil.serialize(expectedDDMFormValues);
		String actualSerializedDDMFormValues = DDMFormValuesJSONSerializerUtil.serialize(actualDDMFormValues);
		
		JSONAssert.assertEquals(expectedSerializedDDMFormValues, actualSerializedDDMFormValues, false);
	}

	protected DDLRecord addRecord(String name,
			String description, int workflowAction) throws Exception {

		DDMFormValues ddmFormValues = createDDMFormValues(ddmForm);

		DDMFormFieldValue nameField = createTextDDMFormFieldValue("name", name, true);
		DDMFormFieldValue descriptionField = createTextDDMFormFieldValue("description", description, true);

		ddmFormValues.addDDMFormFieldValue(nameField);
		ddmFormValues.addDDMFormFieldValue(descriptionField);

		return addRecord(recordSet.getRecordSetId(), ddmFormValues, workflowAction);
	}

	protected void addSampleRecords() throws Exception {
		addRecord(
			"Joe Bloggs", "Simple description",
			WorkflowConstants.ACTION_PUBLISH);

		addRecord(
			"Bloggs","Another description example",
			WorkflowConstants.ACTION_PUBLISH);
	}

	protected SearchContext getSearchContext(String keywords) throws Exception {
		SearchContext searchContext = SearchContextTestUtil.getSearchContext(
			group.getGroupId());

		searchContext.setAttribute("recordSetId", recordSet.getRecordSetId());
		searchContext.setAttribute("status", WorkflowConstants.STATUS_ANY);
		searchContext.setKeywords(keywords);

		return searchContext;
	}

	protected DDMForm createDDMForm() {
		
		DDMForm ddmForm = new DDMForm();
		Set<Locale> availableLocales = new HashSet<Locale>(1);

		availableLocales.add(LocaleUtil.US);
		
		ddmForm.setAvailableLocales(availableLocales);
		ddmForm.setDefaultLocale(LocaleUtil.US);
		
		return ddmForm;
	}
	
	protected DDMFormField createTextDDMFormField(String fieldName, boolean localized, boolean repeatable) {
		
		DDMFormField ddmFormField = new DDMFormField(fieldName, "text");

		ddmFormField.setDataType("string");
		ddmFormField.setLocalizable(localized);
		ddmFormField.setRepeatable(repeatable);
		
		LocalizedValue label = ddmFormField.getLabel();
		
		label.addString(LocaleUtil.US, fieldName);
		
		return ddmFormField;
	}
	
	protected DDMFormField createSeparatorDDMFormField(String fieldName) {
		
		DDMFormField ddmFormField = new DDMFormField(fieldName, "separator");
		
		ddmFormField.setDataType("");
		
		LocalizedValue label = ddmFormField.getLabel();
		
		label.addString(LocaleUtil.US, fieldName);
		
		return ddmFormField;
	}
	
	protected DDMFormValues createDDMFormValues(DDMForm ddmForm) {
		
		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);
		Set<Locale> availableLocales = new HashSet<Locale>(1);
		
		availableLocales.add(LocaleUtil.US);
		
		ddmFormValues.setAvailableLocales(availableLocales);
		ddmFormValues.setDefaultLocale(LocaleUtil.US);
		
		return ddmFormValues;
	}

	protected DDMFormFieldValue createTextDDMFormFieldValue(String fieldName, String fieldValue,
			boolean isFieldLocalized) {
		
		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();
		Value value;
			
		if (isFieldLocalized) {
			value = new LocalizedValue(LocaleUtil.US);
			
			value.addString(LocaleUtil.US, fieldValue);
		} else {
			value = new UnlocalizedValue(fieldValue);
		}
		
		ddmFormFieldValue.setValue(value);
		ddmFormFieldValue.setName(fieldName);
		ddmFormFieldValue.setInstanceId(StringUtil.randomString());
		
		return ddmFormFieldValue;
	}

	protected DDMFormFieldValue createSeparatorDDMFormFieldValue(String fieldName) {
		
		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();
	
		ddmFormFieldValue.setName(fieldName);
		ddmFormFieldValue.setInstanceId(StringUtil.randomString());
		
		return ddmFormFieldValue;
	}
	
	protected DDLRecordSet recordSet;
	protected DDMForm ddmForm;

}