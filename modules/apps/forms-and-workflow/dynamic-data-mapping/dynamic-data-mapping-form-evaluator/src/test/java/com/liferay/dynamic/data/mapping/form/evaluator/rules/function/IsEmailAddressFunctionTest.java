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
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function.IsEmailAddress;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function.IsURLFunction;
import com.liferay.dynamic.data.mapping.form.evaluator.rules.DDMFormRuleEvaluatorBaseTest;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
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
public class IsEmailAddressFunctionTest extends DDMFormRuleEvaluatorBaseTest {

	@Test(expected = DDMFormEvaluationException.class)
	public void testInvalidParameters() throws Exception {
		IsEmailAddress isEmailAddress = new IsEmailAddress();

		isEmailAddress.execute(
			null, null, null, null, ListUtil.fromArray(new String[0]));
	}

	@Test
	public void testIsEmailAddress() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormValues ddmFormValues = createDDMFormValues();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1",
				new UnlocalizedValue("test@liferay.com"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("field1");

		ddmFormFieldEvaluationResult.setReadOnly(false);

		List<String> parameters = ListUtil.fromArray(
			new String[] {
				"isEmailAddress(field1)", "isEmailAddress", "field1"
			});

		IsEmailAddress isEmailAddress = new IsEmailAddress();

		String expression = isEmailAddress.execute(
			null, null, ddmFormFieldEvaluationResults, null, parameters);

		Assert.assertEquals("TRUE", expression);
	}

	@Test
	public void testIsNotEmailAddress() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormValues ddmFormValues = createDDMFormValues();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("field1");

		ddmFormFieldEvaluationResult.setReadOnly(false);

		List<String> parameters = ListUtil.fromArray(
			new String[] {
				"isEmailAddress(field1)", "isEmailAddress", "field1"
			});

		IsURLFunction isURLFunction = new IsURLFunction();

		String expression = isURLFunction.execute(
			null, null, ddmFormFieldEvaluationResults, null, parameters);

		Assert.assertEquals("FALSE", expression);
	}

}