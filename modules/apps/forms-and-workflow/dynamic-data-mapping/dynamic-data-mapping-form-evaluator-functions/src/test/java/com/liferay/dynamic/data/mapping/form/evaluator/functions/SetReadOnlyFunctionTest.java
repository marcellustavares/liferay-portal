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
public class SetReadOnlyFunctionTest {

	@Before
	public void setUp() {
		_defaultSetReadOnlyDDMFormFieldStateObserver =
			new DefaultSetReadOnlyDDMFormFieldStateObserver();
		_setReadOnlyFunction = new SetReadOnlyFunction();
		_setReadOnlyFunction.attach(
			_defaultSetReadOnlyDDMFormFieldStateObserver);
	}

	@Test
	public void testEvaluateFalse() throws Exception {
		_setReadOnlyFunction.evaluate("field1", false);

		Assert.assertEquals(
			false,
			_defaultSetReadOnlyDDMFormFieldStateObserver.isReadOnly("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		_setReadOnlyFunction.evaluate("test");
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		_setReadOnlyFunction.evaluate("field1", true);

		Assert.assertEquals(
			true,
			_defaultSetReadOnlyDDMFormFieldStateObserver.isReadOnly("field1"));
	}

	private DefaultSetReadOnlyDDMFormFieldStateObserver
		_defaultSetReadOnlyDDMFormFieldStateObserver;
	private SetReadOnlyFunction _setReadOnlyFunction;

	private class DefaultSetReadOnlyDDMFormFieldStateObserver
		extends DefaultDDMFormFieldStateObserver {

		@Override
		public boolean isReadOnly(String ddmFormFieldName) {
			if (ddmFormFieldName.equals(_ddmFormFieldName)) {
				return _value;
			}

			throw new IllegalArgumentException();
		}

		@Override
		public void setReadOnly(String ddmFormFieldName, boolean value) {
			_ddmFormFieldName = ddmFormFieldName;
			_value = value;
		}

		private String _ddmFormFieldName;
		private boolean _value;

	}

}