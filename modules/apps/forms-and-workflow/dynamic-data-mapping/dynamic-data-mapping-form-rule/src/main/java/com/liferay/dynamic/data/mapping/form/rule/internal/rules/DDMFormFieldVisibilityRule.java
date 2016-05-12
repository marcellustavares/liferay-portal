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

import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.internal.DDMFormFieldRuleHelper;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class DDMFormFieldVisibilityRule extends DDMFormFieldBaseRule {

	public DDMFormFieldVisibilityRule(
		String ddmFormFieldName, String expression,
		DDMFormFieldRuleHelper ddmFormFieldRuleHelper) {

		super(
			ddmFormFieldName + StringPool.UNDERLINE + "visibility",
			ddmFormFieldName, expression, DDMFormFieldRuleType.VISIBILITY,
			ddmFormFieldRuleHelper);
	}

	@Override
	public void execute() {
		boolean expressionResult = executeExpression();

		DDMFormFieldRuleHelper ddmFormFieldRuleHelper =
			getDDMFormFieldRuleHelper();

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				ddmFormFieldRuleHelper.getDDMFormFieldRuleEvaluationResultMap();

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(getDDMFormFieldName());

		ddmFormFieldRuleEvaluationResult.setVisible(expressionResult);
	}

	@Override
	public boolean isAffectedBy(String expression) {
		String dddFormFieldName = getDDMFormFieldName();
		return expression.contains(
			String.format("isVisible(%s)", dddFormFieldName));
	}

}