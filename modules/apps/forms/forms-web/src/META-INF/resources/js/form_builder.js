AUI.add(
	'liferay-forms-form-builder',
	function(A) {
		var AArray = A.Array;
		var Lang = A.Lang;

		var FieldsUtil = Liferay.Forms.FieldsUtil;

		var FormBuilder = A.Component.create(
			{
				ATTRS: {
					definition: {
						validator: function(val) {
							return Lang.isObject(val);
						}
					},

					fieldTypes: {
						setter: '_setFieldTypes'
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

					_setFieldTypes: function(val) {
						var instance = this;

						return AArray.map(
							val,
							function(item, index) {
								var settings = item.advancedSettings.concat(item.basicSettings);

								var fieldClass = FieldsUtil.getFieldClass(settings);

								var fieldType = new A.FormBuilderFieldType(
									{
										defaultConfig: {
											settings: settings,
										},
										fieldClass: fieldClass,
										icon: item.icon,
										label: item.label,
									}
								);

								fieldType.set('name', item.name);

								return fieldType;
							}
						);
					},

					_setLayout: function(val) {
						var instance = this;

						val = new Liferay.Forms.LayoutDeserializer(
							{
								definition: instance.get('definition'),
								fieldTypes: instance.get('fieldTypes'),
								layout: val
							}
						).deserialize(val);

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
		requires: ['aui-form-builder', 'liferay-forms-field-base', 'liferay-forms-layout-deserializer']
	}
);