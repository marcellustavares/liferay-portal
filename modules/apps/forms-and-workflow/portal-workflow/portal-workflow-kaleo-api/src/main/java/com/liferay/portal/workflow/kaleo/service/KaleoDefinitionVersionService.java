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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Provides the remote service interface for KaleoDefinitionVersion. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDefinitionVersionServiceUtil
 * @see com.liferay.portal.workflow.kaleo.service.base.KaleoDefinitionVersionServiceBaseImpl
 * @see com.liferay.portal.workflow.kaleo.service.impl.KaleoDefinitionVersionServiceImpl
 * @generated
 */
@AccessControlled
@JSONWebService
@OSGiBeanProperties(property =  {
	"json.web.service.context.name=kaleo", "json.web.service.context.path=KaleoDefinitionVersion"}, service = KaleoDefinitionVersionService.class)
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface KaleoDefinitionVersionService extends BaseService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link KaleoDefinitionVersionServiceUtil} to access the kaleo definition version remote service. Add custom service methods to {@link com.liferay.portal.workflow.kaleo.service.impl.KaleoDefinitionVersionServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public KaleoDefinitionVersion addKaleoDefinitionVersion(long userId,
		long groupId, java.lang.String name,
		Map<Locale, java.lang.String> titleMap, java.lang.String content,
		int version, ServiceContext serviceContext) throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public KaleoDefinitionVersion getKaleoDefinitionVersion(
		java.lang.String name, int version, ServiceContext serviceContext)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public KaleoDefinitionVersion getLatestKaleoDefinitionVersion(
		java.lang.String name, ServiceContext serviceContext)
		throws PortalException;

	public KaleoDefinitionVersion publishKaleoDefinitionVersion(long userId,
		long groupId, java.lang.String name,
		Map<Locale, java.lang.String> titleMap, java.lang.String content,
		ServiceContext serviceContext) throws PortalException;

	public KaleoDefinitionVersion updateKaleoDefinitionVersion(long userId,
		java.lang.String name, Map<Locale, java.lang.String> titleMap,
		java.lang.String content, ServiceContext serviceContext)
		throws PortalException;

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public java.lang.String getOSGiServiceIdentifier();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<KaleoDefinitionVersion> getKaleoDefinitionVersions()
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		long companyId, int start, int end,
		OrderByComparator<KaleoDefinitionVersion> orderByComparator)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		long companyId, java.lang.String keywords, int start, int end,
		OrderByComparator<KaleoDefinitionVersion> orderByComparator)
		throws PortalException;

	public void deleteKaleoDefinitionVersions(java.lang.String name,
		ServiceContext serviceContext) throws PortalException;
}