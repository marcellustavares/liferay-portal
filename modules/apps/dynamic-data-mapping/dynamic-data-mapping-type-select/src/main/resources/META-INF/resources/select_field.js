AUI.add(
	'liferay-ddm-form-field-select',
	function(A) {
		var Lang = A.Lang;

		var SelectField = A.Component.create(
			{
				ATTRS: {
					dataSourceType: {
						value: 'manual'
					},

					ddmDataProviderId: {
						value: 0
					},

					options: {
						validator: Array.isArray,
						value: []
					},

					type: {
						value: 'select'
					},

					value: {
						setter: '_setValue',
						value: []
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-select',

				prototype: {
					getOptions: function() {
						var instance = this;

						return A.map(
							instance.get('options'),
							function(item) {
								var label = item.label;

								if (Lang.isObject(label)) {
									label = label[instance.get('locale')];
								}

								return {
									label: label,
									status: instance._getOptionStatus(item),
									value: item.value
								};
							}
						);
					},

					getTemplateContext: function() {
						var instance = this;

						return A.merge(
							SelectField.superclass.getTemplateContext.apply(instance, arguments),
							{
								options: instance.getOptions()
							}
						);
					},

					getTemplateRenderer: function() {
						var instance = this;

						var renderer;

						if (instance.get('dataSourceType') === 'manual') {
							renderer = SelectField.superclass.getTemplateRenderer.apply(instance, arguments);
						}
						else {
							renderer = A.bind('renderTemplate', instance);
						}

						return renderer;
					},

					renderTemplate: function() {
						var instance = this;

						return instance.fetchContainer().html();
					},

					_getOptionStatus: function(option) {
						var instance = this;

						var status = '';

						var value = instance.get('value');

						if (instance.get('localizable')) {
							value = value[instance.get('locale')] || [];
						}

						if (value.indexOf(option.value) > -1) {
							status = 'selected';
						}

						return status;
					},

					_setValue: function(val) {
						return val || [];
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').Select = SelectField;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-field']
	}
);