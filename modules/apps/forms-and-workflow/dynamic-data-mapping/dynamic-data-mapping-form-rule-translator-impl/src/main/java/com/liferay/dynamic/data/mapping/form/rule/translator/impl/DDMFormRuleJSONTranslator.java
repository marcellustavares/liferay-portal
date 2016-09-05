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
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

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

			DDMFormRule ddmFormRule = translateJSONObject(jsonObject);

			ddmFormRules.add(ddmFormRule);
		}

		return ddmFormRules;
	}

	protected boolean isAvailableAction(String action) {
		for (String availableAction : _AVAILABLE_ACTIONS) {
			if (availableAction.equals(action)) {
				return true;
			}
		}

		return false;
	}

	protected boolean isFunctionOperator(String operator) {
		return !Pattern.matches("\\p{Punct}+", operator);
	}

	protected String mountSetFunctionCall(
		String fieldName, String property, String value) {

		return String.format(
			"set(fieldAt(\"%s\", 0), \"%s\", %s)", fieldName, property, value);
	}

	protected String translateAction(JSONObject jsonObject)
		throws DDMFormRuleTranslatorException {

		validateActionJSON(jsonObject);

		String action = jsonObject.getString("action");
		String target = jsonObject.getString("target");

		return translateAction(action, target);
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

	protected List<String> translateActions(JSONArray jsonArray)
		throws DDMFormRuleTranslatorException {

		List<String> actions = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			String action = translateAction(jsonArray.getJSONObject(i));
			actions.add(action);
		}

		return actions;
	}

	protected String translateBinaryOperator(
			String operator, JSONArray operands)
		throws DDMFormRuleTranslatorException {

		StringBundler sb = new StringBundler(6);

		JSONObject firstOperand = operands.getJSONObject(0);

		validateOperandJSON(firstOperand);

		JSONObject secondOperand = operands.getJSONObject(1);

		validateOperandJSON(secondOperand);

		sb.append(translateOperand(firstOperand));
		sb.append(StringPool.SPACE);
		sb.append(operator);
		sb.append(StringPool.SPACE);
		sb.append(translateOperand(secondOperand));
		sb.append(StringPool.SPACE);

		return sb.toString();
	}

	protected String translateCondition(JSONObject jsonObject)
		throws DDMFormRuleTranslatorException {

		validateConditionJSON(jsonObject);

		String operator = jsonObject.getString("operator");
		operator = translateOperator(operator);

		JSONArray operands = jsonObject.getJSONArray("operands");

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

		String logicOperator = jsonObject.getString(
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
			JSONObject jsonObject = conditions.getJSONObject(i);
			String condition = translateCondition(jsonObject);
			sb.append(condition);
		}

		return StringUtil.trim(sb.toString());
	}

	protected String translateFunction(String functionName, JSONArray operands)
		throws DDMFormRuleTranslatorException {

		int capacity = 2 * operands.length() + 2;

		StringBundler sb = new StringBundler(capacity);

		sb.append(functionName);
		sb.append("(");

		for (int i = 0; i < operands.length(); i++) {
			JSONObject jsonObject = operands.getJSONObject(i);

			validateOperandJSON(jsonObject);

			String translatedOperand = translateOperand(jsonObject);

			sb.append(translatedOperand);

			if (i < (operands.length() - 1)) {
				sb.append(", ");
			}
		}

		sb.append(") ");

		return sb.toString();
	}

	protected DDMFormRule translateJSONObject(JSONObject jsonObject)
		throws DDMFormRuleTranslatorException {

		validateDDMFormRuleJSON(jsonObject);

		JSONArray conditionsJSONArray = jsonObject.getJSONArray("conditions");

		String condition = translateConditions(conditionsJSONArray);

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		List<String> actions = translateActions(actionsJSONArray);

		return new DDMFormRule(condition, actions);
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

	protected String translateOperand(JSONObject jsonObject) {
		String type = jsonObject.getString("type");

		if ("constant".equals(type)) {
			return jsonObject.getString("value");
		}
		else {
			return String.format(
				"get(fieldAt(\"%s\", 0), \"value\")",
				jsonObject.getString("value"));
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
				return "!equals";
			default:
				return operator;
		}
	}

	protected void validateActionJSON(JSONObject jsonObject)
		throws DDMFormRuleTranslatorException {

		if (!jsonObject.has("action")) {
			throw new DDMFormRuleTranslatorException("Expected action name");
		}

		if (!jsonObject.has("target")) {
			throw new DDMFormRuleTranslatorException(
				"Expected the target for the action");
		}

		boolean isAvailable = isAvailableAction(jsonObject.getString("action"));

		if (!isAvailable) {
			throw new DDMFormRuleTranslatorException("Unexpected action found");
		}
	}

	protected void validateConditionJSON(JSONObject jsonObject)
		throws DDMFormRuleTranslatorException {

		if (!jsonObject.has("operands")) {
			throw new DDMFormRuleTranslatorException(
				"An array with conditions is expected.");
		}

		if (!jsonObject.has("operator")) {
			throw new DDMFormRuleTranslatorException(
				"An operator is expected.");
		}
	}

	protected void validateDDMFormRuleJSON(JSONObject jsonObject)
		throws DDMFormRuleTranslatorException {

		if (!jsonObject.has("conditions")) {
			throw new DDMFormRuleTranslatorException(
				"An array with conditions is expected.");
		}

		if (!jsonObject.has("actions")) {
			throw new DDMFormRuleTranslatorException(
				"An array with actions is expected.");
		}
	}

	protected void validateOperandJSON(JSONObject jsonObject)
		throws DDMFormRuleTranslatorException {

		if (!jsonObject.has("value")) {
			throw new DDMFormRuleTranslatorException(
				"Parameter's name is expected");
		}
	}

	private static final String[] _AVAILABLE_ACTIONS =
		new String[] {"show", "hide", "enable", "disable"};

	private final String _json;
	private final JSONFactory _jsonFactory;

}