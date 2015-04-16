AUI.add(
	'liferay-forms-definition-serializer',
	function(A) {
		var AArray = A.Array;

		var DefinitionSerializer = A.Component.create(
			{
				EXTENDS: Liferay.Forms.LayoutSerializer,

				NAME: 'liferay-forms-definition-serializer',

				prototype: {
					serialize: function() {
						var instance = this;

						var layout = instance.get('layout');

						return A.JSON.stringify(
							{
								availableLanguageIds: ['en_US'],
								defaultLanguageId: 'en_US',
								fields: instance.serializeRows(layout.get('rows'))
							}
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
							field.getAdvancedSettings().concat(field.getBasicSettings()),
							function(item, index) {
								var value = field.get(item.attrName);

								if (item.attrName === 'options') {
									value = AArray.map(
										value,
										function(option) {
											return {
												value: option,
												label: {
													en_US: option
												}
											};
										}
									);
								}
								else if (item.localizable) {
									value = {
										en_US: value
									}
								}

								config[item.attrName] = value;
							}
						);

						return A.merge(
							config,
							{
								dataType: 'string',
								type: field.get('fieldType')
							}
						);
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