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

	@Override
	public Object visitAnd(@NotNull DDMExpressionParser.AndContext ctx) {
		boolean b1 = (Boolean)ctx.getChild(0).accept(this);
		boolean b2 = (Boolean)ctx.getChild(2).accept(this);

		return b1 && b2;
	}

	@Override
	public Object visitBooleanParen(
		@NotNull DDMExpressionParser.BooleanParenContext ctx) {

		return ctx.getChild(1).accept(this);
	}

	@Override
	public Object visitChangeBoolean(
		@NotNull DDMExpressionParser.ChangeBooleanContext ctx) {

		boolean b = (Boolean)ctx.getChild(1).accept(this);

		return !b;
	}

	@Override
	public Object visitChangeSign(
		@NotNull DDMExpressionParser.ChangeSignContext ctx) {

		Number n = (Number)ctx.getChild(1).accept(this);

		if (isFloatingOperand(n)) {
			return -n.doubleValue();
		}

		return -n.longValue();
	}

	@Override
	public Object visitDivision(
		@NotNull DDMExpressionParser.DivisionContext ctx) {

		Number l = (Number)ctx.getChild(0).accept(this);
		Number r = (Number)ctx.getChild(2).accept(this);

		//System.out.println(String.format("sum %d %d", l, r));

		if (isFloatingOperand(l) || isFloatingOperand(r)) {
			return l.doubleValue() / r.doubleValue();
		}

		return l.longValue() / r.longValue();
	}

	@Override
	public Object visitDouble(@NotNull DDMExpressionParser.DoubleContext ctx) {
		System.out.println("visiting double ");
		return Double.parseDouble(ctx.getText());
	}

	@Override
	public Object visitEq(@NotNull DDMExpressionParser.EqContext ctx) {
		boolean b1 = (Boolean)ctx.getChild(0).accept(this);
		boolean b2 = (Boolean)ctx.getChild(2).accept(this);

		return b1 == b2;
	}

	@Override
	public Object visitExpression(
		@NotNull DDMExpressionParser.ExpressionContext ctx) {

		return ctx.logicalOrExpression().accept(this);
	}

	@Override
	public Object visitFunctionCall(
		@NotNull DDMExpressionParser.FunctionCallContext ctx) {

		Token functionName = ctx.functionName;

		DDMExpressionFunction function = _functions.get(functionName.getText());

		if (function == null) {
			throw new IllegalStateException(
				String.format(
					"function %s not defined", functionName.getText()));
		}

		DDMExpressionParser.FunctionParamsContext functionParamsContext =
			ctx.functionParams();

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

	@Override
	public Object visitGreater(
		@NotNull DDMExpressionParser.GreaterContext ctx) {

		Number l = (Number)ctx.getChild(0).accept(this);
		Number r = (Number)ctx.getChild(2).accept(this);

		if (isFloatingOperand(l) || isFloatingOperand(r)) {
			return l.doubleValue() > r.doubleValue();
		}

		return l.longValue() > r.longValue();
	}

	@Override
	public Object visitInteger(
		@NotNull DDMExpressionParser.IntegerContext ctx) {

		System.out.println("visiting long ");
		return Double.parseDouble(ctx.getText());
	}

	@Override
	public Object visitLogicalConst(
		@NotNull DDMExpressionParser.LogicalConstContext ctx) {

		System.out.println("visiting boolean ");
		return Boolean.parseBoolean(ctx.getText().toLowerCase());
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

	@Override
	public Object visitMinus(@NotNull DDMExpressionParser.MinusContext ctx) {
		Number l = (Number)ctx.getChild(0).accept(this);
		Number r = (Number)ctx.getChild(2).accept(this);

		System.out.println(String.format("sub %d %d", l, r));

		if (isFloatingOperand(l) || isFloatingOperand(r)) {
			return l.doubleValue() - r.doubleValue();
		}

		return l.longValue() - r.longValue();
	}

	@Override
	public Object visitMultiplication(
		@NotNull DDMExpressionParser.MultiplicationContext ctx) {

		Number l = (Number)ctx.getChild(0).accept(this);
		Number r = (Number)ctx.getChild(2).accept(this);

		//System.out.println(String.format("mult %d %d", l, r));

		if (isFloatingOperand(l) || isFloatingOperand(r)) {
			return l.doubleValue() * r.doubleValue();
		}

		return l.longValue() * r.longValue();
	}

	@Override
	public Object visitNEQ(@NotNull DDMExpressionParser.NEQContext ctx) {
		boolean b1 = (Boolean)ctx.getChild(0).accept(this);
		boolean b2 = (Boolean)ctx.getChild(2).accept(this);

		return b1 != b2;
	}

	@Override
	public Object visitNumericVariable(
		@NotNull DDMExpressionParser.NumericVariableContext ctx) {

		String variable = ctx.getText();

		Object variableValue = _variables.get(variable);

		if (variableValue == null) {
			throw new IllegalStateException(
				String.format("variable %s not defined", variable));
		}

		return variableValue;
	}

	@Override
	public Object visitOr(@NotNull DDMExpressionParser.OrContext ctx) {
		boolean b1 = (Boolean)ctx.getChild(0).accept(this);
		boolean b2 = (Boolean)ctx.getChild(2).accept(this);

		return b1 || b2;
	}

	@Override
	public Object visitParen(@NotNull DDMExpressionParser.ParenContext ctx) {
		return ctx.getChild(1).accept(this);
	}

	@Override
	public Object visitPlus(@NotNull DDMExpressionParser.PlusContext ctx) {
		Number l = (Number)ctx.getChild(0).accept(this);
		Number r = (Number)ctx.getChild(2).accept(this);

		//System.out.println(String.format("sum %d %d", l, r));

		if (isFloatingOperand(l) || isFloatingOperand(r)) {
			return l.doubleValue() + r.doubleValue();
		}

		return l.longValue() + r.longValue();
	}

	@Override
	public Object visitString(@NotNull DDMExpressionParser.StringContext ctx) {
		return ctx.getText().replaceAll("\"", "");
	}

	protected boolean isFloatingOperand(Number number) {
		if (number.getClass().isAssignableFrom(Double.class)) {
			return true;
		}

		return false;
	}

	private final Map<String, DDMExpressionFunction> _functions =
		new HashMap<>();
	private final Map<String, Object> _variables = new HashMap<>();

}