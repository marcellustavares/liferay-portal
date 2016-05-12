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

package com.liferay.dynamic.data.mapping.form.rule.internal;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.VariableDependencies;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.internal.functions.DDMFormRuleFunction;
import com.liferay.dynamic.data.mapping.form.rule.internal.functions.DDMFormRuleFunctionFactory;
import com.liferay.dynamic.data.mapping.form.rule.internal.rules.DDMFormFieldBaseRule;
import com.liferay.dynamic.data.mapping.form.rule.internal.rules.DDMFormFieldDataProviderRule;
import com.liferay.dynamic.data.mapping.form.rule.internal.rules.DDMFormFieldReadOnlyRule;
import com.liferay.dynamic.data.mapping.form.rule.internal.rules.DDMFormFieldValidationRule;
import com.liferay.dynamic.data.mapping.form.rule.internal.rules.DDMFormFieldValueRule;
import com.liferay.dynamic.data.mapping.form.rule.internal.rules.DDMFormFieldVisibilityRule;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRule;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Leonardo Barros
 */
public class DDMFormFieldRuleHelper {

	public DDMFormFieldRuleHelper(
		DDMFormRuleEvaluatorContext ddmFormRuleEvaluatorContext) {

		_ddmFormRuleEvaluatorContext = ddmFormRuleEvaluatorContext;
	}

	public DDMFormFieldBaseRule createDDMFormFieldRule(
			DDMFormField ddmFormField, DDMFormFieldValue ddmFormFieldValue,
			DDMFormFieldRule ddmFormFieldRule)
		throws PortalException {

		DDMFormFieldRuleType ddmFormFieldRuleType = ddmFormFieldRule.getType();
		DDMFormFieldBaseRule ddmFormFieldBaseRule = null;

		if (ddmFormFieldRuleType.equals(DDMFormFieldRuleType.DATA_PROVIDER)) {
			ddmFormFieldBaseRule = createDDMFormFieldDataProviderRule(
				ddmFormField, ddmFormFieldValue,
				ddmFormFieldRule.getExpression());
		}
		else if (ddmFormFieldRuleType.equals(DDMFormFieldRuleType.VALUE)) {
			ddmFormFieldBaseRule = createDDMFormFieldValueRule(
				ddmFormField, ddmFormFieldValue,
				ddmFormFieldRule.getExpression());
		}
		else if(ddmFormFieldRuleType.equals(DDMFormFieldRuleType.VALIDATION)) {
			ddmFormFieldBaseRule = createDDMFormFieldValidationRule(
				ddmFormField, ddmFormFieldValue,
				ddmFormFieldRule.getExpression());
		}
		else if(ddmFormFieldRuleType.equals(DDMFormFieldRuleType.VISIBILITY)) {
			ddmFormFieldBaseRule = createDDMFormFieldVisibilityRule(
				ddmFormField, ddmFormFieldValue,
				ddmFormFieldRule.getExpression());
		}
		else if(ddmFormFieldRuleType.equals(DDMFormFieldRuleType.READ_ONLY)) {
			ddmFormFieldBaseRule = createDDMFormFieldReadOnlyRule(
				ddmFormField, ddmFormFieldValue,
				ddmFormFieldRule.getExpression());
		}

		return ddmFormFieldBaseRule;
	}

	public String executeFunctions(String expression) throws PortalException {
		for (String patternStr :
			DDMFormRuleFunctionFactory.getFunctionPatterns()) {

			Pattern pattern = Pattern.compile(patternStr);

			Matcher matcher = pattern.matcher(expression);

			while (matcher.find()) {
				String innerExpression = matcher.group(1);
				String functionName = matcher.group(2);

				DDMFormRuleFunction ddmFormRuleFunction =
					DDMFormRuleFunctionFactory.getFunction(functionName);

				String result = ddmFormRuleFunction.execute(
					_ddmFormRuleEvaluatorContext, mountParameters(matcher));

				expression = expression.replace(innerExpression, result);
			}
		}

		return expression;
	}

	public DDMExpressionFactory getDDMExpressionFactory() {
		return _ddmFormRuleEvaluatorContext.getDDMExpressionFactory();
	}

	public Map<String, DDMFormFieldRuleEvaluationResult>
		getDDMFormFieldRuleEvaluationResultMap() {

		return _ddmFormRuleEvaluatorContext.getDDMFormFieldRuleEvaluationMap();
	}

	public List<DDMFormField> getDDMFormFields() {
		return _ddmFormRuleEvaluatorContext.getDDMFormFields();
	}

	public DDMFormValues getDDMFormValues() {
		return _ddmFormRuleEvaluatorContext.getDDMFormValues();
	}

	public Locale getLocale() {
		return _ddmFormRuleEvaluatorContext.getLocale();
	}

	public List<DDMFormRuleEvaluatorTreeNode>
		getTreeNodesForDDMFormFieldDependencies(String expression)
		throws PortalException {

		List<DDMFormRuleEvaluatorTreeNode> treeNodes = new ArrayList<>();

		List<DDMFormFieldBaseRule> ddmFormFieldBaseRules =
			extractDependenciesRules(expression);

		for (DDMFormFieldBaseRule ddmFormFieldBaseRule :
				ddmFormFieldBaseRules) {

			treeNodes.add(
				new DDMFormRuleEvaluatorTreeNode(ddmFormFieldBaseRule));
		}

		return treeNodes;
	}

	public boolean hasDDMFormFieldDependencies(String expression)
		throws PortalException {

		if (Validator.isNull(expression)) {
			return false;
		}

		DDMExpression<Boolean> ddmExpression =
			getDDMExpressionFactory().createBooleanDDMExpression(expression);

		Map<String, VariableDependencies> dependenciesMap =
			ddmExpression.getVariableDependenciesMap();

		for (DDMFormField ddmFormField : getDDMFormFields()) {
			if (dependenciesMap.containsKey(ddmFormField.getName())) {
				return true;
			}
		}

		return false;
	}

	protected DDMFormFieldBaseRule createDDMFormFieldDataProviderRule(
		DDMFormField ddmFormField, DDMFormFieldValue ddmFormFieldValue,
		String expression) {

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				getDDMFormFieldRuleEvaluationResultMap();

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(ddmFormField.getName());

		if (ddmFormFieldRuleEvaluationResult == null) {
			ddmFormFieldRuleEvaluationResult =
				new DDMFormFieldRuleEvaluationResult(ddmFormField.getName());
			ddmFormFieldRuleEvaluationResultMap.put(
				ddmFormField.getName(), ddmFormFieldRuleEvaluationResult);
		}

		return new DDMFormFieldDataProviderRule(
			ddmFormField.getName(), expression, this);
	}

	protected DDMFormFieldBaseRule createDDMFormFieldReadOnlyRule(
		DDMFormField ddmFormField, DDMFormFieldValue ddmFormFieldValue,
		String expression) throws PortalException {

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				getDDMFormFieldRuleEvaluationResultMap();

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(ddmFormField.getName());

		if (ddmFormFieldRuleEvaluationResult == null) {
			ddmFormFieldRuleEvaluationResult =
				new DDMFormFieldRuleEvaluationResult(ddmFormField.getName());
			ddmFormFieldRuleEvaluationResultMap.put(
				ddmFormField.getName(), ddmFormFieldRuleEvaluationResult);
		}

		if (Validator.isNull(expression)) {
			expression = "FALSE";
			ddmFormFieldRuleEvaluationResult.setReadOnly(false);
		}

		return new DDMFormFieldReadOnlyRule(
			ddmFormField.getName(), expression, this);
	}

	protected DDMFormFieldBaseRule createDDMFormFieldReadOnlyRule(
		String expression, Matcher matcher) {

		String ddmFormFieldName = matcher.group(3);

		return new DDMFormFieldReadOnlyRule(
			ddmFormFieldName, matcher.group(1), this);
	}

	protected DDMFormFieldBaseRule createDDMFormFieldRule(Matcher matcher) {
		String ddmFormFieldName = matcher.group(3);

		return new DDMFormFieldValueRule(
			ddmFormFieldName, matcher.group(1), this);
	}

	protected DDMFormFieldBaseRule createDDMFormFieldValidationRule(
		DDMFormField ddmFormField, DDMFormFieldValue ddmFormFieldValue,
		String expression) throws PortalException {

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				getDDMFormFieldRuleEvaluationResultMap();

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(ddmFormField.getName());

		if (ddmFormFieldRuleEvaluationResult == null) {
			ddmFormFieldRuleEvaluationResult =
				new DDMFormFieldRuleEvaluationResult(ddmFormField.getName());
			ddmFormFieldRuleEvaluationResultMap.put(
				ddmFormField.getName(), ddmFormFieldRuleEvaluationResult);
		}

		if (Validator.isNull(expression)) {
			expression = "TRUE";
			ddmFormFieldRuleEvaluationResult.setValid(true);
		}

		return new DDMFormFieldValidationRule(
			ddmFormField.getName(), expression, this);
	}

	protected DDMFormFieldBaseRule createDDMFormFieldValueRule(
		DDMFormField ddmFormField, DDMFormFieldValue ddmFormFieldValue,
		String expression) {

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				getDDMFormFieldRuleEvaluationResultMap();

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(ddmFormField.getName());

		if (ddmFormFieldRuleEvaluationResult == null) {
			ddmFormFieldRuleEvaluationResult =
				new DDMFormFieldRuleEvaluationResult(ddmFormField.getName());
			ddmFormFieldRuleEvaluationResultMap.put(
				ddmFormField.getName(), ddmFormFieldRuleEvaluationResult);
		}

		String value = ddmFormFieldValue.getValue().getString(getLocale());
		ddmFormFieldRuleEvaluationResult.setValue(value);

		return new DDMFormFieldValueRule(
			ddmFormField.getName(), expression, this);
	}

	protected DDMFormFieldBaseRule createDDMFormFieldValueRule(
		String expression, Matcher matcher) {

		String ddmFormFieldName = matcher.group(3);

		return new DDMFormFieldValueRule(
			ddmFormFieldName, matcher.group(1), this);
	}

	protected DDMFormFieldBaseRule createDDMFormFieldVisibilityRule(
		DDMFormField ddmFormField, DDMFormFieldValue ddmFormFieldValue,
		String expression) throws PortalException {

		Map<String, DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResultMap =
				getDDMFormFieldRuleEvaluationResultMap();

		DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldRuleEvaluationResultMap.get(ddmFormField.getName());

		if (ddmFormFieldRuleEvaluationResult == null) {
			ddmFormFieldRuleEvaluationResult =
				new DDMFormFieldRuleEvaluationResult(ddmFormField.getName());
			ddmFormFieldRuleEvaluationResultMap.put(
				ddmFormField.getName(), ddmFormFieldRuleEvaluationResult);
		}

		if (Validator.isNull(expression)) {
			expression = "TRUE";
			ddmFormFieldRuleEvaluationResult.setVisible(true);
		}

		return new DDMFormFieldVisibilityRule(
			ddmFormField.getName(), expression, this);
	}

	protected DDMFormFieldBaseRule createDDMFormFieldVisibiltyRule(
		String expression, Matcher matcher) {

		String ddmFormFieldName = matcher.group(3);

		return new DDMFormFieldVisibilityRule(
			ddmFormFieldName, matcher.group(1), this);
	}

	protected List<DDMFormFieldBaseRule> extractDependenciesRules(
		String expression) {

		Set<DDMFormFieldBaseRule> ddmFormFieldBaseRules = new HashSet<>();

		List<Matcher> functionMatchers = getFunctionMatchers(expression);

		for (Matcher matcher : functionMatchers) {
			while (matcher.find()) {
				DDMFormFieldBaseRule ddmFormFieldBaseRule =
					createDDMFormFieldRule(matcher);
				ddmFormFieldBaseRules.add(ddmFormFieldBaseRule);
			}
		}

		Matcher matcher = getVariableMatcher(expression);

		if (matcher != null) {
			while (matcher.find()) {
				String variable = matcher.group(1);
				ddmFormFieldBaseRules.add(
					new DDMFormFieldValueRule(variable, variable, this));
			}
		}

		return new ArrayList<>(ddmFormFieldBaseRules);
	}

	protected List<Matcher> getFunctionMatchers(String expression) {
		List<Matcher> matchers = new ArrayList<>();

		Pattern pattern = null;

		Matcher matcher = null;

		for (String patternStr :
				DDMFormRuleFunctionFactory.getFunctionPatterns()) {

			pattern = Pattern.compile(patternStr);

			matcher = pattern.matcher(expression);

			if (matcher.find()) {
				matchers.add(pattern.matcher(expression));
			}
		}

		return matchers;
	}

	protected Matcher getVariableMatcher(String expression) {
		Pattern pattern = Pattern.compile(DDMFormRuleFunction.VARIABLE_PATTERN);

		Matcher matcher = pattern.matcher(expression);

		if (matcher.find()) {
			return pattern.matcher(expression);
		}

		return null;
	}

	protected List<String> mountParameters(Matcher matcher) {
		List<String> parameters = new ArrayList<>(matcher.groupCount());

		for (int i = 1; i <= matcher.groupCount(); i++) {
			parameters.add(matcher.group(i));
		}

		return parameters;
	}

	private final DDMFormRuleEvaluatorContext _ddmFormRuleEvaluatorContext;

}