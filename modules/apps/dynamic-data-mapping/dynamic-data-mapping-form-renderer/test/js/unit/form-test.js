'use strict';

var assert = chai.assert;

describe('Liferay.DDM.Renderer.Form', function() {
	this.timeout(5000);

	before(function(done) {
		var instance = this;

		AUI().use(
			'liferay-ddm-form-renderer',
			'liferay-ddm-form-renderer-test-base',
			function(A) {
				assert.ok(Liferay.DDM.Renderer.TestUtil);
				assert.ok(Liferay.DDM.Renderer.FieldTypes);

				Liferay.DDM.Renderer.TestUtil.initFieldTypesRegistry();

				var container = A.Node.create(Liferay.DDM.Renderer.TestUtil.getTestHTML('form'));

				container.appendTo(document.body);

				instance.ddmForm = new Liferay.DDM.Renderer.Form(
					{
						container: container,
						definition: Liferay.DDM.Renderer.TestUtil.getTestData('definition'),
						values: Liferay.DDM.Renderer.TestUtil.getTestData('values'),
						portletNamespace: '_dynamicdatalistsweb_'
					}
				);

				done();
			}
		);
	});

	it('should get fields from the DOM and find its definition', function(done) {
		var A = AUI(),
			instance = this,
			ddmForm = instance.ddmForm,
			definition = ddmForm.get('definition'),
			fields = ddmForm.get('fields');

		assert.strictEqual(definition.fields.length, fields.length, 'Number of fields in DOM should equal number of fields in definition');

		A.Array.each(
			fields,
			function(item, index) {
				var container = item.get('container');

				assert.isTrue(container.inDoc(), 'field container should be in DOM');

				assert.strictEqual(item.get('name'), definition.fields[index].name, 'field name should equal name in definition');

				// Test DOM elements

				assert.ok(item.getInputNode(), 'there should be at least one input element in the field template');

				var labelNode = item.getLabelNode();

				assert.ok(labelNode, 'there should be at least one label element in the field template');

				assert.strictEqual(labelNode.text(), item.get('label'), 'field labels in DOM should equal to the label attribute on the current displayed locale');

				assert.isTrue(A.Lang.isString(item.getValue()), 'value should be a string');
			}
		);

		done();
	});

	it('should repeat a field', function(done) {
		var A = AUI(),
			instance = this,
			ddmForm = instance.ddmForm,
			definition = ddmForm.get('definition'),
			fields = ddmForm.get('fields');

		var field = fields[0];

		var repeatedField = field.repeat();

		assert.strictEqual(field.get('name'), repeatedField.get('name'), 'field name and repeated field name sohuld be equal');

		assert.notEqual(field.getQualifiedName(), repeatedField.getQualifiedName(), 'field qualifiedName and repeated field qualifiedName sohuld not be equal');

		assert.strictEqual(field.getIndex(), repeatedField.getIndex() - 1, 'repeated field should have an index greater than the original field index');

		done();
	});

	it('should remove a field', function(done) {
		var A = AUI(),
			instance = this,
			ddmForm = instance.ddmForm,
			fields = ddmForm.get('fields');

		var originalLength = fields.length;

		var field = fields[0];

		field.remove();

		assert.strictEqual(originalLength - 1, fields.length, 'one and only one field should be removed');

		assert.strictEqual(-1, A.Array.indexOf(fields, field), 'field sohuld not be in fields array anymore');

		assert.isFalse(field.get('container').inDoc(), 'field container should not be in DOM anymore');

		done();
	});

	it('should get field definition name from field qualified name', function(done) {
		var A = AUI(),
			instance = this,
			ddmForm = instance.ddmForm;

		var qualifiedName = '_forms_ddm$$first_name$WtUc8mPN$0$$en_US';

		assert.strictEqual(Liferay.DDM.Renderer.Util.getFieldNameFromQualifiedName(qualifiedName), 'first_name');

		done();
	});

	it('should have the toolbar clicks to call .repeat and .remove', function(done) {
		var A = AUI(),
			instance = this,
			ddmForm = instance.ddmForm,
			fields = ddmForm.get('fields'),
			field = fields[5],
			container = field.get('container');

		sinon.spy(field, 'repeat');
		sinon.spy(field, 'remove');

		container.one('.lfr-ddm-repeatable-add-button').simulate('click');
		container.one('.lfr-ddm-repeatable-delete-button').simulate('click');

		assert.isTrue(field.repeat.called, 'toolbar click should call .repeat');
		assert.isTrue(field.remove.called, 'toolbar click should call .remove');

		done();
	});
});