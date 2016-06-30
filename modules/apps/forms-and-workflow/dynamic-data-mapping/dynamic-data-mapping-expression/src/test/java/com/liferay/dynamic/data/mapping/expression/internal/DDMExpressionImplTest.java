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

import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Marcellus Tavares
 */
public class DDMExpressionImplTest {

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyExpressionString() throws Exception {
		new DDMExpressionImpl<>(null, Number.class);
	}

	@Test
	public void testGetFunctionNames() throws Exception {
		DDMExpressionImpl ddmExpressionImpl =
			new DDMExpressionImpl<>("pow(pow(log(y))) + sum(3, 4)",
				Number.class);

		Set<String> expectedFunctionNames = new HashSet<>(
			Arrays.asList("pow", "log", "sum"));

		Assert.assertEquals(
			expectedFunctionNames, ddmExpressionImpl.getFunctionNames());
	}

	@Test
	public void testGetVariableNames1() throws Exception {
		DDMExpressionImpl ddmExpressionImpl = new DDMExpressionImpl<>(
			"(var1 + var2_) * __var3", Number.class);

		Set<String> expectedVariableNames = new HashSet<>(
			Arrays.asList("var1", "var2_", "__var3"));

		Assert.assertEquals(
			expectedVariableNames, ddmExpressionImpl.getVariableNames());
	}

	@Test
	public void testGetVariableNames2() throws Exception {
		DDMExpressionImpl ddmExpressionImpl =
			new DDMExpressionImpl<>("(((1+2)*(1-2/x))+log(1*6-y))",
				Number.class);

		Set<String> expectedVariableNames = new HashSet<>(
			Arrays.asList("x", "y"));

		Assert.assertEquals(
			expectedVariableNames, ddmExpressionImpl.getVariableNames());
	}

	@Test(expected = DDMExpressionException.InvalidSyntax.class)
	public void testInvalidExpressionSyntax1() throws Exception {
		new DDMExpressionImpl<>("((", Number.class);
	}

	@Test(expected = DDMExpressionException.InvalidSyntax.class)
	public void testInvalidExpressionSyntax2() throws Exception {
		new DDMExpressionImpl<>("(())", Number.class);
	}

	@Test(expected = DDMExpressionException.InvalidSyntax.class)
	public void testInvalidExpressionSyntax3() throws Exception {
		new DDMExpressionImpl<>(")", Number.class);
	}

	@Test(expected = DDMExpressionException.InvalidSyntax.class)
	public void testInvalidExpressionSyntax4() throws Exception {
		new DDMExpressionImpl<>("+-/i", Number.class);
	}

	@Test(expected = DDMExpressionException.InvalidSyntax.class)
	public void testInvalidExpressionSyntax5() throws Exception {
		new DDMExpressionImpl<>("-----(", Number.class);
	}

	@Test
	public void testReturnLongWithNumber() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Long.class);

		Number number = 1l;

		Object result = ddmExpression.toRetunType(number);

		Assert.assertTrue(result instanceof Long);
		Assert.assertEquals(1l, (long)result);
	}

	@Test
	public void testReturnNumberWithNumber() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Number.class);

		Number number = 42;

		Object result = ddmExpression.toRetunType(number);

		Assert.assertTrue(result instanceof Number);
		Assert.assertEquals(number, result);
	}

	@Test
	public void testReturnTypeBooleanWithBoolean() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Boolean.class);

		Object result = ddmExpression.toRetunType(true);

		Assert.assertTrue(result instanceof Boolean);
		Assert.assertEquals(true, result);
	}

	@Test(expected = DDMExpressionException.IncompatipleReturnType.class)
	public void testReturnTypeBooleanWithNumber() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Boolean.class);

		ddmExpression.toRetunType(RandomTestUtil.randomLong());
	}

	@Test(expected = DDMExpressionException.IncompatipleReturnType.class)
	public void testReturnTypeBooleanWithString() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Boolean.class);

		ddmExpression.toRetunType(StringUtil.randomString());
	}

	@Test
	public void testReturnTypeDoubleWithNumber() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Double.class);

		Number number = 1.5;

		Object result = ddmExpression.toRetunType(number);

		Assert.assertTrue(result instanceof Double);
		Assert.assertEquals(1.5, (double)result, 0.1);
	}

	@Test
	public void testReturnTypeFloatWithNumber() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Float.class);

		Number number = 1.5f;

		Object result = ddmExpression.toRetunType(number);

		Assert.assertTrue(result instanceof Float);
		Assert.assertEquals(1.5, (float)result, 0.1);
	}

	@Test
	public void testReturnTypeIntegerWithNumber() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Integer.class);

		Number number = 1;

		Object result = ddmExpression.toRetunType(number);

		Assert.assertTrue(result instanceof Integer);
		Assert.assertEquals(1, (int)result);
	}

	@Test(expected = DDMExpressionException.IncompatipleReturnType.class)
	public void testReturnTypeNumberWithBoolean() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Number.class);

		ddmExpression.toRetunType(false);
	}

	@Test(expected = DDMExpressionException.IncompatipleReturnType.class)
	public void testReturnTypeNumberWithString() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", Number.class);

		ddmExpression.toRetunType(StringUtil.randomString());
	}

	@Test
	public void testReturnTypeStringWithBoolean() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", String.class);

		Object result = ddmExpression.toRetunType(false);

		Assert.assertTrue(result instanceof String);
		Assert.assertEquals("false", result);
	}

	@Test
	public void testReturnTypeStringWithNumber() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", String.class);

		Object result = ddmExpression.toRetunType(42);

		Assert.assertTrue(result instanceof String);
		Assert.assertEquals("42", result);
	}

	@Test
	public void testReturnTypeStringWithString() throws Exception {
		DDMExpressionImpl ddmExpression = new DDMExpressionImpl<>(
			"true", String.class);

		Object result = ddmExpression.toRetunType("Joe");

		Assert.assertTrue(result instanceof String);
		Assert.assertEquals("Joe", result);
	}

}