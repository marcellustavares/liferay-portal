AUI.add(
	'liferay-ddm-form-field-checkbox',
	function(A) {
		var CheckboxField = A.Component.create(
			{
				ATTRS: {
					options: {
						validator: Array.isArray,
						value: []
					},

					showAsSwitcher: {
						value: false
					},

					type: {
						value: 'checkbox'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-checkbox',

				prototype: {
					getContextValue: function() {
						var instance = this;

						var value = CheckboxField.superclass.getContextValue.apply(instance, arguments);

						if (!Array.isArray(value)) {
							try {
								value = JSON.parse(value);
							}
							catch (e) {
								value = [value];
							}
						}

						return value[0];
					},

					getInputNode: function() {
						var instance = this;

						var container = instance.get('container');

						var checkboxesNodeList = container.all(instance.getInputSelector());

						var inputNode = checkboxesNodeList.item(0);

						var checkedNodeList = checkboxesNodeList.filter(':checked');

						if (checkedNodeList.size()) {
							inputNode = checkedNodeList.item(0);
						}

						return inputNode;
					},

					getOptions: function() {
						var instance = this;

						var value = instance.getContextValue();

						return A.map(
							instance.get('options'),
							function(item) {
								return {
									label: item.label[instance.get('locale')],
									status: value === item.value ? 'checked' : '',
									value: item.value
								};
							}
						);
					},

					getTemplateContext: function() {
						var instance = this;

						var value = instance.getContextValue();

						return A.merge(
							CheckboxField.superclass.getTemplateContext.apply(instance, arguments),
							{
								options: instance.getOptions(),
								showAsSwitcher: instance.get('showAsSwitcher')
							}
						);
					},

					getValue: function() {
						var instance = this;

						var container = instance.get('container');

						var checkboxesNodeList = container.all(instance.getInputSelector());

						var checkedNodeList = checkboxesNodeList.filter(':checked');

						var value = '';

						if (checkedNodeList.size()) {
							value = checkedNodeList.item(0).val();
						}

						return value;
					},

					setValue: function(value) {
						var instance = this;

						var container = instance.get('container');

						var checkboxesNodeList = container.all(instance.getInputSelector());

						checkboxesNodeList.attr('checked', false);

						var checkboxesToCheck = checkboxesNodeList.filter(
							function(node) {
								return node.val() === value;
							}
						);

						checkboxesToCheck.attr('checked', true);
					},

					_renderErrorMessage: function() {
						var instance = this;

						var container = instance.get('container');

						CheckboxField.superclass._renderErrorMessage.apply(instance, arguments);

						container.all('.help-block').appendTo(container);
					},

					_showFeedback: function() {
						var instance = this;

						var container = instance.get('container');

						CheckboxField.superclass._showFeedback.apply(instance, arguments);

						container.all('.form-control-feedback').appendTo(container);
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').Checkbox = CheckboxField;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-field']
	}
);