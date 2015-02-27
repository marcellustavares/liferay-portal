;(function() {
	var PATH_PORTLET = Liferay.AUI.getPortletRootPath();

	AUI().applyConfig(
		{
			groups: {
				ddm: {
					base: PATH_PORTLET + '/dynamic_data_mapping/js/',
					modules: {
						'liferay-ddm-layout': {
							path: 'ddm_layout.js',
							requires: [
								'aui-layout'
							]
						}
					},
					root: PATH_PORTLET + '/dynamic_data_mapping/js/'
				}
			}
		}
	);
})();