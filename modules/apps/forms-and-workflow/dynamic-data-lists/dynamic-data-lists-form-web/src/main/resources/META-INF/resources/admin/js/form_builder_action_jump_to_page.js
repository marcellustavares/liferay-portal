AUI.add(
	'liferay-ddl-form-builder-action-jump-to-page',
	function(A) {
		var SoyTemplateUtil = Liferay.DDL.SoyTemplateUtil;

		var FormBuilderActionJumpToPage = A.Component.create(
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

				EXTENDS: Liferay.DDL.FormBuilderAction,

				NAME: 'liferay-ddl-form-builder-action-jump-to-page',

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

						// {
						// 	action: instance._actions[currentIndex + '-target'].getValue(),
						// 	label: instance._getFieldLabel(actionValue),
						// 	target: actionValue
						// }

						return {
							action: 'jump-to-page',
							label: 'JumpToPage',
							target: instance._field.getValue()
						};
					}
				}
			}
		);

		Liferay.namespace('DDL').FormBuilderActionJumpToPage = FormBuilderActionJumpToPage;
	},
	'',
	{
		requires: ['liferay-ddl-form-builder-action']
	}
);