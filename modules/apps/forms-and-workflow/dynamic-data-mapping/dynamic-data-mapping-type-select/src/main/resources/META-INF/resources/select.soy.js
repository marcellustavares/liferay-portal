// This file was automatically generated from select.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddm.
 * @hassoydeltemplate {ddm.field}
 * @public
 */

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.__deltemplate_s2_2dbfb377 = function(opt_data, opt_ignored) {
  return '' + ddm.select(opt_data);
};
if (goog.DEBUG) {
  ddm.__deltemplate_s2_2dbfb377.soyTemplateName = 'ddm.__deltemplate_s2_2dbfb377';
}
soy.$$registerDelegateFn(soy.$$getDelTemplateId('ddm.field'), 'select', 0, ddm.__deltemplate_s2_2dbfb377);


ddm.select = function(opt_data, opt_ignored) {
  var output = '<div class="form-group' + soy.$$escapeHtmlAttribute(opt_data.visible ? '' : ' hide') + '" data-fieldname="' + soy.$$escapeHtmlAttribute(opt_data.name) + '"><div class="input-select-wrapper">' + ((opt_data.showLabel) ? '<label class="control-label" for="' + soy.$$escapeHtmlAttribute(opt_data.name) + '">' + soy.$$escapeHtml(opt_data.label) + ((opt_data.required) ? '<span class="icon-asterisk text-warning"></span>' : '') + '</label>' + ((opt_data.tip) ? '<p class="liferay-ddm-form-field-tip">' + soy.$$escapeHtml(opt_data.tip) + '</p>' : '') : '') + '<div class="input-group-container"><select class="form-control" dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" ' + ((opt_data.readOnly) ? 'disabled' : '') + ' id="' + soy.$$escapeHtmlAttribute(opt_data.name) + '" name="' + soy.$$escapeHtmlAttribute(opt_data.name) + '" ' + soy.$$filterHtmlAttributes(opt_data.multiple) + ' ' + ((opt_data.multiple == 'multiple') ? 'size="' + soy.$$escapeHtmlAttribute(opt_data.options.length) + '"' : '') + '>' + ((! opt_data.readOnly) ? '<option dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" disabled ' + ((! opt_data.value) ? 'selected' : '') + ' value="">' + soy.$$escapeHtml(opt_data.strings.chooseAnOption) + '</option>' : '');
  var optionList64 = opt_data.options;
  var optionListLen64 = optionList64.length;
  for (var optionIndex64 = 0; optionIndex64 < optionListLen64; optionIndex64++) {
    var optionData64 = optionList64[optionIndex64];
    output += '<option dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" ' + soy.$$filterHtmlAttributes(optionData64.status) + ' value="' + soy.$$escapeHtmlAttribute(optionData64.value) + '">' + soy.$$escapeHtml(optionData64.label) + '</option>';
  }
  output += '</select></div>' + ((opt_data.childElementsHTML) ? soy.$$filterNoAutoescape(opt_data.childElementsHTML) : '') + '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.select.soyTemplateName = 'ddm.select';
}
