AUI.add(
	'liferay-ddm-form-field-datasource',
	function(A) {
		var Lang = A.Lang;

		var TPL_OPTION = '<option value="{value}">{label}</option>';

		var DatasourceField = A.Component.create(
			{
				ATTRS: {
					dataProviderURL: {
						value: '/o/ddm-data-provider'
					},

					type: {
						value: 'datasource'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-datasource',

				prototype: {
					destructor: function() {
						var instance = this;

						instance._destroyed = true;
					},

					getValue: function() {
						var instance = this;

						var datasource = {};

						var datasourceForm = instance.datasourceForm;

						if (datasourceForm) {
							datasource = datasourceForm.toJSON();

							var container = instance.get('container');

							var datasourcesSelectNode = container.one('.datasource-select');

							datasource.datasourceName = datasourcesSelectNode.val();
						}

						return datasource;
					},

					hideDatasourceLoader: function() {
						var instance = this;

						var container = instance.get('container');

						var datasourcesNode = container.one('.datasource-container');

						var spinnerNode = container.one('.icon-refresh');

						datasourcesNode.show();
						spinnerNode.hide();
					},

					render: function() {
						var instance = this;

						DatasourceField.superclass.render.apply(instance, arguments);

						instance.showDatasourcesList();
						instance.syncVisibility();

						return instance;
					},

					showDatasourceLoader: function() {
						var instance = this;

						var container = instance.get('container');

						var datasourcesNode = container.one('.datasource-container');

						var spinnerNode = container.one('.icon-refresh');

						datasourcesNode.hide();
						spinnerNode.show();
					},

					showDatasourcesList: function() {
						var instance = this;

						var container = instance.get('container');

						instance.showDatasourceLoader();

						instance._getJSON(
							{
								cmd: 'list'
							},
							function(datasources) {
								instance.hideDatasourceLoader();

								var options = [];

								if (datasources && datasources.length) {
									options = datasources.map(
										function(datasource) {
											return Lang.sub(
												TPL_OPTION,
												{
													label: datasource,
													value: datasource
												}
											);
										}
									);
								}

								var datasourcesSelectNode = container.one('.datasource-select');

								datasourcesSelectNode.html(options.join(''));

								instance.showSelectedDatasourceForm();
							}
						);
					},

					showSelectedDatasourceForm: function() {
						var instance = this;

						var container = instance.get('container');

						var datasourcesSelectNode = container.one('.datasource-select');

						instance.showDatasourceLoader();

						instance._getJSON(
							{
								cmd: 'getSettings',
								name: datasourcesSelectNode.val()
							},
							function(definition) {
								instance.hideDatasourceLoader();

								instance.datasourceForm = new Liferay.DDM.Renderer.Form(
									{
										container: container.one('.datasource-form'),
										definition: definition,
										portletNamespace: instance.get('portletNamespace'),
										templateNamespace: 'ddm.simple_form',
										values: instance._getValue()
									}
								).render();
							}
						);
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
										if (!instance._destroyed) {
											callback.call(instance, null);
										}
									},
									success: function() {
										if (!instance._destroyed) {
											callback.call(instance, this.get('responseData'));
										}
									}
								}
							}
						);
					},

					_getValue: function() {
						var instance = this;

						var value = instance.get('value');

						if (value && !Lang.isObject(value)) {
							value = JSON.parse(value);
						}

						return value;
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').Datasource = DatasourceField;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-field']
	}
);