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

package com.liferay.dynamic.data.mapping.form.evaluator.functions;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class IsValidFunctionTest {

	@Test
	public void testEvaluateFalse() throws Exception {
		IsValidFunction isValidFunction = new IsValidFunction();

		isValidFunction.attach(
			new DefaultDDMFormFieldStateObserver() {

				@Override
				public boolean isValid(String ddmFormFieldName) {
					if (ddmFormFieldName.equals("field1")) {
						return false;
					}

					throw new IllegalArgumentException();
				}

			});

		Assert.assertFalse((Boolean)isValidFunction.evaluate("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		IsValidFunction isValidFunction = new IsValidFunction();

		isValidFunction.evaluate("test", "test2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid2() throws Exception {
		IsValidFunction isValidFunction = new IsValidFunction();

		isValidFunction.attach(
			new DefaultDDMFormFieldStateObserver() {

				@Override
				public boolean isValid(String ddmFormFieldName) {
					if (ddmFormFieldName.equals("field1")) {
						return false;
					}

					throw new IllegalArgumentException();
				}

			});

		isValidFunction.evaluate("invalid field name");
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		IsValidFunction isValidFunction = new IsValidFunction();

		isValidFunction.attach(
			new DefaultDDMFormFieldStateObserver() {

				@Override
				public boolean isValid(String ddmFormFieldName) {
					if (ddmFormFieldName.equals("field1")) {
						return true;
					}

					throw new IllegalArgumentException();
				}

			});

		Assert.assertTrue((Boolean)isValidFunction.evaluate("field1"));
	}

}