'use strict';

var assert = chai.assert;

describe('DDM Layout Test Suite', function() {
    this.timeout(5000);

    before(function(done) {
        var self = this;

        AUI().use('aui-io-request', 'json', 'liferay-ddm-layout', function(A) {
            var getData = function(name) {
                var response = A.io.request(
                    '/base/test/js/dynamic_data_mapping/assets/' + name + '-data.json',
                    {
                        dataType: 'json',
                        sync: true
                    }
                );

                return response.get('responseData');
            };

            Liferay.DDM.Types = [
                {"icon":"glyphicon glyphicon-font","fieldClass":"A.FormBuilderFieldText","name":"text","label":"Text"}
            ];

            assert.ok(Liferay.DDM.Layout);

            self.ddmLayout = new Liferay.DDM.Layout();
            self.definition = getData('definition');
            self.expectedLayout = getData('layout');

            assert.ok(self.ddmLayout);
            assert.ok(self.definition);
            assert.ok(self.expectedLayout);

            done();
        });
    });

    it('should serialize a simple DDM Layout', function(done) {
        var A = AUI(),
            self = this,
            ddmLayout = self.ddmLayout,
            definition = self.definition,
            expectedLayout = self.expectedLayout;

        var deserializedLayout = Liferay.DDM.Layout.Util.deserialize(expectedLayout, definition);

        ddmLayout.set('rows', deserializedLayout.rows);

        var rows = ddmLayout.get('rows');

        assert.equal(expectedLayout.rows.length, rows.length);

        assert.strictEqual(
            A.JSON.stringify(expectedLayout),
            A.JSON.stringify(ddmLayout.serialize())
        );

        done();
    });
});