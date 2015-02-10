<#include "../init.ftl">

<#assign multiple = false>

<#if fieldStructure.multiple?? && (fieldStructure.multiple == "true")>
	<#assign multiple = true>
</#if>

<#if required>
	<#assign label = label + " (" + languageUtil.get(requestedLocale, "required") + ")">
</#if>

<@aui["field-wrapper"] data=data>
	<#if (readOnly)>
		<@aui.select cssClass=cssClass helpMessage=escape(fieldStructure.tip) label=escape(label) multiple=multiple name=namespacedFieldName readonly=true>
			${fieldStructure.children}
		</@aui.select>
	<#else>
		<@aui.select cssClass=cssClass helpMessage=escape(fieldStructure.tip) label=escape(label) multiple=multiple name=namespacedFieldName>
			${fieldStructure.children}
		</@aui.select>
	</#if>
</@>