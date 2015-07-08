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

package com.liferay.taglib.ddm;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplateUtil;
import com.liferay.taglib.ddm.base.BaseTemplateSelectorTag;

/**
 * @author Juan Fernández
 */
public class TemplateSelectorTag extends BaseTemplateSelectorTag {

	@Override
	protected void cleanUp() {
		super.cleanUp();

		setDefaultDisplayStyle(StringPool.BLANK);
		setLabel("display-template");
	}

	@Override
	public String getDisplayStyle() {
		String displayStyle = getDisplayStyle();

		if (Validator.isNull(displayStyle)) {
			displayStyle = getDefaultDisplayStyle();
		}

		DDMTemplate portletDisplayDDMTemplate = getPortletDisplayDDMTemplate(
			displayStyle);

		if (Validator.isNull(displayStyle) &&
			(portletDisplayDDMTemplate != null)) {

			displayStyle = PortletDisplayTemplateUtil.getDisplayStyle(
				portletDisplayDDMTemplate.getTemplateKey());
		}

		return displayStyle;
	}

	@Override
	public long getDisplayStyleGroupId() {
		if (getDisplayStyleGroupId() > 0) {
			return getDisplayStyleGroupId();
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return themeDisplay.getScopeGroupId();
	}

	protected DDMTemplate getPortletDisplayDDMTemplate(String displayStyle) {
		DDMTemplate portletDisplayDDMTemplate =
			PortletDisplayTemplateUtil.getPortletDisplayTemplateDDMTemplate(
				getDisplayStyleGroupId(), PortalUtil.getClassNameId(
					getClassName()), displayStyle, true);

		return portletDisplayDDMTemplate;
	}

}