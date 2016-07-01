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

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.dynamic.data.mapping.expression.VariableDependencies;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionLexer;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionParser;
import com.liferay.portal.kernel.util.ListUtil;

import java.math.MathContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * @author Miguel Angelo Caldas Gallindo
 * @author Marcellus Tavares
 */
public class DDMExpressionImpl<T> implements DDMExpression<T> {

	public DDMExpressionImpl(String expressionString, Class<T> expressionClass)
		throws DDMExpressionException {

		if (expressionString == null || expressionString.isEmpty()) {
			throw new IllegalArgumentException();
		}

		_expressionString = expressionString;
		_expressionClass = expressionClass;

		parse();

		for (String variableName : getVariableNames()) {
			Variable variable = new Variable(variableName);

			_variables.put(variableName, variable);
		}
	}

	@Override
	public T evaluate() throws DDMExpressionException {
		Set<String> bundledFunctions = new HashSet<>(
			Arrays.asList(
				"between", "concat", "contains", "equals", "isEmailAddress",
				"isURL", "sum"));

		for (String expressionFunction : getFunctionNames()) {
			if (bundledFunctions.contains(expressionFunction) ||
				_ddmExpressionFunctions.keySet().contains(expressionFunction)) {

				continue;
			}

			throw new DDMExpressionException.FunctionNotDefined("");
		}

		try {
			DDMExpressionVisitorImpl ddmExpressionVisitor =
				new DDMExpressionVisitorImpl();

			for (Map.Entry<String, Variable> entry : _variables.entrySet()) {
				Object variableValue = getVariableValue(entry.getValue());

				ddmExpressionVisitor.addVariable(entry.getKey(), variableValue);
			}

			Object result = _expressionContext.accept(ddmExpressionVisitor);

			return (T)toRetunType(result);
		}
		catch (Exception e) {
			throw new DDMExpressionException(e);
		}
	}

	@Override
	public Map<String, VariableDependencies> getVariableDependenciesMap()
		throws DDMExpressionException {

		Map<String, VariableDependencies> variableDependenciesMap =
			new HashMap<>();

		List<Variable> variables = ListUtil.fromCollection(_variables.values());

		for (Variable variable : variables) {
			populateVariableDependenciesMap(variable, variableDependenciesMap);
		}

		return variableDependenciesMap;
	}

	@Deprecated
	@Override
	public void setBooleanVariableValue(
		String variableName, Boolean variableValue) {

		setVariableValue(variableName, variableValue);
	}

	@Override
	public void setDDMExpressionFunction(
		String functionName, DDMExpressionFunction ddmExpressionFunction) {

		_ddmExpressionFunctions.put(functionName, ddmExpressionFunction);
	}

	@Deprecated
	@Override
	public void setDoubleVariableValue(
		String variableName, Double variableValue) {

		setVariableValue(variableName, variableValue);
	}

	@Override
	public void setExpressionStringVariableValue(
		String variableName, String variableValue) {

		Variable variable = _variables.get(variableName);

		if (variable == null) {
			return;
		}

		variable.setExpressionString(variableValue);
	}

	@Deprecated
	@Override
	public void setFloatVariableValue(
		String variableName, Float variableValue) {

		setVariableValue(variableName, variableValue);
	}

	@Deprecated
	@Override
	public void setIntegerVariableValue(
		String variableName, Integer variableValue) {

		setVariableValue(variableName, variableValue);
	}

	@Deprecated
	@Override
	public void setLongVariableValue(String variableName, Long variableValue) {
		setVariableValue(variableName, variableValue);
	}

	@Deprecated
	@Override
	public void setMathContext(MathContext mathContext) {
	}

	@Deprecated
	@Override
	public void setStringVariableValue(
			String variableName, String variableValue)
		throws DDMExpressionException {

		setVariableValue(variableName, variableValue);
	}

	@Override
	public void setVariableValue(String variableName, Object variableValue) {
		Variable variable = _variables.get(variableName);

		if (variable == null) {
			return;
		}

		variable.setValue(variableValue);

		_variableValues.put(variableName, variableValue);
	}

	protected DDMExpression<Object> getExpression(String expressionString)
		throws DDMExpressionException {

		DDMExpressionImpl<Object> ddmExpressionImpl = new DDMExpressionImpl<>(
			expressionString, Object.class);

		for (String variableName : ddmExpressionImpl.getVariableNames()) {
			Variable variable = _variables.get(variableName);

			if (variable != null) {
				Object variableValue = getVariableValue(variable);

				ddmExpressionImpl.setVariableValue(variableName, variableValue);
			}
		}

		return ddmExpressionImpl;
	}

	protected DDMExpression<Object> getExpression(Variable variable)
		throws DDMExpressionException {

		if (variable.getExpressionString() == null) {
			return null;
		}

		DDMExpression<Object> ddmExpression = getExpression(
			variable.getExpressionString());

		return ddmExpression;
	}

	protected Set<String> getFunctionNames() {
		ParseTreeWalker parseTreeWalker = new ParseTreeWalker();

		DDMExpressionListenerImpl ddmExpressionListener =
			new DDMExpressionListenerImpl();

		parseTreeWalker.walk(ddmExpressionListener, _expressionContext);

		return ddmExpressionListener.getFunctionNames();
	}

	protected Set<String> getVariableNames() {
		ParseTreeWalker parseTreeWalker = new ParseTreeWalker();

		DDMExpressionListenerImpl ddmExpressionListener =
			new DDMExpressionListenerImpl();

		parseTreeWalker.walk(ddmExpressionListener, _expressionContext);

		return ddmExpressionListener.getVariableNames();
	}

	protected Object getVariableValue(Variable variable)
		throws DDMExpressionException {

		Object variableValue = _variableValues.get(variable.getName());

		if (variableValue != null) {
			return variableValue;
		}

		DDMExpression<Object> ddmExpression = getExpression(variable);

		if (ddmExpression == null) {
			return variable.getValue();
		}

		variableValue = ddmExpression.evaluate();

		_variableValues.put(variable.getName(), variableValue);

		return variableValue;
	}

	protected void parse() throws DDMExpressionException {
		try {
			CharStream charStream = new ANTLRInputStream(_expressionString);

			DDMExpressionLexer ddmExpressionLexer = new DDMExpressionLexer(
				charStream);

			DDMExpressionParser ddmExpressionParser = new DDMExpressionParser(
				new CommonTokenStream(ddmExpressionLexer));

			ddmExpressionParser.setErrorHandler(new BailErrorStrategy());

			_expressionContext = ddmExpressionParser.expression();
		}
		catch (Exception e) {
			throw new DDMExpressionException.InvalidSyntax(e);
		}
	}

	protected VariableDependencies populateVariableDependenciesMap(
			Variable variable,
			Map<String, VariableDependencies> variableDependenciesMap)
		throws DDMExpressionException {

		VariableDependencies variableDependencies = variableDependenciesMap.get(
			variable.getName());

		if (variableDependencies != null) {
			return variableDependencies;
		}

		variableDependencies = new VariableDependencies(variable.getName());

		if (variable.getExpressionString() != null) {
			DDMExpressionImpl<?> ddmExpressionImpl = new DDMExpressionImpl<>(
				variable.getExpressionString(), Object.class);

			for (String variableName : ddmExpressionImpl.getVariableNames()) {
				if (!_variables.containsKey(variableName)) {
					Variable newVariable = new Variable(variableName);

					_variables.put(variableName, newVariable);
				}

				VariableDependencies variableVariableDependencies =
					populateVariableDependenciesMap(
						_variables.get(variableName), variableDependenciesMap);

				variableVariableDependencies.addAffectedVariable(
					variableDependencies.getVariableName());
				variableDependencies.addRequiredVariable(
					variableVariableDependencies.getVariableName());
			}
		}

		variableDependenciesMap.put(variable.getName(), variableDependencies);

		return variableDependencies;
	}

	protected double toDouble(Object result) throws DDMExpressionException {
		Class<?> clazz = result.getClass();

		if (!Number.class.isAssignableFrom(clazz)) {
			throw new DDMExpressionException.IncompatipleReturnType();
		}

		return (Double)result;
	}

	protected float toFloat(Object result) throws DDMExpressionException {
		Class<?> clazz = result.getClass();

		if (!Number.class.isAssignableFrom(clazz)) {
			throw new DDMExpressionException.IncompatipleReturnType();
		}

		Number number = (Number)result;

		return number.floatValue();
	}

	protected int toInteger(Object result) throws DDMExpressionException {
		Class<?> clazz = result.getClass();

		if (!Number.class.isAssignableFrom(clazz)) {
			throw new DDMExpressionException.IncompatipleReturnType();
		}

		Number number = (Number)result;

		return number.intValue();
	}

	protected long toLong(Object result) throws DDMExpressionException {
		Class<?> clazz = result.getClass();

		if (!Number.class.isAssignableFrom(clazz)) {
			throw new DDMExpressionException.IncompatipleReturnType();
		}

		Number number = (Number)result;

		return number.longValue();
	}

	protected Object toRetunType(Object result) throws DDMExpressionException {
		if (String.class.isAssignableFrom(_expressionClass)) {
			return String.valueOf(result);
		}
		else if (Boolean.class.isAssignableFrom(_expressionClass)) {
			Class<?> clazz = result.getClass();

			if (!Boolean.class.isAssignableFrom(clazz)) {
				throw new DDMExpressionException.IncompatipleReturnType();
			}

			return result;
		}
		else if (Double.class.isAssignableFrom(_expressionClass)) {
			return toDouble(result);
		}
		else if (Float.class.isAssignableFrom(_expressionClass)) {
			return toFloat(result);
		}
		else if (Integer.class.isAssignableFrom(_expressionClass)) {
			return toInteger(result);
		}
		else if (Long.class.isAssignableFrom(_expressionClass)) {
			return toLong(result);
		}
		else if (Number.class.isAssignableFrom(_expressionClass)) {
			Class<?> clazz = result.getClass();

			if (!Number.class.isAssignableFrom(clazz)) {
				throw new DDMExpressionException.IncompatipleReturnType();
			}

			return result;
		}

		return result;

//		Class<?> clazz = result.getClass();
//
//		if (!_expressionClass.isAssignableFrom(clazz)) {
//			throw new DDMExpressionException.IncompatipleReturnType();
//		}
//
//
//
//		if (Number.class.isAssignableFrom(clazz)) {
//			Number number = (Number)result;
//
//			if (clazz.isAssignableFrom(Double.class)) {
//				return number.doubleValue();
//			}
//			else if (clazz.isAssignableFrom(Float.class)) {
//				return number.floatValue();
//			}
//			else if (clazz.isAssignableFrom(Integer.class)) {
//				return number.intValue();
//			}
//			else if (clazz.isAssignableFrom(Long.class)) {
//				return number.longValue();
//			}
//			else {
//				return number;
//			}
//		}
//
//		return result;
	}

	private final Map<String, DDMExpressionFunction> _ddmExpressionFunctions =
		new HashMap<>();
	private final Class<?> _expressionClass;
	private DDMExpressionParser.ExpressionContext _expressionContext;
	private final String _expressionString;
	private final Map<String, Variable> _variables = new TreeMap<>();
	private final Map<String, Object> _variableValues = new HashMap<>();

}