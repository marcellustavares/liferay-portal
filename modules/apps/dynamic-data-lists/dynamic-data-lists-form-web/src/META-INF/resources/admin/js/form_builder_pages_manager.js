AUI.add(
	'liferay-ddl-form-builder-pages-manager',
	function(A) {
		var Renderer = Liferay.DDM.Renderer;
		var AArray = A.Array;
		var FieldTypes = Liferay.DDM.Renderer.FieldTypes;
		var FormBuilderPagesManagerUtil = Liferay.DDL.FormBuilderPagesManagerUtil;
		var Lang = A.Lang;

		var CSS_FORM_BUILDER_ADD_PAGE = A.getClassName('form', 'builder', 'page', 'manager', 'add', 'page');
		var CSS_FORM_BUILDER_REMOVE_PAGE = A.getClassName('form', 'builder', 'page', 'manager', 'remove', 'page');
		var CSS_PAGE_HEADER_TITLE = A.getClassName('form', 'builder', 'page', 'header', 'title');
		var CSS_FORM_BUILDER_TABVIEW = A.getClassName('form', 'builder', 'tabview');

		var FormBuilderPagesManager = A.Component.create(
			{
				ATTRS: {
					mode: {
						validator: function(value) {
							return (value === 'pagination' || value === 'wizard');
						},
						value: 'pagination'
					}
				},

				CSS_PREFIX: 'form-builder-page-manager',

				NAME: 'liferay-ddl-form-builder-pages-manager',

				EXTENDS: A.FormBuilderPageManager,

				prototype: {
					initializer: function() {
						var paginationContainer = this.get('paginationContainer');

						var pageHeader = this.get('pageHeader');
					},

					_addWizardPage: function() {
						var wizardView = this._getWizardView();

						wizardView._addItem({
							title: this._createUntitledPageLabel(this.get('activePageNumber'), this.get('pagesQuantity'))
						});

						wizardView.set('selected', this.get('activePageNumber') - 1);
					},

					_afterWizardViewSelectionChange: function() {
						var pagination = this._getPagination();

						var selectedWizard = this._getWizardView().get('selected');

						if (selectedWizard > -1) {
							pagination.set('page', selectedWizard + 1);

							this.set('activePageNumber', selectedWizard + 1);
						}
					},

					_createWizardItens: function() {
						var items = [];

						var pages = this.get('pagesQuantity');

						for (var i = 1; i <= pages; i ++) {
							var title = this.get('titles')[i - 1];

							var state = (this.get('activePageNumber') === i) ? 'active' : '';

							if (!title) {
								title = this._createUntitledPageLabel(i, pages);
							}

							items.push({
								title: title,
								state: state
							});
						}

						return items;
					},

					_getWizardView: function() {
						var instance = this;

						var wizardNode = A.one('.' + CSS_FORM_BUILDER_TABVIEW);

						if (!instance.wizard) {
							instance.wizard = new Renderer.Wizard({
									boundingBox: wizardNode,
									srcNode: wizardNode.one('> ul'),
									items: instance._createWizardItens(),
									allowNavigation: true
								}).render();

							instance.wizard.after('selectedChange', A.bind(instance._afterWizardViewSelectionChange, instance));
						}

						return instance.wizard;
					},

					_onAddPageClick: function() {
						this._addPage();

						this._addWizardPage();
					},

					_onTitleInputValueChange: function(event) {
						var activePageNumber = this.get('activePageNumber');

						var pagesQuantity = this.get('pagesQuantity');

						var title = event.newVal.trim();

						var titles = this.get('titles');

						titles[activePageNumber - 1] = title;

						if (!title) {
							title = this._createUntitledPageLabel(activePageNumber, pagesQuantity);
						}

						this.set('titles', titles);

						this._upWizardTitle(activePageNumber - 1, title);
					},

					_upWizardTitle: function(index, title) {
						var wizardView = this._getWizardView();

						var items = wizardView.get('items');

						items[index].title = title;

						wizardView.set('items', items);
					},

					_onRemovePageClick: function() {
						var activePageNumber = this.get('activePageNumber');

						var page = Math.max(1, activePageNumber - 1);

						var titles = this.get('titles');

						this._getPagination().prev();

						this.set('pagesQuantity', this.get('pagesQuantity') - 1);

						this.fire(
							'remove', {
								removedIndex: activePageNumber - 1
							}
						);

						this._pagination.getItem(page).addClass('active');

						titles.splice(activePageNumber - 1, 1);

						this.set('titles', titles);

						this.set('activePageNumber', page);

						this._removeWizardPage(activePageNumber - 1);

						if (!this.get('pagesQuantity')) {
							this._addPage();

							this._addWizardPage();

							this._getWizardView().activate(0);
						}
					},

					_onSwitchViewClick: function() {
						if (this.get('mode') === 'pagination') {
							this.set('mode', 'wizard');
						}
						else {
							this.set('mode', 'pagination');
						}
					},

					_renderTopPagination: function() {
						this._getWizardView();
					},

					_removeWizardPage: function(index) {
						var wizardView = this._getWizardView();

						var items = wizardView.get('items');

						wizardView._removeItem(index);;

						this._updateWizardContent();
					},

					_uiSetMode: function(type) {
						var activePageNumber = this.get('activePageNumber');

						var pagination = this._getPagination();

						var wizardView = this._getWizardView();

						if (type === 'wizard') {
							pagination.get('contentBox').hide();

							wizardView.get('contentBox').show();

							this._updateWizardContent();
						}
						else if (type === 'pagination') {
							pagination.get('contentBox').show();

							wizardView.get('contentBox').hide();

							pagination.set('page', activePageNumber);
						}
					},

					_updateWizardContent: function() {
						var instance = this;

						var wizardView = instance._getWizardView();

						var items = wizardView.get('items');

						wizardView.set('selected', instance.get('activePageNumber') - 1);

						AArray.each(items, function(item, index){
							var title = instance.get('titles')[index];

							if (!title) {
								title = instance._createUntitledPageLabel(index + 1, instance.get('pagesQuantity'));
							}

							item.title = title;
						});

						wizardView.set('items', items);
					}
				}
			}
		);

		Liferay.namespace('DDL').FormBuilderPagesManager = FormBuilderPagesManager;
	},
	'',
	{
		requires: ['aui-form-builder-page-manager', 'liferay-ddm-form-renderer-wizard']
	}
);