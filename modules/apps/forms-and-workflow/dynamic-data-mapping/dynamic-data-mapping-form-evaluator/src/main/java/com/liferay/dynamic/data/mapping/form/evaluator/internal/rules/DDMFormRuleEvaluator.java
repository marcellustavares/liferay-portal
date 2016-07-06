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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.rules;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunctionTracker;
import com.liferay.dynamic.data.mapping.expression.VariableDependencies;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldObserver;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldStateObserver;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluator {

	public DDMFormRuleEvaluator(
		DDMExpressionFactory ddmExpressionFactory,
		DDMExpressionFunctionTracker ddmExpressionFunctionTracker,
		Map<String, Map<String, DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults, String expression) {

		_ddmExpressionFactory = ddmExpressionFactory;
		_ddmExpressionFunctionTracker = ddmExpressionFunctionTracker;
		_ddmFormFieldEvaluationResults = ddmFormFieldEvaluationResults;
		_expression = expression;
		_ddmFormFieldStateObserver = new DDMFormFieldStateObserverImpl(
			ddmFormFieldEvaluationResults);
	}

	public boolean evaluate() throws DDMFormEvaluationException {
		try {
			DDMExpression<Boolean> ddmExpression =
				_ddmExpressionFactory.createBooleanDDMExpression(_expression);

			setVariableValues(ddmExpression);
			setFunctions(ddmExpression);

			return ddmExpression.evaluate();
		}
		catch (DDMExpressionException ddmee) {
			throw new DDMFormEvaluationException(ddmee);
		}
	}

	public void execute() throws DDMFormEvaluationException {
		try {
			DDMExpression<String> ddmExpression =
				_ddmExpressionFactory.createStringDDMExpression(_expression);

			setVariableValues(ddmExpression);
			setFunctions(ddmExpression);

			ddmExpression.evaluate();
		}
		catch (DDMExpressionException ddmee) {
			throw new DDMFormEvaluationException(ddmee);
		}
	}

	protected Object getDDMFormFieldValue(String ddmFormFieldName) {
		if (!_ddmFormFieldEvaluationResults.containsKey(ddmFormFieldName)) {
			return null;
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				_ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		Collection<DDMFormFieldEvaluationResult> values =
			ddmFormFieldEvaluationResultMap.values();

		Iterator<DDMFormFieldEvaluationResult> iterator = values.iterator();

		if (!iterator.hasNext()) {
			return null;
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			iterator.next();

		return ddmFormFieldEvaluationResult.getValue();
	}

	protected void setFunctions(DDMExpression<?> ddmExpression) {
		Map<String, DDMExpressionFunction> ddmExpressionFunctionMap =
			_ddmExpressionFunctionTracker.getFunctions();

		for (Map.Entry<String, DDMExpressionFunction> entry :
				ddmExpressionFunctionMap.entrySet()) {

			ddmExpression.setDDMExpressionFunction(
				entry.getKey(), entry.getValue());

			DDMExpressionFunction ddmExpressionFunction = entry.getValue();

			if (ddmExpressionFunction instanceof DDMFormFieldObserver) {
				((DDMFormFieldObserver)ddmExpressionFunction).attach(
					_ddmFormFieldStateObserver);
			}
		}
	}

	protected void setVariableValues(DDMExpression<?> ddmExpression)
		throws DDMExpressionException {

		Map<String, VariableDependencies> variableDependenciesMap =
			ddmExpression.getVariableDependenciesMap();

		for (String variableName : variableDependenciesMap.keySet()) {
			Object variableValue = getDDMFormFieldValue(variableName);

			if (variableValue != null) {
				ddmExpression.setStringVariableValue(
					variableName, variableValue.toString());
			}
		}
	}

	private final DDMExpressionFactory _ddmExpressionFactory;
	private final DDMExpressionFunctionTracker _ddmExpressionFunctionTracker;
	private final Map<String, Map<String, DDMFormFieldEvaluationResult>>
		_ddmFormFieldEvaluationResults;
	private final DDMFormFieldStateObserver _ddmFormFieldStateObserver;
	private final String _expression;

}