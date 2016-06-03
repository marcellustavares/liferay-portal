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
import com.liferay.dynamic.data.mapping.expression.VariableDependencies;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.internal.DDMFormRuleEvaluatorContext;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class ValueRule extends BaseRule {

	public ValueRule(
		String ddmFormFieldName, String instanceId, String expression,
		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext) {

		super(
			ddmFormFieldName, instanceId, DDMFormFieldRuleType.VALUE,
			expression, ddmFormRuleEvaluatorContext);
	}

	@Override
	public void execute() throws Exception {
		if(Validator.isNull(expression)) {
			return;
		}
		
		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				ddmFormRuleEvaluatorContext.
					getDDMFormFieldRuleEvaluationResults();

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(getDDMFormFieldName());


		Class<?> expressionClass = Boolean.class;

		if (isNumberExpression()) {
			expressionClass = Double.class;
		}

		DDMExpression<?> ddmExpression = createDDMExpression(expressionClass);

		String currentFieldName = null;

		try {
			Map<String, VariableDependencies> dependenciesMap =
				ddmExpression.getVariableDependenciesMap();

			for (String variableName : dependenciesMap.keySet()) {
				if (ddmFormFieldRuleEvaluationResultMap.containsKey(
						variableName)) {

					currentFieldName = variableName;

					DDMFormFieldRuleEvaluationResult
						dependentDDMFormFieldRuleEvaluationResult =
							ddmFormFieldRuleEvaluationResultMap.get(
								variableName);

					String dependentValue =
						dependentDDMFormFieldRuleEvaluationResult
							.getValue().toString();

					if (isNumberExpression()) {
						Double.parseDouble(dependentValue);
					}

					ddmExpression.setStringVariableValue(
						variableName, dependentValue);
				}
			}

			ddmFormFieldRuleEvaluationResult.setValue(
				ddmExpression.evaluate().toString());
		}
		catch (NumberFormatException nfe) {
			ddmFormFieldRuleEvaluationResult.setValue("");
			ddmFormFieldRuleEvaluationResult.setValid(false);
			String errorMessage = LanguageUtil.format(
				ddmFormRuleEvaluatorContext.getLocale(),
				"the-value-of-field-was-not-entered-x", currentFieldName,
				false);
			ddmFormFieldRuleEvaluationResult.setErrorMessage(errorMessage);
		}
		catch (Exception e) {
			ddmFormFieldRuleEvaluationResult.setValue("");
			ddmFormFieldRuleEvaluationResult.setValid(false);
		}
	}

}