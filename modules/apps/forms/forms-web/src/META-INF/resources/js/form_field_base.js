AUI.add(
	'liferay-forms-field-base',
	function(A) {
		var AArray = A.Array;

		var FormsFieldBase = A.Component.create(
			{
				ATTRS: {
					advancedSettings: {
						value: {}
					},

					basicSettings: {
						value: {}
					}
				},

				AUGMENTS: [A.FormBuilderFieldBase],

				EXTENDS: A.FormField,

				NAME: 'liferay-form-field-base',

				prototype: {
					_fillAdvancedSettings: function() {
						var instance = this;

						var advancedSettings = instance.get('advancedSettings');

						instance._advancedSettings = instance._normalizeSetitngs(advancedSettings);
					},

					_fillSettings: function() {
						var instance = this;

						var basicSettings = instance.get('basicSettings');

						instance._settings = instance._normalizeSetitngs(basicSettings);
					},

					_getEditor: function(editorType, editorOptions) {
						var instance = this;

						return new A[editorType + 'DataEditor'](editorOptions);
					},

					_normalizeSetitngs: function(settings) {
						var instance = this;

						return AArray.map(
							settings,
							function(item, index) {
								return {
									attrName: item.attrName,
									editor: instance._getEditor(item.editorType, item.editorOptions || {})
								};
							}
						);
					}
				}
			}
		);

		Liferay.namespace('Forms').FieldBase = FormsFieldBase;
	},
	'',
	{
		requires: ['aui-form-builder-field-base', 'aui-form-field', 'aui-boolean-data-editor', 'aui-options-data-editor', 'aui-tabs-data-editor', 'aui-radio-group-data-editor', 'aui-text-data-editor']
	}
);