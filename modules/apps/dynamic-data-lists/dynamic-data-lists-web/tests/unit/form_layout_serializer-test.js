'use strict';

var assert = chai.assert;

describe('Form Layout Serializer Test Suite', function() {
	this.timeout(5000);

	before(function(done) {
		var instance = this;

		AUI().use(
			'aui-io-request',
			'liferay-forms-field-base',
			'liferay-forms-field-types',
			'liferay-forms-layout-serializer',
			'liferay-forms-test-base',
			function(A) {
				assert.ok(Liferay.Forms.LayoutSerializer);
				assert.ok(Liferay.FormsTests.Util);

				Liferay.FormsTests.Util.initFieldTypesRegistry();

				instance.expectedLayout = Liferay.FormsTests.Util.getTestData('layout');

				assert.ok(instance.expectedLayout);

				done();
			}
		);
	});

	it('should serialize a simple DDM Layout', function(done) {
		var A = AUI(),
			instance = this;

		var layout = Liferay.FormsTests.Util.getSampleLayout();

		var serializedLayout = A.JSON.parse(
			new Liferay.Forms.LayoutSerializer(
				{
					layout: layout
				}
			).serialize()
		);

		assert.strictEqual(
			instance.expectedLayout.pages.length,
			serializedLayout.pages.length
		);

		A.Array.each(
			instance.expectedLayout.pages,
			function(page, pageIndex) {
				A.Array.each(
					page.rows,
					function(row, rowIndex) {
						assert.strictEqual(row.columns.length, serializedLayout.pages[pageIndex].rows[rowIndex].columns.length);

						A.Array.each(
							row.columns,
							function(column, columnIndex) {
								assert.strictEqual(column.size, serializedLayout.pages[pageIndex].rows[rowIndex].columns[columnIndex].size);
								assert.strictEqual(column.fieldName, serializedLayout.pages[pageIndex].rows[rowIndex].columns[columnIndex].fieldName);
							}
						);
					}
				);
			}
		);

		done();
	});
});