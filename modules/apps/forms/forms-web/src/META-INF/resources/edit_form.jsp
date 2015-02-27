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
String redirect = ParamUtil.getString(request, "redirect");
boolean showBackURL = ParamUtil.getBoolean(request, "showBackURL", true);

String portletResourceNamespace = ParamUtil.getString(request, "portletResourceNamespace");

DDMStructure structure = (DDMStructure)request.getAttribute(WebKeys.DYNAMIC_DATA_MAPPING_STRUCTURE);

DDLRecordSet ddlRecordSet = (DDLRecordSet)request.getAttribute(WebKeys.DYNAMIC_DATA_LISTS_RECORD_SET);

long groupId = BeanParamUtil.getLong(structure, request, "groupId", scopeGroupId);

String script = BeanParamUtil.getString(structure, request, "script");
%>

<portlet:actionURL var="editFormURL">
	<portlet:param name="mvcPath" value="/edit_form.jsp" />
</portlet:actionURL>

<aui:form action="<%= editFormURL %>" method="post" name="editForm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (structure != null) ? Constants.UPDATE : Constants.ADD %>" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
	<aui:input name="definition" type="hidden" />
	<aui:input name="layout" type="hidden" />
	<aui:input name="saveAndContinue" type="hidden" value="<%= false %>" />

	<%
	boolean localizeTitle = true;
	String title = "new-form";

	if (structure != null) {
		localizeTitle = false;
		title = structure.getName(locale);
	}
	%>

	<liferay-ui:header
		backURL="<%= redirect %>"
		localizeTitle="<%= localizeTitle %>"
		showBackURL="<%= showBackURL %>"
		title="<%= title %>"
	/>

	<aui:model-context bean="<%= ddlRecordSet %>" model="<%= DDLRecordSet.class %>" />

	<%
	String[][] mainSections = { formsRequestHelper.getFormCategoryNames() };
	%>

	<liferay-util:buffer var="htmlBottom">
		<aui:button-row cssClass="form-buttons">
			<aui:button cssClass="hide forms-previous pull-left" icon="icon-circle-arrow-left" value="previous" />

			<aui:button cssClass="hide forms-submit pull-right" disabled="<%= true %>" primary="<%= true %>" type="submit" />

			<aui:button cssClass="forms-next pull-right" disabled="<%= true %>" icon="icon-circle-arrow-right" iconAlign="right" primary="<%= true %>" value="next" />

			<aui:button cssClass="forms-cancel pull-right" href="<%= redirect %>" value="cancel" />
		</aui:button-row>
	</liferay-util:buffer>

	<liferay-ui:form-navigator
		categoryNames="<%= new String[] {""} %>"
		categorySections="<%= mainSections %>"
		displayStyle="steps"
		htmlBottom="<%= htmlBottom %>"
		jspPath="/form/"
		showButtons="<%= false %>"
	/>

	<aui:script use="liferay-forms-portlet">
		new Liferay.Forms.Portlet(
			{
				editFormName: 'editForm',
				namespace: '<portlet:namespace />'
			}
		);
	</aui:script>
</aui:form>