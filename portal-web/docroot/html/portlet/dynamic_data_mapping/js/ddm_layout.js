AUI.add(
	'liferay-ddm-layout',
	function(A) {
		var AArray = A.Array;
		var Lang = A.Lang;

		var STR_BLANK = '';

		var DDMLayout = A.Component.create(
			{
				ATTRS: {
					portletNamespace: {
						value: STR_BLANK
					},

					portletResourceNamespace: {
						value: STR_BLANK
					}
				},

				EXTENDS: A.Layout,

				LOCALIZABLE_FIELD_ATTRS: ['label', 'options', 'predefinedValue', 'style', 'tip'],

				NAME: 'liferay-ddm-layout',

				UNLOCALIZABLE_FIELD_ATTRS: ['dataType', 'fieldNamespace', 'indexType', 'localizable', 'multiple', 'name', 'readOnly', 'repeatable', 'required', 'showLabel', 'type'],

				prototype: {
					getFieldByName: function(definition, name) {
						var instance = this;
					},

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

					serialize: function() {
						var instance = this;

						var serializedRows = [];

						AArray.each(
							instance.get('rows'),
							function(item, index) {
								serializedRows.push(
									{
										cols: instance.serializeCols(item.get('cols')),
										type: instance.getRowType(item)
									}
								);
							}
						);

						return { rows: serializedRows };
					},

					serializeCols: function(columns) {
						var instance = this;

						var serializedColumns = [];

						AArray.each(
							columns,
							function(item, index) {
								serializedColumns.push(
									{
										fieldName: item.get('value').get('name'),
										size: item.get('size')
									}
								);
							}
						);

						return serializedColumns;
					}
				}
			}
		);

		Liferay.namespace('DDM').Layout = DDMLayout;

		Liferay.namespace('DDM').Layout.Util = {
			deserialize: function(layout, definition) {
				var instance = this;

				var rows = [];

				AArray.each(
					layout.rows,
					function(item, index) {
						var row = new A[item.type](
							{
								cols: instance.deserializeColumns(item.columns, definition)
							}
						);

						rows.push(row);
					}
				);

				return { rows: rows };
			},

			deserializeColumns: function(columns, definition) {
				var instance = this;

				var cols = [];

				AArray.each(
					columns,
					function(item, index) {
						var col = new A.LayoutCol(
							{
								size: item.size,
								value: instance.deserializeField(item.fieldName, definition)
							}
						);

						cols.push(col);
					}
				);

				return cols;
			},

			deserializeField: function(fieldName, definition) {
				var instance = this;

				var fieldInfo = Liferay.DDM.Form.Util.getFieldInfo(definition, 'name', fieldName);

				var type = instance.getType(fieldInfo.type);

				var array = type.fieldClass.split('.');

				return new A[array[1]]({
					name: fieldName
				});
			},

			getType: function(name) {
				return AArray.filter(
					Liferay.DDM.Types,
					function(item, index, collection) {
						return item.name === name;
					}
				)[0];
			}
		};
	},
	'',
	{
		requires: ['aui-form-builder-page-break-row', 'aui-form-builder-field-text', 'aui-layout', 'liferay-ddm-form']
	}
);