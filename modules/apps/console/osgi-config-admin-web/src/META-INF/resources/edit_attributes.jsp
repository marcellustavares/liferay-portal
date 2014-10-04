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

<%@page import="com.liferay.portal.kernel.util.SetUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.util.LocaleUtil"%>
<%@ include file="/init.jsp" %>

<%
String servicePID = request.getParameter("servicePID");
DDMForm ddmForm = MetaTypeInfoUtil.attributeForm(servicePID);

ddmForm.setAvailableLocales(SetUtil.fromArray(LanguageUtil.getAvailableLocales()));
ddmForm.setDefaultLocale(LocaleUtil.getDefault());

String ddmFormValuesInputName = "PID";
String fieldsNamespace = "FN";
boolean readOnly = false;
boolean showEmptyFieldLabel = true;
long classNameId = 0;
long classPK = 0;
String randomNamespace = "blah";
boolean repeatable = false;
%>

Editing: <%= servicePID %>

<br />

<aui:form name="fm1">
	<aui:input name="servicePID" type="hidden" value="<%= servicePID %>" />

	<%
	DDMFormFieldRenderingContext ddmFormFieldRenderingContext = new DDMFormFieldRenderingContext();

	ddmFormFieldRenderingContext.setFields(null);
	ddmFormFieldRenderingContext.setHttpServletRequest(request);
	ddmFormFieldRenderingContext.setHttpServletResponse(response);
	ddmFormFieldRenderingContext.setLocale(themeDisplay.getLocale());
	// ?? ddmFormFieldRenderingContext.setMode(mode);
	ddmFormFieldRenderingContext.setNamespace(fieldsNamespace);
	ddmFormFieldRenderingContext.setPortletNamespace(renderResponse.getNamespace());
	ddmFormFieldRenderingContext.setReadOnly(readOnly);
	ddmFormFieldRenderingContext.setShowEmptyFieldLabel(showEmptyFieldLabel);
	%>

	<%= DDMFormRendererUtil.render(ddmForm, ddmFormFieldRenderingContext) %>

	<aui:input name="<%= ddmFormValuesInputName %>" type="hidden" />

	<aui:script use="liferay-ddm-form">
		new Liferay.DDM.Form(
			{
				classNameId: <%= classNameId %>,
				classPK: <%= classPK %>,
				container: '#<%= randomNamespace %>',
				ddmFormValuesInput: '#<portlet:namespace /><%= ddmFormValuesInputName %>',
				definition: <%= DDMFormJSONSerializerUtil.serialize(ddmForm) %>,
				doAsGroupId: <%= scopeGroupId %>,
				fieldsNamespace: '<%= fieldsNamespace %>',
				p_l_id: <%= themeDisplay.getPlid() %>,
				portletNamespace: '<portlet:namespace />',
				repeatable: <%= repeatable %>

				<%

				DDMFormValues ddmFormValues = new DDMFormValues();

				ddmFormValues.setDDMForm(ddmForm);
				ddmFormValues.setAvailableLocales(SetUtil.fromArray(LanguageUtil.getAvailableLocales()));
				ddmFormValues.setDefaultLocale(LocaleUtil.getDefault());
				%>

				<c:if test="<%= ddmFormValues != null %>">
					, values: <%= DDMFormValuesJSONSerializerUtil.serialize(ddmFormValues) %>
				</c:if>
			}
		);
	</aui:script>

</aui:form>