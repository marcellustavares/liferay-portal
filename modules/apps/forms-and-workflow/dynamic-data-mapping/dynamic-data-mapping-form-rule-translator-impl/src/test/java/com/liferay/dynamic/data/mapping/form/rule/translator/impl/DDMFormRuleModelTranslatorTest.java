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

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.internal.DDMExpressionFactoryImpl;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.dynamic.data.mapping.model.DDMFormRuleType;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleModelTranslatorTest {

	@Test
	public void testArithmeticCondition() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");
		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");
		ddmForm.addDDMFormField(ddmFormField2);

		String condition = "2 * 7 < 5 / get(fieldAt(\"Field1\", 0), \"value\")";

		String action = "set(fieldAt(\"Field2\", 0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.VISIBILITY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertArithmeticCondition(jsonArray);
	}

	@Test
	public void testDisableRule() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");

		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");

		ddmForm.addDDMFormField(ddmFormField2);

		String condition = "get(fieldAt(\"Field1\", 0), \"value\") >= 0";

		String action = "set(fieldAt(\"Field2\", 0), \"readOnly\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.VISIBILITY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertDisableRule(jsonArray);
	}

	@Test
	public void testEmptyRules() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(0, jsonArray.length());
	}

	@Test
	public void testEnableAndVisibleRule() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");

		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");

		ddmForm.addDDMFormField(ddmFormField2);

		DDMFormField ddmFormField3 = new DDMFormField("Field3", "number");

		ddmForm.addDDMFormField(ddmFormField3);

		String condition =
			"(get(fieldAt(\"Field1\", 0), \"value\") >= 0 && " +
				"get(fieldAt(\"Field2\", 0), \"value\") > 1) || " +
					"equals(get(fieldAt(\"Field1\", 0), \"value\"), " +
						"get(fieldAt(\"Field2\", 0), \"value\"))";

		String action1 = "set(fieldAt(\"Field3\", 0), \"readOnly\", false)";

		String action2 = "set(fieldAt(\"Field3\", 0), \"visible\", true)";

		List<String> actions = Arrays.asList(action1, action2);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.READ_ONLY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertEnableAndVisibleRule(jsonArray);
	}

	@Test
	public void testFunctionAsOperand() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");
		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");
		ddmForm.addDDMFormField(ddmFormField2);

		DDMFormField ddmFormField3 = new DDMFormField("Field3", "number");
		ddmForm.addDDMFormField(ddmFormField3);

		DDMFormField ddmFormField4 = new DDMFormField("Field4", "number");
		ddmForm.addDDMFormField(ddmFormField4);

		String condition =
			"sum(get(fieldAt(\"Field1\", 0), \"value\"), " +
				"get(fieldAt(\"Field2\", 0), \"value\"), " +
					"get(fieldAt(\"Field3\", 0), \"value\")) > 0";

		String action = "set(fieldAt(\"Field4\",0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.VISIBILITY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertFunctionAsOperand(jsonArray);
	}

	@Test
	public void testFunctionWithoutParameters() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");
		ddmForm.addDDMFormField(ddmFormField1);

		String condition = "test() < 10";

		String action = "set(fieldAt(\"Field1\", 0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.VISIBILITY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertFunctionWithoutParameters(jsonArray);
	}

	@Test
	public void testHideRule() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");

		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");

		ddmForm.addDDMFormField(ddmFormField2);

		String condition = "get(fieldAt(\"Field1\", 0), \"value\") > 10";

		String action = "set(fieldAt(\"Field2\", 0), \"visible\", false)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.VISIBILITY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertHideRule(jsonArray);
	}

	@Test
	public void testHideWithAndExpression() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");

		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "string");

		ddmForm.addDDMFormField(ddmFormField2);

		DDMFormField ddmFormField3 = new DDMFormField("Field3", "string");

		ddmForm.addDDMFormField(ddmFormField3);

		String condition =
			"between(get(fieldAt(\"Field1\", 0), \"value\"), 1, 10) " +
				"&& equals(get(fieldAt(\"Field2\", 0), \"value\"), \"test\")";

		String action = "set(fieldAt(\"Field3\", 0), \"visible\", false)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.READ_ONLY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertHideWithAndExpression(jsonArray);
	}

	@Test
	public void testNoCondition() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField = new DDMFormField("Field2", "number");

		ddmForm.addDDMFormField(ddmFormField);

		String condition = "TRUE";

		String action = "set(fieldAt(\"Field2\", 0), \"readOnly\", false)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.READ_ONLY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertNoCondition(jsonArray);
	}

	@Test
	public void testShowRule() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");
		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");
		ddmForm.addDDMFormField(ddmFormField2);

		DDMFormField ddmFormField3 = new DDMFormField("Field3", "number");
		ddmForm.addDDMFormField(ddmFormField3);

		String condition =
			"get(fieldAt(\"Field1\", 0), \"value\") <= " +
				"get(fieldAt(\"Field2\", 0), \"value\")";

		String action = "set(fieldAt(\"Field3\", 0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.VISIBILITY, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertShowRule(jsonArray);
	}

	@Test
	public void testShowRuleWithFunctionAsOperator() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");
		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");
		ddmForm.addDDMFormField(ddmFormField2);

		DDMFormField ddmFormField3 = new DDMFormField("Field3", "number");
		ddmForm.addDDMFormField(ddmFormField3);

		String condition =
			"contains(get(fieldAt(\"Field1\", 0), \"value\"), " +
				"get(fieldAt(\"Field2\", 0), \"value\"))";

		String action = "set(fieldAt(\"Field3\", 0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.AUTO_FILL, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertShowRuleWithFunctionAsOperator(jsonArray);
	}

	@Test
	public void testShowRuleWithOrExpression() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");
		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");
		ddmForm.addDDMFormField(ddmFormField2);

		DDMFormField ddmFormField3 = new DDMFormField("Field3", "string");
		ddmForm.addDDMFormField(ddmFormField3);

		String condition =
			"not equals(get(fieldAt(\"Field1\", 0), \"value\"), 5) " +
				"|| contains(get(fieldAt(\"Field3\", 0), \"value\"), \"test\")";

		String action = "set(fieldAt(\"Field2\",0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(
			condition, DDMFormRuleType.NOT_AVAILABLE, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertShowRuleWithOrExpression(jsonArray);
	}

	@Test
	public void testTwoRules() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");
		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");
		ddmForm.addDDMFormField(ddmFormField2);

		DDMFormField ddmFormField3 = new DDMFormField("Field3", "number");
		ddmForm.addDDMFormField(ddmFormField3);

		String condition =
			"equals(get(fieldAt(\"Field1\", 0), \"value\"), \"showField\")";

		String action = "set(fieldAt(\"Field2\",0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule1 = new DDMFormRule(
			condition, DDMFormRuleType.VISIBILITY, actions);

		condition =
			"equals(get(fieldAt(\"Field3\", 0), \"value\"), \"hideField\")";

		action = "set(fieldAt(\"Field2\",0), \"visible\", false)";

		actions = Arrays.asList(action);

		DDMFormRule ddmFormRule2 = new DDMFormRule(
			condition, DDMFormRuleType.VISIBILITY, actions);

		List<DDMFormRule> ddmFormRules = Arrays.asList(
			ddmFormRule1, ddmFormRule2);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertTwoRules(jsonArray);
	}

	protected void assertAction(
		JSONObject jsonObject, String action, String target) {

		Assert.assertTrue(jsonObject.has("action"));
		Assert.assertTrue(jsonObject.has("target"));

		Assert.assertEquals(action, jsonObject.getString("action"));
		Assert.assertEquals(target, jsonObject.getString("target"));
	}

	protected void assertArithmeticCondition(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));
		Assert.assertTrue(jsonObject.has("type"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		JSONObject condition = conditions.getJSONObject(0);

		assertArithmeticCondition(condition);

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertArithmeticConditionActions(actions);

		Assert.assertEquals("VISIBILITY", jsonObject.getString("type"));
	}

	protected void assertArithmeticCondition(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("operands"));
		Assert.assertTrue(jsonObject.has("operator"));

		JSONArray operands = jsonObject.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		assertArithmeticConditionOperand1(operand1);

		JSONObject operand2 = operands.getJSONObject(1);

		assertArithmeticConditionOperand2(operand2);

		Assert.assertEquals("less-than", jsonObject.getString("operator"));
	}

	protected void assertArithmeticConditionActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "show", "Field2");
	}

	protected void assertArithmeticConditionOperand1(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("type"));
		Assert.assertTrue(jsonObject.has("value"));

		Assert.assertEquals("expression", jsonObject.getString("type"));

		JSONObject expression = jsonObject.getJSONObject("value");

		Assert.assertTrue(expression.has("operands"));
		Assert.assertTrue(expression.has("operator"));

		Assert.assertEquals("multiplication", expression.getString("operator"));

		JSONArray operands = expression.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));

		Assert.assertEquals("constant", operand1.getString("type"));
		Assert.assertEquals("2", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));

		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("7", operand2.getString("value"));
	}

	protected void assertArithmeticConditionOperand2(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("type"));
		Assert.assertTrue(jsonObject.has("value"));

		Assert.assertEquals("expression", jsonObject.getString("type"));

		JSONObject expression = jsonObject.getJSONObject("value");

		Assert.assertTrue(expression.has("operands"));
		Assert.assertTrue(expression.has("operator"));

		Assert.assertEquals("division", expression.getString("operator"));

		JSONArray operands = expression.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));

		Assert.assertEquals("constant", operand1.getString("type"));
		Assert.assertEquals("5", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));

		Assert.assertEquals("field", operand2.getString("type"));
		Assert.assertEquals("Field1", operand2.getString("value"));
	}

	protected void assertDisableRule(JSONArray jsonArray) throws Exception {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject rule = jsonArray.getJSONObject(0);

		Assert.assertTrue(rule.has("conditions"));
		Assert.assertTrue(rule.has("actions"));
		Assert.assertTrue(rule.has("type"));

		JSONArray conditions = rule.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		assertFieldAndConstantComparison(
			conditions.getJSONObject(0), "Field1", "greater-than-equals", "0");

		assertDisableRuleActions(rule.getJSONArray("actions"));

		String type = rule.getString("type");

		Assert.assertEquals("VISIBILITY", type);
	}

	protected void assertDisableRuleActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "disable", "Field2");
	}

	protected void assertEnableAndVisibleRule(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));
		Assert.assertTrue(jsonObject.has("type"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(3, conditions.length());

		assertEnableAndVisibleRuleCondition1(conditions.getJSONObject(0));
		assertEnableAndVisibleRuleCondition2(conditions.getJSONObject(1));

		assertFieldAndFieldComparison(
			conditions.getJSONObject(2), "Field1", "equals-to", "Field2");

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertEnableAndVisibleRuleActions(actions);

		Assert.assertEquals("READ_ONLY", jsonObject.getString("type"));
	}

	protected void assertEnableAndVisibleRuleActions(JSONArray jsonArray) {
		Assert.assertEquals(2, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "enable", "Field3");
		assertAction(jsonArray.getJSONObject(1), "show", "Field3");
	}

	protected void assertEnableAndVisibleRuleCondition1(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("operands"));
		Assert.assertTrue(jsonObject.has("operator"));
		Assert.assertTrue(jsonObject.has("logic-operator"));

		Assert.assertEquals("AND", jsonObject.get("logic-operator"));

		assertFieldAndConstantComparison(
			jsonObject, "Field1", "greater-than-equals", "0");
	}

	protected void assertEnableAndVisibleRuleCondition2(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("operands"));
		Assert.assertTrue(jsonObject.has("operator"));
		Assert.assertTrue(jsonObject.has("logic-operator"));

		Assert.assertEquals("OR", jsonObject.get("logic-operator"));

		assertFieldAndConstantComparison(
			jsonObject, "Field2", "greater-than", "1");
	}

	protected void assertFieldAndConstantComparison(
		JSONObject jsonObject, String field, String operator, String constant) {

		Assert.assertTrue(jsonObject.has("operator"));
		Assert.assertTrue(jsonObject.has("operands"));

		Assert.assertEquals(operator, jsonObject.getString("operator"));

		JSONArray operands = jsonObject.getJSONArray("operands");

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));

		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals(field, operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));

		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals(constant, operand2.getString("value"));
	}

	protected void assertFieldAndFieldComparison(
		JSONObject jsonObject, String field1, String operator, String field2) {

		Assert.assertTrue(jsonObject.has("operator"));
		Assert.assertTrue(jsonObject.has("operands"));

		Assert.assertEquals(operator, jsonObject.getString("operator"));

		JSONArray operands = jsonObject.getJSONArray("operands");

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));

		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals(field1, operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));

		Assert.assertEquals("field", operand2.getString("type"));
		Assert.assertEquals(field2, operand2.getString("value"));
	}

	protected void assertFieldAtFunction(
		JSONObject jsonObject, String field, String constant) {

		Assert.assertTrue(jsonObject.has("type"));
		Assert.assertTrue(jsonObject.has("value"));
		Assert.assertEquals("function", jsonObject.getString("type"));

		JSONObject function = jsonObject.getJSONObject("value");

		Assert.assertTrue(function.has("parameters"));
		Assert.assertTrue(function.has("name"));

		Assert.assertEquals("fieldAt", function.getString("name"));

		JSONArray parameters = function.getJSONArray("parameters");

		JSONObject parameter1 = parameters.getJSONObject(0);

		Assert.assertTrue(parameter1.has("type"));
		Assert.assertTrue(parameter1.has("value"));

		Assert.assertEquals("field", parameter1.getString("type"));
		Assert.assertEquals(field, parameter1.getString("value"));

		JSONObject parameter2 = parameters.getJSONObject(1);

		Assert.assertTrue(parameter2.has("type"));
		Assert.assertTrue(parameter2.has("value"));

		Assert.assertEquals("constant", parameter2.getString("type"));
		Assert.assertEquals(constant, parameter2.getString("value"));
	}

	protected void assertFunctionAsOperand(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		JSONObject condition = conditions.getJSONObject(0);

		assertFunctionAsOperandCondition1(condition);

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertFunctionAsOperandActions(actions);

		Assert.assertEquals("VISIBILITY", jsonObject.getString("type"));
	}

	protected void assertFunctionAsOperandActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "show", "Field4");
	}

	protected void assertFunctionAsOperandCondition1(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("operands"));
		Assert.assertTrue(jsonObject.has("operator"));

		Assert.assertEquals("greater-than", jsonObject.get("operator"));

		JSONArray operands = jsonObject.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		assertFunctionAsOperandFunction1(operand1);

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("0", operand2.getString("value"));
	}

	protected void assertFunctionAsOperandFunction1(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("type"));
		Assert.assertTrue(jsonObject.has("value"));

		Assert.assertEquals("function", jsonObject.getString("type"));

		JSONObject function = jsonObject.getJSONObject("value");

		Assert.assertTrue(function.has("name"));
		Assert.assertTrue(function.has("parameters"));

		Assert.assertEquals("sum", function.getString("name"));

		JSONArray parameters = function.getJSONArray("parameters");

		Assert.assertEquals(3, parameters.length());

		JSONObject parameter1 = parameters.getJSONObject(0);

		Assert.assertTrue(parameter1.has("type"));
		Assert.assertTrue(parameter1.has("value"));

		Assert.assertEquals("field", parameter1.getString("type"));
		Assert.assertEquals("Field1", parameter1.getString("value"));

		JSONObject parameter2 = parameters.getJSONObject(1);

		Assert.assertTrue(parameter2.has("type"));
		Assert.assertTrue(parameter2.has("value"));

		Assert.assertEquals("field", parameter2.getString("type"));
		Assert.assertEquals("Field2", parameter2.getString("value"));

		JSONObject parameter3 = parameters.getJSONObject(2);

		Assert.assertTrue(parameter3.has("type"));
		Assert.assertTrue(parameter3.has("value"));

		Assert.assertEquals("field", parameter3.getString("type"));
		Assert.assertEquals("Field3", parameter3.getString("value"));
	}

	protected void assertFunctionWithoutParameters(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));
		Assert.assertTrue(jsonObject.has("type"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		JSONObject condition1 = conditions.getJSONObject(0);

		assertFunctionWithoutParametersCondition1(condition1);

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertFunctionWithoutParametersActions(actions);

		Assert.assertEquals("VISIBILITY", jsonObject.getString("type"));
	}

	protected void assertFunctionWithoutParametersActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "show", "Field1");
	}

	protected void assertFunctionWithoutParametersCondition1(
		JSONObject jsonObject) {

		Assert.assertTrue(jsonObject.has("operands"));
		Assert.assertTrue(jsonObject.has("operator"));

		Assert.assertEquals("less-than", jsonObject.getString("operator"));

		JSONArray operands = jsonObject.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		assertFunctionWithoutParametersFunction1(operand1);

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("10", operand2.getString("value"));
	}

	protected void assertFunctionWithoutParametersFunction1(
		JSONObject jsonObject) {

		Assert.assertTrue(jsonObject.has("type"));
		Assert.assertTrue(jsonObject.has("value"));

		Assert.assertEquals("function", jsonObject.getString("type"));

		JSONObject function = jsonObject.getJSONObject("value");

		Assert.assertTrue(function.has("name"));
		Assert.assertTrue(function.has("parameters"));

		Assert.assertEquals("test", function.getString("name"));

		JSONArray parameters = function.getJSONArray("parameters");

		Assert.assertEquals(0, parameters.length());
	}

	protected void assertGetFunction(
		JSONObject jsonObject, String field, String constant) {

		Assert.assertTrue(jsonObject.has("type"));
		Assert.assertTrue(jsonObject.has("value"));
		Assert.assertEquals("function", jsonObject.getString("type"));

		JSONObject function = jsonObject.getJSONObject("value");

		Assert.assertTrue(function.has("parameters"));
		Assert.assertTrue(function.has("name"));

		Assert.assertEquals("get", function.getString("name"));

		JSONArray parameters = function.getJSONArray("parameters");

		JSONObject parameter1 = parameters.getJSONObject(0);

		assertFieldAtFunction(parameter1, field, constant);

		JSONObject parameter2 = parameters.getJSONObject(1);

		Assert.assertTrue(parameter2.has("type"));
		Assert.assertTrue(parameter2.has("value"));

		Assert.assertEquals("constant", parameter2.getString("type"));
		Assert.assertEquals("value", parameter2.getString("value"));
	}

	protected void assertHideRule(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));
		Assert.assertTrue(jsonObject.has("type"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		JSONObject condition = conditions.getJSONObject(0);

		assertFieldAndConstantComparison(
			condition, "Field1", "greater-than", "10");

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertHideRuleActions(actions);

		Assert.assertEquals("VISIBILITY", jsonObject.getString("type"));
	}

	protected void assertHideRuleActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "hide", "Field2");
	}

	protected void assertHideWithAndExpression(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));
		Assert.assertTrue(jsonObject.has("type"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(2, conditions.length());

		assertHideWithAndExpressionCondition1(conditions.getJSONObject(0));

		assertFieldAndConstantComparison(
			conditions.getJSONObject(1), "Field2", "equals-to", "test");

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertHideWithAndExpressionActions(actions);

		Assert.assertEquals("READ_ONLY", jsonObject.getString("type"));
	}

	protected void assertHideWithAndExpressionActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "hide", "Field3");
	}

	protected void assertHideWithAndExpressionCondition1(
		JSONObject jsonObject) {

		Assert.assertTrue(jsonObject.has("operands"));
		Assert.assertTrue(jsonObject.has("operator"));
		Assert.assertTrue(jsonObject.has("logic-operator"));

		Assert.assertEquals("between", jsonObject.get("operator"));

		JSONArray operands = jsonObject.getJSONArray("operands");

		Assert.assertEquals(3, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));

		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field1", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertEquals("constant", operand2.get("type"));
		Assert.assertEquals("1", operand2.get("value"));

		JSONObject operand3 = operands.getJSONObject(2);

		Assert.assertEquals("constant", operand3.get("type"));
		Assert.assertEquals("10", operand3.get("value"));
	}

	protected void assertNoCondition(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("actions"));
		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("type"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(0, conditions.length());

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertNoConditionActions(actions);

		Assert.assertEquals("READ_ONLY", jsonObject.getString("type"));
	}

	protected void assertNoConditionActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "enable", "Field2");
	}

	protected void assertShowRule(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		JSONObject condition = conditions.getJSONObject(0);

		assertFieldAndFieldComparison(
			condition, "Field1", "less-than-equals", "Field2");

		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertShowRuleActions(actions);

		Assert.assertEquals("VISIBILITY", jsonObject.getString("type"));
	}

	protected void assertShowRuleActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "show", "Field3");
	}

	protected void assertShowRuleWithFunctionAsOperator(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		assertFieldAndFieldComparison(
			conditions.getJSONObject(0), "Field1", "contains", "Field2");

		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertShowRuleWithFunctionAsOperatorActions(actions);

		Assert.assertEquals("AUTO_FILL", jsonObject.getString("type"));
	}

	protected void assertShowRuleWithFunctionAsOperatorActions(
		JSONArray jsonArray) {

		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "show", "Field3");
	}

	protected void assertShowRuleWithOrExpression(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(2, conditions.length());

		JSONObject condition1 = conditions.getJSONObject(0);

		assertShowRuleWithOrExpressionCondition1(condition1);

		JSONObject condition2 = conditions.getJSONObject(1);

		assertFieldAndConstantComparison(
			condition2, "Field3", "contains", "test");

		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertShowRuleWithOrExpressionActions(actions);

		Assert.assertEquals("NOT_AVAILABLE", jsonObject.getString("type"));
	}

	protected void assertShowRuleWithOrExpressionActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "show", "Field2");
	}

	protected void assertShowRuleWithOrExpressionCondition1(
		JSONObject jsonObject) {

		Assert.assertTrue(jsonObject.has("operands"));
		Assert.assertTrue(jsonObject.has("operator"));
		Assert.assertTrue(jsonObject.has("logic-operator"));

		Assert.assertEquals("OR", jsonObject.get("logic-operator"));
		Assert.assertEquals("NOT", jsonObject.get("operator"));

		JSONArray operands = jsonObject.getJSONArray("operands");

		Assert.assertEquals(1, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		assertShowRuleWithOrExpressionFunction1(operand1);
	}

	protected void assertShowRuleWithOrExpressionFunction1(
		JSONObject jsonObject) {

		Assert.assertTrue(jsonObject.has("type"));
		Assert.assertTrue(jsonObject.has("value"));

		Assert.assertEquals("function", jsonObject.getString("type"));

		JSONObject function = jsonObject.getJSONObject("value");

		Assert.assertTrue(function.has("name"));
		Assert.assertTrue(function.has("parameters"));

		Assert.assertEquals("equals-to", function.getString("name"));

		JSONArray parameters = function.getJSONArray("parameters");

		JSONObject parameter1 = parameters.getJSONObject(0);

		Assert.assertTrue(parameter1.has("type"));
		Assert.assertTrue(parameter1.has("value"));

		Assert.assertEquals("field", parameter1.getString("type"));
		Assert.assertEquals("Field1", parameter1.getString("value"));

		JSONObject parameter2 = parameters.getJSONObject(1);

		Assert.assertTrue(parameter2.has("type"));
		Assert.assertTrue(parameter2.has("value"));

		Assert.assertEquals("constant", parameter2.getString("type"));
		Assert.assertEquals("5", parameter2.getString("value"));
	}

	protected void assertTwoRules(JSONArray jsonArray) {
		Assert.assertEquals(2, jsonArray.length());

		assertTwoRules1(jsonArray.getJSONObject(0));
		assertTwoRules2(jsonArray.getJSONObject(1));
	}

	protected void assertTwoRules1(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		assertFieldAndConstantComparison(
			conditions.getJSONObject(0), "Field1", "equals-to", "showField");

		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray actions = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actions.length());

		assertAction(actions.getJSONObject(0), "show", "Field2");
	}

	protected void assertTwoRules2(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		assertFieldAndConstantComparison(
			conditions.getJSONObject(0), "Field3", "equals-to", "hideField");

		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray actions = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actions.length());

		assertAction(actions.getJSONObject(0), "hide", "Field2");
	}

	protected DDMFormRuleModelTranslator createDDMFormRuleModelTranslator(
		DDMForm ddmForm) {

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			new DDMFormRuleModelTranslator(
				_ddmExpressionFactory, ddmForm, _jsonFactory);

		return ddmFormRuleModelTranslator;
	}

	private final DDMExpressionFactory _ddmExpressionFactory =
		new DDMExpressionFactoryImpl();
	private final JSONFactory _jsonFactory = new JSONFactoryImpl();

}