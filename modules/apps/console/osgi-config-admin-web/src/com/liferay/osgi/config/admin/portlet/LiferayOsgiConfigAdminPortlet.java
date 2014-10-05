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

package com.liferay.osgi.config.admin.portlet;

import com.liferay.osgi.config.admin.util.MetaTypeInfoUtil;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoaderUtil;
import com.liferay.portal.kernel.template.TemplateTaglibSupportProvider;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.util.PortalUtil;

import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.ObjectWrapper;

import java.io.IOException;
import java.io.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.MimeResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

@Component(
				immediate = true,
				property = {
					"com.liferay.portlet.control-panel-entry-category=configuration",
					"com.liferay.portlet.control-panel-entry-weight=11",
					"com.liferay.portlet.display-category=category.hidden",
					"com.liferay.portlet.instanceable=false",
					"javax.portlet.init-param.template-path=/",
					"javax.portlet.init-param.view-template=/view.ftl",
					"javax.portlet.resource-bundle=content.Language",
					"javax.portlet.security-role-ref=power-user,user"
				},
				service = Portlet.class)
public class LiferayOsgiConfigAdminPortlet
	extends com.liferay.util.bridges.freemarker.FreeMarkerPortlet {

	@Activate
	public void activate(BundleContext context) {
		_context = context;
	}

	protected Map<String, Object> cloneTemplateContext(Template template) {
		Map<String, Object> context = new HashMap<String, Object>();

		for (String key : template.getKeys()) {
			context.put(key, template.get(key));
		}

		return context;
	}

	@Override
	protected void include(
		String path, PortletRequest portletRequest,
		PortletResponse portletResponse, String lifecycle)
		throws IOException, PortletException {

		PortletContext portletContext = getPortletContext();

		String servletContextName = portletContext.getPortletContextName();

		String resourcePath = servletContextName.concat(
			TemplateConstants.SERVLET_SEPARATOR).concat(path);

		boolean resourceExists = false;

		try {
			resourceExists = TemplateResourceLoaderUtil.hasTemplateResource(
				TemplateConstants.LANG_TYPE_FTL, resourcePath);
		}
		catch (TemplateException te) {
			throw new IOException(te.getMessage());
		}

		if (!resourceExists) {
			_log.error(path + " is not a valid include");
		}
		else {
			try {
				TemplateResource templateResource =
					TemplateResourceLoaderUtil.getTemplateResource(
						TemplateConstants.LANG_TYPE_FTL, resourcePath);

				Template template = TemplateManagerUtil.getTemplate(
					TemplateConstants.LANG_TYPE_FTL, templateResource, false);

				TemplateTaglibSupportProvider templateTaglibSupportProvider =
					getTaglibSupportProvider();

				if (templateTaglibSupportProvider != null) {
					templateTaglibSupportProvider.addTaglibSupport(
						template, servletContextName, portletRequest,
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
		template.put(
			"listOfObjectclassDefinitions", _objectDefinitions());
	}

	@Reference
	protected void setConfigAdminService(
		ConfigurationAdmin configurationAdmin) {

		_configurationAdmin = configurationAdmin;
	}

	@Reference
	protected void setMetaTypeService(MetaTypeService metaTypeService) {
		_metaTypeService = metaTypeService;
	}

	private List<ObjectClassDefinition> _objectDefinitions() {
		Bundle[] bundles = _context.getBundles();

		List<ObjectClassDefinition> ocdContainer =
			new ArrayList<ObjectClassDefinition>();

		for (Bundle bundle : bundles) {
			MetaTypeInformation mInfo =
				_metaTypeService.getMetaTypeInformation(
					bundle);

			if (mInfo != null) {
				String[] pids = mInfo.getPids();

				MetaTypeInfoUtil.fillOCD(mInfo, ocdContainer, pids);

				String[] factoryPids = mInfo.getFactoryPids();

				MetaTypeInfoUtil.fillOCD(mInfo, ocdContainer, factoryPids);
			}
		}

		return ocdContainer;
	}

	private static Log _log = LogFactoryUtil.getLog(
		LiferayOsgiConfigAdminPortlet.class);

	private ConfigurationAdmin _configurationAdmin;
	private BundleContext _context;
	private MetaTypeService _metaTypeService;

}