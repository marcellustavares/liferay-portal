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
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.taglib.search.ResultRow" %>

<%@ page import="org.osgi.service.metatype.ObjectClassDefinition" %>

<%@ include file="/init.jsp" %>

<%
	ResultRow resultRow =
		(ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	ObjectClassDefinition ocd =
		(ObjectClassDefinition)resultRow.getObject();
%>

<portlet:renderURL var="editAttributesURL">
	<portlet:param name="mvcPath" value="/edit_attributes.jsp" />
	<portlet:param name="servicePID"
		value="<%= String.valueOf(ocd.getID()) %>" />
</portlet:renderURL>

<liferay-ui:icon-menu>
	<liferay-ui:icon message="edit"
		url="<%= editAttributesURL.toString() %>" />
	<liferay-ui:icon message="show" url="#" />
</liferay-ui:icon-menu>