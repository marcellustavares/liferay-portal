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

package com.liferay.dynamic.data.mapping.form.rule.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormRuleEvaluator;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRule;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Leonardo Barros
 */
@RunWith(Arquillian.class)
public class DDMFormRuleEvaluatorTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		DDMDataProvider[] ddmDataProviders = registry.getServices(
			"com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider",
			"(ddm.data.provider.type=rest)");

		_ddmDataProvider = ddmDataProviders[0];

		_ddmDataProviderInstanceLocalService = registry.getService(
			DDMDataProviderInstanceLocalService.class);

		_ddmFormRuleEvaluator = registry.getService(DDMFormRuleEvaluator.class);
		
		PermissionChecker permissionChecker = 
			PermissionCheckerFactoryUtil.create(TestPropsValues.getUser());
		
		PermissionThreadLocal.setPermissionChecker(permissionChecker);
	}

	@Test
	public void testCallDataProvider() throws Exception {
		Class<?> ddmDataProviderSettings = _ddmDataProvider.getSettings();

		DDMForm ddmFormDataProvider = DDMFormFactory.create(
			ddmDataProviderSettings);

		DDMFormValues ddmFormValuesDataProvider =
			DDMFormValuesTestUtil.createDDMFormValues(ddmFormDataProvider);

		ddmFormValuesDataProvider.addDDMFormFieldValue(
			DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
				"url", "http://viacep.com.br/ws/52061420/json/unicode"));

		for (DDMFormField ddmFormField :
				ddmFormDataProvider.getDDMFormFields()) {

			if (ddmFormField.getName().equals("url")) {
				continue;
			}

			ddmFormValuesDataProvider.addDDMFormFieldValue(
				DDMFormValuesTestUtil.createUnlocalizedDDMFormFieldValue(
					ddmFormField.getName(), StringPool.BLANK));
		}

		Map<Locale, String> nameMap = new HashMap<>();

		nameMap.put(LocaleUtil.US, "data provider");

		Map<Locale, String> descriptionMap = new HashMap<>();

		descriptionMap.put(LocaleUtil.US, "data provider");
		
		DDMDataProviderInstance ddmDataProviderInstance = createDataProvider(
			nameMap, descriptionMap, ddmFormValuesDataProvider);

		DDMDataProviderContext ddmDataProviderContext =
			new DDMDataProviderContext(ddmFormValuesDataProvider);

		JSONArray jsonArray = _ddmDataProvider.doGet(ddmDataProviderContext);

		if (jsonArray.length() == 0) {
			return;
		}

		JSONObject jsonObject = jsonArray.getJSONObject(0);

		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("cep", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormFieldRule ddmFormFieldRule1 = new DDMFormFieldRule(
			String.format(
				"call(%d,\"cep:cep\",\"rua:logradouro;cidade:localidade\")",
				ddmDataProviderInstance.getPrimaryKey()),
			DDMFormFieldRuleType.DATA_PROVIDER);

		fieldDDMFormField0.addDDMFormFieldRule(ddmFormFieldRule1);

		DDMFormField fieldDDMFormField1 = new DDMFormField("rua", "text");

		ddmForm.addDDMFormField(fieldDDMFormField1);

		DDMFormFieldRule ddmFormFieldRule2 = new DDMFormFieldRule(
			"TRUE", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField1.addDDMFormFieldRule(ddmFormFieldRule2);

		DDMFormField fieldDDMFormField2 = new DDMFormField("cidade", "text");

		DDMFormFieldRule ddmFormFieldRule3 = new DDMFormFieldRule(
			"isReadOnly(rua)", DDMFormFieldRuleType.READ_ONLY);

		fieldDDMFormField2.addDDMFormFieldRule(ddmFormFieldRule3);

		ddmForm.addDDMFormField(fieldDDMFormField2);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

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

		List<DDMFormFieldRuleEvaluationResult>
			ddmFormFieldRuleEvaluationResults = _ddmFormRuleEvaluator.evaluate(
				ddmForm, ddmFormValues, LocaleUtil.US);

		Assert.assertEquals(3, ddmFormFieldRuleEvaluationResults.size());
		
		for (DDMFormFieldRuleEvaluationResult ddmFormFieldRuleEvaluationResult :
				ddmFormFieldRuleEvaluationResults) {

			if (ddmFormFieldRuleEvaluationResult.getName().equals("cep")) {
				Assert.assertEquals(
					"52061420", ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertFalse(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(ddmFormFieldRuleEvaluationResult.getName().equals("rua")) {
				Assert.assertEquals(
					jsonObject.get("logradouro"),
					ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
			else if(
				ddmFormFieldRuleEvaluationResult.getName().equals("cidade")) {
				Assert.assertEquals(
					jsonObject.get("localidade"),
					ddmFormFieldRuleEvaluationResult.getValue());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isVisible());
				Assert.assertTrue(ddmFormFieldRuleEvaluationResult.isValid());
				Assert.assertTrue(
					ddmFormFieldRuleEvaluationResult.isReadOnly());
			}
		}
	}

	protected DDMDataProviderInstance createDataProvider(
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			DDMFormValues ddmFormValues)
		throws Exception {
		
		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return _ddmDataProviderInstanceLocalService.addDataProviderInstance(
			TestPropsValues.getUserId(), TestPropsValues.getGroupId(), nameMap,
			descriptionMap, ddmFormValues, "rest", serviceContext);
	}

	private DDMDataProvider _ddmDataProvider;
	private DDMDataProviderInstanceLocalService
		_ddmDataProviderInstanceLocalService;
	private DDMFormRuleEvaluator _ddmFormRuleEvaluator;

}