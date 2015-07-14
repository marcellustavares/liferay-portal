AUI.add(
	'liferay-ddm-form-renderer-validation',
	function(A) {
		var Renderer = Liferay.DDM.Renderer;

		var Util = Renderer.Util;

		var FormValidationSupport = function() {
		};

		FormValidationSupport.ATTRS = {
			url: {
				value: ''
			}
		};

		FormValidationSupport.prototype = {
			initializer: function() {
				var instance = this;
			},

			validate: function(callback) {
				var instance = this;

				A.io.request(
					instance.get('url'),
					{
						dataType: 'JSON',
						on: {
							error: function() {
								callback(false);
							},
							success: function() {
								callback(instance._validateResponse(this.get('responseData')));
							}
						}
					}
				);
			},

			_validateResponse: function(responseData) {
				var instance = this;

				var valid = true;

				instance.eachField(
					function(field) {
						var instanceId = field.get('instanceId');

						var data = Util.getFieldByKey(responseData, instanceId, 'instanceId');

						if (data.valid === false) {
							valid = false;
						}

						var messages = data.messages;

						if (messages && messages.length) {
							field.set('errorMessages', messages);
						}
					}
				);

				return valid;
			}
		};

		Liferay.namespace('DDM.Renderer').FormValidationSupport = FormValidationSupport;
	},
	'',
	{
		requires: ['aui-request']
	}
);