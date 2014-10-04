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

<%@ page import="org.osgi.service.metatype.ObjectClassDefinition" %>

<%@ include file="/init.jsp" %>

<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.List" %>

<portlet:actionURL var="editActionURL">
</portlet:actionURL>

<%
	List<ObjectClassDefinition> ocds =
		(List<ObjectClassDefinition>)request.getAttribute("METATYPE_OCD_CONTAINER");
%>

<aui:form name="fm1">

	<liferay-ui:search-container delta="10">

		<liferay-ui:search-container-results results="<%= ocds %>" />

		<liferay-ui:search-container-row
			className="org.osgi.service.metatype.ObjectClassDefinition"
			keyProperty="ID" modelVar="ocd">

			<liferay-ui:search-container-column-text name="ID" property="ID" />

			<liferay-ui:search-container-column-text name="name" property="name" />

			<liferay-ui:search-container-column-jsp name="actions" align="center"
				path="/actions.jsp" />

		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>