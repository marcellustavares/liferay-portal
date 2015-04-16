AUI.add(
	'liferay-forms-form-builder',
	function(A) {
		var AArray = A.Array;
		var FieldTypes = Liferay.Forms.FieldTypes;
		var Lang = A.Lang;

		var FormBuilder = A.Component.create(
			{
				ATTRS: {
					definition: {
						validator: function(val) {
							return Lang.isObject(val);
						}
					},

					fieldTypes: {
						getter: function() {
							return FieldTypes.getAll();
						}
					},

					layout: {
						setter: '_setLayout',
						validator: function(val) {
							return Lang.isObject(val);
						}
					}
				},

				AUGMENTS: [],

				CSS_PREFIX: 'form-builder',

				EXTENDS: A.FormBuilder,

				NAME: 'liferay-forms-form-builder',

				prototype: {
					_afterFieldSettingsModalSave: function(event) {
						var instance = this;

						var field = event.field;

						FormBuilder.superclass._afterFieldSettingsModalSave.apply(instance, arguments);

						field.renderTemplate();
					},

					_onClickAddPageBreak: function(event) {
						var instance = this;

						FormBuilder.superclass._onClickAddPageBreak.apply(instance, arguments);

						event.preventDefault();
					},

					_setLayout: function(val) {
						var instance = this;

						val = new Liferay.Forms.LayoutDeserializer(
							{
								definition: instance.get('definition'),
								layout: val
							}
						).deserialize();

						FormBuilder.superclass._setLayout.call(instance, val);

						return val;
					}
				}
			}
		);

		Liferay.namespace('Forms').FormBuilder = FormBuilder;
	},
	'',
	{
		requires: ['aui-form-builder', 'liferay-forms-field-base', 'liferay-forms-field-types', 'liferay-forms-layout-deserializer']
	}
);