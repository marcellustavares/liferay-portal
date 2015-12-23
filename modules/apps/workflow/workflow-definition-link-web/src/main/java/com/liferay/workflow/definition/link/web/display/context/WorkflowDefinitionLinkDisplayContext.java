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

package com.liferay.workflow.definition.link.web.display.context;

import com.liferay.portal.NoSuchWorkflowDefinitionLinkException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowDefinitionManagerUtil;
import com.liferay.portal.kernel.workflow.WorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.kernel.workflow.comparator.WorkflowComparatorFactoryUtil;
import com.liferay.portal.model.WorkflowDefinitionLink;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.service.WorkflowDefinitionLinkLocalService;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.workflow.definition.link.web.portlet.constants.WorkflowDefinitionLinkPortletKeys;
import com.liferay.workflow.definition.link.web.search.WorkflowDefinitionLinkSearchEntry;
import com.liferay.workflow.definition.link.web.search.WorkflowDefinitionLinkSearchTerms;
import com.liferay.workflow.definition.link.web.util.WorkflowDefinitionLinkPortletUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.portlet.RenderRequest;

/**
 * @author Leonardo Barros
 */
public class WorkflowDefinitionLinkDisplayContext {

	public WorkflowDefinitionLinkDisplayContext(
			RenderRequest renderRequest,
			WorkflowDefinitionLinkLocalService
				workflowDefinitionLinkLocalService)
		throws PortalException {

		_workflowDefinitionLinkLocalService =
			workflowDefinitionLinkLocalService;

		_workflowDefinitionLinkRequestHelper =
			new WorkflowDefinitionLinkRequestHelper(renderRequest);

		if (calledFromControlPanel()) {
			_groupId = WorkflowConstants.DEFAULT_GROUP_ID;
		}
		else {
			ThemeDisplay themeDisplay =
				_workflowDefinitionLinkRequestHelper.getThemeDisplay();

			_groupId = themeDisplay.getSiteGroupIdOrLiveGroupId();
		}
	}

	public String getDefaultWorkflowDefinitionLink(
			String workflowDefinitionName)
		throws PortalException {

		String defaultWorkflowDefinitionLinkOption = StringPool.BLANK;

		if (!calledFromControlPanel()) {
			try {
				WorkflowDefinitionLink defaultWorkflowDefinitionLink =
					_workflowDefinitionLinkLocalService.
						getDefaultWorkflowDefinitionLink(
							_workflowDefinitionLinkRequestHelper.getCompanyId(),
							workflowDefinitionName, 0, 0);

				defaultWorkflowDefinitionLinkOption =
					defaultWorkflowDefinitionLink.getWorkflowDefinitionName();
			}
			catch (NoSuchWorkflowDefinitionLinkException nswdle) {
				defaultWorkflowDefinitionLinkOption = LanguageUtil.get(
					_workflowDefinitionLinkRequestHelper.getRequest(),
					"no-workflow");
			}
		}
		else {
			defaultWorkflowDefinitionLinkOption = LanguageUtil.get(
				_workflowDefinitionLinkRequestHelper.getRequest(),
				"no-workflow");
		}

		return defaultWorkflowDefinitionLinkOption;
	}

	public long getGroupId() {
		return _groupId;
	}

	public String getLabel(WorkflowDefinition workflowDefinition) {
		return HtmlUtil.escape(workflowDefinition.getName()) + " (" +
			LanguageUtil.format(
				_workflowDefinitionLinkRequestHelper.getLocale(), "version-x",
				workflowDefinition.getVersion(), false) +
			")";
	}

	public List<WorkflowDefinitionLinkSearchEntry> getSearchContainerResults(
			SearchContainer<WorkflowDefinitionLinkSearchEntry> searchContainer)
		throws PortalException {

		WorkflowDefinitionLinkSearchTerms searchTerms =
			(WorkflowDefinitionLinkSearchTerms)searchContainer.getSearchTerms();

		List<WorkflowDefinitionLinkSearchEntry>
			workflowDefinitionLinkSearchEntries =
				createWorkflowDefinitionLinkSearchEntryList();

		if (searchTerms.isAdvancedSearch()) {
			filter(workflowDefinitionLinkSearchEntries, searchTerms);
		}
		else {
			filter(
				workflowDefinitionLinkSearchEntries,
				StringUtil.toLowerCase(searchTerms.getKeywords()));
		}

		searchContainer.setTotal(workflowDefinitionLinkSearchEntries.size());

		sort(workflowDefinitionLinkSearchEntries);

		return workflowDefinitionLinkSearchEntries;
	}

	public String getValue(WorkflowDefinition workflowDefinition) {
		return HtmlUtil.escapeAttribute(
			workflowDefinition.getName()) + StringPool.AT +
			workflowDefinition.getVersion();
	}

	public List<WorkflowDefinition> getWorkflowDefinitions()
		throws PortalException {

		return WorkflowDefinitionManagerUtil.getActiveWorkflowDefinitions(
			_workflowDefinitionLinkRequestHelper.getCompanyId(),
			QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			WorkflowComparatorFactoryUtil.getDefinitionNameComparator(true));
	}

	public boolean isSelected(
			WorkflowDefinition workflowDefinition,
			String workflowDefinitionName)
		throws PortalException {

		WorkflowDefinitionLink workflowDefinitionLink =
			getWorkflowDefinitionLink(workflowDefinitionName);

		if ((workflowDefinitionLink != null) &&
			workflowDefinitionLink.getWorkflowDefinitionName().equals(
				workflowDefinition.getName()) &&
			(workflowDefinitionLink.getWorkflowDefinitionVersion() ==
				workflowDefinition.getVersion())) {

			return true;
		}

		return false;
	}

	protected boolean calledFromControlPanel() {
		return getPortletName().equals(
			WorkflowDefinitionLinkPortletKeys.
				WORKFLOW_DEFINITION_LINK_CONTROL_PANEL);
	}

	protected
		List<WorkflowDefinitionLinkSearchEntry>
			createWorkflowDefinitionLinkSearchEntryList()
				throws PortalException {

		List<WorkflowDefinitionLinkSearchEntry>
			workflowDefinitionLinkSearchEntries = new ArrayList<>();

		List<WorkflowHandler<?>> workflowHandlers = getWorkflowHandlers();

		Locale locale = _workflowDefinitionLinkRequestHelper.getLocale();

		for (WorkflowHandler<?> workflowHandler : workflowHandlers) {
			String resource = ResourceActionsUtil.getModelResource(
				locale, workflowHandler.getClassName());

			String workflowDefinitionName = getWorkflowDefinitionName(
				workflowHandler);

			WorkflowDefinitionLinkSearchEntry
				workflowDefinitionLinkSearchEntry =
					new WorkflowDefinitionLinkSearchEntry(
						workflowHandler.getClassName(), resource,
						workflowDefinitionName);

			workflowDefinitionLinkSearchEntries.add(
				workflowDefinitionLinkSearchEntry);
		}

		return workflowDefinitionLinkSearchEntries;
	}

	protected void filter(
		List<WorkflowDefinitionLinkSearchEntry>
			workflowDefinitionLinkSearchEntries, String keyword) {

		Iterator<WorkflowDefinitionLinkSearchEntry> iterator =
			workflowDefinitionLinkSearchEntries.iterator();

		while (iterator.hasNext()) {
			WorkflowDefinitionLinkSearchEntry entry = iterator.next();

			String resource = StringUtil.toLowerCase(entry.getResource());
			String workflowDefinitionName = StringUtil.toLowerCase(
				entry.getWorkflowDefinitionName());

			if (!resource.contains(keyword) &&
				!workflowDefinitionName.contains(keyword)) {

				iterator.remove();
			}
		}
	}

	protected void filter(
		List<WorkflowDefinitionLinkSearchEntry>
			workflowDefinitionLinkSearchEntries,
		WorkflowDefinitionLinkSearchTerms searchTerms) {

		if (Validator.isNull(searchTerms.getResource()) &&
			Validator.isNull(searchTerms.getWorkflow())) {

			return;
		}

		Iterator<WorkflowDefinitionLinkSearchEntry> iterator =
			workflowDefinitionLinkSearchEntries.iterator();

		while (iterator.hasNext()) {
			WorkflowDefinitionLinkSearchEntry entry = iterator.next();

			String resource = StringUtil.toLowerCase(entry.getResource());
			String workflowDefinitionName = StringUtil.toLowerCase(
				entry.getWorkflowDefinitionName());

			if (Validator.isNotNull(searchTerms.getResource()) &&
				Validator.isNotNull(searchTerms.getWorkflow())) {

				if (searchTerms.isAndOperator()) {
					if (!resource.contains(searchTerms.getResource()) ||
						!workflowDefinitionName.contains(
							searchTerms.getWorkflow())) {

						iterator.remove();
					}
				}
				else {
					if (!resource.contains(searchTerms.getResource()) &&
						!workflowDefinitionName.contains(
							searchTerms.getWorkflow())) {

						iterator.remove();
					}
				}
			}
			else if(Validator.isNotNull(searchTerms.getResource())) {
				if (!resource.contains(searchTerms.getResource())) {
					iterator.remove();
				}
			}
			else if(Validator.isNotNull(searchTerms.getWorkflow())) {
				if (!workflowDefinitionName.contains(
						searchTerms.getWorkflow())) {

					iterator.remove();
				}
			}
		}
	}

	protected String getPortletName() {
		ThemeDisplay themeDisplay =
			_workflowDefinitionLinkRequestHelper.getThemeDisplay();

		return themeDisplay.getPortletDisplay().getPortletName();
	}

	protected WorkflowDefinitionLink getWorkflowDefinitionLink(
			String workflowDefinitionName)
		throws PortalException {

		WorkflowDefinitionLink workflowDefinitionLink = null;

		try {
			if (calledFromControlPanel()) {
				workflowDefinitionLink =
					_workflowDefinitionLinkLocalService.
						getDefaultWorkflowDefinitionLink(
							_workflowDefinitionLinkRequestHelper.getCompanyId(),
							workflowDefinitionName, 0, 0);
			}
			else {
				workflowDefinitionLink =
					_workflowDefinitionLinkLocalService.
						getWorkflowDefinitionLink(
							_workflowDefinitionLinkRequestHelper.getCompanyId(),
							_groupId, workflowDefinitionName, 0, 0, true);
			}
		}
		catch (NoSuchWorkflowDefinitionLinkException nswdle) {
		}

		return workflowDefinitionLink;
	}

	protected String getWorkflowDefinitionName(
			WorkflowHandler<?> workflowHandler)
		throws PortalException {

		WorkflowDefinitionLink workflowDefinitionLink = null;

		try {
			if (calledFromControlPanel()) {
				workflowDefinitionLink =
					_workflowDefinitionLinkLocalService.
						getDefaultWorkflowDefinitionLink(
							_workflowDefinitionLinkRequestHelper.getCompanyId(),
							workflowHandler.getClassName(), 0, 0);
			}
			else {
				workflowDefinitionLink =
					_workflowDefinitionLinkLocalService.
						getWorkflowDefinitionLink(
							_workflowDefinitionLinkRequestHelper.getCompanyId(),
							_groupId, workflowHandler.getClassName(), 0, 0,
							true);
			}
		}
		catch (NoSuchWorkflowDefinitionLinkException nswdle) {
		}

		String workflowDefinitionName = getDefaultWorkflowDefinitionLink(
			workflowHandler.getClassName());

		List<WorkflowDefinition> workflowDefinitions = getWorkflowDefinitions();

		for (WorkflowDefinition workflowDefinition : workflowDefinitions) {
			if ((workflowDefinitionLink != null) &&
				workflowDefinitionLink.getWorkflowDefinitionName().equals(
					workflowDefinition.getName()) &&
				(workflowDefinitionLink.getWorkflowDefinitionVersion() ==
					workflowDefinition.getVersion())) {

				workflowDefinitionName = HtmlUtil.escape(
					workflowDefinition.getName()) + " (" + LanguageUtil.format(
						_workflowDefinitionLinkRequestHelper.getLocale(),
						"version-x", workflowDefinition.getVersion(), false) +
						")";
				break;
			}
		}

		return workflowDefinitionName;
	}

	protected List<WorkflowHandler<?>> getWorkflowHandlers() {
		List<WorkflowHandler<?>> workflowHandlers = null;

		if (getPortletName().equals(
				WorkflowDefinitionLinkPortletKeys.
					WORKFLOW_DEFINITION_LINK_CONTROL_PANEL)) {

			workflowHandlers =
				WorkflowHandlerRegistryUtil.getWorkflowHandlers();
		}
		else {
			workflowHandlers =
				WorkflowHandlerRegistryUtil.getScopeableWorkflowHandlers();
		}

		Iterator<WorkflowHandler<?>> itr = workflowHandlers.iterator();

		while (itr.hasNext()) {
			WorkflowHandler<?> workflowHandler = itr.next();

			if (!workflowHandler.isVisible()) {
				itr.remove();
			}
		}

		return workflowHandlers;
	}

	protected void sort(
		List<WorkflowDefinitionLinkSearchEntry>
			workflowDefinitionLinkSearchEntries) {

		String orderByCol = ParamUtil.getString(
			_workflowDefinitionLinkRequestHelper.getRequest(), "orderByCol",
			"resource");

		String orderByType = ParamUtil.getString(
			_workflowDefinitionLinkRequestHelper.getRequest(), "orderByType",
			"asc");

		Comparator<WorkflowDefinitionLinkSearchEntry> orderByComparator =
			WorkflowDefinitionLinkPortletUtil.
				getWorkflowDefinitionLinkOrderByComparator(
					orderByCol, orderByType);

		Collections.sort(
			workflowDefinitionLinkSearchEntries, orderByComparator);
	}

	private final long _groupId;
	private final WorkflowDefinitionLinkLocalService
		_workflowDefinitionLinkLocalService;
	private final WorkflowDefinitionLinkRequestHelper
		_workflowDefinitionLinkRequestHelper;

}