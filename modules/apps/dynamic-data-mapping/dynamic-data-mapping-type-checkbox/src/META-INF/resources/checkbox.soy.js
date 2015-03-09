// This file was automatically generated from checkbox.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.checkbox = function(opt_data, opt_ignored) {
return '\t<div class="form-group field-wrapper" data-repeatable="false" data-fieldnamespace="' + soy.$$escapeHtml(opt_data.fieldNameSuffix) + '" data-fieldname="' + soy.$$escapeHtml(opt_data.fieldName) + '"><input id="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '" class="field" type="checkbox" name="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '" ' + soy.$$escapeHtml(opt_data.fieldStatus) + '><label for="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '">' + soy.$$escapeHtml(opt_data.fieldLabel) + '</label></div>';
};