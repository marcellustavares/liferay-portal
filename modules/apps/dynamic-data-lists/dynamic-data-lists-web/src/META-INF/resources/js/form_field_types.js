AUI.add(
	'liferay-forms-field-types',
	function(A) {
		var AArray = A.Array;

		var FieldTypes = {
			_fieldTypes: [],

			_getFieldType: function(config) {
				var instance = this;

				var settings = config.advancedSettings.concat(config.basicSettings);

				var defaultConfig = {
					fieldType: config.name,
					settings: settings
				};

				var fieldClass = Liferay.Forms.FieldsUtil.getFieldClass(defaultConfig);

				var fieldType = new A.FormBuilderFieldType(
					{
						defaultConfig: defaultConfig,
						fieldClass: fieldClass,
						icon: config.icon,
						label: config.label,
					}
				);

				fieldType.set('name', config.name);
				fieldType.set('templateNamespace', config.templateNamespace);

				return fieldType;
			},

			get: function(type) {
				var instance = this;

				return AArray.find(
					instance._fieldTypes,
					function(item, index) {
						return item.get('name') === type;
					}
				);
			},

			getAll: function() {
				var instance = this;

				return instance._fieldTypes;
			},

			register: function(fieldTypes) {
				var instance = this;

				instance._fieldTypes = AArray.map(AArray(fieldTypes), instance._getFieldType);
			}
		};

		Liferay.namespace('Forms').FieldTypes = FieldTypes;
	},
	'',
	{
		requires: ['aui-form-builder-field-type', 'liferay-forms-field-base']
	}
);