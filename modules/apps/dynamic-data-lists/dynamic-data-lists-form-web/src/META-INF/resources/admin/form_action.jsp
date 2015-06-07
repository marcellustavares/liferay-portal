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

<%@ include file="/admin/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

DDLRecordSet ddlRecordSet = (DDLRecordSet)row.getObject();

DDMStructure structure = ddlRecordSet.getDDMStructure();
%>

<liferay-ui:icon-menu direction="down" icon="<%= StringPool.BLANK %>" message="<%= StringPool.BLANK %>" showExpanded="<%= false %>" showWhenSingleIcon="<%= false %>">
	<c:if test="<%= formsRequestHelper.canEditStructure(structure) %>">
		<portlet:renderURL var="editURL">
			<portlet:param name="mvcPath" value="/admin/edit_form.jsp" />
			<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
			<portlet:param name="recordSetId" value="<%= String.valueOf(ddlRecordSet.getRecordSetId()) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			iconCssClass="icon-edit"
			message="edit"
			url="<%= editURL %>"
		/>
	</c:if>

	<%
	String editStructureDefaultValuesURL = ddmDisplay.getEditStructureDefaultValuesURL(liferayPortletRequest, liferayPortletResponse, structure, PortalUtil.getCurrentURL(request), PortalUtil.getCurrentURL(request));
	%>

	<c:if test="<%= Validator.isNotNull(editStructureDefaultValuesURL) && formsRequestHelper.canEditStructure(structure) %>">
		<liferay-ui:icon
			iconCssClass="icon-edit"
			message="edit-default-values"
			url="<%= editStructureDefaultValuesURL %>"
		/>
	</c:if>

	<c:if test="<%= formsRequestHelper.canDeleteStructure(structure) %>">
		<portlet:actionURL var="deleteURL">
			<portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="deleteForm" />
			<portlet:param name="recordSetId" value="<%= String.valueOf(ddlRecordSet.getRecordSetId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete url="<%= deleteURL %>" />
	</c:if>
</liferay-ui:icon-menu>

<c:if test="<%= formsRequestHelper.canDuplicateStructure() %>">
	<portlet:renderURL var="duplicateURL">
		<portlet:param name="mvcPath" value="/admin/edit_form.jsp" />
		<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
		<portlet:param name="definition" value="<%= DDMFormJSONSerializerUtil.serialize(structure.getDDMForm()) %>" />
		<portlet:param name="description" value="<%= ddlRecordSet.getDescription() %>" />
		<portlet:param name="layout" value="<%= DDMFormLayoutJSONSerializerUtil.serialize(structure.getDDMFormLayout()) %>" />
		<portlet:param name="name" value="<%= ddlRecordSet.getName() %>" />
	</portlet:renderURL>

	<aui:button cssClass="duplicate-form-button" href="<%= duplicateURL %>" icon="icon-copy" primary="<%= true %>" />
</c:if>