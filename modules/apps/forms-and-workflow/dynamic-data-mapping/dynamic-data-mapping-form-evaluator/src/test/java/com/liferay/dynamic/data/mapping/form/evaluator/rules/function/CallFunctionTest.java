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

package com.liferay.dynamic.data.mapping.form.evaluator.rules.function;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function.CallFunction;
import com.liferay.dynamic.data.mapping.form.evaluator.rules.DDMFormRuleEvaluatorBaseTest;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.portal.json.JSONArrayImpl;
import com.liferay.portal.json.JSONObjectImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
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
public class CallFunctionTest extends DDMFormRuleEvaluatorBaseTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		setUpDDMFormFactory();
	}

	@Test
	public void testDataProvider1() throws Exception {
		List<DDMFormField> ddmFormFields = new ArrayList<>();

		DDMFormField fieldDDMFormField0 = new DDMFormField("cep", "text");

		ddmFormFields.add(fieldDDMFormField0);

		DDMFormField fieldDDMFormField1 = new DDMFormField("rua", "text");

		ddmFormFields.add(fieldDDMFormField1);

		DDMFormField fieldDDMFormField2 = new DDMFormField("cidade", "text");

		ddmFormFields.add(fieldDDMFormField2);

		DDMFormValues ddmFormValues = createDDMFormValues();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"cep_instanceId", "cep", new UnlocalizedValue("52061420"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		DDMFormFieldValue fieldDDMFormFieldValue1 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"rua_instanceId", "rua", new UnlocalizedValue(""));

		ddmFormFieldValues.add(fieldDDMFormFieldValue1);

		DDMFormFieldValue fieldDDMFormFieldValue2 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"cidade_instanceId", "cidade", new UnlocalizedValue(""));

		ddmFormFieldValues.add(fieldDDMFormFieldValue2);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField1, ddmFormValues, ddmFormFieldEvaluationResults);

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField2, ddmFormValues, ddmFormFieldEvaluationResults);

		List<String> parameters = ListUtil.fromArray(
			new String[] {
				"call(2,\"cep=cep\",\"rua=logradouro;cidade=localidade\")",
				"call", "2", "cep=cep", "rua=logradouro;cidade=localidade"
			});

		JSONArray jsonArray = new JSONArrayImpl();
		JSONObject jsonObject = new JSONObjectImpl();
		jsonArray.put(jsonObject);

		jsonObject.put("localidade", "Recife");
		jsonObject.put("logradouro", "Praça de Casa Forte");

		CallFunction callFunction = new CallFunction();

		when(
			ddmDataProvider, "doGet", Matchers.any(
				DDMDataProviderContext.class)).thenReturn(jsonArray);

		String expression = callFunction.execute(
			ddmDataProviderInstanceService, ddmDataProviderTracker,
			ddmFormFieldEvaluationResults, ddmFormValuesJSONDeserializer,
			parameters);

		Assert.assertEquals(StringPool.BLANK, expression);

		Assert.assertEquals(3, ddmFormFieldEvaluationResults.size());

		DDMFormFieldEvaluationResult ddmFormFieldRuleEvaluationResult =
			ddmFormFieldEvaluationResults.get("rua");

		Assert.assertEquals(
			jsonObject.get("logradouro"),
			ddmFormFieldRuleEvaluationResult.getValue());

		ddmFormFieldRuleEvaluationResult = ddmFormFieldEvaluationResults.get(
			"cidade");

		Assert.assertEquals(
			jsonObject.get("localidade"),
			ddmFormFieldRuleEvaluationResult.getValue());
	}

	@Test(expected = DDMFormEvaluationException.class)
	public void testInvalidParameters() throws Exception {
		CallFunction callFunction = new CallFunction();

		callFunction.execute(
			null, null, null, null, ListUtil.fromArray(new String[0]));
	}

	protected void setUpDDMFormFactory() throws Exception {
		mockStatic(DDMFormFactory.class);

		when(
			ddmDataProviderInstanceService, "getDataProviderInstance",
			Matchers.anyLong()).thenReturn(_ddmDataProviderInstance);

		when(
			ddmDataProviderTracker, "getDDMDataProvider",
			Matchers.anyString()).thenReturn(ddmDataProvider);

		when(
			DDMFormFactory.class, "create",
			Matchers.any()).thenReturn(_ddmForm);

		when(
			ddmFormValuesJSONDeserializer, "deserialize",
			Matchers.any(DDMForm.class),
			Matchers.anyString()).thenReturn(_ddmFormValues);
	}

	@Mock
	protected DDMDataProvider ddmDataProvider;

	@Mock
	protected DDMDataProviderInstanceService ddmDataProviderInstanceService;

	@Mock
	protected DDMDataProviderTracker ddmDataProviderTracker;

	@Mock
	protected DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer;

	@Mock
	private DDMDataProviderInstance _ddmDataProviderInstance;

	@Mock
	private DDMForm _ddmForm;

	@Mock
	private DDMFormValues _ddmFormValues;

}