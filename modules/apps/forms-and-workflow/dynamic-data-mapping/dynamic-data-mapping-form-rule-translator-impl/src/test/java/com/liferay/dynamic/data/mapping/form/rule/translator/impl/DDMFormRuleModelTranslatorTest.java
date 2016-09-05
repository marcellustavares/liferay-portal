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
	public void testDisableRule() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");

		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");

		ddmForm.addDDMFormField(ddmFormField2);

		String condition = "Field1 >= 0";

		String action = "set(fieldAt(\"Field2\", 0), \"readOnly\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditionJSONArray.length());

		JSONObject conditionJSONObject = conditionJSONArray.getJSONObject(0);

		Assert.assertTrue(conditionJSONObject.has("operands"));
		Assert.assertTrue(conditionJSONObject.has("operator"));

		Assert.assertEquals(
			"greater-than-equals", conditionJSONObject.getString("operator"));

		JSONArray operands = conditionJSONObject.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field1", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("0", operand2.getString("value"));

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actionsJSONArray.length());

		JSONObject actionJSONObject = actionsJSONArray.getJSONObject(0);

		Assert.assertEquals("disable", actionJSONObject.get("action"));
		Assert.assertEquals("Field2", actionJSONObject.get("target"));
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
			"(Field1 >= 0 && Field2 > 1) || equals(Field1, Field2)";

		String action1 = "set(fieldAt(\"Field3\", 0), \"readOnly\", false)";

		String action2 = "set(fieldAt(\"Field3\", 0), \"visible\", true)";

		List<String> actions = Arrays.asList(action1, action2);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(3, conditionJSONArray.length());

		JSONObject conditionJSONObject1 = conditionJSONArray.getJSONObject(0);

		Assert.assertTrue(conditionJSONObject1.has("operands"));
		Assert.assertTrue(conditionJSONObject1.has("operator"));
		Assert.assertTrue(conditionJSONObject1.has("logic-operator"));

		Assert.assertEquals(
			"greater-than-equals", conditionJSONObject1.getString("operator"));
		Assert.assertEquals("AND", conditionJSONObject1.get("logic-operator"));

		JSONArray operands = conditionJSONObject1.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field1", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("0", operand2.getString("value"));

		JSONObject conditionJSONObject2 = conditionJSONArray.getJSONObject(1);

		Assert.assertTrue(conditionJSONObject2.has("operands"));
		Assert.assertTrue(conditionJSONObject2.has("operator"));
		Assert.assertTrue(conditionJSONObject2.has("logic-operator"));

		Assert.assertEquals(
			"greater-than", conditionJSONObject2.getString("operator"));
		Assert.assertEquals("OR", conditionJSONObject2.get("logic-operator"));

		operands = conditionJSONObject2.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field2", operand1.getString("value"));

		operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("1", operand2.getString("value"));

		JSONObject conditionJSONObject3 = conditionJSONArray.getJSONObject(2);

		Assert.assertTrue(conditionJSONObject3.has("operands"));
		Assert.assertTrue(conditionJSONObject3.has("operator"));

		Assert.assertEquals(
			"equals", conditionJSONObject3.getString("operator"));

		operands = conditionJSONObject3.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field1", operand1.getString("value"));

		operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("field", operand2.getString("type"));
		Assert.assertEquals("Field2", operand2.getString("value"));

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		Assert.assertEquals(2, actionsJSONArray.length());

		JSONObject actionJSONObject1 = actionsJSONArray.getJSONObject(0);

		Assert.assertEquals("enable", actionJSONObject1.get("action"));
		Assert.assertEquals("Field3", actionJSONObject1.get("target"));

		JSONObject actionJSONObject2 = actionsJSONArray.getJSONObject(1);

		Assert.assertEquals("show", actionJSONObject2.get("action"));
		Assert.assertEquals("Field3", actionJSONObject2.get("target"));
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

		String condition = "sum(Field1, Field2, Field3) > 0";

		String action = "set(fieldAt(\"Field4\",0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditionJSONArray.length());

		JSONObject conditionJSONObject1 = conditionJSONArray.getJSONObject(0);

		Assert.assertTrue(conditionJSONObject1.has("operands"));
		Assert.assertTrue(conditionJSONObject1.has("operator"));

		Assert.assertEquals(
			"greater-than", conditionJSONObject1.get("operator"));

		JSONArray operands = conditionJSONObject1.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("function", operand1.getString("type"));

		JSONObject functionOperand = operand1.getJSONObject("value");

		Assert.assertTrue(functionOperand.has("parameters"));
		Assert.assertTrue(functionOperand.has("name"));

		Assert.assertEquals("sum", functionOperand.getString("name"));

		JSONArray functionParameters = functionOperand.getJSONArray(
			"parameters");

		Assert.assertEquals(3, functionParameters.length());

		JSONObject functionParameter1 = functionParameters.getJSONObject(0);

		Assert.assertTrue(functionParameter1.has("type"));
		Assert.assertTrue(functionParameter1.has("value"));

		Assert.assertEquals("field", functionParameter1.getString("type"));
		Assert.assertEquals("Field1", functionParameter1.getString("value"));

		JSONObject functionParameter2 = functionParameters.getJSONObject(1);

		Assert.assertTrue(functionParameter2.has("type"));
		Assert.assertTrue(functionParameter2.has("value"));

		Assert.assertEquals("field", functionParameter2.getString("type"));
		Assert.assertEquals("Field2", functionParameter2.getString("value"));

		JSONObject functionParameter3 = functionParameters.getJSONObject(2);

		Assert.assertTrue(functionParameter3.has("type"));
		Assert.assertTrue(functionParameter3.has("value"));

		Assert.assertEquals("field", functionParameter3.getString("type"));
		Assert.assertEquals("Field3", functionParameter3.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("0", operand2.getString("value"));

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actionsJSONArray.length());

		JSONObject actionJSONObject = actionsJSONArray.getJSONObject(0);

		Assert.assertEquals("show", actionJSONObject.get("action"));
		Assert.assertEquals("Field4", actionJSONObject.get("target"));
	}

	@Test
	public void testHideRule() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");

		ddmForm.addDDMFormField(ddmFormField1);

		DDMFormField ddmFormField2 = new DDMFormField("Field2", "number");

		ddmForm.addDDMFormField(ddmFormField2);

		String condition = "Field1 > 10";

		String action = "set(fieldAt(\"Field2\", 0), \"visible\", false)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditionJSONArray.length());

		JSONObject conditionJSONObject = conditionJSONArray.getJSONObject(0);

		Assert.assertTrue(conditionJSONObject.has("operands"));
		Assert.assertTrue(conditionJSONObject.has("operator"));

		Assert.assertEquals(
			"greater-than", conditionJSONObject.getString("operator"));

		JSONArray operands = conditionJSONObject.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field1", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("10", operand2.getString("value"));

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actionsJSONArray.length());

		JSONObject actionJSONObject = actionsJSONArray.getJSONObject(0);

		Assert.assertEquals("hide", actionJSONObject.get("action"));
		Assert.assertEquals("Field2", actionJSONObject.get("target"));
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

		String condition = "between(Field1, 1, 10) && equals(Field2, \"test\")";

		String action = "set(fieldAt(\"Field3\", 0), \"visible\", false)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(2, conditionJSONArray.length());

		JSONObject conditionJSONObject1 = conditionJSONArray.getJSONObject(0);

		Assert.assertTrue(conditionJSONObject1.has("operands"));
		Assert.assertTrue(conditionJSONObject1.has("operator"));
		Assert.assertTrue(conditionJSONObject1.has("logic-operator"));

		Assert.assertEquals("between", conditionJSONObject1.get("operator"));

		JSONArray operands = conditionJSONObject1.getJSONArray("operands");

		Assert.assertEquals(3, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertEquals("field", operand1.get("type"));
		Assert.assertEquals("Field1", operand1.get("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertEquals("constant", operand2.get("type"));
		Assert.assertEquals("1", operand2.get("value"));

		JSONObject operand3 = operands.getJSONObject(2);

		Assert.assertEquals("constant", operand3.get("type"));
		Assert.assertEquals("10", operand3.get("value"));

		JSONObject conditionJSONObject2 = conditionJSONArray.getJSONObject(1);

		Assert.assertTrue(conditionJSONObject2.has("operands"));
		Assert.assertTrue(conditionJSONObject2.has("operator"));

		Assert.assertEquals("equals", conditionJSONObject2.get("operator"));

		operands = conditionJSONObject2.getJSONArray("operands");
		Assert.assertEquals(2, operands.length());

		operand1 = operands.getJSONObject(0);
		Assert.assertEquals("field", operand1.get("type"));
		Assert.assertEquals("Field2", operand1.get("value"));

		operand2 = operands.getJSONObject(1);
		Assert.assertEquals("constant", operand2.get("type"));
		Assert.assertEquals("test", operand2.get("value"));
	}

	@Test
	public void testNoCondition() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField = new DDMFormField("Field2", "number");

		ddmForm.addDDMFormField(ddmFormField);

		String condition = "TRUE";

		String action = "set(fieldAt(\"Field2\", 0), \"readOnly\", false)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(0, conditionJSONArray.length());

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actionsJSONArray.length());

		JSONObject actionJSONObject = actionsJSONArray.getJSONObject(0);

		Assert.assertEquals("enable", actionJSONObject.get("action"));
		Assert.assertEquals("Field2", actionJSONObject.get("target"));
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

		String condition = "Field1 <= Field2";

		String action = "set(fieldAt(\"Field3\", 0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditionJSONArray.length());

		JSONObject conditionJSONObject = conditionJSONArray.getJSONObject(0);

		Assert.assertTrue(conditionJSONObject.has("operands"));
		Assert.assertTrue(conditionJSONObject.has("operator"));

		Assert.assertEquals(
			"less-than-equals", conditionJSONObject.getString("operator"));

		JSONArray operands = conditionJSONObject.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field1", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("field", operand2.getString("type"));
		Assert.assertEquals("Field2", operand2.getString("value"));

		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actionsJSONArray.length());

		JSONObject actionJSONObject = actionsJSONArray.getJSONObject(0);

		Assert.assertEquals("show", actionJSONObject.get("action"));
		Assert.assertEquals("Field3", actionJSONObject.get("target"));
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

		String condition = "contains(Field1, Field2)";

		String action = "set(fieldAt(\"Field3\", 0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditionJSONArray.length());

		JSONObject conditionJSONObject = conditionJSONArray.getJSONObject(0);

		Assert.assertTrue(conditionJSONObject.has("operands"));
		Assert.assertTrue(conditionJSONObject.has("operator"));

		Assert.assertEquals(
			"contains", conditionJSONObject.getString("operator"));

		JSONArray operands = conditionJSONObject.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field1", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("field", operand2.getString("type"));
		Assert.assertEquals("Field2", operand2.getString("value"));

		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actionsJSONArray.length());

		JSONObject actionJSONObject = actionsJSONArray.getJSONObject(0);

		Assert.assertEquals("show", actionJSONObject.get("action"));
		Assert.assertEquals("Field3", actionJSONObject.get("target"));
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

		String condition = "Field1 != 5 || contains(Field3, \"test\")";

		String action = "set(fieldAt(\"Field2\",0), \"visible\", true)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			createDDMFormRuleModelTranslator(ddmForm);

		String result = ddmFormRuleModelTranslator.translate();

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));

		JSONArray conditionJSONArray = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(2, conditionJSONArray.length());

		JSONObject conditionJSONObject1 = conditionJSONArray.getJSONObject(0);

		Assert.assertTrue(conditionJSONObject1.has("operands"));
		Assert.assertTrue(conditionJSONObject1.has("operator"));
		Assert.assertTrue(conditionJSONObject1.has("logic-operator"));

		Assert.assertEquals("OR", conditionJSONObject1.get("logic-operator"));
		Assert.assertEquals(
			"not-equals-to", conditionJSONObject1.get("operator"));

		JSONArray operands = conditionJSONObject1.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field1", operand1.getString("value"));

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("5", operand2.getString("value"));

		JSONObject conditionJSONObject2 = conditionJSONArray.getJSONObject(1);

		Assert.assertTrue(conditionJSONObject2.has("operands"));
		Assert.assertTrue(conditionJSONObject2.has("operator"));

		Assert.assertEquals("contains", conditionJSONObject2.get("operator"));

		operands = conditionJSONObject2.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		operand1 = operands.getJSONObject(0);

		Assert.assertTrue(operand1.has("type"));
		Assert.assertTrue(operand1.has("value"));
		Assert.assertEquals("field", operand1.getString("type"));
		Assert.assertEquals("Field3", operand1.getString("value"));

		operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("test", operand2.getString("value"));

		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

		Assert.assertEquals(1, actionsJSONArray.length());

		JSONObject actionJSONObject = actionsJSONArray.getJSONObject(0);

		Assert.assertEquals("show", actionJSONObject.get("action"));
		Assert.assertEquals("Field2", actionJSONObject.get("target"));
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