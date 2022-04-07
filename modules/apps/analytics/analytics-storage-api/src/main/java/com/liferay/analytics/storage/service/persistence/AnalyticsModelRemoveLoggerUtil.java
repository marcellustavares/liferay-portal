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

package com.liferay.analytics.storage.service.persistence;

import com.liferay.analytics.storage.model.AnalyticsModelRemoveLogger;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the analytics model remove logger service. This utility wraps <code>com.liferay.analytics.storage.service.persistence.impl.AnalyticsModelRemoveLoggerPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsModelRemoveLoggerPersistence
 * @generated
 */
public class AnalyticsModelRemoveLoggerUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(
		AnalyticsModelRemoveLogger analyticsModelRemoveLogger) {

		getPersistence().clearCache(analyticsModelRemoveLogger);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, AnalyticsModelRemoveLogger>
		fetchByPrimaryKeys(Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<AnalyticsModelRemoveLogger> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<AnalyticsModelRemoveLogger> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<AnalyticsModelRemoveLogger> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static AnalyticsModelRemoveLogger update(
		AnalyticsModelRemoveLogger analyticsModelRemoveLogger) {

		return getPersistence().update(analyticsModelRemoveLogger);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static AnalyticsModelRemoveLogger update(
		AnalyticsModelRemoveLogger analyticsModelRemoveLogger,
		ServiceContext serviceContext) {

		return getPersistence().update(
			analyticsModelRemoveLogger, serviceContext);
	}

	/**
	 * Returns all the analytics model remove loggers where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByCompanyId(
		long companyId) {

		return getPersistence().findByCompanyId(companyId);
	}

	/**
	 * Returns a range of all the analytics model remove loggers where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @return the range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByCompanyId(
		long companyId, int start, int end) {

		return getPersistence().findByCompanyId(companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the analytics model remove loggers where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().findByCompanyId(
			companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the analytics model remove loggers where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByCompanyId(
			companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger findByCompanyId_First(
			long companyId,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByCompanyId_First(
			companyId, orderByComparator);
	}

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger fetchByCompanyId_First(
		long companyId,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().fetchByCompanyId_First(
			companyId, orderByComparator);
	}

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger findByCompanyId_Last(
			long companyId,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByCompanyId_Last(
			companyId, orderByComparator);
	}

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger fetchByCompanyId_Last(
		long companyId,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().fetchByCompanyId_Last(
			companyId, orderByComparator);
	}

	/**
	 * Returns the analytics model remove loggers before and after the current analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the current analytics model remove logger
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a analytics model remove logger with the primary key could not be found
	 */
	public static AnalyticsModelRemoveLogger[] findByCompanyId_PrevAndNext(
			long analyticsModelRemoveLoggerId, long companyId,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByCompanyId_PrevAndNext(
			analyticsModelRemoveLoggerId, companyId, orderByComparator);
	}

	/**
	 * Removes all the analytics model remove loggers where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public static void removeByCompanyId(long companyId) {
		getPersistence().removeByCompanyId(companyId);
	}

	/**
	 * Returns the number of analytics model remove loggers where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching analytics model remove loggers
	 */
	public static int countByCompanyId(long companyId) {
		return getPersistence().countByCompanyId(companyId);
	}

	/**
	 * Returns all the analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @return the matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByGtM_C(
		long companyId, Date modifiedDate) {

		return getPersistence().findByGtM_C(companyId, modifiedDate);
	}

	/**
	 * Returns a range of all the analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @return the range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByGtM_C(
		long companyId, Date modifiedDate, int start, int end) {

		return getPersistence().findByGtM_C(
			companyId, modifiedDate, start, end);
	}

	/**
	 * Returns an ordered range of all the analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByGtM_C(
		long companyId, Date modifiedDate, int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().findByGtM_C(
			companyId, modifiedDate, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByGtM_C(
		long companyId, Date modifiedDate, int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGtM_C(
			companyId, modifiedDate, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger findByGtM_C_First(
			long companyId, Date modifiedDate,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByGtM_C_First(
			companyId, modifiedDate, orderByComparator);
	}

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger fetchByGtM_C_First(
		long companyId, Date modifiedDate,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().fetchByGtM_C_First(
			companyId, modifiedDate, orderByComparator);
	}

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger findByGtM_C_Last(
			long companyId, Date modifiedDate,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByGtM_C_Last(
			companyId, modifiedDate, orderByComparator);
	}

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger fetchByGtM_C_Last(
		long companyId, Date modifiedDate,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().fetchByGtM_C_Last(
			companyId, modifiedDate, orderByComparator);
	}

	/**
	 * Returns the analytics model remove loggers before and after the current analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the current analytics model remove logger
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a analytics model remove logger with the primary key could not be found
	 */
	public static AnalyticsModelRemoveLogger[] findByGtM_C_PrevAndNext(
			long analyticsModelRemoveLoggerId, long companyId,
			Date modifiedDate,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByGtM_C_PrevAndNext(
			analyticsModelRemoveLoggerId, companyId, modifiedDate,
			orderByComparator);
	}

	/**
	 * Removes all the analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 */
	public static void removeByGtM_C(long companyId, Date modifiedDate) {
		getPersistence().removeByGtM_C(companyId, modifiedDate);
	}

	/**
	 * Returns the number of analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @return the number of matching analytics model remove loggers
	 */
	public static int countByGtM_C(long companyId, Date modifiedDate) {
		return getPersistence().countByGtM_C(companyId, modifiedDate);
	}

	/**
	 * Returns all the analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @return the matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByLteM_C(
		long companyId, Date modifiedDate) {

		return getPersistence().findByLteM_C(companyId, modifiedDate);
	}

	/**
	 * Returns a range of all the analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @return the range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByLteM_C(
		long companyId, Date modifiedDate, int start, int end) {

		return getPersistence().findByLteM_C(
			companyId, modifiedDate, start, end);
	}

	/**
	 * Returns an ordered range of all the analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByLteM_C(
		long companyId, Date modifiedDate, int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().findByLteM_C(
			companyId, modifiedDate, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findByLteM_C(
		long companyId, Date modifiedDate, int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByLteM_C(
			companyId, modifiedDate, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger findByLteM_C_First(
			long companyId, Date modifiedDate,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByLteM_C_First(
			companyId, modifiedDate, orderByComparator);
	}

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger fetchByLteM_C_First(
		long companyId, Date modifiedDate,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().fetchByLteM_C_First(
			companyId, modifiedDate, orderByComparator);
	}

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger findByLteM_C_Last(
			long companyId, Date modifiedDate,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByLteM_C_Last(
			companyId, modifiedDate, orderByComparator);
	}

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public static AnalyticsModelRemoveLogger fetchByLteM_C_Last(
		long companyId, Date modifiedDate,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().fetchByLteM_C_Last(
			companyId, modifiedDate, orderByComparator);
	}

	/**
	 * Returns the analytics model remove loggers before and after the current analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the current analytics model remove logger
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a analytics model remove logger with the primary key could not be found
	 */
	public static AnalyticsModelRemoveLogger[] findByLteM_C_PrevAndNext(
			long analyticsModelRemoveLoggerId, long companyId,
			Date modifiedDate,
			OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByLteM_C_PrevAndNext(
			analyticsModelRemoveLoggerId, companyId, modifiedDate,
			orderByComparator);
	}

	/**
	 * Removes all the analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 */
	public static void removeByLteM_C(long companyId, Date modifiedDate) {
		getPersistence().removeByLteM_C(companyId, modifiedDate);
	}

	/**
	 * Returns the number of analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @return the number of matching analytics model remove loggers
	 */
	public static int countByLteM_C(long companyId, Date modifiedDate) {
		return getPersistence().countByLteM_C(companyId, modifiedDate);
	}

	/**
	 * Caches the analytics model remove logger in the entity cache if it is enabled.
	 *
	 * @param analyticsModelRemoveLogger the analytics model remove logger
	 */
	public static void cacheResult(
		AnalyticsModelRemoveLogger analyticsModelRemoveLogger) {

		getPersistence().cacheResult(analyticsModelRemoveLogger);
	}

	/**
	 * Caches the analytics model remove loggers in the entity cache if it is enabled.
	 *
	 * @param analyticsModelRemoveLoggers the analytics model remove loggers
	 */
	public static void cacheResult(
		List<AnalyticsModelRemoveLogger> analyticsModelRemoveLoggers) {

		getPersistence().cacheResult(analyticsModelRemoveLoggers);
	}

	/**
	 * Creates a new analytics model remove logger with the primary key. Does not add the analytics model remove logger to the database.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key for the new analytics model remove logger
	 * @return the new analytics model remove logger
	 */
	public static AnalyticsModelRemoveLogger create(
		long analyticsModelRemoveLoggerId) {

		return getPersistence().create(analyticsModelRemoveLoggerId);
	}

	/**
	 * Removes the analytics model remove logger with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the analytics model remove logger
	 * @return the analytics model remove logger that was removed
	 * @throws NoSuchModelRemoveLoggerException if a analytics model remove logger with the primary key could not be found
	 */
	public static AnalyticsModelRemoveLogger remove(
			long analyticsModelRemoveLoggerId)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().remove(analyticsModelRemoveLoggerId);
	}

	public static AnalyticsModelRemoveLogger updateImpl(
		AnalyticsModelRemoveLogger analyticsModelRemoveLogger) {

		return getPersistence().updateImpl(analyticsModelRemoveLogger);
	}

	/**
	 * Returns the analytics model remove logger with the primary key or throws a <code>NoSuchModelRemoveLoggerException</code> if it could not be found.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the analytics model remove logger
	 * @return the analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a analytics model remove logger with the primary key could not be found
	 */
	public static AnalyticsModelRemoveLogger findByPrimaryKey(
			long analyticsModelRemoveLoggerId)
		throws com.liferay.analytics.storage.exception.
			NoSuchModelRemoveLoggerException {

		return getPersistence().findByPrimaryKey(analyticsModelRemoveLoggerId);
	}

	/**
	 * Returns the analytics model remove logger with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the analytics model remove logger
	 * @return the analytics model remove logger, or <code>null</code> if a analytics model remove logger with the primary key could not be found
	 */
	public static AnalyticsModelRemoveLogger fetchByPrimaryKey(
		long analyticsModelRemoveLoggerId) {

		return getPersistence().fetchByPrimaryKey(analyticsModelRemoveLoggerId);
	}

	/**
	 * Returns all the analytics model remove loggers.
	 *
	 * @return the analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the analytics model remove loggers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @return the range of analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the analytics model remove loggers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findAll(
		int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the analytics model remove loggers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsModelRemoveLoggerModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics model remove loggers
	 * @param end the upper bound of the range of analytics model remove loggers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of analytics model remove loggers
	 */
	public static List<AnalyticsModelRemoveLogger> findAll(
		int start, int end,
		OrderByComparator<AnalyticsModelRemoveLogger> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the analytics model remove loggers from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of analytics model remove loggers.
	 *
	 * @return the number of analytics model remove loggers
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static AnalyticsModelRemoveLoggerPersistence getPersistence() {
		return _persistence;
	}

	private static volatile AnalyticsModelRemoveLoggerPersistence _persistence;

}