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
DDLRecordSet ddlRecordSet = formsRequestHelper.getDDLRecordSet();

String name = BeanParamUtil.getString(ddlRecordSet, request, "name");

String description = BeanParamUtil.getString(ddlRecordSet, request, "description");
%>

<aui:fieldset>
	<aui:field-wrapper label="name">
		<liferay-ui:input-localized cssClass="field form-control lfr-input-text lfr-input-text-container" name="name" xml="<%= name %>" />
	</aui:field-wrapper>

	<aui:field-wrapper label="description">
		<liferay-ui:input-localized cssClass="field form-control lfr-input-text-container lfr-input-textarea" name="description" type="textarea" xml="<%= description %>" />
	</aui:field-wrapper>

	<c:if test="<%= false %>">
		<aui:input label="show-progressbar" name="showProgressbar" type="checkbox" />

		<aui:input label="add-confirmation-page" name="addConfirmationPage" type="checkbox" />
	</c:if>
</aui:fieldset>