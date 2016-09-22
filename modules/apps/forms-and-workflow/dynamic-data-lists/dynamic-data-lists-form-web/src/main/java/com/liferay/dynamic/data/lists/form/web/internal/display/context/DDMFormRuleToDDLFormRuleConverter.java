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

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.model.AndExpression;
import com.liferay.dynamic.data.mapping.expression.model.ArithmeticExpression;
import com.liferay.dynamic.data.mapping.expression.model.ComparisonExpression;
import com.liferay.dynamic.data.mapping.expression.model.Expression;
import com.liferay.dynamic.data.mapping.expression.model.ExpressionVisitor;
import com.liferay.dynamic.data.mapping.expression.model.FunctionCallExpression;
import com.liferay.dynamic.data.mapping.expression.model.MinusExpression;
import com.liferay.dynamic.data.mapping.expression.model.NotExpression;
import com.liferay.dynamic.data.mapping.expression.model.OrExpression;
import com.liferay.dynamic.data.mapping.expression.model.Term;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

/**
 * @author Marcellus Tavares
 */
public class DDMFormRuleToDDLFormRuleConverter {

	public DDMFormRuleToDDLFormRuleConverter(
		DDMFormRule ddmFormRule, DDMExpressionFactory ddmExpressionFactory) {

		_ddmFormRule = ddmFormRule;
		_ddmExpressionFactory = ddmExpressionFactory;
	}


	public DDLFormRule convert() {
		String condition = _ddmFormRule.getCondition();

		try {
			if (Objects.equals(condition, "TRUE")) {
				_booleanRule = true;

				DDLFormRule ddlFormRule = new DDLFormRule();
				for (String action : _ddmFormRule.getActions()) {
					DDMExpression<?> ddmExpression = _ddmExpressionFactory.createBooleanDDMExpression(action);

					ExpressionVisitorImpl expressionVisitor = new ExpressionVisitorImpl();

					ddmExpression.getModel().accept(expressionVisitor);


					ddlFormRule.setDDLFormRuleConditions(expressionVisitor.getConditionStack());
					ddlFormRule.addDDLFormRuleAction(expressionVisitor.getActionStack().pop());
					ddlFormRule.setLogicalOperator(expressionVisitor.getLogicalOperator());

				}
				return ddlFormRule;

			}
			else {
				DDLFormRule ddlFormRule = new DDLFormRule();

				for (String action : _ddmFormRule.getActions()) {

					DDMExpression<?> ddmExpression = _ddmExpressionFactory.createBooleanDDMExpression(action);

					ExpressionVisitorImpl expressionVisitor = new ExpressionVisitorImpl();

					ddmExpression.getModel().accept(expressionVisitor);

					ddlFormRule.addDDLFormRuleAction(expressionVisitor.getActionStack().get(0));
				}


				DDMExpression<?> ddmExpression = _ddmExpressionFactory.createBooleanDDMExpression(_ddmFormRule.getCondition());

				ExpressionVisitorImpl expressionVisitor = new ExpressionVisitorImpl(true);

				ddmExpression.getModel().accept(expressionVisitor);

				ddlFormRule.setDDLFormRuleConditions(expressionVisitor.getConditionStack());
				ddlFormRule.setLogicalOperator(expressionVisitor.getLogicalOperator());

				return ddlFormRule;
			}

		}
		catch (DDMExpressionException e) {
			e.printStackTrace();
		}



		return null;
	}


	private boolean _booleanRule;

	private DDMFormRule _ddmFormRule;
	private DDMExpressionFactory _ddmExpressionFactory;


	private class ExpressionVisitorImpl implements ExpressionVisitor<Object> {

		public ExpressionVisitorImpl() {
		}

		public ExpressionVisitorImpl(boolean condition) {
			_condition = condition;
		}

		@Override
		public Object visitAndExpression(AndExpression andExpression) {
			_logicalOperator = "and";

			Object o1  = andExpression.getLeftOperand().accept(this);
			Object o2  = andExpression.getRightOperand().accept(this);

			if (o1 instanceof DDLFormRuleCondition) {
				_conditionStack.push((DDLFormRuleCondition)o1);
			}

			if (o2 instanceof DDLFormRuleCondition) {
				_conditionStack.push((DDLFormRuleCondition)o2);
			}

			return null;
		}

		@Override
		public Object visitArithmeticExpression(
			ArithmeticExpression arithmeticExpression) {

			DDLFormRuleCondition.Operand left =
				(DDLFormRuleCondition.Operand) arithmeticExpression.getLeftOperand().accept(this);
			DDLFormRuleCondition.Operand right =
				(DDLFormRuleCondition.Operand) arithmeticExpression.getRightOperand().accept(this);

			String leftValue = left.getValue();
			String rightValue = right.getValue();

			if (!Validator.isNumber(leftValue)) {
				leftValue = StringUtil.quote(leftValue);
			}

			if (!Validator.isNumber(rightValue)) {
				rightValue = StringUtil.quote(rightValue);
			}

			return String.format(
				"%s %s %s", leftValue, arithmeticExpression.getOperator(),
				rightValue);
		}

		@Override
		public Object visitComparisonExpression(
			ComparisonExpression comparisonExpression) {

			DDLFormRuleCondition.Operand left = (DDLFormRuleCondition.Operand)comparisonExpression.getLeftOperand().accept(this);
			DDLFormRuleCondition.Operand right = (DDLFormRuleCondition.Operand)comparisonExpression.getRightOperand().accept(this);

			return new DDLFormRuleCondition(_operatorsMap.get(comparisonExpression.getOperator()),
				Arrays.asList(left, right));
		}

		@Override
		public Object visitFunctionCallExpression(
			FunctionCallExpression functionCallExpression) {

			String functionName = functionCallExpression.getFunctionName();
			List<Expression> parameters = functionCallExpression.getParameters();

			if (Objects.equals(functionName, "getValue")) {
				DDLFormRuleCondition.Operand fieldOperand = (DDLFormRuleCondition.Operand) parameters.get(0).accept(this);

				return new DDLFormRuleCondition.Operand("field", fieldOperand.getValue());
			}

			String booleanAction = _functionToBooleanActionsMap.get(functionName);
			String action = _functionToActionsMap.get(functionName);

			if (booleanAction == null &&  action == null) {
				List<DDLFormRuleCondition.Operand> operands = new ArrayList<>();

				for (Expression parameter : parameters) {
					operands.add((DDLFormRuleCondition.Operand)parameter.accept(this));
				}

				DDLFormRuleCondition condition =  new DDLFormRuleCondition(functionName, operands);

				if (_condition) {
					_conditionStack.push(condition);
				}

				return condition;
			}

			DDLFormRuleCondition.Operand fieldOperand = (DDLFormRuleCondition.Operand) parameters.get(0).accept(this);

			if (booleanAction != null) {
				DDLFormRuleCondition condition =
					(DDLFormRuleCondition) parameters.get(1).accept(this);

				_actionStack.push(new DDLFormRuleAction(booleanAction, fieldOperand.getValue()));

				if (condition != null) {
					_conditionStack.push(condition);
				}
			}
			else {
				String value =  (String) parameters.get(1).accept(this);

				_actionStack.push(new DDLFormRuleAction(action, fieldOperand.getValue(), value));
			}

			return null;

		}

		@Override
		public Object visitMinusExpression(MinusExpression minusExpression) {
			return null;
		}

		@Override
		public Object visitNotExpression(NotExpression notExpression) {
			return null;
		}

		@Override
		public Object visitOrExpression(OrExpression orExpression) {
			_logicalOperator = "or";

			Object o1  = orExpression.getLeftOperand().accept(this);
			Object o2  = orExpression.getRightOperand().accept(this);

			if (o1 instanceof DDLFormRuleCondition) {
				_conditionStack.push((DDLFormRuleCondition)o1);
			}

			if (o2 instanceof DDLFormRuleCondition) {
				_conditionStack.push((DDLFormRuleCondition)o2);
			}





			return null;
		}

		@Override
		public Object visitTerm(Term term) {
			return new DDLFormRuleCondition.Operand(
				"constant", term.getValue());
		}

		public Stack<DDLFormRuleAction> getActionStack() {
			return _actionStack;
		}

		public Stack<DDLFormRuleCondition> getConditionStack() {
			return _conditionStack;
		}

		public String getLogicalOperator() {
			return _logicalOperator;
		}

		private Stack<DDLFormRuleAction> _actionStack = new Stack<>();
		private Stack<DDLFormRuleCondition> _conditionStack = new Stack<>();
		private String _logicalOperator = "and";

		private boolean _condition = false;

		private Map<String, String> _functionToBooleanActionsMap = new HashMap<>();

		{
			_functionToBooleanActionsMap.put("setVisible", "show");
			_functionToBooleanActionsMap.put("setEnabled", "enable");
			_functionToBooleanActionsMap.put("setRequired", "require");
			_functionToBooleanActionsMap.put("setInvalid", "invalidate");
		}

		private Map<String, String> _functionToActionsMap = new HashMap<>();

		{
			_functionToActionsMap.put("setValue", "calculate");
		}

		private Map<String, String> _operatorsMap = new HashMap<>();

		{
			_operatorsMap.put(">", "greater-than");
			_operatorsMap.put(">=", "greater-than-equals");
			_operatorsMap.put("<", "less-than");
			_operatorsMap.put("<=", "less-than-equals");
		}


	}

}
