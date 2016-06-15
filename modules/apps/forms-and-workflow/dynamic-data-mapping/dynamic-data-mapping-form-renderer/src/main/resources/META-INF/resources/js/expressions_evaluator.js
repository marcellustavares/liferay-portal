AUI.add(
	'liferay-ddm-form-renderer-expressions-evaluator',
	function(A) {
		var ExpressionsEvaluator = A.Component.create(
			{
				ATTRS: {
					enabled: {
						getter: '_getEnabled',
						value: true
					},

					evaluatorURL: {
						valueFn: '_valueEvaluatorURL'
					},

					form: {
					}
				},

				NAME: 'liferay-ddm-form-renderer-expressions-evaluator',

				prototype: {
					initializer: function() {
						var instance = this;

						instance._queue = new A.Queue();

						instance.after('evaluationEnded', instance._afterEvaluationEnded);
					},

					evaluate: function(trigger, callback) {
						var instance = this;

						var enabled = instance.get('enabled');

						var form = instance.get('form');

						if (instance.isEvaluating()) {
							instance.stop();
						}

						if (enabled && form) {
							instance._queue.add(trigger);

							instance.fire(
								'evaluationStarted',
								{
									trigger: trigger
								}
							);

							form.disableSubmitButton();

							instance._evaluate(
								function(result) {
									form.enableSubmitButton();

									while (instance._queue.size() > 0) {
										var next = instance._queue.next();

										instance.fire(
											'evaluationEnded',
											{
												result: result,
												trigger: next
											}
										);
									}

									if (callback) {
										callback.apply(instance, arguments);
									}
								}
							);
						}
					},

					isEvaluating: function() {
						var instance = this;

						return instance._request !== undefined;
					},

					stop: function() {
						var instance = this;

						if (instance._request) {
							instance._request.destroy();

							delete instance._request;
						}
					},

					_afterEvaluationEnded: function() {
						var instance = this;

						instance.stop();
					},

					_evaluate: function(callback) {
						var instance = this;

						var form = instance.get('form');

						var payload = form.getEvaluationPayload();

						var portletNamespace = form.get('portletNamespace');

						instance._request = A.io.request(
							instance.get('evaluatorURL'),
							{
								data: Liferay.Util.ns(portletNamespace, payload),
								dataType: 'JSON',
								method: 'POST',
								on: {
									failure: function(event) {
										if (event.details[1].statusText !== 'abort') {
											callback.call(instance, null);
										}

										callback.call(instance, {});
									},
									success: function() {
										var result = this.get('responseData');

										callback.call(instance, result);
									}
								}
							}
						);
					},

					_getEnabled: function(enabled) {
						var instance = this;

						return enabled && !!instance.get('evaluatorURL');
					},

					_valueEvaluatorURL: function() {
						var instance = this;

						var evaluatorURL;

						var form = instance.get('form');

						if (form) {
							evaluatorURL = form.get('evaluatorURL');
						}

						return evaluatorURL;
					}
				}
			}
		);

		Liferay.namespace('DDM.Renderer').ExpressionsEvaluator = ExpressionsEvaluator;
	},
	'',
	{
		requires: ['aui-component', 'aui-io-request', 'queue']
	}
);