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

				prototype: {bindUI: function() {
						var instance = this;


					},

					render: function() {
						var instance = this;

						instance.createSourceField().render(instance.get('boundingBox'));
						instance.createTargetField().render(instance.get('boundingBox'));
					},

					createSourceField: function() {
						var instance = this;

						var value;

						var action = instance.get('action');

						if (action && action.target) {
							value = action.target;
						}

						instance._sourceField = new Liferay.DDM.Field.Select(
							{
								fieldName: instance.get('index') + '-action',
								label: Liferay.Language.get('the'),
								options: instance.get('options'),
								showLabel: false,
								value: value,
								visible: true
							}
						);

						return instance._sourceField;
					},

					createTargetField: function() {
						var instance = this;

						var value;

						var action = instance.get('action');

						if (action && action.source) {
							value = action.source;
						}

						instance._targetField = new Liferay.DDM.Field.Select(
							{
								fieldName: instance.get('index') + '-action',
								label: Liferay.Language.get('the'),
								options: instance.get('options'),
								showLabel: false,
								value: value,
								visible: true
							}
						);

						return instance._targetField;
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
							source: instance._sourceField.getValue(),
							target: instance._targetField.getValue()
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