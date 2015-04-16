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

package com.liferay.dynamic.data.lists.web.display;

import com.liferay.dynamic.data.mapping.form.renderer.DDMFormFieldTypesJSONSerializer;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.display.context.util.BaseRequestHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Company;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.dynamicdatalists.NoSuchRecordSetException;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSetConstants;
import com.liferay.portlet.dynamicdatalists.search.RecordSetSearchTerms;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordLocalServiceUtil;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetServiceUtil;
import com.liferay.portlet.dynamicdatalists.service.permission.DDLRecordPermission;
import com.liferay.portlet.dynamicdatalists.service.permission.DDLRecordSetPermission;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormLayoutJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldValueRenderer;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldValueRendererRegistryUtil;
import com.liferay.portlet.dynamicdatamapping.service.permission.DDMPermission;
import com.liferay.portlet.dynamicdatamapping.service.permission.DDMStructurePermission;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.StorageType;
import com.liferay.portlet.dynamicdatamapping.util.DDMDisplay;
import com.liferay.portlet.dynamicdatamapping.util.DDMDisplayRegistryUtil;
import com.liferay.portlet.dynamicdatamapping.util.DDMPermissionHandler;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
public class FormsRequestHelper extends BaseRequestHelper {

	public FormsRequestHelper(HttpServletRequest request) {
		super(request);
	}

	public boolean canCopyStructure() {
		DDMPermissionHandler ddmPermissionHandler =
			getDDMDisplay().getDDMPermissionHandler();

		return DDMPermission.contains(
			getPermissionChecker(), getScopeGroupId(),
			ddmPermissionHandler.getResourceName(0),
			ddmPermissionHandler.getAddStructureActionId());
	}

	public boolean canDeleteRecord(DDLRecord ddlRecord) throws PortalException {
		return DDLRecordPermission.contains(
			getPermissionChecker(), ddlRecord, ActionKeys.DELETE);
	}

	public boolean canDeleteStructure(DDMStructure structure) {
		return DDMStructurePermission.contains(
			getPermissionChecker(), structure, PortletKeys.DYNAMIC_DATA_LISTS,
			ActionKeys.DELETE);
	}

	public boolean canEditRecord(DDLRecord ddlRecord) throws PortalException {
		return DDLRecordPermission.contains(
			getPermissionChecker(), ddlRecord, ActionKeys.UPDATE);
	}

	public boolean canEditStructure(DDMStructure structure) {
		return DDMStructurePermission.contains(
			getPermissionChecker(), structure, PortletKeys.DYNAMIC_DATA_LISTS,
			ActionKeys.UPDATE);
	}

	public boolean canViewManageTemplates(DDMStructure structure) {
		return DDMStructurePermission.contains(
			getPermissionChecker(), structure, PortletKeys.DYNAMIC_DATA_LISTS,
			ActionKeys.VIEW) && isShowManageTemplates();
	}

	public DDLRecordSet getDDLRecordSet() throws PortalException {
		long recordSetId = ParamUtil.getLong(getRequest(), "recordSetId");

		DDLRecordSet ddlRecordSet = null;

		try {
			ddlRecordSet = DDLRecordSetServiceUtil.getRecordSet(recordSetId);
		}
		catch (NoSuchRecordSetException nsrse) {}

		return ddlRecordSet;
	}

	public DDMDisplay getDDMDisplay() {
		return DDMDisplayRegistryUtil.getDDMDisplay(
			PortletKeys.DYNAMIC_DATA_LISTS);
	}

	public DDMForm getDDMForm(DDMStructure ddmStructure)
		throws PortalException {

		DDMForm ddmForm = null;

		String definition = ParamUtil.getString(getRequest(), "definition");

		if (Validator.isNotNull(definition)) {
			ddmForm = DDMFormJSONDeserializerUtil.deserialize(definition);
		}
		else {
			if (ddmStructure != null) {
				ddmForm = ddmStructure.getDDMForm();
			}
			else {
				ddmForm = new DDMForm();
			}
		}

		return ddmForm;
	}

	public String getDDMFormFieldRenderedValue(
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap,
		DDMFormField ddmFormField) {

		String value = StringPool.BLANK;

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			ddmFormField.getName());

		if (ddmFormFieldValues != null) {
			DDMFormFieldValueRenderer ddmFormFieldValueRenderer =
				DDMFormFieldValueRendererRegistryUtil.getDDMFormFieldValueRenderer(
					ddmFormField.getType());

			value = ddmFormFieldValueRenderer.render(
				ddmFormFieldValues, getLocale());
		}

		return value;
	}

	public JSONArray getDDMFormFieldTypesJSONArray() {
		return _ddmFormFieldTypesJSONSerializer.serialize();
	}

	public DDMFormLayout getDDMFormLayout(DDMStructure ddmStructure)
		throws PortalException {

		DDMFormLayout ddmFormLayout = null;

		String layout = ParamUtil.getString(getRequest(), "layout");

		if (Validator.isNotNull(layout)) {
			ddmFormLayout = DDMFormLayoutJSONDeserializerUtil.deserialize(
				layout);
		}
		else {
			if (ddmStructure != null) {
				ddmFormLayout = ddmStructure.getDDMFormLayout();
			}
			else {
				ddmFormLayout = new DDMFormLayout();
			}
		}

		return ddmFormLayout;
	}

	public PortletURL getDDMStructureRowURL(
		RenderResponse renderResponse, DDMStructure ddmStructure) {

		PortletURL rowURL = renderResponse.createRenderURL();

		rowURL.setParameter("mvcPath", "/edit_structure.jsp");
		rowURL.setParameter("redirect", PortalUtil.getCurrentURL(getRequest()));
		rowURL.setParameter(
			"classNameId",
			String.valueOf(PortalUtil.getClassNameId(DDMStructure.class)));
		rowURL.setParameter(
			"classPK", String.valueOf(ddmStructure.getStructureId()));

		return rowURL;
	}

	public String[] getFormCategoryNames() {
		return _FORM_CATEGORY_NAMES;
	}

	public long getScopeClassNameId() {
		return PortalUtil.getClassNameId(getDDMDisplay().getStructureType());
	}

	public String getStorageTypeValue() {
		DDMDisplay ddmDisplay = getDDMDisplay();

		String scopeStorageType = ddmDisplay.getStorageType();
		String storageTypeValue = StringPool.BLANK;

		if (scopeStorageType.equals("expando")) {
			storageTypeValue = StorageType.EXPANDO.getValue();
		}
		else if (scopeStorageType.equals("json")) {
			storageTypeValue = StorageType.JSON.getValue();
		}
		else if (scopeStorageType.equals("xml")) {
			storageTypeValue = StorageType.XML.getValue();
		}

		return storageTypeValue;
	}

	public String getTemplateTypeValue(String scopeTemplateType) {
		String templateTypeValue = StringPool.BLANK;

		if (scopeTemplateType.equals(
				DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY)) {

			templateTypeValue = DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY;
		}
		else if (scopeTemplateType.equals(
					DDMTemplateConstants.TEMPLATE_TYPE_FORM)) {

			templateTypeValue = DDMTemplateConstants.TEMPLATE_TYPE_FORM;
		}

		return templateTypeValue;
	}

	public PortletURL getViewPortletURL(
		RenderResponse renderResponse, long groupId, String tabs1) {

		PortletURL viewPortletURL = renderResponse.createRenderURL();

		viewPortletURL.setParameter(
			"struts_action", "/dynamic_data_mapping/view");
		viewPortletURL.setParameter("groupId", String.valueOf(groupId));
		viewPortletURL.setParameter("tabs1", tabs1);

		return viewPortletURL;
	}

	public boolean isShowAncestorScopes() {
		return ParamUtil.getBoolean(getRequest(), "showAncestorScopes");
	}

	public boolean isShowManageTemplates() {
		return ParamUtil.getBoolean(getRequest(), "showManageTemplates", true);
	}

	public void populateRecordSearchContainer(
		SearchContainer<DDLRecord> searchContainer, DDLRecordSet recordSet) {

		DisplayTerms displayTerms = searchContainer.getDisplayTerms();

		List<DDLRecord> results = searchContainer.getResults();

		int total = searchContainer.getTotal();

		int status = WorkflowConstants.STATUS_APPROVED;

		if (DDLRecordSetPermission.contains(
				getPermissionChecker(), recordSet, ActionKeys.ADD_RECORD)) {

			status = WorkflowConstants.STATUS_ANY;
		}

		if (Validator.isNull(displayTerms.getKeywords())) {
			total = DDLRecordLocalServiceUtil.getRecordsCount(
				recordSet.getRecordSetId(), status);

			searchContainer.setTotal(total);

			results = DDLRecordLocalServiceUtil.getRecords(
				recordSet.getRecordSetId(), status, searchContainer.getStart(),
				searchContainer.getEnd(),
				searchContainer.getOrderByComparator());
		}
		else {
			SearchContext searchContext = SearchContextFactory.getInstance(
				getRequest());

			searchContext.setAttribute(
				"recordSetId", recordSet.getRecordSetId());
			searchContext.setAttribute(Field.STATUS, status);
			searchContext.setEnd(searchContainer.getEnd());
			searchContext.setKeywords(displayTerms.getKeywords());
			searchContext.setStart(searchContainer.getStart());

			BaseModelSearchResult<DDLRecord> baseModelSearchResult =
				DDLRecordLocalServiceUtil.searchDDLRecords(searchContext);

			results = baseModelSearchResult.getBaseModels();

			total = baseModelSearchResult.getLength();

			searchContainer.setTotal(total);
		}

		searchContainer.setResults(results);
	}

	public void populateRecordSetSearchContainer(
			SearchContainer<DDLRecordSet> searchContainer)
		throws PortalException {

		Company company = getCompany();

		RecordSetSearchTerms searchTerms =
			(RecordSetSearchTerms)searchContainer.getSearchTerms();

		List<DDLRecordSet> results = searchContainer.getResults();

		int total = searchContainer.getTotal();

		if (searchTerms.isAdvancedSearch()) {
			total = DDLRecordSetServiceUtil.searchCount(
				company.getCompanyId(), getScopeGroupId(),
				searchTerms.getName(), searchTerms.getDescription(),
				DDLRecordSetConstants.SCOPE_DYNAMIC_DATA_LISTS,
				searchTerms.isAndOperator());

			searchContainer.setTotal(total);

			results = DDLRecordSetServiceUtil.search(
				company.getCompanyId(), getScopeGroupId(),
				searchTerms.getName(), searchTerms.getDescription(),
				DDLRecordSetConstants.SCOPE_DYNAMIC_DATA_LISTS,
				searchTerms.isAndOperator(), searchContainer.getStart(),
				searchContainer.getEnd(),
				searchContainer.getOrderByComparator());
		}
		else {
			total = DDLRecordSetServiceUtil.searchCount(
				company.getCompanyId(), getScopeGroupId(),
				searchTerms.getKeywords(),
				DDLRecordSetConstants.SCOPE_DYNAMIC_DATA_LISTS);

			searchContainer.setTotal(total);

			results = DDLRecordSetServiceUtil.search(
				company.getCompanyId(), getScopeGroupId(),
				searchTerms.getKeywords(),
				DDLRecordSetConstants.SCOPE_DYNAMIC_DATA_LISTS,
				searchContainer.getStart(), searchContainer.getEnd(),
				searchContainer.getOrderByComparator());
		}

		searchContainer.setResults(results);
	}

	public void setDDMFormFieldTypesJSONSerializer(
		DDMFormFieldTypesJSONSerializer ddmFormFieldTypesJSONSerializer) {

		_ddmFormFieldTypesJSONSerializer = ddmFormFieldTypesJSONSerializer;
	}

	private static final String[] _FORM_CATEGORY_NAMES =
		{ "basic_info", "form_builder" };

	private DDMFormFieldTypesJSONSerializer _ddmFormFieldTypesJSONSerializer;

}