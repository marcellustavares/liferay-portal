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
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.math.MathContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

	protected Set<String> getVariableNames() {
		ParseTreeWalker parseTreeWalker = new ParseTreeWalker();

		DDMExpressionListenerImpl ddmExpressionListener =
			new DDMExpressionListenerImpl();

		parseTreeWalker.walk(ddmExpressionListener, _expressionContext);

		return ddmExpressionListener.getVariableNames();
	}

	protected Set<String> getFunctionNames() {
		ParseTreeWalker parseTreeWalker = new ParseTreeWalker();

		DDMExpressionListenerImpl ddmExpressionListener =
			new DDMExpressionListenerImpl();

		parseTreeWalker.walk(ddmExpressionListener, _expressionContext);

		return ddmExpressionListener.getFunctionNames();
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

	@Override
	public T evaluate() throws DDMExpressionException {
		Set<String> bundledFunctions = new HashSet<>(Arrays.asList("between", "concat", "contains", "equals", "isEmailAddress", "isURL", "sum"));

		for (String expressionFunction : getFunctionNames()) {
			if (bundledFunctions.contains(expressionFunction) || _ddmExpressionFunctions.keySet().contains(expressionFunction)) {
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

	@Override
	public void setFloatVariableValue(
		String variableName, Float variableValue) {

		setVariableValue(variableName, variableValue);
	}

	@Override
	public void setIntegerVariableValue(
		String variableName, Integer variableValue) {

		setVariableValue(variableName, variableValue);
	}

	@Override
	public void setLongVariableValue(String variableName, Long variableValue) {
		setVariableValue(variableName, variableValue);
	}

	@Deprecated
	@Override
	public void setMathContext(MathContext mathContext) {
	}

	@Override
	public void setStringVariableValue(
			String variableName, String variableValue)
		throws DDMExpressionException {

//		Double doubleValue = parseDoubleValue(variableValue);
//
//		if (doubleValue == null) {
//			setVariableValue(variableName, encode(variableValue));
//
//			return;
//		}
//
//		if (doubleValue.isNaN() || doubleValue.isInfinite()) {
//			throw new DDMExpressionException.NumberExceedsSupportedRange();
//		}
//		else {
//		}

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

//	protected String decodeString(BigDecimal bigDecimal) {
//		if (bigDecimal.equals(BigDecimal.ZERO)) {
//			return StringPool.BLANK;
//		}
//
//		BigInteger bigInteger = new BigInteger(bigDecimal.toString());

//
//		return new String(bigInteger.toByteArray());
//	}
//
//	protected BigDecimal encode(Boolean variableValue) {
//		if (variableValue.equals(Boolean.TRUE)) {
//			return BigDecimal.ONE;
//		}
//
//		return BigDecimal.ZERO;
//	}
//
//	protected BigDecimal encode(String variableValue) {
//		if (Validator.isNull(variableValue)) {
//			return BigDecimal.ZERO;
//		}
//
//		BigInteger bigInteger = new BigInteger(variableValue.getBytes());
//
//		return new BigDecimal(bigInteger);
//	}


	protected DDMExpression<Object> getExpression(
			String expressionString)
		throws DDMExpressionException {

		DDMExpressionImpl<Object> ddmExpressionImpl =
			new DDMExpressionImpl<Object>(expressionString, Object.class);


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

//	protected boolean isStringBlank(BigDecimal... bigDecimals) {
//		for (BigDecimal bigDecimal : bigDecimals) {
//			if (!bigDecimal.equals(BigDecimal.ZERO)) {
//				return false;
//			}
//		}
//
//		return true;
//	}

//	protected Double parseDoubleValue(String value) {
//		try {
//			return Double.parseDouble(value);
//		}
//		catch (NumberFormatException nfe) {
//			return null;
//		}
//	}

	protected VariableDependencies populateVariableDependenciesMap(
			Variable variable, Map<String,VariableDependencies> variableDependenciesMap)
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

	protected Object toRetunType(Object result) {
		if (_expressionClass.isAssignableFrom(Boolean.class)) {
			return result;
		}

		if (_expressionClass.isAssignableFrom(String.class)) {
			return String.valueOf(result);
		}

		Number number = (Number)result;

		if (_expressionClass.isAssignableFrom(Double.class)) {
			return number.doubleValue();
		}
		else if (_expressionClass.isAssignableFrom(Float.class)) {
			return number.floatValue();
		}
		else if (_expressionClass.isAssignableFrom(Integer.class)) {
			return number.intValue();
		}
		else {
			return number.longValue();
		}
	}

	private final Map<String, DDMExpressionFunction> _ddmExpressionFunctions =
		new HashMap<>();
	private final Class<?> _expressionClass;
	private final String _expressionString;
	private final Map<String, Variable> _variables = new TreeMap<>();
	private final Map<String, Object> _variableValues = new HashMap<>();
	private DDMExpressionParser.ExpressionContext _expressionContext;

}