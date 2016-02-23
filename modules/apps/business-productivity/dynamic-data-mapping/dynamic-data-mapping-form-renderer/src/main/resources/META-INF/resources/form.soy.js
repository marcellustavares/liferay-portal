// This file was automatically generated from form.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.field = function(opt_data, opt_ignored) {
  return '\t' + ((opt_data.field != null) ? soy.$$filterNoAutoescape(opt_data.field) : '');
};


ddm.fields = function(opt_data, opt_ignored) {
  var output = '\t';
  var fieldList10 = opt_data.fields;
  var fieldListLen10 = fieldList10.length;
  for (var fieldIndex10 = 0; fieldIndex10 < fieldListLen10; fieldIndex10++) {
    var fieldData10 = fieldList10[fieldIndex10];
    output += ddm.field(soy.$$augmentMap(opt_data, {field: fieldData10}));
  }
  return output;
};


ddm.form_renderer_js = function(opt_data, opt_ignored) {
  return '\t<link href="/o/dynamic-data-mapping-form-renderer/css/main.css" rel="stylesheet" type="text/css" /><script type="text/javascript">AUI().use( \'liferay-ddm-form-renderer\', \'liferay-ddm-form-renderer-field\', function(A) {Liferay.DDM.Renderer.FieldTypes.register(' + soy.$$filterNoAutoescape(opt_data.fieldTypes) + '); Liferay.component( \'' + soy.$$escapeHtml(opt_data.containerId) + 'DDMForm\', new Liferay.DDM.Renderer.Form({container: \'#' + soy.$$escapeHtml(opt_data.containerId) + '\', definition: ' + soy.$$filterNoAutoescape(opt_data.definition) + ', evaluation: ' + soy.$$filterNoAutoescape(opt_data.evaluation) + ',' + ((opt_data.layout) ? 'layout: ' + soy.$$filterNoAutoescape(opt_data.layout) + ',' : '') + 'portletNamespace: \'' + soy.$$escapeHtml(opt_data.portletNamespace) + '\', readOnly: ' + soy.$$escapeHtml(opt_data.readOnly) + ', readOnlyFields : ' + soy.$$filterNoAutoescape(opt_data.readOnlyFields) + ', showRequiredFieldsWarning: ' + soy.$$escapeHtml(opt_data.showRequiredFieldsWarning) + ', submitLabel: \'' + soy.$$escapeHtml(opt_data.submitLabel) + '\', templateNamespace: \'' + soy.$$escapeHtml(opt_data.templateNamespace) + '\', values: ' + soy.$$filterNoAutoescape(opt_data.values) + '}).render() );});<\/script>';
};


ddm.form_rows = function(opt_data, opt_ignored) {
  var output = '\t';
  var rowList54 = opt_data.rows;
  var rowListLen54 = rowList54.length;
  for (var rowIndex54 = 0; rowIndex54 < rowListLen54; rowIndex54++) {
    var rowData54 = rowList54[rowIndex54];
    output += '<div class="row">' + ddm.form_row_columns(soy.$$augmentMap(opt_data, {columns: rowData54.columns})) + '</div>';
  }
  return output;
};


ddm.form_row_column = function(opt_data, opt_ignored) {
  return '\t<div class="col-md-' + soy.$$escapeHtml(opt_data.column.size) + '">' + ddm.fields(soy.$$augmentMap(opt_data, {fields: opt_data.column.fields})) + '</div>';
};


ddm.form_row_columns = function(opt_data, opt_ignored) {
  var output = '\t';
  var columnList69 = opt_data.columns;
  var columnListLen69 = columnList69.length;
  for (var columnIndex69 = 0; columnIndex69 < columnListLen69; columnIndex69++) {
    var columnData69 = columnList69[columnIndex69];
    output += ddm.form_row_column(soy.$$augmentMap(opt_data, {column: columnData69}));
  }
  return output;
};


ddm.required_warning_message = function(opt_data, opt_ignored) {
  return '\t' + ((opt_data.showRequiredFieldsWarning) ? soy.$$filterNoAutoescape(opt_data.requiredFieldsWarningMessageHTML) : '');
};


ddm.paginated_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-form-content">';
  if (opt_data.pages.length > 1) {
    output += '<div class="lfr-ddm-form-wizard"><ul class="multi-step-progress-bar">';
    var pageList86 = opt_data.pages;
    var pageListLen86 = pageList86.length;
    for (var pageIndex86 = 0; pageIndex86 < pageListLen86; pageIndex86++) {
      var pageData86 = pageList86[pageIndex86];
      output += '<li ' + ((pageIndex86 == 0) ? 'class="active"' : '') + '><div class="progress-bar-title">' + soy.$$filterNoAutoescape(pageData86.title) + '</div><div class="divider"></div><div class="progress-bar-step">' + soy.$$escapeHtml(pageIndex86 + 1) + '</div></li>';
    }
    output += '</ul></div>';
  }
  output += '<div class="lfr-ddm-form-pages">';
  var pageList100 = opt_data.pages;
  var pageListLen100 = pageList100.length;
  for (var pageIndex100 = 0; pageIndex100 < pageListLen100; pageIndex100++) {
    var pageData100 = pageList100[pageIndex100];
    output += '<div class="' + ((pageIndex100 == 0) ? 'active' : '') + ' lfr-ddm-form-page">' + ((pageData100.title) ? '<h3 class="lfr-ddm-form-page-title">' + soy.$$filterNoAutoescape(pageData100.title) + '</h3>' : '') + ((pageData100.description) ? '<h4 class="lfr-ddm-form-page-description">' + soy.$$filterNoAutoescape(pageData100.description) + '</h4>' : '') + ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData100.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData100.rows})) + '</div>';
  }
  output += '</div></div><div class="lfr-ddm-form-pagination-controls"><button class="btn btn-lg btn-primary hide lfr-ddm-form-pagination-prev" type="button"><i class="icon-angle-left"></i> ' + soy.$$escapeHtml(opt_data.strings.previous) + '</button><button class="btn btn-lg btn-primary' + ((opt_data.pages.length == 1) ? ' hide' : '') + ' lfr-ddm-form-pagination-next pull-right" type="button">' + soy.$$escapeHtml(opt_data.strings.next) + ' <i class="icon-angle-right"></i></button>' + ((! opt_data.readOnly) ? '<button class="btn btn-lg btn-primary' + ((opt_data.pages.length > 1) ? ' hide' : '') + ' lfr-ddm-form-submit pull-right" disabled type="submit">' + soy.$$escapeHtml(opt_data.submitLabel) + '</button>' : '') + '</div></div>';
  return output;
};


ddm.simple_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-form-fields">';
  var pageList148 = opt_data.pages;
  var pageListLen148 = pageList148.length;
  for (var pageIndex148 = 0; pageIndex148 < pageListLen148; pageIndex148++) {
    var pageData148 = pageList148[pageIndex148];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData148.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData148.rows}));
  }
  output += '</div></div>';
  return output;
};


ddm.tabbed_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-form-tabs"><ul class="nav nav-tabs nav-tabs-default">';
  var pageList160 = opt_data.pages;
  var pageListLen160 = pageList160.length;
  for (var pageIndex160 = 0; pageIndex160 < pageListLen160; pageIndex160++) {
    var pageData160 = pageList160[pageIndex160];
    output += '<li><a href="javascript:;">' + soy.$$escapeHtml(pageData160.title) + '</a></li>';
  }
  output += '</ul><div class="tab-content">';
  var pageList166 = opt_data.pages;
  var pageListLen166 = pageList166.length;
  for (var pageIndex166 = 0; pageIndex166 < pageListLen166; pageIndex166++) {
    var pageData166 = pageList166[pageIndex166];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData166.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + '<div class="tab-pane ' + ((pageIndex166 == 0) ? 'active' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData166.rows})) + '</div>';
  }
  output += '</div></div></div>';
  return output;
};


ddm.settings_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-settings-form">';
  var pageList184 = opt_data.pages;
  var pageListLen184 = pageList184.length;
  for (var pageIndex184 = 0; pageIndex184 < pageListLen184; pageIndex184++) {
    var pageData184 = pageList184[pageIndex184];
    output += '<div class="lfr-ddm-form-page' + ((pageIndex184 == 0) ? ' active basic' : '') + ((pageIndex184 == pageListLen184 - 1) ? ' advanced' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData184.rows})) + '</div>';
  }
  output += '</div></div>';
  return output;
};
