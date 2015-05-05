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

package com.liferay.portlet.dynamicdatamapping.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link DDMStructureRestrictionLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see DDMStructureRestrictionLocalService
 * @generated
 */
@ProviderType
public class DDMStructureRestrictionLocalServiceWrapper
	implements DDMStructureRestrictionLocalService,
		ServiceWrapper<DDMStructureRestrictionLocalService> {
	public DDMStructureRestrictionLocalServiceWrapper(
		DDMStructureRestrictionLocalService ddmStructureRestrictionLocalService) {
		_ddmStructureRestrictionLocalService = ddmStructureRestrictionLocalService;
	}

	/**
	* Adds the d d m structure restriction to the database. Also notifies the appropriate model listeners.
	*
	* @param ddmStructureRestriction the d d m structure restriction
	* @return the d d m structure restriction that was added
	*/
	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction addDDMStructureRestriction(
		com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction ddmStructureRestriction) {
		return _ddmStructureRestrictionLocalService.addDDMStructureRestriction(ddmStructureRestriction);
	}

	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction addStructureRestricion(
		java.lang.String className, long classPK, long structureId) {
		return _ddmStructureRestrictionLocalService.addStructureRestricion(className,
			classPK, structureId);
	}

	/**
	* Creates a new d d m structure restriction with the primary key. Does not add the d d m structure restriction to the database.
	*
	* @param structureRestrictionId the primary key for the new d d m structure restriction
	* @return the new d d m structure restriction
	*/
	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction createDDMStructureRestriction(
		long structureRestrictionId) {
		return _ddmStructureRestrictionLocalService.createDDMStructureRestriction(structureRestrictionId);
	}

	/**
	* Deletes the d d m structure restriction from the database. Also notifies the appropriate model listeners.
	*
	* @param ddmStructureRestriction the d d m structure restriction
	* @return the d d m structure restriction that was removed
	*/
	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction deleteDDMStructureRestriction(
		com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction ddmStructureRestriction) {
		return _ddmStructureRestrictionLocalService.deleteDDMStructureRestriction(ddmStructureRestriction);
	}

	/**
	* Deletes the d d m structure restriction with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param structureRestrictionId the primary key of the d d m structure restriction
	* @return the d d m structure restriction that was removed
	* @throws PortalException if a d d m structure restriction with the primary key could not be found
	*/
	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction deleteDDMStructureRestriction(
		long structureRestrictionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureRestrictionLocalService.deleteDDMStructureRestriction(structureRestrictionId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.model.PersistedModel deletePersistedModel(
		com.liferay.portal.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureRestrictionLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction deleteStructureRestriction(
		java.lang.String className, long classPk, long ddmStructureId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureRestrictionLocalService.deleteStructureRestriction(className,
			classPk, ddmStructureId);
	}

	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction deleteStructureRestriction(
		long structureRestrictionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureRestrictionLocalService.deleteStructureRestriction(structureRestrictionId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _ddmStructureRestrictionLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _ddmStructureRestrictionLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.dynamicdatamapping.model.impl.DDMStructureRestrictionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return _ddmStructureRestrictionLocalService.dynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.dynamicdatamapping.model.impl.DDMStructureRestrictionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return _ddmStructureRestrictionLocalService.dynamicQuery(dynamicQuery,
			start, end, orderByComparator);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _ddmStructureRestrictionLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return _ddmStructureRestrictionLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction fetchDDMStructureRestriction(
		long structureRestrictionId) {
		return _ddmStructureRestrictionLocalService.fetchDDMStructureRestriction(structureRestrictionId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _ddmStructureRestrictionLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _ddmStructureRestrictionLocalService.getBeanIdentifier();
	}

	/**
	* Returns the d d m structure restriction with the primary key.
	*
	* @param structureRestrictionId the primary key of the d d m structure restriction
	* @return the d d m structure restriction
	* @throws PortalException if a d d m structure restriction with the primary key could not be found
	*/
	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction getDDMStructureRestriction(
		long structureRestrictionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureRestrictionLocalService.getDDMStructureRestriction(structureRestrictionId);
	}

	/**
	* Returns a range of all the d d m structure restrictions.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.dynamicdatamapping.model.impl.DDMStructureRestrictionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of d d m structure restrictions
	* @param end the upper bound of the range of d d m structure restrictions (not inclusive)
	* @return the range of d d m structure restrictions
	*/
	@Override
	public java.util.List<com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction> getDDMStructureRestrictions(
		int start, int end) {
		return _ddmStructureRestrictionLocalService.getDDMStructureRestrictions(start,
			end);
	}

	/**
	* Returns the number of d d m structure restrictions.
	*
	* @return the number of d d m structure restrictions
	*/
	@Override
	public int getDDMStructureRestrictionsCount() {
		return _ddmStructureRestrictionLocalService.getDDMStructureRestrictionsCount();
	}

	@Override
	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureRestrictionLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public java.util.List<com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction> getStructureRestrictions(
		java.lang.String className, long classPK) {
		return _ddmStructureRestrictionLocalService.getStructureRestrictions(className,
			classPK);
	}

	@Override
	public java.util.List<com.liferay.portlet.dynamicdatamapping.model.DDMStructure> getStructures(
		java.lang.String className, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureRestrictionLocalService.getStructures(className,
			classPK);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_ddmStructureRestrictionLocalService.setBeanIdentifier(beanIdentifier);
	}

	/**
	* Updates the d d m structure restriction in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param ddmStructureRestriction the d d m structure restriction
	* @return the d d m structure restriction that was updated
	*/
	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction updateDDMStructureRestriction(
		com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction ddmStructureRestriction) {
		return _ddmStructureRestrictionLocalService.updateDDMStructureRestriction(ddmStructureRestriction);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public DDMStructureRestrictionLocalService getWrappedDDMStructureRestrictionLocalService() {
		return _ddmStructureRestrictionLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedDDMStructureRestrictionLocalService(
		DDMStructureRestrictionLocalService ddmStructureRestrictionLocalService) {
		_ddmStructureRestrictionLocalService = ddmStructureRestrictionLocalService;
	}

	@Override
	public DDMStructureRestrictionLocalService getWrappedService() {
		return _ddmStructureRestrictionLocalService;
	}

	@Override
	public void setWrappedService(
		DDMStructureRestrictionLocalService ddmStructureRestrictionLocalService) {
		_ddmStructureRestrictionLocalService = ddmStructureRestrictionLocalService;
	}

	private DDMStructureRestrictionLocalService _ddmStructureRestrictionLocalService;
}