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
public class VisibilityFunction extends BaseFunction {

	@Override
	public String execute(
			DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext,
			List<String> parameters)
		throws PortalException {

		if (parameters.size() < 3) {
			throw new DDMFormEvaluationException("Invalid function call");
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				ddmFormRuleEvaluatorContext.getDDMFormFieldEvaluationResults();

		String ddmFormFieldName = parameters.get(2);

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResultMap.get(ddmFormFieldName);

		if (ddmFormFieldEvaluationResult.isVisible()) {
			return "TRUE";
		}

		return "FALSE";
	}

	@Override
	public String getPattern() {
		return _PATTERN;
	}

	private static final String _PATTERN = "((isVisible)\\((\\w+)\\))";

}