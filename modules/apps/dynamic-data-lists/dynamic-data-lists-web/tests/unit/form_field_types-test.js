'use strict';

var assert = chai.assert;

describe('Form Field Types Test Suite', function() {
	this.timeout(5000);

	before(function(done) {
		var instance = this;

		AUI().use(
			'aui-io-request',
			'liferay-forms-field-types',
			'liferay-forms-test-base',
			function(A) {
				assert.ok(Liferay.FormsTests.Util);
				assert.ok(Liferay.Forms.FieldTypes);

				instance.fieldTypes = Liferay.FormsTests.Util.getTestData('field-types');

				assert.ok(instance.fieldTypes);

				done();
			}
		);
	});

	it('should register Field Types', function(done) {
		var A = AUI(),
			instance = this,
			FieldTypes = Liferay.Forms.FieldTypes;

		FieldTypes.register(instance.fieldTypes);

		var all = FieldTypes.getAll();

		assert.strictEqual(all.length, instance.fieldTypes.length);

		A.Array.each(
			all,
			function(fieldType, index) {
				assert.isTrue(
					A.instanceOf(fieldType, A.FormBuilderFieldType),
					'All field types should be an instance of A.FormBuilderFieldType.'
				);

				assert.isDefined(
					fieldType.get('name'),
					'All field types should have a name.'
				);

				assert.isDefined(
					fieldType.get('templateNamespace'),
					'All field types should have a templateNamespace.'
				);
			}
		);

		done();
	});
});