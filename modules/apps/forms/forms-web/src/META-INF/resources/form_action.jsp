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

<%@ include file="init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

DDMStructure structure = (DDMStructure)row.getObject();
%>

<liferay-ui:icon-menu direction="down" icon="<%= StringPool.BLANK %>" message="<%= StringPool.BLANK %>" showExpanded="<%= false %>" showWhenSingleIcon="<%= false %>">
	<c:if test="<%= formsRequestHelper.canEditStructure(structure) %>">
		<portlet:renderURL var="editURL">
			<portlet:param name="mvcPath" value="/edit_form.jsp" />
			<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
			<portlet:param name="classNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(DDMStructure.class)) %>" />
			<portlet:param name="classPK" value="<%= String.valueOf(structure.getStructureId()) %>" />
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

	<c:if test="<%= formsRequestHelper.canViewManageTemplates(structure) %>">
		<portlet:renderURL var="manageViewURL">
			<portlet:param name="struts_action" value="/dynamic_data_mapping/view_template" />
			<portlet:param name="classNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(DDMStructure.class)) %>" />
			<portlet:param name="classPK" value="<%= String.valueOf(structure.getStructureId()) %>" />
			<portlet:param name="sourceClassNameId" value="<%= String.valueOf(structure.getClassNameId()) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			iconCssClass="icon-search"
			message="manage-templates"
			url="<%= manageViewURL %>"
		/>
	</c:if>

	<c:if test="<%= formsRequestHelper.canCopyStructure() %>">
		<portlet:renderURL var="copyURL">
			<portlet:param name="closeRedirect" value="<%= HttpUtil.encodeURL(PortalUtil.getCurrentURL(request)) %>" />
			<portlet:param name="struts_action" value="/dynamic_data_mapping/copy_structure" />
			<portlet:param name="classNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(DDMStructure.class)) %>" />
			<portlet:param name="classPK" value="<%= String.valueOf(structure.getStructureId()) %>" />
		</portlet:renderURL>

		<%
		StringBundler sb = new StringBundler(6);

		sb.append("javascript:");
		sb.append(renderResponse.getNamespace());
		sb.append("copyStructure");
		sb.append("('");
		sb.append(copyURL);
		sb.append("');");
		%>

		<liferay-ui:icon
			iconCssClass="icon-copy"
			message="copy"
			url="<%= sb.toString() %>"
		/>
	</c:if>

	<c:if test="<%= formsRequestHelper.canDeleteStructure(structure) %>">
		<portlet:actionURL var="deleteURL">
			<portlet:param name="struts_action" value="/dynamic_data_mapping/edit_structure" />
			<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
			<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
			<portlet:param name="classNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(DDMStructure.class)) %>" />
			<portlet:param name="classPK" value="<%= String.valueOf(structure.getStructureId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete url="<%= deleteURL %>" />
	</c:if>
</liferay-ui:icon-menu>