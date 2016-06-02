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

package com.liferay.dynamic.data.mapping.form.rule.internal.functions;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleFunctionFactory {

	public static DDMFormRuleFunction getFunction(String name) {
		if (name.equals(_BETWEEN)) {
			return _betweenFunction;
		}
		else if(name.equals(_CALL)) {
			return _callFunction;
		}
		else if(name.equals(_CONTAINS)) {
			return _containsFunction;
		}
		else if(name.equals(_EQUALS)) {
			return _equalsFunction;
		}
		else if(name.equals(_IS_READ_ONLY)) {
			return _readOnlyFunction;
		}
		else if(name.equals(_IS_VISIBLE)) {
			return _visibleFunction;
		}

		throw new IllegalArgumentException("Invalid function name");
	}

	public static String[] getFunctionPatterns() {
		if (_ALL_FUNCTIONS_PATTERNS == null) {
			_ALL_FUNCTIONS_PATTERNS = new String[] {
				_betweenFunction.getPattern(), _callFunction.getPattern(),
				_containsFunction.getPattern(), _equalsFunction.getPattern(),
				_readOnlyFunction.getPattern(), _visibleFunction.getPattern()
			};
		}

		return _ALL_FUNCTIONS_PATTERNS;
	}

	private static String[] _ALL_FUNCTIONS_PATTERNS;

	private static final String _BETWEEN = "between";

	private static final String _CALL = "call";

	private static final String _CONTAINS = "contains";

	private static final String _EQUALS = "equals";

	private static final String _IS_READ_ONLY = "isReadOnly";

	private static final String _IS_VISIBLE = "isVisible";

	private static final DDMFormRuleFunction _betweenFunction =
		new DDMFormRuleBetweenFunction();
	private static final DDMFormRuleFunction _callFunction =
		new DDMFormRuleCallFunction();
	private static final DDMFormRuleFunction _containsFunction =
		new DDMFormRuleContainsFunction();
	private static final DDMFormRuleFunction _equalsFunction =
		new DDMFormRuleEqualsFunction();
	private static final DDMFormRuleFunction _readOnlyFunction =
		new DDMFormRuleReadOnlyFunction();
	private static final DDMFormRuleFunction _visibleFunction =
		new DDMFormRuleVisibleFunction();

}