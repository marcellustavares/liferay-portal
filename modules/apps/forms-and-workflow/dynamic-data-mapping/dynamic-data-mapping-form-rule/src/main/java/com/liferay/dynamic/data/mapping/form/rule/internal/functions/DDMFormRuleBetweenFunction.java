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

package com.liferay.dynamic.data.mapping.form.rule.internal.functions;

import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormRuleEvaluationException;
import com.liferay.dynamic.data.mapping.form.rule.internal.DDMFormRuleEvaluatorContext;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleBetweenFunction extends DDMFormRuleBaseFunction {

	@Override
	public String execute(
		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext,
		List<String> parameters) throws DDMFormRuleEvaluationException {

		if (parameters.size() < 5) {
			throw new DDMFormRuleEvaluationException("Invalid function call");
		}

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				ddmFormRuleEvaluatorContext.getDDMFormFieldRuleEvaluationMap();
		String ddmFormFieldName = parameters.get(2);
		String expression1 = parameters.get(3);
		String expression2 = parameters.get(4);

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(ddmFormFieldName);

		try {
			double actualValue = Double.parseDouble(
				ddmFormFieldRuleEvaluationResult.getValue().toString());

			double value1 = 0;
			double value2 = 0;

			Pattern variablePattern = Pattern.compile(VARIABLE_PATTERN);

			try {
				value1 = Double.parseDouble(expression1);
			}
			catch (NumberFormatException nfe) {
				Matcher variableMatcher = variablePattern.matcher(expression1);
				while (variableMatcher.find()) {
					String variable = variableMatcher.group(1);
					DDMFormFieldRuleEvaluationResult fieldEvaluationResult1 =
						ddmFormFieldRuleEvaluationResultMap.get(variable);

					if (fieldEvaluationResult1 == null) {
						throw new DDMFormRuleEvaluationException(
							"Invalid expression");
					}

					value1 = Double.parseDouble(
						fieldEvaluationResult1.getValue().toString());
				}
			}

			try {
				value2 = Double.parseDouble(expression2);
			}
			catch (NumberFormatException nfe) {
				Matcher variableMatcher = variablePattern.matcher(expression2);
				while (variableMatcher.find()) {
					String variable = variableMatcher.group(1);
					DDMFormFieldRuleEvaluationResult fieldEvaluationResult2 =
						ddmFormFieldRuleEvaluationResultMap.get(variable);

					if (fieldEvaluationResult2 == null) {
						throw new DDMFormRuleEvaluationException(
							"Invalid expression");
					}

					value2 = Double.parseDouble(
						fieldEvaluationResult2.getValue().toString());
				}
			}

			if (actualValue >= value1 && actualValue <= value2) {
				return "TRUE";
			}

			return "FALSE";
		}
		catch (Exception e) {
			return "FALSE";
		}
	}

	@Override
	public String getPattern() {
		return _PATTERN;
	}

	private static final String _PATTERN =
		"((between)\\((\\w+),(\\w+),(\\w+)\\))";

}