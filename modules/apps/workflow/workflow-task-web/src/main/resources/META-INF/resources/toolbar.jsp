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

<liferay-frontend:management-bar
	includeCheckBox="<%= false %>"
>
	<liferay-frontend:management-bar-buttons>
		<c:if test="<%= !workflowTaskDisplayContext.isSearch() %>">
			<liferay-frontend:management-bar-display-buttons
				displayViews="<%= workflowTaskDisplayContext.getDisplayViews() %>"
				portletURL="<%= workflowTaskDisplayContext.getPortletURL() %>"
				selectedDisplayStyle="<%= workflowTaskDisplayContext.getDisplayStyle() %>"
			/>
		</c:if>
	</liferay-frontend:management-bar-buttons>

	<liferay-frontend:management-bar-filters>
		<liferay-frontend:management-bar-filter
			label="status"
			managementBarFilterItems="<%= workflowTaskDisplayContext.getManagementBarStatusFilterItems() %>"
			value="<%= workflowTaskDisplayContext.getManagementBarStatusFilterValue() %>"
		/>

		<liferay-frontend:management-bar-sort
			orderByCol="<%= workflowTaskDisplayContext.getOrderByCol() %>"
			orderByType="<%= workflowTaskDisplayContext.getOrderByType() %>"
			orderColumns='<%= new String[] {"asset-title", "last-activity-date", "due-date"} %>'
			portletURL="<%= workflowTaskDisplayContext.getPortletURL() %>"
		/>
	</liferay-frontend:management-bar-filters>

</liferay-frontend:management-bar>