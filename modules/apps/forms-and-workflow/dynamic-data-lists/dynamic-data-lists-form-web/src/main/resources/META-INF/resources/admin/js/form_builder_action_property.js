AUI.add(
	'liferay-ddl-form-builder-action-property',
	function(A) {
		var SoyTemplateUtil = Liferay.DDL.SoyTemplateUtil;

		var FormBuilderActionProperty = A.Component.create(
			{
				ATTRS: {
					action: {
						value: ''
					},

					index: {
						value: ''
					},

					options: {
						value: []
					}
				},

				AUGMENTS: [],

				NAME: 'liferay-ddl-form-builder-action-property',

				prototype: {
					initializer: function() {
						var instance = this;

						var field = instance.createField();
					},

					bindUI: function() {
						var instance = this;


					},

					render: function() {
						var instance = this;

						instance._field.render(instance.get('boundingBox'));
					},

					createField: function() {
						var instance = this;

						var value;

						var action = instance.get('action');

						if (action && action.target) {
							value = action.target;
						}

						instance._field = new Liferay.DDM.Field.Select(
							{
								fieldName: instance.get('index') + '-action',
								label: Liferay.Language.get('the'),
								options: instance.get('options'),
								showLabel: false,
								value: value,
								visible: true
							}
						);

						return instance._field;
					},

					getValue: function() {
						var instance = this;

						return {
							action: 'jump-to-page',
							label: 'JumpToPage',
							target: instance._field.getValue()
						};
					}
				}
			}
		);

		Liferay.namespace('DDL').FormBuilderActionProperty = FormBuilderActionProperty;
	},
	'',
	{
		requires: ['liferay-ddl-form-builder-action']
	}
);