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

package com.liferay.dynamic.data.lists.internal.upgrade.v1_0_2.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.lists.helper.DDLRecordSetTestHelper;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetConstants;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adam Brandizzi
 */
@RunWith(Arquillian.class)
public class UpgradeDDLRecordSetSettingsTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_jsonFactory = new JSONFactoryImpl();

		_ddlRecordSetTestHelper = new DDLRecordSetTestHelper(_group);

		setUpUpgradeDDLRecordSetSettings();
	}

	@Test
	public void testAddRequireAuthenticationSetting() throws Exception {
		String settings = createSettings(false);

		DDLRecordSet recordSet = createRecordSet(settings);

		JSONArray fieldValues = getFieldValues(recordSet.getSettings());

		Assert.assertFalse(containsField(fieldValues, "requireAuthentication"));

		_upgradeDDLRecordSetSettings.upgrade();

		recordSet = getRecordSet(recordSet);

		fieldValues = getFieldValues(recordSet.getSettings());

		Assert.assertTrue(containsField(fieldValues, "requireAuthentication"));
	}

	@Test
	public void testDoNotRepeatRequireAuthenticationSetting() throws Exception {
		String originalSettings = createSettings(true);

		DDLRecordSet recordSet = createRecordSet(originalSettings);

		JSONArray fieldValues = getFieldValues(recordSet.getSettings());

		Assert.assertTrue(containsField(fieldValues, "requireAuthentication"));

		_upgradeDDLRecordSetSettings.upgrade();

		recordSet = getRecordSet(recordSet);

		Assert.assertEquals(originalSettings, recordSet.getSettings());
	}

	@Test
	public void testDoNotUpdateRecordSetsOfOtherScopes() throws Exception {
		String originalSettings = createSettings(false);

		DDLRecordSet recordSet = createRecordSet(
			DDLRecordSetConstants.SCOPE_DYNAMIC_DATA_LISTS, originalSettings);

		JSONArray fieldValues = getFieldValues(recordSet.getSettings());

		Assert.assertFalse(containsField(fieldValues, "requireAuthentication"));

		_upgradeDDLRecordSetSettings.upgrade();

		recordSet = getRecordSet(recordSet);

		Assert.assertEquals(originalSettings, recordSet.getSettings());
	}

	protected boolean containsField(JSONArray fieldValues, String field) {
		for (int i = 0; i < fieldValues.length(); i++) {
			JSONObject fieldValue = fieldValues.getJSONObject(i);

			String fieldName = fieldValue.getString("name");

			if (fieldName.equals(field)) {
				return true;
			}
		}

		return false;
	}

	protected JSONArray createFieldValues(boolean hasSetting) {
		JSONArray array = _jsonFactory.createJSONArray();

		array.put(getFieldValue("BGUgRKuV", "requireCaptcha", "false"));
		array.put(getFieldValue("dAjksJC7", "redirectURL", ""));
		array.put(getFieldValue("WePI6lUQ", "storageType", "json"));
		array.put(getFieldValue("BNNBRhHb", "workflowDefinition", ""));
		array.put(getFieldValue("hn1htfIn", "sendEmailNotification", "false"));
		array.put(getFieldValue("ZCg12Pq1", "emailFromName", ""));
		array.put(getFieldValue("jXHNOUKb", "emailFromAddress", ""));
		array.put(getFieldValue("joHlIcqG", "emailToAddress", ""));
		array.put(getFieldValue("SOjmIgIQ", "emailSubject", ""));
		array.put(getFieldValue("2NXSHTKI", "published", "false"));

		if (hasSetting) {
			JSONObject fieldValue = getFieldValue(
				"2NXSHTKI", "requireAuthentication", "false");

			array.put(fieldValue);
		}

		return array;
	}

	protected DDLRecordSet createRecordSet(int scope, String settings)
		throws Exception {

		DDMForm form = DDMFormTestUtil.createDDMForm("field");

		DDLRecordSet recordSet = _ddlRecordSetTestHelper.addRecordSet(form);

		recordSet.setScope(scope);

		recordSet.setSettings(settings);

		DDLRecordSetLocalServiceUtil.updateDDLRecordSet(recordSet);

		recordSet = DDLRecordSetLocalServiceUtil.getRecordSet(
			recordSet.getRecordSetId());

		return recordSet;
	}

	protected DDLRecordSet createRecordSet(String settings) throws Exception {
		return createRecordSet(DDLRecordSetConstants.SCOPE_FORMS, settings);
	}

	protected String createSettings(boolean hasSetting) {
		JSONObject object = _jsonFactory.createJSONObject();

		JSONArray availableLanguagesJSONArray = getAvailableLanguagesJSONArray(
			"en_US");

		object.put("availableLanguageIdss", availableLanguagesJSONArray);

		object.put("defaultLanguageId", "en_US");

		JSONArray fieldValues = createFieldValues(hasSetting);

		object.put("fieldValues", fieldValues);

		return object.toJSONString();
	}

	protected JSONArray getAvailableLanguagesJSONArray(String languageId) {
		JSONArray array = _jsonFactory.createJSONArray();

		array.put(languageId);

		return array;
	}

	protected JSONObject getFieldValue(
		String instanceId, String name, String value) {

		JSONObject object = _jsonFactory.createJSONObject();

		object.put("instanceId", instanceId);
		object.put("name", name);
		object.put("value", value);

		return object;
	}

	protected JSONArray getFieldValues(String settings) throws JSONException {
		JSONObject settingsJSONObject = _jsonFactory.createJSONObject(settings);

		return settingsJSONObject.getJSONArray("fieldValues");
	}

	protected DDLRecordSet getRecordSet(DDLRecordSet recordSet)
		throws PortalException {

		EntityCacheUtil.clearCache();

		recordSet = DDLRecordSetLocalServiceUtil.getDDLRecordSet(
			recordSet.getRecordSetId());

		return recordSet;
	}

	protected void setUpUpgradeDDLRecordSetSettings() {
		Registry registry = RegistryUtil.getRegistry();

		UpgradeStepRegistrator upgradeStepRegistror = registry.getService(
			"com.liferay.dynamic.data.lists.internal.upgrade." +
				"DDLServiceUpgrade");

		upgradeStepRegistror.register(
			new UpgradeStepRegistrator.Registry() {

				@Override
				public void register(
					String bundleSymbolicName, String fromSchemaVersionString,
					String toSchemaVersionString, UpgradeStep... upgradeSteps) {

					for (UpgradeStep upgradeStep : upgradeSteps) {
						Class<?> clazz = upgradeStep.getClass();

						String className = clazz.getName();

						if (className.contains("UpgradeDDLRecordSetSettings")) {
							_upgradeDDLRecordSetSettings =
								(UpgradeProcess)upgradeStep;
						}
					}
				}

			});
	}

	private DDLRecordSetTestHelper _ddlRecordSetTestHelper;

	@DeleteAfterTestRun
	private Group _group;

	private JSONFactory _jsonFactory;
	private UpgradeProcess _upgradeDDLRecordSetSettings;

}