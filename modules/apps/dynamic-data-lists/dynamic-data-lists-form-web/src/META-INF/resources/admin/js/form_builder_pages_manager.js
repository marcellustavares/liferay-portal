AUI.add(
	'liferay-ddl-form-builder-pages-manager',
	function(A) {
		var AArray = A.Array;
		var Renderer = Liferay.DDM.Renderer;

		var CSS_FORM_BUILDER_CONTENT = A.getClassName('form', 'builder', 'content');
		var CSS_FORM_BUILDER_PAGINATION = A.getClassName('form', 'builder', 'pagination');
		var CSS_FORM_BUILDER_PAGE_CONTROLS = A.getClassName('form', 'builder', 'page', 'controls');
		var CSS_FORM_BUILDER_PAGE_MANAGER_ADD_PAGE_LAST_POSITION = A.getClassName('form', 'builder', 'page', 'manager', 'add', 'last', 'position');
		var CSS_FORM_BUILDER_PAGE_MANAGER_DELETE_PAGE = A.getClassName('form', 'builder', 'page', 'manager', 'delete', 'page');
		var CSS_FORM_BUILDER_PAGE_MANAGER_SWITCH_MODE = A.getClassName('form', 'builder', 'page', 'manager', 'switch', 'mode');
		var CSS_FORM_BUILDER_PAGES_CONTENT = A.getClassName('form', 'builder', 'page', 'manager', 'content');
		var CSS_FORM_BUILDER_SWITCH_VIEW = A.getClassName('form', 'builder', 'controls', 'trigger');
		var CSS_FORM_BUILDER_TABVIEW = A.getClassName('form', 'builder', 'tabview');
		var CSS_PAGE_HEADER = A.getClassName('form', 'builder', 'page', 'header');

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
							'</div>'+
						'</div></div>',

					TPL_PAGE_CONTROL_TRIGGER: 
						'<a href="javascript:;" data-position="{position}" class="' + CSS_FORM_BUILDER_SWITCH_VIEW + '">' +
							'<span class="icon-ellipsis-vertical icon-monospaced"></span>' +
						'</a>',

					initializer: function() {

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

					_afterPagesQuantityChange: function(event) {
						var instance = this;

						FormBuilderPagesManager.superclass._afterPagesQuantityChange.apply(instance, arguments);

						instance._uiSetMode(instance.get('mode'));

						if (event.newVal > 1) {
							A.one('.' + CSS_PAGE_HEADER).one('.' + CSS_FORM_BUILDER_SWITCH_VIEW).hide();
						}
						else {
							A.one('.' + CSS_PAGE_HEADER).one('.' + CSS_FORM_BUILDER_SWITCH_VIEW).show();
						}
					},

					_createPopover: function() {
						var instance = this;

						var popover;

						popover = new A.Popover(
							{
								bodyContent: A.Lang.sub(instance.TPL_POPOVER_CONTENT, {
									addPageLastPosition: instance.get('strings').addPageLastPosition,
									addPageNextPosition: instance.get('strings').addPageNextPosition,
									deleteCurrentPage: instance.get('strings').deleteCurrentPage,
									switchMode: instance.get('strings').switchMode
								}),
								constrain: true,
								cssClass: 'form-builder-page-manager-popover-header',
								visible: false,
								zIndex: 50
							}
						).render();

						popover.get('boundingBox').one('.' + CSS_FORM_BUILDER_PAGE_MANAGER_ADD_PAGE_LAST_POSITION).on('click',
							A.bind(this._onAddLastPageClick, this)
						);

						popover.get('boundingBox').one('.' + CSS_FORM_BUILDER_PAGE_MANAGER_DELETE_PAGE).on('click',
							A.bind(this._onRemovePageClick, this)
						);

						popover.get('boundingBox').one('.' + CSS_FORM_BUILDER_PAGE_MANAGER_SWITCH_MODE).on('click',
							A.bind(this._onSwitchViewClick, this)
						);

						instance._createPopoverTriggers(popover);

						return popover;
					},

					_createPopoverTriggers: function(popover) {
						var instance = this;

						A.one('.' + CSS_FORM_BUILDER_TABVIEW).append(A.Lang.sub(instance.TPL_PAGE_CONTROL_TRIGGER, { position: 'top' }));

						A.one('.' + CSS_FORM_BUILDER_PAGE_CONTROLS).append(A.Lang.sub(instance.TPL_PAGE_CONTROL_TRIGGER, { position: 'left' }));

						A.one('.' + CSS_PAGE_HEADER).prepend(A.Lang.sub(instance.TPL_PAGE_CONTROL_TRIGGER, { position: 'left' }));

						A.one('.' + CSS_FORM_BUILDER_CONTENT).delegate('click', A.bind(instance._onPageControlOptionClick, instance), '.' + CSS_FORM_BUILDER_SWITCH_VIEW);

						A.all('.' + CSS_FORM_BUILDER_SWITCH_VIEW).on('clickoutside',  popover.hide, popover);
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

					_onAddLastPageClick: function() {
						var instance = this;

						instance._addPage();

						instance._addWizardPage();
						
						instance._getPopover().hide();
					},

					_onAddPageClick: function() {
						var instance = this;

						instance._addPage();

						instance._addWizardPage();
					},

					_onPageControlOptionClick: function(event) {
						var popover = this._getPopover();

						event.stopPropagation();

						popover.set('align', {
							node: event.currentTarget,
							points:[A.WidgetPositionAlign.RC, A.WidgetPositionAlign.TC]
						});

						popover.set('position', event.currentTarget.getData('position'));

						popover.toggle();
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

						this._getPopover().hide();

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

						instance._updateWizardTitle(activePageNumber - 1, title);
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

						if (instance.get('pagesQuantity') > 1) {
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
						}
						else {
							wizardView.get('boundingBox').hide();

							pagination.get('boundingBox').hide();
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

					_updateWizardTitle: function(index, title) {
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