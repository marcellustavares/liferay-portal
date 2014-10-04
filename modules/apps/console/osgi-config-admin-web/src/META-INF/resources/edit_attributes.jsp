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
<%@ page import="com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext" %>

<%@ include file="/init.jsp" %>
<%@page
	import="com.liferay.portlet.dynamicdatamapping.render.DDMFormRendererUtil" %>
<%@ page import="com.liferay.portlet.dynamicdatamapping.model.DDMForm" %>
<%@ page import="com.liferay.osgi.config.admin.util.MetaTypeInfoUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="org.osgi.service.metatype.ObjectClassDefinition" %>

<%
	String servicePID = request.getParameter("servicePID");
	DDMForm attributeForm = MetaTypeInfoUtil.attributeForm(servicePID);
	DDMFormFieldRenderingContext context = new DDMFormFieldRenderingContext();
%>

<aui:form name="fm1">
	<aui:input name="servicePID" type="hidden" value="<%= servicePID %>" />
	<aui:fieldset>

	</aui:fieldset>
</aui:form>