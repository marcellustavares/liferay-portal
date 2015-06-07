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
String redirect = ParamUtil.getString(request, "redirect");

DDLRecordSet ddlRecordSet = formsRequestHelper.getDDLRecordSet();

DDMStructure structure = null;

if (ddlRecordSet != null) {
	structure = ddlRecordSet.getDDMStructure();
}

long groupId = BeanParamUtil.getLong(structure, request, "groupId", scopeGroupId);

long recordSetId = BeanParamUtil.getLong(ddlRecordSet, request, "recordSetId");

long structureId = BeanParamUtil.getLong(structure, request, "structureId");

String name = BeanParamUtil.getString(ddlRecordSet, request, "name");

String description = BeanParamUtil.getString(ddlRecordSet, request, "description");

DDMForm ddmForm = formsRequestHelper.getDDMForm(structure);

DDMFormLayout ddmFormLayout = formsRequestHelper.getDDMFormLayout(structure);
%>

<portlet:actionURL var="editFormURL">
	<portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="editForm" />
</portlet:actionURL>

<aui:form action="<%= editFormURL %>" cssClass="ddl-form-builder-form" method="post" name="editForm">
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
	<aui:input name="recordSetId" type="hidden" value="<%= recordSetId %>" />
	<aui:input name="structureId" type="hidden" value="<%= structureId %>" />

	<aui:fieldset cssClass="ddl-form-builder-basic-info">
		<aui:input ignoreRequestValue="<%= true %>" name="name" placeholder='<%= LanguageUtil.get(request, "type-the-form-name-here") %>' required="<%= true %>" value="<%= LocalizationUtil.getLocalization(name, themeDisplay.getLanguageId()) %>"  wrapperCssClass="ddl-form-builder-name field form-control lfr-input-text lfr-input-text-container" />

		<aui:input ignoreRequestValue="<%= true %>" name="description" placeholder='<%= LanguageUtil.get(request, "add-a-short-description") %>' value="<%= LocalizationUtil.getLocalization(description, themeDisplay.getLanguageId()) %>" wrapperCssClass="ddl-form-builder-description field form-control lfr-input-text lfr-input-text-container" />

		<c:if test="<%= false %>">
			<aui:input label="show-progressbar" name="showProgressbar" type="checkbox" />

			<aui:input label="add-confirmation-page" name="addConfirmationPage" type="checkbox" />
		</c:if>

		<aui:button-row cssClass="ddl-form-builder-buttons">
			<aui:button cssClass="forms-submit pull-right" label="save" primary="<%= true %>" type="submit" />
		</aui:button-row>
	</aui:fieldset>

	<aui:fieldset cssClass="ddl-form-builder-app">
		<aui:input name="definition" type="hidden" />
		<aui:input name="layout" type="hidden" />

		<div id="<portlet:namespace />formBuilder"></div>
	</aui:fieldset>

	<aui:script>
		var initHandler = Liferay.after(
			'form:registered',
			function(event) {
				if (event.formName === '<portlet:namespace />editForm') {
					var fieldTypes = <%= formsRequestHelper.getDDMFormFieldTypesJSONArray() %>;

					var fieldModules = _.map(
						fieldTypes,
						function(item) {
							return item.javaScriptModule;
						}
					);

					Liferay.provide(
						window,
						'<portlet:namespace />init',
						function() {
							Liferay.DDM.Renderer.FieldTypes.register(fieldTypes);

							var formBuilder = new Liferay.DDL.FormBuilder(
								{
									definition: <%= DDMFormJSONSerializerUtil.serialize(ddmForm) %>,
									layouts: <%= DDMFormLayoutJSONSerializerUtil.serialize(ddmFormLayout) %>
								}
							).render('#<portlet:namespace />formBuilder');

							new Liferay.DDL.Portlet(
								{
									editForm: event.form,
									formBuilder: formBuilder,
									namespace: '<portlet:namespace />'
								}
							);
						},
						['liferay-ddl-form-builder', 'liferay-ddl-portlet'].concat(fieldModules)
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