AUI.add(
	'liferay-portlet-workflow-designer',
	function(A) {
		var AArray = A.Array;
		var AObject = A.Object;
		var DiagramBuilder = A.DiagramBuilder;
		var getClassName = A.getClassName;
		var Lang = A.Lang;
		var LString = Lang.String;
		var Template = A.Template;
		var WidgetStdMod = A.WidgetStdMod;

		var DiagramBuilderTypes = DiagramBuilder.types;

		var emptyFn = Lang.emptyFn;
		var emptyFnFalse = Lang.emptyFnFalse;
		var isArray = Array.isArray;
		var isNull = Lang.isNull;
		var isNumber = Lang.isNumber;
		var isObject = Lang.isObject;
		var isString = Lang.isString;
		var isValue = Lang.isValue;

		var BUFFER_ATTR = [null, '="', null, '" '];

		var BUFFER_CLOSE_NODE = ['</', null, '>'];

		var BUFFER_OPEN_NODE = ['<', null, null, '>'];

		var COL_TYPES_ASSIGNMENT = ['address', 'resourceActions', 'roleId', 'roleType', 'scriptedAssignment', 'taskAssignees', 'user', 'userId'];

		var COL_TYPES_FIELD = ['condition', 'fork', 'join', 'join-xor', 'state', 'task'];

		var CSS_CELLEDITOR_ASSIGNMENT_VIEW = getClassName('celleditor', 'assignment', 'view');

		var CSS_CELLEDITOR_VIEW_TYPE = getClassName('celleditor', 'view', 'type');

		var DEFAULT_LANGUAGE = 'groovy';

		var SCHEMA_FIELDS_PATH = ['results', '0', 'name'];

		var SCHEMA_MAP_RESULTS = {
			resultFields: [
				{
					key: 'name',
					locator: 'Name[@language-id="' + themeDisplay.getLanguageId() + '"]'
				}
			],
			resultListLocator: 'root'
		};

		var STR_BLANK = '';

		var STR_CDATA_CLOSE = ']]>';

		var STR_CDATA_OPEN = '<![CDATA[';

		var STR_CHAR_CRLF = '\r\n';

		var STR_CHAR_TAB = '\t';

		var STR_DASH = '-';

		var STR_DOT = '.';

		var STR_ELLIPSIS = '&hellip;';

		var STR_SPACE = ' ';

		var SELECTOR_PREFIX_CELLEDITOR_VIEW_TYPE = STR_DOT + CSS_CELLEDITOR_VIEW_TYPE + STR_DASH;

		var cdata = function(value) {
			value = value.replace(STR_CDATA_OPEN, '').replace(STR_CDATA_CLOSE, '');

			return STR_CDATA_OPEN + value + STR_CDATA_CLOSE;
		};

		var jsonParse = function(val) {
			var jsonObj = null;

			try {
				jsonObj = JSON.parse(val);
			}
			catch (e) {
			}

			return jsonObj;
		};

		var jsonStringify = function(val) {
			var jsonString = null;

			try {
				jsonString = JSON.stringify(val);
			}
			catch (e) {
			}

			return jsonString;
		};

		var serializeForm = function(form) {
			var data = {};

			if (form) {
				form.all(':input:not(:button)').each(
					function(item, index, collection) {
						var checked = item.get('checked');
						var name = item.get('name');
						var type = item.get('type');

						var value = item.val();

						if (name) {
							if (!isArray(data[name])) {
								data[name] = [];
							}

							if ((type === 'checkbox' || type === 'radio') && !checked) {
								value = null;
							}

							data[name].push(value);
						}
					}
				);
			}

			return data;
		};

		var uniformRandomInt = function(a, b) {
			return parseInt(a + Math.random() * (b - a), 10) || 0;
		};

		var KaleoDesignerStrings = {
			actions: Liferay.Language.get('actions'),
			actionsType: Liferay.Language.get('actions-type'),
			addAnchorMessage: Liferay.Language.get('add-anchor'),
			addSection: Liferay.Language.get('add-section'),
			approve: Liferay.Language.get('approve'),
			assetCreator: Liferay.Language.get('asset-creator'),
			assignments: Liferay.Language.get('assignments'),
			assignmentType: Liferay.Language.get('assignment-type'),
			autoCreate: Liferay.Language.get('auto-create'),
			beanshell: Liferay.Language.get('beanshell'),
			cancel: Liferay.Language.get('cancel'),
			closeMessage: Liferay.Language.get('close'),
			'default': Liferay.Language.get('default'),
			definition: Liferay.Language.get('definition'),
			deleteNodesMessage: Liferay.Language.get('are-you-sure-you-want-to-delete-the-selected-nodes'),
			description: Liferay.Language.get('description'),
			drl: Liferay.Language.get('drl'),
			edit: Liferay.Language.get('edit'),
			editMessage: Liferay.Language.get('edit'),
			editRecipients: Liferay.Language.get('edit-recipients'),
			email: Liferay.Language.get('email'),
			emailAddress: Liferay.Language.get('email-address'),
			executionType: Liferay.Language.get('execution-type'),
			forms: Liferay.Language.get('forms'),
			formTemplate: Liferay.Language.get('form-template'),
			freemarker: Liferay.Language.get('freemarker'),
			groovy: Liferay.Language.get('groovy'),
			im: Liferay.Language.get('instant-messenger'),
			initial: Liferay.Language.get('Initial'),
			inspectTaskMessage: Liferay.Language.get('inspect-the-task-nodes-to-assign-a-form-template'),
			javascript: Liferay.Language.get('javascript'),
			language: Liferay.Language.get('language'),
			name: Liferay.Language.get('name'),
			notificationRecipients: Liferay.Language.get('notification-recipients'),
			notifications: Liferay.Language.get('notifications'),
			notificationType: Liferay.Language.get('notification-type'),
			onAssignment: Liferay.Language.get('on-assignment'),
			onEntry: Liferay.Language.get('on-entry'),
			onExit: Liferay.Language.get('on-exit'),
			organization: Liferay.Language.get('organization'),
			priority: Liferay.Language.get('priority'),
			privateMessage: Liferay.Language.get('private-message'),
			python: Liferay.Language.get('python'),
			recipientType: Liferay.Language.get('recipient-type'),
			regular: Liferay.Language.get('regular'),
			remove: Liferay.Language.get('remove'),
			resourceActions: Liferay.Language.get('resource-actions'),
			role: Liferay.Language.get('role'),
			roleId: Liferay.Language.get('role-id'),
			roleName: Liferay.Language.get('role-name'),
			roleType: Liferay.Language.get('role-type'),
			ruby: Liferay.Language.get('ruby'),
			save: Liferay.Language.get('save'),
			screenName: Liferay.Language.get('screen-name'),
			script: Liferay.Language.get('script'),
			scriptedAssignment: Liferay.Language.get('scripted-assignment'),
			scriptLanguage: Liferay.Language.get('script-language'),
			search: Liferay.Language.get('search'),
			site: Liferay.Language.get('site'),
			taskAssignees: Liferay.Language.get('task-assignees'),
			taskTimers: Liferay.Language.get('task-timers'),
			template: Liferay.Language.get('template'),
			templateLanguage: Liferay.Language.get('template-language'),
			text: Liferay.Language.get('text'),
			type: Liferay.Language.get('type'),
			user: Liferay.Language.get('user'),
			userId: Liferay.Language.get('user-id'),
			userNotification: Liferay.Language.get('user-notification'),
			velocity: Liferay.Language.get('velocity')

		};

		Liferay.KaleoDesignerStrings = KaleoDesignerStrings;

		var AutoCompleteUtil = {
			create: function(portletNamespace, inputNode, url, requestTemplate, resultTextLocator, selectFn) {
				var instance = this;

				if (!inputNode.ac) {
					inputNode.plug(
						A.Plugin.AutoComplete,
						{
							activateFirstItem: true,
							maxResults: 20,
							on: {
								select: selectFn
							},
							requestTemplate: requestTemplate || '&' + portletNamespace + 'keywords={query}',
							resultHighlighter: 'wordMatch',
							resultTextLocator: resultTextLocator || 'name',
							source: url
						}
					);

					instance._INSTANCES.push(inputNode.ac);
				}

				return inputNode.ac;
			},

			destroyAll: function() {
				var instance = this;

				var INSTANCES = instance._INSTANCES;

				AArray.invoke(INSTANCES, 'destroy');

				INSTANCES.length = 0;
			},

			_INSTANCES: []
		};

		var PropertyListFormatter = {
			assignmentsType: function(data) {
				var value = data.value;

				var assignmentType;

				if (value && value.assignmentType) {
					assignmentType = value.assignmentType[0];
				}

				return KaleoDesignerStrings[assignmentType || 'assetCreator'];
			},

			forms: function(data) {
				var value = data.value;

				var templateName;

				if (value) {
					templateName = value.templateName;
				}

				return AArray(templateName).join(', ');
			},

			names: function(data) {
				var value = data.value;

				var names;

				if (value) {
					names = value.name;
				}

				return AArray(names).join(', ');
			},

			script: function(data) {
				return STR_ELLIPSIS;
			}
		};

		var XMLUtil = {
			REGEX_TOKEN_1: /(>)(<)(\/*)/g,
			REGEX_TOKEN_2: /.+<\/\w[^>]*>$/,
			REGEX_TOKEN_3: /^<\/\w/,
			REGEX_TOKEN_4: /^<\w[^>]*[^\/]>.*$/,

			create: function(name, content, attrs) {
				var instance = this;

				var node = instance.createObj(name, attrs);

				return node.open + (isValue(content) ? content : STR_BLANK) + node.close;
			},

			createObj: function(name, attrs) {
				var instance = this;

				var attrBuffer = [STR_SPACE];
				var normalizedName = LString.uncamelize(name, STR_DASH).toLowerCase();

				A.each(
					attrs,
					function(item, index, collection) {
						if (item !== undefined) {
							BUFFER_ATTR[0] = index;
							BUFFER_ATTR[2] = item;

							attrBuffer.push(BUFFER_ATTR.join(STR_BLANK));
						}
					}
				);

				var attributes = Lang.trimRight(attrBuffer.join(STR_BLANK));

				BUFFER_CLOSE_NODE[1] = normalizedName;

				BUFFER_OPEN_NODE[1] = normalizedName;
				BUFFER_OPEN_NODE[2] = attributes;

				return {
					close: BUFFER_CLOSE_NODE.join(STR_BLANK),
					open: BUFFER_OPEN_NODE.join(STR_BLANK)
				};
			},

			format: function(xml) {
				var instance = this;

				var i;

				var formatted = STR_BLANK;
				var inCDATA = false;
				var pad = 0;

				var lines = xml.replace(instance.REGEX_TOKEN_1, '$1' + STR_CHAR_CRLF + '$2$3').split(/\r?\n/g);

				lines.forEach(
					function(item, index, collection) {
						var indent = 0;

						if (!inCDATA) {
							if (item.match(instance.REGEX_TOKEN_2)) {
								indent = 0;
							}
							else if (item.match(instance.REGEX_TOKEN_3)) {
								if (pad !== 0) {
									pad -= 1;
								}
							}
							else if (item.match(instance.REGEX_TOKEN_4)) {
								indent = 1;
							}

							formatted += LString.repeat(STR_CHAR_TAB, pad);
						}

						formatted += item + STR_CHAR_CRLF;

						if (item.indexOf(STR_CDATA_OPEN) > -1) {
							inCDATA = true;
						}
						else if (item.indexOf(STR_CDATA_CLOSE) > -1) {
							inCDATA = false;
						}

						pad += indent;
					}
				);

				return formatted;
			}
		};

		var KaleoDesigner = A.Component.create(
			{
				ATTRS: {
					aceEditorConfig: {
						setter: '_setAceEditor',
						validator: isObject,
						value: null
					},

					availableFields: {
						validator: isObject,
						valueFn: function() {
							return KaleoDesigner.AVAILABLE_FIELDS.DEFAULT;
						}
					},

					availablePropertyModels: {
						validator: isObject,
						valueFn: function() {
							return KaleoDesigner.AVAILABLE_PROPERTY_MODELS.DEFAULT;
						}
					},

					contentTabView: {
						setter: '_setContentTabView',
						validator: isObject,
						value: null,
						writeOnce: true
					},

					data: {
						validator: isObject,
						value: {}
					},

					definition: {
						lazyAdd: false,
						setter: '_setDefinition'
					},

					definitionDescription: {
						validator: isString,
						value: STR_BLANK
					},

					definitionName: {
						validator: isString,
						valueFn: function() {
							return KaleoDesignerStrings.definition + uniformRandomInt(0, 100);
						}
					},

					definitionVersion: {
						validator: isNumber,
						value: 1
					},

					portletNamespace: {
						value: STR_BLANK
					},

					portletResourceNamespace: {
						value: STR_BLANK
					},

					xmlNamespace: {
						value: {
							'xmlns': 'urn:liferay.com:liferay-workflow_6.2.0',
							'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
							'xsi:schemaLocation': 'urn:liferay.com:liferay-workflow_6.2.0 http://www.liferay.com/dtd/liferay-workflow-definition_6_2_0.xsd'
						}
					}
				},

				EXTENDS: DiagramBuilder,

				NAME: 'diagram-builder',

				UI_ATTRS: ['definition'],

				prototype: {
					initializer: function() {
						var instance = this;

						var metadata = instance.getDefinitionMetadata();

						if (metadata) {
							instance.setAttrs(metadata);
						}

						instance.after('render', instance._afterRenderKaleoDesigner);

						instance.after(instance._renderContentTabs, instance, '_renderTabs');
					},

					connectDefinitionFields: function() {
						var instance = this;

						var connectors = [];

						instance.forEachDefinitionField(
							function(tagName, fieldData) {
								fieldData.results.forEach(
									function(item1, index1, collection1) {
										item1.transitions.forEach(
											function(item2, index2, collection2) {
												connectors.push(
													{
														connector: {
															'default': item2['default'],
															name: item2.name
														},
														source: item1.name,
														target: item2.target
													}
												);
											}
										);
									}
								);
							}
						);

						instance.connectAll(connectors);
					},

					editNode: function(diagramNode) {
						var instance = this;

						if (diagramNode.getProperties()) {
							KaleoDesigner.superclass.editNode.apply(this, arguments);
						}
						else {
							instance.closeEditProperties();
						}
					},

					forEachDefinitionField: function(fn) {
						var instance = this;

						COL_TYPES_FIELD.forEach(
							function(item, index, collection) {
								var fieldData = instance._translateXMLToJSON(item);

								if (fn && !fieldData.error) {
									fn.call(instance, item, fieldData);
								}
							}
						);
					},

					getDefinitionMetadata: function() {
						var instance = this;

						var output = A.DataSchema.XML.apply(
							{
								metaFields: {
									definitionDescription: '//workflow-definition/description',
									definitionName: '//workflow-definition/name',
									definitionVersion: '//workflow-definition/version'
								}
							},
							instance.definitionDoc
						);

						return output.meta;
					},

					getLocalizedName: function(name) {
						var doc = A.DataType.XML.parse(name);

						var schema = A.DataSchema.XML.apply(SCHEMA_MAP_RESULTS, doc);

						return A.Object.getValue(schema, SCHEMA_FIELDS_PATH);
					},

					sanitizeDefinitionXML: function(val) {
						var instance = this;

						val = decodeURIComponent(val);

						val = val.replace(/\s*(<!\[CDATA\[)/g, '$1');
						val = val.replace(/(\]\]>)\s*/g, '$1');

						return val.replace(/(<workflow-definition)[^>]*(>)/, '$1$2');
					},

					showEditor: function() {
						var instance = this;

						var editor = instance.editor;

						if (!editor) {
							editor = new A.AceEditor(instance.get('aceEditorConfig')).render();

							instance.editor = editor;
						}

						var content = instance.get('definition');

						if (!content || instance.validateDefinition(content)) {
							content = instance.toFormattedXML();
						}

						editor.set('value', content);
					},

					toFormattedXML: function() {
						var instance = this;

						return XMLUtil.format(instance.toXML());
					},

					toXML: function() {
						var instance = this;

						var json = instance.toJSON();

						var definitionDescription = instance.get('definitionDescription');
						var definitionName = instance.get('definitionName');
						var definitionVersion = instance.get('definitionVersion');
						var xmlNamespace = instance.get('xmlNamespace');

						var buffer = [];

						var xmlWorkflowDefinition = XMLUtil.createObj('workflow-definition', xmlNamespace);

						buffer.push('<?xml version="1.0"?>', xmlWorkflowDefinition.open);

						buffer.push(
							XMLUtil.create('name', definitionName),
							XMLUtil.create('description', definitionDescription),
							XMLUtil.create('version', definitionVersion)
						);

						json.nodes.forEach(
							function(item, index, collection) {
								var description = item.description;
								var initial = item.initial;
								var metadata = item.metadata;
								var name = item.name;
								var script = item.script;
								var scriptLanguage = item.scriptLanguage;

								var xmlNode = XMLUtil.createObj(item.xmlType);

								buffer.push(xmlNode.open, XMLUtil.create('name', name));

								if (description) {
									buffer.push(XMLUtil.create('description', cdata(jsonStringify(description))));
								}

								if (metadata) {
									buffer.push(XMLUtil.create('metadata', cdata(jsonStringify(metadata))));
								}

								instance._appendXMLActions(buffer, item);

								if (initial) {
									buffer.push(XMLUtil.create('initial', initial));
								}

								if (script) {
									buffer.push(XMLUtil.create('script', cdata(script)));
								}

								if (scriptLanguage) {
									buffer.push(XMLUtil.create('scriptLanguage', scriptLanguage));
								}

								instance._appendXMLAssignments(buffer, item);
								instance._appendXMLTransitions(buffer, item);

								buffer.push(xmlNode.close);
							}
						);

						buffer.push(xmlWorkflowDefinition.close);

						return buffer.join(STR_BLANK);
					},

					validateDefinition: function(definition) {
						var instance = this;

						var doc = A.DataType.XML.parse(definition);

						var valid = true;

						if (isNull(doc) || isNull(doc.documentElement) || A.one(doc).one('parsererror')) {
							valid = false;
						}

						return valid;
					},

					_afterRenderKaleoDesigner: function() {
						var instance = this;

						instance.connectDefinitionFields();

						instance.canvasRegion = instance.canvas.get('region');
					},

					_afterSelectionChangeKaleoDesigner: function(event) {
						var instance = this;
						var tabContentNode = event.newVal.get('boundingBox');

						if (instance.get('rendered')) {
							instance.stopEditing();

							if (tabContentNode === instance.sourceNode) {
								instance.showEditor();
							}
						}
					},

					_appendXMLActions: function(buffer, data) {
						var instance = this;

						var actions = data.actions;
						var notifications = data.notifications;

						var hasAction = isObject(actions) && !AObject.isEmpty(actions);
						var hasNotification = isObject(notifications) && !AObject.isEmpty(notifications);
						var xmlActions = XMLUtil.createObj('actions');

						if (hasAction || hasNotification) {
							buffer.push(xmlActions.open);
						}

						if (hasAction) {
							var description = actions.description;
							var executionType = actions.executionType;
							var language = actions.scriptLanguage;
							var script = actions.script;

							var xmlAction = XMLUtil.createObj('action');

							actions.name.forEach(
								function(item, index, collection) {
									buffer.push(xmlAction.open, XMLUtil.create('name', item));

									if (description) {
										buffer.push(XMLUtil.create('description', description[index]));
									}

									if (script) {
										buffer.push(XMLUtil.create('script', cdata(script[index])));
									}

									if (language) {
										buffer.push(XMLUtil.create('scriptLanguage', language[index]));
									}

									if (executionType) {
										buffer.push(XMLUtil.create('executionType', executionType[index]));
									}

									buffer.push(xmlAction.close);
								}
							);
						}

						if (hasNotification) {
							instance._appendXMLNotifications(buffer, data);
						}

						if (hasAction || hasNotification) {
							buffer.push(xmlActions.close);
						}
					},

					_appendXMLAssignments: function(buffer, data, lookupKey, wrapperNodeName) {
						var instance = this;

						var dataAssignments = data[lookupKey || 'assignments'];

						if (dataAssignments) {
							var assignmentType = AArray(dataAssignments.assignmentType)[0];

							var xmlAssignments = XMLUtil.createObj(wrapperNodeName || 'assignments');

							buffer.push(xmlAssignments.open);

							if (dataAssignments.address) {
								dataAssignments.address.forEach(
									function(item, index, collection) {
										if (isValue(item)) {
											buffer.push(XMLUtil.create('address', item));
										}
									}
								);
							}

							var xmlRoles = XMLUtil.createObj('roles');

							if (assignmentType === 'resourceActions') {
								var xmlResourceAction = XMLUtil.create('resourceAction', dataAssignments.resourceActions);

								buffer.push(XMLUtil.create('resourceActions', xmlResourceAction));
							}
							else if (assignmentType === 'roleId') {
								var xmlRoleId = XMLUtil.create('roleId', dataAssignments.roleId);

								buffer.push(xmlRoles.open, XMLUtil.create('role', xmlRoleId), xmlRoles.close);
							}
							else if (assignmentType === 'roleType') {
								buffer.push(xmlRoles.open);

								var xmlRole = XMLUtil.createObj('role');

								dataAssignments.roleType.forEach(
									function(item, index, collection) {
										var roleName = dataAssignments.roleName[index];

										if (roleName) {
											buffer.push(
												xmlRole.open,
												XMLUtil.create('roleType', item),
												XMLUtil.create('name', roleName),
												XMLUtil.create('autoCreate', dataAssignments.autoCreate[index] || String(false)),
												xmlRole.close
											);
										}
									}
								);

								buffer.push(xmlRoles.close);
							}
							else if (assignmentType === 'scriptedAssignment') {
								var xmlScriptedAssignment = XMLUtil.createObj('scriptedAssignment');

								dataAssignments.script.forEach(
									function(item, index, collection) {
										buffer.push(
											xmlScriptedAssignment.open,
											XMLUtil.create('script', cdata(item)),
											XMLUtil.create('scriptLanguage', dataAssignments.scriptLanguage[index]),
											xmlScriptedAssignment.close
										);
									}
								);
							}
							else if (assignmentType === 'user') {
								var xmlUser = XMLUtil.createObj('user');

								dataAssignments.userId.forEach(
									function(item, index, collection) {
										var userContent = null;

										if (isValue(item)) {
											userContent = XMLUtil.create('userId', item);
										}
										else if (isValue(dataAssignments.emailAddress[index])) {
											userContent = XMLUtil.create('emailAddress', dataAssignments.emailAddress[index]);
										}
										else if (isValue(dataAssignments.screenName[index])) {
											userContent = XMLUtil.create('screenName', dataAssignments.screenName[index]);
										}

										if (userContent) {
											buffer.push(
												xmlUser.open,
												userContent,
												xmlUser.close
											);
										}
									}
								);
							}
							else if (assignmentType === 'taskAssignees') {
								buffer.push('<assignees/>');
							}
							else if (!dataAssignments.address || dataAssignments.address.filter(isValue).length === 0) {
								buffer.push('<user/>');
							}

							buffer.push(xmlAssignments.close);
						}
					},

					_appendXMLNotifications: function(buffer, data) {
						var instance = this;

						var notifications = data.notifications;

						if (notifications) {
							var description = notifications.description;
							var executionType = notifications.executionType;
							var notificationType = notifications.notificationType;
							var recipients = notifications.recipients;
							var template = notifications.template;
							var templateLanguage = notifications.templateLanguage;

							var xmlNotification = XMLUtil.createObj('notification');

							notifications.name.forEach(
								function(item, index, collection) {
									buffer.push(xmlNotification.open, XMLUtil.create('name', item));

									if (description) {
										buffer.push(XMLUtil.create('description', description[index]));
									}

									if (template) {
										buffer.push(XMLUtil.create('template', cdata(template[index])));
									}

									if (templateLanguage) {
										buffer.push(XMLUtil.create('templateLanguage', templateLanguage[index]));
									}

									if (notificationType) {
										buffer.push(XMLUtil.create('notificationType', notificationType[index]));
									}

									instance._appendXMLAssignments(
										buffer,
										{
											recipients: recipients[index]
										},
										'recipients',
										'recipients'
									);

									if (executionType) {
										buffer.push(XMLUtil.create('executionType', executionType[index]));
									}

									buffer.push(xmlNotification.close);
								}
							);
						}
					},

					_appendXMLTransitions: function(buffer, data) {
						var instance = this;

						if (data.transitions.length > 0) {
							var xmlTransition = XMLUtil.createObj('transition');
							var xmlTransitions = XMLUtil.createObj('transitions');

							buffer.push(xmlTransitions.open);

							var pickDefault = data.transitions.some(
								function(item, index, collection) {
									return item.connector['default'] === true;
								}
							);

							pickDefault = !pickDefault;

							data.transitions.forEach(
								function(item, index, collection) {
									var defaultValue = item.connector['default'];

									if (pickDefault && index === 0) {
										defaultValue = true;
									}

									buffer.push(
										xmlTransition.open,
										XMLUtil.create('name', item.connector.name),
										XMLUtil.create('target', item.target),
										XMLUtil.create('default', defaultValue),
										xmlTransition.close
									);
								}
							);

							buffer.push(xmlTransitions.close);
						}
					},

					_getRandomXY: function() {
						var instance = this;

						var region = instance.canvas.get('region');

						return [
							uniformRandomInt(0, region.width - 100),
							uniformRandomInt(0, region.height - 100)
						];
					},

					_getSchemaActions: function(key, tagName) {
						var instance = this;

						return {
							key: key || 'actions',
							schema: {
								resultFields: [
									{
										key: 'description',
										locator: 'description'
									},
									{
										key: 'executionType',
										locator: 'execution-type'
									},
									{
										key: 'name',
										locator: 'name'
									},
									{
										key: 'priority',
										locator: 'priority'
									},
									{
										key: 'script',
										locator: 'script'
									},
									{
										key: 'scriptLanguage',
										locator: 'script-language'
									}
								],
								resultListLocator: tagName || 'action'
							}
						};
					},

					_getSchemaAssignments: function(key, tagName) {
						var instance = this;

						return {
							key: key || 'assignments',
							schema: {
								resultFields: [
									{
										key: 'address',
										locator: 'address'
									},
									{
										key: 'resourceActions',
										schema: {
											resultFields: [
												{
													key: 'resourceAction',
													locator: 'resource-action'
												}
											],
											resultListLocator: 'resource-actions'
										}
									},
									{
										key: 'roleId',
										schema: {
											resultFields: [
												{
													key: 'roleId',
													locator: 'role-id'
												}
											],
											resultListLocator: 'role'
										}
									},
									{
										key: 'roleType',
										schema: {
											resultFields: [
												{
													key: 'autoCreate',
													locator: 'auto-create'
												},
												{
													key: 'roleName',
													locator: 'name'
												},
												{
													key: 'roleType',
													locator: 'role-type'
												}
											],
											resultListLocator: 'role'
										}
									},
									{
										key: 'scriptedAssignment',
										schema: {
											resultFields: [
												{
													key: 'script',
													locator: 'script'
												},
												{
													key: 'scriptLanguage',
													locator: 'script-language'
												}
											],
											resultListLocator: 'scripted-assignment'
										}
									},
									{
										key: 'taskAssignees',
										locator: 'assignees'
									},
									{
										key: 'user',
										schema: {
											resultFields: [
												{
													key: 'emailAddress',
													locator: 'email-address'
												},
												{
													key: 'screenName',
													locator: 'screen-name'
												},
												{
													key: 'userId',
													locator: 'user-id'
												}
											],
											resultListLocator: 'user'
										}
									}
								],
								resultListLocator: tagName || 'assignments'
							}
						};
					},

					_getSchemaNotifications: function(key, tagName, assignmentKey, assignmentTagName) {
						var instance = this;

						assignmentKey = assignmentKey || 'recipients';
						assignmentTagName = assignmentTagName || 'recipients';

						return {
							key: key || 'notifications',
							schema: {
								resultFields: [
									{
										key: 'description',
										locator: 'description'
									},
									{
										key: 'executionType',
										locator: 'execution-type'
									},
									{
										key: 'name',
										locator: 'name'
									},
									{
										key: 'notificationType',
										locator: 'notification-type'
									},
									{
										key: 'template',
										locator: 'template'
									},
									{
										key: 'templateLanguage',
										locator: 'template-language'
									},
									instance._getSchemaAssignments(assignmentKey, assignmentTagName)
								],
								resultListLocator: tagName || 'notification'
							}
						};
					},

					_getSchemaTaskTimers: function(key, tagNode) {
						var instance = this;

						return {
							key: key || 'taskTimers',
							schema: {
								resultFields: [
									{
										key: 'blocking',
										locator: 'blocking'
									},
									{
										key: 'duration',
										locator: 'delay/duration'
									},
									{
										key: 'name',
										locator: 'name'
									},
									{
										key: 'scale',
										locator: 'delay/scale'
									},
									instance._getSchemaActions('timerActions', 'timer-action'),
									instance._getSchemaNotifications('timerNotifications', 'timer-notification', 'reassignments', 'reassignments')
								],
								resultListLocator: tagNode || 'task-timer'
							}
						};
					},

					_getSchemaTransitions: function(key, tagName) {
						var instance = this;

						return {
							key: key || 'transitions',
							schema: {
								resultFields: [
									{
										key: 'default',
										locator: 'default'
									},
									{
										key: 'name',
										locator: 'name'
									},
									{
										key: 'target',
										locator: 'target'
									}
								],
								resultListLocator: tagName || 'transition'
							}
						};
					},

					_normalizeToActions: function(data) {
						var instance = this;

						var actions = {};

						data.actions.forEach(
							function(item1, index1, collection1) {
								A.each(
									item1,
									function(item2, index2, collection2) {
										if (isValue(item2)) {
											if (index2 === 'script') {
												item2 = Lang.trim(item2);
											}

											instance._put(actions, index2, item2);
										}
									}
								);
							}
						);

						return actions;
					},

					_normalizeToAssignments: function(data) {
						var instance = this;

						var assignments = {
							assignmentType: [STR_BLANK]
						};

						if (data && data.length) {
							COL_TYPES_ASSIGNMENT.forEach(
								function(item1, index1, collection1) {
									var assignmentValue = AArray(data[0][item1]);

									assignmentValue.forEach(
										function(item2, index2, collection2) {
											if (isObject(item2)) {
												A.each(
													item2,
													function(item3, index3, collection3) {
														instance._put(assignments, index3, item3);

														if (isValue(item3)) {
															assignments.assignmentType = AArray(item1);
														}
													}
												);
											}
											else {
												if (isValue(item2)) {
													assignments.assignmentType = AArray(item1);
												}

												instance._put(assignments, item1, item2);
											}
										}
									);
								}
							);
						}

						return assignments;
					},

					_normalizeToNotifications: function(data) {
						var instance = this;

						var notifications = {};

						data.notifications.forEach(
							function(item1, index1, collection1) {
								A.each(
									item1,
									function(item2, index2, collection2) {
										if (isValue(item2)) {
											if (index2 === 'recipients') {
												item2 = instance._normalizeToAssignments(item2);
											}

											instance._put(notifications, index2, item2);
										}
									}
								);
							}
						);

						return notifications;
					},

					_normalizeToTaskTimers: function(data) {
						var instance = this;

						return {};
					},

					_put: function(obj, key, value) {
						var instance = this;

						obj[key] = obj[key] || [];

						obj[key].push(value);
					},

					_renderContentTabs: function() {
						var instance = this;

						if (!instance.contentTabView) {
							var contentTabView = new A.TabView(instance.get('contentTabView'));

							contentTabView.render();

							instance.viewNode = contentTabView.item(0).get('boundingBox');
							instance.sourceNode = contentTabView.item(1).get('boundingBox');

							instance.contentTabView = contentTabView;
						}
					},

					_setAceEditor: function(val) {
						var instance = this;

						var portletNamespace = instance.get('portletNamespace');

						var canvasRegion = instance.canvasRegion;

						return A.merge(
							{
								boundingBox: '#' + portletNamespace + 'editorWrapper',
								height: canvasRegion.height,
								mode: 'xml',
								width: canvasRegion.width
							},
							val
						);
					},

					_setContentTabView: function(val) {
						var instance = this;

						var boundingBox = instance.get('boundingBox');

						var contentTabListNode = boundingBox.one('.tabbable .nav-tabs');

						var defaultValue = {
							after: {
								selectionChange: A.bind(instance._afterSelectionChangeKaleoDesigner, instance)
							},
							boundingBox: boundingBox.one('.tabbable'),
							bubbleTargets: instance,
							contentBox: boundingBox.one('.tabbable .tabbable-content'),
							contentNode: boundingBox.one('.tabbable .tabbable-content .tabview-content'),
							cssClass: 'tabbable',
							listNode: contentTabListNode
						};

						if (!contentTabListNode) {
							var strings = instance.getStrings();

							defaultValue.items = [
								{
									label: strings.view
								},
								{
									label: strings.source
								}
							];
						}

						return A.merge(defaultValue, val);
					},

					_setDefinition: function(val) {
						var instance = this;

						val = instance.sanitizeDefinitionXML(val);

						instance.definitionDoc = A.DataType.XML.parse(val);

						return val;
					},

					_translateXMLtoFields: function() {
						var instance = this;

						var fields = [];

						instance.forEachDefinitionField(
							function(tagName, fieldData) {
								fieldData.results.forEach(
									function(item, index, collection) {
										var description = jsonParse(item.description);

										var type = tagName;

										if (item.initial) {
											type = 'start';
										}

										var metadata = jsonParse(item.metadata);

										if (metadata) {
											if (metadata.terminal) {
												type = 'end';
											}
										}
										else {
											metadata = {
												xy: instance._getRandomXY()
											};
										}

										fields.push(
											{
												actions: instance._normalizeToActions(item),
												assignments: instance._normalizeToAssignments(item.assignments),
												description: description,
												fields: [{}],
												initial: item.initial,
												metadata: metadata,
												name: item.name,
												notifications: instance._normalizeToNotifications(item),
												script: item.script,
												scriptLanguage: item.scriptLanguage || DEFAULT_LANGUAGE,
												taskTimers: instance._normalizeToTaskTimers(item),
												type: type,
												xy: metadata.xy
											}
										);
									}
								);
							}
						);

						return fields;
					},

					_translateXMLToJSON: function(tagName) {
						var instance = this;

						var schema = {
							resultFields: [
								'description',
								'initial',
								'metadata',
								'name',
								'script',
								{
									key: 'scriptLanguage',
									locator: 'script-language'
								},
								instance._getSchemaActions(),
								instance._getSchemaAssignments(),
								instance._getSchemaNotifications(),
								instance._getSchemaTaskTimers(),
								instance._getSchemaTransitions()
							],
							resultListLocator: tagName
						};

						return A.DataSchema.XML.apply(schema, instance.definitionDoc);
					},

					_uiSetAvailableFields: function(val) {
						var instance = this;

						var disabled = instance.get('disabled');
						var fieldsNode = instance.fieldsNode;

						if (fieldsNode) {
							if (disabled) {
								fieldsNode.html('<div class="alert alert-info">' + KaleoDesignerStrings.inspectTaskMessage + '</div>');
							}
							else {
								KaleoDesigner.superclass._uiSetAvailableFields.apply(this, arguments);
							}
						}
					},

					_uiSetDefinition: function(val) {
						var instance = this;

						var fields = instance._translateXMLtoFields();

						instance.clearFields();

						instance.set('fields', fields);

						if (instance.get('rendered')) {
							instance.connectDefinitionFields();
						}
					}
				}
			}
		);

		var BaseAbstractEditor = A.Component.create(
			{
				ATTRS: {
					builder: {
					},

					strings: {
						value: KaleoDesignerStrings
					},

					viewTemplate: {
						setter: '_setViewTemplate'
					}
				},

				EXTENDS: A.BaseCellEditor,

				NAME: 'base-abstract-multi-section-editor',

				prototype: {
					initializer: function() {
						var instance = this;

						instance.get('boundingBox').delegate('click', A.bind(instance._onClickViewMenu, instance), '.celleditor-view-menu a');

						instance.after('visibleChange', instance._afterEditorVisibleChange);
					},

					addDynamicViews: emptyFn,

					addStaticViews: emptyFn,

					customizeToolbar: function() {
						var instance = this;

						var strings = instance.getStrings();

						instance.addSectionButton = new A.Button(
							{
								disabled: true,
								icon: 'icon-plus-sign',
								id: 'addSectionButton',
								label: strings.addSection,
								on: {
									click: A.bind(instance.handleAddViewSection, instance)
								}
							}
						).render();

						instance.toolbar.add([instance.addSectionButton]);
					},

					getValue: function() {
						var instance = this;

						return serializeForm(instance.get('contentBox'));
					},

					getViewNodes: function() {
						var instance = this;

						return instance.bodyNode.all(STR_DOT + CSS_CELLEDITOR_ASSIGNMENT_VIEW);
					},

					handleAddViewSection: emptyFn,

					removeAllViews: function(viewId) {
						var instance = this;

						if (instance.bodyNode) {
							instance.bodyNode.all(SELECTOR_PREFIX_CELLEDITOR_VIEW_TYPE + viewId).remove();
						}
					},

					showView: function(viewId) {
						var instance = this;

						instance.viewId = viewId;

						instance.getViewNodes().hide();

						instance.bodyNode.all(SELECTOR_PREFIX_CELLEDITOR_VIEW_TYPE + viewId).show();

						if (instance.get('rendered')) {
							instance.syncToolbarUI();
						}
					},

					syncToolbarUI: function() {
						var instance = this;

						instance.addSectionButton.set('disabled', !instance.viewId);
					},

					syncViewsUI: function() {
						var instance = this;

						instance._uiSetValue(instance.get('value'));
					},

					_afterEditorVisibleChange: function(event) {
						var instance = this;

						if (event.newVal) {
							instance.syncViewsUI();
						}
					},

					_afterRender: function() {
						var instance = this;

						BaseAbstractEditor.superclass._afterRender.apply(this, arguments);

						instance.addStaticViews();

						instance.customizeToolbar();

						instance.syncToolbarUI();

						instance.syncViewsUI();
					},

					_onClickViewMenu: function(event) {
						var anchor = event.currentTarget;

						if (anchor.hasClass('celleditor-view-menu-remove')) {
							anchor.ancestor('.celleditor-view').remove();
						}

						event.halt();
					},

					_setViewTemplate: function(val) {
						var instance = this;

						if (!A.instanceOf(val, A.Template)) {
							val = new Template(val);
						}

						return val;
					},

					_syncElementsFocus: emptyFn,

					_uiSetValue: function(val) {
						var instance = this;

						var contentBox = instance.get('contentBox');

						instance.addDynamicViews(val);

						A.each(
							val,
							function(item1, index1, collection1) {
								var fields = contentBox.all('[name="' + index1 + '"]');

								item1 = AArray(item1);

								fields.each(
									function(item2, index2, collection) {
										var value = item1[index2];

										if (item2.test('input[type=checkbox],input[type=radio]')) {
											item2.set('checked', A.DataType.Boolean.parse(value));
										}
										else {
											item2.val(value);
										}
									}
								);
							}
						);
					}
				}
			}
		);

		var AssignmentsEditor = A.Component.create(
			{
				ATTRS: {
					assignmentsType: {
						valueFn: '_valueAssignmentsType'
					},

					roleTypes: {
						valueFn: function() {
							var instance = this;

							var strings = instance.getStrings();

							return [
								{
									label: strings.site,
									value: 'site'
								},
								{
									label: strings.regular,
									value: 'regular'
								},
								{
									label: strings.organization,
									value: 'organization'
								}
							];
						}
					},

					scriptLanguages: {
						valueFn: function() {
							var instance = this;

							var strings = instance.getStrings();

							return [
								{
									label: strings.beanshell,
									value: 'beanshell'
								},
								{
									label: strings.drl,
									value: 'drl'
								},
								{
									label: strings.groovy,
									value: 'groovy'
								},
								{
									label: strings.javascript,
									value: 'javascript'
								},
								{
									label: strings.python,
									value: 'python'
								},
								{
									label: strings.ruby,
									value: 'ruby'
								}
							];
						}
					},

					strings: {
						valueFn: function() {
							return A.merge(
								KaleoDesignerStrings,
								{
									assignmentTypeLabel: KaleoDesignerStrings.assignmentType,
									defaultAssignmentLabel: KaleoDesignerStrings.assetCreator
								}
							);
						}
					},

					viewTemplate: {
						value: [
							'<div class="{$ans}celleditor-assignment-view {$ans}celleditor-view {$ans}celleditor-view-type-{viewId} {$ans}hide">',
								'{content}',
								'<tpl if="values.showMenu === true">',
									'<div class="{$ans}celleditor-view-menu">',
										'<a class="{$ans}celleditor-view-menu-remove" href="#">' + KaleoDesignerStrings.remove + '</a>',
									'</div>',
								'</tpl>',
							'</div>'
						]
					}
				},

				EXTENDS: BaseAbstractEditor,

				NAME: 'assignments-cell-editor',

				prototype: {
					addDynamicViews: function(val) {
						var instance = this;

						Liferay.KaleoDesignerAutoCompleteUtil.destroyAll();

						instance.removeAllViews('roleType');

						instance.addViewRoleType(instance._countRoleTypeViews(val));

						instance.removeAllViews('user');

						instance.addViewUser(instance._countUserViews(val));

						if (val) {
							instance.showView(val.assignmentType);
						}
					},

					addStaticViews: function() {
						var instance = this;

						var strings = instance.getStrings();

						var assignmentsViewTpl = instance.get('viewTemplate');

						var inputTpl = Template.get('input');
						var selectTpl = Template.get('select');
						var textareaTpl = Template.get('textarea');

						var select = selectTpl.render(
							{
								auiLabelCssClass: 'celleditor-label',
								label: strings.assignmentTypeLabel,
								name: 'assignmentType',
								options: instance.get('assignmentsType')
							}
						);

						var selectWrapper = A.Node.create('<div/>').append(select);

						instance.typeSelect = selectWrapper.one('select');

						instance.setStdModContent(WidgetStdMod.BODY, selectWrapper);

						instance.typeSelect.on(['change', 'keyup'], A.bind(instance._onTypeValueChange, instance));

						var buffer = [];

						var resourceActionContent = textareaTpl.parse(
							{
								auiCssClass: 'celleditor-textarea-small',
								auiLabelCssClass: 'celleditor-label',
								id: A.guid(),
								label: strings.resourceActions,
								name: 'resourceActions'
							}
						);

						buffer.push(
							assignmentsViewTpl.parse(
								{
									content: resourceActionContent,
									viewId: 'resourceActions'
								}
							)
						);

						var roleIdContent = [
							inputTpl.parse(
								{
									auiCssClass: 'assignments-cell-editor-input',
									auiLabelCssClass: 'celleditor-label',
									id: A.guid(),
									label: strings.role,
									name: 'roleNameAC',
									placeholder: KaleoDesignerStrings.search,
									size: 35,
									type: 'text'
								}
							),

							inputTpl.parse(
								{
									auiCssClass: 'assignments-cell-editor-input',
									auiLabelCssClass: 'celleditor-label',
									disabled: true,
									id: A.guid(),
									label: strings.roleId,
									name: 'roleId',
									size: 35,
									type: 'text'
								}
							)
						].join(STR_BLANK);

						buffer.push(
							assignmentsViewTpl.parse(
								{
									content: roleIdContent,
									viewId: 'roleId'
								}
							)
						);

						var scriptedAssignmentContent = [
							textareaTpl.parse(
								{
									auiCssClass: 'celleditor-textarea',
									auiLabelCssClass: 'celleditor-label',
									id: A.guid(),
									label: strings.script,
									name: 'script'
								}
							),

							selectTpl.parse(
								{
									auiLabelCssClass: 'celleditor-label',
									id: A.guid(),
									label: strings.scriptLanguage,
									name: 'scriptLanguage',
									options: instance.get('scriptLanguages')
								}
							)
						].join(STR_BLANK);

						buffer.push(
							assignmentsViewTpl.parse(
								{
									content: scriptedAssignmentContent,
									viewId: 'scriptedAssignment'
								}
							)
						);

						instance.setStdModContent(WidgetStdMod.BODY, buffer.join(STR_BLANK), WidgetStdMod.AFTER);
					},

					addViewRoleType: function(num) {
						var instance = this;

						num = num || 1;

						var strings = instance.getStrings();

						var assignmentsViewTpl = instance.get('viewTemplate');

						var checkboxTpl = Template.get('checkbox');
						var inputTpl = Template.get('input');
						var selectTpl = Template.get('select');

						var buffer = [];

						for (var i = 0; i < num; i++) {
							var roleTypeContent = [
								selectTpl.parse(
									{
										auiCssClass: 'assignments-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.roleType,
										name: 'roleType',
										options: instance.get('roleTypes')
									}
								),

								inputTpl.parse(
									{
										auiCssClass: 'assignments-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.roleName,
										name: 'roleName',
										size: 35,
										type: 'text'
									}
								),

								'<br/>',

								checkboxTpl.parse(
									{
										auiCssClass: 'assignments-cell-editor-input',
										auiLabelCssClass: 'celleditor-label-checkbox',
										id: A.guid(),
										label: strings.autoCreate,
										name: 'autoCreate',
										type: 'checkbox',
										value: 'true'
									}
								)
							].join(STR_BLANK);

							buffer.push(
								assignmentsViewTpl.parse(
									{
										content: roleTypeContent,
										showMenu: true,
										viewId: 'roleType'
									}
								)
							);
						}

						instance.setStdModContent(WidgetStdMod.BODY, buffer.join(STR_BLANK), WidgetStdMod.AFTER);
					},

					addViewUser: function(num) {
						var instance = this;

						num = num || 1;

						var strings = instance.getStrings();

						var assignmentsViewTpl = instance.get('viewTemplate');

						var inputTpl = Template.get('input');

						var buffer = [];

						for (var i = 0; i < num; i++) {
							var userContent = [
								inputTpl.parse(
									{
										auiCssClass: 'assignments-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.name,
										name: 'fullName',
										placeholder: KaleoDesignerStrings.search,
										size: 35,
										type: 'text'
									}
								),

								inputTpl.parse(
									{
										auiCssClass: 'assignments-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										disabled: true,
										id: A.guid(),
										label: strings.screenName,
										name: 'screenName',
										size: 35,
										type: 'text'
									}
								),

								inputTpl.parse(
									{
										auiCssClass: 'assignments-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										disabled: true,
										id: A.guid(),
										label: strings.emailAddress,
										name: 'emailAddress',
										size: 35,
										type: 'text'
									}
								),

								inputTpl.parse(
									{
										auiCssClass: 'assignments-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										disabled: true,
										id: A.guid(),
										label: strings.userId,
										name: 'userId',
										size: 35,
										type: 'text'
									}
								)
							].join(STR_BLANK);

							buffer.push(
								assignmentsViewTpl.parse(
									{
										content: userContent,
										showMenu: true,
										viewId: 'user'
									}
								)
							);
						}

						instance.setStdModContent(WidgetStdMod.BODY, buffer.join(STR_BLANK), WidgetStdMod.AFTER);
					},

					handleAddViewSection: function(event) {
						var instance = this;

						var button = event.target;

						if (!button.get('disabled')) {
							var viewId = instance.viewId;

							if (viewId === 'user') {
								instance.addViewUser();
							}
							else if (viewId === 'roleType') {
								instance.addViewRoleType();
							}

							instance.showView(viewId);
						}
					},

					syncToolbarUI: function() {
						var instance = this;

						var viewId = instance.viewId;

						var disabled = viewId !== 'roleType' && viewId !== 'user';

						instance.addSectionButton.set('disabled', disabled);
					},

					syncViewsUI: function() {
						var instance = this;

						AssignmentsEditor.superclass.syncViewsUI.apply(this, arguments);

						instance.showView(instance.typeSelect.val());
					},

					_countRoleTypeViews: function(val) {
						var instance = this;

						var count = 0;

						if (val) {
							count = val.roleType ? val.roleType.filter(isValue).length : 1;
						}

						return count;
					},

					_countUserViews: function(val) {
						var instance = this;

						var count = 0;

						if (val) {
							count = Math.max(
								val.emailAddress ? val.emailAddress.filter(isValue).length : 1,
								val.screenName ? val.screenName.filter(isValue).length : 1,
								val.userId ? val.userId.filter(isValue).length : 1
							);
						}

						return count;
					},

					_onTypeValueChange: function(event) {
						var instance = this;

						instance.showView(event.currentTarget.val());
					},

					_syncElementsFocus: function() {
						var instance = this;

						instance.typeSelect.focus();
					},

					_valueAssignmentsType: function() {
						var instance = this;

						var strings = instance.getStrings();

						return [
							{
								label: strings.defaultAssignmentLabel,
								value: STR_BLANK
							},
							{
								label: strings.resourceActions,
								value: 'resourceActions'
							},
							{
								label: strings.role,
								value: 'roleId'
							},
							{
								label: strings.roleType,
								value: 'roleType'
							},
							{
								label: strings.scriptedAssignment,
								value: 'scriptedAssignment'
							},
							{
								label: strings.user,
								value: 'user'
							}
						];
					}
				}
			}
		);

		var NotificationRecipientsEditor = A.Component.create(
			{
				ATTRS: {
					index: {
						value: 0
					},

					parentEditor: {
						value: null
					},

					strings: {
						valueFn: function() {
							return A.merge(
								KaleoDesignerStrings,
								{
									assignmentTypeLabel: KaleoDesignerStrings.recipientType,
									defaultAssignmentLabel: KaleoDesignerStrings.assetCreator
								}
							);
						}
					}
				},

				EXTENDS: AssignmentsEditor,

				NAME: 'notifications-cell-editor',

				prototype: {
					_valueAssignmentsType: function() {
						var instance = this;

						var parentEditor = instance.get('parentEditor');

						var formValue = serializeForm(parentEditor.get('contentBox'));

						var executionType = formValue.executionType[instance.get('index')];

						var assignmentsTypes = NotificationRecipientsEditor.superclass._valueAssignmentsType.apply(instance, arguments);

						if (executionType === 'onAssignment') {
							assignmentsTypes.push(
								{
									label: KaleoDesignerStrings.taskAssignees,
									value: 'taskAssignees'
								}
							);
						}

						return assignmentsTypes;
					}
				}
			}
		);

		var FormsEditor = A.Component.create(
			{
				ATTRS: {
					viewTemplate: {
						value: [
							'<div class="{$ans}celleditor-forms-view {$ans}celleditor-view {$ans}celleditor-view-type-{viewId}">{content}</div>'
						]
					}
				},

				EXTENDS: BaseAbstractEditor,

				NAME: 'forms-cell-editor',

				prototype: {
					addStaticViews: function() {
						var instance = this;

						var strings = instance.getStrings();

						var formsViewTpl = instance.get('viewTemplate');

						var inputTpl = Template.get('input');
						var textareaTpl = Template.get('textarea');

						var buffer = [];

						var formsContent = [
							inputTpl.parse(
								{
									auiCssClass: 'forms-cell-editor-input',
									auiLabelCssClass: 'celleditor-label',
									id: A.guid(),
									label: strings.formTemplate,
									name: 'templateName',
									placeholder: KaleoDesignerStrings.search,
									size: 35,
									type: 'text'
								}
							),

							inputTpl.parse(
								{
									auiCssClass: 'forms-cell-editor-input',
									auiLabelCssClass: 'celleditor-label',
									id: A.guid(),
									name: 'templateId',
									size: 35,
									type: 'hidden'
								}
							)
						].join(STR_BLANK);

						buffer.push(
							formsViewTpl.parse(
								{
									content: formsContent,
									viewId: 'formTemplateId'
								}
							)
						);

						instance.setStdModContent(WidgetStdMod.BODY, buffer.join(STR_BLANK), WidgetStdMod.AFTER);
					}
				}
			}
		);

		var executionTypesSetterFn = function(val) {
			var instance = this;

			var strings = instance.getStrings();

			var selectedNode = instance.get('builder.selectedNode');
			var type = selectedNode.get('type');

			if (type === 'task') {
				val.push(
					{
						label: strings.onAssignment,
						value: 'onAssignment'
					}
				);
			}

			val.push(
				{
					label: strings.onEntry,
					value: 'onEntry'
				},
				{
					label: strings.onExit,
					value: 'onExit'
				}
			);

			return val;
		};

		var NotificationsEditor = A.Component.create(
			{
				ATTRS: {
					executionTypes: {
						setter: executionTypesSetterFn,
						value: []
					},

					notificationTypes: {
						valueFn: function() {
							var instance = this;

							var strings = instance.getStrings();

							return [
								{
									label: strings.email,
									value: 'email'
								},
								{
									label: strings.im,
									value: 'im'
								},
								{
									label: strings.privateMessage,
									value: 'private-message'
								},
								{
									label: strings.userNotification,
									value: 'user-notification'
								}
							];
						}
					},

					recipients: {
						getter: '_getRecipients',
						value: []
					},

					templateLanguages: {
						valueFn: function() {
							var instance = this;

							var strings = instance.getStrings();

							return [
								{
									label: strings.freemarker,
									value: 'freemarker'
								},
								{
									label: strings.text,
									value: 'text'
								},
								{
									label: strings.velocity,
									value: 'velocity'
								}
							];
						}
					},

					viewTemplate: {
						value: [
							'<div class="{$ans}celleditor-notifications-view {$ans}celleditor-view {$ans}celleditor-view-type-{viewId}">',
								'{content}',
								'<div class="{$ans}celleditor-view-menu">',
									'<a class="{$ans}celleditor-view-menu-edit-recipients" href="#">' + KaleoDesignerStrings.editRecipients + '</a> ',
									'<a class="{$ans}celleditor-view-menu-remove" href="#">' + KaleoDesignerStrings.remove + '</a>',
								'</div>',
							'</div>'
						]
					}
				},

				EXTENDS: BaseAbstractEditor,

				NAME: 'notifications-cell-editor',

				prototype: {
					addDynamicViews: function(val) {
						var instance = this;

						instance.removeAllViews('notification');

						instance.addNotificationView(instance._countNotificationViews(val));
					},

					addNotificationView: function(num) {
						var instance = this;

						num = num || 1;

						var strings = instance.getStrings();

						var notificationsViewTpl = instance.get('viewTemplate');

						var inputTpl = Template.get('input');
						var selectTpl = Template.get('select');
						var textareaTpl = Template.get('textarea');

						var buffer = [];

						for (var i = 0; i < num; i++) {
							var notificationContent = [
								inputTpl.parse(
									{
										auiCssClass: 'notifications-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.name,
										name: 'name',
										size: 35,
										type: 'text'
									}
								),

								textareaTpl.parse(
									{
										auiCssClass: 'celleditor-textarea-small',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.description,
										name: 'description'
									}
								),

								selectTpl.parse(
									{
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.templateLanguage,
										name: 'templateLanguage',
										options: instance.get('templateLanguages')
									}
								),

								textareaTpl.parse(
									{
										auiCssClass: 'celleditor-textarea',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.template,
										name: 'template'
									}
								),

								selectTpl.parse(
									{
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.notificationType,
										name: 'notificationType',
										options: instance.get('notificationTypes')
									}
								),

								selectTpl.parse(
									{
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.executionType,
										name: 'executionType',
										options: instance.get('executionTypes')
									}
								)
							].join(STR_BLANK);

							buffer.push(
								notificationsViewTpl.parse(
									{
										content: notificationContent,
										viewId: 'notification'
									}
								)
							);
						}

						instance.setStdModContent(WidgetStdMod.BODY, buffer.join(STR_BLANK), WidgetStdMod.AFTER);
					},

					getEditRecipientsLinks: function() {
						var instance = this;

						return instance.get('boundingBox').all('.celleditor-view-menu-edit-recipients');
					},

					getRecipientsEditor: function(anchor) {
						var instance = this;

						var recipients = instance.get('recipients');
						var recipientsEditorIndex = instance.getEditRecipientsLinks().indexOf(anchor);
						var recipientsEditorValue = recipients[recipientsEditorIndex];

						if (instance._recipientsEditor) {
							instance._recipientsEditor.setAttrs(
								{
									index: recipientsEditorIndex,
									value: recipientsEditorValue
								}
							);
						}
						else {
							instance._recipientsEditor = new NotificationRecipientsEditor(
								{
									builder: instance.get('builder'),
									index: recipientsEditorIndex,
									on: {
										save: function(event) {
											var editor = this;

											var value = editor.getValue();

											editor.set('value', value);

											recipients[editor.get('index')] = value;

											instance.set('recipients', recipients);
										}
									},
									parentEditor: instance,
									render: anchor.ancestor('.basecelleditor'),
									value: recipientsEditorValue,
									visible: false
								}
							);
						}

						return instance._recipientsEditor;
					},

					getValue: function() {
						var instance = this;

						var editRecipientsLinks = instance.getEditRecipientsLinks();

						var recipients = [];

						editRecipientsLinks.each(
							function(item, index, collection) {
								var recipientsEditor = instance.getRecipientsEditor(item);

								if (recipientsEditor) {
									recipients.push(recipientsEditor.getValue());
								}
							}
						);

						return A.merge(
							NotificationsEditor.superclass.getValue.apply(this, arguments),
							{
								recipients: recipients
							}
						);
					},

					handleAddViewSection: function(event) {
						var instance = this;

						var button = event.target;

						if (!button.get('disabled')) {
							var viewId = instance.viewId;

							if (viewId === 'notification') {
								instance.addNotificationView();
							}

							instance.showView(viewId);
						}
					},

					syncToolbarUI: function() {
						var instance = this;

						instance.addSectionButton.set('disabled', false);
					},

					syncViewsUI: function() {
						var instance = this;

						NotificationsEditor.superclass.syncViewsUI.apply(this, arguments);

						instance.showView('notification');
					},

					_countNotificationViews: function(val) {
						var instance = this;

						var count = 0;

						if (val) {
							count = val.notificationType ? val.notificationType.filter(isValue).length : 1;
						}

						return count;
					},

					_getRecipients: function(val) {
						var instance = this;

						return instance.get('value.recipients') || val;
					},

					_onClickEditRecipientsMenu: function(event) {
						var instance = this;

						var anchor = event.currentTarget;

						var recipientsEditor = instance.getRecipientsEditor(anchor);

						recipientsEditor.show().set('xy', anchor.getXY());
					},

					_onClickViewMenu: function(event) {
						var instance = this;

						NotificationsEditor.superclass._onClickViewMenu.apply(this, arguments);

						if (event.currentTarget.test('.celleditor-view-menu-edit-recipients')) {
							instance._onClickEditRecipientsMenu(event);
						}

						event.halt();
					},

					_syncElementsFocus: function() {
						var instance = this;

						instance.bodyNode.one(':input').focus();
					}
				}
			}
		);

		var ActionsEditor = A.Component.create(
			{
				ATTRS: {
					executionTypes: {
						setter: executionTypesSetterFn,
						value: []
					},

					scriptLanguages: {
						valueFn: function() {
							var instance = this;

							var strings = instance.getStrings();

							return [
								{
									label: strings.beanshell,
									value: 'beanshell'
								},
								{
									label: strings.drl,
									value: 'drl'
								},
								{
									label: strings.groovy,
									value: 'groovy'
								},
								{
									label: strings.javascript,
									value: 'javascript'
								},
								{
									label: strings.python,
									value: 'python'
								},
								{
									label: strings.ruby,
									value: 'ruby'
								}
							];
						}
					},

					viewTemplate: {
						value: [
							'<div class="{$ans}celleditor-actions-view {$ans}celleditor-view {$ans}celleditor-view-type-{viewId}">',
								'{content}',
								'<div class="{$ans}celleditor-view-menu">',
									'<a class="{$ans}celleditor-view-menu-remove" href="#">' + KaleoDesignerStrings.remove + '</a>',
								'</div>',
							'</div>'
						]
					}
				},

				EXTENDS: BaseAbstractEditor,

				NAME: 'actions-cell-editor',

				prototype: {
					addActionView: function(num) {
						var instance = this;

						num = num || 1;

						var strings = instance.getStrings();

						var actionsViewTpl = instance.get('viewTemplate');

						var inputTpl = Template.get('input');
						var selectTpl = Template.get('select');
						var textareaTpl = Template.get('textarea');

						var buffer = [];

						for (var i = 0; i < num; i++) {
							var actionContent = [
								inputTpl.parse(
									{
										auiCssClass: 'actions-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.name,
										name: 'name',
										size: 35,
										type: 'text'
									}
								),

								textareaTpl.parse(
									{
										auiCssClass: 'celleditor-textarea-small',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.description,
										name: 'description'
									}
								),

								textareaTpl.parse(
									{
										auiCssClass: 'celleditor-textarea',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.script,
										name: 'script'
									}
								),

								selectTpl.parse(
									{
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.scriptLanguage,
										name: 'scriptLanguage',
										options: instance.get('scriptLanguages')
									}
								),

								selectTpl.parse(
									{
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.executionType,
										name: 'executionType',
										options: instance.get('executionTypes')
									}
								),

								inputTpl.parse(
									{
										auiCssClass: 'actions-cell-editor-input',
										auiLabelCssClass: 'celleditor-label',
										id: A.guid(),
										label: strings.priority,
										name: 'priority',
										size: 35,
										type: 'text'
									}
								)
							].join(STR_BLANK);

							buffer.push(
								actionsViewTpl.parse(
									{
										content: actionContent,
										viewId: 'action'
									}
								)
							);
						}

						instance.setStdModContent(WidgetStdMod.BODY, buffer.join(STR_BLANK), WidgetStdMod.AFTER);
					},

					addDynamicViews: function(val) {
						var instance = this;

						instance.removeAllViews('action');

						instance.addActionView(instance._countActionViews(val));
					},

					handleAddViewSection: function(event) {
						var instance = this;

						var button = event.target;

						if (!button.get('disabled')) {
							var viewId = instance.viewId;

							if (viewId === 'action') {
								instance.addActionView();
							}

							instance.showView(viewId);
						}
					},

					syncToolbarUI: function() {
						var instance = this;

						instance.addSectionButton.set('disabled', false);
					},

					syncViewsUI: function() {
						var instance = this;

						ActionsEditor.superclass.syncViewsUI.apply(this, arguments);

						instance.showView('action');
					},

					_countActionViews: function(val) {
						var instance = this;

						var count = 0;

						if (val) {
							return val.name ? val.name.filter(isValue).length : 1;
						}

						return count;
					},

					_syncElementsFocus: function() {
						var instance = this;

						instance.bodyNode.one(':input').focus();
					}
				}
			}
		);

		var ScriptEditor = A.Component.create(
			{
				EXTENDS: A.BaseCellEditor,

				NAME: 'script-cell-editor',

				prototype: {
					initializer: function() {
						var instance = this;

						instance.editor = new A.AceEditor(
							{
								height: 300,
								width: 550
							}
						);
					},

					getValue: function() {
						var instance = this;

						return instance.editor.get('value');
					},

					_afterRender: function() {
						var instance = this;

						var editor = instance.editor;

						ScriptEditor.superclass._afterRender.apply(this, arguments);

						instance.setStdModContent(WidgetStdMod.BODY, STR_BLANK, WidgetStdMod.AFTER);

						setTimeout(
							function() {
								editor.render(instance.bodyNode);
							},
							0
						);
					},

					_syncElementsFocus: emptyFn,

					_uiSetValue: function(val) {
						var instance = this;

						var editor = instance.editor;

						if (editor && isValue(val)) {
							editor.set('value', val);
						}
					}
				}
			}
		);

		var Connector = A.Component.create(
			{
				ATTRS: {
					'default': {
						setter: A.DataType.Boolean.parse,
						value: false
					}
				},

				EXTENDS: A.Connector,

				NAME: 'line',

				STRINGS: KaleoDesignerStrings,

				prototype: {
					SERIALIZABLE_ATTRS: A.Connector.prototype.SERIALIZABLE_ATTRS.concat(['default']),

					getPropertyModel: function() {
						var instance = this;

						var parentModel = A.Connector.superclass.getPropertyModel.apply(this, arguments);

						return AArray(parentModel).concat(
							[
								{
									attributeName: 'default',
									editor: new A.RadioCellEditor(
										{
											options: ['true', 'false']
										}
									),
									name: KaleoDesignerStrings['default']
								}
							]
						);
					}
				}
			}
		);

		var DiagramNodeState = A.Component.create(
			{
				ATTRS: {
					actions: {
					},

					cssClass: {
						value: 'icon-db-state'
					},

					initial: {
						setter: A.DataType.Boolean.parse,
						value: false
					},

					metadata: {
						validator: isObject,
						value: {}
					},

					notifications: {
					},

					strings: {
						value: KaleoDesignerStrings
					},

					taskTimers: {
					},

					xmlType: {
						value: 'state'
					}
				},

				AUGMENTS: [A.WidgetCssClass],

				EXTENDS: A.DiagramNodeState,

				NAME: 'diagram-node',

				prototype: {
					SERIALIZABLE_ATTRS: A.DiagramNode.prototype.SERIALIZABLE_ATTRS.concat(['actions', 'notifications', 'initial', 'metadata', 'recipients', 'script', 'scriptLanguage', 'taskTimers', 'xmlType']),

					getConnectionNode: function() {
						var instance = this;

						var node = new Liferay.KaleoDesignerNodes.DiagramNodeTask(
							{
								xy: [100, 100]
							}
						);

						return node;
					},

					getPropertyModel: function() {
						var instance = this;

						var builder = instance.get('builder');

						var availablePropertyModels = builder.get('availablePropertyModels');

						var strings = instance.getStrings();
						var type = instance.get('type');

						var model = AArray(
							[
								{
									attributeName: 'actions',
									editor: new ActionsEditor(
										{
											builder: builder
										}
									),
									formatter: PropertyListFormatter.names,
									name: strings.actions
								},
								{
									attributeName: 'notifications',
									editor: new NotificationsEditor(
										{
											builder: builder
										}
									),
									formatter: PropertyListFormatter.names,
									name: strings.notifications
								}
							]
						);

						var typeModel = availablePropertyModels[type];

						var parentModel = DiagramNodeState.superclass.getPropertyModel.apply(this, arguments);

						var returnValue;

						if (typeModel) {
							returnValue = typeModel.call(this, model, parentModel, arguments);
						}
						else {
							returnValue = model.concat(parentModel);
						}

						return returnValue;
					},

					updateMetadata: function(key, value) {
						var instance = this;

						var metadata = instance.get('metadata');

						metadata[key] = value;

						instance.set('metadata', metadata);
					},

					_uiSetXY: function(val) {
						var instance = this;

						DiagramNodeState.superclass._uiSetXY.apply(this, arguments);

						instance.updateMetadata('xy', val);
					}
				}
			}
		);

		DiagramBuilderTypes.state = DiagramNodeState;

		var DiagramNodeCondition = A.Component.create(
			{
				ATTRS: {
					cssClass: {
						value: 'icon-db-condition'
					},

					height: {
						value: 60
					},

					script: {
						validator: isString,
						value: 'returnValue = "Transition Name";'
					},

					scriptLanguage: {
						validator: isString,
						value: 'groovy'
					},

					type: {
						validator: isString,
						value: 'condition'
					},

					width: {
						value: 60
					},

					xmlType: {
						validator: isString,
						value: 'condition'
					}
				},

				EXTENDS: DiagramNodeState,

				NAME: 'diagram-node',

				prototype: {
					hotPoints: A.DiagramNode.DIAMOND_POINTS,

					getPropertyModel: function() {
						var instance = this;

						var builder = instance.get('builder');

						var availablePropertyModels = builder.get('availablePropertyModels');

						var type = instance.get('type');

						var strings = instance.getStrings();

						var model = AArray(
							[
								{
									attributeName: 'script',
									editor: new ScriptEditor(),
									formatter: PropertyListFormatter.script,
									name: strings.script
								},
								{
									attributeName: 'scriptLanguage',
									editor: new A.DropDownCellEditor(
										{
											options: {
												beanshell: strings.beanshell,
												drl: strings.drl,
												groovy: strings.groovy,
												javascript: strings.javascript,
												python: strings.python,
												ruby: strings.ruby
											}
										}
									),
									name: strings.scriptLanguage
								}
							]
						);

						var typeModel = availablePropertyModels[type];

						var parentModel = DiagramNodeCondition.superclass.getPropertyModel.apply(this, arguments);

						var returnValue;

						if (typeModel) {
							returnValue = typeModel.call(this, model, parentModel, arguments);
						}
						else {
							returnValue = model.concat(parentModel);
						}

						return returnValue;
					},

					renderShapeBoundary: A.DiagramNodeCondition.prototype.renderShapeBoundary,

					_valueShapeBoundary: A.DiagramNodeCondition.prototype._valueShapeBoundary
				}
			}
		);

		DiagramBuilderTypes.condition = DiagramNodeCondition;

		var DiagramNodeJoin = A.Component.create(
			{
				ATTRS: {
					cssClass: {
						value: 'icon-db-join'
					},

					height: {
						value: 60
					},

					type: {
						validator: isString,
						value: 'join'
					},

					width: {
						value: 60
					},

					xmlType: {
						validator: isString,
						value: 'join'
					}
				},

				EXTENDS: DiagramNodeState,

				NAME: 'diagram-node',

				prototype: {
					hotPoints: A.DiagramNode.DIAMOND_POINTS,

					renderShapeBoundary: A.DiagramNodeJoin.prototype.renderShapeBoundary,

					_valueShapeBoundary: A.DiagramNodeJoin.prototype._valueShapeBoundary
				}
			}
		);

		DiagramBuilderTypes.join = DiagramNodeJoin;

		var DiagramNodeJoinXOR = A.Component.create(
			{
				ATTRS: {
					cssClass: {
						value: 'icon-db-joinxor'
					},

					type: {
						validator: isString,
						value: 'join-xor'
					},

					xmlType: {
						validator: isString,
						value: 'join-xor'
					}
				},

				EXTENDS: DiagramNodeJoin,

				NAME: 'diagram-node'
			}
		);

		DiagramBuilderTypes['join-xor'] = DiagramNodeJoinXOR;

		var DiagramNodeFork = A.Component.create(
			{
				ATTRS: {
					cssClass: {
						value: 'icon-db-fork'
					},

					height: {
						value: 60
					},

					type: {
						validator: isString,
						value: 'fork'
					},

					width: {
						value: 60
					},

					xmlType: {
						validator: isString,
						value: 'fork'
					}
				},

				EXTENDS: DiagramNodeState,

				NAME: 'diagram-node',

				prototype: {
					hotPoints: A.DiagramNode.DIAMOND_POINTS,

					getConnectionNode: function() {
						var instance = this;

						var node = new DiagramNodeJoin(
							{
								xy: [100, 100]
							}
						);

						return node;
					},

					renderShapeBoundary: A.DiagramNodeFork.prototype.renderShapeBoundary,

					_valueShapeBoundary: A.DiagramNodeFork.prototype._valueShapeBoundary
				}
			}
		);

		DiagramBuilderTypes.fork = DiagramNodeFork;

		var DiagramNodeStart = A.Component.create(
			{
				ATTRS: {
					cssClass: {
						value: 'icon-db-start'
					},

					initial: {
						value: true
					},

					maxFields: {
						validator: isNumber,
						value: 1
					},

					type: {
						validator: isString,
						value: 'start'
					}
				},

				EXTENDS: DiagramNodeState,

				NAME: 'diagram-node',

				prototype: {
					getConnectionNode: function() {
						var instance = this;

						var node = new DiagramNodeCondition(
							{
								xy: [100, 100]
							}
						);

						return node;
					}
				}
			}
		);

		DiagramBuilderTypes.start = DiagramNodeStart;

		var DiagramNodeEnd = A.Component.create(
			{
				ATTRS: {
					cssClass: {
						value: 'icon-db-end'
					},

					metadata: {
						value: {
							terminal: true
						}
					},

					type: {
						validator: isString,
						value: 'end'
					}
				},

				EXTENDS: DiagramNodeState,

				NAME: 'diagram-node',

				prototype: {
					_handleAddAnchorEvent: function(event) {
						var instance = this;

						instance.addField(
							{
								maxTargets: 0
							}
						);
					},

					_handleAddNodeEvent: function(event) {
						var instance = this;

						var builder = instance.get('builder');

						var source = instance.findAvailableAnchor();

						if (source) {
							var diagramNode = instance.getConnectionNode();

							builder.addField(diagramNode);
							diagramNode.addField({}).connect(source);
						}
					}
				}
			}
		);

		DiagramBuilderTypes.end = DiagramNodeEnd;

		var DiagramNodeTask = A.Component.create(
			{
				ATTRS: {
					assignments: {
						validator: isObject,
						value: {}
					},

					cssClass: {
						value: 'icon-db-task'
					},

					forms: {
						value: {
							templateId: [0],
							templateName: [STR_BLANK]
						}
					},

					height: {
						value: 70
					},

					type: {
						validator: isString,
						value: 'task'
					},

					width: {
						value: 70
					},

					xmlType: {
						validator: isString,
						value: 'task'
					}
				},

				EXTENDS: DiagramNodeState,

				NAME: 'diagram-node',

				prototype: {
					SERIALIZABLE_ATTRS: DiagramNodeState.prototype.SERIALIZABLE_ATTRS.concat(['assignments']),

					hotPoints: A.DiagramNode.SQUARE_POINTS,

					renderShapeBoundary: A.DiagramNodeTask.prototype.renderShapeBoundary,

					_valueShapeBoundary: A.DiagramNodeTask.prototype._valueShapeBoundary
				}
			}
		);

		KaleoDesigner.AVAILABLE_FIELDS = {
			DEFAULT: [
				{
					iconClass: 'icon-db-condition',
					label: Liferay.Language.get('condition'),
					type: 'condition'
				},
				{
					iconClass: 'icon-db-end',
					label: Liferay.Language.get('end'),
					type: 'end'
				},
				{
					iconClass: 'icon-db-fork',
					label: Liferay.Language.get('fork'),
					type: 'fork'
				},
				{
					iconClass: 'icon-db-join',
					label: Liferay.Language.get('join'),
					type: 'join'
				},
				{
					iconClass: 'icon-db-joinxor',
					label: Liferay.Language.get('join-xor'),
					type: 'join-xor'
				},
				{
					iconClass: 'icon-db-start',
					label: Liferay.Language.get('start'),
					type: 'start'
				},
				{
					iconClass: 'icon-db-state',
					label: Liferay.Language.get('state'),
					type: 'state'
				},
				{
					iconClass: 'icon-db-task',
					label: Liferay.Language.get('task'),
					type: 'task'
				}
			]
		};

		KaleoDesigner.AVAILABLE_PROPERTY_MODELS = {
			DEFAULT: {},

			KALEO_FORMS_ASSIGN_TASK_FORMS: {
				condition: emptyFnFalse,

				end: emptyFnFalse,

				fork: emptyFnFalse,

				join: emptyFnFalse,

				start: emptyFnFalse,

				state: emptyFnFalse,

				task: function(model, parentModel) {
					var instance = this;

					var builder = instance.get('builder');

					var portletResourceNamespace = builder.get('portletResourceNamespace');

					var strings = instance.getStrings();

					return [
						{
							attributeName: 'forms',
							editor: new FormsEditor(
								{
									after: {
										visibleChange: function(event) {
											if (event.newVal) {
												var builder = instance.get('builder');

												var kaleoProcessId = builder.get('data.kaleoProcessId');

												var workflowTaskName = instance.get('name');

												var getDDMTemplate = Liferay.Util.getOpener()[portletResourceNamespace + 'getDDMTemplate'];

												if (getDDMTemplate) {
													getDDMTemplate(
														kaleoProcessId,
														workflowTaskName,
														function(json1, json2) {
															var templateName = builder.getLocalizedName(json2.name);

															var value = {
																templateId: [json1.DDMTemplateId],
																templateName: [templateName]
															};

															var editingNode = builder.editingNode;

															if (editingNode) {
																editingNode.set('forms', value);
															}

															event.currentTarget.set('value', value);
														}
													);
												}
											}
										}
									}
								}
							),
							formatter: PropertyListFormatter.forms,
							name: strings.forms
						},
						{
							attributeName: 'name',
							editor: false,
							formatter: PropertyListFormatter.name,
							name: strings.name
						},
						{
							attributeName: 'type',
							editor: false,
							name: strings.type
						}
					];
				}
			},

			KALEO_FORMS_EDIT: {
				task: function(model, parentModel) {
					var instance = this;

					var strings = instance.getStrings();

					return parentModel.concat(model).concat(
						[
							{
								attributeName: 'assignments',
								editor: new AssignmentsEditor(),
								formatter: PropertyListFormatter.assignmentsType,
								name: strings.assignments
							}
						]
					);
				}
			}
		};

		DiagramBuilderTypes.task = DiagramNodeTask;

		A.Connector = Connector;

		Liferay.KaleoDesigner = KaleoDesigner;

		Liferay.KaleoDesignerAutoCompleteUtil = AutoCompleteUtil;

		Liferay.KaleoDesignerEditors = {
			ActionsEditor: ActionsEditor,
			AssignmentsEditor: AssignmentsEditor,
			BaseAbstractEditor: BaseAbstractEditor,
			FormsEditor: FormsEditor,
			NotificationRecipientsEditor: NotificationRecipientsEditor,
			NotificationsEditor: NotificationsEditor,
			ScriptEditor: ScriptEditor
		};

		Liferay.KaleoDesignerNodes = {
			DiagramNodeCondition: DiagramNodeCondition,
			DiagramNodeEnd: DiagramNodeEnd,
			DiagramNodeFork: DiagramNodeFork,
			DiagramNodeJoin: DiagramNodeJoin,
			DiagramNodeStart: DiagramNodeStart,
			DiagramNodeState: DiagramNodeState,
			DiagramNodeTask: DiagramNodeTask
		};
	},
	'',
	{
		requires: ['aui-ace-editor', 'aui-ace-editor-mode-xml', 'aui-diagram-builder', 'aui-tpl-snippets-deprecated', 'autocomplete', 'autocomplete-highlighters', 'dataschema-xml', 'datasource', 'datatype-xml', 'event-valuechange', 'io-form', 'liferay-util-window']
	}
);