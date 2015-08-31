AUI.add(
	'liferay-ddm-form-renderer-types',
	function(A) {
		var AArray = A.Array;
		var ALang = A.Lang;

		var _fieldTypes = [];

		var FieldTypes = {
			get: function(type) {
				var instance = this;

				return AArray.find(
					_fieldTypes,
					function(item, index) {
						return item.get('name') === type;
					}
				);
			},

			getAll: function(inludeSystem) {
				var instance = this;

				if (!ALang.isBoolean(inludeSystem)) {
					inludeSystem = false;
				}

				return AArray.filter(
					_fieldTypes,
					function(item) {
						if (item.get('system') && !inludeSystem) {
							return false;
						}

						return true
					}
				);
			},

			register: function(fieldTypes) {
				var instance = this;

				_fieldTypes = AArray(fieldTypes).map(instance._getFieldType);
			},

			_getFieldType: function(config) {
				var instance = this;

				var fieldType = new A.FormBuilderFieldType(
					{
						defaultConfig: {
							type: config.name
						},
						fieldClass: Liferay.DDM.Renderer.Field,
						icon: config.icon,
						label: config.name
					}
				);

				fieldType.set('className', config.javaScriptClass);
				fieldType.set('name', config.name);
				fieldType.set('settings', config.settings);
				fieldType.set('system', config.system);
				fieldType.set('templateNamespace', config.templateNamespace);

				return fieldType;
			}
		};

		Liferay.namespace('DDM.Renderer').FieldTypes = FieldTypes;
	},
	'',
	{
		requires: ['array-extras', 'aui-form-builder-field-type']
	}
);