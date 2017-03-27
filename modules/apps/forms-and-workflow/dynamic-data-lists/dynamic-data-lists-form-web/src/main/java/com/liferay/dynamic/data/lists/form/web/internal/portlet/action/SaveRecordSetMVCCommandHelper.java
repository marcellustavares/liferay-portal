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

package com.liferay.dynamic.data.lists.form.web.internal.portlet.action;

import com.liferay.dynamic.data.lists.exception.RecordSetSettingsRedirectURLException;
import com.liferay.dynamic.data.lists.form.web.internal.converter.DDLFormRuleDeserializer;
import com.liferay.dynamic.data.lists.form.web.internal.converter.DDLFormRuleToDDMFormRuleConverter;
import com.liferay.dynamic.data.lists.form.web.internal.converter.model.DDLFormRule;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetConstants;
import com.liferay.dynamic.data.lists.model.DDLRecordSetSettings;
import com.liferay.dynamic.data.lists.service.DDLRecordSetService;
import com.liferay.dynamic.data.mapping.exception.StructureDefinitionException;
import com.liferay.dynamic.data.mapping.exception.StructureLayoutException;
import com.liferay.dynamic.data.mapping.form.values.query.DDMFormValuesQuery;
import com.liferay.dynamic.data.mapping.form.values.query.DDMFormValuesQueryFactory;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.service.DDMStructureService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = SaveRecordSetMVCCommandHelper.class)
public class SaveRecordSetMVCCommandHelper {

	public DDLRecordSet saveRecordSet(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		long recordSetId = ParamUtil.getLong(portletRequest, "recordSetId");

		if (recordSetId == 0) {
			return addRecordSet(portletRequest, portletResponse);
		}
		else {
			return updateRecordSet(portletRequest, portletResponse);
		}
	}

	protected DDMStructure addDDMStructure(
			PortletRequest portletRequest, DDMFormValues settingsDDMFormValues)
		throws Exception {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDMStructure.class.getName(), portletRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long groupId = ParamUtil.getLong(portletRequest, "groupId");
		String structureKey = ParamUtil.getString(
			portletRequest, "structureKey");
		String storageType = getStorageType(settingsDDMFormValues);
		String name = ParamUtil.getString(portletRequest, "name");
		String description = ParamUtil.getString(portletRequest, "description");
		DDMForm ddmForm = getDDMForm(portletRequest, serviceContext);
		DDMFormLayout ddmFormLayout = getDDMFormLayout(portletRequest);

		return ddmStructureService.addStructure(
			groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
			_portal.getClassNameId(DDLRecordSet.class), structureKey,
			getLocalizedMap(themeDisplay.getSiteDefaultLocale(), name),
			getLocalizedMap(themeDisplay.getSiteDefaultLocale(), description),
			ddmForm, ddmFormLayout, storageType,
			DDMStructureConstants.TYPE_AUTO, serviceContext);
	}

	protected DDLRecordSet addRecordSet(
			PortletRequest portletRequest, long ddmStructureVersionId,
			DDMFormValues settingsDDMFormValues)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String name = ParamUtil.getString(portletRequest, "name");
		String description = ParamUtil.getString(portletRequest, "description");

		return addRecordSet(
			portletRequest, ddmStructureVersionId,
			getLocalizedMap(themeDisplay.getSiteDefaultLocale(), name),
			getLocalizedMap(themeDisplay.getSiteDefaultLocale(), description),
			settingsDDMFormValues);
	}

	protected DDLRecordSet addRecordSet(
			PortletRequest portletRequest, long ddmStructureVersionId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			DDMFormValues settingsDDMFormValues)
		throws Exception {

		long groupId = ParamUtil.getLong(portletRequest, "groupId");
		String recordSetKey = ParamUtil.getString(
			portletRequest, "recordSetKey");

		validateRedirectURL(settingsDDMFormValues);

		String settings = ddmFormValuesJSONSerializer.serialize(
			settingsDDMFormValues);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDLRecordSet.class.getName(), portletRequest);

		DDLRecordSet ddlRecordSet = ddlRecordSetService.addRecordSet(
			groupId, ddmStructureVersionId, recordSetKey, nameMap,
			descriptionMap, DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT,
			DDLRecordSetConstants.SCOPE_FORMS, settings, serviceContext);

		updateWorkflowDefinitionLink(
			portletRequest, ddlRecordSet, settingsDDMFormValues);

		return ddlRecordSet;
	}

	protected DDLRecordSet addRecordSet(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		DDMFormValues settingsDDMFormValues = getSettingsDDMFormValues(
			portletRequest);

		DDMStructure ddmStructure = addDDMStructure(
			portletRequest, settingsDDMFormValues);

		DDMStructureVersion ddmStructureVersion =
			ddmStructure.getLatestStructureVersion();

		DDLRecordSet recordSet = addRecordSet(
			portletRequest, ddmStructureVersion.getStructureVersionId(),
			settingsDDMFormValues);

		return recordSet;
	}

	protected DDMForm getDDMForm(
			PortletRequest portletRequest, ServiceContext serviceContext)
		throws PortalException {

		try {
			String definition = ParamUtil.getString(
				portletRequest, "definition");

			DDMForm ddmForm = ddmFormJSONDeserializer.deserialize(definition);

			serviceContext.setAttribute("form", ddmForm);

			ServiceContextThreadLocal.pushServiceContext(serviceContext);

			List<DDMFormRule> ddmFormRules = getDDMFormRules(portletRequest);

			ddmForm.setDDMFormRules(ddmFormRules);

			return ddmForm;
		}
		catch (PortalException pe) {
			throw new StructureDefinitionException(pe);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	protected DDMFormLayout getDDMFormLayout(PortletRequest portletRequest)
		throws PortalException {

		try {
			String layout = ParamUtil.getString(portletRequest, "layout");

			return ddmFormLayoutJSONDeserializer.deserialize(layout);
		}
		catch (PortalException pe) {
			throw new StructureLayoutException(pe);
		}
	}

	protected List<DDMFormRule> getDDMFormRules(PortletRequest portletRequest)
		throws PortalException {

		String rules = ParamUtil.getString(portletRequest, "rules");

		if (Validator.isNull(rules) || Objects.equals("[]", rules)) {
			return Collections.emptyList();
		}

		List<DDLFormRule> ddlFormRules = ddlFormRuleDeserializer.deserialize(
			rules);

		return ddlFormRulesToDDMFormRulesConverter.convert(ddlFormRules);
	}

	protected Map<Locale, String> getLocalizedMap(Locale locale, String value) {
		Map<Locale, String> localizedMap = new HashMap<>();

		localizedMap.put(locale, value);

		return localizedMap;
	}

	protected DDMFormValues getSettingsDDMFormValues(
			PortletRequest portletRequest)
		throws PortalException {

		String serializedSettingsDDMFormValues = ParamUtil.getString(
			portletRequest, "serializedSettingsDDMFormValues");

		DDMForm ddmForm = DDMFormFactory.create(DDLRecordSetSettings.class);

		DDMFormValues settingsDDMFormValues =
			ddmFormValuesJSONDeserializer.deserialize(
				ddmForm, serializedSettingsDDMFormValues);

		return settingsDDMFormValues;
	}

	protected String getStorageType(DDMFormValues ddmFormValues)
		throws PortalException {

		DDMFormValuesQuery ddmFormValuesQuery =
			ddmFormValuesQueryFactory.create(ddmFormValues, "/storageType");

		DDMFormFieldValue ddmFormFieldValue =
			ddmFormValuesQuery.selectSingleDDMFormFieldValue();

		Value value = ddmFormFieldValue.getValue();

		String storageType = value.getString(ddmFormValues.getDefaultLocale());

		if (Validator.isNull(storageType)) {
			storageType = StorageType.JSON.toString();
		}

		return storageType;
	}

	protected String getWorkflowDefinition(DDMFormValues ddmFormValues)
		throws PortalException {

		DDMFormValuesQuery ddmFormValuesQuery =
			ddmFormValuesQueryFactory.create(
				ddmFormValues, "/workflowDefinition");

		DDMFormFieldValue ddmFormFieldValue =
			ddmFormValuesQuery.selectSingleDDMFormFieldValue();

		Value value = ddmFormFieldValue.getValue();

		return value.getString(ddmFormValues.getDefaultLocale());
	}

	protected DDMStructure updateDDMStructure(PortletRequest portletRequest)
		throws Exception {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDMStructure.class.getName(), portletRequest);

		long ddmStructureId = ParamUtil.getLong(
			portletRequest, "ddmStructureId");
		String name = ParamUtil.getString(portletRequest, "name");
		String description = ParamUtil.getString(portletRequest, "description");
		DDMForm ddmForm = getDDMForm(portletRequest, serviceContext);
		DDMFormLayout ddmFormLayout = getDDMFormLayout(portletRequest);

		return ddmStructureService.updateStructure(
			ddmStructureId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
			getLocalizedMap(ddmForm.getDefaultLocale(), name),
			getLocalizedMap(ddmForm.getDefaultLocale(), description), ddmForm,
			ddmFormLayout, serviceContext);
	}

	protected DDLRecordSet updateRecordSet(
			PortletRequest portletRequest, long ddmStructureVersionId,
			DDMFormValues settingsDDMFormValues)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long recordSetId = ParamUtil.getLong(portletRequest, "recordSetId");

		String name = ParamUtil.getString(portletRequest, "name");
		String description = ParamUtil.getString(portletRequest, "description");

		validateRedirectURL(settingsDDMFormValues);

		String settings = ddmFormValuesJSONSerializer.serialize(
			settingsDDMFormValues);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDLRecordSet.class.getName(), portletRequest);

		DDLRecordSet ddlRecordSet = ddlRecordSetService.updateRecordSet(
			recordSetId, ddmStructureVersionId,
			getLocalizedMap(themeDisplay.getSiteDefaultLocale(), name),
			getLocalizedMap(themeDisplay.getSiteDefaultLocale(), description),
			DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT, settings,
			serviceContext);

		updateWorkflowDefinitionLink(
			portletRequest, ddlRecordSet, settingsDDMFormValues);

		return ddlRecordSet;
	}

	protected DDLRecordSet updateRecordSet(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		DDMStructure ddmStructure = updateDDMStructure(portletRequest);

		DDMStructureVersion ddmStructureVersion =
			ddmStructure.getLatestStructureVersion();

		DDMFormValues settingsDDMFormValues = getSettingsDDMFormValues(
			portletRequest);

		DDLRecordSet recordSet = updateRecordSet(
			portletRequest, ddmStructureVersion.getStructureVersionId(),
			settingsDDMFormValues);

		return recordSet;
	}

	protected void updateWorkflowDefinitionLink(
			PortletRequest portletRequest, DDLRecordSet recordSet,
			DDMFormValues ddmFormValues)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long groupId = ParamUtil.getLong(portletRequest, "groupId");

		String workflowDefinition = getWorkflowDefinition(ddmFormValues);

		if (workflowDefinition.equals("no-workflow")) {
			workflowDefinition = "";
		}

		workflowDefinitionLinkLocalService.updateWorkflowDefinitionLink(
			themeDisplay.getUserId(), themeDisplay.getCompanyId(), groupId,
			DDLRecordSet.class.getName(), recordSet.getRecordSetId(), 0,
			workflowDefinition);
	}

	protected void validateRedirectURL(DDMFormValues settingsDDMFormValues)
		throws PortalException {

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			settingsDDMFormValues.getDDMFormFieldValuesMap();

		if (!ddmFormFieldValuesMap.containsKey("redirectURL")) {
			return;
		}

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			"redirectURL");

		DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValues.get(0);

		Value value = ddmFormFieldValue.getValue();

		for (Locale availableLocale : value.getAvailableLocales()) {
			String valueString = value.getString(availableLocale);

			if (Validator.isNotNull(valueString)) {
				String escapedRedirect = _portal.escapeRedirect(valueString);

				if (Validator.isNull(escapedRedirect)) {
					throw new RecordSetSettingsRedirectURLException();
				}
			}
		}
	}

	@Reference
	protected DDLFormRuleDeserializer ddlFormRuleDeserializer;

	@Reference
	protected DDLFormRuleToDDMFormRuleConverter
		ddlFormRulesToDDMFormRulesConverter;

	@Reference
	protected DDLRecordSetService ddlRecordSetService;

	@Reference
	protected DDMFormJSONDeserializer ddmFormJSONDeserializer;

	@Reference
	protected DDMFormLayoutJSONDeserializer ddmFormLayoutJSONDeserializer;

	@Reference
	protected DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer;

	@Reference
	protected DDMFormValuesJSONSerializer ddmFormValuesJSONSerializer;

	@Reference
	protected DDMFormValuesQueryFactory ddmFormValuesQueryFactory;

	@Reference
	protected DDMStructureService ddmStructureService;

	@Reference
	protected volatile WorkflowDefinitionLinkLocalService
		workflowDefinitionLinkLocalService;

	@Reference
	private Portal _portal;

}