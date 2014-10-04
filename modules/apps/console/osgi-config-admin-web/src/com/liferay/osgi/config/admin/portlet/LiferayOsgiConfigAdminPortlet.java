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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.util.bridges.freemarker.FreeMarkerPortlet;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
		"javax.portlet.init-param.view-template=/view.vm",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class LiferayOsgiConfigAdminPortlet extends FreeMarkerPortlet {

	@Activate
	public void activate(BundleContext context) {
		_context = context;
	}

	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		Bundle[] bundles = _context.getBundles();

		List<ObjectClassDefinition> ocdContainer =
			new ArrayList<ObjectClassDefinition>();

		for (Bundle bundle : bundles) {
			MetaTypeInformation mInfo = _metaTypeService.getMetaTypeInformation(
				bundle);

			if (mInfo != null) {
				String[] pids = mInfo.getPids();

				MetaTypeInfoUtil.fillOCD(mInfo, ocdContainer, pids);

				String[] factoryPids = mInfo.getFactoryPids();

				MetaTypeInfoUtil.fillOCD(mInfo, ocdContainer, factoryPids);
			}
		}

		renderRequest.setAttribute("METATYPE_OCD_CONTAINER", ocdContainer);

		super.doView(renderRequest, renderResponse);
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