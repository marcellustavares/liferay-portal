<#--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
-->

<#-- TODO unable to include init.ftl, debug to fix it -->

<#assign aui = PortletJspTagLibs["/WEB-INF/tld/aui.tld"] />
<#assign liferay_portlet = PortletJspTagLibs["/WEB-INF/tld/liferay-portlet-ext.tld"] />
<#assign liferay_security = PortletJspTagLibs["/WEB-INF/tld/liferay-security.tld"] />
<#assign liferay_theme = PortletJspTagLibs["/WEB-INF/tld/liferay-theme.tld"] />
<#assign liferay_ui = PortletJspTagLibs["/WEB-INF/tld/liferay-ui.tld"] />
<#assign liferay_util = PortletJspTagLibs["/WEB-INF/tld/liferay-util.tld"] />
<#assign portlet = PortletJspTagLibs["/WEB-INF/tld/liferay-portlet.tld"] />

<@portlet["defineObjects"] />

<@liferay_theme["defineObjects"] />

<@liferay_ui["search-container"]
	emptyResultsMessage="no-services-were-found"
	iteratorURL=showAttributesURL
	total=ocdIterator.getTotal()
>
	<@liferay_ui["search-container-results"]
		results=ocdIterator.getResults(searchContainer.getStart(), searchContainer.getEnd())
	/>

	<@liferay_ui["search-container-row"]
		className="org.osgi.service.metatype.ObjectClassDefinition"
		keyProperty="ID"
		modelVar="ocd">

		<@portlet["renderURL"] varImpl="showAttributesURL">
			<@portlet["param"] name="mvcPath" value="/show_attributes.ftl" />
			<@portlet["param"] name="reportKey" value="${ocd.getID()}" />
		</@>

		<@liferay_ui["search-container-column-text"]
			href=showAttributesURL
			name="ID"
			value=ocd.getID()/>

		<@liferay_ui["search-container-column-text"]
			href=showAttributesURL
			name="name"
			value=ocd.getName()	/>


		<@liferay_ui["search-container-column-text"]
			align="right"
			name=""
		>
			<@liferay_ui["icon-menu"]>
				<@liferay_ui["icon"]
					image="view"
					label=true
					message="show-attribute"
					method="get"
					url="${showAttributesURL}"
				/>

				<@portlet["actionURL"] name="editAttributes" var="editAttributeURL">
					<@portlet["param"] name="mvcPath" value="/edit_attributes.ftl" />
					<@portlet["param"] name="servicePID" value="${ocd.getID()}" />
				</@>

				<@liferay_ui["icon"]
					image="edit"
					label=true
					message="edit-attributes"
					method="post"
					url="${editAttributeURL}"
				/>
			</@>
		</@>
	</@>

	<@liferay_ui["search-iterator"] />
</@>
