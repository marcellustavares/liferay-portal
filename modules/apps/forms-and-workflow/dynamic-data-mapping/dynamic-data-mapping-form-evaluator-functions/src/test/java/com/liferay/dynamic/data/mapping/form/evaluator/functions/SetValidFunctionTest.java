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
import org.junit.Before;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class SetValidFunctionTest {

	@Before
	public void setUp() {
		_defaultSetValidDDMFormFieldStateObserver =
			new DefaultSetValidDDMFormFieldStateObserver();
		_setValidFunction = new SetValidFunction();
		_setValidFunction.attach(_defaultSetValidDDMFormFieldStateObserver);
	}

	@Test
	public void testEvaluateFalse() throws Exception {
		_setValidFunction.evaluate("field1", false);

		Assert.assertEquals(
			false, _defaultSetValidDDMFormFieldStateObserver.isValid("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		SetValidFunction setValidFunction = new SetValidFunction();

		setValidFunction.evaluate("test");
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		_setValidFunction.evaluate("field1", true);

		Assert.assertEquals(
			true, _defaultSetValidDDMFormFieldStateObserver.isValid("field1"));
	}

	private DefaultSetValidDDMFormFieldStateObserver
		_defaultSetValidDDMFormFieldStateObserver;
	private SetValidFunction _setValidFunction;

	private class DefaultSetValidDDMFormFieldStateObserver
		extends DefaultDDMFormFieldStateObserver {

		@Override
		public boolean isValid(String ddmFormFieldName) {
			if (ddmFormFieldName.equals(_ddmFormFieldName)) {
				return _value;
			}

			throw new IllegalArgumentException();
		}

		@Override
		public void setValid(String ddmFormFieldName, boolean value) {
			_ddmFormFieldName = ddmFormFieldName;
			_value = value;
		}

		private String _ddmFormFieldName;
		private boolean _value;

	}

}