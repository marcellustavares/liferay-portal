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

package com.liferay.portal.analytics.internal.messaging;

import com.liferay.portal.analytics.configuration.AnalyticsMessagingConfiguration;
import com.liferay.portal.analytics.constants.AnalyticsDestinationNames;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.concurrent.CallerRunsPolicy;
import com.liferay.portal.kernel.concurrent.RejectedExecutionHandler;
import com.liferay.portal.kernel.concurrent.ThreadPoolExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.util.HashMapDictionary;

import java.util.Dictionary;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	configurationPid = "com.liferay.portal.analytics.configuration.AnalyticsMessagingConfiguration",
	immediate = true
)
public class AnalyticsMessagingConfigurator {

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		AnalyticsMessagingConfiguration portalAnalyticsMessagingConfiguration =
			ConfigurableUtil.createConfigurable(
				AnalyticsMessagingConfiguration.class, properties);

		int destinationQueueMaxSize = getDestinationQueueMaxSize(
			portalAnalyticsMessagingConfiguration.destinationQueueMaxSize());

		int destinationWorkersCoreSize = getDestinationWorkersCoreSize(
			portalAnalyticsMessagingConfiguration.destinationWorkersCoreSize(),
			portalAnalyticsMessagingConfiguration.destinationWorkersMaxSize());

		int destinationWorkersMaxSize = getDestinationWorkersMaxSize(
			portalAnalyticsMessagingConfiguration.destinationWorkersCoreSize(),
			portalAnalyticsMessagingConfiguration.destinationWorkersMaxSize());

		registerPortalAnalyticsDestination(
			destinationQueueMaxSize, destinationWorkersCoreSize,
			destinationWorkersMaxSize);
	}

	@Deactivate
	protected void deactivate() {
		unregisterPortalAnalyticsDestination();

		_bundleContext = null;
	}

	protected int getDestinationQueueMaxSize(int destinationQueueMaxSize) {
		if (destinationQueueMaxSize > 0) {
			return destinationQueueMaxSize;
		}

		return _DEFAULT_DESTINATION_QUEUE_MAX_SIZE;
	}

	protected int getDestinationWorkersCoreSize(
		int destinationWorkersCoreSize, int destinationWorkersMaxSize) {

		if ((destinationWorkersCoreSize > 0) &&
			(destinationWorkersMaxSize > destinationWorkersCoreSize)) {

			return destinationWorkersCoreSize;
		}

		return _DEFAULT_DESTINATION_WORKERS_CORE_SIZE;
	}

	protected int getDestinationWorkersMaxSize(
		int destinationWorkersCoreSize, int destinationWorkersMaxSize) {

		if ((destinationWorkersMaxSize > 0) &&
			(destinationWorkersMaxSize > destinationWorkersCoreSize)) {

			return destinationWorkersMaxSize;
		}

		return _DEFAULT_DESTINATION_WORKERS_MAX_SIZE;
	}

	protected void registerPortalAnalyticsDestination(
		int queueMaxSize, int workersCoreSize, int workersMaxSize) {

		DestinationConfiguration destinationConfiguration =
			DestinationConfiguration.createParallelDestinationConfiguration(
				AnalyticsDestinationNames.ANALYTICS);

		setDestinationConfigurationProperties(
			destinationConfiguration, queueMaxSize, workersCoreSize,
			workersMaxSize);

		Destination destination = _destinationFactory.createDestination(
			destinationConfiguration);

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put("destination.name", destination.getName());

		_serviceRegistration = _bundleContext.registerService(
			Destination.class, destination, properties);
	}

	protected void setDestinationConfigurationProperties(
		DestinationConfiguration destinationConfiguration, int queueMaxSize,
		int workersCoreSize, int workersMaxSize) {

		destinationConfiguration.setMaximumQueueSize(queueMaxSize);
		destinationConfiguration.setWorkersCoreSize(workersCoreSize);
		destinationConfiguration.setWorkersMaxSize(workersMaxSize);

		RejectedExecutionHandler rejectedExecutionHandler =
			new CallerRunsPolicy() {

				@Override
				public void rejectedExecution(
					Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

					if (_log.isWarnEnabled()) {
						_log.warn(
							"The current thread will handle the request " +
								"because the analytics task queue is at its " +
									"maximum capacity");
					}

					super.rejectedExecution(runnable, threadPoolExecutor);
				}

			};

		destinationConfiguration.setRejectedExecutionHandler(
			rejectedExecutionHandler);
	}

	protected void unregisterPortalAnalyticsDestination() {
		Destination destination = _bundleContext.getService(
			_serviceRegistration.getReference());

		destination.destroy();

		_serviceRegistration.unregister();

		_serviceRegistration = null;
	}

	private static final int _DEFAULT_DESTINATION_QUEUE_MAX_SIZE = 500;

	private static final int _DEFAULT_DESTINATION_WORKERS_CORE_SIZE = 2;

	private static final int _DEFAULT_DESTINATION_WORKERS_MAX_SIZE = 5;

	private static final Log _log = LogFactoryUtil.getLog(
		AnalyticsMessagingConfigurator.class);

	private BundleContext _bundleContext;

	@Reference
	private DestinationFactory _destinationFactory;

	private ServiceRegistration<Destination> _serviceRegistration;

}