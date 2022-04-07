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

package com.liferay.analytics.message.storage.service.impl;

import com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger;
import com.liferay.analytics.message.storage.service.base.AnalyticsModelRemoveLoggerLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.analytics.message.storage.model.AnalyticsModelRemoveLogger",
	service = AopService.class
)
public class AnalyticsModelRemoveLoggerLocalServiceImpl
	extends AnalyticsModelRemoveLoggerLocalServiceBaseImpl {

	@Override
	public AnalyticsModelRemoveLogger addAnalyticsModelRemoveLogger(
		long companyId, Date createDate, String className, long classPK,
		long userId) {

		AnalyticsModelRemoveLogger analyticsModelRemoveLogger =
			analyticsModelRemoveLoggerPersistence.create(
				counterLocalService.increment());

		analyticsModelRemoveLogger.setCompanyId(companyId);
		analyticsModelRemoveLogger.setUserId(userId);
		analyticsModelRemoveLogger.setCreateDate(createDate);
		analyticsModelRemoveLogger.setModifiedDate(createDate);
		analyticsModelRemoveLogger.setClassName(className);
		analyticsModelRemoveLogger.setClassPK(classPK);

		return analyticsModelRemoveLoggerPersistence.update(
			analyticsModelRemoveLogger);
	}

	@Override
	public void deleteGtAnalyticsModelRemoveLoggers(
		long companyId, Date gtModifiedDate) {

		analyticsModelRemoveLoggerPersistence.removeByGtM_C(
			companyId, gtModifiedDate);
	}

	@Override
	public void deleteLteAnalyticsModelRemoveLoggers(
		long companyId, Date lteModifiedDate) {

		analyticsModelRemoveLoggerPersistence.removeByLteM_C(
			companyId, lteModifiedDate);
	}

	@Override
	public List<AnalyticsModelRemoveLogger> getAnalyticsModelRemoveLoggers(
		long companyId, int start, int end) {

		return analyticsModelRemoveLoggerPersistence.findByCompanyId(
			companyId, start, end);
	}

	@Override
	public int getAnalyticsModelRemoveLoggersCount(long companyId) {
		return analyticsModelRemoveLoggerPersistence.countByCompanyId(
			companyId);
	}

	@Override
	public List<AnalyticsModelRemoveLogger> getGtAnalyticsModelRemoveLoggers(
		long companyId, Date gtModifiedDate, int start, int end) {

		return analyticsModelRemoveLoggerPersistence.findByGtM_C(
			companyId, gtModifiedDate, start, end);
	}

	@Override
	public int getGtAnalyticsModelRemoveLoggersCount(
		long companyId, Date gtModifiedDate) {

		return analyticsModelRemoveLoggerPersistence.countByGtM_C(
			companyId, gtModifiedDate);
	}

	@Override
	public List<AnalyticsModelRemoveLogger> getLteAnalyticsModelRemoveLoggers(
		long companyId, Date lteModifiedDate, int start, int end) {

		return analyticsModelRemoveLoggerPersistence.findByLteM_C(
			companyId, lteModifiedDate, start, end);
	}

}