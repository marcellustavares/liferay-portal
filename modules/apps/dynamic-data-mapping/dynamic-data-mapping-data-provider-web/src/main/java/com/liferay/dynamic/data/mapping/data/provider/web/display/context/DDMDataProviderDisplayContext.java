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

import com.liferay.dynamic.data.mapping.data.provider.web.display.context.util.DDMDataProviderRequestHelper;
import com.liferay.dynamic.data.mapping.data.provider.web.search.DDMDataProviderSearchTerms;
import com.liferay.dynamic.data.mapping.model.DDMDataProvider;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderService;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Leonardo Barros
 */
public class DDMDataProviderDisplayContext {

	public DDMDataProviderDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		DDMDataProviderService ddmDataProviderService) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_ddmDataProviderService = ddmDataProviderService;

		_ddmDataProviderRequestHelper = new DDMDataProviderRequestHelper(
			renderRequest);
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

	private final DDMDataProviderRequestHelper _ddmDataProviderRequestHelper;
	private final DDMDataProviderService _ddmDataProviderService;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;

}