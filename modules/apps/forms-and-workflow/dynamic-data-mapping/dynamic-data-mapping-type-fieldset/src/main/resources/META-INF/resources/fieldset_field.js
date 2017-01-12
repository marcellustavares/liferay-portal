AUI.add(
	'liferay-ddm-form-field-fieldset',
	function(A) {
		var AObject = A.Object;

		var CSS_REMOVE_BORDER = 'remove-border';

		var CSS_REMOVE_BORDER_BOTTOM = A.getClassName(CSS_REMOVE_BORDER, 'bottom');

		var CSS_REMOVE_BORDER_TOP = A.getClassName(CSS_REMOVE_BORDER, 'top');

		var SELECTOR_FIELDSET_FIELD = '.liferay-ddm-form-field-fieldset';

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

						instance._eventHandlers.push(
							instance.before('contextChange', instance._beforeContextChange)
						);

						if (instance.get('repeatedIndex') > 0) {
							instance.set('showLabel', false);
						}
					},

					copyConfiguration: function() {
						var instance = this;

						var config = FieldSetField.superclass.copyConfiguration.apply(instance, arguments);

						config.context.fields.forEach(
							function(field) {
								delete field.instanceId;
								delete field.name;
								delete field.value;
							}
						);

						config.fields = config.context.fields;

						return config;
					},

					getValue: function() {
						return '';
					},

					removeChild: function(field) {
						var instance = this;

						var context = instance.get('context');

						var fields = context.fields;

						var index = instance.indexOf(field);

						if (index > -1) {
							fields.splice(index, 1);

							instance.set('fields', fields);
						}
					},

					render: function() {
						var instance = this;

						FieldSetField.superclass.render.apply(instance, arguments);

						instance.eachField(
							function(field) {
								field.render();
							}
						);

						instance._syncUISiblingFields();

						return instance;
					},

					setValue: function() {
					},

					_beforeContextChange: function(event) {
						var instance = this;

						var fields = event.newVal.fields;

						fields.forEach(A.bind(instance._setNewNestedFieldContext, instance));
					},

					_changeUISiblingContainer: function(element, topCss, bottomCss) {
						element.one(SELECTOR_FIELDSET_FIELD).addClass(topCss).addClass(bottomCss);
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
										var instance = this;

										var instanceId = instance.get('instanceId');

										var container = instance._getContainerByInstanceId(instanceId);

										if (!container) {
											var name = instance.get('fieldName');

											var parent = instance.get('parent');

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
										var instance = this;

										var parent = instance.get('parent');

										return [
											instance.get('portletNamespace'),
											'ddm$$',
											parent.get('fieldName'),
											'$',
											parent.get('instanceId'),
											'$',
											parent.get('repeatedIndex'),
											'#',
											instance.get('fieldName'),
											'$',
											instance.get('instanceId'),
											'$',
											instance.get('repeatedIndex'),
											'$$',
											instance.get('locale')
										].join('');
									}
								}
							}
						);

						var nestedFieldContext = A.merge(
							config,
							{
								fieldName: Util.getFieldNameFromQualifiedName(config.name),
								parent: instance,
								portletNamespace: instance.get('portletNamespace'),
								repeatedIndex: 0
							}
						);

						return new FieldSetNestedField(nestedFieldContext);
					},

					_getMiddleSiblingFields: function(element, index, list) {
						if (index >= 1 && index < list.length - 1) {
							return element;
						}
					},

					_setFields: function(fields) {
						var instance = this;

						return fields.map(A.bind(instance._createNestedField, instance));
					},

					_setNewNestedFieldContext: function(nestedField) {
						var instance = this;

						if (nestedField.name) {
							var name = Util.getFieldNameFromQualifiedName(nestedField.name);

							var field = instance.getField(name);

							if (field) {
								field.set('context', nestedField);
							}
						}
					},

					_syncUISiblingFields: function() {
						var instance = this;

						var siblings = instance.getRepeatedSiblings();

						if (siblings.length > 1) {
							var firstSiblingContainer = siblings[0].get('container');

							instance._changeUISiblingContainer(firstSiblingContainer, '', CSS_REMOVE_BORDER_BOTTOM);

							var middleSiblings = siblings.filter(instance._getMiddleSiblingFields);

							for (var i = 0; i < middleSiblings.length; i++) {
								var middleSiblingContainer = middleSiblings[i].get('container');

								instance._changeUISiblingContainer(middleSiblingContainer, CSS_REMOVE_BORDER_TOP, CSS_REMOVE_BORDER_BOTTOM);
							}

							var lastSiblingContainer = siblings[siblings.length - 1].get('container');

							instance._changeUISiblingContainer(lastSiblingContainer, CSS_REMOVE_BORDER_TOP, '');
						}
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