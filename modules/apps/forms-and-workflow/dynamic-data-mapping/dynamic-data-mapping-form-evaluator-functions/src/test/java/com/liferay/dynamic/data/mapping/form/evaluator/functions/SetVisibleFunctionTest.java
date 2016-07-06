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
public class SetVisibleFunctionTest {

	@Before
	public void setUp() {
		_defaultSetVisibleDDMFormFieldStateObserver =
			new DefaultSetVisibleDDMFormFieldStateObserver();
		_setVisibleFunction = new SetVisibleFunction();
		_setVisibleFunction.attach(_defaultSetVisibleDDMFormFieldStateObserver);
	}

	@Test
	public void testEvaluateFalse() throws Exception {
		_setVisibleFunction.evaluate("field1", false);

		Assert.assertEquals(
			false,
			_defaultSetVisibleDDMFormFieldStateObserver.isVisible("field1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		SetVisibleFunction setVisibleFunction = new SetVisibleFunction();

		setVisibleFunction.evaluate("test");
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		_setVisibleFunction.evaluate("field1", true);

		Assert.assertEquals(
			true,
			_defaultSetVisibleDDMFormFieldStateObserver.isVisible("field1"));
	}

	private DefaultSetVisibleDDMFormFieldStateObserver
		_defaultSetVisibleDDMFormFieldStateObserver;
	private SetVisibleFunction _setVisibleFunction;

	private class DefaultSetVisibleDDMFormFieldStateObserver
		extends DefaultDDMFormFieldStateObserver {

		@Override
		public boolean isVisible(String ddmFormFieldName) {
			if (ddmFormFieldName.equals(_ddmFormFieldName)) {
				return _value;
			}

			throw new IllegalArgumentException();
		}

		@Override
		public void setVisible(String ddmFormFieldName, boolean value) {
			_ddmFormFieldName = ddmFormFieldName;
			_value = value;
		}

		private String _ddmFormFieldName;
		private boolean _value;

	}

}