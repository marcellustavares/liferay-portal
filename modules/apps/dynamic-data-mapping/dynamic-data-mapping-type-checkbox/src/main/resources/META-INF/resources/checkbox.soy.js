// This file was automatically generated from checkbox.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.checkbox = function(opt_data, opt_ignored) {
  var output = '\t<div class="form-group form-inline' + soy.$$escapeHtml(opt_data.visible ? '' : ' hide') + ' liferay-ddm-form-field-checkbox" data-fieldname="' + soy.$$escapeHtml(opt_data.name) + '">' + ((opt_data.showLabel) ? '<label class="control-label">' + soy.$$escapeHtml(opt_data.label) + ' ' + ((opt_data.required) ? '<b>*</b>' : '') + '</label>' : '');
  var optionList17 = opt_data.options;
  var optionListLen17 = optionList17.length;
  for (var optionIndex17 = 0; optionIndex17 < optionListLen17; optionIndex17++) {
    var optionData17 = optionList17[optionIndex17];
    output += (opt_data.showAsSwitcher) ? '<div class="checkbox"><label class="control-label" for="' + soy.$$escapeHtml(opt_data.name) + '"><input class="hide toggle-switch" ' + ((opt_data.readOnly) ? 'disabled' : '') + ' id="' + soy.$$escapeHtml(optionData17.name) + '" name="' + soy.$$escapeHtml(optionData17.name) + '" type="checkbox" ' + soy.$$escapeHtml(optionData17.status) + ' value="' + soy.$$escapeHtml(optionData17.value) + '" /><span aria-hidden="true" class="toggle-switch-bar"><span class="toggle-switch-handle"><span class="toggle-switch-text">' + ddm.checkbox_option_label(opt_data) + '</span></span></span></label></div>' : '<div class="checkbox checkbox-default"><label class="checkbox-inline" for="' + soy.$$escapeHtml(optionData17.value) + '"><input ' + ((opt_data.readOnly) ? 'disabled' : '') + ' id="' + soy.$$escapeHtml(optionData17.name) + '" name="' + soy.$$escapeHtml(optionData17.name) + '" type="checkbox" ' + soy.$$escapeHtml(optionData17.status) + ' value="' + soy.$$escapeHtml(optionData17.value) + '" />' + ddm.checkbox_option_label(opt_data) + '</label></div>';
  }
  output += soy.$$filterNoAutoescape(opt_data.childElementsHTML) + '</div>';
  return output;
};


ddm.checkbox_option_label = function(opt_data, opt_ignored) {
  return '\t' + soy.$$escapeHtml(opt_data.option.label) + ((opt_data.required) ? '<b>*</b>' : '');
};
