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

package com.liferay.dynamic.data.mapping.form.rule;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.internal.DDMExpressionFactoryImpl;
import com.liferay.dynamic.data.mapping.form.rule.internal.DDMFormRuleEvaluatorContext;
import com.liferay.dynamic.data.mapping.form.rule.internal.DDMFormRuleEvaluatorImpl;
import com.liferay.dynamic.data.mapping.form.rule.internal.functions.CallFunction;
import com.liferay.dynamic.data.mapping.form.rule.internal.functions.FunctionFactory;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRule;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.json.JSONArrayImpl;
import com.liferay.portal.json.JSONObjectImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * @author Leonardo Barros
 */
@PrepareForTest({FunctionFactory.class})
@RunWith(PowerMockRunner.class)
public class DDMFormRuleEvaluatorImplTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		//setUpDDMFormRuleFunctionFactory();
		setUpDDMFormRuleEvaluator();
		setUpLanguageUtil();
	}

	@Test
	public void testEvaluateBetween() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"between(field1,10,field0)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("30"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("15"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(2, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"30", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"15", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateContains() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"contains(field1,\"val\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("field0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(2, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"field0", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateEquals() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"equals(field1,\"value1\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("field0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(2, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"field0", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	

	@Test
	public void testEvaluateNotBetween() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"between(field1,25,field0)", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("30"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("15"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(2, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"30", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"15", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateNotEquals() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"equals(field1,\"value2\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("field0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(2, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"field0", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateNotReadOnly() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"equals(field0,\"read-only\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("value0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(1, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"value0", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateReadOnly2() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"isReadOnly(field0)", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField2.addDDMFormFieldRule(ddmFormFieldRule2);

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"isVisible(field2)", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"contains(field0,\"hello\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0",
				new UnlocalizedValue("hello world"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("value2"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(3, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"hello world", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field2")) {

				Assert.assertEquals(
					"value2", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateVisible() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"equals(field0,\"test\")", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"isVisible(field1)", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField2.addDDMFormFieldRule(ddmFormFieldRule2);

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("test"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("value2"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(3, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"test", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field2")) {

				Assert.assertEquals(
					"value2", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateWithDifferentFunctions() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"equals(field1,\"test\") && not(contains(field2,\"hello\"))",
			DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue(""));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("test"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2",
				new UnlocalizedValue("hello world"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(3, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"test", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field2")) {

				Assert.assertEquals(
					"hello world", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateCalculatedValueWithNoInput() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"field1 * field2", DDMFormFieldRuleType.VALUE);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("test"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("10"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue(""));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(3, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					LanguageUtil.format(
						LocaleUtil.US, "the-value-of-field-was-not-entered-x",
						"field2", false),
					ddmFormFieldRuleEvaluationResult.getErrorMessage());
				Assert.assertEquals(
					"", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertFalse(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"10", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field2")) {

				Assert.assertEquals(
					"", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateCalculatedValue() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"field1 * field2", DDMFormFieldRuleType.VALUE);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("test"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("10"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("5"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(3, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"50.0", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"10", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field2")) {

				Assert.assertEquals(
					"5", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}

	@Test
	public void testEvaluateVisible2() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"isVisible(field1)", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField2.addDDMFormFieldRule(ddmFormFieldRule2);

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"equals(field0,\"test\")", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("test"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("value2"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(3, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"test", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field2")) {

				Assert.assertEquals(
					"value2", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateNotReadOnly2() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"isReadOnly(field1)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"isReadOnly(field3)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"equals(field0,\"value0\") && contains(field3,\"world\")",
			DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField2.addDDMFormFieldRule(ddmFormFieldRule2);

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormField fieldDDMFormField3 = new DDMFormField("field3", "text");

		DDMFormFieldRule ddmFormFieldRule3 = new DDMFormFieldRule(
			"equals(field1,field2)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField3.addDDMFormFieldRule(ddmFormFieldRule3);

		ddmForm.addDDMFormField(fieldDDMFormField3);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("value0"));

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("test"));

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("test"));

		DDMFormFieldValue fieldDDMFormFieldValue3 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field3_instanceId", "field3",
				new UnlocalizedValue("hello world"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);
		ddmFormFieldValues.add(fieldDDMFormFieldValue1);
		ddmFormFieldValues.add(fieldDDMFormFieldValue2);
		ddmFormFieldValues.add(fieldDDMFormFieldValue3);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(4, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"value0", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if (
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"test", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if (ddmFormFieldRuleEvaluationResult.getName(
						).equals("field2")) {

				Assert.assertEquals(
					"test", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if (ddmFormFieldRuleEvaluationResult.getName(
						).equals("field3")) {

				Assert.assertEquals(
					"hello world", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateReadOnly() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"equals(field0,\"read-only\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"contains(field1,\"nothing\")", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"isReadOnly(field0)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule2);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0",
				new UnlocalizedValue("read-only"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(2, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"read-only", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testCallDataProvider1() throws Exception {
		JSONArray jsonArray = new JSONArrayImpl();

		JSONObject jsonObject1 = new JSONObjectImpl();
		jsonArray.put(jsonObject1);

		jsonObject1.put("countryId", "1");
		jsonObject1.put("nameCurrentValue", "United States");

		JSONObject jsonObject2 = new JSONObjectImpl();
		jsonArray.put(jsonObject2);

		jsonObject2.put("countryId", "2");
		jsonObject2.put("nameCurrentValue", "Brazil");

		Method method = Whitebox.getMethod(
			CallFunction.class, "executeDataProvider",
			DDMFormRuleEvaluatorContext.class, Long.class, String.class);

		doReturn(jsonArray).when(_ddmFormRuleCallFunction, method).
			withArguments(
				Matchers.any(
					DDMFormRuleEvaluatorContext.class), Matchers.anyLong(),
					Matchers.anyString());

		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("country", "select");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"call(1,\"\",\"country={\"key\":\"countryId\"," +
				"\"value\":\"nameCurrentValue\"}\")",
			DDMFormFieldRuleType.DATA_PROVIDER);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"country_instanceId", "country", new UnlocalizedValue(""));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(1, ddmFormFieldRuleEvaluationResults.size());

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResults.get(0);

		Assert.assertEquals(
			JSONArray.class,
			ddmFormFieldRuleEvaluationResult.getValue().getClass());

		Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
		Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
		Assert.assertFalse(ddmFormFieldRuleEvaluationResult.isReadOnly());
	}

	@Test
	public void testCallDataProvider2() throws Exception {
		JSONArray jsonArray = new JSONArrayImpl();
		JSONObject jsonObject = new JSONObjectImpl();
		jsonArray.put(jsonObject);

		jsonObject.put("localidade", "Recife");
		jsonObject.put("logradouro", "Praça de Casa Forte");

		when(_ddmFormRuleCallFunction, "executeDataProvider",
			Matchers.any(), Matchers.any(),
			Matchers.any()).thenReturn(jsonArray);

		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("cep", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"call(2,\"cep=cep\",\"rua=logradouro;cidade=localidade\")",
			DDMFormFieldRuleType.DATA_PROVIDER);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormField fieldDDMFormField1 = new DDMFormField("rua", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"TRUE", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule2);

		DDMFormField fieldDDMFormField2 = new DDMFormField("cidade", "text");

		DDMFormFieldRule ddmFormFieldRule3 = new DDMFormFieldRule(
			"isReadOnly(rua)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField2.addDDMFormFieldRule(ddmFormFieldRule3);

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"cep_instanceId", "cep", new UnlocalizedValue("52061420"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"rua_instanceId", "rua", new UnlocalizedValue(""));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"cidade_instanceId", "cidade", new UnlocalizedValue(""));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(3, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("cep")) {
				Assert.assertEquals(
					"52061420", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(ddmFormFieldRuleEvaluationResult.getName().equals("rua")) {
				Assert.assertEquals(
					jsonObject.get("logradouro"),
					ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("cidade")) {
				Assert.assertEquals(
					jsonObject.get("localidade"),
					ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateNotVisible() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"equals(field0,\"nothing\")", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("value0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(2, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"value0", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	@Test
	public void testEvaluateNotContains() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"contains(field1,\"hello\")", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"contains(field0,\"world\")", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("value0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults =
				_ddmFormRuleEvaluatorImpl.evaluate(
					ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(2, ddmFormFieldRuleEvaluationResults.size());

		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("field0")) {
				Assert.assertEquals(
					"value0", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("field1")) {

				Assert.assertEquals(
					"value1", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}
	
	protected DDMFormFieldValue createDDMFormFieldValue(
		String instanceId, String name, String value) {

		return DDMFormValuesTestUtil.createDDMFormFieldValue(
			instanceId, name, new UnlocalizedValue(value));
	}

	protected void setUpDDMFormRuleEvaluator() throws Exception {
		_ddmFormRuleEvaluatorImpl = new DDMFormRuleEvaluatorImpl();

		field(DDMFormRuleEvaluatorImpl.class, "_ddmExpressionFactory").set(
			_ddmFormRuleEvaluatorImpl, _ddmExpressionFactory);
	}

	protected void setUpDDMFormRuleFunctionFactory() throws Exception {
		mockStatic(FunctionFactory.class);

		when(FunctionFactory.class, "getFunction", "call").
			thenReturn(_ddmFormRuleCallFunction);

		when(
			FunctionFactory.class, "getFunction",
			Matchers.anyString()).thenCallRealMethod();

		when(FunctionFactory.class, "getFunctionPatterns").thenCallRealMethod();
	}

	protected void setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(_language);
	}

	private final DDMExpressionFactory _ddmExpressionFactory =
		new DDMExpressionFactoryImpl();

	@Mock
	private CallFunction _ddmFormRuleCallFunction;

	private DDMFormRuleEvaluatorImpl _ddmFormRuleEvaluatorImpl;

	@Mock
	private Language _language;

}