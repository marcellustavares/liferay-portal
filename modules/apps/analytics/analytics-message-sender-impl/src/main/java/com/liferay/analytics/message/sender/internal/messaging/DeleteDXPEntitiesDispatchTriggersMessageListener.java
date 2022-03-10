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

package com.liferay.analytics.message.sender.internal.messaging;

import com.liferay.analytics.message.sender.constants.DXPEntitiesDispatchTriggerProcessorCommand;
import com.liferay.analytics.message.sender.constants.DXPEntitiesDispatchTriggersDestinantionNames;
import com.liferay.analytics.message.sender.helper.DXPEntityDispatchTriggerHelper;
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
	property = "destination.name=" + DXPEntitiesDispatchTriggersDestinantionNames.DXP_ENTITIES_DISPATCH_TRIGGER_PROCESSOR,
	service = MessageListener.class
)
public class DeleteDXPEntitiesDispatchTriggersMessageListener
	extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		Object object = message.get("command");

		if (!_analyticsConfigurationTracker.isActive() ||
			!Objects.equals(
				object, DXPEntitiesDispatchTriggerProcessorCommand.DELETE)) {

			return;
		}

		_dxpEntityDispatchTriggerHelper.deleteDispatchTriggers(
			message.getLong("companyId"));
	}

	@Reference
	private AnalyticsConfigurationTracker _analyticsConfigurationTracker;

	@Reference
	private DXPEntityDispatchTriggerHelper _dxpEntityDispatchTriggerHelper;

}