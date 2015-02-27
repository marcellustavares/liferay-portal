AUI.add(
	'liferay-forms-definition-serializer',
	function(A) {
		var AArray = A.Array;

		var DefinitionSerializer = A.Component.create(
			{
				EXTENDS: Liferay.Forms.LayoutSerializer,

				NAME: 'liferay-forms-definition-serializer',

				prototype: {
					getFieldSettings: function(field) {
						var instance = this;

						var advancedSettings = field._advancedSettings;

						var basicSettings = field._settings;

						return advancedSettings.concat(basicSettings);
					},

					serialize: function() {
						var instance = this;

						var layout = instance.get('layout');

						return A.JSON.stringify(
							instance.serializeRows(layout.get('rows'))
						);
					},

					serializeColumn: function(column) {
						var instance = this;

						var field = undefined;

						var value = column.get('value');

						if (A.instanceOf(value, A.FormField)) {
							field = instance.serializeField(value);
						}

						return field;
					},

					serializeColumns: function() {
						var instance = this;

						return AArray.filter(
							DefinitionSerializer.superclass.serializeColumns.apply(instance, arguments),
							function(item, index) {
								return item !== undefined;
							}
						);
					},

					serializeField: function(field) {
						var instance = this;

						var config = {};

						AArray.each(
							instance.getFieldSettings(field),
							function(item, index) {
								config[item.attrName] = item.editor.get('originalValue');
							}
						);

						return config;
					},

					serializeRow: function(row) {
						var instance = this;

						return instance.serializeColumns(row.get('cols'));
					},

					serializeRows: function() {
						var instance = this;

						return AArray.flatten(DefinitionSerializer.superclass.serializeRows.apply(instance, arguments));
					}
				}
			}
		);

		Liferay.namespace('Forms').DefinitionSerializer = DefinitionSerializer;
	},
	'',
	{
		requires: ['json', 'liferay-forms-layout-serializer']
	}
);