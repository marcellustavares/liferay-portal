;(function() {
	AUI().applyConfig(
		{
			groups: {
				'field-checkbox': {
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
				'field-radio': {
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
				'field-select': {
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
				'field-text': {
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
				'form': {
					base: '/o/ddm-renderer/js/',
					modules: {
						'liferay-ddm-form-renderer': {
							path: 'form.js',
							requires: [
								'array-extras',
								'liferay-ddm-form-renderer-field',
								'liferay-ddm-form-renderer-field-types',
								'liferay-ddm-form-renderer-util'
							]
						},
						'liferay-ddm-form-renderer-field': {
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
								'liferay-ddm-form-renderer-field-types',
								'liferay-radio-field',
								'liferay-select-field',
								'liferay-text-field'
							]
						},
						'liferay-ddm-form-renderer-util': {
							path: 'util.js',
							requires: [
								'array-extras',
							]
						},
						'liferay-ddm-form-renderer-field-types': {
							path: 'field_types.js',
							requires: [
								'array-extras',
								'aui-form-builder-field-type',
								'liferay-ddm-form-renderer-field',
								'liferay-ddm-form-renderer-util'
							]
						}
					},
					root: '/o/ddm-renderer/js/'
				},
				'third-party': {
					base: '/o/ddm-renderer/third-party/',
					modules: {
						'liferay-soy-utils': {
							path: 'soyutils.js'
						}
					},
					root: '/o/ddm-renderer/third-party/'
				}
			}
		}
	);
})();