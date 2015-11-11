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

package com.liferay.dynamic.data.lists.form.web.portlet.configuration.icon;

import com.liferay.portal.kernel.portlet.configuration.icon.BasePortletConfigurationIcon;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.theme.PortletDisplay;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marcellus Tavares
 */
public class DDLRecordSetSettingsPortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	public DDLRecordSetSettingsPortletConfigurationIcon(
		HttpServletRequest request) {

		super(request);
	}

	@Override
	public String getMessage() {
		return "settings";
	}

	@Override
	public String getURL() {
		long recordSetId = getRecordSetId();

		return "javascript:Liferay.DDL.openSettings(" + String.valueOf(recordSetId) + ")";
	}

	@Override
	public boolean isShow() {
		return true;
	}

	@Override
	public boolean isToolTip() {
		return false;
	}

	@Override
	public boolean isUseDialog() {
		return false;
	}

	protected long getRecordSetId() {
		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		return ParamUtil.getLong(
			request, portletDisplay.getNamespace() + "recordSetId");
	}

}