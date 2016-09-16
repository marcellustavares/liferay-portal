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
public class SetInvalidFunctionTest {

	@Test
	public void testEvaluate() throws Exception {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResultList =
			new ArrayList<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult1 =
			new DDMFormFieldEvaluationResult("field0", null);

		ddmFormFieldEvaluationResult1.setValue(true);

		ddmFormFieldEvaluationResultList.add(ddmFormFieldEvaluationResult1);

		ddmFormFieldEvaluationResults.put(
			"field0", ddmFormFieldEvaluationResultList);

		SetInvalidFunction setInvalidFunction = new SetInvalidFunction(
			ddmFormFieldEvaluationResults);

		setInvalidFunction.evaluate("field0", "error");

		Assert.assertFalse(ddmFormFieldEvaluationResult1.isValid());
		Assert.assertEquals(
			"error", ddmFormFieldEvaluationResult1.getErrorMessage());
	}

	@Test
	public void testEvaluateWithNoResultForField() throws Exception {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResultList =
			new ArrayList<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult1 =
			new DDMFormFieldEvaluationResult("field1", null);

		ddmFormFieldEvaluationResult1.setValid(true);
		ddmFormFieldEvaluationResult1.setErrorMessage(null);

		ddmFormFieldEvaluationResultList.add(ddmFormFieldEvaluationResult1);

		ddmFormFieldEvaluationResults.put(
			"field1", ddmFormFieldEvaluationResultList);

		SetInvalidFunction setInvalidFunction = new SetInvalidFunction(
			ddmFormFieldEvaluationResults);

		setInvalidFunction.evaluate("not_available", "error");

		Assert.assertTrue(ddmFormFieldEvaluationResult1.isValid());
		Assert.assertEquals(
			StringPool.BLANK, ddmFormFieldEvaluationResult1.getErrorMessage());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() throws Exception {
		SetInvalidFunction setInvalidFunction = new SetInvalidFunction(null);

		setInvalidFunction.evaluate("param1");
	}

}