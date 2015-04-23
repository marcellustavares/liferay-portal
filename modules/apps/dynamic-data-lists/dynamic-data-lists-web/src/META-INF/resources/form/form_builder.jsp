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

DDMStructure ddmStructure = null;

if (ddlRecordSet != null) {
	ddmStructure = ddlRecordSet.getDDMStructure();
}

DDMForm ddmForm = formsRequestHelper.getDDMForm(ddmStructure);

DDMFormLayout ddmFormLayout = formsRequestHelper.getDDMFormLayout(ddmStructure);
%>

<div id="<portlet:namespace />formBuilder"></div>

<aui:input name="definition" type="hidden" />
<aui:input name="layout" type="hidden" />

<aui:script use="liferay-forms-form-builder,liferay-forms-field-types">
Liferay.Forms.FieldTypes.register(<%= formsRequestHelper.getDDMFormFieldTypesJSONArray() %>);

var definition = <%= DDMFormJSONSerializerUtil.serialize(ddmForm) %>;

var layout = <%= DDMFormLayoutJSONSerializerUtil.serialize(ddmFormLayout) %>;

Liferay.component(
	'<portlet:namespace />FormBuilder',
	function() {
		return new Liferay.Forms.FormBuilder(
			{
				definition: definition,
				layout: layout
			}
		).render('#<portlet:namespace />formBuilder');
	}
);

Liferay.component('<portlet:namespace />FormBuilder');
</aui:script>