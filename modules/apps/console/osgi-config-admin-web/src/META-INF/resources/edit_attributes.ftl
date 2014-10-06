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

<h4>
<@liferay_ui["message"] key="editing-service" arguments="${servicePID}"/>
</h4>

<@aui["form"] method="post" name="fmOCDAttribute">
<@aui["input"] name="servicePID" type="hidden" value="${servicePID}" />
<@aui["fieldset"]>
${editAttributeFormContent}
</@>
</@>