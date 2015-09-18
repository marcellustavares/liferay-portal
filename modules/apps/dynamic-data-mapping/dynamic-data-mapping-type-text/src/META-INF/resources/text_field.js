AUI.add(
	'liferay-ddm-form-field-text',
	function(A) {
		var TextField = A.Component.create(
			{
				ATTRS: {
					placeholder: {
						value: ''
					},

					type: {
						value: 'text'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-text',

				prototype: {
					getTemplateContext: function() {
						var instance = this;

						return A.merge(
							TextField.superclass.getTemplateContext.apply(instance, arguments),
							{
								placeholder: instance.get('placeholder')
							}
						);
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').Text = TextField;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-field']
	}
);