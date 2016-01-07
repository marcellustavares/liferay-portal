AUI.add(
	'liferay-ddl-portlet',
	function(A) {
		var DefinitionSerializer = Liferay.DDL.DefinitionSerializer;
		var LayoutSerializer = Liferay.DDL.LayoutSerializer;

		var TPL_BUTTON_SPINNER = '<span><span class="icon-spinner"></span></span>';

		var DDLPortlet = A.Component.create(
			{
				ATTRS: {
					dataProviders: {
					},

					definition: {
					},

					editForm: {
					},

					formBuilder: {
						valueFn: '_valueFormBuilder'
					},

					layout: {
					},

					publishRecordSetURL: {
					},

					recordSetId: {
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'liferay-ddl-portlet',

				openDDMDataProvider: function(dataProviderURL) {
					Liferay.Util.openWindow(
						{
							dialog: {
								cssClass: 'dynamic-data-mapping-data-providers-modal',
								destroyOnHide: true
							},
							id: 'ddmDataProvider',
							title: Liferay.Language.get('data-providers'),
							uri: dataProviderURL
						}
					);
				},

				prototype: {
					initializer: function() {
						var instance = this;

						instance.definitionSerializer = new DefinitionSerializer();

						instance.layoutSerializer = new LayoutSerializer(
							{
								builder: instance.get('formBuilder')
							}
						);

						instance.renderUI();

						instance.bindUI();
					},

					renderUI: function() {
						var instance = this;

						instance.one('#loader').remove();

						instance.get('formBuilder').render(instance.one('#formBuilder'));

						instance.enableButtons();
					},

					bindUI: function() {
						var instance = this;

						var editForm = instance.get('editForm');

						editForm.set('onSubmit', A.bind('_onSubmitEditForm', instance));

						instance._eventHandlers = [
							Liferay.on('destroyPortlet', A.bind('_onDestroyPortlet', instance))
						];
					},

					destructor: function() {
						var instance = this;

						instance.get('formBuilder').destroy();

						(new A.EventHandle(instance._eventHandlers)).detach();
					},

					enableButtons: function() {
						var instance = this;

						var buttons = instance.all('.ddl-button');

						Liferay.Util.toggleDisabled(buttons, false);
					},

					openPublishModal: function() {
						var instance = this;

						var publishCheckbox = instance.one('#publishCheckbox');

						var publishModalID = instance.ns('publishModalContainer');

						var publishModalNode = instance.one('#' + publishModalID);

						publishCheckbox.setData('previousValue', publishCheckbox.attr('checked'));

						if (publishModalNode) {
							Liferay.Util.getWindow(publishModalID).show();
						}
						else {
							instance._createPublishModal();
						}
					},

					serializeFormBuilder: function() {
						var instance = this;

						var description = window[instance.ns('descriptionEditor')].getHTML();

						instance.one('#description').val(description);

						var formBuilder = instance.get('formBuilder');

						var pages = formBuilder.get('layouts');

						var definitionInput = instance.one('#definition');

						instance.definitionSerializer.set('pages', pages);

						definitionInput.val(instance.definitionSerializer.serialize());

						var layoutInput = instance.one('#layout');

						instance.layoutSerializer.set('pages', pages);

						layoutInput.val(instance.layoutSerializer.serialize());

						var name = window[instance.ns('nameEditor')].getHTML();

						instance.one('#name').val(name);

						var settingsInput = instance.one('#serializedSettingsDDMFormValues');

						var settings = Liferay.component('settingsDDMForm').toJSON();

						settingsInput.val(JSON.stringify(settings));
					},

					submitForm: function() {
						var instance = this;

						instance.serializeFormBuilder();

						var submitButton = instance.one('#submit');

						submitButton.html(Liferay.Language.get('saving'));

						submitButton.append(TPL_BUTTON_SPINNER);

						var editForm = instance.get('editForm');

						submitForm(editForm.form);
					},

					_afterCopyToClipBoardButtonClick: function() {
						var instance = this;

						var message;

						if (!instance._copyPublishURL()) {
							message = Liferay.Language.get('unable-to-copy-to-clipboard') + ' ' + Liferay.Language.get('copy-directly');
						}
						else {
							message = Liferay.Language.get('copied-to-clipboard');
						}

						instance._showCopyToClipboardMessage(message);
					},

					_afterOpenPublishModal: function(dialogWindow) {
						var instance = this;

						var bodyNode = dialogWindow.bodyNode;

						var publishNodeID = instance.ns('publishModal');

						var publishNode = instance.one('#' + publishNodeID);

						if (publishNode) {
							publishNode.show();

							bodyNode.append(publishNode);

							instance._bindPublishModal();
						}
					},

					_bindPublishModal: function() {
						var instance = this;

						A.one('.button-to-copy').on('click', A.bind('_afterCopyToClipBoardButtonClick', instance));

						Liferay.Util.getWindow(instance.ns('publishModalContainer')).after('visibleChange', function() {
							var popover = A.Widget.getByNode(A.one('#copyToClipboardMessage'));

							if (popover) {
								popover.destroy();
							}
						});
					},

					_copyPublishURL: function() {
						var instance = this;

						var message;

						var textElement = A.one('.text-to-copy');

						textElement.getDOMNode().select();

						return document.execCommand('copy');
					},

					_createPublishModal: function() {
						var instance = this;

						Liferay.Util.openWindow(
							{
								dialog: {
									cssClass: 'publish-modal-container',
									height: 400,
									resizable: false,
									width: 720,
									'toolbars.footer': [
										{
											cssClass: 'btn-lg btn-primary',
											label: Liferay.Language.get('confirm'),
											on: {
												click: A.bind('_onConfirmPublishModal', instance)
											}
										},
										{
											cssClass: 'btn-lg btn-link',
											label: Liferay.Language.get('cancel'),
											on: {
												click: A.bind('_onCancelPublishModal', instance)
											}
										}
									]
								},
								id: instance.ns('publishModalContainer'),
								title: Liferay.Language.get('publish')
							},
							A.bind('_afterOpenPublishModal', instance)
						);
					},

					_onCancelPublishModal: function() {
						var instance = this;

						var publishCheckbox = instance.one('#publishCheckbox');

						publishCheckbox.attr('checked', publishCheckbox.getData('previousValue'));

						Liferay.Util.getWindow(instance.ns('publishModalContainer')).hide();
					},

					_onConfirmPublishModal: function() {
						var instance = this;

						instance._setFormAsPublish();

						Liferay.Util.getWindow(instance.ns('publishModalContainer')).hide();
					},

					_onDestroyPortlet: function(event) {
						var instance = this;

						instance.destroy();
					},

					_onSubmitEditForm: function(event) {
						var instance = this;

						event.preventDefault();

						instance.serializeFormBuilder();

						instance.submitForm();
					},

					_setFormAsPublish: function() {
						var instance = this;

						var publishCheckbox = instance.one('#publishCheckbox');

						var payload = instance.ns(
							{
								published: publishCheckbox.attr('checked'),
								recordSetId: instance.get('recordSetId')
							}
						);

						A.io.request(
							instance.get('publishRecordSetURL'),
							{
								data: payload,
								dataType: 'JSON',
								method: 'POST'
							}
						);
					},

					_showCopyToClipboardMessage: function(message) {
						var instance = this;

						var popover = A.Widget.getByNode(A.one('#copyToClipboardMessage'));

						if (!popover) {
							popover = new A.Popover(
								{
									align: {
										node: A.one('.text-to-copy'),
										points:[A.WidgetPositionAlign.BC, A.WidgetPositionAlign.TC]
									},
									position: 'top',
									plugins: [A.Plugin.WidgetAnim],
									id: 'copyToClipboardMessage',
									visible: false,
									zIndex: 90000
								}
							);
						}

						popover.set('bodyContent', message);

						if (!popover.get('visible')) {
							setTimeout(function() {
								if (popover) {
									popover.hide();
								}
							}, 3000);
						}

						popover.set('visible', true);

						popover.render();

						popover.hide();

						popover.show();
					},

					_valueFormBuilder: function() {
						var instance = this;

						var layout = instance.get('layout');

						return new Liferay.DDL.FormBuilder(
							{
								dataProviders: instance.get('dataProviders'),
								definition: instance.get('definition'),
								pagesJSON: layout.pages,
								portletNamespace: instance.get('namespace')
							}
						);
					}
				}
			}
		);

		Liferay.namespace('DDL').Portlet = DDLPortlet;
	},
	'',
	{
		requires: ['liferay-ddl-form-builder', 'liferay-ddl-form-builder-definition-serializer', 'liferay-ddl-form-builder-layout-serializer', 'liferay-portlet-base', 'aui-popover', 'widget-anim']
	}
);