AUI.add(
	'liferay-ddm-form-field-text',
	function(A) {
		var Lang = A.Lang;

		var TextField = A.Component.create(
			{
				ATTRS: {
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

						var tip = instance.get('tip');

						if (Lang.isObject(tip)) {
							tip = tip[instance.get('locale')];
						}

						return A.merge(
							TextField.superclass.getTemplateContext.apply(instance, arguments),
							{
								placeholder: instance.get('placeholder'),
								tip: tip
							}
						);
					},

					render: function() {
						var instance = this;

						TextField.superclass.render.apply(instance, arguments);

						var container = instance.get('container');

						new A.Tooltip(
							{
								position: 'left',
								trigger: container.one('.help-icon'),
								visible: false
							}
						).render();
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