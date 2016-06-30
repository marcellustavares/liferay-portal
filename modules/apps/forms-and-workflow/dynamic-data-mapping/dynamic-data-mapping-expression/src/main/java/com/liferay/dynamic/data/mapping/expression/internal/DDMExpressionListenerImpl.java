/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.expression.internal;

import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionBaseListener;
import com.liferay.dynamic.data.mapping.expression.internal.parser.DDMExpressionParser;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marcellus Tavares
 */
public class DDMExpressionListenerImpl extends DDMExpressionBaseListener {

	public void enterNumericVariable(
		@NotNull DDMExpressionParser.NumericVariableContext ctx) {

		_variableNames.add(ctx.getText());
	}

	public void enterLogicalVariable(
		@NotNull DDMExpressionParser.LogicalVariableContext ctx) {

		_variableNames.add(ctx.getText());
	}

	public void enterFunctionCall(
		@NotNull DDMExpressionParser.FunctionCallContext ctx) {

		_functionNames.add(ctx.functionName.getText());
	}

	public Set<String> getVariableNames() {
		return _variableNames;
	}

	public Set<String> getFunctionNames() {
		return _functionNames;
	}

	private Set<String> _variableNames = new HashSet<>();
	private Set<String> _functionNames = new HashSet<>();


}
