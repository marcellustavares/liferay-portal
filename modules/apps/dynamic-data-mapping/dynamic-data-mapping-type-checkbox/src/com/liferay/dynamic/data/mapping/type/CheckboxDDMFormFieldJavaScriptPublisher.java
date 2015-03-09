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

package com.liferay.dynamic.data.mapping.type;

import com.liferay.portal.kernel.util.HashMapDictionary;

import java.net.URL;

import java.util.Dictionary;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

/**
 * @author Marcellus Tavares
 */
@Component(
	property = {
		"osgi.http.whiteboard.resource.pattern=/ddm/checkbox/js/*",
		"osgi.http.whiteboard.resource.prefix=/META-INF/resources",
		"osgi.http.whiteboard.servlet.pattern=/ddm/checkbox/js/*"
	},
	immediate = true, service = Servlet.class
)
public class CheckboxDDMFormFieldJavaScriptPublisher extends HttpServlet {

	@Activate
	protected void activate(BundleContext bundleContext) {
		Dictionary<String, Object> properties = new HashMapDictionary<>();

		Bundle bundle = bundleContext.getBundle();

		ServletContextHelper servletContextHelper =
			new ServletContextHelper(bundle) {

				@Override
				public URL getResource(String name) {
					return super.getResource(name);
				}

			};

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
			bundle.getSymbolicName());
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH,
			"/" + bundle.getSymbolicName());

		_servletContextHelperServiceRegistration =
			bundleContext.registerService(
				ServletContextHelper.class, servletContextHelper, properties);
	}

	@Deactivate
	protected void deactivate() {
		_servletContextHelperServiceRegistration.unregister();
	}

	private ServiceRegistration<ServletContextHelper>
		_servletContextHelperServiceRegistration;

}