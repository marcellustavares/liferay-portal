AUI.add(
	'liferay-forms-field-base',
	function(A) {
		var AArray = A.Array;
		var AObject = A.Object;

		var FormsFieldBase = A.Component.create(
			{
				ATTRS: {
					fieldType: {
						value: ''
					},

					settings: {
						value: []
					}
				},

				AUGMENTS: [A.FormBuilderFieldBase],

				EXTENDS: A.FormField,

				NAME: 'liferay-form-field-base',

				prototype: {
					initializer: function() {
						var instance = this;

						instance._fillAdvancedSettings();
						instance._fillSettings();

						instance.renderUI();

						(new A.EventHandle(instance._fieldEventHandles)).detach();
					},

					renderUI: function() {
						var instance = this;

						instance.renderTemplate();
					},

					_fillAdvancedSettings: function() {
						var instance = this;

						instance._advancedSettings = instance.getAdvancedSettings();
					},

					_fillSettings: function() {
						var instance = this;

						instance._settings = instance.getBasicSettings();
					},

					_getEditor: function(editorType, editorOptions) {
						var instance = this;

						var value = Liferay.Forms.FieldsUtil.getEditorDefaultValue(editorType);

						return new A[editorType + 'DataEditor'](
							A.merge(
								editorOptions,
								{
									editedValue: value
								}
							)
						);
					},

					_normalizeSettings: function(settings) {
						var instance = this;

						AArray.each(
							settings,
							function(item, index) {
								item.editor = instance._getEditor(item.editorType, item.editorOptions);
							}
						);

						return settings;
					},

					getAdvancedSettings: function() {
						var instance = this;

						return instance._normalizeSettings(
							AArray.filter(
								instance.get('settings'),
								function(item, index) {
									return item.advanced === true;
								}
							)
						);
					},

					getBasicSettings: function() {
						var instance = this;

						return instance._normalizeSettings(
							AArray.filter(
								instance.get('settings'),
								function(item, index) {
									return item.advanced === false;
								}
							)
						);
					},

					getTemplate: function() {
						var instance = this;

						var config = {
							childElementsHTML: '',
							value: ''
						};

						AArray.each(
							instance.get('settings'),
							function(item, index) {
								config[item.attrName] = instance.get(item.attrName);
							}
						);

						var fieldType = Liferay.Forms.FieldTypes.get(instance.get('fieldType'));

						var templateNamespace = fieldType.get('templateNamespace');

						var renderer = AObject.getValue(window, templateNamespace.split('.'));

						return renderer(config);
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

		var FieldsUtil = {
			getEditorDefaultValue: function(editorType) {
				var instance = this;

				var value = '';

				if (editorType === 'RadioGroup') {
					value = undefined;
				}
				else if (editorType === 'Options') {
					value = [];
				}

				return value;
			},

			getFieldClass: function(defaultConfig) {
				var instance = this;

				var attributes = {
					fieldType: {
						value: defaultConfig.fieldType
					},

					settings: {
						value: defaultConfig.settings
					}
				};

				AArray.each(
					defaultConfig.settings,
					function(item, index) {
						attributes[item.attrName] = {
							value: item.value || instance.getEditorDefaultValue(item.editorType)
						};
					}
				);

				return A.Component.create(
					{
						ATTRS: attributes,

						EXTENDS: FormsFieldBase,

						NAME: 'liferay-form-field'
					}
				);
			}
		};

		Liferay.namespace('Forms').FieldsUtil = FieldsUtil;
	},
	'',
	{
		requires: ['aui-form-builder-field-base', 'aui-form-field', 'aui-boolean-data-editor', 'aui-options-data-editor', 'aui-tabs-data-editor', 'aui-radio-group-data-editor', 'aui-text-data-editor', 'liferay-checkbox-field', 'liferay-forms-field-types', 'liferay-radio-field', 'liferay-select-field', 'liferay-text-field']
	}
);