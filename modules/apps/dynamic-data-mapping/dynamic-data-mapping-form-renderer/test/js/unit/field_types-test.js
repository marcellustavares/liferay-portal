'use strict';

var assert = chai.assert;

describe('DDM Form Field Types Test Suite', function() {
	this.timeout(5000);

	before(function(done) {
		var instance = this;

		AUI().use(
			'liferay-ddm-form-renderer-field-types',
			'liferay-ddm-form-renderer-test-base',
			function(A) {
				assert.ok(Liferay.DDM.Renderer.TestUtil);
				assert.ok(Liferay.DDM.Renderer.FieldTypes);

				instance.fieldTypes = Liferay.DDM.Renderer.TestUtil.getTestData('field-types');
				instance.definition = Liferay.DDM.Renderer.TestUtil.getTestData('definition');

				assert.ok(instance.fieldTypes);

				done();
			}
		);
	});

	it('should register Field Types', function(done) {
		var A = AUI(),
			instance = this,
			FieldTypes = Liferay.DDM.Renderer.FieldTypes;

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

				assert.ok(
					fieldType.get('name'),
					'All field types should have a name.'
				);

				assert.ok(
					fieldType.get('templateNamespace'),
					'All field types should have a templateNamespace.'
				);
			}
		);

		done();
	});
});