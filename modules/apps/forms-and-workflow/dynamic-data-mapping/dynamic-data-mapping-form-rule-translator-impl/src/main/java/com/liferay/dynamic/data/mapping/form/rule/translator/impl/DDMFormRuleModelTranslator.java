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

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.model.AndExpression;
import com.liferay.dynamic.data.mapping.expression.model.BinaryExpression;
import com.liferay.dynamic.data.mapping.expression.model.Expression;
import com.liferay.dynamic.data.mapping.expression.model.FunctionCallExpression;
import com.liferay.dynamic.data.mapping.expression.model.OrExpression;
import com.liferay.dynamic.data.mapping.expression.model.Term;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleModelTranslator {

	public DDMFormRuleModelTranslator(
		DDMExpressionFactory ddmExpressionFactory, DDMForm ddmForm,
		JSONFactory jsonFactory) {

		_ddmExpressionFactory = ddmExpressionFactory;
		_ddmForm = ddmForm;
		_jsonFactory = jsonFactory;
	}

	public String translate() throws Exception {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		List<DDMFormRule> ddmFormRules = _ddmForm.getDDMFormRules();

		if (ListUtil.isEmpty(ddmFormRules)) {
			return jsonArray.toString();
		}

		for (DDMFormRule ddmFormRule : ddmFormRules) {
			if (ddmFormRule.isEnabled()) {
				JSONObject jsonObject = translateDDMFormRule(ddmFormRule);
				jsonArray.put(jsonObject);
			}
		}

		return jsonArray.toString();
	}

	protected String extractFieldName(Expression expression) {
		if (!(expression instanceof FunctionCallExpression)) {
			return StringPool.BLANK;
		}

		FunctionCallExpression functionExpression =
			(FunctionCallExpression)expression;

		List<Expression> parameters = functionExpression.getParameters();

		if (ListUtil.isEmpty(parameters) || (parameters.size() < 2)) {
			return StringPool.BLANK;
		}

		Term fieldName = (Term)parameters.get(0);

		return fieldName.getValue();
	}

	protected String extractProperty(Expression expression) {
		if (!(expression instanceof Term)) {
			return StringPool.BLANK;
		}

		Term property = (Term)expression;

		return property.getValue();
	}

	protected void translateAction(
		FunctionCallExpression expression, JSONObject jsonObject) {

		String functionName = expression.getFunctionName();

		if (functionName.equals("set")) {
			translateSetFunction(expression, jsonObject);
		}
	}

	protected JSONObject translateAction(String action) throws PortalException {
		DDMExpression<String> ddmExpression =
			_ddmExpressionFactory.createStringDDMExpression(action);

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		Expression expression = ddmExpression.getModel();

		if (expression instanceof FunctionCallExpression) {
			translateAction((FunctionCallExpression)expression, jsonObject);
		}

		return jsonObject;
	}

	protected JSONArray translateActions(List<String> actions)
		throws PortalException {

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (String action : actions) {
			JSONObject jsonObject = translateAction(action);
			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	protected void translateAndExpression(
		AndExpression expression, JSONArray jsonArray) {

		translateLogicExpression(expression, jsonArray, "AND");
	}

	protected void translateBinaryExpression(
		BinaryExpression expression, JSONArray jsonArray) {

		List<Expression> operands = Arrays.asList(
			expression.getLeftOperand(), expression.getRightOperand());

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		jsonObject.put("operands", translateOperands(operands));
		jsonObject.put("operator", translateOperator(expression.getOperator()));

		jsonArray.put(jsonObject);
	}

	protected JSONArray translateCondition(String expression)
		throws PortalException {

		DDMExpression<Boolean> ddmExpression =
			_ddmExpressionFactory.createBooleanDDMExpression(expression);

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		translateExpression(ddmExpression.getModel(), jsonArray);

		return jsonArray;
	}

	protected JSONObject translateDDMFormRule(DDMFormRule ddmFormRule)
		throws PortalException {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		JSONArray conditions = translateCondition(ddmFormRule.getCondition());

		jsonObject.put("conditions", conditions);

		JSONArray actions = translateActions(ddmFormRule.getActions());

		jsonObject.put("actions", actions);

		return jsonObject;
	}

	protected void translateExpression(
		Expression expression, JSONArray jsonArray) {

		if (expression instanceof AndExpression) {
			translateAndExpression((AndExpression)expression, jsonArray);
		}
		else if (expression instanceof OrExpression) {
			translateOrExpression((OrExpression)expression, jsonArray);
		}
		else if (expression instanceof BinaryExpression) {
			translateBinaryExpression((BinaryExpression)expression, jsonArray);
		}
		else if (expression instanceof FunctionCallExpression) {
			translateFunctionCallExpression(
				(FunctionCallExpression)expression, jsonArray);
		}
	}

	protected void translateFunctionCallExpression(
		FunctionCallExpression expression, JSONArray jsonArray) {

		List<Expression> parameters = expression.getParameters();

		if (ListUtil.isEmpty(parameters)) {
			return;
		}

		JSONArray operands = translateOperands(parameters);

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		jsonObject.put("operands", operands);
		jsonObject.put("operator", expression.getFunctionName());

		jsonArray.put(jsonObject);
	}

	protected JSONObject translateFunctionOperand(
		FunctionCallExpression functionCallExpression) {

		JSONObject jsonObject = _jsonFactory.createJSONObject();
		JSONObject functionJSONObject = _jsonFactory.createJSONObject();

		jsonObject.put("type", "function");
		jsonObject.put("value", functionJSONObject);

		functionJSONObject.put(
			"name", functionCallExpression.getFunctionName());

		List<Expression> parameters = functionCallExpression.getParameters();

		JSONArray jsonArray = translateOperands(parameters);

		functionJSONObject.put("parameters", jsonArray);

		return jsonObject;
	}

	protected void translateLogicExpression(
		BinaryExpression expression, JSONArray jsonArray,
		String logicOperator) {

		Expression leftOperand = expression.getLeftOperand();
		Expression rightOperand = expression.getRightOperand();

		translateExpression(leftOperand, jsonArray);

		JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);

		jsonObject.put("logic-operator", logicOperator);

		translateExpression(rightOperand, jsonArray);
	}

	protected JSONArray translateOperands(List<Expression> operands) {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (Expression operand : operands) {
			JSONObject jsonObject = null;

			if (operand instanceof Term) {
				jsonObject = translateTermOperand((Term)operand);
			}
			else if (operand instanceof FunctionCallExpression) {
				jsonObject = translateFunctionOperand(
					(FunctionCallExpression)operand);
			}

			if (jsonObject != null) {
				jsonArray.put(jsonObject);
			}
		}

		return jsonArray;
	}

	protected String translateOperator(String operator) {
		switch (operator) {
			case "+":
				return "addition";
			case "-":
				return "subtraction";
			case "/":
				return "division";
			case "*":
				return "multiplication";
			case ">":
				return "greater-than";
			case ">=":
				return "greater-than-equals";
			case "<":
				return "less-than";
			case "<=":
				return "less-than-equals";
			case "==":
				return "equals-to";
			case "!=":
				return "not-equals-to";
			default:
				return operator;
		}
	}

	protected void translateOrExpression(
		OrExpression expression, JSONArray jsonArray) {

		translateLogicExpression(expression, jsonArray, "OR");
	}

	protected void translateSetFunction(
		FunctionCallExpression expression, JSONObject jsonObject) {

		List<Expression> parameters = expression.getParameters();

		if (ListUtil.isEmpty(parameters) || (parameters.size() < 3)) {
			return;
		}

		String fieldName = extractFieldName(parameters.get(0));

		jsonObject.put("target", fieldName);

		String property = extractProperty(parameters.get(1));

		if (property.equals("visible")) {
			translateSetVisibleFunction(parameters.get(2), jsonObject);
		}
		else if (property.equals("readOnly")) {
			translateSetReadOnlyFunction(parameters.get(2), jsonObject);
		}
	}

	protected void translateSetReadOnlyFunction(
		Expression expression, JSONObject jsonObject) {

		if (!(expression instanceof Term)) {
			return;
		}

		Term valueTerm = (Term)expression;

		String value = StringUtil.toLowerCase(valueTerm.getValue());

		if (value.equals("false")) {
			jsonObject.put("action", "enable");
		}
		else if (value.equals("true")) {
			jsonObject.put("action", "disable");
		}
	}

	protected void translateSetVisibleFunction(
		Expression expression, JSONObject jsonObject) {

		if (!(expression instanceof Term)) {
			return;
		}

		Term valueTerm = (Term)expression;

		String value = StringUtil.toLowerCase(valueTerm.getValue());

		if (value.equals("false")) {
			jsonObject.put("action", "hide");
		}
		else if (value.equals("true")) {
			jsonObject.put("action", "show");
		}
	}

	protected JSONObject translateTermOperand(Term term) {
		Map<String, DDMFormField> ddmFormFields = _ddmForm.getDDMFormFieldsMap(
			true);

		String value = term.getValue();
		String type = "constant";

		if (ddmFormFields.containsKey(value)) {
			type = "field";
		}

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		jsonObject.put("type", type);
		jsonObject.put("value", value);

		return jsonObject;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormRuleModelTranslator.class);

	private final DDMExpressionFactory _ddmExpressionFactory;
	private final DDMForm _ddmForm;
	private final JSONFactory _jsonFactory;

}