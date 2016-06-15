AUI.add(
	'liferay-ddm-form-renderer-validation',
	function(A) {
		var Lang = A.Lang;

		var Renderer = Liferay.DDM.Renderer;

		var FormValidationSupport = function() {
		};

		FormValidationSupport.ATTRS = {
			evaluation: {
				value: null
			},

			evaluatorURL: {
				value: ''
			},

			evaluator: {
				valueFn: '_valueEvaluator'
			}
		};

		FormValidationSupport.prototype = {
			initializer: function() {
				var instance = this;

				var evaluator = instance.get('evaluator');

				instance._eventHandlers.push(
					evaluator.after('evaluationEnded', A.bind('_afterEvaluationEnded', instance)),
					evaluator.after('evaluationStarted', A.bind('_afterEvaluationStarted', instance))
				);
			},

			hasErrors: function() {
				var instance = this;

				var hasErrors = false;

				instance.eachField(
					function(field) {
						hasErrors = field.hasErrors();

						return hasErrors;
					}
				);

				return hasErrors;
			},

			pageHasErrors: function(pageNode) {
				var instance = this;

				var hasErrors = false;

				instance.eachField(
					function(field) {
						if (pageNode.contains(field.get('container'))) {
							hasErrors = field.hasErrors();
						}

						return hasErrors;
					}
				);

				return hasErrors;
			},

			processPageValidation: function(pageNode, result) {
				var instance = this;

				var fieldToFocus;

				instance.eachField(
					function(field) {
						if (pageNode.contains(field.get('container'))) {
							field.processValidation(result);

							if (field.hasErrors() && !fieldToFocus) {
								fieldToFocus = field;
							}
						}
					}
				);

				if (fieldToFocus) {
					fieldToFocus.focus();
				}
			},

			processValidation: function(result) {
				var instance = this;

				var fieldToFocus;

				instance.eachField(
					function(field) {
						field.processValidation(result);

						if (field.hasErrors() && !fieldToFocus) {
							fieldToFocus = field;
						}
					}
				);

				if (fieldToFocus) {
					fieldToFocus.focus();
				}
			},

			validate: function(callback) {
				var instance = this;

				var evaluator = instance.get('evaluator');

				evaluator.evaluate(
					instance,
					function(result) {
						var hasErrors = true;

						if (result && Lang.isObject(result)) {
							instance.processValidation(result);

							hasErrors = instance.hasErrors();
						}

						if (callback) {
							callback.call(instance, hasErrors, result);
						}
					}
				);
			},

			validatePage: function(pageNode, callback) {
				var instance = this;

				var evaluator = instance.get('evaluator');

				evaluator.evaluate(
					instance,
					function(result) {
						var hasErrors = true;

						if (result && Lang.isObject(result)) {
							instance.processPageValidation(pageNode, result);

							hasErrors = instance.pageHasErrors(pageNode);
						}

						if (callback) {
							callback.call(instance, hasErrors, result);
						}
					}
				);
			},

			_afterEvaluationEnded: function(event) {
				var instance = this;

				var result = event.result;

				if (event.trigger === instance) {
					instance.hideFeedback();
				}

				if (!result || !Lang.isObject(result)) {
					var strings = instance.get('strings');

					instance.showAlert(strings.requestErrorMessage);
				}
			},

			_afterEvaluationStarted: function(event) {
				var instance = this;

				if (event.trigger === instance) {
					instance.showLoadingFeedback();
				}
			},

			_valueEvaluator: function() {
				var instance = this;

				return new Renderer.ExpressionsEvaluator(
					{
						form: instance
					}
				);
			}
		};

		Liferay.namespace('DDM.Renderer').FormValidationSupport = FormValidationSupport;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-expressions-evaluator']
	}
);