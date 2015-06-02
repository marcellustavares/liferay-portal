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
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

DDLRecord ddlRecord = (DDLRecord)row.getObject();
%>

<liferay-ui:icon-menu direction="down" icon="<%= StringPool.BLANK %>" message="<%= StringPool.BLANK %>" showExpanded="<%= false %>" showWhenSingleIcon="<%= false %>">
	<c:if test="<%= formsRequestHelper.canEditRecord(ddlRecord) %>">
		<portlet:renderURL var="editURL">
			<portlet:param name="mvcPath" value="/view_form.jsp" />
			<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
			<portlet:param name="recordId" value="<%= String.valueOf(ddlRecord.getRecordId()) %>" />
			<portlet:param name="recordSetId" value="<%= String.valueOf(ddlRecord.getRecordSetId()) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			iconCssClass="icon-edit"
			message="edit"
			url="<%= editURL %>"
		/>
	</c:if>

	<c:if test="<%= formsRequestHelper.canDeleteRecord(ddlRecord) %>">
		<portlet:actionURL var="deleteURL">
			<portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="deleteRecord" />
			<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
			<portlet:param name="recordId" value="<%= String.valueOf(ddlRecord.getRecordId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete url="<%= deleteURL %>" />
	</c:if>
</liferay-ui:icon-menu>