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

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidAction1() throws Exception {
		String json = read("invalid_action_1.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		ddmFormRuleJSONTranslator.translate();
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidAction2() throws Exception {
		String json = read("invalid_action_2.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		ddmFormRuleJSONTranslator.translate();
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidCondition1() throws Exception {
		String json = read("invalid_condition_1.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		ddmFormRuleJSONTranslator.translate();
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidCondition2() throws Exception {
		String json = read("invalid_condition_2.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		ddmFormRuleJSONTranslator.translate();
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidDDMFormRuleJSON1() throws Exception {
		String json = read("invalid_ddm_form_rule_1.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		ddmFormRuleJSONTranslator.translate();
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testInvalidDDMFormRuleJSON2() throws Exception {
		String json = read("invalid_ddm_form_rule_2.json");

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(json, _jsonFactory);

		ddmFormRuleJSONTranslator.translate();
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
			"get(fieldAt(\"Field1\", 0), \"value\") == " +
				"get(fieldAt(\"Field2\", 0), \"value\") || " +
					"get(fieldAt(\"Field1\", 0), \"value\") != 5",
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

		Assert.assertEquals("Field1 > 1", condition);

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

	private final JSONFactory _jsonFactory = new JSONFactoryImpl();

}