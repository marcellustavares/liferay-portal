// This file was automatically generated from select.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.checkbox = function(opt_data, opt_ignored) {
return '\t<div class="form-group field-wrapper" data-repeatable="false" data-fieldnamespace="' + soy.$$escapeHtml(opt_data.fieldNameSuffix) + '" data-fieldname="' + soy.$$escapeHtml(opt_data.fieldName) + '"><input id="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '" class="field" type="checkbox" name="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '" ' + soy.$$escapeHtml(opt_data.fieldStatus) + '><label for="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '">' + soy.$$escapeHtml(opt_data.fieldLabel) + '</label></div>';
};


ddm.radio = function(opt_data, opt_ignored) {
var output = '\t<div class="form-group field-wrapper" data-repeatable="false" data-fieldnamespace="' + soy.$$escapeHtml(opt_data.fieldNameSuffix) + '" data-fieldname="' + soy.$$escapeHtml(opt_data.fieldName) + '"><span class="control-label">' + soy.$$escapeHtml(opt_data.fieldLabel) + '</span>';
var currentIndexLimit26 = opt_data.fieldChoicesValues.length;
for (var currentIndex26 = 0; currentIndex26 < currentIndexLimit26; currentIndex26++) {
	output += '<div class="radio"><label for="' + soy.$$escapeHtml(opt_data.fieldChoicesIds[currentIndex26]) + '"><input id="' + soy.$$escapeHtml(opt_data.fieldChoicesIds[currentIndex26]) + '" class="field" type="radio" value="' + soy.$$escapeHtml(opt_data.fieldChoicesValues[currentIndex26]) + '" name="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '" ' + soy.$$escapeHtml(opt_data.fieldChoicesStatus[currentIndex26]) + '>' + soy.$$escapeHtml(opt_data.fieldChoicesLabels[currentIndex26]) + '</label></div>';
}
output += '</div>';
return output;
};


ddm.select = function(opt_data, opt_ignored) {
var output = '\t<div class="form-group field-wrapper" data-repeatable="false" data-fieldnamespace="' + soy.$$escapeHtml(opt_data.fieldNameSuffix) + '" data-fieldname="' + soy.$$escapeHtml(opt_data.fieldName) + '"><div class="form-group input-select-wrapper"><label class="control-label" for="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '">' + soy.$$escapeHtml(opt_data.fieldLabel) + '</label><select id="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '" class="form-control" dir="' + soy.$$escapeHtml(opt_data.dir) + '" name="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '" ' + soy.$$escapeHtml(opt_data.fieldIsMultiple) + '>';
var currentIndexLimit59 = opt_data.fieldChoicesValues.length;
for (var currentIndex59 = 0; currentIndex59 < currentIndexLimit59; currentIndex59++) {
	output += '<option dir="' + soy.$$escapeHtml(opt_data.dir) + '" value="' + soy.$$escapeHtml(opt_data.fieldChoicesValues[currentIndex59]) + '" ' + soy.$$escapeHtml(opt_data.fieldChoicesStatus[currentIndex59]) + '>' + soy.$$escapeHtml(opt_data.fieldChoicesLabels[currentIndex59]) + '</option>';
}
output += '</select></div></div>';
return output;
};