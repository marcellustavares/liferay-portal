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

package com.liferay.osgi.config.admin.portlet.internal.freemarker;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.JSPSupportServlet;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.freemarker.FreeMarkerTaglibFactoryUtil;

import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import org.osgi.service.component.annotations.Component;

/**
 * @author Kamesh Sampath
 */
@Component(service = TemplateTaglibSupportProvider.class)
public class OsgiFreeMarkerTaglibSupportProvider
	implements TemplateTaglibSupportProvider {

	@Override
	public void addTaglibSupport(
		Template template, ServletContext servletContext,
		PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {
		if (_log.isInfoEnabled()) {
			_log.info("Adding OSGI Taglib support");
		}

		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			portletRequest);

		template.prepare(request);

		template.put(
			"fullTemplatesPath", "META-INF/resources");

		GenericServlet freemarkerOsgiServlet =
			new JSPSupportServlet(servletContext);

		ServletContextHashModel servletContextHashModel =
			new ServletContextHashModel(
				freemarkerOsgiServlet, ObjectWrapper.DEFAULT_WRAPPER);

		template.put("Application", servletContextHashModel);

		TemplateHashModel taglibsFactory =
			FreeMarkerTaglibFactoryUtil.createTaglibFactory(servletContext);

		template.put("PortletJspTagLibs", taglibsFactory);

		HttpServletRequestWrapper httpServletRequestWrapper =
			new HttpServletRequestWrapper(
				PortalUtil.getHttpServletRequest(portletRequest));
		HttpServletResponseWrapper httpServletResponseWrapper =
			new HttpServletResponseWrapper(
				PortalUtil.getHttpServletResponse(portletResponse));

		HttpRequestHashModel httpRequestHashModel =
			new HttpRequestHashModel(
				httpServletRequestWrapper, httpServletResponseWrapper,
				ObjectWrapper.DEFAULT_WRAPPER);

		template.put("Request", httpRequestHashModel);
	}

	private static Log _log =
		LogFactoryUtil.getLog(OsgiFreeMarkerTaglibSupportProvider.class);

}
