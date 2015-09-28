;(function() {
	var PATH_DDM_TYPE_DATASOURCE = Liferay.ThemeDisplay.getPathContext() + '/o/ddm-type-datasource';

	AUI().applyConfig(
		{
			groups: {
				'field-datasource': {
					base: PATH_DDM_TYPE_DATASOURCE + '/',
					modules: {
						'liferay-ddm-form-field-datasource': {
							condition: {
								trigger: 'liferay-ddm-form-renderer'
							},
							path: 'datasource_field.js',
							requires: [
								'liferay-ddm-form-renderer-field'
							]
						},
						'liferay-ddm-form-field-datasource-template': {
							condition: {
								trigger: 'liferay-ddm-form-renderer'
							},
							path: 'datasource.soy.js',
							requires: [
								'soyutils'
							]
						}
					},
					root: PATH_DDM_TYPE_DATASOURCE + '/'
				}
			}
		}
	);
})();