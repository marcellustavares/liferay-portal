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

package com.liferay.forms.web.portlet.display;

import java.util.List;
import java.util.Set;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.display.context.util.BaseRequestHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSetConstants;
import com.liferay.portlet.dynamicdatalists.search.RecordSetSearchTerms;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetServiceUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistryUtil;
import com.liferay.portlet.dynamicdatamapping.service.permission.DDMPermission;
import com.liferay.portlet.dynamicdatamapping.service.permission.DDMStructurePermission;
import com.liferay.portlet.dynamicdatamapping.storage.StorageType;
import com.liferay.portlet.dynamicdatamapping.util.DDMDisplay;
import com.liferay.portlet.dynamicdatamapping.util.DDMDisplayRegistryUtil;
public class FormsRequestHelper extends BaseRequestHelper {

	public FormsRequestHelper(HttpServletRequest request) {
		super(request);
	}

	public boolean canCopyStructure() {
		DDMDisplay ddmDisplay = getDDMDisplay();

		return DDMPermission.contains(getPermissionChecker(),
				getScopeGroupId(), ddmDisplay.getResourceName(),
				ddmDisplay.getAddStructureActionId());
	}

	public boolean canDeleteStructure(DDMStructure structure) {
		return DDMStructurePermission.contains(getPermissionChecker(),
			structure, PortletKeys.DYNAMIC_DATA_LISTS, ActionKeys.DELETE);
	}

	public boolean canEditStructure(DDMStructure structure) {
		return DDMStructurePermission.contains(getPermissionChecker(),
			structure, PortletKeys.DYNAMIC_DATA_LISTS, ActionKeys.UPDATE);
	}

	public boolean canViewManageTemplates(DDMStructure structure) {
		return DDMStructurePermission.contains(
			getPermissionChecker(), structure, PortletKeys.DYNAMIC_DATA_LISTS,
			ActionKeys.VIEW) && isShowManageTemplates();
	}

	public DDMDisplay getDDMDisplay() {
		return DDMDisplayRegistryUtil
			.getDDMDisplay(PortletKeys.DYNAMIC_DATA_LISTS);
	}

	public PortletURL getDDMStructureRowURL(RenderResponse renderResponse,
			DDMStructure ddmStructure) {

		PortletURL rowURL = renderResponse.createRenderURL();

		rowURL.setParameter("mvcPath", "/edit_structure.jsp");
		rowURL.setParameter("redirect", PortalUtil.getCurrentURL(getRequest()));
		rowURL.setParameter("classNameId",
			String.valueOf(PortalUtil.getClassNameId(DDMStructure.class)));
		rowURL.setParameter("classPK",
			String.valueOf(ddmStructure.getStructureId()));

		return rowURL;
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
		} else if (scopeStorageType.equals("json")) {
			storageTypeValue = StorageType.JSON.getValue();
		} else if (scopeStorageType.equals("xml")) {
			storageTypeValue = StorageType.XML.getValue();
		}

		return storageTypeValue;
	}

	public String getTemplateTypeValue(String scopeTemplateType) {
		String templateTypeValue = StringPool.BLANK;

		if (scopeTemplateType
				.equals(DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY)) {

			templateTypeValue = DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY;
		} else if (scopeTemplateType
				.equals(DDMTemplateConstants.TEMPLATE_TYPE_FORM)) {

			templateTypeValue = DDMTemplateConstants.TEMPLATE_TYPE_FORM;
		}

		return templateTypeValue;
	}

	public PortletURL getViewPortletURL(RenderResponse renderResponse,
			long groupId, String tabs1) {

		PortletURL viewPortletURL = renderResponse.createRenderURL();

		viewPortletURL.setParameter("struts_action",
			"/dynamic_data_mapping/view");
		viewPortletURL.setParameter("groupId", String.valueOf(groupId));
		viewPortletURL.setParameter("tabs1", tabs1);

		return viewPortletURL;
	}

	public boolean isShowAncestorScopes() {
		return ParamUtil.getBoolean(getRequest(), "showAncestorScopes");
	}

	public void populateSearchContainer(
			SearchContainer<DDLRecordSet> searchContainer)
		throws PortalException {

		Company company = getCompany();

		RecordSetSearchTerms searchTerms = (RecordSetSearchTerms)searchContainer
			.getSearchTerms();

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

	public boolean isShowManageTemplates() {
		return ParamUtil.getBoolean(getRequest(), "showManageTemplates", true);
	}
	
	public String[] getFormCategoryNames() {
		return _FORM_CATEGORY_NAMES;
	}
	
	public DDMFormLayout getDDMFormLayout(
			DDMStructure ddmStructure, String script)
		throws PortalException {

		DDMFormLayout ddmFormLayout = null;

		if (Validator.isNotNull(script)) {
			if (ddmStructure != null) {
				ddmFormLayout = ddmStructure.getDDMFormLayout();
			}
			else {
				ddmFormLayout = new DDMFormLayout();
			}
		}
		else {
			ddmFormLayout = new DDMFormLayout();
		}
		
		return ddmFormLayout;
	}
	
	private static final String[] _FORM_CATEGORY_NAMES = { "basic_info", "form_builder" };

}