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

package com.liferay.dynamic.data.mapping.expression;

import java.math.MathContext;

import java.util.Map;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public interface DDMExpression<T> {

	public T evaluate() throws DDMExpressionException;

	public Map<String, VariableDependencies> getVariableDependenciesMap()
		throws DDMExpressionException;

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setVariableValue(String,
	 *             Object)}
	 */
	@Deprecated
	public void setBooleanVariableValue(
		String variableName, Boolean variableValue);

	public void setDDMExpressionFunction(
		String functionName, DDMExpressionFunction ddmExpressionFunction);

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setVariableValue(String,
	 *             Object)}
	 */
	@Deprecated
	public void setDoubleVariableValue(
		String variableName, Double variableValue);

	public void setExpressionStringVariableValue(
		String variableName, String variableValue);

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setVariableValue(String,
	 *             Object)}
	 */
	@Deprecated
	public void setFloatVariableValue(String variableName, Float variableValue);

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setVariableValue(String,
	 *             Object)}
	 */
	@Deprecated
	public void setIntegerVariableValue(
		String variableName, Integer variableValue);

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setVariableValue(String,
	 *             Object)}
	 */
	@Deprecated
	public void setLongVariableValue(String variableName, Long variableValue);

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public void setMathContext(MathContext mathContext);

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setVariableValue(String,
	 *             Object)}
	 */
	@Deprecated
	public void setStringVariableValue(
			String variableName, String variableValue)
		throws DDMExpressionException;

	public void setVariableValue(String variableName, Object variableValue);

}