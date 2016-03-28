// This file was automatically generated from radio.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddm.
 * @public
 */

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.radio = function(opt_data, opt_ignored) {
  var output = '<div class="form-group" data-fieldname="' + soy.$$escapeHtmlAttribute(opt_data.name) + '">' + ((opt_data.showLabel) ? '<label class="control-label">' + soy.$$escapeHtml(opt_data.label) + ((opt_data.required) ? '<span class="icon-asterisk text-warning"></span>' : '') + '</label>' + ((opt_data.tip) ? '<p class="liferay-ddm-form-field-tip">' + soy.$$escapeHtml(opt_data.tip) + '</p>' : '') : '') + '<div class="clearfix radio-options">';
  var optionList49 = opt_data.options;
  var optionListLen49 = optionList49.length;
  for (var optionIndex49 = 0; optionIndex49 < optionListLen49; optionIndex49++) {
    var optionData49 = optionList49[optionIndex49];
    output += ((! opt_data.inline) ? '<div class="radio">' : '') + '<label class="radio-default' + soy.$$escapeHtmlAttribute(opt_data.inline ? ' radio-inline' : '') + ' radio-option-' + soy.$$escapeHtmlAttribute(optionData49.value) + '" for="' + soy.$$escapeHtmlAttribute(optionData49.value) + '"><input class="field" dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" ' + ((opt_data.readOnly) ? 'disabled' : '') + ' id="' + soy.$$escapeHtmlAttribute(optionData49.value) + '" name="' + soy.$$escapeHtmlAttribute(opt_data.name) + '" ' + soy.$$filterHtmlAttributes(optionData49.status) + ' type="radio" value="' + soy.$$escapeHtmlAttribute(optionData49.value) + '" /> ' + soy.$$escapeHtml(optionData49.label) + '</label>' + ((! opt_data.inline) ? '</div>' : '');
  }
  output += '</div>' + ((opt_data.childElementsHTML) ? soy.$$filterNoAutoescape(opt_data.childElementsHTML) : '') + '</div>';
  return output;
};
if (goog.DEBUG) {
  ddm.radio.soyTemplateName = 'ddm.radio';
}
