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

package com.liferay.analytics.dxp.entity.internal.exporter.dispatch.messaging;

import com.liferay.analytics.dxp.entity.exporter.constants.AnalyticsDXPEntityDispatchTriggerDestinationNames;
import com.liferay.analytics.dxp.entity.exporter.constants.AnalyticsDXPEntityDispatchTriggerProcessorCommand;
import com.liferay.analytics.dxp.entity.exporter.helper.AnalyticsDXPEntityDispatchTriggerHelper;
import com.liferay.analytics.settings.configuration.AnalyticsConfigurationTracker;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcos Martins
 */
@Component(
	immediate = true,
	property = "destination.name=" + AnalyticsDXPEntityDispatchTriggerDestinationNames.ANALYTICS_DXP_ENTITY_DISPATCH_TRIGGER_PROCESSOR,
	service = MessageListener.class
)
public class AddAnalyticsDXPEntityDispatchTriggerMessageListener
	extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		if (!_analyticsConfigurationTracker.isActive() ||
			!Objects.equals(
				message.get("command"),
				AnalyticsDXPEntityDispatchTriggerProcessorCommand.ADD)) {

			return;
		}

		_analyticsDXPEntityDispatchTriggerHelper.addDispatchTriggers(
			message.getLong("companyId"));
	}

	@Reference
	private AnalyticsConfigurationTracker _analyticsConfigurationTracker;

	@Reference
	private AnalyticsDXPEntityDispatchTriggerHelper
		_analyticsDXPEntityDispatchTriggerHelper;

}