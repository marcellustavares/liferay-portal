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

<%@ include file="/html/portlet/application_display_template/init.jsp" %>

<%
PortletURL portletURL = renderResponse.createRenderURL();

List<TemplateHandler> templateHandlers = PortletDisplayTemplateUtil.getPortletDisplayTemplateHandlers();
%>

<liferay-ui:search-container
	searchContainer='<%= new SearchContainer(renderRequest, new DisplayTerms(request), null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.MAX_DELTA, portletURL, null, "no-categories-found") %>'
	total="<%= templateHandlers.size() %>"
>

	<liferay-ui:search-container-results
		results="<%= templateHandlers %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.portal.kernel.template.TemplateHandler"
		modelVar="templateHandler"
	>

		<%
		PortletURL rowURL = renderResponse.createRenderURL();

		rowURL.setParameter("struts_action", "/application_display_template/view_template");
		rowURL.setParameter("backURL", currentURL);
		rowURL.setParameter("classNameId", String.valueOf(PortalUtil.getClassNameId(templateHandler.getClassName())));

		String rowHREF = rowURL.toString();
		%>

		<liferay-ui:search-container-column-text
			href="<%= rowHREF %>"
			name="category"
			value="<%= HtmlUtil.escape(templateHandler.getName(locale)) %>"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator
		paginate="<%= false %>"
	/>
</liferay-ui:search-container>