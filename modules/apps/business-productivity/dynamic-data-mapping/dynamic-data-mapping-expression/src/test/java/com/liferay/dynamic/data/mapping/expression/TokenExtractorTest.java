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
	public void testExpressionWithConstantsOnly() {
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
	public void testExpressionWithNoConstants() {
		String expressionString = "(a / b) >= c";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 3);

		Assert.assertEquals("a", variableMap.get("a"));
		Assert.assertEquals("b", variableMap.get("b"));
		Assert.assertEquals("c", variableMap.get("c"));
	}

	@Test
	public void testExpressionWithNoVariablesAndConstants() {
		String expressionString = "+ > >= && !=";

		Map<String, String> variableMap = _tokenExtractor.extract(
			expressionString);

		Assert.assertEquals(variableMap.size(), 0);
	}

	@Test
	public void testExpressionWithVariablesAndConstants() {
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

	@Test
	public void testWithEmptyExpression() {
		Map<String, String> variableMap = _tokenExtractor.extract(
			StringPool.BLANK);
		Assert.assertNull(_tokenExtractor.getExpression());
		Assert.assertEquals(variableMap.size(), 0);
	}

	@Test
	public void testWithNullExpression() {
		Map<String, String> variableMap = _tokenExtractor.extract(null);
		Assert.assertNull(_tokenExtractor.getExpression());
		Assert.assertEquals(variableMap.size(), 0);
	}

	@Test
	public void testWithoutCallingExtractMethod() {
		Assert.assertNull(_tokenExtractor.getExpression());
		Assert.assertNull(_tokenExtractor.getVariableMap());
	}

	private TokenExtractor _tokenExtractor;

}