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

package com.liferay.analytics.message.storage.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link AnalyticsModelRemoveLoggerLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsModelRemoveLoggerLocalService
 * @generated
 */
public class AnalyticsModelRemoveLoggerLocalServiceWrapper
	implements AnalyticsModelRemoveLoggerLocalService,
			   ServiceWrapper<AnalyticsModelRemoveLoggerLocalService> {

	public AnalyticsModelRemoveLoggerLocalServiceWrapper() {
		this(null);
	}

	public AnalyticsModelRemoveLoggerLocalServiceWrapper(
		AnalyticsModelRemoveLoggerLocalService
			analyticsModelRemoveLoggerLocalService) {

		_analyticsModelRemoveLoggerLocalService =
			analyticsModelRemoveLoggerLocalService;
	}

	/**
	 * Adds the analytics model remove logger to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AnalyticsModelRemoveLoggerLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param analyticsModelRemoveLogger the analytics model remove logger
	 * @return the analytics model remove logger that was added
	 */
	@Override
	public
		com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger
			addAnalyticsModelRemoveLogger(
				com.liferay.analytics.message.storage.model.
					AnalyticsModelRemoveLogger analyticsModelRemoveLogger) {

		return _analyticsModelRemoveLoggerLocalService.
			addAnalyticsModelRemoveLogger(analyticsModelRemoveLogger);
	}

	@Override
	public
		com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger
			addAnalyticsModelRemoveLogger(
				long companyId, java.util.Date createDate, String className,
				long classPK, long userId) {

		return _analyticsModelRemoveLoggerLocalService.
			addAnalyticsModelRemoveLogger(
				companyId, createDate, className, classPK, userId);
	}

	/**
	 * Creates a new analytics model remove logger with the primary key. Does not add the analytics model remove logger to the database.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key for the new analytics model remove logger
	 * @return the new analytics model remove logger
	 */
	@Override
	public
		com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger
			createAnalyticsModelRemoveLogger(
				long analyticsModelRemoveLoggerId) {

		return _analyticsModelRemoveLoggerLocalService.
			createAnalyticsModelRemoveLogger(analyticsModelRemoveLoggerId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsModelRemoveLoggerLocalService.createPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Deletes the analytics model remove logger from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AnalyticsModelRemoveLoggerLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param analyticsModelRemoveLogger the analytics model remove logger
	 * @return the analytics model remove logger that was removed
	 */
	@Override
	public
		com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger
			deleteAnalyticsModelRemoveLogger(
				com.liferay.analytics.message.storage.model.
					AnalyticsModelRemoveLogger analyticsModelRemoveLogger) {

		return _analyticsModelRemoveLoggerLocalService.
			deleteAnalyticsModelRemoveLogger(analyticsModelRemoveLogger);
	}

	/**
	 * Deletes the analytics model remove logger with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AnalyticsModelRemoveLoggerLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the analytics model remove logger
	 * @return the analytics model remove logger that was removed
	 * @throws PortalException if a analytics model remove logger with the primary key could not be found
	 */
	@Override
	public
		com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger
				deleteAnalyticsModelRemoveLogger(
					long analyticsModelRemoveLoggerId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsModelRemoveLoggerLocalService.
			deleteAnalyticsModelRemoveLogger(analyticsModelRemoveLoggerId);
	}

	@Override
	public void deleteGtAnalyticsModelRemoveLoggers(
		long companyId, java.util.Date gtModifiedDate) {

		_analyticsModelRemoveLoggerLocalService.
			deleteGtAnalyticsModelRemoveLoggers(companyId, gtModifiedDate);
	}

	@Override
	public void deleteLteAnalyticsModelRemoveLoggers(
		long companyId, java.util.Date lteModifiedDate) {

		_analyticsModelRemoveLoggerLocalService.
			deleteLteAnalyticsModelRemoveLoggers(companyId, lteModifiedDate);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsModelRemoveLoggerLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _analyticsModelRemoveLoggerLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _analyticsModelRemoveLoggerLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _analyticsModelRemoveLoggerLocalService.dynamicQuery();
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

		return _analyticsModelRemoveLoggerLocalService.dynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.analytics.message.storage.model.impl.AnalyticsModelRemoveLoggerModelImpl</code>.
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

		return _analyticsModelRemoveLoggerLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.analytics.message.storage.model.impl.AnalyticsModelRemoveLoggerModelImpl</code>.
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

		return _analyticsModelRemoveLoggerLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
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

		return _analyticsModelRemoveLoggerLocalService.dynamicQueryCount(
			dynamicQuery);
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

		return _analyticsModelRemoveLoggerLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public
		com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger
			fetchAnalyticsModelRemoveLogger(long analyticsModelRemoveLoggerId) {

		return _analyticsModelRemoveLoggerLocalService.
			fetchAnalyticsModelRemoveLogger(analyticsModelRemoveLoggerId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _analyticsModelRemoveLoggerLocalService.
			getActionableDynamicQuery();
	}

	/**
	 * Returns the analytics model remove logger with the primary key.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the analytics model remove logger
	 * @return the analytics model remove logger
	 * @throws PortalException if a analytics model remove logger with the primary key could not be found
	 */
	@Override
	public
		com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger
				getAnalyticsModelRemoveLogger(long analyticsModelRemoveLoggerId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsModelRemoveLoggerLocalService.
			getAnalyticsModelRemoveLogger(analyticsModelRemoveLoggerId);
	}

	/**
	 * Returns a range of all the analytics model remove loggers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.analytics.message.storage.model.impl.AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @return the range of analytics model remove loggers
	 */
	@Override
	public java.util.List
		<com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger>
			getAnalyticsModelRemoveLoggers(int start, int end) {

		return _analyticsModelRemoveLoggerLocalService.
			getAnalyticsModelRemoveLoggers(start, end);
	}

	@Override
	public java.util.List
		<com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger>
			getAnalyticsModelRemoveLoggers(long companyId, int start, int end) {

		return _analyticsModelRemoveLoggerLocalService.
			getAnalyticsModelRemoveLoggers(companyId, start, end);
	}

	/**
	 * Returns the number of analytics model remove loggers.
	 *
	 * @return the number of analytics model remove loggers
	 */
	@Override
	public int getAnalyticsModelRemoveLoggersCount() {
		return _analyticsModelRemoveLoggerLocalService.
			getAnalyticsModelRemoveLoggersCount();
	}

	@Override
	public int getAnalyticsModelRemoveLoggersCount(long companyId) {
		return _analyticsModelRemoveLoggerLocalService.
			getAnalyticsModelRemoveLoggersCount(companyId);
	}

	@Override
	public java.util.List
		<com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger>
			getGtAnalyticsModelRemoveLoggers(
				long companyId, java.util.Date gtModifiedDate, int start,
				int end) {

		return _analyticsModelRemoveLoggerLocalService.
			getGtAnalyticsModelRemoveLoggers(
				companyId, gtModifiedDate, start, end);
	}

	@Override
	public int getGtAnalyticsModelRemoveLoggersCount(
		long companyId, java.util.Date gtModifiedDate) {

		return _analyticsModelRemoveLoggerLocalService.
			getGtAnalyticsModelRemoveLoggersCount(companyId, gtModifiedDate);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _analyticsModelRemoveLoggerLocalService.
			getIndexableActionableDynamicQuery();
	}

	@Override
	public java.util.List
		<com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger>
			getLteAnalyticsModelRemoveLoggers(
				long companyId, java.util.Date lteModifiedDate, int start,
				int end) {

		return _analyticsModelRemoveLoggerLocalService.
			getLteAnalyticsModelRemoveLoggers(
				companyId, lteModifiedDate, start, end);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _analyticsModelRemoveLoggerLocalService.
			getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _analyticsModelRemoveLoggerLocalService.getPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Updates the analytics model remove logger in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AnalyticsModelRemoveLoggerLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param analyticsModelRemoveLogger the analytics model remove logger
	 * @return the analytics model remove logger that was updated
	 */
	@Override
	public
		com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger
			updateAnalyticsModelRemoveLogger(
				com.liferay.analytics.message.storage.model.
					AnalyticsModelRemoveLogger analyticsModelRemoveLogger) {

		return _analyticsModelRemoveLoggerLocalService.
			updateAnalyticsModelRemoveLogger(analyticsModelRemoveLogger);
	}

	@Override
	public AnalyticsModelRemoveLoggerLocalService getWrappedService() {
		return _analyticsModelRemoveLoggerLocalService;
	}

	@Override
	public void setWrappedService(
		AnalyticsModelRemoveLoggerLocalService
			analyticsModelRemoveLoggerLocalService) {

		_analyticsModelRemoveLoggerLocalService =
			analyticsModelRemoveLoggerLocalService;
	}

	private AnalyticsModelRemoveLoggerLocalService
		_analyticsModelRemoveLoggerLocalService;

}