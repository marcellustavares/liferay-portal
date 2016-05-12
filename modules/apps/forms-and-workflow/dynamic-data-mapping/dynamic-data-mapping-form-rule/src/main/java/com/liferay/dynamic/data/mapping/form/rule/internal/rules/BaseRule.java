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

package com.liferay.dynamic.data.mapping.form.rule.internal.rules;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.VariableDependencies;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.internal.DDMFormRuleEvaluatorContext;
import com.liferay.dynamic.data.mapping.form.rule.internal.functions.Function;
import com.liferay.dynamic.data.mapping.form.rule.internal.functions.FunctionFactory;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Leonardo Barros
 */
public abstract class BaseRule implements Rule {

	public BaseRule(
		String ddmFormFieldName, String instanceId,
		DDMFormFieldRuleType ddmFormFieldRuleType, String expression,
		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext) {

		this.ddmFormFieldName = ddmFormFieldName;
		this.instanceId = instanceId;
		this.ddmFormFieldRuleType = ddmFormFieldRuleType;
		this.expression = expression;
		this.ddmFormRuleEvaluatorContext = ddmFormRuleEvaluatorContext;
	}

	@Override
	public String getDDMFormFieldName() {
		return ddmFormFieldName;
	}

	@Override
	public DDMFormFieldRuleType getDDMFormFieldRuleType() {
		return ddmFormFieldRuleType;
	}

	@Override
	public String getInstanceId() {
		return instanceId;
	}

	protected <T> DDMExpression<T> createDDMExpression(Class<T> clazz)
		throws Exception {

		DDMExpressionFactory ddmExpressionFactory =
			ddmFormRuleEvaluatorContext.getDDMExpressionFactory();

		if (clazz.equals(Boolean.class)) {
			return (DDMExpression<T>)ddmExpressionFactory.
				createBooleanDDMExpression(expression);
		}
		else if(clazz.equals(Double.class)) {
			return (DDMExpression<T>)ddmExpressionFactory.
				createDoubleDDMExpression(expression);
		}

		return (DDMExpression<T>)ddmExpressionFactory.
			createBooleanDDMExpression(expression);
	}

	protected <T> T executeExpression(Class<T> clazz) throws Exception {
		executeFunctions();

		DDMExpression<T> ddmExpression = createDDMExpression(clazz);

		Map<String, VariableDependencies> dependenciesMap =
			ddmExpression.getVariableDependenciesMap();

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				ddmFormRuleEvaluatorContext.
					getDDMFormFieldRuleEvaluationResults();

		for (String variableName : dependenciesMap.keySet()) {
			if (ddmFormFieldRuleEvaluationResultMap.containsKey(variableName)) {
				DDMFormFieldRuleEvaluationResult
					ddmFormFieldRuleEvaluationResult =
						ddmFormFieldRuleEvaluationResultMap.get(variableName);

				ddmExpression.setStringVariableValue(
					variableName,
					ddmFormFieldRuleEvaluationResult.getValue().toString());
			}
		}

		return ddmExpression.evaluate();
	}

	protected void executeFunctions() throws Exception {
		for (String patternStr : FunctionFactory.getFunctionPatterns()) {
			Pattern pattern = Pattern.compile(patternStr);

			Matcher matcher = pattern.matcher(expression);

			while (matcher.find()) {
				String innerExpression = matcher.group(1);
				String functionName = matcher.group(2);

				Function ddmFormRuleFunction = FunctionFactory.getFunction(
					functionName);

				String result = ddmFormRuleFunction.execute(
					ddmFormRuleEvaluatorContext, mountParameters(matcher));

				expression = expression.replace(innerExpression, result);
			}
		}
	}

	protected boolean isNumberExpression() {
		for (String operator : _ARITHMETIC_OPERATORS) {
			if (expression.contains(operator)) {
				return true;
			}
		}

		return false;
	}

	protected List<String> mountParameters(Matcher matcher) {
		List<String> parameters = new ArrayList<>(matcher.groupCount());

		for (int i = 1; i <= matcher.groupCount(); i++) {
			parameters.add(matcher.group(i));
		}

		return parameters;
	}

	protected final String ddmFormFieldName;
	protected final DDMFormFieldRuleType ddmFormFieldRuleType;
	protected final DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext;
	protected String expression;
	protected final String instanceId;

	private static final String[] _ARITHMETIC_OPERATORS = {"*", "+", "-", "/"};

}