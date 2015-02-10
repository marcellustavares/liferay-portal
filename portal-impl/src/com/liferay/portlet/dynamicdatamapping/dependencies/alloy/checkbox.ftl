<#include "../init.ftl">

<@aui["field-wrapper"] data=data>
	<#if readOnly>
		<@aui.input name=namespacedFieldName type="hidden" value=fieldValue />

		<@aui.input cssClass=cssClass disabled=true helpMessage=escape(fieldStructure.tip) label=escape(label) name="${namespacedFieldName}Checkbox" type="checkbox" value=fieldValue />
	<#else>
		<@aui.input cssClass=cssClass helpMessage=escape(fieldStructure.tip) label=escape(label) name=namespacedFieldName type="checkbox" value=fieldValue>
			<#if required>
				<@aui.validator name="required" />
			</#if>
		</@aui.input>
	</#if>

	${fieldStructure.children}
</@>