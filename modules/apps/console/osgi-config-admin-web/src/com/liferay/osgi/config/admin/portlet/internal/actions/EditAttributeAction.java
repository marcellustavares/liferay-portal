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

package com.liferay.osgi.config.admin.portlet.internal.actions;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.ActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.MetaTypeService;

/**
 * @author Kamesh Sampath
 */

@Component(immediate = true, service = ActionCommand.class,
				property = {
					"action.command.name=editAttributes",
					"javax.portlet.name=com_liferay_osgi_config_admin_portlet_LiferayOsgiConfigAdminPortlet"
				})
public class EditAttributeAction implements ActionCommand {

	@Activate
	public void activate(ComponentContext context) {
		_context = context;
	}

	@Override
	public boolean processCommand(
		PortletRequest portletRequest, PortletResponse portletResponse)
		throws PortletException {
		String servicePID = ParamUtil.getString(portletRequest, "servicePID");

		if (_log.isInfoEnabled()) {
			_log.info("Editing service with PID:" + servicePID);
		}

		return true;
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

	private ConfigurationAdmin _configurationAdmin;
	private ComponentContext _context;
	private MetaTypeService _metaTypeService;

	private static Log _log = LogFactoryUtil.getLog(EditAttributeAction.class);
}
