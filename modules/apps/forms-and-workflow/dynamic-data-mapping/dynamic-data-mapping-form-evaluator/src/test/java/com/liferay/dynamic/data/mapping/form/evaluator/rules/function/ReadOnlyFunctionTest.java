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

package com.liferay.dynamic.data.mapping.form.evaluator.rules.function;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorContext;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function.ReadOnlyFunction;
import com.liferay.dynamic.data.mapping.form.evaluator.rules.DDMFormRuleEvaluatorBaseTest;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@RunWith(PowerMockRunner.class)
public class ReadOnlyFunctionTest extends DDMFormRuleEvaluatorBaseTest {

	@Test(expected = DDMFormEvaluationException.class)
	public void testInvalidParameters() throws Exception {
		List<DDMFormField> ddmFormFields = new ArrayList<>();
		DDMFormValues ddmFormValues = createDDMFormValues();
		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext =
			createDDMFormRuleEvaluatorContext(ddmFormFields, ddmFormValues);

		ReadOnlyFunction readOnlyFunction = new ReadOnlyFunction();

		readOnlyFunction.execute(
			ddmFormRuleEvaluatorContext, ListUtil.fromArray(new String[0]));
	}

	@Test
	public void testNotReadOnly() throws Exception {
		List<DDMFormField> ddmFormFields = new ArrayList<>();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field1", "text");

		ddmFormFields.add(fieldDDMFormField0);

		DDMFormValues ddmFormValues = createDDMFormValues();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext =
			createDDMFormRuleEvaluatorContext(ddmFormFields, ddmFormValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults =
				ddmFormRuleEvaluatorContext.getDDMFormFieldEvaluationResults();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("field1");

		ddmFormFieldEvaluationResult.setReadOnly(false);

		List<String> parameters = ListUtil.fromArray(
			new String[] {"isReadOnly(field1)", "isReadOnly", "field1"});

		ReadOnlyFunction readOnlyFunction = new ReadOnlyFunction();

		String expression = readOnlyFunction.execute(
			ddmFormRuleEvaluatorContext, parameters);

		Assert.assertEquals("FALSE", expression);
	}

	@Test
	public void testReadOnly() throws Exception {
		List<DDMFormField> ddmFormFields = new ArrayList<>();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmFormFields.add(fieldDDMFormField0);

		DDMFormValues ddmFormValues = createDDMFormValues();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("value0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext =
			createDDMFormRuleEvaluatorContext(ddmFormFields, ddmFormValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults =
				ddmFormRuleEvaluatorContext.getDDMFormFieldEvaluationResults();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("field0");

		ddmFormFieldEvaluationResult.setReadOnly(true);

		List<String> parameters = ListUtil.fromArray(
			new String[] {"isReadOnly(field0)", "isReadOnly", "field0"});

		ReadOnlyFunction readOnlyFunction = new ReadOnlyFunction();

		String expression = readOnlyFunction.execute(
			ddmFormRuleEvaluatorContext, parameters);

		Assert.assertEquals("TRUE", expression);
	}

}