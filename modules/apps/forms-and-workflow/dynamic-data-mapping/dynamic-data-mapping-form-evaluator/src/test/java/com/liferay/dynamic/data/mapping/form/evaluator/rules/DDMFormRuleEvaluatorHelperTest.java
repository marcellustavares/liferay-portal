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

package com.liferay.dynamic.data.mapping.form.evaluator.rules;

import com.liferay.dynamic.data.mapping.expression.internal.DDMExpressionFactoryImpl;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorGraph;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorHelper;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorNode;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRule;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@RunWith(PowerMockRunner.class)
public class DDMFormRuleEvaluatorHelperTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		setUpLanguageUtil();
	}

	@Test
	public void testEvaluateBetween() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"between(field1,10,field0)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("30"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("15"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				new DDMExpressionFactoryImpl(), ddmForm);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>(
			ddmFormRuleEvaluatorGraph.getNodes());

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"between(field1,10,field0)", DDMFormFieldRuleType.READ_ONLY,
				"field1", StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode0));

		ddmFormRuleEvaluatorNode0 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode0));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field0", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode1));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field1", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode2));
	}

	@Test
	public void testEvaluateCalculatedValue() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"field1 * field2", DDMFormFieldRuleType.VALUE);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("test"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("10"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("5"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				new DDMExpressionFactoryImpl(), ddmForm);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>(
			ddmFormRuleEvaluatorGraph.getNodes());

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"field1 * field2", DDMFormFieldRuleType.VALUE, "field0",
				StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode0));

		ddmFormRuleEvaluatorNode0 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode0));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field1", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode1));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field2", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode2));
	}

	@Test
	public void testEvaluateContains() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"contains(field1,\"val\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("field0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				new DDMExpressionFactoryImpl(), ddmForm);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>(
			ddmFormRuleEvaluatorGraph.getNodes());

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"contains(field1,\"val\")", DDMFormFieldRuleType.READ_ONLY,
				"field0", StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode0));

		ddmFormRuleEvaluatorNode0 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode0));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field1", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode1));
	}

	@Test
	public void testEvaluateEquals() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"equals(field1,\"value1\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("field0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				new DDMExpressionFactoryImpl(), ddmForm);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>(
			ddmFormRuleEvaluatorGraph.getNodes());

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"equals(field1,\"value1\")", DDMFormFieldRuleType.READ_ONLY,
				"field0", StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode0));

		ddmFormRuleEvaluatorNode0 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode0));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field1", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode1));
	}

	@Test
	public void testEvaluateReadOnly() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"equals(field0,\"read-only\")", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"contains(field1,\"nothing\")", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"isReadOnly(field0)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule2);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0",
				new UnlocalizedValue("read-only"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				new DDMExpressionFactoryImpl(), ddmForm);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>(
			ddmFormRuleEvaluatorGraph.getNodes());

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"isReadOnly(field0)", DDMFormFieldRuleType.READ_ONLY, "field1",
				StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode0));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"contains(field1,\"nothing\")", DDMFormFieldRuleType.VISIBILITY,
				"field1", StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode1));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"equals(field0,\"read-only\")", DDMFormFieldRuleType.READ_ONLY,
				"field0", StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode2));

		ddmFormRuleEvaluatorNode1 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode1));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode3 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field1", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode1.getEdges().contains(
				ddmFormRuleEvaluatorNode3));

		ddmFormRuleEvaluatorNode2 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode2));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode4 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field0", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode2.getEdges().contains(
				ddmFormRuleEvaluatorNode4));
	}

	@Test
	public void testEvaluateVisible() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"equals(field0,\"test\")", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"isVisible(field1)", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField2.addDDMFormFieldRule(ddmFormFieldRule2);

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("test"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2", new UnlocalizedValue("value2"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				new DDMExpressionFactoryImpl(), ddmForm);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>(
			ddmFormRuleEvaluatorGraph.getNodes());

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"isVisible(field1)", DDMFormFieldRuleType.VISIBILITY, "field2",
				StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode0));

		ddmFormRuleEvaluatorNode0 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode0));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"equals(field0,\"test\")", DDMFormFieldRuleType.VISIBILITY,
				"field1", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode1));

		ddmFormRuleEvaluatorNode1 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode1));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode3 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field0", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode1.getEdges().contains(
				ddmFormRuleEvaluatorNode3));
	}

	@Test
	public void testEvaluateWithDifferentFunctions() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"equals(field1,\"test\") && not(contains(field2,\"hello\"))",
			DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue(""));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("test"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field2_instanceId", "field2",
				new UnlocalizedValue("hello world"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				new DDMExpressionFactoryImpl(), ddmForm);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>(
			ddmFormRuleEvaluatorGraph.getNodes());

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"equals(field1,\"test\") && not(contains(field2,\"hello\"))",
				DDMFormFieldRuleType.VISIBILITY, "field0", StringPool.BLANK);

		Assert.assertTrue(nodes.contains(ddmFormRuleEvaluatorNode0));

		ddmFormRuleEvaluatorNode0 = nodes.get(
			nodes.indexOf(ddmFormRuleEvaluatorNode0));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field1", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode1));

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field2", StringPool.BLANK);

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode0.getEdges().contains(
				ddmFormRuleEvaluatorNode2));
	}

	protected void setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(_language);
	}

	@Mock
	private Language _language;

}