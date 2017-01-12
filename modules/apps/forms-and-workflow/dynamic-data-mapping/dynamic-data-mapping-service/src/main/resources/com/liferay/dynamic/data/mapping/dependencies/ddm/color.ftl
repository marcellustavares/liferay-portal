<#include "../init.ftl">

<#if !(fields?? && fields.get(fieldName)??) && validator.isNull(fieldRawValue)>
	<#assign fieldRawValue = predefinedValue />
</#if>

<@liferay_aui["field-wrapper"] cssClass="form-builder-field" data=data required=required>
	<div class="form-group">
		<@liferay_aui.input
			helpMessage=escape(fieldStructure.tip)
			inlineField=true label=escape(label)
			name="${namespacedFieldName}Color"
			readonly="readonly" type="text"
			value=escape(fieldStructure.tip)
		/>

		<@liferay_aui.input name=namespacedFieldName type="hidden" value=fieldRawValue>
			<#if required>
				<@liferay_aui.validator name="required" />
			</#if>
		</@>
	</div>
</@>

<@liferay_aui.script use="aui-base,aui-color-picker-popover">
	var A = AUI();

	var colorPicker = new A.ColorPickerPopover(
		{
			trigger: '#${portletNamespace}${namespacedFieldName}Color',
			zIndex: 65535
		}
	);

	var valueField = A.one('#${portletNamespace}${namespacedFieldName}');

	colorPicker.render();

	colorPicker.on('select',
		function(event) {
			event.trigger.setStyle('backgroundColor', event.color);

			valueField.val(event.color);
		}
	);

	colorPicker.set(
		'color',
		'${fieldRawValue}',
		{
			trigger: A.one('#${portletNamespace}${namespacedFieldName}Color')
		}
	);
</@liferay_aui.script>