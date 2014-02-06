<#include "../init.ftl">

<#assign style = fieldStructure.style!"">

<@aui["field-wrapper"] data=data label=escape(label)>
	<div class="separator" style="${escapeAttribute(style)}" />

	${fieldStructure.children}
</@>