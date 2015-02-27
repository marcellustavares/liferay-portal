AUI.add(
	'liferay-portlet-dynamic-data-mapping',
	function(A) {
		var AArray = A.Array;
		var Lang = A.Lang;

		var BODY = A.getBody();

		var instanceOf = A.instanceOf;
		var isArray = Lang.isArray;
		var isObject = Lang.isObject;
		var isString = Lang.isString;
		var isUndefined = Lang.isUndefined;

		var STR_BLANK = '';

		var STR_SPACE = ' ';

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

				NAME: 'liferayformbuilder',

				UNLOCALIZABLE_FIELD_ATTRS: ['dataType', 'fieldNamespace', 'indexType', 'localizable', 'multiple', 'name', 'readOnly', 'repeatable', 'required', 'showLabel', 'type'],

				prototype: {
				}
			}
		);

		DDMLayout.Util = {
			getFileEntry: function(fileJSON, callback) {
				var instance = this;

				fileJSON = instance.parseJSON(fileJSON);

				Liferay.Service(
					'/dlapp/get-file-entry-by-uuid-and-group-id',
					{
						groupId: fileJSON.groupId,
						uuid: fileJSON.uuid
					},
					callback
				);
			},

			getFileEntryURL: function(fileEntry) {
				var instance = this;

				var buffer = [
					themeDisplay.getPathContext(),
					'documents',
					fileEntry.groupId,
					fileEntry.folderId,
					encodeURIComponent(fileEntry.title)
				];

				return buffer.join('/');
			},

			normalizeKey: function(str) {
				var instance = this;

				if (isString(str)) {
					for (var i = 0; i < str.length; i++) {
						var item = str.charAt(i);

						if (!A.Text.Unicode.test(item, 'L') && !A.Text.Unicode.test(item, 'N') && !A.Text.Unicode.test(item, 'Pd')) {
							str = str.replace(item, STR_SPACE);
						}
					}

					str = str.replace(/\s/g, '_');
				}

				return str;
			},

			normalizeValue: function(value) {
				var instance = this;

				if (Lang.isUndefined(value)) {
					value = STR_BLANK;
				}

				return value;
			},

			parseJSON: function(value) {
				var instance = this;

				var data = {};

				try {
					data = JSON.parse(value);
				}
				catch (e) {
				}

				return data;
			},

			validateFieldName: function(fieldName) {
				return (/^[\w]+$/).test(fieldName);
			}
		};

		Liferay.namespace('DDM').Layout = DDMLayout;
	},
	'',
	{
		requires: ['arraysort', 'aui-form-builder', 'aui-form-validator', 'aui-map', 'aui-text-unicode', 'json', 'liferay-menu', 'liferay-translation-manager', 'liferay-util-window', 'text']
	}
);