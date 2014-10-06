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

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletBagPool;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.freemarker.FreeMarkerPortlet;

import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.ObjectWrapper;

import java.io.IOException;
import java.io.Writer;

import java.net.URL;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class OsgiFreeMarkerPortlet extends FreeMarkerPortlet {

	protected TemplateResource getTemplateResource(String templatePath) {

		if (templatePath.indexOf(StringPool.SLASH) != 0) {
			templatePath = StringPool.SLASH.concat(templatePath);
		}

		URL url = _bundle.getEntry("META-INF/resources".concat(templatePath));

		return new URLTemplateResource(templatePath, url);
	}

	@Override
	protected void include(
		String path, PortletRequest portletRequest,
		PortletResponse portletResponse, String lifecycle)
		throws IOException, PortletException {

		if (_bundle == null) {
			_bundle = FrameworkUtil.getBundle(getClass());
		}

		TemplateResource templateResource = getTemplateResource(path);

		if (templateResource == null) {
			_log.error("Unable to load template resource" + path);
		}

		try {
			Template template = TemplateManagerUtil.getTemplate(
				TemplateConstants.LANG_TYPE_FTL, templateResource, false);

			_servletContext =
				PortletBagPool.get(getPortletName()).getServletContext();

			TemplateTaglibSupportProvider templateTaglibSupportProvider =
				_taglibSupportProvider();

			_log.debug("Adding taglib support to context:" + _servletContext);

			if (templateTaglibSupportProvider != null) {
				templateTaglibSupportProvider.addTaglibSupport(
					template, _servletContext, portletRequest,
					portletResponse);
			}

			// LPS-43725

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

			populateContext(
				path, portletRequest, portletResponse, template);

			Writer writer = null;

			if (portletResponse instanceof MimeResponse) {
				MimeResponse mimeResponse = (MimeResponse)portletResponse;

				writer = UnsyncPrintWriterPool.borrow(
					mimeResponse.getWriter());
			}
			else {
				writer = new UnsyncStringWriter();
			}

			template.processTemplate(writer);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}

		if (clearRequestParameters) {
			if (lifecycle.equals(PortletRequest.RENDER_PHASE)) {
				portletResponse.setProperty("clear-request-parameters", "true");
			}
		}
	}

	protected void populateContext(
		String path, PortletRequest portletRequest,
		PortletResponse portletResponse, Template template)
		throws Exception {

		template.put("portletConfig", getPortletConfig());
		template.put("portletContext", getPortletContext());
		template.put("preferences", portletRequest.getPreferences());
		template.put(
			"userInfo", portletRequest.getAttribute(PortletRequest.USER_INFO));

		template.put("portletRequest", portletRequest);

		if (portletRequest instanceof ActionRequest) {
			template.put("actionRequest", portletRequest);
		}
		else if (portletRequest instanceof RenderRequest) {
			template.put("renderRequest", portletRequest);
		}
		else {
			template.put("resourceRequest", portletRequest);
		}

		template.put("portletResponse", portletResponse);

		if (portletResponse instanceof ActionResponse) {
			template.put("actionResponse", portletResponse);
		}
		else if (portletResponse instanceof RenderResponse) {
			template.put("renderResponse", portletResponse);
		}
		else {
			template.put("resourceResponse", portletResponse);
		}
	}

	protected ServletContext _servletContext;

	private TemplateTaglibSupportProvider _taglibSupportProvider() {

		ServiceReference<TemplateTaglibSupportProvider> serviceRef =
			_bundle.getBundleContext().getServiceReference(
				TemplateTaglibSupportProvider.class);

		return _bundle.getBundleContext().getService(serviceRef);
	}

	private static Log _log = LogFactoryUtil.getLog(
		OsgiFreeMarkerPortlet.class);
	private Bundle _bundle;

}