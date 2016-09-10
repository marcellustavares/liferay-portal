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

package com.liferay.dynamic.data.mapping.form.rule.translator.impl;

import com.liferay.dynamic.data.mapping.form.rule.translator.DDMFormRuleTranslatorException;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.dynamic.data.mapping.model.DDMFormRuleType;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleJSONTranslator {

	public DDMFormRuleJSONTranslator(String json, JSONFactory jsonFactory) {
		_json = json;
		_jsonFactory = jsonFactory;
	}

	public List<DDMFormRule> translate() throws Exception {
		JSONArray jsonArray = _jsonFactory.createJSONArray(_json);

		List<DDMFormRule> ddmFormRules = new ArrayList<>();

		if (jsonArray.length() == 0) {
			return ddmFormRules;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			DDMFormRule ddmFormRule = translateDDMFormRule(jsonObject);

			ddmFormRules.add(ddmFormRule);
		}

		return ddmFormRules;
	}

	protected String escapeOperand(
		String functionName, String translatedOperand) {

		if (Validator.isNumber(translatedOperand)) {
			return translatedOperand;
		}

		if ("equals".equals(functionName) ||
			"not equals".equals(functionName) ||
			"contains".equals(functionName)) {

			return StringPool.QUOTE + translatedOperand + StringPool.QUOTE;
		}

		return translatedOperand;
	}

	protected boolean isAvailableAction(String action) {
		for (String availableAction : _AVAILABLE_ACTIONS) {
			if (availableAction.equals(action)) {
				return true;
			}
		}

		return false;
	}

	protected boolean isConstantOperand(JSONObject jsonObject) {
		String type = jsonObject.getString("type");
		return "constant".equals(type);
	}

	protected boolean isFunctionOperator(String operator) {
		return !Pattern.matches("\\p{Punct}+", operator);
	}

	protected String mountSetFunctionCall(
		String fieldName, String property, String value) {

		if ("value".equals(property)) {
			return String.format(
				"set(fieldAt(\"%s\", 0), \"%s\", \"%s\")", fieldName, property,
				value);
		}
		else {
			return String.format(
				"set(fieldAt(\"%s\", 0), \"%s\", %s)", fieldName, property,
				value);
		}
	}

	protected String translateAction(JSONObject action)
		throws DDMFormRuleTranslatorException {

		validateAction(action);

		return translateAction(
			action.getString("action"), action.getString("target"));
	}

	protected String translateAction(String action, String target) {
		if ("show".equals(action)) {
			return mountSetFunctionCall(target, "visible", "true");
		}
		else if ("hide".equals(action)) {
			return mountSetFunctionCall(target, "visible", "false");
		}
		else if ("enable".equals(action)) {
			return mountSetFunctionCall(target, "readOnly", "false");
		}
		else if ("disable".equals(action)) {
			return mountSetFunctionCall(target, "readOnly", "true");
		}

		return StringPool.BLANK;
	}

	protected List<String> translateActions(JSONArray actions)
		throws DDMFormRuleTranslatorException {

		List<String> translatedActions = new ArrayList<>();

		for (int i = 0; i < actions.length(); i++) {
			String action = translateAction(actions.getJSONObject(i));
			translatedActions.add(action);
		}

		return translatedActions;
	}

	protected String translateArithmeticExpression(
			String operator, JSONArray operands)
		throws DDMFormRuleTranslatorException {

		StringBundler sb = new StringBundler(5);

		String leftOperand = translateOperand(operands.getJSONObject(0));

		sb.append(leftOperand);

		sb.append(StringPool.SPACE);

		sb.append(operator);

		sb.append(StringPool.SPACE);

		String rightOperand = translateOperand(operands.getJSONObject(1));

		sb.append(rightOperand);

		return sb.toString();
	}

	protected String translateBinaryOperator(
			String operator, JSONArray operands)
		throws DDMFormRuleTranslatorException {

		StringBundler sb = new StringBundler(6);

		JSONObject firstOperand = operands.getJSONObject(0);

		validateOperand(firstOperand);

		JSONObject secondOperand = operands.getJSONObject(1);

		validateOperand(secondOperand);

		sb.append(translateOperand(firstOperand));
		sb.append(StringPool.SPACE);
		sb.append(operator);
		sb.append(StringPool.SPACE);
		sb.append(translateOperand(secondOperand));
		sb.append(StringPool.SPACE);

		return sb.toString();
	}

	protected String translateCondition(
			JSONObject condition, int conditionIndex, JSONArray conditions)
		throws DDMFormRuleTranslatorException {

		validateCondition(condition, conditionIndex, conditions);

		String operator = condition.getString("operator");
		operator = translateOperator(operator);

		JSONArray operands = condition.getJSONArray("operands");

		StringBundler sb = new StringBundler(3);

		if (isFunctionOperator(operator)) {
			String translatedFunction = translateFunction(operator, operands);

			sb.append(translatedFunction);
		}
		else {
			String translatedBinaryOperator = translateBinaryOperator(
				operator, operands);

			sb.append(translatedBinaryOperator);
		}

		String logicOperator = condition.getString(
			"logic-operator", StringPool.BLANK);

		sb.append(translateLogicOperator(logicOperator));

		sb.append(StringPool.SPACE);

		return sb.toString();
	}

	protected String translateConditions(JSONArray conditions)
		throws DDMFormRuleTranslatorException {

		if (conditions.length() == 0) {
			return "TRUE";
		}

		StringBundler sb = new StringBundler(conditions.length());

		for (int i = 0; i < conditions.length(); i++) {
			JSONObject condition = conditions.getJSONObject(i);
			String translatedCondition = translateCondition(
				condition, i, conditions);
			sb.append(translatedCondition);
		}

		return StringUtil.trim(sb.toString());
	}

	protected DDMFormRule translateDDMFormRule(JSONObject ddmFormRule)
		throws DDMFormRuleTranslatorException {

		validateDDMFormRule(ddmFormRule);

		JSONArray conditions = ddmFormRule.getJSONArray("conditions");

		String condition = translateConditions(conditions);

		JSONArray actions = ddmFormRule.getJSONArray("actions");

		List<String> translatedActions = translateActions(actions);

		String type = ddmFormRule.getString("type");

		DDMFormRuleType ddmFormRuleType = DDMFormRuleType.parse(type);

		return new DDMFormRule(condition, ddmFormRuleType, translatedActions);
	}

	protected String translateFunction(String functionName, JSONArray operands)
		throws DDMFormRuleTranslatorException {

		int capacity = 2 * operands.length() + 2;

		StringBundler sb = new StringBundler(capacity);

		sb.append(functionName);
		sb.append("(");

		for (int i = 0; i < operands.length(); i++) {
			JSONObject jsonObject = operands.getJSONObject(i);

			validateOperand(jsonObject);

			String translatedOperand = translateOperand(jsonObject);

			if (isConstantOperand(jsonObject)) {
				translatedOperand = escapeOperand(
					functionName, translatedOperand);
			}

			sb.append(translatedOperand);

			if (i < (operands.length() - 1)) {
				sb.append(", ");
			}
		}

		sb.append(") ");

		return sb.toString();
	}

	protected String translateLogicOperator(String operator) {
		if ("AND".equals(operator)) {
			return "&&";
		}
		else if ("OR".equals(operator)) {
			return "||";
		}

		return StringPool.BLANK;
	}

	protected String translateOperand(JSONObject operand)
		throws DDMFormRuleTranslatorException {

		String type = operand.getString("type");

		if ("constant".equals(type)) {
			return operand.getString("value");
		}
		else if ("arithmetic".equals(type)) {
			JSONObject jsonObject = operand.getJSONObject("value");

			String operator = jsonObject.getString("operator");
			operator = translateOperator(operator);

			JSONArray operands = jsonObject.getJSONArray("operands");
			return translateArithmeticExpression(operator, operands);
		}
		else if ("function".equals(type)) {
			JSONObject jsonObject = operand.getJSONObject("value");

			String name = jsonObject.getString("name");
			JSONArray parameters = jsonObject.getJSONArray("parameters");
			String translatedFunction = translateFunction(name, parameters);

			return translatedFunction.trim();
		}
		else {
			return String.format(
				"get(fieldAt(\"%s\", 0), \"value\")",
				operand.getString("value"));
		}
	}

	protected String translateOperator(String operator) {
		switch (operator) {
			case "addition":
				return "+";
			case "subtraction":
				return "-";
			case "division":
				return "/";
			case "multiplication":
				return "*";
			case "greater-than":
				return ">";
			case "greater-than-equals":
				return ">=";
			case "less-than":
				return "<";
			case "less-than-equals":
				return "<=";
			case "equals-to":
				return "equals";
			case "not-equals-to":
				return "not equals";
			case "is-url":
				return "isURL";
			case "is-email-address":
				return "isEmailAddress";
			default:
				return operator;
		}
	}

	protected void validateAction(JSONObject action)
		throws DDMFormRuleTranslatorException {

		if (!action.has("action")) {
			throw new DDMFormRuleTranslatorException("Expected action name");
		}

		if (!action.has("target")) {
			throw new DDMFormRuleTranslatorException(
				"Expected the target for the action");
		}

		boolean isAvailable = isAvailableAction(action.getString("action"));

		if (!isAvailable) {
			throw new DDMFormRuleTranslatorException("Unexpected action found");
		}
	}

	protected void validateCondition(
			JSONObject condition, int conditionIndex, JSONArray conditions)
		throws DDMFormRuleTranslatorException {

		if (!condition.has("operands")) {
			throw new DDMFormRuleTranslatorException(
				"An array with conditions is expected.");
		}

		if (!condition.has("operator")) {
			throw new DDMFormRuleTranslatorException(
				"An operator is expected.");
		}

		if (condition.has("logic-operator") &&
			(conditionIndex == (conditions.length() - 1))) {

			throw new DDMFormRuleTranslatorException(
				"Unexpected logic operator found.");
		}
	}

	protected void validateDDMFormRule(JSONObject ddmFormRule)
		throws DDMFormRuleTranslatorException {

		if (!ddmFormRule.has("conditions")) {
			throw new DDMFormRuleTranslatorException(
				"An array with conditions is expected.");
		}

		if (!ddmFormRule.has("actions")) {
			throw new DDMFormRuleTranslatorException(
				"An array with actions is expected.");
		}

		if (!ddmFormRule.has("type")) {
			throw new DDMFormRuleTranslatorException(
				"Rule's type not defined.");
		}
	}

	protected void validateOperand(JSONObject operand)
		throws DDMFormRuleTranslatorException {

		if (!operand.has("value")) {
			throw new DDMFormRuleTranslatorException(
				"Parameter's name is expected");
		}
	}

	private static final String[] _AVAILABLE_ACTIONS =
		new String[] {"show", "hide", "enable", "disable"};

	private final String _json;
	private final JSONFactory _jsonFactory;

}