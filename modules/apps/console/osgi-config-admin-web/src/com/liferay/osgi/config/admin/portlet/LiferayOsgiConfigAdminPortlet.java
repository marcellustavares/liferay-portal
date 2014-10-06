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
import com.liferay.osgi.config.admin.util.ObjectClassDefinitonsIterator;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.Template;

import javax.portlet.Portlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.MetaTypeService;

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
	extends OsgiFreeMarkerPortlet {

	@Activate
	public void activate(BundleContext context) {
		_context = context;
	}

	@Override
	protected void populateContext(
		String path, PortletRequest portletRequest,
		PortletResponse portletResponse, Template template)
		throws Exception {
		super.populateContext(path, portletRequest, portletResponse, template);
		template.put(
			"ocdIterator", new ObjectClassDefinitonsIterator(_context,_metaTypeService));
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



	private static Log _log = LogFactoryUtil.getLog(
		LiferayOsgiConfigAdminPortlet.class);

	private ConfigurationAdmin _configurationAdmin;
	private BundleContext _context;
	private MetaTypeService _metaTypeService;

}