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
boolean showBackURL = ParamUtil.getBoolean(request, "showBackURL", true);

DDLRecordSet ddlRecordSet = formsRequestHelper.getDDLRecordSet();

DDMStructure structure = null;

if (ddlRecordSet != null) {
	structure = ddlRecordSet.getDDMStructure();
}

long groupId = BeanParamUtil.getLong(structure, request, "groupId", scopeGroupId);

long recordSetId = BeanParamUtil.getLong(ddlRecordSet, request, "recordSetId");

long structureId = BeanParamUtil.getLong(structure, request, "structureId");
%>

<portlet:actionURL var="editFormURL">
	<portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="editForm" />
</portlet:actionURL>

<aui:form action="<%= editFormURL %>" method="post" name="editForm">
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
	<aui:input name="recordSetId" type="hidden" value="<%= recordSetId %>" />
	<aui:input name="structureId" type="hidden" value="<%= structureId %>" />

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
			<aui:button cssClass="forms-previous hide pull-left" icon="icon-circle-arrow-left" value="previous" />

			<aui:button cssClass="forms-submit hide pull-right" disabled="<%= true %>" primary="<%= true %>" type="submit" />

			<aui:button cssClass="forms-next pull-right" disabled="<%= false %>" icon="icon-circle-arrow-right" iconAlign="right" primary="<%= true %>" value="next" />

			<aui:button cssClass="forms-cancel pull-right" href="<%= redirect %>" value="cancel" />
		</aui:button-row>
	</liferay-util:buffer>

	<liferay-ui:form-navigator
		categoryNames='<%= new String[] {""} %>'
		categorySections="<%= mainSections %>"
		displayStyle="steps"
		formName="editForm"
		htmlBottom="<%= htmlBottom %>"
		jspPath="/form/"
		showButtons="<%= false %>"
	/>

	<aui:script>
		var editForm;
		var tabView;

		var initHandler = Liferay.after(
			[
				'form:registered',
				'formNavigator:<portlet:namespace />init'
			],
			function(event) {
				var formName = '<portlet:namespace />editForm';

				if (event.formName === formName) {
					editForm = event.form;
				}
				else if (event.type === 'formNavigator:<portlet:namespace />init') {
					tabView = Liferay.component(formName + 'Tabview');
				}

				if (editForm && tabView) {
					Liferay.provide(
						window,
						'<portlet:namespace />init',
						function() {
							new Liferay.Forms.Portlet(
								{
									editForm: editForm,
									namespace: '<portlet:namespace />',
									tabView: tabView
								}
							);
						},
						['liferay-forms-portlet']
					);

					<portlet:namespace />init();
				}
			}
		);

		var clearPortletHandlers = function(event) {
			if (event.portletId === '<%= portletDisplay.getRootPortletId() %>') {
				initHandler.detach();

				Liferay.detach('destroyPortlet', clearPortletHandlers);
			}
		};

		Liferay.on('destroyPortlet', clearPortletHandlers);
	</aui:script>
</aui:form>