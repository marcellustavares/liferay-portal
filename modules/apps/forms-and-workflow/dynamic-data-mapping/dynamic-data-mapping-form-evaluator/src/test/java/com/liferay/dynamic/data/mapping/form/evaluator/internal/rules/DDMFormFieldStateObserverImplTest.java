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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.rules;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.portal.kernel.util.StringPool;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormFieldStateObserverImplTest {

	@Before
	public void setUp() {
		Map<String, Map<String, DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		_ddmFormFieldInstanceEvaluationResultMap = new HashMap<>();

		ddmFormFieldEvaluationResults.put(
			"field1", _ddmFormFieldInstanceEvaluationResultMap);

		_ddmFormFieldStateObserverImpl = new DDMFormFieldStateObserverImpl(
			ddmFormFieldEvaluationResults);
	}

	@Test
	public void testGetDDMFormFieldValue1() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setValue("simple text");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		Assert.assertEquals(
			"simple text",
			_ddmFormFieldStateObserverImpl.getDDMFormFieldValue("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDDMFormFieldValue2() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setValue("simple text");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.getDDMFormFieldValue("field2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDDMFormFieldValue3() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setValue("simple text");

		_ddmFormFieldStateObserverImpl.getDDMFormFieldValue("field1");
	}

	@Test
	public void testIsReadOnly1() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setReadOnly(false);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		Assert.assertFalse(_ddmFormFieldStateObserverImpl.isReadOnly("field1"));
	}

	@Test
	public void testIsReadOnly2() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setReadOnly(true);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		Assert.assertTrue(_ddmFormFieldStateObserverImpl.isReadOnly("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsReadOnly3() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setReadOnly(true);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.isReadOnly("field2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsReadOnly4() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setReadOnly(true);

		_ddmFormFieldStateObserverImpl.isReadOnly("field1");
	}

	@Test
	public void testIsValid1() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setValid(false);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		Assert.assertFalse(_ddmFormFieldStateObserverImpl.isValid("field1"));
	}

	@Test
	public void testIsValid2() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setValid(true);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		Assert.assertTrue(_ddmFormFieldStateObserverImpl.isValid("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsValid3() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setValid(true);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.isValid("field2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsValid4() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setValid(true);

		_ddmFormFieldStateObserverImpl.isValid("field1");
	}

	@Test
	public void testIsVisible1() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setVisible(false);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		Assert.assertFalse(_ddmFormFieldStateObserverImpl.isVisible("field1"));
	}

	@Test
	public void testIsVisible2() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setVisible(true);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		Assert.assertTrue(_ddmFormFieldStateObserverImpl.isVisible("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsVisible3() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setVisible(true);

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.isVisible("field2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsVisible4() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		ddmFormFieldEvaluationResult.setVisible(true);

		_ddmFormFieldStateObserverImpl.isVisible("field1");
	}

	@Test
	public void testSetReadOnly1() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setReadOnly("field1", false);

		Assert.assertFalse(_ddmFormFieldStateObserverImpl.isReadOnly("field1"));
	}

	@Test
	public void testSetReadOnly2() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setReadOnly("field1", true);

		Assert.assertTrue(_ddmFormFieldStateObserverImpl.isReadOnly("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetReadOnly3() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setReadOnly("field2", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetReadOnly4() throws Exception {
		_ddmFormFieldStateObserverImpl.setReadOnly("field1", false);
	}

	@Test
	public void testSetValid1() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setValid("field1", false);

		Assert.assertFalse(_ddmFormFieldStateObserverImpl.isValid("field1"));
	}

	@Test
	public void testSetValid2() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setValid("field1", true);

		Assert.assertTrue(_ddmFormFieldStateObserverImpl.isValid("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetValid3() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setValid("field2", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetValid4() throws Exception {
		_ddmFormFieldStateObserverImpl.setValid("field1", false);
	}

	@Test
	public void testSetVisible1() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setVisible("field1", false);

		Assert.assertFalse(_ddmFormFieldStateObserverImpl.isVisible("field1"));
	}

	@Test
	public void testSetVisible2() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setVisible("field1", true);

		Assert.assertTrue(_ddmFormFieldStateObserverImpl.isVisible("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetVisible3() throws Exception {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			createDefaultDDMFormFieldEvaluationResult("field1_instance");

		_ddmFormFieldInstanceEvaluationResultMap.put(
			"field1_instance", ddmFormFieldEvaluationResult);

		_ddmFormFieldStateObserverImpl.setVisible("field2", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetVisible4() throws Exception {
		_ddmFormFieldStateObserverImpl.setValid("field1", false);
	}

	protected DDMFormFieldEvaluationResult
		createDefaultDDMFormFieldEvaluationResult(String instanceId) {

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			new DDMFormFieldEvaluationResult("field1", instanceId);

		ddmFormFieldEvaluationResult.setErrorMessage(StringPool.BLANK);
		ddmFormFieldEvaluationResult.setReadOnly(false);
		ddmFormFieldEvaluationResult.setValid(true);
		ddmFormFieldEvaluationResult.setVisible(true);

		return ddmFormFieldEvaluationResult;
	}

	private Map<String, DDMFormFieldEvaluationResult>
		_ddmFormFieldInstanceEvaluationResultMap;
	private DDMFormFieldStateObserverImpl _ddmFormFieldStateObserverImpl;

}