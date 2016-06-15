// This file was automatically generated from form.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddm.
 * @hassoydeltemplate {ddm.field}
 * @hassoydelcall {ddm.field}
 * @public
 */

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.__deltemplate_s2_86478705 = function(opt_data, opt_ignored) {
  return '';
};
if (goog.DEBUG) {
  ddm.__deltemplate_s2_86478705.soyTemplateName = 'ddm.__deltemplate_s2_86478705';
}
soy.$$registerDelegateFn(soy.$$getDelTemplateId('ddm.field'), '', 0, ddm.__deltemplate_s2_86478705);


ddm.fields = function(opt_data, opt_ignored) {
  var output = '';
  var fieldList8 = opt_data.fields;
  var fieldListLen8 = fieldList8.length;
  for (var fieldIndex8 = 0; fieldIndex8 < fieldListLen8; fieldIndex8++) {
    var fieldData8 = fieldList8[fieldIndex8];
    var variant__soy4 = fieldData8.type;
    output += '<div class="clearfix lfr-ddm-form-field-container">' + soy.$$getDelegateFn(soy.$$getDelTemplateId('ddm.field'), variant__soy4, true)(fieldData8) + '</div>';
  }
  return output;
};
if (goog.DEBUG) {
  ddm.fields.soyTemplateName = 'ddm.fields';
}


ddm.form_renderer_js = function(opt_data, opt_ignored) {
  return '<script type="text/javascript">AUI().use( \'liferay-ddm-form-renderer\', \'liferay-ddm-form-renderer-field\', function(A) {Liferay.DDM.Renderer.FieldTypes.register(' + soy.$$filterNoAutoescape(opt_data.fieldTypes) + '); Liferay.component( \'' + soy.$$escapeJsString(opt_data.containerId) + 'DDMForm\', new Liferay.DDM.Renderer.Form({container: \'#' + soy.$$escapeJsString(opt_data.containerId) + '\', context: ' + soy.$$filterNoAutoescape(opt_data.context) + ', evaluatorURL: \'' + soy.$$escapeJsString(opt_data.evaluatorURL) + '\', portletNamespace: \'' + soy.$$escapeJsString(opt_data.portletNamespace) + '\', recordSetId: ' + soy.$$escapeJsValue(opt_data.recordSetId) + '}).render() );});<\/script>';
};
if (goog.DEBUG) {
  ddm.form_renderer_js.soyTemplateName = 'ddm.form_renderer_js';
}


ddm.form_rows = function(opt_data, opt_ignored) {
  var output = '';
  var rowList33 = opt_data.rows;
  var rowListLen33 = rowList33.length;
  for (var rowIndex33 = 0; rowIndex33 < rowListLen33; rowIndex33++) {
    var rowData33 = rowList33[rowIndex33];
    output += '<div class="row">' + ddm.form_row_columns(soy.$$augmentMap(opt_data, {columns: rowData33.columns})) + '</div>';
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
  var columnList45 = opt_data.columns;
  var columnListLen45 = columnList45.length;
  for (var columnIndex45 = 0; columnIndex45 < columnListLen45; columnIndex45++) {
    var columnData45 = columnList45[columnIndex45];
    output += ddm.form_row_column(soy.$$augmentMap(opt_data, {column: columnData45}));
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
    var pageList69 = opt_data.pages;
    var pageListLen69 = pageList69.length;
    for (var pageIndex69 = 0; pageIndex69 < pageListLen69; pageIndex69++) {
      var pageData69 = pageList69[pageIndex69];
      output += '<li ' + ((pageIndex69 == 0) ? 'class="active"' : '') + '><div class="progress-bar-title">' + soy.$$filterNoAutoescape(pageData69.title) + '</div><div class="divider"></div><div class="progress-bar-step">' + soy.$$escapeHtml(pageIndex69 + 1) + '</div></li>';
    }
    output += '</ul></div>';
  }
  output += '<div class="lfr-ddm-form-pages">';
  var pageList96 = opt_data.pages;
  var pageListLen96 = pageList96.length;
  for (var pageIndex96 = 0; pageIndex96 < pageListLen96; pageIndex96++) {
    var pageData96 = pageList96[pageIndex96];
    output += '<div class="' + ((pageIndex96 == 0) ? 'active' : '') + ' lfr-ddm-form-page">' + ((pageData96.title) ? '<h3 class="lfr-ddm-form-page-title">' + soy.$$filterNoAutoescape(pageData96.title) + '</h3>' : '') + ((pageData96.description) ? '<h4 class="lfr-ddm-form-page-description">' + soy.$$filterNoAutoescape(pageData96.description) + '</h4>' : '') + ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData96.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData96.rows})) + '</div>';
  }
  output += '</div></div><div class="lfr-ddm-form-pagination-controls"><button class="btn btn-lg btn-primary hide lfr-ddm-form-pagination-prev" type="button"><i class="icon-angle-left"></i> ' + soy.$$escapeHtml(opt_data.strings.previous) + '</button><button class="btn btn-lg btn-primary' + ((opt_data.pages.length == 1) ? ' hide' : '') + ' lfr-ddm-form-pagination-next pull-right" type="button">' + soy.$$escapeHtml(opt_data.strings.next) + ' <i class="icon-angle-right"></i></button>' + ((opt_data.showSubmitButton) ? '<button class="btn btn-lg btn-primary' + ((opt_data.pages.length > 1) ? ' hide' : '') + ' lfr-ddm-form-submit pull-right" disabled type="submit">' + soy.$$escapeHtml(opt_data.submitLabel) + '</button>' : '') + '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.paginated_form.soyTemplateName = 'ddm.paginated_form';
}


ddm.simple_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-fields">';
  var pageList126 = opt_data.pages;
  var pageListLen126 = pageList126.length;
  for (var pageIndex126 = 0; pageIndex126 < pageListLen126; pageIndex126++) {
    var pageData126 = pageList126[pageIndex126];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData126.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData126.rows}));
  }
  output += '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.simple_form.soyTemplateName = 'ddm.simple_form';
}


ddm.tabbed_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-form-tabs"><ul class="nav nav-tabs nav-tabs-default">';
  var pageList136 = opt_data.pages;
  var pageListLen136 = pageList136.length;
  for (var pageIndex136 = 0; pageIndex136 < pageListLen136; pageIndex136++) {
    var pageData136 = pageList136[pageIndex136];
    output += '<li><a href="javascript:;">' + soy.$$escapeHtml(pageData136.title) + '</a></li>';
  }
  output += '</ul><div class="tab-content">';
  var pageList150 = opt_data.pages;
  var pageListLen150 = pageList150.length;
  for (var pageIndex150 = 0; pageIndex150 < pageListLen150; pageIndex150++) {
    var pageData150 = pageList150[pageIndex150];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData150.showRequiredFieldsWarning, requiredFieldsWarningMessageHTML: opt_data.requiredFieldsWarningMessageHTML})) + '<div class="tab-pane ' + ((pageIndex150 == 0) ? 'active' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData150.rows})) + '</div>';
  }
  output += '</div></div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.tabbed_form.soyTemplateName = 'ddm.tabbed_form';
}


ddm.settings_form = function(opt_data, opt_ignored) {
  var output = '<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtmlAttribute(opt_data.containerId) + '"><div class="lfr-ddm-settings-form">';
  var pageList168 = opt_data.pages;
  var pageListLen168 = pageList168.length;
  for (var pageIndex168 = 0; pageIndex168 < pageListLen168; pageIndex168++) {
    var pageData168 = pageList168[pageIndex168];
    output += '<div class="lfr-ddm-form-page' + ((pageIndex168 == 0) ? ' active basic' : '') + ((pageIndex168 == pageListLen168 - 1) ? ' advanced' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData168.rows})) + '</div>';
  }
  output += '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.settings_form.soyTemplateName = 'ddm.settings_form';
}
