;(function() {
	AUI().applyConfig(
		{
			groups: {
				'checkbox-mock': {
					base: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-checkbox/src/META-INF/resources/',
					modules: {
						'liferay-checkbox-field-mock': {
							condition: {
								name: 'liferay-checkbox-field-mock',
								trigger: 'liferay-checkbox-field',
								when: 'instead'
							},
							path: 'checkbox.soy.js'
						}
					},
					root: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-checkbox/src/META-INF/resources/'
				},

				'radio-mock': {
					base: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-radio/src/META-INF/resources/',
					modules: {
						'liferay-radio-field-mock': {
							condition: {
								name: 'liferay-radio-field-mock',
								trigger: 'liferay-radio-field',
								when: 'instead'
							},
							path: 'radio.soy.js'
						}
					},
					root: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-radio/src/META-INF/resources/'
				},

				'select-mock': {
					base: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-select/src/META-INF/resources/',
					modules: {
						'liferay-select-field-mock': {
							condition: {
								name: 'liferay-select-field-mock',
								trigger: 'liferay-select-field',
								when: 'instead'
							},
							path: 'select.soy.js'
						}
					},
					root: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-select/src/META-INF/resources/'
				},

				'soyutils-mock': {
					base: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-form-renderer/src/META-INF/resources/',
					modules: {
						'liferay-soy-utils-mock': {
							condition: {
								name: 'liferay-soy-utils-mock',
								trigger: 'liferay-soy-utils',
								when: 'instead'
							},
							path: 'third-party/soyutils.js'
						}
					},
					root: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-form-renderer/src/META-INF/resources/'
				},

				'text-mock': {
					base: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-text/src/META-INF/resources/',
					modules: {
						'liferay-text-field-mock': {
							condition: {
								name: 'liferay-text-field-mock',
								trigger: 'liferay-text-field',
								when: 'instead'
							},
							path: 'text.soy.js'
						}
					},
					root: '/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-type-text/src/META-INF/resources/'
				}
			}
		}
	);
})();