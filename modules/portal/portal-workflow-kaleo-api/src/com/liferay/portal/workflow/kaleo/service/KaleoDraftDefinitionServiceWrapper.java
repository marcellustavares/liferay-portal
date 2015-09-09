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

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _kaleoDraftDefinitionService.getBeanIdentifier();
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