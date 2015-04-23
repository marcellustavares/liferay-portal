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

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

long groupId = ParamUtil.getLong(request, "groupId", scopeGroupId);
long recordId = ParamUtil.getLong(request, "recordId");
long recordSetId = ParamUtil.getLong(request, "recordSetId");

String formHTML = (String)request.getAttribute("formHTML");
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title="back"
/>

<portlet:actionURL var="saveFormURL">
	<portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="saveForm" />
</portlet:actionURL>

<aui:form action="<%= saveFormURL %>" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
	<aui:input name="recordId" type="hidden" value="<%= recordId %>" />
	<aui:input name="recordSetId" type="hidden" value="<%= recordSetId %>" />
	<aui:input name="availableLanguageIds" type="hidden" value="en_US" />
	<aui:input name="defaultLanguageId" type="hidden" value="en_US" />

	<%= formHTML %>

	<aui:button type="submit" value="submit" />
</aui:form>