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

package com.liferay.portal.portlet.bridge.soy;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommandCache;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.servlet.MultiSessionMessages;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.portlet.bridge.soy.internal.SoyPortletHelper;
import com.liferay.portal.portlet.bridge.soy.internal.SoyPortletRequestFactory;
import com.liferay.portal.portlet.bridge.soy.internal.SoyPortletRouterHelper;
import com.liferay.portal.template.soy.utils.SoyContext;
import com.liferay.portal.template.soy.utils.SoyTemplateResourcesProvider;
import com.liferay.portlet.ActionRequestImpl;
import com.liferay.portlet.ActionResponseImpl;
import com.liferay.portlet.RenderRequestImpl;

import java.io.IOException;
import java.io.Writer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.MimeResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Miroslav Ligas
 * @author Bruno Basto
 */
public class SoyPortlet extends MVCPortlet {

	/**
	 * @deprecated As of 3.2.0, use {@link SoyPortlet#init(PortletConfig)}} instead
	 */
	@Deprecated
	@Override
	public void init() throws PortletException {
		super.init();
	}

	/**
	 * @throws PorletException
	 * @see MVCPortlet
	 */
	@Override
	public void init(PortletConfig portletConfig) throws PortletException {
		super.init(portletConfig);

		propagateRequestParameters = GetterUtil.getBoolean(
			getInitParameter("propagate-request-parameters"), true);

		_bundle = FrameworkUtil.getBundle(getClass());
		_portletConfig = portletConfig;

		try {
			MVCCommandCache mvcRenderCommandCache = getRenderMVCCommandCache();

			_soyPortletHelper = new SoyPortletHelper(
				_bundle, mvcRenderCommandCache);

			FriendlyURLMapper friendlyURLMapper = getFriendlyURLMapper();

			_metalPortletHelper = new SoyPortletRouterHelper(
				mvcRenderCommandCache, friendlyURLMapper);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	/**
	 * @throws IOExcpetion
	 * @throws PorletException
	 * @see MVCPortlet
	 */
	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		Template template = getTemplate(renderRequest);

		renderRequest.setAttribute(WebKeys.TEMPLATE, template);

		if (_isPjaxRequest(renderRequest)) {
			return;
		}

		super.render(renderRequest, renderResponse);
	}

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		HttpServletResponse response = PortalUtil.getHttpServletResponse(
			resourceResponse);

		Portlet portlet = getPortlet();

		try {
			if (isProcessAction(resourceRequest)) {
				_callProcessAction(
					resourceRequest, resourceResponse, response, portlet);

				return;
			}
			else {
				if (callResourceMethod(resourceRequest, resourceResponse)) {
					return;
				}

				_callRender(resourceRequest, resourceResponse, portlet);

				prepareTemplate(resourceRequest, resourceResponse);

				response.setContentType(ContentTypes.APPLICATION_JSON);

				Template template = getTemplate(resourceRequest);

				ServletResponseUtil.write(
					response, _metalPortletHelper.serializeTemplate(template));
			}
		}
		catch (Exception e) {
			_log.error("Error on the Serve Resource Phase", e);
		}
	}

	protected FriendlyURLMapper getFriendlyURLMapper() {
		Portlet portlet = getPortlet();

		return portlet.getFriendlyURLMapperInstance();
	}

	/**
	 * @deprecated As of 3.1.0
	 * @param path
	 * @return
	 */
	@Deprecated
	protected Set<String> getJavaScriptRequiredModules(String path) {
		try {
			Set<String> javaScriptRequiredModules = new HashSet<>();

			String javaScriptRequiredModule =
				_soyPortletHelper.getJavaScriptLoaderModule(path);

			javaScriptRequiredModules.add(javaScriptRequiredModule);

			return javaScriptRequiredModules;
		}
		catch (Exception e) {
			return Collections.emptySet();
		}
	}

	@Override
	protected String getPath(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		String path = super.getPath(portletRequest, portletResponse);

		if (Validator.isNull(path) || StringPool.SLASH.equals(path)) {
			return viewTemplate;
		}

		return path;
	}

	protected Portlet getPortlet() {
		LiferayPortletConfig liferayPortletConfig =
			(LiferayPortletConfig)_portletConfig;

		return liferayPortletConfig.getPortlet();
	}

	protected String getPortletComponentId(String portletNamespace) {
		return portletNamespace.concat("PortletComponent");
	}

	protected String getPortletWrapperId(String portletNamespace) {
		StringBundler sb = new StringBundler(3);

		sb.append(portletNamespace);
		sb.append(StringPool.UNDERLINE);
		sb.append("SoyWrapper");

		return sb.toString();
	}

	/**
	 * @param portletResponse
	 * @return
	 * @throws IOException
	 */
	protected Writer getResponseWriter(PortletResponse portletResponse)
		throws IOException {

		Writer writer = null;

		if (portletResponse instanceof MimeResponse) {
			MimeResponse mimeResponse = (MimeResponse)portletResponse;

			writer = UnsyncPrintWriterPool.borrow(mimeResponse.getWriter());
		}
		else {
			writer = new UnsyncStringWriter();
		}

		return writer;
	}

	/**
	 * @param portletRequest
	 * @return
	 * @throws PortletException
	 */
	protected Template getTemplate(PortletRequest portletRequest)
		throws PortletException {

		if (_template == null) {
			try {
				_template = _createTemplate();
			}
			catch (TemplateException te) {
				throw new PortletException("Unable to create template", te);
			}
		}

		return _template;
	}

	/**
	 * @param namespace
	 * @param portletRequest
	 * @param portletResponse
	 * @param lifecycle
	 * @throws IOException
	 * @throws PortletException
	 *
	 * @see MVCPortlet
	 */
	@Override
	protected void include(
			String path, PortletRequest portletRequest,
			PortletResponse portletResponse, String lifecycle)
		throws IOException, PortletException {

		try {
			Writer writer = getResponseWriter(portletResponse);

			prepareTemplate(portletRequest, portletResponse);

			_writeTemplate(portletRequest, portletResponse, writer);

			_writeJavaScript(portletRequest, portletResponse, writer);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}

		if (clearRequestParameters) {
			if (lifecycle.equals(PortletRequest.RENDER_PHASE)) {
				portletResponse.setProperty("clear-request-parameters", "true");
			}
		}
	}

	protected boolean isProcessAction(PortletRequest portletRequest) {
		int original_p_p_lifecycle = ParamUtil.getInteger(
			portletRequest, "original_p_p_lifecycle");

		if (original_p_p_lifecycle == 1) {
			return true;
		}

		return false;
	}

	protected void populateJavaScriptTemplateContext(
		Template template, String portletNamespace) {

		String portletComponentId = getPortletComponentId(portletNamespace);

		template.put(
			"element", "#" + getPortletWrapperId(portletNamespace) + " > div");

		template.put("id", portletComponentId);
	}

	protected void prepareTemplate(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		MultiSessionMessages.add(
			portletRequest,
			SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

		Template template = getTemplate(portletRequest);

		String path = getPath(portletRequest, portletResponse);

		String controllerPath = _soyPortletHelper.getJavaScriptLoaderModule(
			path);

		template.put("controllerPath", controllerPath);

		template.put(
			TemplateConstants.NAMESPACE,
			_soyPortletHelper.getTemplateNamespace(path));

		if (propagateRequestParameters) {
			propagateRequestParameters(portletRequest);
		}

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		template.prepare(httpServletRequest);

		String portletNamespace = portletResponse.getNamespace();

		populateJavaScriptTemplateContext(template, portletNamespace);

		MultiSessionMessages.clear(portletRequest);

		SessionErrors.clear(portletRequest);

		SessionMessages.clear(portletRequest);
	}

	protected void propagateRequestParameters(PortletRequest portletRequest)
		throws PortletException {

		Template template = getTemplate(portletRequest);

		SoyContext soyContext = new SoyContext();

		Map<String, Object> soyContextParametersMap = new HashMap<>();

		Map<String, String[]> parametersMap = portletRequest.getParameterMap();

		for (Map.Entry<String, String[]> entry : parametersMap.entrySet()) {
			String parameterName = entry.getKey();
			String[] parameterValues = entry.getValue();

			if (parameterValues.length == 1) {
				soyContextParametersMap.put(parameterName, parameterValues[0]);
			}
			else if (parameterValues.length > 1) {
				soyContextParametersMap.put(parameterName, parameterValues);
			}
		}

		soyContext.putInjectedData("requestParams", soyContextParametersMap);

		template.putAll(soyContext);
	}

	protected boolean propagateRequestParameters;

	/**
	 * @deprecated As of 3.1.0, use {@link SoyPortlet#getTemplate(PortletRequest)}} instead
	 */
	@Deprecated
	protected Template template;

	private void _callProcessAction(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse,
			HttpServletResponse response, Portlet portlet)
		throws Exception {

		SoyPortletRequestFactory soyPortletRequestFactory =
			new SoyPortletRequestFactory(portlet);

		ActionRequestImpl actionRequestImpl =
			soyPortletRequestFactory.createActionRequest(resourceRequest);

		ActionResponseImpl actionResponseImpl =
			soyPortletRequestFactory.createActionResponse(
				actionRequestImpl, resourceResponse);

		processAction(actionRequestImpl, actionResponseImpl);

		String portletNamespace = resourceResponse.getNamespace();

		String redirect = HttpUtil.setParameter(
			actionResponseImpl.getRedirectLocation(), portletNamespace + "pjax",
			"true");

		redirect = HttpUtil.setParameter(redirect, "p_p_lifecycle", "2");

		response.sendRedirect(redirect);
	}

	private void _callRender(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse,
			Portlet portlet)
		throws Exception {

		SoyPortletRequestFactory soyPortletRequestFactory =
			new SoyPortletRequestFactory(portlet);

		RenderRequestImpl renderRequestImpl =
			soyPortletRequestFactory.createRenderRequest(
				resourceRequest, resourceResponse);

		renderRequestImpl.getParameterMap();

		RenderResponse renderResponse =
			soyPortletRequestFactory.createRenderResponse(
				renderRequestImpl, resourceResponse);

		render(renderRequestImpl, renderResponse);

		String mvcRenderCommandName = ParamUtil.getString(
			resourceRequest, "mvcRenderCommandName", "/");

		MVCRenderCommand mvcRenderCommand = _getMVCRenderCommand(
			mvcRenderCommandName);

		String path = getPath(resourceRequest, resourceResponse);

		if (mvcRenderCommand != MVCRenderCommand.EMPTY) {
			path = mvcRenderCommand.render(renderRequestImpl, renderResponse);
		}

		resourceRequest.setAttribute(
			getMVCPathAttributeName(renderResponse.getNamespace()), path);
	}

	private Template _createTemplate() throws TemplateException {
		List<TemplateResource> templateResources = _getTemplateResources();

		return TemplateManagerUtil.getTemplate(
			TemplateConstants.LANG_TYPE_SOY, templateResources, false);
	}

	private MVCRenderCommand _getMVCRenderCommand(String mvcRenderCommandName) {
		MVCCommandCache mvcRenderCommandCache = getRenderMVCCommandCache();

		return (MVCRenderCommand)mvcRenderCommandCache.getMVCCommand(
			mvcRenderCommandName);
	}

	/**
	 * Collects all the template resources needed for the portlet. It will include template resources from different
	 * MVCCommands linked to the portlet.
	 *
	 * @throws TemplateException
	 */
	private List<TemplateResource> _getTemplateResources()
		throws TemplateException {

		if (_templateResources == null) {
			_templateResources =
				SoyTemplateResourcesProvider.getBundleTemplateResources(
					_bundle, templatePath);

			MVCCommandCache mvcCommandCache = getRenderMVCCommandCache();

			for (String mvcCommandName : mvcCommandCache.getMVCCommandNames()) {
				MVCCommand mvcCommand = _getMVCRenderCommand(mvcCommandName);

				Bundle bundle = FrameworkUtil.getBundle(mvcCommand.getClass());

				List<TemplateResource> mvcCommandTemplateResources =
					SoyTemplateResourcesProvider.getBundleTemplateResources(
						bundle, templatePath);

				_templateResources.addAll(mvcCommandTemplateResources);
			}
		}

		return _templateResources;
	}

	private boolean _isPjaxRequest(PortletRequest portletRequest) {
		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			portletRequest);

		return GetterUtil.getBoolean(request.getHeader("X-PJAX"));
	}

	/**
	 * Writes the JavaScript for the portlet.
	 *
	 * @param portletRequest
	 * @param portletResponse
	 * @param writer
	 * @throws Exception
	 */
	private void _writeJavaScript(
			PortletRequest portletRequest, PortletResponse portletResponse,
			Writer writer)
		throws Exception {

		String portletNamespace = portletResponse.getNamespace();

		String portletComponentId = getPortletComponentId(portletNamespace);

		String portletId = PortalUtil.getPortletId(portletRequest);

		String portletWrapperId = getPortletWrapperId(portletNamespace);

		Template template = getTemplate(portletRequest);

		ScriptData scriptData = new ScriptData();

		String portletJavaScript = _metalPortletHelper.getRouterJavaScript(
			portletComponentId, portletId, portletNamespace, portletWrapperId,
			template);

		scriptData.append(
			portletId, portletJavaScript,
			"portal-portlet-bridge-soy/router/SoyPortletRouter",
			ScriptData.ModulesType.ES6);

		scriptData.writeTo(writer);
	}

	/**
	 * Writes the template HTML output for the portlet.
	 *
	 * @param portletRequest
	 * @param portletResponse
	 * @param writer
	 * @throws Exception
	 */
	private void _writeTemplate(
			PortletRequest portletRequest, PortletResponse portletResponse,
			Writer writer)
		throws Exception {

		String portletNamespace = portletResponse.getNamespace();

		writer.write("<div id=\"");
		writer.write(getPortletWrapperId(portletNamespace));
		writer.write("\">");

		Template template = getTemplate(portletRequest);

		template.processTemplate(writer);

		writer.write("</div>");
	}

	private static final Log _log = LogFactoryUtil.getLog(SoyPortlet.class);

	private Bundle _bundle;
	private SoyPortletRouterHelper _metalPortletHelper;
	private PortletConfig _portletConfig;
	private SoyPortletHelper _soyPortletHelper;
	private Template _template;
	private List<TemplateResource> _templateResources;

}