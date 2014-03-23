/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.service.BaseDDMServiceTestCase;
import com.liferay.portlet.dynamicdatamapping.storage.query.Condition;
import com.liferay.portlet.dynamicdatamapping.storage.query.FieldCondition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marcellus Tavares
 */
@ExecutionTestListeners(listeners = {
	EnvironmentExecutionTestListener.class,
	TransactionalExecutionTestListener.class
})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public abstract class BaseStorageAdapterTest extends BaseDDMServiceTestCase {

	@Test
	public abstract void testBooleanField() throws Exception;

	@Test
	public abstract void testConditionEquals() throws Exception;

	@Test
	public abstract void testConditionEqualsWithLocale() throws Exception;

	@Test
	public abstract void testConditionEqualsWithRepeatable() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionExcludes() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionExcludesWithLocale() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionExcludesWithRepeatable() throws Exception;

	@Test
	public abstract void testConditionGreaterThan() throws Exception;

	@Test
	public abstract void testConditionGreaterThanOrEqualTo() throws Exception;

	@Test
	public abstract void testConditionGreaterThanOrEqualToWithLocale()
		throws Exception;

	@Test
	public abstract void testConditionGreaterThanOrEqualToWithRepeatable()
		throws Exception;

	@Test
	public abstract void testConditionGreaterThanWithLocale() throws Exception;

	@Test
	public abstract void testConditionGreaterThanWithRepeatable()
		throws Exception;

	@Test
	public abstract void testConditionIn() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionIncludes() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionIncludesWithLocale() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionIncludesWithRepeatable() throws Exception;

	//@Ignore("Not ready to run")
	@Test
	public abstract void testConditionInWithLocale() throws Exception;

	@Test
	public abstract void testConditionInWithRepeatable() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionJoin() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionJoinWithLocale() throws Exception;

	//@Ignore("Not ready to run")
	//@Test
	public abstract void testConditionJoinWithRepeatable() throws Exception;

	@Test
	public abstract void testConditionLessThan() throws Exception;

	@Test
	public abstract void testConditionLessThanOrEqualTo() throws Exception;

	@Test
	public abstract void testConditionLessThanOrEqualToWithLocale()
		throws Exception;

	@Test
	public abstract void testConditionLessThanOrEqualToWithRepeatable()
		throws Exception;

	@Test
	public abstract void testConditionLessThanWithLocale() throws Exception;

	@Test
	public abstract void testConditionLessThanWithRepeatable() throws Exception;

	@Test
	public abstract void testConditionLike() throws Exception;

	@Test
	public abstract void testConditionLikeWithLocale() throws Exception;

	@Test
	public abstract void testConditionLikeWithRepeatable() throws Exception;

	@Test
	public abstract void testConditionNotEquals() throws Exception;

	@Test
	public abstract void testConditionNotEqualsWithLocale() throws Exception;

	@Test
	public abstract void testConditionNotEqualsWithRepeatable()
		throws Exception;

	@Test
	public abstract void testConditionNotIn() throws Exception;

	@Test
	public abstract void testConditionNotInWithLocale() throws Exception;

	@Test
	public abstract void testConditionNotInWithRepeatable() throws Exception;

	@Test
	public abstract void testDateField() throws Exception;

	@Test
	public abstract void testDecimalField() throws Exception;

	@Test
	public abstract void testDocLibraryField() throws Exception;

	@Test
	public abstract void testIntegerField() throws Exception;

	@Test
	public abstract void testLinkToPageField() throws Exception;

	@Test
	public abstract void testNumberField() throws Exception;

	@Test
	public abstract void testRadioField() throws Exception;

	@Test
	public abstract void testSelectField() throws Exception;

	@Test
	public abstract void testTextField() throws Exception;

	protected long create(StorageAdapter storageAdapter, long ddmStructureId,
		Fields fields)
		throws Exception {

		return storageAdapter.create(TestPropsValues.getCompanyId(),
			ddmStructureId, fields, ServiceTestUtil.getServiceContext(group
				.getGroupId()));
	}

	protected String getDocLibraryFieldValue(FileEntry fileEntry) {

		StringBundler sb = new StringBundler(7);

		sb.append("{\"groupId\":");
		sb.append(fileEntry.getGroupId());
		sb.append(",\"uuid\":\"");
		sb.append(fileEntry.getUuid());
		sb.append("\",\"version\":\"");
		sb.append(fileEntry.getVersion());
		sb.append("\"}");

		return sb.toString();
	}

	protected abstract StorageAdapter getStorageAdapter();

	protected void testCondition(ConditionData conditionData) throws Exception {
		long ddmStructureId = _doCreateFieldData(conditionData);

		for (ExpectedResult expectedResult : conditionData.getExpectedResults()) {
			validateQueryResult(ddmStructureId, expectedResult.getCondition(),
				expectedResult.getResultCount());
		}
	}

	protected void validate(long ddmStructureId, Fields fields)
		throws Exception {

		JSONSerializer jsonSerializer = JSONFactoryUtil
			.createJSONSerializer();

		String expectedFieldsString = jsonSerializer.serializeDeep(fields);

		long classPK = create(getStorageAdapter(), ddmStructureId, fields);

		Fields actualFields = getStorageAdapter() .getFields(classPK);

		Assert.assertEquals(
			expectedFieldsString, jsonSerializer.serializeDeep(actualFields));
	}

	protected void validateQueryResult(long ddmStructureId,
		Condition condition, int expectedResultCount)
		throws Exception {

		int resultCount = getStorageAdapter().queryCount(ddmStructureId,
			condition);

		try {
			Assert.assertEquals(expectedResultCount, resultCount);
		}
		catch (Throwable t) {
			StringBuilder message = new StringBuilder();

			FieldCondition fieldCondition = (FieldCondition)condition;

			message.append("\nCONDITION NOT SATISFIED [\n");
			message.append("\t{\"condition\" : {\"comparisonOperator\" : \"");
			message.append(fieldCondition.getComparisonOperator());
			message.append("\", \"fieldName\" : \"");
			message.append(fieldCondition.getName());
			message.append("\", \"valueForTest\" : \"");
			message.append(fieldCondition.getValue());
			message.append("\", \"expectedResultCount\" : \"");
			message.append(expectedResultCount);
			message.append("\", \"resultCount\" : \"");
			message.append(resultCount);
			message.append("\"}}\n]");

			throw new Exception(message.toString(), t);
		}
	}

	protected long _classNameId = PortalUtil.getClassNameId(DDLRecordSet.class);
	protected Locale _enLocale = LocaleUtil.fromLanguageId("en_US");
	protected Locale _ptLocale = LocaleUtil.fromLanguageId("pt_BR");

	private void _doCreate(long ddmStructureId, Fields fields)
		throws Exception {

		create(getStorageAdapter(), ddmStructureId, fields);
	}

	private long _doCreateFieldData(ConditionData conditionData)
		throws Exception {

		String xsd = readText(conditionData.getStructureSchema());

		DDMStructure structure = addStructure(_classNameId, null, conditionData
			.getStructureName(), xsd, StorageType.XML.getValue(),
				DDMStructureConstants.TYPE_DEFAULT);

		Fields fields = new Fields();

		Map<Locale, List<Serializable>> dataMap = conditionData.getSampleValues();

		Field numberField = new Field(structure.getStructureId(), conditionData
				.getFieldName(), dataMap, _enLocale);

		fields.put(numberField);

		_doCreate(structure.getStructureId(), fields);

		return structure
			.getStructureId();
	}

}