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

package com.liferay.dynamic.data.lists.web.context.util;

import com.liferay.dynamic.data.lists.configuration.DDLServiceConfiguration;
import com.liferay.dynamic.data.lists.constants.DDLConstants;
import com.liferay.portal.kernel.display.context.util.BaseRequestHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.configuration.ConfigurationFactoryUtil;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ParameterMapSettingsLocator;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lino Alves
 */
public class DDLWebRequestHelper extends BaseRequestHelper {

	public DDLWebRequestHelper(HttpServletRequest request) {
		super(request);
	}

	public DDLServiceConfiguration getDDLServiceConfiguration() {
		try {
			if (_ddlServiceConfiguration == null) {
				if (Validator.isNotNull(getPortletResource())) {
					HttpServletRequest request = getRequest();

					_ddlServiceConfiguration =
						ConfigurationFactoryUtil.getConfiguration(
							DDLServiceConfiguration.class,
						new ParameterMapSettingsLocator(
							request.getParameterMap(),
							new GroupServiceSettingsLocator(
								getSiteGroupId(), DDLConstants.SERVICE_NAME)));
				}
				else {
					_ddlServiceConfiguration =
						ConfigurationFactoryUtil.getConfiguration(
							DDLServiceConfiguration.class,
							new GroupServiceSettingsLocator(
								getSiteGroupId(), DDLConstants.SERVICE_NAME));
				}
			}

			return _ddlServiceConfiguration;
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	private DDLServiceConfiguration _ddlServiceConfiguration;

}