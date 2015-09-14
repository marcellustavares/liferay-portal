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

package com.liferay.portal.workflow.kaleo.designer.designer.portlet;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.permission.RolePermissionUtil;
import com.liferay.portal.service.permission.UserPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.workflow.kaleo.exception.DuplicateKaleoDraftDefinitionNameException;
import com.liferay.portal.workflow.kaleo.exception.KaleoDraftDefinitionContentException;
import com.liferay.portal.workflow.kaleo.exception.KaleoDraftDefinitionNameException;
import com.liferay.portal.workflow.kaleo.exception.NoSuchDraftDefinitionException;
import com.liferay.portal.workflow.kaleo.model.KaleoDraftDefinition;
import com.liferay.portal.workflow.kaleo.service.KaleoDraftDefinitionServiceUtil;
import com.liferay.portal.workflow.kaleo.designer.util.KaleoDesignerUtil;
import com.liferay.portal.workflow.kaleo.designer.util.WebKeys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eduardo Lundgren
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.autopropagated-parameters=availableFields",
		"com.liferay.portlet.autopropagated-parameters=availablePropertyModels",
		"com.liferay.portlet.autopropagated-parameters=kaleoProcessId",
		"com.liferay.portlet.autopropagated-parameters=openerWindowName",
		"com.liferay.portlet.autopropagated-parameters=portletResourceNamespace",
		"com.liferay.portlet.autopropagated-parameters=propertiesSaveCallback",
		"com.liferay.portlet.autopropagated-parameters=refreshOpenerOnClose",
		"com.liferay.portlet.autopropagated-parameters=saveCallback",
		"com.liferay.portlet.autopropagated-parameters=uiScope",
		"com.liferay.portlet.css-class-wrapper=kaleo-designer-portlet",
		"com.liferay.portlet.control-panel-entry-category=configuration",
		"com.liferay.portlet.control-panel-entry-weight=5.0",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.header-portlet-javascript=/js/main.js",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Kaleo Designer Web",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.copy-request-parameters=true",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=com_liferay_portal_workflow__kaleo_designer_designer_portlet_KaleoDesignerPortlet",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class KaleoDesignerPortlet extends MVCPortlet {

	public void deleteKaleoDraftDefinition(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String name = ParamUtil.getString(actionRequest, "name");
		int version = ParamUtil.getInteger(actionRequest, "version");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			actionRequest);

		KaleoDraftDefinitionServiceUtil.deleteKaleoDraftDefinitions(
			name, version, serviceContext);
	}

	public void publishKaleoDraftDefinition(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String content = null;

		try {
			String name = ParamUtil.getString(actionRequest, "name");
			Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
				actionRequest, "title");
			content = ParamUtil.getString(actionRequest, "content");

			if (Validator.isNull(name)) {
				name = getName(
					content, titleMap.get(themeDisplay.getSiteDefaultLocale()));
			}

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			KaleoDraftDefinition kaleoDraftDefinition =
				KaleoDraftDefinitionServiceUtil.publishKaleoDraftDefinition(
					themeDisplay.getUserId(), themeDisplay.getCompanyGroupId(),
					name, titleMap, content, serviceContext);

			actionRequest.setAttribute(
				WebKeys.KALEO_DRAFT_DEFINITION, kaleoDraftDefinition);
		}
		catch (Exception e) {
			if (isSessionErrorException(e)) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}

				SessionErrors.add(actionRequest, e.getClass(), e);

				actionRequest.setAttribute(
					WebKeys.KALEO_DRAFT_DEFINITION_CONTENT, content);
			}
			else {
				throw e;
			}
		}
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		if (!SessionErrors.contains(
				renderRequest,
				DuplicateKaleoDraftDefinitionNameException.class)) {

			try {
				KaleoDraftDefinition kaleoDraftDefinition =
					KaleoDesignerUtil.getKaleoDraftDefinition(renderRequest);

				renderRequest.setAttribute(
					WebKeys.KALEO_DRAFT_DEFINITION, kaleoDraftDefinition);
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		super.render(renderRequest, renderResponse);
	}

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws PortletException {

		try {
			String resourceID = resourceRequest.getResourceID();

			if (resourceID.equals("kaleoDraftDefinitions")) {
				serveKaleoDraftDefinitions(resourceRequest, resourceResponse);
			}
			else if (resourceID.equals("roles")) {
				serveRoles(resourceRequest, resourceResponse);
			}
			else if (resourceID.equals("users")) {
				serveUsers(resourceRequest, resourceResponse);
			}
			else {
				super.serveResource(resourceRequest, resourceResponse);
			}
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	public void updateKaleoDraftDefinition(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String content = null;

		try {
			long kaleoDraftDefinitionId = ParamUtil.getLong(
				actionRequest, "kaleoDraftDefinitionId");

			Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
				actionRequest, "title");
			content = ParamUtil.getString(actionRequest, "content");
			int version = ParamUtil.getInteger(actionRequest, "version");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			KaleoDraftDefinition kaleoDraftDefinition = null;

			if (kaleoDraftDefinitionId <= 0) {
				String name = getName(
					content, titleMap.get(themeDisplay.getSiteDefaultLocale()));

				kaleoDraftDefinition =
					KaleoDraftDefinitionServiceUtil.addKaleoDraftDefinition(
						themeDisplay.getUserId(),
						themeDisplay.getCompanyGroupId(), name, titleMap,
						content, version, 1, serviceContext);
			}
			else {
				String name = ParamUtil.getString(actionRequest, "name");

				kaleoDraftDefinition =
					KaleoDraftDefinitionServiceUtil.updateKaleoDraftDefinition(
						themeDisplay.getUserId(), name, titleMap, content,
						version, serviceContext);
			}

			actionRequest.setAttribute(
				WebKeys.KALEO_DRAFT_DEFINITION, kaleoDraftDefinition);
		}
		catch (Exception e) {
			if (isSessionErrorException(e)) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}

				SessionErrors.add(actionRequest, e.getClass(), e);

				actionRequest.setAttribute(
					WebKeys.KALEO_DRAFT_DEFINITION_CONTENT, content);
			}
			else {
				throw e;
			}
		}
	}

	protected String getName(String content, String defaultName) {
		if (Validator.isNull(content)) {
			return defaultName;
		}

		try {
			Document document = SAXReaderUtil.read(content);

			Element rootElement = document.getRootElement();

			return rootElement.elementText("name");
		}
		catch (DocumentException de) {
			return defaultName;
		}
	}

	protected Integer[] getRoleTypesObj(int type) {
		if ((type == RoleConstants.TYPE_ORGANIZATION) ||
			(type == RoleConstants.TYPE_REGULAR) ||
			(type == RoleConstants.TYPE_SITE)) {

			return new Integer[] {type};
		}
		else {
			return new Integer[0];
		}
	}

	@Override
	protected boolean isSessionErrorException(Throwable cause) {
		if (cause instanceof DuplicateKaleoDraftDefinitionNameException ||
			cause instanceof KaleoDraftDefinitionContentException ||
			cause instanceof KaleoDraftDefinitionNameException ||
			cause instanceof NoSuchDraftDefinitionException ||
			cause instanceof WorkflowException) {

			return true;
		}

		return false;
	}

	protected void serveKaleoDraftDefinitions(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String name = ParamUtil.getString(resourceRequest, "name");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		int draftVersion = ParamUtil.getInteger(
			resourceRequest, "draftVersion");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			resourceRequest);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (Validator.isNotNull(name) && (draftVersion > 0)) {
			KaleoDraftDefinition kaleoDraftDefinition =
				KaleoDraftDefinitionServiceUtil.getKaleoDraftDefinition(
					name, version, draftVersion, serviceContext);

			jsonObject.put("content", kaleoDraftDefinition.getContent());
			jsonObject.put(
				"draftVersion", kaleoDraftDefinition.getDraftVersion());
			jsonObject.put("name", kaleoDraftDefinition.getName());
			jsonObject.put(
				"title",
				kaleoDraftDefinition.getTitle(themeDisplay.getLocale()));
			jsonObject.put("version", kaleoDraftDefinition.getVersion());
		}

		writeJSON(resourceRequest, resourceResponse, jsonObject);
	}

	protected void serveRoles(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String keywords = ParamUtil.getString(resourceRequest, "keywords");
		int type = ParamUtil.getInteger(resourceRequest, "type");

		List<Role> roles = RoleLocalServiceUtil.search(
			themeDisplay.getCompanyId(), keywords, getRoleTypesObj(type), 0,
			SearchContainer.DEFAULT_DELTA, (OrderByComparator)null);

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (Role role : roles) {
			if (!RolePermissionUtil.contains(
					themeDisplay.getPermissionChecker(), role.getRoleId(),
					ActionKeys.VIEW)) {

				continue;
			}

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put("name", role.getName());
			jsonObject.put("roleId", role.getRoleId());

			jsonArray.put(jsonObject);
		}

		writeJSON(resourceRequest, resourceResponse, jsonArray);
	}

	protected void serveUsers(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String keywords = ParamUtil.getString(resourceRequest, "keywords");

		List<User> users = UserLocalServiceUtil.search(
			themeDisplay.getCompanyId(), keywords,
			WorkflowConstants.STATUS_APPROVED,
			new LinkedHashMap<String, Object>(), 0,
			SearchContainer.DEFAULT_DELTA, (OrderByComparator)null);

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (User user : users) {
			if (!UserPermissionUtil.contains(
					themeDisplay.getPermissionChecker(), user.getUserId(),
					ActionKeys.VIEW)) {

				continue;
			}

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put("emailAddress", user.getEmailAddress());
			jsonObject.put("fullName", user.getFullName());
			jsonObject.put("screenName", user.getScreenName());
			jsonObject.put("userId", user.getUserId());

			jsonArray.put(jsonObject);
		}

		writeJSON(resourceRequest, resourceResponse, jsonArray);
	}

	private static Log _log = LogFactoryUtil.getLog(KaleoDesignerPortlet.class);

}