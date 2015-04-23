AUI.add(
	'liferay-forms-portlet',
	function(A) {
		var Lang = A.Lang;
		var DefinitionSerializer = Liferay.Forms.DefinitionSerializer;
		var LayoutSerializer = Liferay.Forms.LayoutSerializer;

		var FormsPortlet = A.Component.create(
			{
				ATTRS: {
					editForm: {
					},

					tabView: {
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'liferay-forms-portlet',

				prototype: {
					initializer: function() {
						var instance = this;

						instance.definitionSerializer = new DefinitionSerializer();

						instance.formSteps = new Liferay.Forms.Steps(
							{
								form: instance.get('editForm'),
								namespace: instance.get('namespace'),
								tabView: instance.get('tabView')
							}
						);

						instance.layoutSerializer = new LayoutSerializer();

						instance.bindUI();
					},

					bindUI: function() {
						var instance = this;

						var editForm = instance.get('editForm');

						editForm.set('onSubmit', A.bind(instance._onSubmitEditForm, instance));

						instance._eventHandlers = [
							Liferay.after('form:registered', A.bind(instance._afterFormRegistered, instance)),
							Liferay.on('destroyPortlet', A.bind(instance._onDestroyPortlet, instance))
						];
					},

					destructor: function() {
						var instance = this;

						(new A.EventHandle(instance._eventHandlers)).detach();

						instance.formSteps.destroy();
					},

					_onDestroyPortlet: function(event) {
						var instance = this;

						instance.destroy();
					},

					_onSubmitEditForm: function(event) {
						var instance = this;

						var formBuilder = Liferay.component(instance.ns('FormBuilder'));

						var layout = formBuilder.get('layout');

						var definitionInput = instance.one('#definition');

						instance.definitionSerializer.set('layout', layout);

						definitionInput.val(instance.definitionSerializer.serialize());

						var layoutInput = instance.one('#layout');

						instance.layoutSerializer.set('layout', layout);

						layoutInput.val(instance.layoutSerializer.serialize());
					}
				}
			}
		);

		Liferay.namespace('Forms').Portlet = FormsPortlet;
	},
	'',
	{
		requires: ['liferay-form', 'liferay-forms-definition-serializer', 'liferay-forms-layout-serializer', 'liferay-forms-steps', 'liferay-portlet-base']
	}
);