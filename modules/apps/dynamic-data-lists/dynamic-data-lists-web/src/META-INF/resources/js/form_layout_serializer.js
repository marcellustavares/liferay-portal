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
							type = 'Page';
						}
						else {
							type = 'Row';
						}

						return type;
					},

					serialize: function() {
						var instance = this;

						var layout = instance.get('layout');

						return A.JSON.stringify(
							{
								pages: instance.serializePages(layout.get('rows'))
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

					serializePages: function(rows) {
						var instance = this;

						var page;

						var pages = [];

						AArray.each(
							rows,
							function(item, index) {
								console.log(item, item.get('title'));

								if (instance.getRowType(item) === 'Row') {
									page.rows.push(instance.serializeRow(item));
								}
								else {
									page = {
										rows: [],
										title: item.get('title')
									};

									pages.push(page);
								}
							}
						);

						return AArray.map(
							pages,
							function(item, index) {
								item.title = {
									en_US: item.title || index + 1
								};

								return item;
							}
						);
					},

					serializeRow: function(row) {
						var instance = this;

						return {
							columns: instance.serializeColumns(row.get('cols'))
						};
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
		requires: ['aui-form-builder-page-break-row', 'aui-layout', 'json']
	}
);