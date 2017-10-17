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

package com.liferay.portal.analytics.configuration;

import aQute.bnd.annotation.ProviderType;
import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Marcellus Tavares
 */
@ExtendedObjectClassDefinition(category = "foundation")
@Meta.OCD(
	id = "com.liferay.portal.analytics.configuration.AnalyticsMessagingConfiguration",
	localization = "content/Language",
	name = "portal-analytics-configuration-name"
)
@ProviderType
public interface AnalyticsMessagingConfiguration {

	@Meta.AD(
		deflt = "2", description = "destination-workers-core-size-description",
		name = "destination-workers-core-size-name", required = false
	)
	public int destinationWorkersCoreSize();

	@Meta.AD(
		deflt = "5", description = "destination-workers-max-size-description",
		name = "destination-workers-max-size-name", required = false
	)
	public int destinationWorkersMaxSize();

	@Meta.AD(
		deflt = "500", description = "destination-queue-max-size-description",
		name = "destination-queue-max-size-name", required = false
	)
	public int destinationQueueMaxSize();

}