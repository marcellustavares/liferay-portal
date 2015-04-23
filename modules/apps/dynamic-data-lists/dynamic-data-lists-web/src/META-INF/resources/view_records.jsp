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

DDLRecordSet recordSet = (DDLRecordSet)request.getAttribute(FormsPortletKeys.RECORD_SET);

long formDDMTemplateId = ParamUtil.getLong(request, "formDDMTemplateId");

boolean editable = ParamUtil.getBoolean(request, "editable", true);
boolean hasDeletePermission = false;
boolean hasUpdatePermission = false;
boolean showAddRecordButton = false;

if (editable) {
	hasDeletePermission = DDLRecordSetPermission.contains(permissionChecker, recordSet.getRecordSetId(), ActionKeys.DELETE);
	hasUpdatePermission = DDLRecordSetPermission.contains(permissionChecker, recordSet.getRecordSetId(), ActionKeys.UPDATE);
	showAddRecordButton = DDLRecordSetPermission.contains(permissionChecker, recordSet.getRecordSetId(), ActionKeys.ADD_RECORD);
}

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/dynamic_data_lists/view_record_set");
portletURL.setParameter("redirect", redirect);
portletURL.setParameter("recordSetId", String.valueOf(recordSet.getRecordSetId()));
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title="back"
/>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">

	<%
	List<String> headerNames = new ArrayList<String>();

	DDMStructure ddmStructure = recordSet.getDDMStructure(formDDMTemplateId);

	DDMForm ddmForm = ddmStructure.getFullHierarchyDDMForm();

	List<DDMFormField> ddmFormfields = ddmForm.getDDMFormFields();

	for (DDMFormField ddmFormField : ddmFormfields) {
		LocalizedValue label = ddmFormField.getLabel();

		headerNames.add(label.getString(locale));
	}

	if (hasUpdatePermission) {
		headerNames.add("status");
		headerNames.add("modified-date");
		headerNames.add("author");
	}

	headerNames.add(StringPool.BLANK);
	%>

	<liferay-ui:search-container
		searchContainer='<%= new SearchContainer(renderRequest, new DisplayTerms(request), null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, portletURL, headerNames, LanguageUtil.format(request, "no-x-records-were-found", HtmlUtil.escape(ddmStructure.getName(locale)), false)) %>'
	>

		<aui:nav-bar>
			<aui:nav cssClass="navbar-nav" searchContainer="<%= searchContainer %>">
				<c:if test="<%= showAddRecordButton %>">
					<portlet:renderURL var="addRecordURL">
						<portlet:param name="mvcPath" value="/view_form.jsp" />
						<portlet:param name="redirect" value="<%= PortalUtil.getCurrentURL(request) %>" />
						<portlet:param name="recordSetId" value="<%= String.valueOf(recordSet.getRecordSetId()) %>" />
						<portlet:param name="formDDMTemplateId" value="<%= String.valueOf(formDDMTemplateId) %>" />
					</portlet:renderURL>

					<aui:nav-item href="<%= addRecordURL %>" iconCssClass="icon-plus" label="add-entry" />
				</c:if>
			</aui:nav>

			<aui:nav-bar-search file="/html/portlet/dynamic_data_lists/record_search.jsp" searchContainer="<%= searchContainer %>" />
		</aui:nav-bar>

		<liferay-ui:search-container-results>

			<%
			formsRequestHelper.populateRecordSearchContainer(searchContainer, recordSet);
			%>

		</liferay-ui:search-container-results>

		<%
		List resultRows = searchContainer.getResultRows();

		for (int i = 0; i < results.size(); i++) {
			DDLRecord record = (DDLRecord)results.get(i);

			DDLRecordVersion recordVersion = record.getRecordVersion();

			if (editable) {
				recordVersion = record.getLatestRecordVersion();
			}

			DDMFormValues ddmFormValues = StorageEngineUtil.getDDMFormValues(recordVersion.getDDMStorageId());

			Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap = ddmFormValues.getDDMFormFieldValuesMap();

			ResultRow row = new ResultRow(record, record.getRecordId(), i);

			row.setParameter("formDDMTemplateId", String.valueOf(formDDMTemplateId));
			row.setParameter("hasDeletePermission", String.valueOf(hasDeletePermission));
			row.setParameter("hasUpdatePermission", String.valueOf(hasUpdatePermission));

			PortletURL rowURL = renderResponse.createRenderURL();

			rowURL.setParameter("mvcPath", "/view_form.jsp");
			rowURL.setParameter("redirect", PortalUtil.getCurrentURL(request));
			rowURL.setParameter("recordId", String.valueOf(record.getRecordId()));
			rowURL.setParameter("recordSetId", String.valueOf(record.getRecordSetId()));
			rowURL.setParameter("version", recordVersion.getVersion());
			rowURL.setParameter("formDDMTemplateId", String.valueOf(formDDMTemplateId));

			// Columns

			for (DDMFormField ddmFormField : ddmFormfields) {
				String value = formsRequestHelper.getDDMFormFieldRenderedValue(ddmFormFieldValuesMap, ddmFormField);

				if (editable) {
					row.addText(value, rowURL);
				}
				else {
					row.addText(value);
				}
			}

			if (hasUpdatePermission) {
				row.addStatus(recordVersion.getStatus(), recordVersion.getStatusByUserId(), recordVersion.getStatusDate(), rowURL);
				row.addDate(record.getModifiedDate(), rowURL);
				row.addText(HtmlUtil.escape(PortalUtil.getUserName(recordVersion)), rowURL);
			}

			// Action

			row.addJSP("/record_action.jsp", "entry-action", application, request, response);

			// Add result row

			resultRows.add(row);
		}
		%>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>