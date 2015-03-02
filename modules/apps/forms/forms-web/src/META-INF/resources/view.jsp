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

<%@page import="com.liferay.portlet.dynamicdatalists.search.RecordSetSearch"%>
<%@ page import="com.liferay.portlet.dynamicdatalists.service.permission.DDLRecordSetPermission" %>

<%@ include file="init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "structures");

long groupId = ParamUtil.getLong(request, "groupId", themeDisplay.getSiteGroupId());

PortletURL portletURL = formsRequestHelper.getViewPortletURL(renderResponse, groupId, tabs1);
%>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />
	<aui:input name="deleteStructureIds" type="hidden" />

	<liferay-ui:search-container
		rowChecker="<%= new RowChecker(renderResponse) %>"
		searchContainer="<%= new RecordSetSearch(renderRequest, portletURL) %>"
	>

		<%
		request.setAttribute(WebKeys.SEARCH_CONTAINER, searchContainer);
		%>

		<liferay-util:include page="/toolbar.jsp" servletContext="<%= application %>">
			<liferay-util:param name="groupId" value="<%= String.valueOf(groupId) %>" />
		</liferay-util:include>

		<liferay-ui:search-container-results>

			<%
			formsRequestHelper.populateSearchContainer(searchContainer);
			%>

		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="com.liferay.portlet.dynamicdatalists.model.DDLRecordSet"
			escapedModel="<%= true %>"
			keyProperty="recordSetId"
			modelVar="recordSet"
		>
			<liferay-portlet:renderURL varImpl="rowURL">
				<portlet:param name="mvcPath" value="/edit_form.jsp" />
				<portlet:param name="redirect" value="<%= searchContainer.getIteratorURL().toString() %>" />
				<portlet:param name="recordSetId" value="<%= String.valueOf(recordSet.getRecordSetId()) %>" />
			</liferay-portlet:renderURL>

			<%
			if (!DDLRecordSetPermission.contains(permissionChecker, recordSet, ActionKeys.VIEW)) {
				rowURL = null;
			}
			%>

			<liferay-ui:search-container-column-text
				href="<%= rowURL %>"
				name="id"
				orderable="<%= false %>"
				property="recordSetId"
			/>

			<liferay-ui:search-container-column-text
				href="<%= rowURL %>"
				name="name"
				orderable="<%= false %>"
				value="<%= recordSet.getName(locale) %>"
			/>

			<liferay-ui:search-container-column-text
				href="<%= rowURL %>"
				name="description"
				orderable="<%= false %>"
				value="<%= StringUtil.shorten(recordSet.getDescription(locale), 100) %>"
			/>

			<liferay-ui:search-container-column-date
				href="<%= rowURL %>"
				name="modified-date"
				orderable="<%= false %>"
				value="<%= recordSet.getModifiedDate() %>"
			/>

			<liferay-ui:search-container-column-jsp
				align="right"
				cssClass="entry-action"
				path="/form_action.jsp"
			/>
		</liferay-ui:search-container-row>

		<c:if test="<%= total > 0 %>">
			<aui:button-row>
				<aui:button cssClass="delete-structures-button" disabled="<%= true %>" name="delete" onClick='<%= renderResponse.getNamespace() + "deleteStructures();" %>' value="delete" />
			</aui:button-row>

			<div class="separator"><!-- --></div>
		</c:if>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>