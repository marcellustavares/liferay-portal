AUI.add(
	'liferay-ddl-form-builder-pages-manager',
	function(A) {
		var AArray = A.Array;
		var Renderer = Liferay.DDM.Renderer;

		var CSS_FORM_BUILDER_TABVIEW = A.getClassName('form', 'builder', 'tabview');
		var CSS_FORM_BUILDER_PAGE_MANAGER_POPOVER = A.getClassName('form', 'builder', 'page', 'manager', 'popover');
		var CSS_FORM_BUILDER_PAGE_MANAGER_ADD_PAGE_LAST_POSITION = A.getClassName('form', 'builder', 'page', 'manager', 'add', 'last', 'position');
		var CSS_FORM_BUILDER_PAGE_MANAGER_DELETE_PAGE = A.getClassName('form', 'builder', 'page', 'manager', 'delete', 'page');
		var CSS_FORM_BUILDER_PAGE_MANAGER_SWITCH_MODE = A.getClassName('form', 'builder', 'page', 'manager', 'switch', 'mode');
		var CSS_FORM_BUILDER_PAGES_CONTENT = A.getClassName('form', 'builder', 'page', 'manager', 'content');
		var CSS_FORM_BUILDER_PAGINATION = A.getClassName('form', 'builder', 'pagination');
		var CSS_FORM_BUILDER_PAGE_CONTROLS = A.getClassName('form', 'builder', 'page', 'controls');
		var CSS_FORM_BUILDER_SWITCH_VIEW = A.getClassName('form', 'builder', 'switch', 'view');

		var FormBuilderPagesManager = A.Component.create(
			{
				ATTRS: {
					mode: {
						validator: '_validateMode',
						value: 'pagination'
					}
				},

				CSS_PREFIX: 'form-builder-page-manager',

				NAME: 'liferay-ddl-form-builder-pages-manager',

				EXTENDS: A.FormBuilderPageManager,

				prototype: {
					TPL_PAGES: '<div class="' + CSS_FORM_BUILDER_PAGES_CONTENT + '">' +
						'<div class="' + CSS_FORM_BUILDER_PAGINATION + '">' +
							'<div class="' + CSS_FORM_BUILDER_PAGE_CONTROLS + '">' +
								'<a href="javascript:;" class="' + CSS_FORM_BUILDER_SWITCH_VIEW + ' glyphicon glyphicon-th"></a>' +
							'</div>'+
						'</div></div>',

					TPL_HEADER_PAGE_CONTROL: 
						'<div class="liferay-ddl-form-builder-header-controls">' +
							'<div id="formBuilderHeaderControls" class="dropdown">' +
								'<a href="javascript:;" class="dropdown-toggle form-builder-header-controls-trigger">' +
									'<span class="icon-ellipsis-vertical icon-monospaced"></span>' +
								'</a>' +
							'</div>' +
						'</div>',
					
					initializer: function() {
						A.one('.' + CSS_FORM_BUILDER_TABVIEW).append(this.TPL_HEADER_PAGE_CONTROL);

						this._createHeaderPopover();
					},

					_createHeaderPopover: function() {
						var trigger = A.one('.form-builder-header-controls-trigger');

						var options = A.Lang.sub(this.TPL_POPOVER_CONTENT, {
							addPageLastPosition: this.get('strings').addPageLastPosition,
							addPageNextPosition: this.get('strings').addPageNextPosition,
							deleteCurrentPage: this.get('strings').deleteCurrentPage,
							switchMode: this.get('strings').switchMode
						});

						var popover = new A.Popover({
								align: {
									node: trigger,
									 points:[A.WidgetPositionAlign.RC, A.WidgetPositionAlign.LC]
								},
								bodyContent: options,
								constrain: true,
								cssClass: 'form-builder-page-manager-popover-header',
								visible: false,
								position: 'left',
								zIndex: 50
						  }).render();

						trigger.after('click', popover.toggle, popover);
						trigger.after('clickoutside', popover.hide, popover);

						popover.get('boundingBox').one('.' + CSS_FORM_BUILDER_PAGE_MANAGER_ADD_PAGE_LAST_POSITION).on('click',
							A.bind(this._onAddLastPageClick, this)
						);
						popover.get('boundingBox').one('.' + CSS_FORM_BUILDER_PAGE_MANAGER_DELETE_PAGE).on('click',
							A.bind(this._onRemovePageClick, this)
						);
						popover.get('boundingBox').one('.' + CSS_FORM_BUILDER_PAGE_MANAGER_SWITCH_MODE).on('click',
							A.bind(this._onSwitchViewClick, this)
						);
					},

					_addWizardPage: function() {
						var instance = this;

						var activePageNumber = instance.get('activePageNumber');

						var pagesQuantity = instance.get('pagesQuantity');

						var wizardView = instance._getWizardView();

						wizardView._addItem(
							{
								title: instance._createUntitledPageLabel(activePageNumber, pagesQuantity)
							}
						);

						wizardView.set('selected', activePageNumber - 1);
					},

					_afterWizardViewSelectionChange: function() {
						var instance = this;

						var pagination = instance._getPagination();

						var selectedWizard = instance._getWizardView().get('selected');

						if (selectedWizard > -1) {
							pagination.set('page', selectedWizard + 1);

							instance.set('activePageNumber', selectedWizard + 1);
						}
					},

					_createWizardItens: function() {
						var instance = this;

						var activePageNumber = instance.get('activePageNumber');

						var items = [];

						var pages = instance.get('pagesQuantity');

						var titles = instance.get('titles');

						for (var i = 1; i <= pages; i++) {
							var title = titles[i - 1];

							if (!title) {
								title = instance._createUntitledPageLabel(i, pages);
							}

							items.push(
								{
									state: (activePageNumber === i) ? 'active' : '',
									title: title
								}
							);
						}

						return items;
					},

					_getWizardView: function() {
						var instance = this;

						if (!instance.wizard) {
							var wizardNode = A.one('.' + CSS_FORM_BUILDER_TABVIEW);

							instance.wizard = new Renderer.Wizard(
								{
									allowNavigation: true,
									boundingBox: wizardNode,
									items: instance._createWizardItens(),
									srcNode: wizardNode.one('> ul')
								}
							).render();

							instance.wizard.after('selectedChange', A.bind(instance._afterWizardViewSelectionChange, instance));
						}

						return instance.wizard;
					},

					_onAddPageClick: function() {
						var instance = this;

						instance._addPage();

						instance._addWizardPage();
					},

					_onAddLastPageClick: function() {
						var instance = this;

						instance._addPage();

						instance._addWizardPage();
						
						instance._getPopover().hide();
					},

					_onRemovePageClick: function() {
						var instance = this;

						var activePageNumber = instance.get('activePageNumber');

						instance._getPagination().prev();

						instance.set('pagesQuantity', instance.get('pagesQuantity') - 1);

						instance.fire(
							'remove',
							{
								removedIndex: activePageNumber - 1
							}
						);

						var page = Math.max(1, activePageNumber - 1);

						instance._pagination.getItem(page).addClass('active');

						var titles = instance.get('titles');

						titles.splice(activePageNumber - 1, 1);

						instance.set('titles', titles);

						instance.set('activePageNumber', page);

						instance._removeWizardPage(activePageNumber - 1);

						if (!instance.get('pagesQuantity')) {
							instance._addPage();

							instance._addWizardPage();

							instance._getWizardView().activate(0);
						}
					},

					_onSwitchViewClick: function() {
						var instance = this;

						if (instance.get('mode') === 'pagination') {
							instance.set('mode', 'wizard');
						}
						else {
							instance.set('mode', 'pagination');
						}
					},

					_onTitleInputValueChange: function(event) {
						var instance = this;

						var activePageNumber = instance.get('activePageNumber');

						var pagesQuantity = instance.get('pagesQuantity');

						var title = event.newVal.trim();

						var titles = instance.get('titles');

						titles[activePageNumber - 1] = title;

						if (!title) {
							title = instance._createUntitledPageLabel(activePageNumber, pagesQuantity);
						}

						instance.set('titles', titles);

						instance._upWizardTitle(activePageNumber - 1, title);
					},

					_removeWizardPage: function(index) {
						var instance = this;

						var wizardView = instance._getWizardView();

						wizardView._removeItem(index);

						instance._updateWizardContent();
					},

					_renderTopPagination: function() {
						var instance = this;

						instance._getWizardView();
					},

					_uiSetMode: function(type) {
						var instance = this;

						var activePageNumber = instance.get('activePageNumber');

						var pagination = instance._getPagination();

						var wizardView = instance._getWizardView();

						if (type === 'wizard') {
							pagination.get('boundingBox').hide();

							wizardView.get('boundingBox').show();

							instance._updateWizardContent();
						}
						else if (type === 'pagination') {
							pagination.get('boundingBox').show();

							wizardView.get('boundingBox').hide();

							pagination.set('page', activePageNumber);
						}
					},

					_updateWizardContent: function() {
						var instance = this;

						var wizardView = instance._getWizardView();

						var items = wizardView.get('items');

						wizardView.set('selected', instance.get('activePageNumber') - 1);

						AArray.each(
							items,
							function(item, index) {
								var title = instance.get('titles')[index];

								if (!title) {
									title = instance._createUntitledPageLabel(index + 1, instance.get('pagesQuantity'));
								}

								item.title = title;
							}
						);

						wizardView.set('items', items);
					},

					_upWizardTitle: function(index, title) {
						var instance = this;

						var wizardView = instance._getWizardView();

						var items = wizardView.get('items');

						items[index].title = title;

						wizardView.set('items', items);
					},

					_validateMode: function(mode) {
						return (mode === 'pagination' || mode === 'wizard');
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