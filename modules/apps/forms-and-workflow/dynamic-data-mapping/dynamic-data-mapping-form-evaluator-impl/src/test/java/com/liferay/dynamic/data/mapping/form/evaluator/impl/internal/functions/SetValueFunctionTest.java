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

package com.liferay.dynamic.data.mapping.form.evaluator.impl.internal.functions;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class SetValueFunctionTest {

	@Test
	public void testEvaluate() throws Exception {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResultList =
			new ArrayList<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult1 =
			new DDMFormFieldEvaluationResult("field0", null);

		ddmFormFieldEvaluationResult1.setValue(1);

		ddmFormFieldEvaluationResultList.add(ddmFormFieldEvaluationResult1);

		ddmFormFieldEvaluationResults.put(
			"field0", ddmFormFieldEvaluationResultList);

		SetValueFunction setValueFunction = new SetValueFunction(
			ddmFormFieldEvaluationResults);

		setValueFunction.evaluate("field0", 2);

		Assert.assertEquals(
			Integer.valueOf(2), ddmFormFieldEvaluationResult1.getValue());
	}

	@Test
	public void testEvaluateWithNoResultForField() throws Exception {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResultList =
			new ArrayList<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult1 =
			new DDMFormFieldEvaluationResult("field1", null);

		ddmFormFieldEvaluationResult1.setValue("EMPTY");

		ddmFormFieldEvaluationResultList.add(ddmFormFieldEvaluationResult1);

		ddmFormFieldEvaluationResults.put(
			"field1", ddmFormFieldEvaluationResultList);

		SetValueFunction setValueFunction = new SetValueFunction(
			ddmFormFieldEvaluationResults);

		setValueFunction.evaluate("not_available", "");

		Assert.assertEquals("EMPTY", ddmFormFieldEvaluationResult1.getValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() throws Exception {
		SetValueFunction setValueFunction = new SetValueFunction(null);

		setValueFunction.evaluate("param1");
	}

}