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
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;

import java.util.Locale;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class RuleFactory {

	public static Rule createDDMFormFieldRule(
		String expression, DDMExpressionFactory ddmExpressionFactory,
		DDMDataProviderInstanceService ddmDataProviderInstanceService,
		DDMDataProviderTracker ddmDataProviderTracker,
		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults, String ddmFormFieldName,
		DDMFormFieldRuleType ddmFormFieldRuleType,
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer,
		String instanceId, Locale locale) {

		if (ddmFormFieldRuleType == DDMFormFieldRuleType.DATA_PROVIDER) {
			return new DataProviderRule(
				expression, ddmExpressionFactory,
				ddmDataProviderInstanceService, ddmDataProviderTracker,
				ddmFormFieldEvaluationResults, ddmFormFieldName,
				ddmFormValuesJSONDeserializer, instanceId);
		}
		else if(ddmFormFieldRuleType == DDMFormFieldRuleType.READ_ONLY) {
			return new ReadOnlyRule(
				expression, ddmExpressionFactory,
				ddmDataProviderInstanceService, ddmDataProviderTracker,
				ddmFormFieldEvaluationResults, ddmFormFieldName,
				ddmFormValuesJSONDeserializer, instanceId);
		}
		else if(ddmFormFieldRuleType == DDMFormFieldRuleType.VALIDATION) {
			return new ValidationRule(
				expression, ddmExpressionFactory,
				ddmDataProviderInstanceService, ddmDataProviderTracker,
				ddmFormFieldEvaluationResults, ddmFormFieldName,
				ddmFormValuesJSONDeserializer, instanceId);
		}
		else if(ddmFormFieldRuleType == DDMFormFieldRuleType.VALUE) {
			return new ValueRule(
				expression, ddmExpressionFactory,
				ddmDataProviderInstanceService, ddmDataProviderTracker,
				ddmFormFieldEvaluationResults, ddmFormFieldName,
				ddmFormValuesJSONDeserializer, instanceId, locale);
		}
		else if(ddmFormFieldRuleType == DDMFormFieldRuleType.VISIBILITY) {
			return new VisibilityRule(
				expression, ddmExpressionFactory,
				ddmDataProviderInstanceService, ddmDataProviderTracker,
				ddmFormFieldEvaluationResults, ddmFormFieldName,
				ddmFormValuesJSONDeserializer, instanceId);
		}

		return null;
	}

}