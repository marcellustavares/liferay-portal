AUI.add(
	'liferay-ddm-form-field-captcha',
	function(A) {
		var TPL_CAPTCHA_FIELD = '<div class="form-group"></div>';

		var FieldTypes = Liferay.DDM.Renderer.FieldTypes;

		var CaptchaField = A.Component.create(
			{
				ATTRS: {
					type: {
						value: 'captcha'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-captcha',

				prototype: {
					getTemplateRenderer: function() {
						var instance = this;

						return A.bind('renderTemplate', instance);
					},

					getValue: function() {
						return '';
					},

					renderTemplate: function() {
						var instance = this;

						return instance._valueContainer().html();
					}
				}
			}
		);

		FieldTypes.register(
			{
				javaScriptClass: 'Liferay.DDM.Field.Captcha',
				name: 'captcha',
				settings: [],
				settingsLayout: {},
				system: true
			}
		);

		Liferay.namespace('DDM.Field').Captcha = CaptchaField;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-field']
	}
);