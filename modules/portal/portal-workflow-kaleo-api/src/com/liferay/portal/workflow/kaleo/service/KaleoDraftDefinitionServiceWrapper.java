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

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link KaleoDraftDefinitionService}.
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDraftDefinitionService
 * @generated
 */
@ProviderType
public class KaleoDraftDefinitionServiceWrapper
	implements KaleoDraftDefinitionService,
		ServiceWrapper<KaleoDraftDefinitionService> {
	public KaleoDraftDefinitionServiceWrapper(
		KaleoDraftDefinitionService kaleoDraftDefinitionService) {
		_kaleoDraftDefinitionService = kaleoDraftDefinitionService;
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition addKaleoDraftDefinition(
		long userId, long groupId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content, int version, int draftVersion,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDraftDefinitionService.addKaleoDraftDefinition(userId,
			groupId, name, titleMap, content, version, draftVersion,
			serviceContext);
	}

	@Override
	public void deleteKaleoDraftDefinitions(java.lang.String name, int version,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		_kaleoDraftDefinitionService.deleteKaleoDraftDefinitions(name, version,
			serviceContext);
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _kaleoDraftDefinitionService.getBeanIdentifier();
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition getKaleoDraftDefinition(
		java.lang.String name, int version, int draftVersion,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDraftDefinitionService.getKaleoDraftDefinition(name,
			version, draftVersion, serviceContext);
	}

	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition> getKaleoDraftDefinitions()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDraftDefinitionService.getKaleoDraftDefinitions();
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition getLatestKaleoDraftDefinition(
		java.lang.String name, int version,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDraftDefinitionService.getLatestKaleoDraftDefinition(name,
			version, serviceContext);
	}

	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition> getLatestKaleoDraftDefinitions(
		long companyId, int version, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDraftDefinitionService.getLatestKaleoDraftDefinitions(companyId,
			version, start, end, orderByComparator);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition publishKaleoDraftDefinition(
		long userId, long groupId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDraftDefinitionService.publishKaleoDraftDefinition(userId,
			groupId, name, titleMap, content, serviceContext);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_kaleoDraftDefinitionService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition updateKaleoDraftDefinition(
		long userId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content, int version,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoDraftDefinitionService.updateKaleoDraftDefinition(userId,
			name, titleMap, content, version, serviceContext);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public KaleoDraftDefinitionService getWrappedKaleoDraftDefinitionService() {
		return _kaleoDraftDefinitionService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedKaleoDraftDefinitionService(
		KaleoDraftDefinitionService kaleoDraftDefinitionService) {
		_kaleoDraftDefinitionService = kaleoDraftDefinitionService;
	}

	@Override
	public KaleoDraftDefinitionService getWrappedService() {
		return _kaleoDraftDefinitionService;
	}

	@Override
	public void setWrappedService(
		KaleoDraftDefinitionService kaleoDraftDefinitionService) {
		_kaleoDraftDefinitionService = kaleoDraftDefinitionService;
	}

	private KaleoDraftDefinitionService _kaleoDraftDefinitionService;
}