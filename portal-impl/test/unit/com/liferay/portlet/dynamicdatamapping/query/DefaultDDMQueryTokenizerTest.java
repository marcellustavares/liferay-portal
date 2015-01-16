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

package com.liferay.portlet.dynamicdatamapping.query;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Adolfo Pérez
 */
public class DefaultDDMQueryTokenizerTest {

	public static void assertTokenSequence(
			DDMQueryTokenizer tokenizer, DDMQueryToken... tokens)
		throws Exception {

		for (DDMQueryToken token : tokens) {
			Assert.assertEquals(token, tokenizer.nextToken());
		}
	}

	@Test
	public void testEmptyReturnsNullToken() throws Exception {
		Reader reader = new StringReader("");
		DDMQueryTokenizer tokenizer = new DefaultDDMQueryTokenizer(reader);

		Assert.assertNull(tokenizer.nextToken());
	}

	@Test
	public void testIgnoresWhitespace() throws Exception {
		Reader reader = new StringReader(
			"  / */employees /employee[ value  = 'foo' ] ");
		DDMQueryTokenizer tokenizer = new DefaultDDMQueryTokenizer(reader);

		assertTokenSequence(
			tokenizer, DDMQueryToken.CHILD_OF, DDMQueryToken.STAR,
			DDMQueryToken.CHILD_OF, DDMQueryToken.createIdentifier("employees"),
			DDMQueryToken.CHILD_OF, DDMQueryToken.createIdentifier("employee"),
			DDMQueryToken.OPEN_BRACKET, DDMQueryToken.createIdentifier("value"),
			DDMQueryToken.EQUAL, DDMQueryToken.createString("foo"),
			DDMQueryToken.CLOSE_BRACKET);

		Assert.assertNull(tokenizer.nextToken());
	}

	@Test
	public void testReadsTokensInOrder() throws Exception {
		Reader reader = new StringReader("*/],=['foo'bar");
		DDMQueryTokenizer tokenizer = new DefaultDDMQueryTokenizer(reader);

		assertTokenSequence(
			tokenizer, DDMQueryToken.STAR, DDMQueryToken.CHILD_OF,
			DDMQueryToken.CLOSE_BRACKET, DDMQueryToken.COMMA,
			DDMQueryToken.EQUAL, DDMQueryToken.OPEN_BRACKET,
			DDMQueryToken.createString("foo"),
			DDMQueryToken.createIdentifier("bar"));

		Assert.assertNull(tokenizer.nextToken());
	}

}