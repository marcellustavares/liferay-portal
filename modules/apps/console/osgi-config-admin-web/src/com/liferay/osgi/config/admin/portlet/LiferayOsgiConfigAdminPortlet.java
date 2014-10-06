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

import com.liferay.osgi.config.admin.portlet.internal.freemarker.OsgiFreeMarkerPortlet;
import com.liferay.osgi.config.admin.util.MetaTypeInfoUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.Template;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * @author Kamesh Sampath
 */
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
	extends OsgiFreeMarkerPortlet implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		_servletContext = null;

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		_servletContext = servletContextEvent.getServletContext();
	}

	@Activate
	public void activate(BundleContext context) {
		_context = context;

		Bundle bundle = context.getBundle();

		Dictionary<String, Object> properties = new Hashtable<String, Object>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
			"osgi-configadmin-web");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH,
			"/osgi-configadmin-web");

		ServletContextHelper servletContextHelper =
			new ServletContextHelper(bundle) {
			};

		_context.registerService(
			ServletContextHelper.class, servletContextHelper, properties);
	}

	@Override
	protected void populateContext(
		String path, PortletRequest portletRequest,
		PortletResponse portletResponse, Template template)
		throws Exception {
		super.populateContext(path, portletRequest, portletResponse, template);
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