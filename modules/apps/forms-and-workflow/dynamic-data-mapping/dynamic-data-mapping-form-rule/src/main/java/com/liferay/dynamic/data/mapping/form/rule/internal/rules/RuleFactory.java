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

import com.liferay.dynamic.data.mapping.form.rule.internal.DDMFormRuleEvaluatorContext;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;

/**
 * @author Leonardo Barros
 */
public class RuleFactory {

	public static Rule createDDMFormFieldRule(
		String ddmFormFieldName, String instanceId, String expression,
		DDMFormFieldRuleType ddmFormFieldRuleType,
		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext) {

		if (ddmFormFieldRuleType == DDMFormFieldRuleType.DATA_PROVIDER) {
			return new DataProviderRule(
				ddmFormFieldName, instanceId, expression,
				ddmFormRuleEvaluatorContext);
		}
		else if(ddmFormFieldRuleType == DDMFormFieldRuleType.READ_ONLY) {
			return new ReadOnlyRule(
				ddmFormFieldName, instanceId, expression,
				ddmFormRuleEvaluatorContext);
		}
		else if(ddmFormFieldRuleType == DDMFormFieldRuleType.VALIDATION) {
			return new ValidationRule(
				ddmFormFieldName, instanceId, expression,
				ddmFormRuleEvaluatorContext);
		}
		else if(ddmFormFieldRuleType == DDMFormFieldRuleType.VALUE) {
			return new ValueRule(
				ddmFormFieldName, instanceId, expression,
				ddmFormRuleEvaluatorContext);
		}
		else if(ddmFormFieldRuleType == DDMFormFieldRuleType.VISIBILITY) {
			return new VisibilityRule(
				ddmFormFieldName, instanceId, expression,
				ddmFormRuleEvaluatorContext);
		}

		return null;
	}

}