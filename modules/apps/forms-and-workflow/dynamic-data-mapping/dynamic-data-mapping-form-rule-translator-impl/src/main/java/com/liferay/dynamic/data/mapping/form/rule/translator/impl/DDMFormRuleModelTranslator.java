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
import com.liferay.dynamic.data.mapping.expression.model.ArithmeticExpression;
import com.liferay.dynamic.data.mapping.expression.model.BinaryExpression;
import com.liferay.dynamic.data.mapping.expression.model.Expression;
import com.liferay.dynamic.data.mapping.expression.model.FunctionCallExpression;
import com.liferay.dynamic.data.mapping.expression.model.NotExpression;
import com.liferay.dynamic.data.mapping.expression.model.OrExpression;
import com.liferay.dynamic.data.mapping.expression.model.Term;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
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
		JSONArray translatedDDMFormRules = _jsonFactory.createJSONArray();

		List<DDMFormRule> ddmFormRules = _ddmForm.getDDMFormRules();

		if (ListUtil.isEmpty(ddmFormRules)) {
			return translatedDDMFormRules.toString();
		}

		for (DDMFormRule ddmFormRule : ddmFormRules) {
			if (ddmFormRule.isEnabled()) {
				JSONObject translatedDDMFormRule = translateDDMFormRule(
					ddmFormRule);
				translatedDDMFormRules.put(translatedDDMFormRule);
			}
		}

		return translatedDDMFormRules.toString();
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
		FunctionCallExpression expression, JSONObject action) {

		String functionName = expression.getFunctionName();

		if (functionName.equals("set")) {
			translateSetFunction(expression, action);
		}
	}

	protected JSONObject translateAction(String action) throws PortalException {
		DDMExpression<String> ddmExpression =
			_ddmExpressionFactory.createStringDDMExpression(action);

		JSONObject translatedAction = _jsonFactory.createJSONObject();

		Expression expression = ddmExpression.getModel();

		if (expression instanceof FunctionCallExpression) {
			translateAction(
				(FunctionCallExpression)expression, translatedAction);
		}

		return translatedAction;
	}

	protected JSONArray translateActions(List<String> actions)
		throws PortalException {

		JSONArray translatedActions = _jsonFactory.createJSONArray();

		for (String action : actions) {
			JSONObject translatedAction = translateAction(action);
			translatedActions.put(translatedAction);
		}

		return translatedActions;
	}

	protected void translateAndExpression(
		AndExpression expression, JSONArray translatedConditions) {

		translateLogicExpression(expression, translatedConditions, "AND");
	}

	protected JSONObject translateArithmeticOperand(
		ArithmeticExpression arithmeticExpression) {

		JSONObject translatedOperand = _jsonFactory.createJSONObject();
		JSONObject translatedExpression = _jsonFactory.createJSONObject();

		translatedOperand.put("type", "expression");
		translatedOperand.put("value", translatedExpression);

		List<Expression> operands = Arrays.asList(
			arithmeticExpression.getLeftOperand(),
			arithmeticExpression.getRightOperand());

		JSONArray translatedOperands = translateOperands(operands);

		translatedExpression.put("operands", translatedOperands);

		String translatedOperator = translateOperator(
			arithmeticExpression.getOperator());

		translatedExpression.put("operator", translatedOperator);

		return translatedOperand;
	}

	protected void translateBinaryExpression(
		BinaryExpression expression, JSONArray translatedConditions) {

		List<Expression> operands = Arrays.asList(
			expression.getLeftOperand(), expression.getRightOperand());

		JSONObject translatedCondition = _jsonFactory.createJSONObject();

		translatedCondition.put("operands", translateOperands(operands));
		translatedCondition.put(
			"operator", translateOperator(expression.getOperator()));

		translatedConditions.put(translatedCondition);
	}

	protected JSONArray translateCondition(String expression)
		throws PortalException {

		DDMExpression<Boolean> ddmExpression =
			_ddmExpressionFactory.createBooleanDDMExpression(expression);

		JSONArray translatedConditions = _jsonFactory.createJSONArray();

		translateExpression(ddmExpression.getModel(), translatedConditions);

		return translatedConditions;
	}

	protected JSONObject translateDDMFormRule(DDMFormRule ddmFormRule)
		throws PortalException {

		JSONObject translatedDDMFormRule = _jsonFactory.createJSONObject();

		JSONArray translatedCondition = translateCondition(
			ddmFormRule.getCondition());

		translatedDDMFormRule.put("conditions", translatedCondition);

		JSONArray translatedActions = translateActions(
			ddmFormRule.getActions());

		translatedDDMFormRule.put("actions", translatedActions);

		return translatedDDMFormRule;
	}

	protected void translateExpression(
		Expression expression, JSONArray translatedConditions) {

		if (expression instanceof AndExpression) {
			translateAndExpression(
				(AndExpression)expression, translatedConditions);
		}
		else if (expression instanceof OrExpression) {
			translateOrExpression(
				(OrExpression)expression, translatedConditions);
		}
		else if (expression instanceof BinaryExpression) {
			translateBinaryExpression(
				(BinaryExpression)expression, translatedConditions);
		}
		else if (expression instanceof FunctionCallExpression) {
			translateFunctionCallExpression(
				(FunctionCallExpression)expression, translatedConditions);
		}
		else if (expression instanceof NotExpression) {
			translateNotExpression(
				(NotExpression)expression, translatedConditions);
		}
	}

	protected void translateFunctionCallExpression(
		FunctionCallExpression expression, JSONArray translatedConditions) {

		List<Expression> parameters = expression.getParameters();

		JSONObject translatedCondition = _jsonFactory.createJSONObject();

		translatedCondition.put("operator", expression.getFunctionName());

		translatedConditions.put(translatedCondition);

		if (ListUtil.isEmpty(parameters)) {
			return;
		}

		JSONArray translatedOperands = translateOperands(parameters);

		translatedCondition.put("operands", translatedOperands);
	}

	protected JSONObject translateFunctionOperand(
		FunctionCallExpression functionCallExpression) {

		JSONObject translatedOperand = _jsonFactory.createJSONObject();
		JSONObject translatedFunction = _jsonFactory.createJSONObject();

		translatedOperand.put("type", "function");
		translatedOperand.put("value", translatedFunction);

		translatedFunction.put(
			"name", functionCallExpression.getFunctionName());

		List<Expression> parameters = functionCallExpression.getParameters();

		JSONArray translatedOperands = translateOperands(parameters);

		translatedFunction.put("parameters", translatedOperands);

		return translatedOperand;
	}

	protected void translateLogicExpression(
		BinaryExpression expression, JSONArray translatedConditions,
		String logicOperator) {

		Expression leftOperand = expression.getLeftOperand();
		Expression rightOperand = expression.getRightOperand();

		translateExpression(leftOperand, translatedConditions);

		JSONObject lastTranslatedCondition = translatedConditions.getJSONObject(
			translatedConditions.length() - 1);

		lastTranslatedCondition.put("logic-operator", logicOperator);

		translateExpression(rightOperand, translatedConditions);
	}

	protected void translateNotExpression(
		NotExpression expression, JSONArray translatedConditions) {

		Expression operand = expression.getOperand();

		JSONObject translatedOperand = translateOperand(operand);

		JSONObject translatedCondition = _jsonFactory.createJSONObject();

		JSONArray translatedOperands = _jsonFactory.createJSONArray();

		translatedOperands.put(translatedOperand);

		translatedCondition.put("operands", translatedOperands);

		translatedCondition.put("operator", "NOT");

		translatedConditions.put(translatedCondition);
	}

	protected JSONObject translateOperand(Expression operand) {
		JSONObject translatedOperand = null;

		if (operand instanceof Term) {
			translatedOperand = translateTermOperand((Term)operand);
		}
		else if (operand instanceof FunctionCallExpression) {
			translatedOperand = translateFunctionOperand(
				(FunctionCallExpression)operand);
		}
		else if (operand instanceof ArithmeticExpression) {
			translatedOperand = translateArithmeticOperand(
				(ArithmeticExpression)operand);
		}

		return translatedOperand;
	}

	protected JSONArray translateOperands(List<Expression> operands) {
		JSONArray translatedOperands = _jsonFactory.createJSONArray();

		for (Expression operand : operands) {
			JSONObject translatedOperand = translateOperand(operand);

			if (translatedOperand != null) {
				translatedOperands.put(translatedOperand);
			}
		}

		return translatedOperands;
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
			case "equals":
				return "equals-to";
			case "not equals":
				return "not-equals-to";
			default:
				return operator;
		}
	}

	protected void translateOrExpression(
		OrExpression expression, JSONArray translatedConditions) {

		translateLogicExpression(expression, translatedConditions, "OR");
	}

	protected void translateSetFunction(
		FunctionCallExpression expression, JSONObject translatedAction) {

		List<Expression> parameters = expression.getParameters();

		if (ListUtil.isEmpty(parameters) || (parameters.size() < 3)) {
			return;
		}

		String fieldName = extractFieldName(parameters.get(0));

		translatedAction.put("target", fieldName);

		String property = extractProperty(parameters.get(1));

		if (property.equals("visible")) {
			translateSetVisibleFunction(parameters.get(2), translatedAction);
		}
		else if (property.equals("readOnly")) {
			translateSetReadOnlyFunction(parameters.get(2), translatedAction);
		}
	}

	protected void translateSetReadOnlyFunction(
		Expression expression, JSONObject translatedAction) {

		if (!(expression instanceof Term)) {
			return;
		}

		Term valueTerm = (Term)expression;

		String value = StringUtil.toLowerCase(valueTerm.getValue());

		if (value.equals("false")) {
			translatedAction.put("action", "enable");
		}
		else if (value.equals("true")) {
			translatedAction.put("action", "disable");
		}
	}

	protected void translateSetVisibleFunction(
		Expression expression, JSONObject translatedAction) {

		if (!(expression instanceof Term)) {
			return;
		}

		Term valueTerm = (Term)expression;

		String value = StringUtil.toLowerCase(valueTerm.getValue());

		if (value.equals("false")) {
			translatedAction.put("action", "hide");
		}
		else if (value.equals("true")) {
			translatedAction.put("action", "show");
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

		JSONObject translatedOperand = _jsonFactory.createJSONObject();

		translatedOperand.put("type", type);
		translatedOperand.put("value", value);

		return translatedOperand;
	}

	private final DDMExpressionFactory _ddmExpressionFactory;
	private final DDMForm _ddmForm;
	private final JSONFactory _jsonFactory;

}