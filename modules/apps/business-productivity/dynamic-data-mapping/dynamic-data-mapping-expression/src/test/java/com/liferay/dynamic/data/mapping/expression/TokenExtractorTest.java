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

package com.liferay.dynamic.data.mapping.expression;

import com.liferay.dynamic.data.mapping.expression.internal.TokenExtractor;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcellus Tavares
 */
public class TokenExtractorTest {

	@Before
	public void setUp() {
		_tokenExtractor = new TokenExtractor();
	}

	@Test
	public void testExpressionWithConstantsOnly() throws Exception {
		String expressionString = "(1 + 2) * 3";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 3);

		Collection<String> values = variableMap.values();

		Assert.assertTrue(values.contains("1"));
		Assert.assertTrue(values.contains("2"));
		Assert.assertTrue(values.contains("3"));
	}

	@Test
	public void testExpressionWithEmptyExpression() throws Exception {
		Map<String, String> variableMap = _tokenExtractor.extract(
			StringPool.BLANK);
		Assert.assertNull(_tokenExtractor.getExpression());
		Assert.assertEquals(variableMap.size(), 0);
	}

	@Test
	public void testExpressionWithFunctionCall() throws Exception {
		String expressionString =
			"equals(name,\"joe\") && isEmailAddress(email)";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 3);

		Assert.assertEquals("name", variableMap.get("name"));
		Assert.assertEquals("email", variableMap.get("email"));

		Collection<String> values = variableMap.values();

		Assert.assertTrue(values.contains("joe"));
	}

	@Test
	public void testExpressionWithFunctionCallVariablesAndConstants()
		throws Exception {

		String expressionString = "(a + b) * 3456.12 > 10000 && isURL(field)";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 5);

		Assert.assertEquals("a", variableMap.get("a"));
		Assert.assertEquals("b", variableMap.get("b"));
		Assert.assertEquals("field", variableMap.get("field"));

		Collection<String> values = variableMap.values();

		Assert.assertTrue(values.contains("3456.12"));
		Assert.assertTrue(values.contains("10000"));
	}

	@Test
	public void testExpressionWithNoConstants() throws Exception {
		String expressionString = "(a / b) >= c";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 3);

		Assert.assertEquals("a", variableMap.get("a"));
		Assert.assertEquals("b", variableMap.get("b"));
		Assert.assertEquals("c", variableMap.get("c"));
	}

	@Test(expected = DDMExpressionEvaluationException.class)
	public void testExpressionWithNotSupportedFunction() throws Exception {
		String expressionString = "round(a / b) && sqrt(99999)";

		_tokenExtractor.extract(expressionString);
	}

	@Test
	public void testExpressionWithNoVariablesAndConstants() throws Exception {
		String expressionString = "+ > >= && !=";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 0);
	}

	@Test
	public void testExpressionWithNullExpression() throws Exception {
		Map<String, String> variableMap = _tokenExtractor.extract(null);
		Assert.assertNull(_tokenExtractor.getExpression());
		Assert.assertEquals(variableMap.size(), 0);
	}

	@Test
	public void testExpressionWithoutCallingExtractMethod() throws Exception {
		Assert.assertNull(_tokenExtractor.getExpression());
		Assert.assertNull(_tokenExtractor.getVariableMap());
	}

	@Test
	public void testExpressionWithStringConstants() throws Exception {
		String expressionString = "name != \"joe\"";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 2);

		Assert.assertEquals("name", variableMap.get("name"));

		Collection<String> values = variableMap.values();

		Assert.assertTrue(values.contains("joe"));
	}

	@Test
	public void testExpressionWithVariablesAndConstants() throws Exception {
		String expressionString = "((a + b) * variable_3) / var4  > 5";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 5);
		Assert.assertEquals("a", variableMap.get("a"));
		Assert.assertEquals("b", variableMap.get("b"));
		Assert.assertEquals("variable_3", variableMap.get("variable_3"));
		Assert.assertEquals("var4", variableMap.get("var4"));

		Collection<String> values = variableMap.values();

		Assert.assertTrue(values.contains("5"));
	}

	private TokenExtractor _tokenExtractor;

}