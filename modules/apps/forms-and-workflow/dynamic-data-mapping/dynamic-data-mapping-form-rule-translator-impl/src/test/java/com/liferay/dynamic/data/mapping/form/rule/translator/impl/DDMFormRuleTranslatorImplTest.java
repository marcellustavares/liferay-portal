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
import com.liferay.dynamic.data.mapping.form.rule.translator.DDMFormRuleTranslator;
import com.liferay.dynamic.data.mapping.form.rule.translator.DDMFormRuleTranslatorException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleTranslatorImplTest {

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testCatchException1() throws Exception {
		DDMForm ddmForm = new DDMForm();

		String condition = "< 5";

		List<String> actions = Collections.emptyList();

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleTranslator ddmFormRuleTranslator =
			createDDMFormRuleTranslator();

		ddmFormRuleTranslator.translate(ddmForm);
	}

	@Test(expected = DDMFormRuleTranslatorException.class)
	public void testCatchException2() throws Exception {
		String json = read("invalid_action_1.json");

		DDMFormRuleTranslator ddmFormRuleTranslator =
			createDDMFormRuleTranslator();

		ddmFormRuleTranslator.translate(json);
	}

	@Test
	public void testTranslateJSON() throws Exception {
		String json = read("simple_rule.json");

		DDMFormRuleTranslator ddmFormRuleTranslator =
			createDDMFormRuleTranslator();

		List<DDMFormRule> ddmFormRules = ddmFormRuleTranslator.translate(json);

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

	@Test
	public void testTranslateModel() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField ddmFormField1 = new DDMFormField("Field1", "number");
		ddmForm.addDDMFormField(ddmFormField1);

		String condition = "2 + 7 < 5";

		String action = "set(fieldAt(\"Field1\", 0), \"visible\", false)";

		List<String> actions = Arrays.asList(action);

		DDMFormRule ddmFormRule = new DDMFormRule(condition, actions);
		List<DDMFormRule> ddmFormRules = Arrays.asList(ddmFormRule);

		ddmForm.setDDMFormRules(ddmFormRules);

		DDMFormRuleTranslator ddmFormRuleTranslator =
			createDDMFormRuleTranslator();

		String result = ddmFormRuleTranslator.translate(ddmForm);

		JSONArray jsonArray = _jsonFactory.createJSONArray(result);

		assertTranslateModel(jsonArray);
	}

	protected void assertAction(
		JSONObject jsonObject, String action, String target) {

		Assert.assertTrue(jsonObject.has("action"));
		Assert.assertTrue(jsonObject.has("target"));

		Assert.assertEquals(action, jsonObject.getString("action"));
		Assert.assertEquals(target, jsonObject.getString("target"));
	}

	protected void assertTranslateModel(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		Assert.assertTrue(jsonObject.has("conditions"));
		Assert.assertTrue(jsonObject.has("actions"));

		JSONArray conditions = jsonObject.getJSONArray("conditions");

		Assert.assertEquals(1, conditions.length());

		JSONObject condition1 = conditions.getJSONObject(0);

		assertTranslateModelCondition1(condition1);

		JSONArray actions = jsonObject.getJSONArray("actions");

		assertTranslateModelActions(actions);
	}

	protected void assertTranslateModelActions(JSONArray jsonArray) {
		Assert.assertEquals(1, jsonArray.length());

		assertAction(jsonArray.getJSONObject(0), "hide", "Field1");
	}

	protected void assertTranslateModelArithmetic1(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("type"));
		Assert.assertTrue(jsonObject.has("value"));

		JSONObject expression = jsonObject.getJSONObject("value");

		Assert.assertTrue(expression.has("operands"));
		Assert.assertTrue(expression.has("operator"));

		Assert.assertEquals("addition", expression.getString("operator"));

		JSONArray operands = expression.getJSONArray("operands");

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

	protected void assertTranslateModelCondition1(JSONObject jsonObject) {
		Assert.assertTrue(jsonObject.has("operands"));
		Assert.assertTrue(jsonObject.has("operator"));

		Assert.assertEquals("less-than", jsonObject.getString("operator"));

		JSONArray operands = jsonObject.getJSONArray("operands");

		Assert.assertEquals(2, operands.length());

		JSONObject operand1 = operands.getJSONObject(0);

		assertTranslateModelArithmetic1(operand1);

		JSONObject operand2 = operands.getJSONObject(1);

		Assert.assertTrue(operand2.has("type"));
		Assert.assertTrue(operand2.has("value"));
		Assert.assertEquals("constant", operand2.getString("type"));
		Assert.assertEquals("5", operand2.getString("value"));
	}

	protected DDMFormRuleTranslator createDDMFormRuleTranslator() {
		DDMFormRuleTranslatorImpl ddmFormRuleTranslator =
			new DDMFormRuleTranslatorImpl();

		ddmFormRuleTranslator.setDDMExpressionFactory(_ddmExpressionFactory);
		ddmFormRuleTranslator.setJSONFactory(_jsonFactory);

		return ddmFormRuleTranslator;
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

	private final DDMExpressionFactory _ddmExpressionFactory =
		new DDMExpressionFactoryImpl();
	private final JSONFactory _jsonFactory = new JSONFactoryImpl();

}