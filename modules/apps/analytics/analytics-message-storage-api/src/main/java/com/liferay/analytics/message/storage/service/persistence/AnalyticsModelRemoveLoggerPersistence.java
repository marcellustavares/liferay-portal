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

package com.liferay.analytics.message.storage.service.persistence;

import com.liferay.analytics.message.storage.exception.NoSuchModelRemoveLoggerException;
import com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the analytics model remove logger service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsModelRemoveLoggerUtil
 * @generated
 */
@ProviderType
public interface AnalyticsModelRemoveLoggerPersistence
	extends BasePersistence<AnalyticsModelRemoveLogger> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link AnalyticsModelRemoveLoggerUtil} to access the analytics model remove logger persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the analytics model remove loggers where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching analytics model remove loggers
	 */
	public java.util.List<AnalyticsModelRemoveLogger> findByCompanyId(
		long companyId);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByCompanyId(
		long companyId, int start, int end);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger findByCompanyId_First(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger fetchByCompanyId_First(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger findByCompanyId_Last(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger fetchByCompanyId_Last(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

	/**
	 * Returns the analytics model remove loggers before and after the current analytics model remove logger in the ordered set where companyId = &#63;.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the current analytics model remove logger
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a analytics model remove logger with the primary key could not be found
	 */
	public AnalyticsModelRemoveLogger[] findByCompanyId_PrevAndNext(
			long analyticsModelRemoveLoggerId, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Removes all the analytics model remove loggers where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public void removeByCompanyId(long companyId);

	/**
	 * Returns the number of analytics model remove loggers where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching analytics model remove loggers
	 */
	public int countByCompanyId(long companyId);

	/**
	 * Returns all the analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @return the matching analytics model remove loggers
	 */
	public java.util.List<AnalyticsModelRemoveLogger> findByGtM_C(
		long companyId, Date modifiedDate);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByGtM_C(
		long companyId, Date modifiedDate, int start, int end);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByGtM_C(
		long companyId, Date modifiedDate, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByGtM_C(
		long companyId, Date modifiedDate, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger findByGtM_C_First(
			long companyId, Date modifiedDate,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger fetchByGtM_C_First(
		long companyId, Date modifiedDate,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger findByGtM_C_Last(
			long companyId, Date modifiedDate,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger fetchByGtM_C_Last(
		long companyId, Date modifiedDate,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

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
	public AnalyticsModelRemoveLogger[] findByGtM_C_PrevAndNext(
			long analyticsModelRemoveLoggerId, long companyId,
			Date modifiedDate,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Removes all the analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 */
	public void removeByGtM_C(long companyId, Date modifiedDate);

	/**
	 * Returns the number of analytics model remove loggers where companyId = &#63; and modifiedDate &gt; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @return the number of matching analytics model remove loggers
	 */
	public int countByGtM_C(long companyId, Date modifiedDate);

	/**
	 * Returns all the analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @return the matching analytics model remove loggers
	 */
	public java.util.List<AnalyticsModelRemoveLogger> findByLteM_C(
		long companyId, Date modifiedDate);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByLteM_C(
		long companyId, Date modifiedDate, int start, int end);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByLteM_C(
		long companyId, Date modifiedDate, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

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
	public java.util.List<AnalyticsModelRemoveLogger> findByLteM_C(
		long companyId, Date modifiedDate, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger findByLteM_C_First(
			long companyId, Date modifiedDate,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Returns the first analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger fetchByLteM_C_First(
		long companyId, Date modifiedDate,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger findByLteM_C_Last(
			long companyId, Date modifiedDate,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Returns the last analytics model remove logger in the ordered set where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics model remove logger, or <code>null</code> if a matching analytics model remove logger could not be found
	 */
	public AnalyticsModelRemoveLogger fetchByLteM_C_Last(
		long companyId, Date modifiedDate,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

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
	public AnalyticsModelRemoveLogger[] findByLteM_C_PrevAndNext(
			long analyticsModelRemoveLoggerId, long companyId,
			Date modifiedDate,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsModelRemoveLogger> orderByComparator)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Removes all the analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 */
	public void removeByLteM_C(long companyId, Date modifiedDate);

	/**
	 * Returns the number of analytics model remove loggers where companyId = &#63; and modifiedDate &le; &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @return the number of matching analytics model remove loggers
	 */
	public int countByLteM_C(long companyId, Date modifiedDate);

	/**
	 * Caches the analytics model remove logger in the entity cache if it is enabled.
	 *
	 * @param analyticsModelRemoveLogger the analytics model remove logger
	 */
	public void cacheResult(
		AnalyticsModelRemoveLogger analyticsModelRemoveLogger);

	/**
	 * Caches the analytics model remove loggers in the entity cache if it is enabled.
	 *
	 * @param analyticsModelRemoveLoggers the analytics model remove loggers
	 */
	public void cacheResult(
		java.util.List<AnalyticsModelRemoveLogger> analyticsModelRemoveLoggers);

	/**
	 * Creates a new analytics model remove logger with the primary key. Does not add the analytics model remove logger to the database.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key for the new analytics model remove logger
	 * @return the new analytics model remove logger
	 */
	public AnalyticsModelRemoveLogger create(long analyticsModelRemoveLoggerId);

	/**
	 * Removes the analytics model remove logger with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the analytics model remove logger
	 * @return the analytics model remove logger that was removed
	 * @throws NoSuchModelRemoveLoggerException if a analytics model remove logger with the primary key could not be found
	 */
	public AnalyticsModelRemoveLogger remove(long analyticsModelRemoveLoggerId)
		throws NoSuchModelRemoveLoggerException;

	public AnalyticsModelRemoveLogger updateImpl(
		AnalyticsModelRemoveLogger analyticsModelRemoveLogger);

	/**
	 * Returns the analytics model remove logger with the primary key or throws a <code>NoSuchModelRemoveLoggerException</code> if it could not be found.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the analytics model remove logger
	 * @return the analytics model remove logger
	 * @throws NoSuchModelRemoveLoggerException if a analytics model remove logger with the primary key could not be found
	 */
	public AnalyticsModelRemoveLogger findByPrimaryKey(
			long analyticsModelRemoveLoggerId)
		throws NoSuchModelRemoveLoggerException;

	/**
	 * Returns the analytics model remove logger with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param analyticsModelRemoveLoggerId the primary key of the analytics model remove logger
	 * @return the analytics model remove logger, or <code>null</code> if a analytics model remove logger with the primary key could not be found
	 */
	public AnalyticsModelRemoveLogger fetchByPrimaryKey(
		long analyticsModelRemoveLoggerId);

	/**
	 * Returns all the analytics model remove loggers.
	 *
	 * @return the analytics model remove loggers
	 */
	public java.util.List<AnalyticsModelRemoveLogger> findAll();

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
	public java.util.List<AnalyticsModelRemoveLogger> findAll(
		int start, int end);

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
	public java.util.List<AnalyticsModelRemoveLogger> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator);

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
	public java.util.List<AnalyticsModelRemoveLogger> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<AnalyticsModelRemoveLogger> orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the analytics model remove loggers from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of analytics model remove loggers.
	 *
	 * @return the number of analytics model remove loggers
	 */
	public int countAll();

}