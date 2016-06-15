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

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.expression.internal.DDMExpressionFactoryImpl;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluator;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorGraph;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorHelper;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRule;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@PrepareForTest({DDMFormFactory.class})
@RunWith(PowerMockRunner.class)
public class DDMFormRuleEvaluatorTest extends DDMFormRuleEvaluatorBaseTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		setUpDDMFormFactory();
	}

	@Test(expected = DDMFormEvaluationException.class)
	public void testIndirectLoopCondition() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"isVisible(field1)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"isReadOnly(field2)", DDMFormFieldRuleType.VISIBILITY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("field2", "text");

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"isReadOnly(field0)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField2.addDDMFormFieldRule(ddmFormFieldRule2);

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("value0"));

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

		DDMFormRuleEvaluator ddmFormRuleEvaluator = createDDMFormRuleEvaluator(
			ddmForm);

		ddmFormRuleEvaluator.evaluate();
	}

	@Test(expected = DDMFormEvaluationException.class)
	public void testLoopCondition() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		DDMFormFieldRule ddmFormFieldRule0 = new DDMFormFieldRule(
			"isReadOnly(field1)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule0);

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("field1", "text");

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			"isReadOnly(field0)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule1);

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("value0"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		DDMFormRuleEvaluator ddmFormRuleEvaluator = createDDMFormRuleEvaluator(
			ddmForm);

		ddmFormRuleEvaluator.evaluate();
	}

	protected DDMFormRuleEvaluator createDDMFormRuleEvaluator(DDMForm ddmForm)
		throws Exception {

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			createDDMFormRuleEvaluatorGraph(ddmForm);

		return new DDMFormRuleEvaluator(
			_ddmDataProviderInstanceService, _ddmDataProviderTracker,
			new DDMExpressionFactoryImpl(), _ddmForm, ddmFormRuleEvaluatorGraph,
			_ddmFormValues, _ddmFormValuesJSONDeserializer, LocaleUtil.US);
	}

	protected DDMFormRuleEvaluatorGraph createDDMFormRuleEvaluatorGraph(
			DDMForm ddmForm)
		throws Exception {

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				new DDMExpressionFactoryImpl(), ddmForm);

		return ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();
	}

	protected void setUpDDMFormFactory() throws Exception {
		mockStatic(DDMFormFactory.class);

		when(
			_ddmDataProviderInstanceService, "getDataProviderInstance",
			Matchers.anyLong()).thenReturn(_ddmDataProviderInstance);

		when(
			_ddmDataProviderTracker, "getDDMDataProvider",
			Matchers.anyString()).thenReturn(_ddmDataProvider);

		when(
			DDMFormFactory.class, "create",
			Matchers.any()).thenReturn(_ddmForm);

		when(
			_ddmFormValuesJSONDeserializer, "deserialize",
			Matchers.any(DDMForm.class),
			Matchers.anyString()).thenReturn(_ddmFormValues);
	}

	@Mock
	private DDMDataProvider _ddmDataProvider;

	@Mock
	private DDMDataProviderInstance _ddmDataProviderInstance;

	@Mock
	private DDMDataProviderInstanceService _ddmDataProviderInstanceService;

	@Mock
	private DDMDataProviderTracker _ddmDataProviderTracker;

	@Mock
	private DDMForm _ddmForm;

	@Mock
	private DDMFormValues _ddmFormValues;

	@Mock
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;

}