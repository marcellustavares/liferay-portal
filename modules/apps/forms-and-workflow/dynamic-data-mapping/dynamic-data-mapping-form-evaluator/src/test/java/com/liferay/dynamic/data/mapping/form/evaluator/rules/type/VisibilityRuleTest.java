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

package com.liferay.dynamic.data.mapping.form.evaluator.rules.type;

import com.liferay.dynamic.data.mapping.expression.internal.DDMExpressionFactoryImpl;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.type.VisibilityRule;
import com.liferay.dynamic.data.mapping.form.evaluator.rules.DDMFormRuleEvaluatorBaseTest;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class VisibilityRuleTest extends DDMFormRuleEvaluatorBaseTest {

	@Test
	public void testNotVisible() throws Exception {
		List<DDMFormField> ddmFormFields = new ArrayList<>();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field1", "text");

		ddmFormFields.add(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field2", "text");

		ddmFormFields.add(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field3", "text");

		ddmFormFields.add(fieldDDMFormField2);

		DDMFormValues ddmFormValues = createDDMFormValues();

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("2"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field3_instanceId", "field3", new UnlocalizedValue("3"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField1, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField2, ddmFormValues, ddmFormFieldEvaluationResults);

		VisibilityRule visibilityRule = new VisibilityRule(
			"field3 > (field1 + field2)", new DDMExpressionFactoryImpl(), null,
			null, ddmFormFieldEvaluationResults, "field3", null,
			StringPool.BLANK);

		visibilityRule.evaluate();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("field3");

		Assert.assertFalse(ddmFormFieldEvaluationResult.isVisible());
	}

	@Test
	public void testNotVisible2() throws Exception {
		List<DDMFormField> ddmFormFields = new ArrayList<>();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field1", "text");

		ddmFormFields.add(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field2", "text");

		ddmFormFields.add(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field3", "text");

		ddmFormFields.add(fieldDDMFormField2);

		DDMFormField fieldDDMFormField3 = new DDMFormField("field4", "text");

		ddmFormFields.add(fieldDDMFormField3);

		DDMFormValues ddmFormValues = createDDMFormValues();

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("2"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field3_instanceId", "field3", new UnlocalizedValue("3"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		DDMFormFieldValue fieldDDMFormFieldValue3 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field4_instanceId", "field4", new UnlocalizedValue("3"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue3);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField1, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField2, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField3, ddmFormValues, ddmFormFieldEvaluationResults);

		VisibilityRule visibilityRule = new VisibilityRule(
			"field1 * field2 >= (field3 + field4)",
			new DDMExpressionFactoryImpl(), null, null,
			ddmFormFieldEvaluationResults, "field3", null, StringPool.BLANK);

		visibilityRule.evaluate();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("field3");

		Assert.assertFalse(ddmFormFieldEvaluationResult.isVisible());
	}

	@Test
	public void testVisible() throws Exception {
		List<DDMFormField> ddmFormFields = new ArrayList<>();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field1", "text");

		ddmFormFields.add(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field2", "text");

		ddmFormFields.add(fieldDDMFormField1);

		DDMFormValues ddmFormValues = createDDMFormValues();

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("value2"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField1, ddmFormValues, ddmFormFieldEvaluationResults);

		VisibilityRule visibilityRule = new VisibilityRule(
			"equals(field1,\"value1\")", new DDMExpressionFactoryImpl(), null,
			null, ddmFormFieldEvaluationResults, "field2", null,
			StringPool.BLANK);

		visibilityRule.evaluate();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("field2");

		Assert.assertTrue(ddmFormFieldEvaluationResult.isVisible());
	}

	@Test
	public void testVisible2() throws Exception {
		List<DDMFormField> ddmFormFields = new ArrayList<>();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field1", "text");

		ddmFormFields.add(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field2", "text");

		ddmFormFields.add(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field3", "text");

		ddmFormFields.add(fieldDDMFormField2);

		DDMFormValues ddmFormValues = createDDMFormValues();

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("value2"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field3_instanceId", "field3", new UnlocalizedValue("value3"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField1, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField2, ddmFormValues, ddmFormFieldEvaluationResults);

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("field3");

		ddmFormFieldEvaluationResult.setReadOnly(false);

		VisibilityRule visibilityRule = new VisibilityRule(
			"equals(field1,\"value1\") && not(isReadOnly(field3))",
			new DDMExpressionFactoryImpl(), null, null,
			ddmFormFieldEvaluationResults, "field2", null, StringPool.BLANK);

		visibilityRule.evaluate();

		ddmFormFieldEvaluationResult = ddmFormFieldEvaluationResults.get(
			"field2");

		Assert.assertTrue(ddmFormFieldEvaluationResult.isVisible());
	}

}