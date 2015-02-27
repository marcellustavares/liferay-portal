AUI.add(
	'liferay-forms-form-builder',
	function(A) {
		var AArray = A.Array;
		var Lang = A.Lang;

		var FormBuilder = A.Component.create(
			{
				ATTRS: {
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

				CSS_PREFIX: 'form-builder',

				EXTENDS: A.FormBuilder,

				NAME: 'liferay-forms-form-builder',

				prototype: {
					_setFieldTypes: function(val) {
						var instance = this;

						return AArray.map(
							val,
							function(item, index) {
								var fieldClass = instance.getFieldClass(
									item.advancedSettings,
									item.basicSettings
								);

								return new A.FormBuilderFieldType(
									{
										defaultConfig: {
											advancedSettings: item.advancedSettings,
											basicSettings: item.basicSettings
										},
										fieldClass: fieldClass,
										icon: item.icon,
										label: item.label,
									}
								)
							}
						);
					},

					_setLayout: function(val) {
						var instance = this;

						if (!A.instanceOf(val, A.Layout)) {
							val = new A.Layout(val);
						}

						FormBuilder.superclass._setLayout.call(instance, val);

						return val;
					},

					getFieldClass: function(advancedSettings, basicSettings) {
						var instance = this;

						var attributes = {};

						AArray.each(
							advancedSettings.concat(basicSettings),
							function(item, index) {
								var value = '';

								if (item.editorType === 'RadioGroup') {
									value = undefined;
								}

								attributes[item.attrName] = {
									value: value
								};
							}
						);

						return A.Component.create(
							{
								ATTRS: attributes,

								EXTENDS: Liferay.Forms.FieldBase,

								NAME: 'liferay-form-field'
							}
						);
					}
				}
			}
		);

		Liferay.namespace('Forms').FormBuilder = FormBuilder;
	},
	'',
	{
		requires: ['aui-form-builder', 'liferay-forms-field-base', 'liferay-forms-layout']
	}
);