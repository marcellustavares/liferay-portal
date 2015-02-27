AUI.add(
	'liferay-forms-layout-serializer',
	function(A) {
		var AArray = A.Array;

		var LayoutSerializer = A.Component.create(
			{
				ATTRS: {
					layout: {
						validator: function(val) {
							return A.instanceOf(val, A.Layout);
						}
					}
				},

				EXTENDS: A.Base,

				NAME: 'liferay-forms-layout-serializer',

				prototype: {
					getRowType: function(row) {
						var instance = this;

						var type;

						if (A.instanceOf(row, A.FormBuilderPageBreakRow)) {
							type = 'FormBuilderPageBreakRow';
						}
						else {
							type = 'LayoutRow';
						}

						return type;
					},

					serialize: function(layout) {
						var instance = this;

						var layout = instance.get('layout');

						return A.JSON.stringify(
							{
								rows: instance.serializeRows(layout.get('rows'))
							}
						);
					},

					serializeColumn: function(column) {
						var instance = this;

						var fieldName = '';

						var value = column.get('value');

						if (A.instanceOf(value, A.FormField)) {
							fieldName = value.get('name');
						}

						return {
							fieldName: fieldName,
							size: column.get('size')
						}
					},

					serializeColumns: function(columns) {
						var instance = this;

						return AArray.map(columns, A.bind(instance.serializeColumn, instance));
					},

					serializeRow: function(row) {
						var instance = this;

						var rowJSON = {
							type: instance.getRowType(row)
						};

						if (rowJSON.type !== 'PageBreakRow') {
							rowJSON.cols = instance.serializeColumns(row.get('cols'));
						}

						return rowJSON;
					},

					serializeRows: function(rows) {
						var instance = this;

						return AArray.map(rows, A.bind(instance.serializeRow, instance));
					}
				}
			}
		);

		Liferay.namespace('Forms').LayoutSerializer = LayoutSerializer;
	},
	'',
	{
		requires: ['form-builder-page-break-row', 'aui-layout', 'json']
	}
);