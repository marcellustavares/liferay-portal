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
public class DDMFormFieldDataProviderRule extends DDMFormFieldBaseRule {

	public DDMFormFieldDataProviderRule(
		String ddmFormFieldName, String expression,
		DDMFormFieldRuleHelper ddmFormFieldRuleHelper) {

		super(
			ddmFormFieldName + StringPool.UNDERLINE + "dataprovider",
			ddmFormFieldName, expression, DDMFormFieldRuleType.DATA_PROVIDER,
			ddmFormFieldRuleHelper);
	}

	@Override
	public void execute() {
		String expression = getExpression();

		DDMFormFieldRuleHelper ddmFormFieldRuleHelper =
			getDDMFormFieldRuleHelper();

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				ddmFormFieldRuleHelper.getDDMFormFieldRuleEvaluationResultMap();

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(getDDMFormFieldName());

		try {
			ddmFormFieldRuleHelper.executeFunctions(expression);
		}
		catch (Exception e) {
			ddmFormFieldRuleEvaluationResult.setValid(false);
		}
	}

}