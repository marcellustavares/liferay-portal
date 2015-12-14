// This file was automatically generated from checkbox.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.checkbox = function(opt_data, opt_ignored) {
var output = '\t<div class="form-group form-inline' + soy.$$escapeHtml(opt_data.visible ? '' : ' hide') + ' liferay-ddm-form-field-checkbox" data-fieldname="' + soy.$$escapeHtml(opt_data.name) + '">';
if (opt_data.showAsSwitcher) {
	output += '<div class="checkbox"><label class="control-label" for="' + soy.$$escapeHtml(opt_data.name) + '"><input class="hide toggle-switch" ' + ((opt_data.readOnly) ? 'disabled' : '') + ' id="' + soy.$$escapeHtml(opt_data.name) + '" name="' + soy.$$escapeHtml(opt_data.name) + '" type="checkbox" ' + soy.$$escapeHtml(opt_data.status) + ' value="true" /><span aria-hidden="true" class="toggle-switch-bar"><span class="toggle-switch-handle"><span class="toggle-switch-text">' + soy.$$escapeHtml(opt_data.label) + ((opt_data.required) ? '<b>*</b>' : '') + '</span></span></span></label></div>';
} else {
	output += '<div class="checkbox checkbox-default">' + ((opt_data.showLabel) ? '<div>' + soy.$$escapeHtml(opt_data.label) + ' ' + ((opt_data.required) ? '<b>*</b>' : '') + '</div>' : '');
	var optionList39 = opt_data.options;
	var optionListLen39 = optionList39.length;
	for (var optionIndex39 = 0; optionIndex39 < optionListLen39; optionIndex39++) {
	  var optionData39 = optionList39[optionIndex39];
	  output += '<label class="checkbox-inline" for="' + soy.$$escapeHtml(optionData39.value) + '"><input ' + ((opt_data.readOnly) ? 'disabled' : '') + ' id="' + soy.$$escapeHtml(optionData39.value) + '" name="' + soy.$$escapeHtml(optionData39.value) + '" type="checkbox" ' + soy.$$escapeHtml(optionData39.status) + ' value="true" />' + soy.$$escapeHtml(optionData39.label) + '</label>';
	}
	output += '</div>';
}
output += soy.$$filterNoAutoescape(opt_data.childElementsHTML) + '</div>';
return output;
};