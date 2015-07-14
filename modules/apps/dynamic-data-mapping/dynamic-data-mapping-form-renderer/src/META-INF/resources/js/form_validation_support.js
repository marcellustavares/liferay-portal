AUI.add(
	'liferay-ddm-form-renderer-validation',
	function(A) {
		var Renderer = Liferay.DDM.Renderer;

		var Util = Renderer.Util;

		var FormValidationSupport = function() {
		};

		FormValidationSupport.ATTRS = {
			validationURL: {
				value: ''
			}
		};

		FormValidationSupport.prototype = {
			validate: function(callback) {
				var instance = this;

				A.io.request(
					instance.get('validationURL'),
					{
						dataType: 'JSON',
						on: {
							error: function() {
								callback(false);
							},
							success: function() {
								var valid = instance._validateResponse(this.get('responseData'));

								callback(valid);
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

						var messages = data.messages;

						if (messages && messages.length) {
							field.set('errorMessages', messages);
						}

						if (data.valid === false) {
							valid = false;
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