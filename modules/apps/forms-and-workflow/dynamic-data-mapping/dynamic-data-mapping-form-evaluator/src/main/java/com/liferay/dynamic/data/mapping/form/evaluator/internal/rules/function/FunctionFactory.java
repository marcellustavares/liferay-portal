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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function;

/**
 * @author Leonardo Barros
 */
public class FunctionFactory {

	public static String[] getDataProviderFunctionPatterns() {
		return new String[] {
			_callFunction.getPattern(), _getOptionsFunction.getPattern()
		};
	}

	public static Function getFunction(String name) {
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
		else if(name.equals(_GET_OPTIONS)) {
			return _getOptionsFunction;
		}
		else if(name.equals(_IS_EMAIL_ADDRESS)) {
			return _isEmailAddressFunction;
		}
		else if(name.equals(_IS_NUMBER)) {
			return _isNumberFunction;
		}
		else if(name.equals(_IS_READ_ONLY)) {
			return _readOnlyFunction;
		}
		else if(name.equals(_IS_URL)) {
			return _isUrlFunction;
		}
		else if(name.equals(_IS_VISIBLE)) {
			return _visibilityFunction;
		}

		throw new IllegalArgumentException("Invalid function name");
	}

	public static String[] getFunctionPatterns() {
		if (_ALL_FUNCTIONS_PATTERNS == null) {
			_ALL_FUNCTIONS_PATTERNS = new String[] {
				_betweenFunction.getPattern(), _callFunction.getPattern(),
				_containsFunction.getPattern(), _equalsFunction.getPattern(),
				_getOptionsFunction.getPattern(),
				_isEmailAddressFunction.getPattern(),
				_isNumberFunction.getPattern(), _isUrlFunction.getPattern(),
				_readOnlyFunction.getPattern(), _visibilityFunction.getPattern()
			};
		}

		return _ALL_FUNCTIONS_PATTERNS;
	}

	public static String getReadOnlyFunctionPattern() {
		return _readOnlyFunction.getPattern();
	}

	public static String[] getValidationFunctionPatterns() {
		return new String[] {
			_isEmailAddressFunction.getPattern(),
			_isNumberFunction.getPattern(), _isUrlFunction.getPattern()
		};
	}

	public static String[] getValueFunctionPatterns() {
		return new String[] {
			_betweenFunction.getPattern(), _containsFunction.getPattern(),
			_equalsFunction.getPattern(), Function.VARIABLE_PATTERN
		};
	}

	public static String getVisibilityFunctionPattern() {
		return _visibilityFunction.getPattern();
	}

	private static String[] _ALL_FUNCTIONS_PATTERNS;

	private static final String _BETWEEN = "between";

	private static final String _CALL = "call";

	private static final String _CONTAINS = "contains";

	private static final String _EQUALS = "equals";

	private static final String _GET_OPTIONS = "getOptions";

	private static final String _IS_EMAIL_ADDRESS = "isEmailAddress";

	private static final String _IS_NUMBER = "isNumber";

	private static final String _IS_READ_ONLY = "isReadOnly";

	private static final String _IS_URL = "isURL";

	private static final String _IS_VISIBLE = "isVisible";

	private static final Function _betweenFunction = new BetweenFunction();
	private static final Function _callFunction = new CallFunction();
	private static final Function _containsFunction = new ContainsFunction();
	private static final Function _equalsFunction = new EqualsFunction();
	private static final Function _getOptionsFunction =
		new GetOptionsFunction();
	private static final Function _isEmailAddressFunction =
		new IsEmailAddress();
	private static final Function _isNumberFunction = new IsNumberFunction();
	private static final Function _isUrlFunction = new IsURLFunction();
	private static final Function _readOnlyFunction = new ReadOnlyFunction();
	private static final Function _visibilityFunction =
		new VisibilityFunction();

}