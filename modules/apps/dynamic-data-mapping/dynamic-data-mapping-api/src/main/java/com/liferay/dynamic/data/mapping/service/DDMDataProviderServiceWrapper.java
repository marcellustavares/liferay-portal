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

package com.liferay.dynamic.data.mapping.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link DDMDataProviderService}.
 *
 * @author Brian Wing Shun Chan
 * @see DDMDataProviderService
 * @generated
 */
@ProviderType
public class DDMDataProviderServiceWrapper implements DDMDataProviderService,
	ServiceWrapper<DDMDataProviderService> {
	public DDMDataProviderServiceWrapper(
		DDMDataProviderService ddmDataProviderService) {
		_ddmDataProviderService = ddmDataProviderService;
	}

	@Override
	public com.liferay.dynamic.data.mapping.model.DDMDataProvider addDataProvider(
		long groupId,
		java.util.Map<java.util.Locale, java.lang.String> nameMap,
		java.util.Map<java.util.Locale, java.lang.String> descriptionMap,
		java.lang.String data, java.lang.String type,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmDataProviderService.addDataProvider(groupId, nameMap,
			descriptionMap, data, type, serviceContext);
	}

	@Override
	public void deleteDataProvider(long dataProviderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		_ddmDataProviderService.deleteDataProvider(dataProviderId);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public java.lang.String getOSGiServiceIdentifier() {
		return _ddmDataProviderService.getOSGiServiceIdentifier();
	}

	@Override
	public java.util.List<com.liferay.dynamic.data.mapping.model.DDMDataProvider> search(
		long companyId, long[] groupIds, java.lang.String keywords, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.dynamic.data.mapping.model.DDMDataProvider> orderByComparator) {
		return _ddmDataProviderService.search(companyId, groupIds, keywords,
			start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.dynamic.data.mapping.model.DDMDataProvider> search(
		long companyId, long[] groupIds, java.lang.String name,
		java.lang.String description, boolean andOperator, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.dynamic.data.mapping.model.DDMDataProvider> orderByComparator) {
		return _ddmDataProviderService.search(companyId, groupIds, name,
			description, andOperator, start, end, orderByComparator);
	}

	@Override
	public int searchCount(long companyId, long[] groupIds,
		java.lang.String keywords) {
		return _ddmDataProviderService.searchCount(companyId, groupIds, keywords);
	}

	@Override
	public int searchCount(long companyId, long[] groupIds,
		java.lang.String name, java.lang.String description, boolean andOperator) {
		return _ddmDataProviderService.searchCount(companyId, groupIds, name,
			description, andOperator);
	}

	@Override
	public com.liferay.dynamic.data.mapping.model.DDMDataProvider updateDataProvider(
		long dataProviderId,
		java.util.Map<java.util.Locale, java.lang.String> nameMap,
		java.util.Map<java.util.Locale, java.lang.String> descriptionMap,
		java.lang.String data,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmDataProviderService.updateDataProvider(dataProviderId,
			nameMap, descriptionMap, data, serviceContext);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public DDMDataProviderService getWrappedDDMDataProviderService() {
		return _ddmDataProviderService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedDDMDataProviderService(
		DDMDataProviderService ddmDataProviderService) {
		_ddmDataProviderService = ddmDataProviderService;
	}

	@Override
	public DDMDataProviderService getWrappedService() {
		return _ddmDataProviderService;
	}

	@Override
	public void setWrappedService(DDMDataProviderService ddmDataProviderService) {
		_ddmDataProviderService = ddmDataProviderService;
	}

	private DDMDataProviderService _ddmDataProviderService;
}