AUI.add(
	'liferay-forms-portlet',
	function(A) {
		var Lang = A.Lang;
		var DefinitionSerializer = Liferay.Forms.DefinitionSerializer;
		var LayoutSerializer = Liferay.Forms.LayoutSerializer;

		var FormsPortlet = A.Component.create(
			{
				ATTRS: {
					editFormName: {
						setter: 'ns'
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'liferay-forms-portlet',

				prototype: {
					initializer: function(config) {
						var instance = this;

						instance.definitionSerializer = new DefinitionSerializer();
						instance.layoutSerializer = new LayoutSerializer();

						instance.bindUI();
					},

					bindUI: function() {
						var instance = this;

						instance._eventHandlers = [
							Liferay.after('form:registered', A.bind(instance._afterFormRegistered, instance)),
							Liferay.on('destroyPortlet', A.bind(instance._onDestroyPortlet, instance))
						];
					},

					destructor: function() {
						var instance = this;

						(new A.EventHandle(instance._eventHandlers)).detach();
					},

					_afterFormRegistered: function(event) {
						var instance = this;

						var editForm = Liferay.Form.get(instance.get('editFormName'));;

						if (editForm === event.form) {
							Liferay.component(
								instance.ns('FormSteps'),
								function() {
									return new Liferay.Forms.Steps(
										{
											form: editForm,
											namespace: instance.get('namespace'),
											tabView: Liferay.component(instance.ns('fmTabview'))
										}
									);
								}
							);

							Liferay.component(instance.ns('FormSteps')).syncUI();

							editForm.set('onSubmit', A.bind(instance._onSubmitEditForm, instance));
						}
					},

					_onDestroyPortlet: function(event) {
						var instance = this;

						instance.destroy();
					},

					_onSubmitEditForm: function(event) {
						var instance = this;

						event.preventDefault();

						var formBuilder = Liferay.component(instance.ns('FormBuilder'));

						var layout = formBuilder.get('layout');

						var definitionInput = instance.one('#definition');

						instance.definitionSerializer.set('layout', layout);

						definitionInput.val(instance.definitionSerializer.serialize());

						console.log(definitionInput.val());

						var layoutInput = instance.one('#layout');

						instance.layoutSerializer.set('layout', layout);

						layoutInput.val(instance.layoutSerializer.serialize());

						console.log(layoutInput.val());
					}
				}
			}
		);

		Liferay.namespace('Forms').Portlet = FormsPortlet;
	},
	'',
	{
		requires: ['aui-base', 'liferay-forms-definition-serializer', 'liferay-forms-layout-serializer', 'liferay-forms-steps', 'liferay-portlet-base']
	}
);