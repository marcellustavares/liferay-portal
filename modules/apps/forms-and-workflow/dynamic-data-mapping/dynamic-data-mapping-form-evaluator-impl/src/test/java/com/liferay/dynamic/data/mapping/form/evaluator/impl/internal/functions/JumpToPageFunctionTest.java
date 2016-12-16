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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Inácio Nery
 */
public class JumpToPageFunctionTest extends BaseDDMFormRuleFunctionTest {

	@Test
	public void testEvaluate() {
		Map<String, String> pageFlow = new HashMap<>();

		JumpToPageFunction jumpToPageFunction = new JumpToPageFunction(
			pageFlow);

		Object result = jumpToPageFunction.evaluate("1", "5");

		Assert.assertEquals(true, result);

		Assert.assertEquals("5", pageFlow.get("1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() throws Exception {
		JumpToPageFunction jumpToPageFunction = new JumpToPageFunction(null);

		jumpToPageFunction.evaluate();
	}

}