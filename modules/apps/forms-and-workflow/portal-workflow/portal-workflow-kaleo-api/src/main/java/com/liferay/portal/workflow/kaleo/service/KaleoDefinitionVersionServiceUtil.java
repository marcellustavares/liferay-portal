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

import com.liferay.osgi.util.ServiceTrackerFactory;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for KaleoDefinitionVersion. This utility wraps
 * {@link com.liferay.portal.workflow.kaleo.service.impl.KaleoDefinitionVersionServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDefinitionVersionService
 * @see com.liferay.portal.workflow.kaleo.service.base.KaleoDefinitionVersionServiceBaseImpl
 * @see com.liferay.portal.workflow.kaleo.service.impl.KaleoDefinitionVersionServiceImpl
 * @generated
 */
@ProviderType
public class KaleoDefinitionVersionServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portal.workflow.kaleo.service.impl.KaleoDefinitionVersionServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion addKaleoDefinitionVersion(
		long userId, long groupId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content, int version,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addKaleoDefinitionVersion(userId, groupId, name, titleMap,
			content, version, serviceContext);
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion getKaleoDefinitionVersion(
		java.lang.String name, int version,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getKaleoDefinitionVersion(name, version, serviceContext);
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion getLatestKaleoDefinitionVersion(
		java.lang.String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getLatestKaleoDefinitionVersion(name, serviceContext);
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion publishKaleoDefinitionVersion(
		long userId, long groupId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .publishKaleoDefinitionVersion(userId, groupId, name,
			titleMap, content, serviceContext);
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion updateKaleoDefinitionVersion(
		long userId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .updateKaleoDefinitionVersion(userId, name, titleMap,
			content, serviceContext);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getKaleoDefinitionVersions()
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getKaleoDefinitionVersions();
	}

	public static java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getLatestKaleoDefinitionVersions(companyId, start, end,
			orderByComparator);
	}

	public static java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		long companyId, java.lang.String keywords, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getLatestKaleoDefinitionVersions(companyId, keywords,
			start, end, orderByComparator);
	}

	public static void deleteKaleoDefinitionVersions(java.lang.String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().deleteKaleoDefinitionVersions(name, serviceContext);
	}

	public static KaleoDefinitionVersionService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<KaleoDefinitionVersionService, KaleoDefinitionVersionService> _serviceTracker =
		ServiceTrackerFactory.open(KaleoDefinitionVersionService.class);
}