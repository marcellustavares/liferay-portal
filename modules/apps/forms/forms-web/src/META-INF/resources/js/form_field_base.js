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
					initializer: function() {
						var instance = this;

						(new A.EventHandle(instance._fieldEventHandles)).detach();
					},

					renderUI: function() {
						var instance = this;

						instance.renderTemplate();
					},

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
					},

					getSettings: function() {
						var instance = this;

						var advancedSettings = instance._advancedSettings || [];

						var basicSettings = instance._settings || [];

						return advancedSettings.concat(basicSettings);
					},

					getTemplate: function() {
						var instance = this;

						var config = {
							value: ''
						};

						AArray.each(
							instance.getSettings(),
							function(item, index) {
								config[item.attrName] = instance.get(item.attrName);
							}
						);

						return ddm.text(config);
					},

					renderTemplate: function() {
						var instance = this;

						var markup = instance.getTemplate();

						var content = this.get('content');

						content.one('.form-field-content').setHTML(markup);
					}
				}
			}
		);

		Liferay.namespace('Forms').FieldBase = FormsFieldBase;
	},
	'',
	{
		requires: ['aui-form-builder-field-base', 'aui-form-field', 'aui-boolean-data-editor', 'aui-options-data-editor', 'aui-tabs-data-editor', 'aui-radio-group-data-editor', 'aui-text-data-editor', 'liferay-text-field']
	}
);