'use strict';

var assert = chai.assert;

describe('Form Builder Test Suite', function() {
	this.timeout(5000);

	before(function(done) {
		var instance = this;

		AUI().use(
			'aui-io-request',
			'liferay-forms-field-types',
			'liferay-forms-form-builder',
			'liferay-forms-test-base',
			function(A) {
				assert.ok(Liferay.Forms.FieldTypes);
				assert.ok(Liferay.Forms.FormBuilder);
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

	it('should initialize the Form Builder with all field types that are registered', function(done) {
		var A = AUI(),
			instance = this;

		var formBuilder = new Liferay.Forms.FormBuilder(
			{
				definition: instance.definition,
				layout: instance.layout
			}
		).render();

		var fieldTypes = formBuilder.get('fieldTypes');

		assert.equal(fieldTypes, Liferay.Forms.FieldTypes.getAll());

		done();
	});
});