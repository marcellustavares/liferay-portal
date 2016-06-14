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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.type;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.VariableDependencies;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function.Function;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function.FunctionFactory;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.portal.kernel.util.Validator;

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
		String expression, DDMExpressionFactory ddmExpressionFactory,
		DDMDataProviderInstanceService ddmDataProviderInstanceService,
		DDMDataProviderTracker ddmDataProviderTracker,
		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults, String ddmFormFieldName,
		DDMFormFieldRuleType ddmFormFieldRuleType,
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer,
		String instanceId) {

		this.expression = expression;
		this.ddmExpressionFactory = ddmExpressionFactory;
		this.ddmDataProviderInstanceService = ddmDataProviderInstanceService;
		this.ddmDataProviderTracker = ddmDataProviderTracker;
		this.ddmFormFieldEvaluationResults = ddmFormFieldEvaluationResults;
		this.ddmFormFieldName = ddmFormFieldName;
		this.ddmFormFieldRuleType = ddmFormFieldRuleType;
		this.ddmFormValuesJSONDeserializer = ddmFormValuesJSONDeserializer;
		this.instanceId = instanceId;
	}

	@Override
	public void evaluate() throws DDMFormEvaluationException {
		if (Validator.isNull(expression)) {
			return;
		}

		executeExpression(Boolean.class);
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

	protected <T> T executeExpression(Class<T> clazz)
		throws DDMFormEvaluationException {

		try {
			executeFunctions();

			DDMExpression<T> ddmExpression = createDDMExpression(clazz);

			Map<String, VariableDependencies> dependenciesMap =
				ddmExpression.getVariableDependenciesMap();

			for (String variableName : dependenciesMap.keySet()) {
				if (ddmFormFieldEvaluationResults.containsKey(variableName)) {
					DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
						ddmFormFieldEvaluationResults.get(variableName);

					ddmExpression.setStringVariableValue(
						variableName,
						ddmFormFieldEvaluationResult.getValue().toString());
				}
			}

			return ddmExpression.evaluate();
		}
		catch (Exception e) {
			throw new DDMFormEvaluationException(e);
		}
	}

	protected void executeFunctions() throws DDMFormEvaluationException {
		try {
			for (String patternStr : FunctionFactory.getFunctionPatterns()) {
				Pattern pattern = Pattern.compile(patternStr);

				Matcher matcher = pattern.matcher(expression);

				while (matcher.find()) {
					String innerExpression = matcher.group(1);
					String functionName = matcher.group(2);

					Function ddmFormRuleFunction = FunctionFactory.getFunction(
						functionName);

					String result = ddmFormRuleFunction.execute(
						ddmDataProviderInstanceService, ddmDataProviderTracker,
						ddmFormFieldEvaluationResults,
						ddmFormValuesJSONDeserializer,
						mountParameters(matcher));

					expression = expression.replace(innerExpression, result);
				}
			}
		}
		catch (Exception e) {
			throw new DDMFormEvaluationException(e);
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

	protected final DDMDataProviderInstanceService
		ddmDataProviderInstanceService;
	protected final DDMDataProviderTracker ddmDataProviderTracker;
	protected final DDMExpressionFactory ddmExpressionFactory;
	protected final Map<String, DDMFormFieldEvaluationResult>
		ddmFormFieldEvaluationResults;
	protected final String ddmFormFieldName;
	protected final DDMFormFieldRuleType ddmFormFieldRuleType;
	protected final DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer;
	protected String expression;
	protected final String instanceId;

	private static final String[] _ARITHMETIC_OPERATORS = {"*", "+", "-", "/"};

}