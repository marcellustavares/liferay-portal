AUI.add(
	'liferay-forms-steps',
	function(A) {
		var Lang = A.Lang;

		var FormSteps = A.Component.create(
			{
				ATTRS: {
					currentStep: {
						valueFn: '_valueCurrentStep'
					},

					form: {
						value: null
					},

					tabView: {
						value: null
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'liferay-forms-steps',

				prototype: {
					initializer: function(config) {
						var instance = this;

						instance.nextBtn = instance.one('.forms-next');
						instance.prevBtn = instance.one('.forms-previous');
						instance.submitBtn = instance.one('.forms-submit');

						instance.form = instance.get('form');

						instance.form.addTarget(instance);

						instance.tabView = instance.get('tabView');

						instance.tabView.addTarget(instance);

						instance.validator = instance.form.formValidator;

						instance.validator.set('validateOnInput', true);

						instance.validator.addTarget(instance);

						instance.bindUI();
						instance.syncUI();
					},

					bindUI: function() {
						var instance = this;

						instance.after('currentStepChange', instance._afterCurrentStepChange, instance);

						instance.after('tab:selectedChange', instance._afterTabSelectedChange, instance);
						instance.on('tab:selectedChange', instance._onTabSelectedChange, instance);

						instance.nextBtn.on('click', instance._onClickNext, instance);
						instance.prevBtn.on('click', instance._onClickPrev, instance);
					},

					syncUI: function() {
						var instance = this;

						instance.updateNavigationControls();

						instance.validator.resetAllFields();
					},

					_afterCurrentStepChange: function(event) {
						var instance = this;

						instance.syncUI();
					},

					_afterTabSelectedChange: function(event) {
						var instance = this;

						var tabView = instance.tabView;

						if (event.newVal === 1) {
							var activeTabIndex = tabView.indexOf(event.target);

							var currentStep = activeTabIndex + 1;

							instance.set('currentStep', currentStep);
						}
					},

					_onClickNext: function(event) {
						var instance = this;

						instance.navigate(1);
					},

					_onClickPrev: function(event) {
						var instance = this;

						instance.navigate(-1);
					},

					_onTabSelectedChange: function(event) {
						var instance = this;

						var tabView = instance.tabView;

						if (event.newVal === 1) {
							var activeTabIndex = tabView.indexOf(event.target);

							if (!instance.validateStep(activeTabIndex)) {
								event.preventDefault();
							}
						}
					},

					_isCurrentStepValid: function() {
						var instance = this;

						var currentStep = instance.get('currentStep');

						return instance.validateStep(currentStep);
					},

					_valueCurrentStep: function() {
						var instance = this;

						var tabView = instance.get('tabView');

						var activeTabIndex = tabView.indexOf(tabView.get('selection'));

						return activeTabIndex + 1;
					},

					getTabViewPanels: function() {
						var instance = this;

						var queries = A.TabviewBase._queries;

						return instance.tabView.get('contentBox').all(queries.tabPanel);
					},

					navigate: function(offset) {
						var instance = this;

						var tabView = instance.tabView;

						var tabViewTabs = tabView.getTabs();
						var activeTab = tabView.getActiveTab();

						var newActiveTabIndex = tabViewTabs.indexOf(activeTab) + offset;

						var newActiveTab = tabView.item(newActiveTabIndex);

						if (newActiveTab) {
							tabView.selectChild(newActiveTabIndex);
						}
					},

					updateNavigationControls: function(currentStepValid) {
						var instance = this;

						if (currentStepValid === undefined) {
							currentStepValid = instance._isCurrentStepValid();
						}

						instance.nextBtn.attr('disabled', !currentStepValid);
						instance.nextBtn.toggleClass('disabled', !currentStepValid);

						instance.submitBtn.attr('disabled', !currentStepValid);
						instance.submitBtn.toggleClass('disabled', !currentStepValid);

						var currentStep = instance.get('currentStep');

						if (currentStep === 1) {
							instance.nextBtn.show();
							instance.prevBtn.hide();
							instance.submitBtn.hide();
						}
						else {
							instance.nextBtn.hide();
							instance.prevBtn.show();
							instance.submitBtn.show();
						}
					},

					validatePanel: function(panel) {
						var instance = this;

						var validator = instance.validator;

						validator.eachRule(
							function(rule, fieldName) {
								var field = validator.getField(fieldName);

								if (panel.contains(field)) {
									validator.validateField(field);
								}
							}
						);
					},

					validateStep: function(step) {
						var instance = this;

						var tabViewPanels = instance.getTabViewPanels();

						var tabViewTabs = instance.tabView.getTabs();

						var valid = true;

						instance.validator.resetAllFields();

						tabViewPanels.each(
							function(item, index, collection) {
								if (index <= (step - 1)) {
									instance.validatePanel(item);

									var tabNode = tabViewTabs.item(index);

									var tabHasError = item.one('.error-field');

									tabNode.toggleClass('section-error', tabHasError);
									tabNode.toggleClass('section-success', !tabHasError);

									if (tabHasError) {
										valid = false;
									}
								}
							}
						);

						return valid;
					}
				}
			}
		);

		Liferay.namespace('Forms').Steps = FormSteps;
	},
	'',
	{
		requires: ['aui-base', 'aui-tabview', 'liferay-portlet-base']
	}
);