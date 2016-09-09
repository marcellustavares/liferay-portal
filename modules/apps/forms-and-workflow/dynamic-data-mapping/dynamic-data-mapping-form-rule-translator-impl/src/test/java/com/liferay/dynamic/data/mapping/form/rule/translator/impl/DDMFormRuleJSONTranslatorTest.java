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
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleJSONTranslatorTest {

	@Test
	public void testAndExpressionCondition() throws Exception {
		String json = read("and_expression.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();
		String condition = ddmFormRule.getCondition();

		Assert.assertEquals(
			"get(fieldAt(\"Field1\", 0), \"value\") >= " +
				"get(fieldAt(\"Field2\", 0), \"value\") && " +
					"get(fieldAt(\"Field1\", 0), \"value\") <= 10",
			condition);

		Assert.assertEquals(1, actions.size());

		String action = actions.get(0);

		Assert.assertEquals(
			"set(fieldAt(\"Field4\", 0), \"readOnly\", true)", action);
	}

	@Test
	public void testConditionWithAddition() throws Exception {
		testArithmeticCondition(
			"condition_with_addition.json", "1 + 3 <= 4",
			"set(fieldAt(\"Field1\", 0), \"visible\", false)");
	}

	@Test
	public void testConditionWithArithmetic() throws Exception {
		testArithmeticCondition(
			"condition_with_arithmetic.json", "5 - 2 > 4 / 3",
			"set(fieldAt(\"Field1\", 0), \"readOnly\", false)");
	}

	@Test
	public void testConditionWithFunction() throws Exception {
		String json = read("condition_with_function.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();
		String condition = ddmFormRule.getCondition();

		Assert.assertEquals(
			"equals(get(fieldAt(\"Field1\", 0), \"value\"), " +
				"get(fieldAt(\"Field2\", 0), \"value\"))",
			condition);

		Assert.assertEquals(1, actions.size());

		String action = actions.get(0);

		Assert.assertEquals(
			"set(fieldAt(\"Field3\", 0), \"visible\", true)", action);
	}

	@Test
	public void testConditionWithFunctionAndComparison() throws Exception {
		String json = read("condition_with_function_and_comparison.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();
		String condition = ddmFormRule.getCondition();

		Assert.assertEquals(
			"contains(get(fieldAt(\"Field1\", 0), \"value\"), " +
				"get(fieldAt(\"Field2\", 0), \"value\")) && " +
					"get(fieldAt(\"Field2\", 0), \"value\") < 4",
			condition);

		Assert.assertEquals(1, actions.size());

		String action = actions.get(0);

		Assert.assertEquals(
			"set(fieldAt(\"Field3\", 0), \"readOnly\", false)", action);
	}

	@Test
	public void testConditionWithMultiplication() throws Exception {
		testArithmeticCondition(
			"condition_with_multiplication.json", "2 * 5 < 10",
			"set(fieldAt(\"Field1\", 0), \"readOnly\", true)");
	}

	@Test
	public void testEmptyActionsAndConditions() throws Exception {
		String json = read("empty_conditions_actions.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();
		String condition = ddmFormRule.getCondition();

		Assert.assertTrue(ddmFormRule.isEnabled());
		Assert.assertEquals("TRUE", condition);
		Assert.assertTrue(actions.isEmpty());
	}

	@Test
	public void testEmptyArray() throws Exception {
		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator("[]", _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(0, ddmFormRules.size());
	}

	@Test
	public void testFunctionAsOperand() throws Exception {
		String json = read("function_as_operand.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();
		String condition = ddmFormRule.getCondition();

		Assert.assertEquals("sum(1, 3, 5) < 4", condition);

		Assert.assertEquals(1, actions.size());

		String action = actions.get(0);

		Assert.assertEquals(
			"set(fieldAt(\"Field1\", 0), \"visible\", true)", action);
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidAction1() throws Exception {
		testInvalid("invalid_action_1.json");
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidAction2() throws Exception {
		testInvalid("invalid_action_2.json");
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidAction3() throws Exception {
		testInvalid("invalid_action_3.json");
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidCondition1() throws Exception {
		testInvalid("invalid_condition_1.json");
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidCondition2() throws Exception {
		testInvalid("invalid_condition_2.json");
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidDDMFormRuleJSON1() throws Exception {
		testInvalid("invalid_ddm_form_rule_1.json");
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidDDMFormRuleJSON2() throws Exception {
		testInvalid("invalid_ddm_form_rule_2.json");
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidLogicOperator() throws Exception {
		testInvalid("invalid_logic_operator.json");
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidOperand() throws Exception {
		testInvalid("invalid_operand.json");
	}

	@Test
	public void testMultipleActionsRule() throws Exception {
		String json = read("multiple_actions_rule.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();
		String condition = ddmFormRule.getCondition();

		Assert.assertTrue(ddmFormRule.isEnabled());
		Assert.assertEquals("TRUE", condition);
		Assert.assertEquals(4, actions.size());

		String action1 = actions.get(0);

		Assert.assertEquals(
			"set(fieldAt(\"Field1\", 0), \"visible\", true)", action1);

		String action2 = actions.get(1);

		Assert.assertEquals(
			"set(fieldAt(\"Field2\", 0), \"visible\", false)", action2);

		String action3 = actions.get(2);

		Assert.assertEquals(
			"set(fieldAt(\"Field3\", 0), \"readOnly\", false)", action3);

		String action4 = actions.get(3);

		Assert.assertEquals(
			"set(fieldAt(\"Field4\", 0), \"readOnly\", true)", action4);
	}

	@Test
	public void testOrExpressionCondition() throws Exception {
		String json = read("or_expression.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();
		String condition = ddmFormRule.getCondition();

		Assert.assertEquals(
			"equals(get(fieldAt(\"Field1\", 0), \"value\"), " +
				"get(fieldAt(\"Field2\", 0), \"value\")) || " +
					"not equals(get(fieldAt(\"Field1\", 0), \"value\"), 5)",
			condition);

		Assert.assertEquals(1, actions.size());

		String action = actions.get(0);

		Assert.assertEquals(
			"set(fieldAt(\"Field3\", 0), \"visible\", false)", action);
	}

	@Test
	public void testSimpleRule() throws Exception {
		String json = read("simple_rule.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();
		String condition = ddmFormRule.getCondition();

		Assert.assertEquals(
			"get(fieldAt(\"Field1\", 0), \"value\") > 1", condition);

		Assert.assertEquals(1, actions.size());

		String action = actions.get(0);

		Assert.assertEquals(
			"set(fieldAt(\"Field2\", 0), \"visible\", true)", action);
	}

	protected String getBasePath() {
		return "com/liferay/dynamic/data/mapping/form/rule/" +
			"translator/dependencies/";
	}

	protected String read(String fileName) throws Exception {
		Class<?> clazz = getClass();

		return StringUtil.read(
			clazz.getClassLoader(), getBasePath() + fileName);
	}

	protected void testArithmeticCondition(
			String file, String condition, String action)
		throws Exception {

		String json = read(file);

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		List<DDMFormRule> ddmFormRules = ddmFormRuleJSONTranslator.translate();

		Assert.assertEquals(1, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		List<String> actions = ddmFormRule.getActions();

		Assert.assertEquals(condition, ddmFormRule.getCondition());

		Assert.assertEquals(1, actions.size());

		Assert.assertEquals(action, actions.get(0));
	}

	protected void testInvalid(String file) throws Exception {
		String json = read(file);

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		ddmFormRuleJSONTranslator.translate();
	}

	private final JSONFactory _jsonFactory = new JSONFactoryImpl();

}