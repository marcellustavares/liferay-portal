AUI.add(
	'liferay-ddm-form-field-fieldset',
	function(A) {
		var AObject = A.Object;

		var Renderer = Liferay.DDM.Renderer;

		var FieldTypes = Renderer.FieldTypes;

		var Util = Renderer.Util;

		var FieldSetField = A.Component.create(
			{
				ATTRS: {
					fields: {
						setter: '_setFields',
						validator: Array.isArray,
						value: []
					},

					showLabel: {
						state: true,
						value: true
					},

					type: {
						value: 'fieldset'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-fieldset',

				prototype: {

					initializer: function() {
						var instance = this;

						if (instance.get('repeatedIndex') > 0) {
							instance.set('showLabel', false);
						}

						var context = instance.get('context');

						context.fields.forEach(function(fieldContext) {
							fieldContext.fieldName = Util.getFieldNameFromQualifiedName(fieldContext.name);

							var field = instance.getField(fieldContext.fieldName);

							if (!fieldContext.instanceId) {
								var instanceId = Util.generateInstanceId(8);

								fieldContext.instanceId = instanceId;
								field.set('instanceId', instanceId);
							}

							var qualifiedName = field.getQualifiedName();

							fieldContext.name = qualifiedName;
							field.set('name', qualifiedName);
						});
					},

					copyConfiguration: function() {
						var instance = this;

						var config = FieldSetField.superclass.copyConfiguration.apply(instance, arguments);

						config.context.fields.forEach(function(field) {
							delete field.instanceId;
							delete field.name;
							delete field.value;
						});

						return config;
					},

					getValue: function() {
						return '';
					},

					render: function() {
						var instance = this;

						FieldSetField.superclass.render.apply(instance, arguments);

						instance.eachField(
							function(field) {
								field.render();
							}
						);

						return instance;
					},

					setValue: function() {
					},

					_afterContextChange: function(event) {
						var instance = this;

						var fields = event.newVal.fields;

						fields.forEach(function(nestedField) {
							if (nestedField.name) {
								var name = Util.getFieldNameFromQualifiedName(nestedField.name);

								var field = instance.getField(name);

								if (field) {
									field.set('context', nestedField);
								}
							}
						});

						FieldSetField.superclass._afterContextChange.apply(instance, arguments);
					},

					_createNestedField: function(config) {
						var instance = this;

						var fieldType = FieldTypes.get(config.type);

						var fieldClassName = fieldType.get('className');

						var fieldClass = AObject.getValue(window, fieldClassName.split('.'));

						var FieldSetNestedField = A.Component.create(
								{
									EXTENDS: fieldClass,

									NAME: 'liferay-ddm-form-field-fieldset-nestedfield',

									prototype: {
										fetchContainer: function() {
											var nestedFieldInstance = this;

											var instanceId = nestedFieldInstance.get('instanceId');

											var container = nestedFieldInstance._getContainerByInstanceId(instanceId);

											if (!container) {
												var name = nestedFieldInstance.get('fieldName');

												var parent = nestedFieldInstance.get('parent');

												container = parent.filterNodes(
													function(qualifiedName) {
														var nodeFieldName = Util.getFieldNameFromQualifiedName(qualifiedName);

														return name === nodeFieldName;
													}
												).item(0);
											}

											return container;
										},

										getQualifiedName: function() {
											var nestedFieldInstance = this;

											var parent = nestedFieldInstance.get('parent');

											return [
												nestedFieldInstance.get('portletNamespace'),
												'ddm$$',
												parent.get('fieldName'),
												'$',
												parent.get('instanceId'),
												'$',
												parent.get('repeatedIndex'),
												'#',
												nestedFieldInstance.get('fieldName'),
												'$',
												nestedFieldInstance.get('instanceId'),
												'$',
												nestedFieldInstance.get('repeatedIndex'),
												'$$',
												nestedFieldInstance.get('locale')
											].join('');
										}
									}
								}
							);

						var nestedFieldContext = A.merge(
							config,
							{
								context: A.clone(config),
								fieldName: Util.getFieldNameFromQualifiedName(config.name),
								parent: instance,
								portletNamespace: instance.get('portletNamespace'),
								repeatedIndex: 0,
							}
						);

						return new FieldSetNestedField(nestedFieldContext);
					},

					_setFields: function(fields) {
						var instance = this;

						return fields.map(A.bind(instance._createNestedField, instance));
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').FieldSet = FieldSetField;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-field']
	}
);