'use strict';

var assert = chai.assert;

describe('Form Layout Deserializer Test Suite', function() {
	this.timeout(5000);

	before(function(done) {
		var instance = this;

		AUI().use(
			'aui-io-request',
			'liferay-forms-field-base',
			'liferay-forms-field-types',
			'liferay-forms-layout-deserializer',
			'liferay-forms-test-base',
			function(A) {
				assert.ok(Liferay.Forms.LayoutDeserializer);
				assert.ok(Liferay.FormsTests.Util);

				Liferay.FormsTests.Util.initFieldTypesRegistry();

				instance.definition = Liferay.FormsTests.Util.getTestData('definition');
				instance.layout = Liferay.FormsTests.Util.getTestData('layout');

				assert.ok(instance.definition);
				assert.ok(instance.layout);

				done();
			}
		);
	});

	it('should deserialize a simple DDM Layout', function(done) {
		var A = AUI(),
			instance = this;

		var deserializedLayout = new Liferay.Forms.LayoutDeserializer(
			{
				definition: instance.definition,
				layout: instance.layout
			}
		).deserialize();

		var layout = Liferay.FormsTests.Util.getSampleLayout();

		assert.strictEqual(
			layout.get('rows').length,
			deserializedLayout.get('rows').length
		);

		A.Array.each(
			layout.get('rows'),
			function(row, index) {
				assert.strictEqual(row.get('type'), deserializedLayout.get('rows')[index].get('type'));
				assert.strictEqual(
					row.get('cols').length,
					deserializedLayout.get('rows')[index].get('cols').length,
					'Row should have correct number of columns'
				);

				A.Array.each(
					row.get('cols'),
					function(column, colIndex) {
						assert.strictEqual(column.get('size'), deserializedLayout.get('rows')[index].get('cols')[colIndex].get('size'));

						var actualValue = deserializedLayout.get('rows')[index].get('cols')[colIndex].get('value');
						var expectedValue = column.get('value');

						assert.strictEqual(A.instanceOf(expectedValue, A.FormField), A.instanceOf(actualValue, A.FormField));
					}
				);
			}
		);

		done();
	});
});