// This file was automatically generated from datasource.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.datasource = function(opt_data, opt_ignored) {
  return '\t<div class="form-group' + soy.$$escapeHtml(opt_data.visible ? '' : ' hide') + '" data-fieldname="' + soy.$$escapeHtml(opt_data.name) + '"><label class="control-label">' + soy.$$escapeHtml(opt_data.label) + ((opt_data.required) ? '<b>*</b>' : '') + '</label><input name="' + soy.$$escapeHtml(opt_data.name) + '" type="hidden" /><div class="datasource-container"><div class="form-group"><select class="datasource-select form-control"></select></div><div class="datasource-form"></div></div><span class="hide icon-refresh icon-spin"></span></div>';
};
