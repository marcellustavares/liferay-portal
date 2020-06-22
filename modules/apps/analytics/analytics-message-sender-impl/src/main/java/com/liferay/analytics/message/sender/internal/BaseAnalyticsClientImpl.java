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

package com.liferay.analytics.message.sender.internal;

import com.liferay.analytics.message.storage.service.AnalyticsMessageLocalService;
import com.liferay.analytics.settings.configuration.AnalyticsConfiguration;
import com.liferay.analytics.settings.configuration.AnalyticsConfigurationTracker;
import com.liferay.analytics.settings.security.constants.AnalyticsSecurityConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.CompanyService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.security.permission.PermissionCheckerUtil;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
public abstract class BaseAnalyticsClientImpl {

	protected CloseableHttpClient getCloseableHttpClient() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		httpClientBuilder.useSystemProperties();

		return httpClientBuilder.build();
	}

	protected abstract Log getLog();

	protected boolean isEnabled(long companyId) {
		if (!analyticsConfigurationTracker.isActive()) {
			_logDebug("Analytics Configuration Tracker is not active");

			return false;
		}

		AnalyticsConfiguration analyticsConfiguration =
			analyticsConfigurationTracker.getAnalyticsConfiguration(companyId);

		if (analyticsConfiguration.liferayAnalyticsEndpointURL() == null) {
			_logDebug("Analytics endpoint URL is null");

			return false;
		}

		return true;
	}

	protected void processInvalidTokenMessage(long companyId, String message) {
		if (message.equals("INVALID_TOKEN")) {
			_logWarn(
				StringBundler.concat(
					"Disconnecting data source for company ", companyId,
					". Cause: ", message));

			_disconnectDataSource(companyId);

			analyticsMessageLocalService.deleteAnalyticsMessages(companyId);

			_logInfo("Deleted all analytics messages for company " + companyId);
		}
	}

	@Reference
	protected AnalyticsConfigurationTracker analyticsConfigurationTracker;

	@Reference
	protected AnalyticsMessageLocalService analyticsMessageLocalService;

	@Reference
	protected CompanyService companyService;

	@Reference
	protected ConfigurationProvider configurationProvider;

	@Reference
	protected UserLocalService userLocalService;

	private void _disconnectDataSource(long companyId) {
		PermissionCheckerUtil.setThreadValues(
			userLocalService.fetchUserByScreenName(
				companyId,
				AnalyticsSecurityConstants.SCREEN_NAME_ANALYTICS_ADMIN));

		UnicodeProperties unicodeProperties = new UnicodeProperties(true);

		unicodeProperties.setProperty("liferayAnalyticsConnectionType", "");
		unicodeProperties.setProperty("liferayAnalyticsDataSourceId", "");
		unicodeProperties.setProperty("liferayAnalyticsEndpointURL", "");
		unicodeProperties.setProperty(
			"liferayAnalyticsFaroBackendSecuritySignature", "");
		unicodeProperties.setProperty("liferayAnalyticsFaroBackendURL", "");
		unicodeProperties.setProperty("liferayAnalyticsGroupIds", "");
		unicodeProperties.setProperty("liferayAnalyticsURL", "");

		try {
			companyService.updatePreferences(companyId, unicodeProperties);
		}
		catch (Exception exception) {
			_logWarn(
				"Unable to remove analytics preferences for company " +
					companyId,
				exception);
		}

		try {
			configurationProvider.deleteCompanyConfiguration(
				AnalyticsConfiguration.class, companyId);
		}
		catch (Exception exception) {
			_logWarn(
				"Unable to remove analytics configuration for company " +
					companyId,
				exception);
		}
	}

	private void _logDebug(String message) {
		Log log = getLog();

		if (log.isDebugEnabled()) {
			log.debug(message);
		}
	}

	private void _logInfo(String message) {
		Log log = getLog();

		if (log.isInfoEnabled()) {
			log.info(message);
		}
	}

	private void _logWarn(Object message) {
		Log log = getLog();

		if (log.isWarnEnabled()) {
			log.warn(message);
		}
	}

	private void _logWarn(Object message, Throwable throwable) {
		Log log = getLog();

		if (log.isWarnEnabled()) {
			log.warn(message, throwable);
		}
	}

}