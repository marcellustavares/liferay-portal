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
				ddl: {
					base: '/o/ddl-web/js/',
					modules: {
						'liferay-ddl-form-builder': {
							path: 'form_builder.js',
							requires: [
								'aui-form-builder',
								'aui-form-builder-pages',
								'liferay-ddl-form-builder-field',
								'liferay-ddl-form-builder-layout-deserializer',
								'liferay-ddl-form-builder-layout-visitor',
								'liferay-ddm-form-field-types',
								'liferay-ddm-form-renderer'
							]
						},
						'liferay-ddl-form-builder-definition-serializer': {
							path: 'form_definition_serializer.js',
							requires: [
								'json',
								'liferay-ddl-form-builder-layout-visitor',
								'liferay-ddm-form-renderer-field'
							]
						},
						'liferay-ddl-form-builder-field': {
							path: 'form_builder_field.js',
							requires: [
								'aui-form-field',
								'liferay-ddm-form-field-types'
							]
						},
						'liferay-ddl-form-builder-layout-deserializer': {
							path: 'form_layout_deserializer.js',
							requires: [
								'aui-layout',
								'liferay-ddl-form-builder-field',
								'liferay-ddm-form-field-types'
							]
						},
						'liferay-ddl-form-builder-layout-serializer': {
							path: 'form_layout_serializer.js',
							requires: [
								'json',
								'liferay-ddl-form-builder-layout-visitor',
								'liferay-ddm-form-renderer-field'
							]
						},
						'liferay-ddl-form-builder-layout-visitor': {
							path: 'form_layout_visitor.js',
							requires: [
								'aui-layout'
							]
						},
						'liferay-ddl-portlet': {
							condition: {
								name: 'liferay-ddl-portlet',
								trigger: 'liferay-form',
								when: 'before'
							},
							path: 'form_portlet.js',
							requires: [
								'liferay-ddl-form-builder-definition-serializer',
								'liferay-ddl-form-builder-layout-serializer',
								'liferay-portlet-base'
							]
						}
					},
					root: '/o/ddl-web/js/'
				},
				ddm: {
					base: '/o/ddm-form-renderer/js/',
					modules: {
						'liferay-ddm-form-field': {
							path: 'field.js',
							requires: [
								'aui-boolean-data-editor',
								'aui-form-builder-field-base',
								'aui-form-field',
								'aui-options-data-editor',
								'aui-radio-group-data-editor',
								'aui-tabs-data-editor',
								'aui-text-data-editor',
								'liferay-checkbox-field',
								'liferay-ddm-form-field-types',
								'liferay-text-field'
							]
						},
						'liferay-ddm-form-field-types': {
							path: 'field_types.js',
							requires: [
								'array-extras',
								'aui-form-builder-field-type',
								'liferay-ddm-form-field',
								'liferay-ddm-form-renderer-util'
							]
						},
						'liferay-ddm-form-renderer': {
							path: 'form.js',
							requires: [
								'array-extras',
								'liferay-ddm-form-field',
								'liferay-ddm-form-field-types',
								'liferay-ddm-form-renderer-util'
							]
						},
						'liferay-ddm-form-renderer-util': {
							path: 'util.js',
							requires: [
								'array-extras',
								'liferay-ddm-form-field'
							]
						}
					},
					root: '/o/ddm-form-renderer/js/'
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
				}
			}
		}
	);
})();