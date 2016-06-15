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

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function.GetOptionsFunction;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.portal.json.JSONArrayImpl;
import com.liferay.portal.json.JSONObjectImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@PrepareForTest({DDMFormFactory.class, JSONFactoryUtil.class})
@RunWith(PowerMockRunner.class)
public class GetOptionsFunctionTest extends CallFunctionTest {

	@Test
	public void testGetOptions1() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("country", "select");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormValues ddmFormValues = createDDMFormValues();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"country_instanceId", "country", new UnlocalizedValue(""));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		List<String> parameters = ListUtil.fromArray(
			new String[] {
				"getOptions(1,\"\",\"country={\"key\":\"countryId\"," +
					"\"value\":\"nameCurrentValue\"}\")",
				"getOptions", "1", "",
				"country={\"key\":\"countryId\"," +
					"\"value\":\"nameCurrentValue\"}"
			});

		JSONArray jsonArray = new JSONArrayImpl();
		JSONObject jsonObject = new JSONObjectImpl();
		jsonArray.put(jsonObject);

		jsonObject.put("countryId", "1");
		jsonObject.put("nameCurrentValue", "Brazil");

		jsonObject = new JSONObjectImpl();
		jsonArray.put(jsonObject);

		jsonObject.put("countryId", "2");
		jsonObject.put("nameCurrentValue", "USA");

		GetOptionsFunction getOptionsFunction = new GetOptionsFunction();

		when(
			ddmDataProvider, "doGet", Matchers.any(
				DDMDataProviderContext.class)).thenReturn(jsonArray);

		JSONObject resultMapJSONObject = new JSONObjectImpl();

		resultMapJSONObject.put("key", "countryId");
		resultMapJSONObject.put("value", "nameCurrentValue");

		mockStatic(JSONFactoryUtil.class);

		when(JSONFactoryUtil.class, "createJSONObject", Matchers.anyString()).
			thenReturn(resultMapJSONObject);

		String expression = getOptionsFunction.execute(
			ddmDataProviderInstanceService, ddmDataProviderTracker,
			ddmFormFieldEvaluationResults, ddmFormValuesJSONDeserializer,
			parameters);

		Assert.assertEquals(StringPool.BLANK, expression);

		Assert.assertEquals(1, ddmFormFieldEvaluationResults.size());

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResults.get("country");

		List<KeyValuePair> options = ddmFormFieldEvaluationResult.getOptions();

		Assert.assertEquals(2, options.size());

		KeyValuePair option = options.get(0);

		Assert.assertEquals("1", option.getKey());
		Assert.assertEquals("Brazil", option.getValue());

		option = options.get(1);
		Assert.assertEquals("2", option.getKey());
		Assert.assertEquals("USA", option.getValue());
	}

}