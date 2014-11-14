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

import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
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
import com.liferay.portlet.dynamicdatamapping.io.DDMFormXSDSerializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;
import com.liferay.portlet.dynamicdatamapping.storage.StorageType;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

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
	}

	@Test
	public void testAddRecordWithLocalizedTextField() throws Exception {
		DDMForm ddmForm = createDDMForm();

		ddmForm.addDDMFormField(createTextDDMFormField("Name", true, false));

		DDMStructure ddmStructure = addStructure(
			PortalUtil.getClassNameId(DDLRecordSet.class), null,
			"Test Structure", DDMFormXSDSerializerUtil.serialize(ddmForm),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDLRecordSet recordSet = addRecordSet(ddmStructure.getStructureId());

		DDMFormValues expectedDDMFormValues = createDDMFormValues(ddmForm);

		expectedDDMFormValues.addDDMFormFieldValue(
			createLocalizedTextDDMFormFieldValue("Name", "Joe Bloggs"));

		DDLRecord record = addRecord(
			recordSet.getRecordSetId(), expectedDDMFormValues,
			WorkflowConstants.ACTION_PUBLISH);

		DDLRecord actualRecord = DDLRecordLocalServiceUtil.getRecord(
			record.getRecordId());

		DDMFormValues actualDDMFormValues = actualRecord.getDDMFormValues();

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testAddRecordWithNestedFieldAndSeparatorAsParentField()
		throws Exception {

		DDMForm ddmForm = createDDMForm();

		DDMFormField separatorDDMFormField = createSeparatorDDMFormField(
			"Separator");

		separatorDDMFormField.addNestedDDMFormField(
			createTextDDMFormField("Name", true, false));

		ddmForm.addDDMFormField(separatorDDMFormField);

		DDMStructure ddmStructure = addStructure(
			PortalUtil.getClassNameId(DDLRecordSet.class), null,
			"Test Structure", DDMFormXSDSerializerUtil.serialize(ddmForm),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDLRecordSet recordSet = addRecordSet(ddmStructure.getStructureId());

		DDMFormValues expectedDDMFormValues = createDDMFormValues(ddmForm);

		DDMFormFieldValue separatorDDMFormFieldValue =
			createSeparatorDDMFormFieldValue("Separator");

		separatorDDMFormFieldValue.addNestedDDMFormFieldValue(
			createLocalizedTextDDMFormFieldValue("Name", "Joe Bloggs"));

		expectedDDMFormValues.addDDMFormFieldValue(separatorDDMFormFieldValue);

		DDLRecord record = addRecord(
			recordSet.getRecordSetId(), expectedDDMFormValues,
			WorkflowConstants.ACTION_PUBLISH);

		DDLRecord actualRecord = DDLRecordLocalServiceUtil.getRecord(
			record.getRecordId());

		DDMFormValues actualDDMFormValues = actualRecord.getDDMFormValues();

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testAddRecordWithNestedFieldsAndTextAsParentField()
		throws Exception {

		DDMForm ddmForm = createDDMForm();

		DDMFormField parentDDMFormField = createTextDDMFormField(
			"Name", true, true);

		parentDDMFormField.addNestedDDMFormField(
			createTextDDMFormField("Phone", false, true));

		ddmForm.addDDMFormField(parentDDMFormField);

		DDMStructure ddmStructure = addStructure(
			PortalUtil.getClassNameId(DDLRecordSet.class), null,
			"Test Structure", DDMFormXSDSerializerUtil.serialize(ddmForm),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDLRecordSet recordSet = addRecordSet(ddmStructure.getStructureId());

		DDMFormValues expectedDDMFormValues = createDDMFormValues(ddmForm);

		DDMFormFieldValue scottDDMFormFieldValue =
			createLocalizedTextDDMFormFieldValue("Name", "Scott Joplin");

		scottDDMFormFieldValue.addNestedDDMFormFieldValue(
			createUnlocalizedTextDDMFormFieldValue("Phone", "12"));

		scottDDMFormFieldValue.addNestedDDMFormFieldValue(
			createUnlocalizedTextDDMFormFieldValue("Phone", "34"));

		expectedDDMFormValues.addDDMFormFieldValue(scottDDMFormFieldValue);

		DDMFormFieldValue louisDDMFormFieldValue =
			createLocalizedTextDDMFormFieldValue("Name", "Louis Armstrong");

		louisDDMFormFieldValue.addNestedDDMFormFieldValue(
			createUnlocalizedTextDDMFormFieldValue("Phone", "56"));

		louisDDMFormFieldValue.addNestedDDMFormFieldValue(
			createUnlocalizedTextDDMFormFieldValue("Phone", "78"));

		expectedDDMFormValues.addDDMFormFieldValue(louisDDMFormFieldValue);

		DDLRecord record = addRecord(
			recordSet.getRecordSetId(), expectedDDMFormValues,
			WorkflowConstants.ACTION_PUBLISH);

		DDLRecord actualRecord = DDLRecordLocalServiceUtil.getRecord(
			record.getRecordId());

		DDMFormValues actualDDMFormValues = actualRecord.getDDMFormValues();

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testAddRecordWithRepeatableTextField() throws Exception {
		DDMForm ddmForm = createDDMForm();

		ddmForm.addDDMFormField(createTextDDMFormField("Name", true, true));

		DDMStructure ddmStructure = addStructure(
			PortalUtil.getClassNameId(DDLRecordSet.class), null,
			"Test Structure", DDMFormXSDSerializerUtil.serialize(ddmForm),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDLRecordSet recordSet = addRecordSet(ddmStructure.getStructureId());

		DDMFormValues expectedDDMFormValues = createDDMFormValues(ddmForm);

		expectedDDMFormValues.addDDMFormFieldValue(
			createLocalizedTextDDMFormFieldValue("Name", "Joe Bloggs I"));

		expectedDDMFormValues.addDDMFormFieldValue(
			createLocalizedTextDDMFormFieldValue("Name", "Joe Bloggs II"));

		expectedDDMFormValues.addDDMFormFieldValue(
			createLocalizedTextDDMFormFieldValue("Name", "Joe Bloggs III"));

		DDLRecord record = addRecord(
			recordSet.getRecordSetId(), expectedDDMFormValues,
			WorkflowConstants.ACTION_PUBLISH);

		DDLRecord actualRecord = DDLRecordLocalServiceUtil.getRecord(
			record.getRecordId());

		DDMFormValues actualDDMFormValues = actualRecord.getDDMFormValues();

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
	}

	@Test
	public void testAddRecordWithUnlocalizedAndUnrepeatableTextField()
		throws Exception {

		DDMForm ddmForm = createDDMForm();

		ddmForm.addDDMFormField(createTextDDMFormField("Name", false, false));

		DDMStructure ddmStructure = addStructure(
			PortalUtil.getClassNameId(DDLRecordSet.class), null,
			"Test Structure", DDMFormXSDSerializerUtil.serialize(ddmForm),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDLRecordSet recordSet = addRecordSet(ddmStructure.getStructureId());

		DDMFormValues expectedDDMFormValues = createDDMFormValues(ddmForm);

		expectedDDMFormValues.addDDMFormFieldValue(
			createUnlocalizedTextDDMFormFieldValue("Name", "Joe Bloggs"));

		DDLRecord record = addRecord(
			recordSet.getRecordSetId(), expectedDDMFormValues,
			WorkflowConstants.ACTION_PUBLISH);

		DDLRecord actualRecord = DDLRecordLocalServiceUtil.getRecord(
			record.getRecordId());

		DDMFormValues actualDDMFormValues = actualRecord.getDDMFormValues();

		assertEquals(expectedDDMFormValues, actualDDMFormValues);
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

	protected DDLRecord addRecord(
			String name, String description, int workflowAction)
		throws Exception {

		DDMStructure ddmStructure = recordSet.getDDMStructure();

		DDMFormValues ddmFormValues = createDDMFormValues(
			ddmStructure.getDDMForm());

		DDMFormFieldValue nameDDMFormFieldValue =
			createLocalizedTextDDMFormFieldValue("name", name);

		ddmFormValues.addDDMFormFieldValue(nameDDMFormFieldValue);

		DDMFormFieldValue descriptionDDMFormFieldValue =
			createLocalizedTextDDMFormFieldValue("description", description);

		ddmFormValues.addDDMFormFieldValue(descriptionDDMFormFieldValue);

		return addRecord(
			recordSet.getRecordSetId(), ddmFormValues, workflowAction);
	}

	protected void addSampleRecords() throws Exception {
		addRecord(
			"Joe Bloggs", "Simple description",
			WorkflowConstants.ACTION_PUBLISH);

		addRecord(
			"Bloggs","Another description example",
			WorkflowConstants.ACTION_PUBLISH);
	}

	protected void assertEquals(
			DDMFormValues expectedDDMFormValues,
			DDMFormValues actualDDMFormValues)
		throws Exception {

		String expectedSerializedDDMFormValues =
			DDMFormValuesJSONSerializerUtil.serialize(expectedDDMFormValues);

		String actualSerializedDDMFormValues =
			DDMFormValuesJSONSerializerUtil.serialize(actualDDMFormValues);

		JSONAssert.assertEquals(
			expectedSerializedDDMFormValues, actualSerializedDDMFormValues,
			false);
	}

	protected DDMForm createDDMForm() {
		DDMForm ddmForm = new DDMForm();

		Set<Locale> availableLocales = new HashSet<Locale>();

		availableLocales.add(LocaleUtil.US);

		ddmForm.setAvailableLocales(availableLocales);
		ddmForm.setDefaultLocale(LocaleUtil.US);

		return ddmForm;
	}

	protected DDMFormValues createDDMFormValues(DDMForm ddmForm) {
		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		Set<Locale> availableLocales = new HashSet<Locale>();

		availableLocales.add(LocaleUtil.US);

		ddmFormValues.setAvailableLocales(availableLocales);
		ddmFormValues.setDefaultLocale(LocaleUtil.US);

		return ddmFormValues;
	}

	protected DDMFormFieldValue createLocalizedTextDDMFormFieldValue(
		String name, String enValue) {

		Value localizedValue = new LocalizedValue(LocaleUtil.US);

		localizedValue.addString(LocaleUtil.US, enValue);

		return createTextDDMFormFieldValue(name, localizedValue);
	}

	protected DDMFormField createSeparatorDDMFormField(String name) {
		DDMFormField ddmFormField = new DDMFormField(name, "separator");

		ddmFormField.setDataType(StringPool.BLANK);

		LocalizedValue label = ddmFormField.getLabel();

		label.addString(LocaleUtil.US, name);

		return ddmFormField;
	}

	protected DDMFormFieldValue createSeparatorDDMFormFieldValue(String name) {
		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName(name);
		ddmFormFieldValue.setInstanceId(StringUtil.randomString());

		return ddmFormFieldValue;
	}

	protected DDMFormField createTextDDMFormField(
		String name, boolean localizable, boolean repeatable) {

		DDMFormField ddmFormField = new DDMFormField(name, "text");

		ddmFormField.setDataType("string");
		ddmFormField.setLocalizable(localizable);
		ddmFormField.setRepeatable(repeatable);

		LocalizedValue label = ddmFormField.getLabel();

		label.addString(LocaleUtil.US, name);

		return ddmFormField;
	}

	protected DDMFormFieldValue createTextDDMFormFieldValue(
		String name, Value value) {

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setInstanceId(StringUtil.randomString());
		ddmFormFieldValue.setName(name);
		ddmFormFieldValue.setValue(value);

		return ddmFormFieldValue;
	}

	protected DDMFormFieldValue createUnlocalizedTextDDMFormFieldValue(
		String name, String value) {

		return createTextDDMFormFieldValue(name, new UnlocalizedValue(value));
	}

	protected SearchContext getSearchContext(String keywords) throws Exception {
		SearchContext searchContext = SearchContextTestUtil.getSearchContext(
			group.getGroupId());

		searchContext.setAttribute("recordSetId", recordSet.getRecordSetId());
		searchContext.setAttribute("status", WorkflowConstants.STATUS_ANY);
		searchContext.setKeywords(keywords);

		return searchContext;
	}

	protected DDLRecordSet recordSet;

}