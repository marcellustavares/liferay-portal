AUI.add(
	'liferay-ddm-form-renderer-test-base',
	function(A) {
		var AArray = A.Array;
		var AJSON = A.JSON;
		var FieldTypes = Liferay.DDM.Renderer.FieldTypes;

		var TestUtil = {
			getFormHTML: function(portletNamespace) {
				var instance = this;

				return ddm.pages(
					{
						fieldTypes: AJSON.stringify(instance.getTestData('field-types')),
						form: AJSON.stringify(instance.getTestData('definition')),
						pages: instance.getLayoutPages(),
						portletNamespace: portletNamespace
					}
				);
			},

			getLayoutPages: function() {
				var instance = this;

				var layout = instance.getTestData('layout');

				console.log(layout);

				return layout.pages;
			},

			getTestData: function(name) {
				var instance = this;

				var response = A.io.request(
					'/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-form-renderer/test/js/unit/assets/' + name + '-data.json',
					{
						dataType: 'json',
						sync: true
					}
				);

				return response.get('responseData');
			},

			getTestHTML: function(name) {
				var instance = this;

				var response = A.io.request(
					'/base/modules/apps/dynamic-data-mapping/dynamic-data-mapping-form-renderer/test/js/unit/assets/' + name + '.html',
					{
						sync: true
					}
				);

				return response.get('responseData');
			},

			initFieldTypesRegistry: function() {
				var instance = this;

				FieldTypes.register(instance.getTestData('field-types'));
			}
		};

		Liferay.namespace('DDM.Renderer').TestUtil = TestUtil;
	},
	'',
	{
		requires: [
			'aui-io-request',
			'json',
			'liferay-ddm-form-renderer-field',
			'liferay-ddm-form-renderer-field-types'
		]
	}
);