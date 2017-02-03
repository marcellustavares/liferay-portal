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

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

/**
 * @author Inácio Nery
 */
public class DynamicIncludeResourcesUtil {

	public static ServletContext getPathServletContext(String path) {
		for (ServletContext servletContext :
				_instance._servletContexts.values()) {

			if (path.startsWith(servletContext.getContextPath())) {
				return servletContext;
			}
		}

		return null;
	}

	public static URL getResource(ServletContext servletContext, String path) {
		path = PortalWebResourcesUtil.stripContextPath(servletContext, path);

		try {
			URL url = servletContext.getResource(path);

			if (url != null) {
				return url;
			}
		}
		catch (MalformedURLException murle) {
		}

		return null;
	}

	public static URL getResource(String path) {
		ServletContext servletContext = getPathServletContext(path);

		if (servletContext != null) {
			return getResource(servletContext, path);
		}

		return null;
	}

	private DynamicIncludeResourcesUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			DynamicInclude.class,
			new DynamicIncludeResourcesServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private static final DynamicIncludeResourcesUtil _instance =
		new DynamicIncludeResourcesUtil();

	private final ServiceTracker<DynamicInclude, DynamicInclude>
		_serviceTracker;
	private final Map<ServiceReference<DynamicInclude>, ServletContext>
		_servletContexts = new ConcurrentHashMap<>();

	private class DynamicIncludeResourcesServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<DynamicInclude, DynamicInclude> {

		@Override
		public DynamicInclude addingService(
			ServiceReference<DynamicInclude> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			DynamicInclude dynamicInclude = registry.getService(
				serviceReference);

			ServletContext servletContext = dynamicInclude.getServletContext();

			if (servletContext != null) {
				_servletContexts.put(serviceReference, servletContext);
			}

			return dynamicInclude;
		}

		@Override
		public void modifiedService(
			ServiceReference<DynamicInclude> serviceReference,
			DynamicInclude dynamicInclude) {
		}

		@Override
		public void removedService(
			ServiceReference<DynamicInclude> serviceReference,
			DynamicInclude dynamicInclude) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			_servletContexts.remove(serviceReference);
		}

	}

}