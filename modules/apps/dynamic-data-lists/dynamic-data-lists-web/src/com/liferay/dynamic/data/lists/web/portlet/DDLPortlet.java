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

package com.liferay.dynamic.data.lists.web.portlet;

import com.liferay.dynamic.data.lists.web.constants.FormsPortletKeys;
import com.liferay.dynamic.data.lists.web.display.FormsRendererHelper;
import com.liferay.dynamic.data.lists.web.display.FormsRequestHelper;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormFieldTypesJSONSerializer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.PortletApp;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatalists.NoSuchRecordSetException;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.control-panel-entry-category=content",
		"com.liferay.portlet.control-panel-entry-weight=4.0",
		"com.liferay.portlet.css-class-wrapper=portlet-forms",
		"com.liferay.portlet.display-category=category.content",
		"com.liferay.portlet.footer-portlet-javascript=/js/modules.js",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=false",
		"com.liferay.portlet.render-weight=0",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Forms", "javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.config-template=/configuration.jsp",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + FormsPortletKeys.FORMS,
		"javax.portlet.resource-bundle=content.Language"
	},
	service = Portlet.class
)
public class DDLPortlet extends MVCPortlet {

	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		super.doView(renderRequest, renderResponse);
	}

	@Override
	public void init(PortletConfig portletConfig) throws PortletException {
		super.init(portletConfig);

		LiferayPortletConfig liferayPortletConfig =
			(LiferayPortletConfig)portletConfig;

		com.liferay.portal.model.Portlet portlet =
			liferayPortletConfig.getPortlet();

		PortletApp portletApp = portlet.getPortletApp();

		ServletContextPool.put(
			portletApp.getServletContextName(), portletApp.getServletContext());
	}

	@Override
	protected void include(
			String path, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws IOException, PortletException {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(renderRequest);

		FormsRequestHelper formsRequestHelper = new FormsRequestHelper(
			httpServletRequest);

		formsRequestHelper.setDDMFormFieldTypesJSONSerializer(
			_ddmFormFieldTypesJSONSerializer);

		renderRequest.setAttribute("formsRequestHelper", formsRequestHelper);

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (path.equals("/view_form.jsp")) {
			long recordSetId = ParamUtil.getLong(renderRequest, "recordSetId");

			long recordId = ParamUtil.getLong(renderRequest, "recordId");

			String formHTML = StringPool.BLANK;

			FormsRendererHelper formsRendererHelper = new FormsRendererHelper();

			try {
				formHTML = formsRendererHelper.render(
					recordSetId, recordId, renderResponse.getNamespace(),
					themeDisplay.getLocale());
			}
			catch (PortalException e) {
				e.printStackTrace();
			}

			renderRequest.setAttribute("formHTML", formHTML);
		}
		else if (path.equals("/view_records.jsp")) {
			long recordSetId = ParamUtil.getLong(renderRequest, "recordSetId");

			DDLRecordSet recordSet = null;

			try {
				if (Validator.isNotNull(recordSetId)) {
					recordSet = DDLRecordSetLocalServiceUtil.getRecordSet(
						recordSetId);
				}
			}
			catch (NoSuchRecordSetException nsrse) {
				nsrse.printStackTrace();
			}
			catch (PortalException e) {
				e.printStackTrace();
			}

			renderRequest.setAttribute(FormsPortletKeys.RECORD_SET, recordSet);
		}

		include(
			path, renderRequest, renderResponse, PortletRequest.RENDER_PHASE);
	}

	@Reference
	protected void setDDMFormFieldTypesJSONSerializer(
		DDMFormFieldTypesJSONSerializer ddmFormFieldTypesJSONSerializer) {

		_ddmFormFieldTypesJSONSerializer = ddmFormFieldTypesJSONSerializer;
	}

	private DDMFormFieldTypesJSONSerializer _ddmFormFieldTypesJSONSerializer;

}