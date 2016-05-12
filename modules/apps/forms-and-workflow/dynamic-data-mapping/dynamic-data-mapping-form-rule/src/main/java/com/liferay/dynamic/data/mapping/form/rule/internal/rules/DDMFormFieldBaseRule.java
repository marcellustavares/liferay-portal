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
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.VariableDependencies;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.internal.DDMFormFieldRuleHelper;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.Locale;
import java.util.Map;

import org.easyrules.core.BasicRule;

/**
 * @author Leonardo Barros
 */
public abstract class DDMFormFieldBaseRule extends BasicRule {

	public DDMFormFieldBaseRule(
		String ruleName, String ddmFormFieldName, String expression,
		DDMFormFieldRuleType ddmFormFieldRuleType,
		DDMFormFieldRuleHelper ddmFormFieldRuleHelper) {

		super(ruleName);

		_ddmFormFieldName = ddmFormFieldName;
		_expression = expression;
		_ddmFormFieldRuleType = ddmFormFieldRuleType;
		_ddmFormFieldRuleHelper = ddmFormFieldRuleHelper;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!super.equals(obj)) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		DDMFormFieldBaseRule other = (DDMFormFieldBaseRule)obj;

		if (_ddmFormFieldName == null) {
			if (other._ddmFormFieldName != null) {
				return false;
			}
		}
		else if (!_ddmFormFieldName.equals(other._ddmFormFieldName)) {
			return false;
		}

		if (_ddmFormFieldRuleType != other._ddmFormFieldRuleType) {
			return false;
		}

		return true;
	}

	@Override
	public boolean evaluate() {
		return true;
	}

	public String getDDMFormFieldName() {
		return _ddmFormFieldName;
	}

	public DDMFormFieldRuleType getDDMFormFieldRuleType() {
		return _ddmFormFieldRuleType;
	}

	public String getExpression() {
		return _expression;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result +
			((_ddmFormFieldName == null) ? 0 : _ddmFormFieldName.hashCode());
		result = prime * result +
			((_ddmFormFieldRuleType == null) ?
				0 : _ddmFormFieldRuleType.hashCode());
		return result;
	}

	public boolean isAffectedBy(String expression) {
		return false;
	}

	protected boolean executeExpression() {
		try {
			_expression = _ddmFormFieldRuleHelper.executeFunctions(_expression);

			DDMExpressionFactory ddmExpressionFactory =
				_ddmFormFieldRuleHelper.getDDMExpressionFactory();

			DDMExpression<Boolean> ddmExpression =
				ddmExpressionFactory.createBooleanDDMExpression(_expression);

			Map<String, VariableDependencies> dependenciesMap =
				ddmExpression.getVariableDependenciesMap();

			Map<String, DDMFormFieldRuleEvaluationResult>
				ddmFormFieldRuleEvaluationResultMap =
					_ddmFormFieldRuleHelper.
						getDDMFormFieldRuleEvaluationResultMap();

			for (String variableName : dependenciesMap.keySet()) {
				if (ddmFormFieldRuleEvaluationResultMap.containsKey(
						variableName)) {

					DDMFormFieldRuleEvaluationResult
						ddmFormFieldRuleEvaluationResult =
							ddmFormFieldRuleEvaluationResultMap.get(
								variableName);

					ddmExpression.setStringVariableValue(
						variableName,
						ddmFormFieldRuleEvaluationResult.getValue().toString());
				}
			}

			return ddmExpression.evaluate();
		}
		catch (PortalException pe) {
		}

		return false;
	}

	protected DDMFormFieldRuleHelper getDDMFormFieldRuleHelper() {
		return _ddmFormFieldRuleHelper;
	}

	protected Locale getLocale() {
		return _ddmFormFieldRuleHelper.getLocale();
	}

	protected boolean isNumberExpression() {
		for (String operator : _ARITHMETIC_OPERATORS) {
			if (_expression.contains(operator)) {
				return true;
			}
		}

		return false;
	}

	private static final String[] _ARITHMETIC_OPERATORS = {"*", "+", "-", "/"};

	private final String _ddmFormFieldName;
	private final DDMFormFieldRuleHelper _ddmFormFieldRuleHelper;
	private final DDMFormFieldRuleType _ddmFormFieldRuleType;
	private String _expression;

}