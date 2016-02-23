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

import com.liferay.dynamic.data.mapping.expression.DDMExpressionEvaluationException;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import com.udojava.evalex.Expression.ExpressionException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Marcellus Tavares
 * @author Leonardo Barros
 */
public class TokenExtractor {

	public Map<String, String> extract(String expressionString)
		throws DDMExpressionEvaluationException {

		if (Validator.isNull(expressionString)) {
			return Collections.emptyMap();
		}

		try {
			_expression = expressionString;

			_variableMap = new HashMap<>();

			Matcher matcher = _stringPattern.matcher(_expression);

			while (matcher.find()) {
				createVariable(matcher.group(1), true);
			}

			com.udojava.evalex.Expression expression =
				new com.udojava.evalex.Expression(_expression);

			Iterator<String> tokenIterator =
				expression.getExpressionTokenizer();

			while (tokenIterator.hasNext()) {
				String token = tokenIterator.next();

				Matcher tokenMatcher = _operatorsPattern.matcher(token);

				if (!tokenMatcher.matches() &&
					!ArrayUtil.contains(
						_FUNCTIONS, StringUtil.toLowerCase(token)) &&
					!ArrayUtil.contains(
						_BOOLEAN_CONSTANTS, StringUtil.toLowerCase(token))) {

					Matcher variableMatcher = _variablePattern.matcher(token);

					if (variableMatcher.matches()) {
						if (!_variableMap.containsKey(token)) {
							_variableMap.put(token, token);
						}
					}
					else {
						createVariable(token, false);
					}
				}
			}

			return _variableMap;
		}
		catch (ExpressionException ee) {
			throw new DDMExpressionEvaluationException(ee);
		}
	}

	public String getExpression() {
		return _expression;
	}

	public Map<String, String> getVariableMap() {
		return _variableMap;
	}

	protected void createVariable(String token, boolean stringConstant) {
		String variableName = StringUtil.randomId();

		_variableMap.put(variableName, token);

		if (stringConstant) {
			_expression = StringUtil.replace(
				_expression, "\"" + token + "\"", variableName);
		}
		else {
			_expression = StringUtil.replace(_expression, token, variableName);
		}
	}

	private static final String[] _BOOLEAN_CONSTANTS = {"false", "true"};

	private static final String[] _FUNCTIONS = new String[] {
		"not", "if", "random", "min", "max", "abs", "round", "floor", "ceiling",
		"log", "log10", "sqrt", "sin", "cos", "tan", "asin", "acos", "atan",
		"sinh", "cosh", "tanh", "rad", "deg", "between", "concat", "contains",
		"equals", "isemailaddress", "isurl", "sum"
	};

	private static final Pattern _operatorsPattern = Pattern.compile(
		"[+-/\\*%\\^\\(\\)]|[<>=!]=?|&&|\\|\\|");
	private static final Pattern _stringPattern = Pattern.compile(
		"\"([^\"]*)\"");
	private static final Pattern _variablePattern = Pattern.compile(
		"\\b([a-zA-Z]+[\\w_]*)(?!\\()\\b");

	private String _expression;
	private Map<String, String> _variableMap;

}