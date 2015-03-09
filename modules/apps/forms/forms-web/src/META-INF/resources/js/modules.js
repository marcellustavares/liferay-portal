;(function() {
	AUI().applyConfig(
		{
			groups: {
				checkbox: {
						base: '/o/com.liferay.dynamic.data.mapping.type.checkbox/ddm/checkbox/js/',
						modules: {
							'liferay-checkbox-field': {
								path: 'checkbox.soy.js',
								requires: [
									'liferay-soy-utils'
								]
							}
						},
						root: '/o/com.liferay.dynamic.data.mapping.type.checkbox/ddm/checkbox/js/'
				},
				radio: {
						base: '/o/com.liferay.dynamic.data.mapping.type.radio/ddm/radio/js/',
						modules: {
							'liferay-radio-field': {
								path: 'radio.soy.js',
								requires: [
									'liferay-soy-utils'
								]
							}
						},
						root: '/o/com.liferay.dynamic.data.mapping.type.radio/ddm/radio/js/'
				},
				select: {
						base: '/o/com.liferay.dynamic.data.mapping.type.select/ddm/select/js/',
						modules: {
							'liferay-select-field': {
								path: 'select.soy.js',
								requires: [
									'liferay-soy-utils'
								]
							}
						},
						root: '/o/com.liferay.dynamic.data.mapping.type.select/ddm/select/js/'
				},
				text: {
						base: '/o/com.liferay.dynamic.data.mapping.type.text/ddm/text/js/',
						modules: {
							'liferay-text-field': {
								path: 'text.soy.js',
								requires: [
									'liferay-soy-utils'
								]
							}
						},
						root: '/o/com.liferay.dynamic.data.mapping.type.text/ddm/text/js/'
				},
				forms: {
					base: '/o/comliferayformsweb/js/',
					modules: {
						'liferay-soy-utils': {
							path: 'soyutils.js'
						},
						'liferay-forms-definition-serializer': {
							path: 'form_definition_serializer.js',
							requires: [
								'liferay-forms-form-builder'
							]
						},
						'liferay-forms-field-base': {
							path: 'form_field_base.js',
							requires: [
								'aui-boolean-data-editor',
								'aui-form-builder-field-base',
								'aui-form-field',
								'aui-options-data-editor',
								'aui-radio-group-data-editor',
								'aui-tabs-data-editor',
								'aui-text-data-editor'
							]
						},
						'liferay-forms-form-builder': {
							path: 'form_builder.js',
							requires: [
								'aui-form-builder',
								'liferay-forms-field-base',
								'liferay-forms-layout-serializer'
							]
						},
						'liferay-forms-layout-deserializer': {
							path: 'form_layout_deserializer.js',
							requires: [
								'aui-form-builder-page-break-row',
								'aui-layout'
							]
						},
						'liferay-forms-layout-serializer': {
							path: 'form_layout_serializer.js',
							requires: [
								'aui-form-builder-page-break-row',
								'aui-layout',
								'json'
							]
						},
						'liferay-forms-portlet': {
							path: 'form_portlet.js',
							requires: [
								'aui-base',
								'liferay-forms-layout-serializer',
								'liferay-forms-steps',
								'liferay-portlet-base'
							]
						},
						'liferay-forms-steps': {
							path: 'form_steps.js',
							requires: [
								'aui-base',
								'aui-tabview',
								'liferay-portlet-base'
							]
						}
					},
					root: '/o/comliferayformsweb/js/'
				}
			}
		}
	);
})();