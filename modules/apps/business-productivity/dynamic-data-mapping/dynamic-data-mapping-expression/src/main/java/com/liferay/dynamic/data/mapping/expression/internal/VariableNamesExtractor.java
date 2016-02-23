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

package com.liferay.dynamic.data.mapping.expression.internal;

import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Marcellus Tavares
 * @author Leonardo Barros
 */
public class VariableNamesExtractor {

	public void extract(String expressionString) {
		if (expressionString == null) {
			_variables = Collections.emptyList();
			_constants = Collections.emptyList();
			return;
		}

		_variables = new ArrayList<>();
		_constants = new ArrayList<>();

		com.udojava.evalex.Expression expression =
			new com.udojava.evalex.Expression(expressionString);

		Iterator<String> tokenIterator = expression.getExpressionTokenizer();

		while (tokenIterator.hasNext()) {
			String token = tokenIterator.next();

			Matcher tokenMatcher = _operatorsPattern.matcher(token);

			if (!tokenMatcher.matches() && !isFunction(token)) {
				Matcher variableMatcher = _variablePattern.matcher(token);

				if (variableMatcher.matches()) {
					_variables.add(token);
				}
				else {
					_constants.add(token);
				}
			}
		}
	}

	public List<String> getConstants() {
		return _constants;
	}

	public List<String> getVariables() {
		return _variables;
	}

	protected boolean isFunction(String token) {
		for (String function : _FUNCTIONS) {
			if (StringUtil.equalsIgnoreCase(function, token)) {
				return true;
			}
		}

		return false;
	}

	private static final String[] _FUNCTIONS = new String[] {
		"not", "if", "random", "min", "max", "abs", "round", "floor", "ceiling",
		"log", "log10", "sqrt", "sin", "cos", "tan", "asin", "acos", "atan",
		"sinh", "cosh", "tanh", "rad", "deg", "between", "concat", "contains",
		"equals", "isEmailAddress", "isURL", "sum"
	};

	private static final Pattern _operatorsPattern = Pattern.compile(
		"[+-/\\*%\\^\\(\\)]|[<>=!]=?|&&|\\|\\|");
	private static final Pattern _variablePattern = Pattern.compile(
		"\\b([a-zA-Z]+[\\w_]*)(?!\\()\\b", Pattern.MULTILINE);

	private List<String> _constants;
	private List<String> _variables;

}