AUI.add(
	'liferay-ddm-form-field-select',
	function(A) {
		var Lang = A.Lang;

		var SelectField = A.Component.create(
			{
				ATTRS: {
					dataProviderURL: {
						value: '/o/ddm-data-provider'
					},

					datasource: {
						value: {}
					},

					datasourceType: {
						value: 'manually'
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

					loadOptions: function() {
						var instance = this;

						var datasource = instance.get('datasource');

						if (!instance._loading) {
							instance._loading = true;

							instance._getJSON(
								{
									name: JSON.parse(datasource).datasourceName,
									settings: datasource
								},
								function(options) {
									instance._loading = false;
									instance._loaded = true;

									instance.set('options', options);

									instance.render();
								}
							);
						}
					},

					render: function() {
						var instance = this;

						SelectField.superclass.render.apply(instance, arguments);

						if (!instance._loaded && instance.get('datasourceType') === 'datasource') {
							instance.loadOptions();
						}

						return instance;
					},

					_getJSON: function(data, callback) {
						var instance = this;

						A.io.request(
							instance.get('dataProviderURL'),
							{
								data: data,
								dataType: 'JSON',
								method: 'GET',
								on: {
									failure: function() {
										callback.call(instance, null);
									},
									success: function() {
										callback.call(instance, this.get('responseData'));
									}
								}
							}
						);
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
		requires: ['aui-request', 'liferay-ddm-form-renderer-field']
	}
);