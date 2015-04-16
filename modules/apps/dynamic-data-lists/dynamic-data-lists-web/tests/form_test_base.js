AUI.add(
	'liferay-forms-test-base',
	function(A) {
		var TestUtil = {
			getSampleLayout: function() {
				var instance = this;

				var textFieldType = Liferay.Forms.FieldTypes.get('text');

				var textFieldClass = textFieldType.get('fieldClass');

				var defaultTextFieldConfig = {
					indexType: 'keyword',
					label: 'Text',
					repeatable: false,
					required: false,
					showLabel: true
				};

				return new A.Layout(
					{
						rows: [
							new A.FormBuilderPageBreakRow({
								index: 1,
								quantity: 1
							}),
							new A.LayoutRow({
								cols: [
									new A.LayoutCol({
										size: 6,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text1',
													options: ['value1', 'value2']
												}
											)
										)
									}),
									new A.LayoutCol({
										size: 6,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text2'
												}
											)
										)
									})
								]
							}),
							new A.LayoutRow({
								cols: [
									new A.LayoutCol({
										size: 3,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text3'
												}
											)
										)
									}),
									new A.LayoutCol({
										size: 3,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text4'
												}
											)
										)
									}),
									new A.LayoutCol({
										size: 3,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text5'
												}
											)
										)
									}),
									new A.LayoutCol({
										size: 3,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text6'
												}
											)
										)
									})
								]
							}),
							new A.LayoutRow({
								cols: [
									new A.LayoutCol({
										size: 12,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text7'
												}
											)
										)
									})
								]
							}),
							new A.LayoutRow({
								cols: [
									new A.LayoutCol({
										size: 3,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text8'
												}
											)
										)
									}),
									new A.LayoutCol({
										size: 3,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text9'
												}
											)
										)
									}),
									new A.LayoutCol({
										size: 3,
										value: new textFieldClass(
											A.merge(
												defaultTextFieldConfig,
												{
													name: 'text10'
												}
											)
										)
									}),
									new A.LayoutCol({
										size: 3
									})
								]
							})
						]
					}
				);
			},

			getTestData: function(name) {
				var instance = this;

				var response = A.io.request(
					'/base/modules/apps/dynamic-data-mapping/dynamic-data-lists-web/tests/unit/assets/' + name + '-data.json',
					{
						dataType: 'json',
						sync: true
					}
				);

				return response.get('responseData');
			},

			initFieldTypesRegistry: function() {
				var instance = this;

				Liferay.Forms.FieldTypes.register(instance.getTestData('field-types'));
			}
		};

		Liferay.namespace('FormsTests').Util = TestUtil;
	},
	'',
	{
		requires: ['aui-io-request', 'aui-layout', 'liferay-forms-field-types']
	}
);