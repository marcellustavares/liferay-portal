<%--
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

@generated
--%>

<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.portal.kernel.json.JSONArray" %><%@
page import="com.liferay.portal.kernel.json.JSONObject" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.LocaleUtil" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %><%@
page import="com.liferay.portal.util.PortalUtil" %><%@
page import="com.liferay.taglib.aui.AUIUtil" %>

<%@ page import="java.util.Locale" %><%@
page import="java.util.Set" %>

<%@ page import="javax.portlet.PortletRequest" %>

<%
java.util.Locale[] availableLocales = (java.util.Locale[])request.getAttribute("liferay-ddm:translation-manager:availableLocales");
java.lang.String defaultLanguageId = GetterUtil.getString((java.lang.String)request.getAttribute("liferay-ddm:translation-manager:defaultLanguageId"));
java.lang.String editingLanguageId = GetterUtil.getString((java.lang.String)request.getAttribute("liferay-ddm:translation-manager:editingLanguageId"));
java.lang.String id = GetterUtil.getString((java.lang.String)request.getAttribute("liferay-ddm:translation-manager:id"));
boolean initialize = GetterUtil.getBoolean(String.valueOf(request.getAttribute("liferay-ddm:translation-manager:initialize")), true);
boolean readOnly = GetterUtil.getBoolean(String.valueOf(request.getAttribute("liferay-ddm:translation-manager:readOnly")));
Map<String, Object> dynamicAttributes = (Map<String, Object>)request.getAttribute("liferay-ddm:translation-manager:dynamicAttributes");
Map<String, Object> scopedAttributes = (Map<String, Object>)request.getAttribute("liferay-ddm:translation-manager:scopedAttributes");

PortletRequest portletRequest = (PortletRequest)request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);

String namespace = AUIUtil.getNamespace(portletRequest, portletResponse);

if (Validator.isNull(namespace)) {
	namespace = AUIUtil.getNamespace(request);
}
%>

<%@ include file="/translation_manager/init-ext.jspf" %>