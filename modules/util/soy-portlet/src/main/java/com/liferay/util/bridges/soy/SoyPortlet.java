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

package com.liferay.util.bridges.soy;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoaderUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;

import java.io.IOException;
import java.io.Writer;

import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Miroslav Ligas
 */
public class SoyPortlet extends MVCPortlet {

	public static final String SOY_FILE_SUFFIX = "*.soy";

	@Override
	public void init() throws PortletException {
		super.init();

		_bundle = FrameworkUtil.getBundle(
			this.getClass()).getBundleContext().getBundle();
	}

	@Override
	protected void include(
			String namespace, PortletRequest portletRequest,
			PortletResponse portletResponse, String lifecycle)
		throws IOException, PortletException {

		try {
			Enumeration<URL> resources = _getResourcesFormPath(templatePath);

			if (resources == null) {
				throw new PortletException(
					"Could not find soy template on path: " + templatePath);
			}

			List<TemplateResource> templateResources = _getTemplateResources(
				resources);

			Template template = TemplateManagerUtil.getTemplates(
				TemplateConstants.LANG_TYPE_SOY, templateResources, false);

			template.put(TemplateConstants.NAMESPACE, namespace);

			Writer writer = null;

			if (portletResponse instanceof MimeResponse) {
				MimeResponse mimeResponse = (MimeResponse)portletResponse;

				writer = UnsyncPrintWriterPool.borrow(mimeResponse.getWriter());
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

	private Enumeration<URL> _getResourcesFormPath(String path) {
		return _bundle.findEntries(path, SOY_FILE_SUFFIX, true);
	}

	private List<TemplateResource> _getTemplateResources(Enumeration<URL> urls)
		throws TemplateException {

		List<TemplateResource> result = new ArrayList<>();

		long bundleId = _bundle.getBundleId();

		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();

			String templatePath =
				bundleId + StringPool.DOUBLE_DASH + url.getPath();

			TemplateResource templateResource =
				TemplateResourceLoaderUtil.getTemplateResource(
					TemplateConstants.LANG_TYPE_SOY, templatePath);

			result.add(templateResource);
		}

		return result;
	}

	private static final Log _log = LogFactoryUtil.getLog(SoyPortlet.class);

	private Bundle _bundle;

}