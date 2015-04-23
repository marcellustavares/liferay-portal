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
--%>

<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@
taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.dynamic.data.lists.web.display.FormsRequestHelper" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMForm" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMFormField" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMStructure" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.LocalizedValue" %><%@
page import="com.liferay.taglib.search.ResultRow" %><%@
page import="com.liferay.dynamic.data.lists.web.constants.FormsPortletKeys" %><%@
page import="com.liferay.taglib.search.ResultRow" %><%@
page import="com.liferay.portlet.dynamicdatamapping.storage.StorageEngineUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues" %><%@
page import="com.liferay.dynamic.data.lists.web.display.FormsRequestHelper" %><%@
page import="com.liferay.portal.kernel.bean.BeanParamUtil" %><%@
page import="com.liferay.portal.kernel.dao.search.DisplayTerms" %><%@
page import="com.liferay.portal.kernel.dao.search.RowChecker" %><%@
page import="com.liferay.portal.kernel.dao.search.SearchContainer" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.HttpUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.StringBundler" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.Validator" %><%@
page import="com.liferay.portal.security.permission.ActionKeys" %><%@
page import="com.liferay.portal.util.PortalUtil" %><%@
page import="com.liferay.portal.util.WebKeys" %><%@
page import="com.liferay.portlet.dynamicdatalists.model.DDLRecordSet" %><%@
page import="com.liferay.portlet.dynamicdatalists.search.RecordSetSearch" %><%@
page import="com.liferay.portlet.dynamicdatalists.service.permission.DDLRecordSetPermission" %><%@
page import="com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONSerializerUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.io.DDMFormLayoutJSONSerializerUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMForm" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMStructure" %><%@
page import="com.liferay.portlet.dynamicdatamapping.service.permission.DDMPermission" %><%@
page import="com.liferay.portlet.dynamicdatamapping.util.DDMDisplay" %><%@
page import="com.liferay.portlet.dynamicdatamapping.util.DDMPermissionHandler" %><%@
page import="com.liferay.portlet.dynamicdatalists.model.DDLRecordVersion" %><%@
page import="com.liferay.portlet.dynamicdatalists.model.DDLRecord" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@

page import="javax.portlet.ActionRequest" %><%@
page import="javax.portlet.PortletURL" %><%@
page import="javax.portlet.WindowState" %>

<%@ page import="java.util.ArrayList" %><%@
page import="java.util.List" %><%@
page import="java.util.Map" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<%
String scopeTitle = ParamUtil.getString(request, "scopeTitle");
boolean showToolbar = ParamUtil.getBoolean(request, "showToolbar", true);

FormsRequestHelper formsRequestHelper = (FormsRequestHelper)request.getAttribute("formsRequestHelper");

DDMDisplay ddmDisplay = formsRequestHelper.getDDMDisplay();

long scopeClassNameId = PortalUtil.getClassNameId(ddmDisplay.getStructureType());

DDMPermissionHandler ddmPermissionHandler = ddmDisplay.getDDMPermissionHandler();

WindowState windowState = liferayPortletRequest.getWindowState();
%>

<aui:script>
	Liferay.namespace('Forms').portletNamespace = '<portlet:namespace />';
</aui:script>