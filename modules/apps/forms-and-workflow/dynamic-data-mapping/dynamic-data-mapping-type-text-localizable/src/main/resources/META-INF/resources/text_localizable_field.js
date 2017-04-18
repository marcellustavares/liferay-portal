AUI.add(
	'liferay-ddm-form-field-text-localizable',
	function(A) {
		var Renderer = Liferay.DDM.Renderer;
		var AArray = A.Array;

		new A.TooltipDelegate(
			{
				position: 'left',
				trigger: '.liferay-ddm-form-field-text-localizable .help-icon',
				triggerHideEvent: ['blur', 'mouseleave'],
				triggerShowEvent: ['focus', 'mouseover'],
				visible: false
			}
		);

		var TextLocalizableField = A.Component.create(
			{
				ATTRS: {
					availableLocalesMetadata: {
						validator: AArray.isArray,
						value:[]
					},

					placeholder: {
						state: true,
						value: ''
					},

					type: {
						value: 'text_localizable'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-text-localizable',

				prototype: {
					initializer: function() {
						var instance = this;

						Liferay.InputLocalized.unregister(instance.getQualifiedName());

						instance._eventHandlers.push(
							instance.bindContainerEvent('blur', instance._hideLocalizedPanel, '.liferay-ddm-form-field-text-localizable'),
							instance.bindContainerEvent('focus', instance._showLocalizedPanel, '.liferay-ddm-form-field-text-localizable'),
							instance.bindContainerEvent('mouseenter', instance._showLocalizedPanel, '.liferay-ddm-form-field-text-localizable'),
							instance.bindContainerEvent('mouseleave', instance._hideLocalizedPanel, '.liferay-ddm-form-field-text-localizable')
						);
					},

					getChangeEventName: function() {
						return 'input';
					},

					getValue: function() {
						var instance = this;

						var localizedValues = {};

						var inputLocalized = instance._getInputLocalizedInstance();

						var languageIds = instance._getLanguageIds();

						if(!inputLocalized) {
							languageIds.forEach(function(languageId) {
								localizedValues[languageId] = '';
							});
						}
						else {
							languageIds.forEach(function(languageId) {
								localizedValues[languageId] = inputLocalized.getValue(languageId);
							});
						}

						return localizedValues;
					},

					showErrorMessage: function() {
						var instance = this;

						TextLocalizableField.superclass.showErrorMessage.apply(instance, arguments);

						var container = instance.get('container');

						var inputGroup = container.one('.input-group-container');

						inputGroup.insert(container.one('.help-block'), 'after');
					},

					_createInputLocalized: function() {
						var instance = this;

						var inputParentNode = instance.getInputNode().get('parentNode');

						Liferay.InputLocalized.register(
							instance.getQualifiedName(),
							{
								boundingBox: inputParentNode,
								columns: 20,
								contentBox: inputParentNode.one('.input-localized-content'),
								defaultLanguageId: instance.get('locale'),
								fieldPrefix: "",
								fieldPrefixSeparator: "",
								id: instance.get('fieldName'),
								inputPlaceholder: instance.getInputSelector(),
								instanceId: instance.getQualifiedName(),
								items: instance._getLanguageIds(),
								itemsError: [],
								lazy: true,
								name: instance.get('fieldName'),
								namespace: instance.get('portletNamespace'),
								toggleSelection: false,
								translatedLanguages: instance.get('locale')
							}
						);
					},

					_getInputLocalizedInstance: function() {
						var instance = this;

						return Liferay.InputLocalized._instances[instance.getQualifiedName()];
					},

					_getLanguageIds: function() {
						var instance = this;

						var availableLocalesMetadata = instance.get('availableLocalesMetadata');

						var languageIds = [];

						availableLocalesMetadata.forEach(function(availableLocaleMetadata) {
							languageIds.push(availableLocaleMetadata.languageId);
						});

						return languageIds;
					},

					_hideLocalizedPanel: function() {
						var instance = this;

						if (!instance.get('container').one('.input-localized').hasClass('input-localized-focused')) {
							instance.get('container').one('.input-localized-content').addClass('hidden');
						}
					},

					_showLocalizedPanel: function() {
						var instance = this;

						if (!instance._getInputLocalizedInstance()) {
							instance._createInputLocalized();
						}

						instance.get('container').one('.input-localized-content').removeClass('hidden');
					}

				}
			}
		);

		Liferay.namespace('DDM.Field').TextLocalizable = TextLocalizableField;
	},
	'',
	{
		requires: ['aui-autosize-deprecated', 'aui-tooltip', 'liferay-ddm-form-renderer-field', 'liferay-input-localized']
	}
);