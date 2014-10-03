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

<%@page import="java.util.ListIterator"%>
<%@page import="org.osgi.service.metatype.AttributeDefinition"%>
<%@page import="org.osgi.service.metatype.ObjectClassDefinition"%>
<%@page import="com.liferay.osgi.console.web.model.MetatypeInfoForm"%>
<%@page import="java.util.List"%>
<%@ include file="/init.jsp"%>

<table>
	<%
		List<MetatypeInfoForm> metatypeInfoForms =
			(List<MetatypeInfoForm>)request.getAttribute("METATYPE_INFO");
		ListIterator<MetatypeInfoForm> iterator =
			metatypeInfoForms.listIterator();
		while (iterator.hasNext()) {
			MetatypeInfoForm infoForm = iterator.next();
			ObjectClassDefinition ocd = infoForm.getOcd();
	%>
	<tr>
		<td>Service PID</td>
		<td><b><%=infoForm.getPid()%></b></td>
	</tr>
	<tr>
		<th colspan="2">Attributes Info</th>
	</tr>
	<tr>
		<td>
			<table border="1">
				<tr>
					<td>ID</td>
					<td>Name</td>
				</tr>
				<%
					AttributeDefinition[] attrDefs =
							ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
						for (int i = 0; i < attrDefs.length; i++) {
							AttributeDefinition attrDef = attrDefs[i];
				%>


				<tr>
					<td><%=attrDef.getID()%>
					<td><%=attrDef.getName()%></td>
				</tr>
				<%
					}
				%>

			</table>
		</td>
	</tr>
	<%
		}
	%>
</table>