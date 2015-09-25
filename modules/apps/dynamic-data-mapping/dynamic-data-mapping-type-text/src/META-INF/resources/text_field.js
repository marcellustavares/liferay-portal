AUI.add(
	'liferay-ddm-form-field-text',
	function(A) {
		var Lang = A.Lang;

		new A.TooltipDelegate(
			{
				position: 'left',
				trigger: '.lfr-ddm-form-field-container .help-icon',
				visible: false
			}
		);

		var TextField = A.Component.create(
			{
				ATTRS: {
					displayStyle: {
						value: 'singleline'
					},

					placeholder: {
						value: ''
					},

					type: {
						value: 'text'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-text',

				prototype: {
					getTemplateContext: function() {
						var instance = this;

						return A.merge(
							TextField.superclass.getTemplateContext.apply(instance, arguments),
							{
								displayStyle: instance.get('displayStyle'),
								placeholder: instance._getLocalizedValue(instance.get('placeholder')),
								tip: instance._getLocalizedValue(instance.get('tip'))
							}
						);
					},

					_getLocalizedValue: function(localizedValue) {
						var instance = this;

						if (Lang.isObject(localizedValue)) {
							localizedValue = localizedValue[instance.get('locale')];
						}

						return localizedValue;
					},

					_renderErrorMessage: function() {
						var instance = this;

						TextField.superclass._renderErrorMessage.apply(instance, arguments);

						var container = instance.get('container');

						var inputGroup = container.one('.input-group-default');

						if (inputGroup) {
							inputGroup.insert(container.one('.validation-message'), 'after');
						}
					},

					_showFeedback: function() {
						var instance = this;

						TextField.superclass._showFeedback.apply(instance, arguments);

						var container = instance.get('container');

						var feedBack = container.one('.form-control-feedback');

						var inputGroupAddOn = container.one('.input-group-addon');

						if (inputGroupAddOn) {
							feedBack.appendTo(inputGroupAddOn);
						}
						else {
							var inputGroupContainer = container.one('.input-group-container');

							inputGroupContainer.insert(feedBack, 'after');
						}
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').Text = TextField;
	},
	'',
	{
		requires: ['aui-tooltip', 'liferay-ddm-form-renderer-field']
	}
);