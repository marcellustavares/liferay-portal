AUI.add(
	'liferay-ddl-search-container',
	function(A) {
		var Lang = A.Lang;

		var SearchContainer = A.Component.create(
			{
				ATTRS: {
					duplicating: {
						value: false
					},

					searchContainer: {
						setter: 'byId'
					},

					toolbar: {
						setter: 'byId'
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'liferay-ddl-portlet',

				prototype: {
					initializer: function() {
						var instance = this;

						instance.bindUI();
					},

					bindUI: function() {
						var instance = this;

						var toolbar = instance.get('toolbar');

						instance._eventHandlers = [
							instance.after('duplicatingChange', A.bind(instance._afterDuplicatingChange, instance)),
							Liferay.on('destroyPortlet', A.bind(instance._onDestroyPortlet, instance)),
							toolbar.one('.duplicate-form').on('click', A.bind(instance._onClickDuplicateForm, instance))
						];
					},

					destructor: function() {
						var instance = this;

						(new A.EventHandle(instance._eventHandlers)).detach();
					},

					toggleDuplicationMode: function() {
						var instance = this;

						instance.set('duplicating', !instance.get('duplicating'));
					},

					_afterDuplicatingChange: function(event) {
						var instance = this;

						var searchContainer = instance.get('searchContainer');

						searchContainer.toggleClass('duplicating-form', event.newVal);
					},

					_onClickDuplicateForm: function() {
						var instance = this;

						instance.toggleDuplicationMode();
					},

					_onDestroyPortlet: function(event) {
						var instance = this;

						instance.destroy();
					}
				}
			}
		);

		Liferay.namespace('DDL').SearchContainer = SearchContainer;
	},
	'',
	{
		requires: ['liferay-portlet-base']
	}
);