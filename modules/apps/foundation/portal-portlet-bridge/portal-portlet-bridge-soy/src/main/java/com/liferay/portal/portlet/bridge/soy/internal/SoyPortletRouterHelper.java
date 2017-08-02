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

package com.liferay.portal.portlet.bridge.soy.internal;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.Route;
import com.liferay.portal.kernel.portlet.Router;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommandCache;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Bruno Basto
 * @author Chema Balsas
 */
public class SoyPortletRouterHelper {

	public SoyPortletRouterHelper(
			MVCCommandCache mvcRenderCommandCache,
			FriendlyURLMapper friendlyURLMapper)
		throws Exception {

		_mvcRenderCommandCache = mvcRenderCommandCache;
		_friendlyURLMapper = friendlyURLMapper;

		_jsonSerializer = JSONFactoryUtil.createJSONSerializer();
		_routerJavaScriptTPL = _getRouterJavaScriptTPL();
	}

	public String getRouterJavaScript(
			String elementId, String portletId, String portletNamespace,
			String portletWrapperId, Template template)
		throws Exception {

		Set<String> mvcRenderCommandNames = _getMVCRenderCommandNames();

		String mvcRenderCommandNamesString = _jsonSerializer.serialize(
			mvcRenderCommandNames);

		template.remove("element");

		String contextString = _jsonSerializer.serializeDeep(template);

		List<Map<String, Object>> friendlyURLRoutes = _getFriendlyURLRoutes();

		String friendlyURLRoutesString = _jsonSerializer.serializeDeep(
			friendlyURLRoutes);

		return StringUtil.replace(
			_routerJavaScriptTPL,
			new String[] {
				"$ELEMENT_ID", "$MVC_RENDER_COMMAND_NAMES", "$PORTLET_ID",
				"$PORTLET_NAMESPACE", "$PORTLET_WRAPPER_ID", "$CONTEXT",
				"$FRIENDLY_URL_ROUTES", "$FRIENDLY_URL_MAPPING",
				"$FRIENDLY_URL_PREFIX"
			},
			new String[] {
				elementId, mvcRenderCommandNamesString, portletId,
				portletNamespace, portletWrapperId, contextString,
				friendlyURLRoutesString, getFriendlyURLMapping(),
				String.valueOf(isCheckMappingWithPrefix())
			});
	}

	public String serializeTemplate(Template template) {
		return _jsonSerializer.serializeDeep(template);
	}

	protected String getFriendlyURLMapping() {
		if (_friendlyURLMapper == null) {
			return StringPool.BLANK;
		}

		return _friendlyURLMapper.getMapping();
	}

	protected boolean isCheckMappingWithPrefix() {
		if (_friendlyURLMapper == null) {
			return false;
		}

		return _friendlyURLMapper.isCheckMappingWithPrefix();
	}

	private List<Map<String, Object>> _getFriendlyURLRoutes() {
		List<Map<String, Object>> routesMapping = new ArrayList<>();

		if (_friendlyURLMapper != null) {
			Router router = _friendlyURLMapper.getRouter();

			List<Route> routes = router.getRoutes();

			for (Route route : routes) {
				Map<String, Object> mapping = new HashMap<>();

				mapping.put(
					"implicitParameters", route.getImplicitParameters());
				mapping.put("pattern", route.getPattern());

				routesMapping.add(mapping);
			}
		}

		return routesMapping;
	}

	private Set<String> _getMVCRenderCommandNames() {
		MVCCommandCache mvcRenderCommandCache = _mvcRenderCommandCache;

		return mvcRenderCommandCache.getMVCCommandNames();
	}

	private String _getRouterJavaScriptTPL() throws Exception {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/router.js.tpl");

		return StringUtil.read(inputStream);
	}

	private final FriendlyURLMapper _friendlyURLMapper;
	private final JSONSerializer _jsonSerializer;
	private final MVCCommandCache _mvcRenderCommandCache;
	private final String _routerJavaScriptTPL;

}