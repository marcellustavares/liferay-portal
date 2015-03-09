// This file was automatically generated from radio.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.radio = function(opt_data, opt_ignored) {
  var output = '\t<div class="form-group field-wrapper" data-repeatable="false" data-fieldnamespace="' + soy.$$escapeHtml(opt_data.fieldNameSuffix) + '" data-fieldname="' + soy.$$escapeHtml(opt_data.fieldName) + '"><span class="control-label">' + soy.$$escapeHtml(opt_data.fieldLabel) + '</span>';
  var currentIndexLimit10 = opt_data.fieldChoicesValues.length;
  for (var currentIndex10 = 0; currentIndex10 < currentIndexLimit10; currentIndex10++) {
    output += '<div class="radio"><label for="' + soy.$$escapeHtml(opt_data.fieldChoicesIds[currentIndex10]) + '"><input id="' + soy.$$escapeHtml(opt_data.fieldChoicesIds[currentIndex10]) + '" class="field" type="radio" value="' + soy.$$escapeHtml(opt_data.fieldChoicesValues[currentIndex10]) + '" name="' + soy.$$escapeHtml(opt_data.fieldQualifiedName) + '" ' + soy.$$escapeHtml(opt_data.fieldChoicesStatus[currentIndex10]) + '>' + soy.$$escapeHtml(opt_data.fieldChoicesLabels[currentIndex10]) + '</label></div>';
  }
  output += '</div>';
  return output;
};
