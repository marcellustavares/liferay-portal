;(function() {
	AUI().applyConfig(
		{
			groups: {
				checkbox: {
						base: '/o/ddm-type-checkbox/',
						modules: {
							'liferay-checkbox-field': {
								path: 'checkbox.soy.js',
								requires: [
									'liferay-soy-utils'
								]
							}
						},
						root: '/o/ddm-type-checkbox/'
				},
				radio: {
						base: '/o/ddm-type-radio/',
						modules: {
							'liferay-radio-field': {
								path: 'radio.soy.js',
								requires: [
									'liferay-soy-utils'
								]
							}
						},
						root: '/o/ddm-type-radio/'
				},
				select: {
						base: '/o/ddm-type-select/',
						modules: {
							'liferay-select-field': {
								path: 'select.soy.js',
								requires: [
									'liferay-soy-utils'
								]
							}
						},
						root: '/o/ddm-type-select/'
				},
				text: {
						base: '/o/ddm-type-text/',
						modules: {
							'liferay-text-field': {
								path: 'text.soy.js',
								requires: [
									'liferay-soy-utils'
								]
							}
						},
						root: '/o/ddm-type-text/'
				},
				'third-party': {
						base: '/o/ddl-web/third-party/',
						modules: {
							'liferay-soy-utils': {
								path: 'soyutils.js'
							}
						},
						root: '/o/ddl-web/third-party/'
				},
				forms: {
					base: '/o/ddl-web/js/',
					modules: {
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
								'aui-text-data-editor',
								'liferay-checkbox-field',
								'liferay-forms-field-types',
								'liferay-radio-field',
								'liferay-select-field',
								'liferay-text-field'
							]
						},
						'liferay-forms-field-types': {
							path: 'form_field_types.js',
							requires: [
								'aui-form-builder-field-type',
								'liferay-forms-field-base'
							]
						},
						'liferay-forms-form-builder': {
							path: 'form_builder.js',
							requires: [
								'aui-form-builder',
								'liferay-forms-field-base',
								'liferay-forms-field-types',
								'liferay-forms-layout-serializer'
							]
						},
						'liferay-forms-layout-deserializer': {
							path: 'form_layout_deserializer.js',
							requires: [
								'aui-form-builder-page-break-row',
								'aui-layout',
								'liferay-forms-field-base',
								'liferay-forms-field-types'
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
							condition: {
								name: 'liferay-forms-portlet',
								trigger: 'liferay-form',
								when: 'before'
							},
							path: 'form_portlet.js',
							requires: [
								'liferay-forms-definition-serializer',
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
					root: '/o/ddl-web/js/'
				}
			}
		}
	);
})();