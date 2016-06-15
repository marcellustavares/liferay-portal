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

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class IsNumberFunction extends BaseFunction {

	@Override
	public String execute(
			DDMDataProviderInstanceService ddmDataProviderInstanceService,
			DDMDataProviderTracker ddmDataProviderTracker,
			Map<String, DDMFormFieldEvaluationResult>
				ddmFormFieldEvaluationResults,
			DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer,
			List<String> parameters)
		throws DDMFormEvaluationException {

		if (parameters.size() < 3) {
			throw new DDMFormEvaluationException("Invalid function call");
		}

		String ddmFormFieldName = parameters.get(2);

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		String actualValue = ddmFormFieldEvaluationResult.getValue().toString();

		if (Validator.isNumber(actualValue)) {
			return "TRUE";
		}

		return "FALSE";
	}

	@Override
	public String getPattern() {
		return _PATTERN;
	}

	private static final String _PATTERN = "((isNumber)\\((\\w+)\\))";

}