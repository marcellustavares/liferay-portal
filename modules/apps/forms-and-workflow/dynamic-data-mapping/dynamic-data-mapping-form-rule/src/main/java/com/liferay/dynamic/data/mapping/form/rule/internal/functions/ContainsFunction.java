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

/**
 * @author Leonardo Barros
 */
public class ContainsFunction extends BaseFunction {

	@Override
	public String execute(
		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext,
		List<String> parameters) throws DDMFormRuleEvaluationException {

		if (parameters.size() < 4) {
			throw new DDMFormRuleEvaluationException("Invalid function call");
		}

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				ddmFormRuleEvaluatorContext.
					getDDMFormFieldRuleEvaluationResults();

		String ddmFormFieldName = parameters.get(2);
		String value = parameters.get(3);

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(ddmFormFieldName);

		String actualValue =
			ddmFormFieldRuleEvaluationResult.getValue().toString();

		if (actualValue.contains(value)) {
			return "TRUE";
		}

		return "FALSE";
	}

	@Override
	public String getPattern() {
		return _PATTERN;
	}

	private static final String _PATTERN =
		"((contains)\\((\\w+),\"(\\w+)\"\\))";

}