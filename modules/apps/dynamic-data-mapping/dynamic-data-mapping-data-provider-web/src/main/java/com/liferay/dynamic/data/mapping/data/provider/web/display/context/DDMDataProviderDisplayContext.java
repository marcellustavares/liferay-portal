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

package com.liferay.dynamic.data.mapping.data.provider.web.display.context;

import com.liferay.dynamic.data.mapping.constants.DDMActionKeys;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderSettings;
import com.liferay.dynamic.data.mapping.data.provider.web.display.context.util.DDMDataProviderRequestHelper;
import com.liferay.dynamic.data.mapping.data.provider.web.search.DDMDataProviderSearchTerms;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.model.DDMDataProvider;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.registry.DDMFormFactory;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderLocalService;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderService;
import com.liferay.dynamic.data.mapping.service.permission.DDMDataProviderPermission;
import com.liferay.dynamic.data.mapping.service.permission.DDMPermission;
import com.liferay.osgi.service.tracker.map.ServiceTrackerCustomizerFactory.ServiceWrapper;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMap;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.util.PortalUtil;

import java.util.List;
import java.util.Set;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Leonardo Barros
 */
public class DDMDataProviderDisplayContext {

	public DDMDataProviderDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		DDMDataProviderService ddmDataProviderService,
		DDMDataProviderLocalService ddmDataProviderLocalService,
		DDMFormRenderer ddmFormRenderer,
		ServiceTrackerMap<String, ServiceWrapper<DDMDataProviderSettings>>
			ddmDataProvidersMap) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_ddmDataProviderService = ddmDataProviderService;
		_ddmDataProviderLocalService = ddmDataProviderLocalService;
		_ddmFormRenderer = ddmFormRenderer;
		_ddmDataProvidersMap = ddmDataProvidersMap;

		_ddmDataProviderRequestHelper = new DDMDataProviderRequestHelper(
			renderRequest);
	}

	public DDMDataProvider getDataProvider() throws PortalException {
		if (_ddmDataProvider != null) {
			return _ddmDataProvider;
		}

		long dataProviderId = ParamUtil.getLong(
			_renderRequest, "dataProviderId");

		_ddmDataProvider = _ddmDataProviderLocalService.fetchDDMDataProvider(
			dataProviderId);

		return _ddmDataProvider;
	}

	public String getDataProviderDefinition() throws PortalException {
		if (_ddmDataProvider != null) {
			return _ddmDataProvider.getDefinition();
		}

		String dataProviderType = ParamUtil.getString(
			_renderRequest, "dataProviderType");

		DDMDataProviderSettings ddmDataProviderSettings =
			_ddmDataProvidersMap.getService(dataProviderType).getService();

		Class<?> clazz = ddmDataProviderSettings.getSettings();

		DDMForm ddmForm = DDMFormFactory.create(clazz);

		DDMFormRenderingContext ddmFormRenderingContext =
			createDDMFormRenderingContext();

		return _ddmFormRenderer.render(ddmForm, ddmFormRenderingContext);
	}

	public Set<String> getDataProviderTypes() {
		return _ddmDataProvidersMap.keySet();
	}

	public PortletURL getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/view.jsp");
		portletURL.setParameter(
			"groupId",
			String.valueOf(_ddmDataProviderRequestHelper.getScopeGroupId()));

		return portletURL;
	}

	public List<DDMDataProvider> getSearchContainerResults(
			SearchContainer<DDMDataProvider> searchContainer)
		throws PortalException {

		DDMDataProviderSearchTerms searchTerms =
			(DDMDataProviderSearchTerms)searchContainer.getSearchTerms();

		if (searchTerms.isAdvancedSearch()) {
			return _ddmDataProviderService.search(
				_ddmDataProviderRequestHelper.getCompanyId(),
				new long[] {_ddmDataProviderRequestHelper.getScopeGroupId()},
				searchTerms.getName(), searchTerms.getDescription(),
				searchTerms.isAndOperator(), searchContainer.getStart(),
				searchContainer.getEnd(),
				searchContainer.getOrderByComparator());
		}
		else {
			return _ddmDataProviderService.search(
				_ddmDataProviderRequestHelper.getCompanyId(),
				new long[] {_ddmDataProviderRequestHelper.getScopeGroupId()},
				searchTerms.getKeywords(), searchContainer.getStart(),
				searchContainer.getEnd(),
				searchContainer.getOrderByComparator());
		}
	}

	public int getSearchContainerTotal(
			SearchContainer<DDMDataProvider> searchContainer)
		throws PortalException {

		DDMDataProviderSearchTerms searchTerms =
			(DDMDataProviderSearchTerms)searchContainer.getSearchTerms();

		if (searchTerms.isAdvancedSearch()) {
			return _ddmDataProviderService.searchCount(
				_ddmDataProviderRequestHelper.getCompanyId(),
				new long[] {_ddmDataProviderRequestHelper.getScopeGroupId()},
				searchTerms.getName(), searchTerms.getDescription(),
				searchTerms.isAndOperator());
		}
		else {
			return _ddmDataProviderService.searchCount(
				_ddmDataProviderRequestHelper.getCompanyId(),
				new long[] {_ddmDataProviderRequestHelper.getScopeGroupId()},
				searchTerms.getKeywords());
		}
	}

	public boolean isShowAddDataProviderButton() {
		return DDMPermission.contains(
			_ddmDataProviderRequestHelper.getPermissionChecker(),
			_ddmDataProviderRequestHelper.getScopeGroupId(),
			DDMActionKeys.ADD_DATA_PROVIDER, DDMDataProvider.class.getName());
	}

	public boolean isShowDeleteDataProviderIcon(long dataProviderId)
		throws PortalException {

		return DDMDataProviderPermission.contains(
			_ddmDataProviderRequestHelper.getPermissionChecker(),
			dataProviderId, ActionKeys.DELETE);
	}

	protected DDMFormRenderingContext createDDMFormRenderingContext() {
		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			PortalUtil.getHttpServletRequest(_renderRequest));
		ddmFormRenderingContext.setHttpServletResponse(
			PortalUtil.getHttpServletResponse(_renderResponse));
		ddmFormRenderingContext.setLocale(
			_ddmDataProviderRequestHelper.getLocale());
		ddmFormRenderingContext.setPortletNamespace(
			_renderResponse.getNamespace());

		return ddmFormRenderingContext;
	}

	private DDMDataProvider _ddmDataProvider;
	private final DDMDataProviderLocalService _ddmDataProviderLocalService;
	private final DDMDataProviderRequestHelper _ddmDataProviderRequestHelper;
	private final DDMDataProviderService _ddmDataProviderService;
	private final
		ServiceTrackerMap<String, ServiceWrapper<DDMDataProviderSettings>>
			_ddmDataProvidersMap;
	private final DDMFormRenderer _ddmFormRenderer;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;

}