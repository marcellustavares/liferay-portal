<#include "../init.ftl">

<@aui["field-wrapper"] data=data>
	<#if (fields??) && (fieldValue != "")>
    [ <a href="javascript:;" id="${portletNamespace}${namespacedFieldName}ToggleImage" onClick="${portletNamespace}${namespacedFieldName}ToggleImage();">${languageUtil.get(locale, "show")}</a> ]

    <div class="hide wcm-image-preview" id="${portletNamespace}${namespacedFieldName}Container">
        <img id="${portletNamespace}${namespacedFieldName}Image" src="${fieldValue}" class="img-polaroid"/>
    </div>
	</#if>

	${fieldStructure.children}
</@>

<@aui.script>
	Liferay.provide(
		window,
		'${portletNamespace}${namespacedFieldName}ToggleImage',
		function() {
			var A = AUI();

			var toggleText = '${languageUtil.get(locale, "show")}';

			var containerNode = A.one('#${portletNamespace}${namespacedFieldName}Container');

			if (containerNode.test(':hidden')) {
				toggleText = '${languageUtil.get(locale, "hide")}';
			}

			A.one('#${portletNamespace}${namespacedFieldName}ToggleImage').setContent(toggleText);

			containerNode.toggle();
		},
		['aui-base']
	);
</@>