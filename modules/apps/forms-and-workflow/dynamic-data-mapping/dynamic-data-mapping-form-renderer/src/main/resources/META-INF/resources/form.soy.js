// This file was automatically generated from form.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddm.
 * @public
 */

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.field = function(opt_data, opt_ignored) {
  return '' + ((opt_data.field != null) ? soy.$$filterNoAutoescape(opt_data.field) : '');
};
if (goog.DEBUG) {
  ddm.field.soyTemplateName = 'ddm.field';
}


ddm.fields = function(opt_data, opt_ignored) {
  var output = '';
  var fieldList10 = opt_data.fields;
  var fieldListLen10 = fieldList10.length;
  for (var fieldIndex10 = 0; fieldIndex10 < fieldListLen10; fieldIndex10++) {
    var fieldData10 = fieldList10[fieldIndex10];
    output += ddm.field(soy.$$augmentMap(opt_data, {field: fieldData10}));
  }
  return output;
};
if (goog.DEBUG) {
  ddm.fields.soyTemplateName = 'ddm.fields';
}


ddm.form_renderer_js = function(opt_data, opt_ignored) {
  return '<script type="text/javascript">console.log(' + soy.$$escapeJsValue(opt_data.containerId) + '); console.log(' + soy.$$escapeJsValue(opt_data.evaluatorURL) + '); console.log(' + soy.$$escapeJsValue(opt_data.pages) + '); console.log(' + soy.$$escapeJsValue(opt_data.portletNamespace) + '); console.log(' + soy.$$escapeJsValue(opt_data.readOnly) + '); console.log(' + soy.$$escapeJsValue(opt_data.requiredFieldsWarningMessageHTML) + '); console.log(' + soy.$$escapeJsValue(opt_data.showRequiredFieldsWarning) + '); console.log(' + soy.$$escapeJsValue(opt_data.showSubmitButton) + '); console.log(' + soy.$$escapeJsValue(opt_data.strings) + '); console.log(' + soy.$$escapeJsValue(opt_data.submitLabel) + '); console.log(' + soy.$$escapeJsValue(opt_data.templateNamespace) + ');<\/script>';
};
if (goog.DEBUG) {
  ddm.form_renderer_js.soyTemplateName = 'ddm.form_renderer_js';
}


ddm.form_rows = function(opt_data, opt_ignored) {
  var output = '';
  var rowList41 = opt_data.rows;
  var rowListLen41 = rowList41.length;
  for (var rowIndex41 = 0; rowIndex41 < rowListLen41; rowIndex41++) {
    var rowData41 = rowList41[rowIndex41];
    output += '<div class="row">' + ddm.form_row_columns(soy.$$augmentMap(opt_data, {columns: rowData41.columns})) + '</div>';
  }
  return output;
};
if (goog.DEBUG) {
  ddm.form_rows.soyTemplateName = 'ddm.form_rows';
}


ddm.form_row_column = function(opt_data, opt_ignored) {
  return '<div class="col-md-' + soy.$$escapeHtmlAttribute(opt_data.column.size) + '">' + ddm.fields(soy.$$augmentMap(opt_data, {fields: opt_data.column.fields})) + '</div>';
};
if (goog.DEBUG) {
  ddm.form_row_column.soyTemplateName = 'ddm.form_row_column';
}


ddm.form_row_columns = function(opt_data, opt_ignored) {
  var output = '';
  var columnList53 = opt_data.columns;
  var columnListLen53 = columnList53.length;
  for (var columnIndex53 = 0; columnIndex53 < columnListLen53; columnIndex53++) {
    var columnData53 = columnList53[columnIndex53];
    output += ddm.form_row_column(soy.$$augmentMap(opt_data, {column: columnData53}));
  }
  return output;
};
if (goog.DEBUG) {
  ddm.form_row_columns.soyTemplateName = 'ddm.form_row_columns';
}


ddm.required_warning_message = function(opt_data, opt_ignored) {
  return '' + ((opt_data.showRequiredFieldsWarning) ? soy.$$filterNoAutoescape(opt_data.requiredFieldsWarningMessageHTML) : '');
};
if (goog.DEBUG) {
  ddm.required_warning_message.soyTemplateName = 'ddm.required_warning_message';
}


ddm.paginated_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-content">';
  if (opt_data.pages.length > 1) {
    output += '<div class="lfr-ddm-form-wizard"><ul class="multi-step-progress-bar">';
    var pageList77 = opt_data.pages;
    var pageListLen77 = pageList77.length;
    for (var pageIndex77 = 0; pageIndex77 < pageListLen77; pageIndex77++) {
      var pageData77 = pageList77[pageIndex77];
      output += '<li ' + ((pageIndex77 == 0) ? 'class="active"' : '') + '><div class="progress-bar-title">' + soy.$$filterNoAutoescape(pageData77.title) + '</div><div class="divider"></div><div class="progress-bar-step">' + soy.$$escapeHtml(pageIndex77 + 1) + '</div></li>';
    }
    output += '</ul></div>';
  }
  output += '<div class="lfr-ddm-form-pages" autoescape="deprecated-contextual">';
  var pageList104 = opt_data.pages;
  var pageListLen104 = pageList104.length;
  for (var pageIndex104 = 0; pageIndex104 < pageListLen104; pageIndex104++) {
    var pageData104 = pageList104[pageIndex104];
    output += '<div class="' + ((pageIndex104 == 0) ? 'active' : '') + ' lfr-ddm-form-page">' + ((pageData104.title) ? '<h3 class="lfr-ddm-form-page-title">' + soy.$$filterNoAutoescape(pageData104.title) + '</h3>' : '') + ((pageData104.description) ? '<h4 class="lfr-ddm-form-page-description">' + soy.$$filterNoAutoescape(pageData104.description) + '</h4>' : '') + ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData104.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData104.rows})) + '</div>';
  }
  output += '</div></div><div class="lfr-ddm-form-pagination-controls"><button class="btn btn-lg btn-primary hide lfr-ddm-form-pagination-prev" type="button"><i class="icon-angle-left"></i> ' + soy.$$escapeHtml(opt_data.strings.previous) + '</button><button class="btn btn-lg btn-primary' + ((opt_data.pages.length == 1) ? ' hide' : '') + ' lfr-ddm-form-pagination-next pull-right" type="button">' + soy.$$escapeHtml(opt_data.strings.next) + ' <i class="icon-angle-right"></i></button>' + ((opt_data.showSubmitButton) ? '<button class="btn btn-lg btn-primary' + ((opt_data.pages.length > 1) ? ' hide' : '') + ' lfr-ddm-form-submit pull-right" disabled type="submit">' + soy.$$escapeHtml(opt_data.submitLabel) + '</button>' : '') + '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.paginated_form.soyTemplateName = 'ddm.paginated_form';
}


ddm.simple_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-fields">';
  var pageList134 = opt_data.pages;
  var pageListLen134 = pageList134.length;
  for (var pageIndex134 = 0; pageIndex134 < pageListLen134; pageIndex134++) {
    var pageData134 = pageList134[pageIndex134];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData134.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData134.rows}));
  }
  output += '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.simple_form.soyTemplateName = 'ddm.simple_form';
}


ddm.tabbed_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-tabs"><ul class="nav nav-tabs nav-tabs-default">';
  var pageList144 = opt_data.pages;
  var pageListLen144 = pageList144.length;
  for (var pageIndex144 = 0; pageIndex144 < pageListLen144; pageIndex144++) {
    var pageData144 = pageList144[pageIndex144];
    output += '<li><a href="javascript:;">' + soy.$$escapeHtml(pageData144.title) + '</a></li>';
  }
  output += '</ul><div class="tab-content">';
  var pageList158 = opt_data.pages;
  var pageListLen158 = pageList158.length;
  for (var pageIndex158 = 0; pageIndex158 < pageListLen158; pageIndex158++) {
    var pageData158 = pageList158[pageIndex158];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData158.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + '<div class="tab-pane ' + ((pageIndex158 == 0) ? 'active' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData158.rows})) + '</div>';
  }
  output += '</div></div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.tabbed_form.soyTemplateName = 'ddm.tabbed_form';
}


ddm.settings_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-settings-form">';
  var pageList176 = opt_data.pages;
  var pageListLen176 = pageList176.length;
  for (var pageIndex176 = 0; pageIndex176 < pageListLen176; pageIndex176++) {
    var pageData176 = pageList176[pageIndex176];
    output += '<div class="lfr-ddm-form-page' + ((pageIndex176 == 0) ? ' active basic' : '') + ((pageIndex176 == pageListLen176 - 1) ? ' advanced' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData176.rows})) + '</div>';
  }
  output += '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.settings_form.soyTemplateName = 'ddm.settings_form';
}
