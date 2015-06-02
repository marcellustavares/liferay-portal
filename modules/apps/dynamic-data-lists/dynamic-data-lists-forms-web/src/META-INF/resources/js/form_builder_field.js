AUI.add(
	'liferay-ddl-form-builder-field',
	function(A) {
		var AArray = A.Array;
		var AObject = A.Object;
		var FieldTypes = Liferay.DDM.Renderer.FieldTypes;

		var CSS_FIELD = A.getClassName('form', 'builder', 'field');

		var CSS_FIELD_CONTENT_TOOLBAR = A.getClassName('form', 'builder', 'field', 'content', 'toolbar');

		var CSS_TOOLBAR_CONTAINER = A.getClassName('form', 'builder', 'field', 'toolbar', 'container');

		var TPL_SETTINGS_CONTAINER = '<form action="javascript:;" class="hide"></form>';

		var FormBuilderField = A.Component.create(
			{
				ATTRS: {
					// TODO - Remove this. Not consistent
					content: {
						getter: function() {
							var instance = this;

							return instance.get('container');
						}
					},

					settingsForm: {
						valueFn: '_valueSettingsForm'
					},

					settingsModal: {
						valueFn: '_valueSettingsModal'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddl-form-builder-field',

				prototype: {
					initializer: function() {
						var instance = this;

						var container = instance.get('container');

						container.setData('field-instance', instance);
					},

					renderUI: function() {
						var instance = this;

						FormBuilderField.superclass.renderUI.apply(instance, arguments);

						var container = instance.get('container');

						container.addClass(CSS_FIELD);

						var wrapper = container.one('.field-wrapper');

						wrapper.append('<div class="' + CSS_TOOLBAR_CONTAINER + '"></div>');

						wrapper.addClass(CSS_FIELD_CONTENT_TOOLBAR);
					},

					destructor: function() {
						var instance = this;

						instance.get('settingsForm').destroy();
						instance.get('settingsModal').destroy();
					},

					_getDefinitionFromSettingsForm: function() {
						var instance = this;

						var definition = {};

						var settingsForm = instance.get('settingsForm');

						var fieldSettingsJSON = settingsForm.toJSON();

						AArray.each(
							fieldSettingsJSON.fields,
							function(item) {
								definition[item.name] = item.value;
							}
						);

						definition.type = instance.get('fieldType');

						return definition;
					},

					_getSettingsFormValuesFromDefinition: function() {
						var instance = this;

						var values = { fields: [] };

						A.each(
							instance.get('definition'),
							function(item, index) {
								values.fields.push(
									{
										name: index,
										value: item
									}
								);
							}
						);

						return values;
					},

					_onSubmitSettings: function(event) {
						var instance = this;

						event.preventDefault();

						instance._saveSettings();

						var settingsModal = instance.get('settingsModal');

						settingsModal.hide();
					},

					_renderSettingsModal: function() {
						var instance = this;

						var settingsModal = instance.get('settingsModal');

						var settingsForm = instance.get('settingsForm');

						var settingsFormContainer = settingsForm.get('container');

						settingsFormContainer.show();

						settingsModal.render();

						settingsModal.show();

						settingsModal.align();

						Liferay.Util.focusFormField(settingsFormContainer.one('input'));
					},

					_saveSettings: function() {
						var instance = this;

						var settingsForm = instance.get('settingsForm');

						instance.set('definition', instance._getDefinitionFromSettingsForm());
						instance.set('form', settingsForm);
						instance.set('parent', settingsForm);

						instance.fire(
							'field:saveSettings',
							{
								field: instance
							}
						);

						instance.render();
					},

					_valueSettingsForm: function() {
						var instance = this;

						var container = A.Node.create(TPL_SETTINGS_CONTAINER);

						var fieldType = FieldTypes.get(instance.get('fieldType'));

						container.append('<button type="submit" style="display:none;" />');

						container.on('submit', A.bind(instance._onSubmitSettings, instance));

						return new Liferay.DDM.Renderer.Form(
							{
								container: container,
								definition: fieldType.get('settings'),
								portletNamespace: instance.get('portletNamespace'),
								values: instance._getSettingsFormValuesFromDefinition()
							}
						);
					},

					_valueSettingsModal: function() {
						var instance = this;

						var settingsForm = instance.get('settingsForm');

						var settingsModal = new A.Modal(
							{
								bodyContent: settingsForm.get('container'),
								centered: true,
								destroyOnHide: false,
								draggable: false,
								modal: true,
								resizable: false,
								toolbars: {},
								visible: false,
								zIndex: Liferay.zIndex.WINDOW
							}
						);

						settingsModal.addToolbar(
							[
								{
									label: Liferay.Language.get('cancel'),
									on: {
										click: A.bind(settingsModal.hide, settingsModal)
									}
								},
								{
									label: Liferay.Language.get('save'),
									on: {
										click: A.bind(instance._onSubmitSettings, instance)
									},
									primary: true
								}
							]
						);

						return settingsModal;
					},

					showSettingsPanel: function(fieldTypeName) {
						var instance = this;

						instance.set('fieldType', fieldTypeName);

						instance._renderSettingsModal();
					}
				}
			}
		);

		Liferay.namespace('DDL').FormBuilderField = FormBuilderField;
	},
	'',
	{
		requires: ['liferay-checkbox-field', 'liferay-ddm-form-field-types', 'liferay-ddm-form-renderer', 'liferay-ddm-form-renderer-field', 'liferay-select-field', 'liferay-text-field']
	}
);