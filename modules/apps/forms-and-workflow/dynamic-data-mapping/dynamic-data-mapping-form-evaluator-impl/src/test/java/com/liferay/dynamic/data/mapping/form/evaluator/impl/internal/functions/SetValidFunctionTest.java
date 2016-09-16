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
public class SetValidFunctionTest {

	@Test
	public void testEvaluate() throws Exception {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResultList =
			new ArrayList<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult1 =
			new DDMFormFieldEvaluationResult("field0", null);

		ddmFormFieldEvaluationResult1.setValid(false);

		ddmFormFieldEvaluationResultList.add(ddmFormFieldEvaluationResult1);

		ddmFormFieldEvaluationResults.put(
			"field0", ddmFormFieldEvaluationResultList);

		SetValidFunction setValidFunction = new SetValidFunction(
			ddmFormFieldEvaluationResults);

		setValidFunction.evaluate("field0");

		Assert.assertTrue(ddmFormFieldEvaluationResult1.isValid());
	}

	@Test
	public void testEvaluateWithNoResultForField() throws Exception {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResultList =
			new ArrayList<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult1 =
			new DDMFormFieldEvaluationResult("field1", null);

		ddmFormFieldEvaluationResult1.setValid(false);

		ddmFormFieldEvaluationResultList.add(ddmFormFieldEvaluationResult1);

		ddmFormFieldEvaluationResults.put(
			"field1", ddmFormFieldEvaluationResultList);

		SetValidFunction setValidFunction = new SetValidFunction(
			ddmFormFieldEvaluationResults);

		setValidFunction.evaluate("not_available");

		Assert.assertFalse(ddmFormFieldEvaluationResult1.isValid());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument1() throws Exception {
		SetValidFunction setValidFunction = new SetValidFunction(null);

		setValidFunction.evaluate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument2() throws Exception {
		SetValidFunction setValidFunction = new SetValidFunction(null);

		setValidFunction.evaluate("param1", "param2");
	}

}