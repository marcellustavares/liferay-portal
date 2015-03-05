AUI.add(
	'liferay-forms-layout-deserializer',
	function(A) {
		var AArray = A.Array;
		var Lang = A.Lang;

		var FieldsUtil = Liferay.Forms.FieldsUtil;

		var LayoutDeserializer = A.Component.create(
			{
				ATTRS: {
					definition: {
						validator: function(val) {
							return Lang.isObject(val);
						}
					},

					fieldTypes: {
						validator: function(val) {
							return Lang.isArray(val);
						}
					},

					layout: {
						validator: function(val) {
							return Lang.isObject(val);
						}
					}
				},

				EXTENDS: A.Base,

				NAME: 'liferay-forms-layout-deserializer',

				prototype: {
					deserialize: function() {
						var instance = this;

						var layout = instance.get('layout');

						return new A.Layout(
							{
								rows: instance.deserializeRows(layout.rows)
							}
						);
					},

					deserializeColumn: function(column) {
						var instance = this;

						return new A.LayoutCol(
							{
								size: column.size,
								value: instance.deserializeField(column.fieldName)
							}
						);
					},

					deserializeColumns: function(columns) {
						var instance = this;

						return AArray.map(columns, A.bind(instance.deserializeColumn, instance));
					},

					deserializeField: function(fieldName) {
						var instance = this;

						var definition = instance.get('definition');

						var fieldDefinition = instance.searchFieldDefinition(definition, 'name', fieldName);

						var settings = [];

						A.each(
							fieldDefinition,
							function(item, index, collection) {
								if (item.hasOwnProperty('en_US')) {
									item = item['en_US'];
								}

								settings.push(
									{
										attrName: index,
										value: item
									}
								);
							}
						);

						var fieldClass = FieldsUtil.getFieldClass(settings);

						return new fieldClass(settings);
					},

					deserializeRow: function(row) {
						var instance = this;

						var deserializedRow;

						var type = row.type;

						if (type === 'FormBuilderPageBreakRow') {
							deserializedRow = new A.FormBuilderPageBreakRow(
								{
									index: 1,
									quantity: 1
								}
							);
						}
						else {
							deserializedRow = new A.LayoutRow(
								{
									cols: instance.deserializeColumns(row.columns),
									type: type
								}
							);
						}

						return deserializedRow;
					},

					deserializeRows: function(rows) {
						var instance = this;

						return AArray.map(rows, A.bind(instance.deserializeRow, instance));
					},

					searchFieldDefinition: function(parent, key, value) {
						var queue = new A.Queue(parent);

						var addToQueue = function(item) {
							if (AArray.indexOf(queue._q, item) === -1) {
								queue.add(item);
							}
						};

						var fieldInfo = {};

						while (queue.size() > 0) {
							var next = queue.next();

							if (next[key] === value) {
								fieldInfo = next;
							}
							else {
								var children = next.fields || next.nestedFields || next.fieldValues || next.nestedFieldValues;

								if (children) {
									AArray.each(children, addToQueue);
								}
							}
						}

						return fieldInfo;
					}
				}
			}
		);

		Liferay.namespace('Forms').LayoutDeserializer = LayoutDeserializer;
	},
	'',
	{
		requires: ['aui-form-builder-page-break-row', 'aui-layout', 'liferay-forms-field-base']
	}
);