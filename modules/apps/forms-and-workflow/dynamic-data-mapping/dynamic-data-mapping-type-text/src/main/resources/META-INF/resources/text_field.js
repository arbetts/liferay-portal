AUI.add(
	'liferay-ddm-form-field-text',
	function(A) {
		var Renderer = Liferay.DDM.Renderer;

		var Util = Renderer.Util;

		new A.TooltipDelegate(
			{
				position: 'left',
				trigger: '.liferay-ddm-form-field-text .help-icon',
				triggerHideEvent: ['blur', 'mouseleave'],
				triggerShowEvent: ['focus', 'mouseover'],
				visible: false
			}
		);

		var TextField = A.Component.create(
			{
				ATTRS: {
					displayStyle: {
						state: true,
						value: 'singleline'
					},

					options: {
						value: []
					},

					placeholder: {
						state: true,
						value: ''
					},

					type: {
						value: 'text'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-text',

				prototype: {
					initializer: function() {
						var instance = this;

						instance._eventHandlers.push(
							instance.after('optionsChange', instance._afterOptionsChange),
							instance.on('valueChanged', instance._onContentChange)
						);
					},

					bindInputEvent: function(eventName, callback, volatile) {
						var instance = this;

						if (eventName === instance.getChangeEventName()) {
							callback = A.debounce(callback, 300, instance);
						}

						return TextField.superclass.bindInputEvent.apply(instance, [eventName, callback, volatile]);
					},

					getAutoComplete: function() {
						var instance = this;

						var autoComplete = instance._autoComplete;

						var inputNode = instance.getInputNode();

						if (autoComplete) {
							autoComplete.set('inputNode', inputNode);
						}
						else {
							instance._createAutocomplete();
							autoComplete = instance._autoComplete;
						}

						return autoComplete;
					},

					getChangeEventName: function() {
						return 'input';
					},

					render: function() {
						var instance = this;

						TextField.superclass.render.apply(instance, arguments);

						var options = instance.get('options');

						if (options.length && instance.get('visible')) {
							instance._createAutocomplete();
						}

						return instance;
					},

					showErrorMessage: function() {
						var instance = this;

						TextField.superclass.showErrorMessage.apply(instance, arguments);

						var container = instance.get('container');

						var inputGroup = container.one('.input-group-container');

						inputGroup.insert(container.one('.help-block'), 'after');
					},

					_afterOptionsChange: function(event) {
						var instance = this;

						var autoComplete = instance.getAutoComplete();

						if (!Util.compare(event.newVal, event.prevVal)) {
							autoComplete.set('source', event.newVal);

							autoComplete.fire(
								'query',
								{
									inputValue: instance.getValue(),
									query: instance.getValue(),
									src: A.AutoCompleteBase.UI_SRC
								}
							);
						}
					},

					_createAutocomplete: function() {
						var instance = this;

						var inputNode = instance.getInputNode();

						if (instance._autoComplete) {
							instance._autoComplete.destroy();
						}

						instance._autoComplete = new A.AutoComplete(
							{
								after: {
									select: A.bind(instance.evaluate, instance)
								},
								inputNode: inputNode,
								maxResults: 10,
								render: true,
								resultFilters: ['charMatch', 'subWordMatch'],
								resultHighlighter: 'subWordMatch',
								resultTextLocator: 'label',
								source: instance.get('options')
							}
						);
					},

					_onContentChange: function() {
						var instance = this;

						var inputNode = instance.getInputNode();

						var rows = inputNode.val().split('\n');

						inputNode.set('rows', rows.length + 1);
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').Text = TextField;
	},
	'',
	{
		requires: ['aui-autosize-deprecated', 'aui-tooltip', 'autocomplete', 'autocomplete-filters', 'autocomplete-highlighters', 'autocomplete-highlighters-accentfold', 'liferay-ddm-form-renderer-field']
	}
);