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

package com.liferay.dynamic.data.mapping.expression.internal;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Marcellus Tavares
 */
public class FunctionEvaluationTest {

	@Test
	public void testFunctionEval1() throws Exception {
		int expected = Math.abs(-5);

		DDMExpression<Number> ddmExpression = new DDMExpressionImpl<>(
			"abs(-5)", Number.class);

		ddmExpression.setDDMExpressionFunction(
			"abs", (params) -> Math.abs((Double)params[0]));

		Number actual = ddmExpression.evaluate();

		Assert.assertEquals(expected, actual.intValue());
	}

	@Test
	public void testFunctionEval2() throws Exception{
		DDMExpression<Boolean> ddmExpression = new DDMExpressionImpl<>(
			"not (length(\"123\") > length(\"1\"))", Boolean.class);

		ddmExpression.setDDMExpressionFunction(
			"length", (params) -> ((String)params[0]).length());

		Assert.assertEquals(false, ddmExpression.evaluate());
	}

	@Test(expected = DDMExpressionException.FunctionNotDefined.class)
	public void testFunctionEval3() throws Exception {
		DDMExpression<Number> ddmExpression = new DDMExpressionImpl<>(
			"time()", Number.class);

		ddmExpression.evaluate();
	}

	@Test
	public void testFunctionEval4() throws Exception {
		DDMExpression<Boolean> ddmExpression = new DDMExpressionImpl<>(
			"pow(2, 4) > (16 - 1)", Boolean.class);

		ddmExpression.setDDMExpressionFunction(
			"pow", (params) -> Math.pow((Double)params[0], (Double)params[1]));

		Assert.assertTrue(ddmExpression.evaluate());
	}

	@Test
	public void testFunctionEval5() throws Exception{
		double expected = Math.pow(2., Math.pow(2., Math.pow(2., 4.)));

		DDMExpression<Double> ddmExpression = new DDMExpressionImpl<>(
			"pow(2., pow(2., pow(2.,4.)))", Double.class);

		ddmExpression.setDDMExpressionFunction(
			"pow",
			(params) -> Math.pow((Double)params[0], (Double)params[1]));

		double actual = ddmExpression.evaluate();

		Assert.assertEquals(expected, actual, 0.01);
	}

}