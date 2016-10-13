// This file was automatically generated from fieldset.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddm.
 * @hassoydeltemplate {ddm.field}
 * @public
 */

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.__deltemplate_s2_95ba1b96 = function(opt_data, opt_ignored) {
  return '' + ddm.fieldset(opt_data);
};
if (goog.DEBUG) {
  ddm.__deltemplate_s2_95ba1b96.soyTemplateName = 'ddm.__deltemplate_s2_95ba1b96';
}
soy.$$registerDelegateFn(soy.$$getDelTemplateId('ddm.field'), 'fieldset', 0, ddm.__deltemplate_s2_95ba1b96);


ddm.fieldset_rows = function(opt_data, opt_ignored) {
  var output = '';
  var rowList9 = opt_data.rows;
  var rowListLen9 = rowList9.length;
  for (var rowIndex9 = 0; rowIndex9 < rowListLen9; rowIndex9++) {
    var rowData9 = rowList9[rowIndex9];
    output += '<div class="row">' + ddm.fieldset_row_columns(soy.$$augmentMap(opt_data, {columns: rowData9.columns})) + '</div>';
  }
  return output;
};
if (goog.DEBUG) {
  ddm.fieldset_rows.soyTemplateName = 'ddm.fieldset_rows';
}


ddm.fieldset_row_column = function(opt_data, opt_ignored) {
  return '<div class="col-md-' + soy.$$escapeHtmlAttribute(opt_data.column.size) + '">' + ddm.fields(soy.$$augmentMap(opt_data, {fields: opt_data.column.fields})) + '</div>';
};
if (goog.DEBUG) {
  ddm.fieldset_row_column.soyTemplateName = 'ddm.fieldset_row_column';
}


ddm.fieldset_row_columns = function(opt_data, opt_ignored) {
  var output = '';
  var columnList21 = opt_data.columns;
  var columnListLen21 = columnList21.length;
  for (var columnIndex21 = 0; columnIndex21 < columnListLen21; columnIndex21++) {
    var columnData21 = columnList21[columnIndex21];
    output += ddm.fieldset_row_column(soy.$$augmentMap(opt_data, {column: columnData21}));
  }
  return output;
};
if (goog.DEBUG) {
  ddm.fieldset_row_columns.soyTemplateName = 'ddm.fieldset_row_columns';
}


ddm.fieldset = function(opt_data, opt_ignored) {
  return '<div class="form-group' + soy.$$escapeHtmlAttribute(opt_data.visible ? '' : ' hide') + ' liferay-ddm-form-field-fieldset" data-fieldname="' + soy.$$escapeHtmlAttribute(opt_data.name) + '">' + ddm.fieldset_rows(opt_data) + '</div>';
};
if (goog.DEBUG) {
  ddm.fieldset.soyTemplateName = 'ddm.fieldset';
}
