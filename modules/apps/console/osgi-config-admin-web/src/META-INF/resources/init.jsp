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

<%@ taglib uri="/META-INF/aui.tld" prefix="aui" %>
<%@ taglib uri="/META-INF/c.tld" prefix="c" %>
<%@ taglib uri="/META-INF/liferay-portlet-ext.tld" prefix="liferay-portlet" %>
<%@ taglib uri="/META-INF/liferay-portlet_2_0.tld" prefix="portlet" %>
<%@ taglib uri="/META-INF/liferay-theme.tld" prefix="liferay-theme" %>
<%@ taglib uri="/META-INF/liferay-ui.tld" prefix="liferay-ui" %>

<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.liferay.osgi.config.admin.util.MetaTypeInfoUtil" %>
<%@ page import="com.liferay.portal.kernel.util.LocaleUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONSerializerUtil" %>
<%@ page import="com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONSerializerUtil" %>
<%@ page import="com.liferay.portlet.dynamicdatamapping.model.DDMForm" %>
<%@ page import="com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext" %>
<%@ page import="com.liferay.portlet.dynamicdatamapping.render.DDMFormRendererUtil" %>
<%@ page import="com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues" %>
<%@ page import="com.liferay.taglib.search.ResultRow" %>

<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.List" %>

<%@ page import="javax.portlet.WindowState" %>

<%@ page import="org.osgi.service.metatype.ObjectClassDefinition" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<%
WindowState windowState = liferayPortletRequest.getWindowState();
%>