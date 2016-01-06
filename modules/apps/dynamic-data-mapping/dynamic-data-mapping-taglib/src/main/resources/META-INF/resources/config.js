;(function() {
	var PATH_DDM_TAGLIB = Liferay.ThemeDisplay.getPathContext() + '/o/dynamic-data-mapping-taglib';

	AUI().applyConfig(
		{
			groups: {
				'dynamic-data-mapping-taglib': {
					base: PATH_DDM_TAGLIB + '/',
					modules: {
						'liferay-translation-manager': {
							path: 'translation_manager/js/translation_manager.js',
							requires: [
								'aui-base'
							]
						}
					},
					root: PATH_DDM_TAGLIB + '/'
				}
			}
		}
	);
})();