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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorContext;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class EqualsFunction extends BaseFunction {

	@Override
	public String execute(
			DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext,
			List<String> parameters)
		throws PortalException {

		if (parameters.size() < 4) {
			throw new DDMFormEvaluationException("Invalid function call");
		}

		String ddmFormFieldName = parameters.get(2);
		String value = parameters.get(3);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				ddmFormRuleEvaluatorContext.getDDMFormFieldEvaluationResults();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResultMap.get(ddmFormFieldName);

		String actualValue = ddmFormFieldEvaluationResult.getValue().toString();

		if (actualValue.equals(value)) {
			return "TRUE";
		}

		return "FALSE";
	}

	@Override
	public String getPattern() {
		return _PATTERN;
	}

	private static final String _PATTERN = "((equals)\\((\\w+),\"(\\w+)\"\\))";

}