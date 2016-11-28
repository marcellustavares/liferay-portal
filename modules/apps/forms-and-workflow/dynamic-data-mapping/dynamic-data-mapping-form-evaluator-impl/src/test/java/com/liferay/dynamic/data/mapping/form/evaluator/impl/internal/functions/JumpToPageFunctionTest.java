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

import java.util.Observable;
import java.util.Observer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Inácio Nery
 */
public class JumpToPageFunctionTest extends BaseDDMFormRuleFunctionTest {

	@Test
	public void testEvaluate() {
		JumpToPageFunction jumpToPageFunction = new JumpToPageFunction(
			_OBSERVER);

		Object result = jumpToPageFunction.evaluate("1");

		Assert.assertEquals(true, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() throws Exception {
		JumpToPageFunction jumpToPageFunction = new JumpToPageFunction(
			_OBSERVER);

		jumpToPageFunction.evaluate();
	}

	@Test(expected = NullPointerException.class)
	public void testNullPointer() throws Exception {
		new JumpToPageFunction(null);
	}

	private static final Observer _OBSERVER = new Observer() {

		@Override
		public void update(Observable observable, Object value) {
			Assert.assertTrue(observable instanceof JumpToPageFunction);
			Assert.assertEquals("1", String.valueOf(value));
		}

	};

}