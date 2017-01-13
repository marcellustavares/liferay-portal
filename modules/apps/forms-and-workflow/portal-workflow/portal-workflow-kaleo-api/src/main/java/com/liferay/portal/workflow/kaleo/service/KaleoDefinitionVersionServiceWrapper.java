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

package com.liferay.portal.workflow.kaleo.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link KaleoDefinitionVersionService}.
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDefinitionVersionService
 * @generated
 */
@ProviderType
public class KaleoDefinitionVersionServiceWrapper
	implements KaleoDefinitionVersionService,
		ServiceWrapper<KaleoDefinitionVersionService> {
	public KaleoDefinitionVersionServiceWrapper(
		KaleoDefinitionVersionService kaleoDefinitionVersionService) {
		_kaleoDefinitionVersionService = kaleoDefinitionVersionService;
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion addKaleoDefinitionVersion(
		long userId, long groupId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content, int version,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDefinitionVersionService.addKaleoDefinitionVersion(userId,
			groupId, name, titleMap, content, version, serviceContext);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion getKaleoDefinitionVersion(
		java.lang.String name, int version,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDefinitionVersionService.getKaleoDefinitionVersion(name,
			version, serviceContext);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion getLatestKaleoDefinitionVersion(
		java.lang.String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDefinitionVersionService.getLatestKaleoDefinitionVersion(name,
			serviceContext);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion publishKaleoDefinitionVersion(
		long userId, long groupId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDefinitionVersionService.publishKaleoDefinitionVersion(userId,
			groupId, name, titleMap, content, serviceContext);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion updateKaleoDefinitionVersion(
		long userId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDefinitionVersionService.updateKaleoDefinitionVersion(userId,
			name, titleMap, content, serviceContext);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public java.lang.String getOSGiServiceIdentifier() {
		return _kaleoDefinitionVersionService.getOSGiServiceIdentifier();
	}

	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getKaleoDefinitionVersions()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDefinitionVersionService.getKaleoDefinitionVersions();
	}

	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDefinitionVersionService.getLatestKaleoDefinitionVersions(companyId,
			start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		long companyId, java.lang.String keywords, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDefinitionVersionService.getLatestKaleoDefinitionVersions(companyId,
			keywords, start, end, orderByComparator);
	}

	@Override
	public void deleteKaleoDefinitionVersions(java.lang.String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		_kaleoDefinitionVersionService.deleteKaleoDefinitionVersions(name,
			serviceContext);
	}

	@Override
	public KaleoDefinitionVersionService getWrappedService() {
		return _kaleoDefinitionVersionService;
	}

	@Override
	public void setWrappedService(
		KaleoDefinitionVersionService kaleoDefinitionVersionService) {
		_kaleoDefinitionVersionService = kaleoDefinitionVersionService;
	}

	private KaleoDefinitionVersionService _kaleoDefinitionVersionService;
}