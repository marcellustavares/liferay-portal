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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for KaleoDraftDefinition. This utility wraps
 * {@link com.liferay.portal.workflow.kaleo.service.impl.KaleoDraftDefinitionServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDraftDefinitionService
 * @see com.liferay.portal.workflow.kaleo.service.base.KaleoDraftDefinitionServiceBaseImpl
 * @see com.liferay.portal.workflow.kaleo.service.impl.KaleoDraftDefinitionServiceImpl
 * @generated
 */
@ProviderType
public class KaleoDraftDefinitionServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portal.workflow.kaleo.service.impl.KaleoDraftDefinitionServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition addKaleoDraftDefinition(
		long userId, long groupId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content, int version, int draftVersion,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addKaleoDraftDefinition(userId, groupId, name, titleMap,
			content, version, draftVersion, serviceContext);
	}

	public static void deleteKaleoDraftDefinitions(java.lang.String name,
		int version, com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().deleteKaleoDraftDefinitions(name, version, serviceContext);
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition getKaleoDraftDefinition(
		java.lang.String name, int version, int draftVersion,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getKaleoDraftDefinition(name, version, draftVersion,
			serviceContext);
	}

	public static java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition> getKaleoDraftDefinitions()
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getKaleoDraftDefinitions();
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition getLatestKaleoDraftDefinition(
		java.lang.String name, int version,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getLatestKaleoDraftDefinition(name, version, serviceContext);
	}

	public static java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition> getLatestKaleoDraftDefinitions(
		long companyId, int version, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getLatestKaleoDraftDefinitions(companyId, version, start,
			end, orderByComparator);
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition publishKaleoDraftDefinition(
		long userId, long groupId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .publishKaleoDraftDefinition(userId, groupId, name,
			titleMap, content, serviceContext);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition updateKaleoDraftDefinition(
		long userId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content, int version,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .updateKaleoDraftDefinition(userId, name, titleMap, content,
			version, serviceContext);
	}

	public static KaleoDraftDefinitionService getService() {
		return _serviceTracker.getService();
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	@Deprecated
	public void setService(KaleoDraftDefinitionService service) {
	}

	private static ServiceTracker<KaleoDraftDefinitionService, KaleoDraftDefinitionService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(KaleoDraftDefinitionServiceUtil.class);

		_serviceTracker = new ServiceTracker<KaleoDraftDefinitionService, KaleoDraftDefinitionService>(bundle.getBundleContext(),
				KaleoDraftDefinitionService.class, null);

		_serviceTracker.open();
	}
}