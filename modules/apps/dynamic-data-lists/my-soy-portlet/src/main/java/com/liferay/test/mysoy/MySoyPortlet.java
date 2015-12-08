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

package com.liferay.test.mysoy;

import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.soy.SoyPortlet;

import javax.portlet.Portlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.application-type=full-page-application",
		"com.liferay.portlet.application-type=widget",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.scopeable=true",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=My Soy Portlet",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.copy-request-parameters=true",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=mysoyportlet.view",
		"javax.portlet.name=my_soy_portlet",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=guest,power-user,user",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class MySoyPortlet extends SoyPortlet {

	@Override
	protected void doInclude(
		PortletRequest portletRequest, PortletResponse portletResponse,
		Template template) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String mvcPath = ParamUtil.getString(portletRequest, "mvcPath");

		if (mvcPath.equals("alo")) {
			template.put(TemplateConstants.NAMESPACE, "mysoyportlet.alo");
		}
		else {
			LiferayPortletResponse liferayPortletResponse =
				PortalUtil.getLiferayPortletResponse(portletResponse);

			LiferayPortletURL liferayPortletURL =
				liferayPortletResponse.createLiferayPortletURL(
						themeDisplay.getPlid(),
						themeDisplay.getPortletDisplay().getPortletName(),
						themeDisplay.getLifecycle());

			liferayPortletURL.setParameter("mvcPath", "alo");

			template.put("userName", themeDisplay.getUser().getFirstName());
			template.put("link", liferayPortletURL.toString());
		}
	}

}