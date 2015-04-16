'use strict';

var assert = chai.assert;

describe('Form Definition Serializer Test Suite', function() {
	this.timeout(5000);

	before(function(done) {
		var instance = this;

		AUI().use(
			'aui-io-request',
			'liferay-forms-field-base',
			'liferay-forms-field-types',
			'liferay-forms-definition-serializer',
			'liferay-forms-test-base',
			function(A) {
				assert.ok(Liferay.FormsTests.Util);

				Liferay.FormsTests.Util.initFieldTypesRegistry();

				instance.expectedDefinition = Liferay.FormsTests.Util.getTestData('definition');

				assert.ok(instance.expectedDefinition);

				assert.ok(Liferay.Forms.DefinitionSerializer);

				done();
			}
		);
	});

	it('should serialize a simple DDM Layout', function(done) {
		var A = AUI(),
			instance = this;

		var textFieldType = Liferay.Forms.FieldTypes.get('text');

		var textFieldClass = textFieldType.get('fieldClass');

		var textFieldConfig = {
			indexType: 'keyword',
			label: 'Text',
			repeatable: false,
			required: false,
			showLabel: true
		};

		var layout = Liferay.FormsTests.Util.getSampleLayout();

		var serializedDefinition = A.JSON.parse(
			new Liferay.Forms.DefinitionSerializer(
				{
					layout: layout
				}
			).serialize()
		);

		assert.strictEqual(
			instance.expectedDefinition.fields.length,
			serializedDefinition.fields.length
		);

		var defaultConfig = textFieldType.get('defaultConfig');

		var settings = defaultConfig.settings;

		A.Array.each(
			instance.expectedDefinition.fields,
			function(field, index) {
				A.Array.each(
					settings,
					function(setting) {
						if (setting.localizable || setting.attrName === 'options') {
							A.each(
								field[setting.attrName],
								function(value, lang) {
									assert.strictEqual(
										value,
										serializedDefinition.fields[index][setting.attrName][lang],
										'Setting "' + setting.attrName + '" should match'
									);
								}
							);
						}
						else {
							assert.strictEqual(
								field[setting.attrName],
								serializedDefinition.fields[index][setting.attrName],
								'Setting "' + setting.attrName + '" should match'
							);
						}
					}
				);
			}
		);

		done();
	});
});