AUI.add(
	'liferay-ddm-form-renderer-field-events',
	function(A) {
		var FieldEventsSupport = function() {
		};

		FieldEventsSupport.prototype = {
			initializer: function() {
				var instance = this;

				instance._eventHandlers.push(
					instance.after(instance._afterEventsRender, instance, 'render')
				);

				instance._domEvents = [];
			},

			bindContainerEvent: function(eventName, callback, selector) {
				var instance = this;

				var container = instance.get('container');

				var query = selector;

				if (query.call) {
					query = query.call(instance);
				}

				var handler = container.delegate(eventName, A.bind(callback, instance), query);

				instance._domEvents.push(
					{
						callback: callback,
						handler: handler,
						name: eventName,
						selector: selector
					}
				);

				return handler;
			},

			bindInputEvent: function(eventName, callback) {
				var instance = this;

				return instance.bindContainerEvent(eventName, callback, instance.getInputSelector);
			},

			_afterEventsRender: function() {
				var instance = this;

				var events = instance._domEvents;

				instance._domEvents = [];

				var length = events.length;

				while (length--) {
					var event = events[length];

					event.handler.detach();

					instance.bindContainerEvent(event.name, event.callback, event.selector);
				}

				instance._bindDefaultEvents();
			},

			_bindDefaultEvents: function() {
				var instance = this;

				instance.bindInputEvent('blur', instance._onInputBlur);

				if (instance._isTextType()) {
					instance.bindInputEvent('input', A.debounce(instance._onValueChange, 150, instance));
				}
				else {
					instance.bindInputEvent('change', instance._onValueChange);
				}
			},

			_isTextType: function() {
				var instance = this;

				var inputNode = instance.getInputNode();

				var textType = false;

				if (inputNode) {
					var tagName = inputNode.get('tagName');

					textType = inputNode.get('type') === 'text' || tagName.toLowerCase() === 'textarea';
				}

				return textType;
			},

			_onInputBlur: function(event) {
				var instance = this;

				instance.fire(
					'blur',
					{
						domEvent: event,
						field: instance
					}
				);
			},

			_onValueChange: function(event) {
				var instance = this;

				if (instance.get('rendered')) {
					instance.fire(
						'valueChanged',
						{
							domEvent: event,
							field: instance,
							value: instance.getValue()
						}
					);
				}
			}
		};

		Liferay.namespace('DDM.Renderer').FieldEventsSupport = FieldEventsSupport;
	},
	'',
	{
		requires: []
	}
);