AUI.add(
	'liferay-forms-field-base',
	function(A) {
		var AArray = A.Array;

		var FormsFieldBase = A.Component.create(
			{
				ATTRS: {
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

						return new A[editorType + 'DataEditor'](editorOptions);
					},

					_normalizeSettings: function(settings) {
						var instance = this;

						AArray.each(
							settings,
							function(item, index) {
								item.editor = instance._getEditor(item.editorType, item.editorOptions);
							}
						);

						console.log(settings);

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
							value: ''
						};

						AArray.each(
							instance.get('settings'),
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

		var FieldsUtil = {
			getFieldClass: function(settings) {
				var instance = this;

				var attributes = {
					settings: {
						value: settings
					}
				};

				AArray.each(
					settings,
					function(item, index) {
						attributes[item.attrName] = {
							value: item.value || ''
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
		requires: ['aui-form-builder-field-base', 'aui-form-field', 'aui-boolean-data-editor', 'aui-options-data-editor', 'aui-tabs-data-editor', 'aui-radio-group-data-editor', 'aui-text-data-editor', 'liferay-text-field']
	}
);