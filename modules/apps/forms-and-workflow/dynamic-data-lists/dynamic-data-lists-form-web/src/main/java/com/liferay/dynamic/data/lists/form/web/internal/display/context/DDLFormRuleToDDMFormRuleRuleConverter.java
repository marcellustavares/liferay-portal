/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.lists.form.web.internal.display.context;

import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author Marcellus Tavares
 */
public class DDLFormRuleToDDMFormRuleRuleConverter {

	public DDLFormRuleToDDMFormRuleRuleConverter(DDLFormRule ddlFormRule) {
		_ddlFormRule = ddlFormRule;
	}

	public List<DDMFormRule> convert() {
		List<DDMFormRule> ddmFormRules = new ArrayList<>();

		String convertedCondition = convertConditions();


		List<DDLFormRuleAction> actions = _ddlFormRule.getDDLFormRuleActions();

		for (DDLFormRuleAction ddlFormRuleAction : actions) {
			if (_actionsToBooleanFunctionMap.containsKey(ddlFormRuleAction.getAction())) {
				_booleanActionsStack.push(
					convertBooleanAction(
						ddlFormRuleAction.getAction(),
						ddlFormRuleAction.getTarget(), convertedCondition));
			}
		}

		if (!_booleanActionsStack.isEmpty()) {
			ddmFormRules.add(new DDMFormRule("TRUE", _booleanActionsStack));
		}

		for (DDLFormRuleAction ddlFormRuleAction : actions) {
			ddmFormRules.add(new DDMFormRule(convertedCondition, convertAction(ddlFormRuleAction)));
		}


		return ddmFormRules;
	}

	protected String convertBooleanAction(String action, String target, String condition) {
		String functionName = _actionsToBooleanFunctionMap.get(action);

		if (functionName != null) {
			return String.format(
				"%s('%s', %s)", functionName, target, condition);
		}

		return null;
	}

	protected String convertAction(DDLFormRuleAction ddlFormRuleAction) {
		String functionName = _actionsToFunctionFunctionMap.get(ddlFormRuleAction.getAction());

		if (functionName != null) {
			String value = ddlFormRuleAction.getValue();

			StringTokenizer stringTokenizer = new StringTokenizer(value);

			StringBundler sb = new StringBundler();

			while( stringTokenizer.hasMoreTokens() ) {
				String token = stringTokenizer.nextToken();

				if (token.startsWith("'")){
					sb.append("getValue(" + token + ")");
				}
				else {
					sb.append(token);
				}

				sb.append(" ");
			}

			sb.setIndex(sb.index() - 1);

			return String.format(
				"%s('%s', %s)", functionName, ddlFormRuleAction.getTarget(), sb.toString());
		}

		return null;
	}

	protected String convertConditions() {
		StringBundler sb = new StringBundler();

		if (_ddlFormRule.getDdlFormRuleConditions().size() == 1) {
			return convertCondition(_ddlFormRule.getDdlFormRuleConditions().get(0));
		}

		for (DDLFormRuleCondition ddlFormRuleCondition :
				_ddlFormRule.getDdlFormRuleConditions()) {

			sb.append(convertCondition(ddlFormRuleCondition));
			sb.append(" ");
			sb.append(_ddlFormRule.getLogicalOperator());
			sb.append(" ");
		}

		sb.setIndex(sb.index() - 3);

		return sb.toString();
	}

	protected String convertCondition(DDLFormRuleCondition ddlFormRuleCondition) {
		StringBundler sb = new StringBundler();

		List<DDLFormRuleCondition.Operand> operands =
				ddlFormRuleCondition.getOperands();

		if (_operatorsToFunctionMap.containsKey(ddlFormRuleCondition.getOperator())) {
			sb.append(_operatorsToFunctionMap.get(ddlFormRuleCondition.getOperator()));
			sb.append("(");

			for (DDLFormRuleCondition.Operand operand : operands) {
				sb.append(convertOperand(operand));
				sb.append(", ");
			}

			sb.setIndex(sb.index() - 1);

			sb.append(")");
		}
		else {
			sb.append(convertOperand(operands.get(0)));
			sb.append(" ");
			sb.append(_operatorsMap.get(ddlFormRuleCondition.getOperator()));
			sb.append(" ");
			sb.append(convertOperand(operands.get(1)));
		}

		return sb.toString();
	}

	protected String convertOperand(DDLFormRuleCondition.Operand operand) {
		if (Objects.equals("constant", operand.getType())) {
			if (Validator.isNumber(operand.getValue())) {
				return operand.getValue();
			}
			else {
				return "'" + operand.getValue() + "'";
			}

		}

		return "getValue('"+ operand.getValue() + "')";

	}

	private Stack<String> _booleanActionsStack = new Stack<>();

	private Map<String, String> _actionsToBooleanFunctionMap = new HashMap<>();
	{
		_actionsToBooleanFunctionMap.put("show", "setVisible");
		_actionsToBooleanFunctionMap.put("enable", "setEnabled");
		_actionsToBooleanFunctionMap.put("require", "setRequired");
		_actionsToBooleanFunctionMap.put("invalidate", "setInvalid");
	}

	private Map<String, String> _actionsToFunctionFunctionMap = new HashMap<>();
	{
		_actionsToFunctionFunctionMap.put("calculate", "setValue");
	}

	private Map<String, String> _operatorsToFunctionMap = new HashMap<>();

	{
		_operatorsToFunctionMap.put("contains", "contains");
		_operatorsToFunctionMap.put("isEmailAddress", "isEmailAddress");
		_operatorsToFunctionMap.put("equals-to", "equals");
	}

	private Map<String, String> _operatorsMap = new HashMap<>();

		{
			_operatorsMap.put("greater-than", ">");
			_operatorsMap.put("greater-than-equals", ">=");
			_operatorsMap.put("less-than", "<");
			_operatorsMap.put("less-than-equals", "<=");
		}

	private List<DDMFormRule> _ddmFormRules = new ArrayList<>();

	private DDLFormRule _ddlFormRule;

}
