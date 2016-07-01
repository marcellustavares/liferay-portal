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

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionBaseVisitor;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionParser;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * @author Marcellus Tavares
 */
public class DDMExpressionVisitorImpl extends DDMExpressionBaseVisitor<Object> {

	public DDMExpressionVisitorImpl() {
		_functions.put(
			"between",
			new DDMExpressionFunction() {

				public Object evaluate(Object... parameters) {
					Number parameter = (Number)parameters[0];

					Number minParameter = (Number)parameters[1];
					Number maxParameter = (Number)parameters[2];

					if ((parameter.doubleValue() >=
							minParameter.doubleValue()) &&
						(parameter.doubleValue() <=
							maxParameter.doubleValue())) {

						return Boolean.TRUE;
					}

					return Boolean.FALSE;
				}

			});

		_functions.put(
			"concat",
			new DDMExpressionFunction() {

				public Object evaluate(Object... parameters) {
					StringBundler sb = new StringBundler(parameters.length);

					for (Object parameter : parameters) {
						String string = (String)parameter;

						if (Validator.isNull(string)) {
							continue;
						}

						sb.append(string);
					}

					return sb.toString();
				}

			});

		_functions.put(
			"contains",
			new DDMExpressionFunction() {

				public Object evaluate(Object... parameters) {
					String parameter1 = (String)parameters[0];
					String parameter2 = (String)parameters[1];

					if ((parameter1 == null) || (parameter2 == null)) {
						return false;
					}

					String string1 = StringUtil.toLowerCase(parameter1);
					String string2 = StringUtil.toLowerCase(parameter2);

					if (string1.contains(string2)) {
						return Boolean.TRUE;
					}

					return Boolean.FALSE;
				}

			});

		_functions.put(
			"equals",
			new DDMExpressionFunction() {

				public Object evaluate(Object... parameters) {
					String string1 = (String)parameters[0];
					String string2 = (String)parameters[1];

					if ((string1 == null) || (string2 == null)) {
						return false;
					}

					if (string1.contains(string2)) {
						return Boolean.TRUE;
					}

					return Boolean.FALSE;
				}

			});

		_functions.put(
			"isEmailAddress",
			new DDMExpressionFunction() {

				public Object evaluate(Object... parameters) {
					String string = (String)parameters[0];

					if (Validator.isEmailAddress(string)) {
						return Boolean.TRUE;
					}

					return Boolean.FALSE;
				}

			});

		_functions.put(
			"isURL",
			new DDMExpressionFunction() {

				public Object evaluate(Object... parameters) {
					String string = (String)parameters[0];

					if (Validator.isUrl(string)) {
						return Boolean.TRUE;
					}

					return Boolean.FALSE;
				}

		});

		_functions.put(
			"sum",
			new DDMExpressionFunction() {

				public Object evaluate(Object... parameters) {
					double result = 0;

					for (Object parameter : parameters) {
						Number parameterDouble = (Number)parameter;

						result += parameterDouble.doubleValue();
					}

					return result;
				}

			});
	}

	public void addFunction(String name, DDMExpressionFunction function) {
		_functions.put(name, function);
	}

	public void addVariable(String name, Object value) {
		_variables.put(name, value);
	}

	public Object visitAndExpression(
		@NotNull DDMExpressionParser.AndExpressionContext context) {

		boolean b1 = (Boolean)context.getChild(0).accept(this);
		boolean b2 = (Boolean)context.getChild(2).accept(this);

		return b1 && b2;
	}

	public Object visitBooleanParenthesis(
		@NotNull DDMExpressionParser.BooleanParenthesisContext context) {

		return context.getChild(1).accept(this);
	}

	public Object visitNotExpression(
		@NotNull DDMExpressionParser.NotExpressionContext context) {

		boolean b = (Boolean)context.getChild(1).accept(this);

		return !b;
	}

	public Object visitMinusExpression(
		@NotNull DDMExpressionParser.MinusExpressionContext context) {

		Number n = (Number)context.getChild(1).accept(this);

		return -n.doubleValue();
	}

	public Object visitDivisionExpression(
		@NotNull DDMExpressionParser.DivisionExpressionContext context) {

		Number l = (Number)context.getChild(0).accept(this);
		Number r = (Number)context.getChild(2).accept(this);

		return l.doubleValue() / r.doubleValue();
	}

	public Object visitFloatingPointLiteral(
		@NotNull DDMExpressionParser.FloatingPointLiteralContext context) {

		return Double.parseDouble(context.getText());
	}

	public Object visitEqualsExpression(
		@NotNull DDMExpressionParser.EqualsExpressionContext context) {

		boolean b1 = (Boolean)context.getChild(0).accept(this);
		boolean b2 = (Boolean)context.getChild(2).accept(this);

		return b1 == b2;
	}


	@Override
	public Object visitExpression(
		@NotNull DDMExpressionParser.ExpressionContext context) {

		return context.logicalOrExpression().accept(this);
	}

	public Object visitFunctionCallExpression(
		@NotNull DDMExpressionParser.FunctionCallExpressionContext context) {

		Token functionName = context.functionName;

		DDMExpressionFunction function = _functions.get(functionName.getText());

		if (function == null) {
			throw new IllegalStateException(
				String.format(
					"function %s not defined", functionName.getText()));
		}

		DDMExpressionParser.FunctionParametersContext functionParamsContext =
			context.functionParameters();

		Object[] params = {};

		if (functionParamsContext != null) {
			int paramsCount = functionParamsContext.getChildCount();

			LinkedList list = new LinkedList();

			for (int i = 0; i < paramsCount; i += 2) {
				list.add(functionParamsContext.getChild(i).accept(this));
			}

			params = list.toArray(new Object[list.size()]);
		}

		return function.evaluate(params);

	}

	public Object visitGreaterThanExpression(
		@NotNull DDMExpressionParser.GreaterThanExpressionContext context) {

		Number l = (Number)context.getChild(0).accept(this);
		Number r = (Number)context.getChild(2).accept(this);

		return l.doubleValue() > r.doubleValue();
	}

	public Object visitGreaterThanOrEqualsExpression(
		@NotNull DDMExpressionParser.GreaterThanOrEqualsExpressionContext
			context) {

		Number l = (Number)context.getChild(0).accept(this);
		Number r = (Number)context.getChild(2).accept(this);

		return l.doubleValue() >= r.doubleValue();
	}

	public Object visitLessThanExpression(
		@NotNull DDMExpressionParser.LessThanExpressionContext context) {

		Number l = (Number)context.getChild(0).accept(this);
		Number r = (Number)context.getChild(2).accept(this);

		return l.doubleValue() < r.doubleValue();
	}

	public Object visitLessThanOrEqualsExpression(
		@NotNull DDMExpressionParser.LessThanOrEqualsExpressionContext
			context) {

		Number l = (Number)context.getChild(0).accept(this);
		Number r = (Number)context.getChild(2).accept(this);

		return l.doubleValue() <= r.doubleValue();
	}

	public Object visitIntegerLiteral(
		@NotNull DDMExpressionParser.IntegerLiteralContext context) {

		return Double.parseDouble(context.getText());
	}

	public Object visitLogicalConstant(
		@NotNull DDMExpressionParser.LogicalConstantContext context) {

		return Boolean.parseBoolean(context.getText().toLowerCase());
	}

	@Override
	public Object visitLogicalVariable(
		@NotNull DDMExpressionParser.LogicalVariableContext ctx) {

		String variable = ctx.getText();

		Object variableValue = _variables.get(variable);

		if (variableValue == null) {
			throw new IllegalStateException(
				String.format("variable %s not defined", variable));
		}

		return variableValue;
	}

	public Object visitSubtractionExpression(
		@NotNull DDMExpressionParser.SubtractionExpressionContext context) {

		Number l = (Number)context.getChild(0).accept(this);
		Number r = (Number)context.getChild(2).accept(this);

		return l.doubleValue() - r.doubleValue();
	}

	public Object visitMultiplicationExpression(
		@NotNull DDMExpressionParser.MultiplicationExpressionContext context) {

		Number l = (Number)context.getChild(0).accept(this);
		Number r = (Number)context.getChild(2).accept(this);

		return l.doubleValue() * r.doubleValue();
	}


	public Object visitNotEqualsExpression(
		@NotNull DDMExpressionParser.NotEqualsExpressionContext context) {

		boolean b1 = (Boolean)context.getChild(0).accept(this);
		boolean b2 = (Boolean)context.getChild(2).accept(this);

		return b1 != b2;
	}

	@Override
	public Object visitNumericVariable(
		@NotNull DDMExpressionParser.NumericVariableContext context) {

		String variable = context.getText();

		Object variableValue = _variables.get(variable);

		if (variableValue == null) {
			throw new IllegalStateException(
				String.format("variable %s not defined", variable));
		}

		return variableValue;
	}

	public Object visitOrExpression(
		@NotNull DDMExpressionParser.OrExpressionContext context) {

		boolean b1 = (Boolean)context.getChild(0).accept(this);
		boolean b2 = (Boolean)context.getChild(2).accept(this);

		return b1 || b2;

	}

	public Object visitNumericParenthesis(
		@NotNull DDMExpressionParser.NumericParenthesisContext context) {

		return context.getChild(1).accept(this);
	}


	public Object visitAdditionExpression(
		@NotNull DDMExpressionParser.AdditionExpressionContext context) {

		Number l = (Number)context.getChild(0).accept(this);
		Number r = (Number)context.getChild(2).accept(this);

		return l.doubleValue() + r.doubleValue();
	}


	public Object visitStringLiteral(
		@NotNull DDMExpressionParser.StringLiteralContext context) {

		return context.getText().replaceAll("\"", "");
	}

	private final Map<String, DDMExpressionFunction> _functions =
		new HashMap<>();
	private final Map<String, Object> _variables = new HashMap<>();

}