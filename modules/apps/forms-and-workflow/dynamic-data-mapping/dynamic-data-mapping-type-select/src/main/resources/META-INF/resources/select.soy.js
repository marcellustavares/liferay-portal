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
  var output = '<div class="form-group' + soy.$$escapeHtmlAttribute(opt_data.visible ? '' : ' hide') + '" data-fieldname="' + soy.$$escapeHtmlAttribute(opt_data.name) + '"><div class="input-select-wrapper">' + ((opt_data.showLabel) ? ddm.select_label(opt_data) : '') + '<div class="form-builder-select-field input-group-container">' + ((! opt_data.readOnly) ? ddm.hidden_select(opt_data) : '') + '<div class="form-control select-field-trigger" dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" id="' + soy.$$escapeHtmlAttribute(opt_data.name) + '" name="' + soy.$$escapeHtmlAttribute(opt_data.name) + '"><a class="select-arrow-down-container" href="javascript:;">' + ((opt_data.selectCaretDoubleIcon) ? soy.$$filterNoAutoescape(opt_data.selectCaretDoubleIcon) : '') + '</a>';
  if (opt_data.readOnly) {
    output += '<span class="option-selected option-selected-placeholder">' + soy.$$escapeHtml(opt_data.multiple ? opt_data.strings.chooseOptions : opt_data.strings.chooseAnOption) + '</span>';
  } else {
    if (opt_data.multiple) {
      if (opt_data.value.length == 0) {
        output += '<span class="option-selected option-selected-placeholder">' + soy.$$escapeHtml(opt_data.strings.chooseOptions) + '</span>';
      } else {
        output += '<ul class="multiple-badge-list">';
        var currentValueList48 = opt_data.value;
        var currentValueListLen48 = currentValueList48.length;
        for (var currentValueIndex48 = 0; currentValueIndex48 < currentValueListLen48; currentValueIndex48++) {
          var currentValueData48 = currentValueList48[currentValueIndex48];
          output += ddm.badge_item({badgeCloseIcon: opt_data.badgeCloseIcon, value: currentValueData48.value, label: currentValueData48.label});
        }
        output += '</ul>';
      }
    } else {
      if (opt_data.value && opt_data.value.length > 0) {
        var currentValueList59 = opt_data.value;
        var currentValueListLen59 = currentValueList59.length;
        for (var currentValueIndex59 = 0; currentValueIndex59 < currentValueListLen59; currentValueIndex59++) {
          var currentValueData59 = currentValueList59[currentValueIndex59];
          output += (currentValueData59 && currentValueData59.label) ? '<span class="option-selected">' + soy.$$escapeHtml(currentValueData59.label) + '</span>' : '';
        }
      } else {
        output += '<span class="option-selected option-selected-placeholder">' + soy.$$escapeHtml(opt_data.strings.chooseAnOption) + '</span>';
      }
    }
  }
  output += '</div>' + ((! opt_data.readOnly) ? '<div class="drop-chosen ' + soy.$$escapeHtmlAttribute(opt_data.open ? '' : 'hide') + '"><div class="search-chosen"><div class="select-search-container">' + ((opt_data.selectSearchIcon) ? '<a class="" href="javascript:;">' + soy.$$filterNoAutoescape(opt_data.selectSearchIcon) + '</a>' : '') + '</div><input autocomplete="off" class="drop-chosen-search" placeholder="Search" type="text"></div><ul class="results-chosen">' + ddm.select_options(opt_data) + '</ul></div>' : '') + '</div>' + ((opt_data.childElementsHTML) ? soy.$$filterNoAutoescape(opt_data.childElementsHTML) : '') + '</div></div>';
  return output;
};
if (goog.DEBUG) {
  ddm.select.soyTemplateName = 'ddm.select';
}


ddm.badge_item = function(opt_data, opt_ignored) {
  return '<li><span class="badge badge-default badge-sm multiple-badge" data-original-title="' + soy.$$escapeHtmlAttribute(opt_data.label) + '" title="' + soy.$$escapeHtmlAttribute(opt_data.label) + '">' + soy.$$escapeHtml(opt_data.label) + '<a class="trigger-badge-item-close" data-badge-value="' + soy.$$escapeHtmlAttribute(opt_data.value) + '" href="javascript:void(0)">' + soy.$$filterNoAutoescape(opt_data.badgeCloseIcon) + '</a></span></li>';
};
if (goog.DEBUG) {
  ddm.badge_item.soyTemplateName = 'ddm.badge_item';
}


ddm.select_label = function(opt_data, opt_ignored) {
  return '<label class="control-label" for="' + soy.$$escapeHtmlAttribute(opt_data.name) + '">' + soy.$$escapeHtml(opt_data.label) + ((opt_data.required) ? '<span class="icon-asterisk text-warning"></span>' : '') + '</label>' + ((opt_data.tip) ? '<p class="liferay-ddm-form-field-tip">' + soy.$$escapeHtml(opt_data.tip) + '</p>' : '');
};
if (goog.DEBUG) {
  ddm.select_label.soyTemplateName = 'ddm.select_label';
}


ddm.hidden_select = function(opt_data, opt_ignored) {
  var output = '<select class="form-control hide" dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" id="' + soy.$$escapeHtmlAttribute(opt_data.name) + '" name="' + soy.$$escapeHtmlAttribute(opt_data.name) + '" ' + ((opt_data.multiple) ? 'multiple size="' + soy.$$escapeHtmlAttribute(opt_data.options.length) + '"' : '') + '>' + ((! opt_data.readOnly) ? '<option dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" disabled ' + ((opt_data.value.length == 0) ? 'selected' : '') + ' value="">' + soy.$$escapeHtml(opt_data.strings.chooseAnOption) + '</option>' : '');
  var optionList142 = opt_data.options;
  var optionListLen142 = optionList142.length;
  for (var optionIndex142 = 0; optionIndex142 < optionListLen142; optionIndex142++) {
    var optionData142 = optionList142[optionIndex142];
    output += ddm.select_hidden_options({dir: opt_data.dir, option: optionData142, values: opt_data.value});
  }
  output += '</select>';
  return output;
};
if (goog.DEBUG) {
  ddm.hidden_select.soyTemplateName = 'ddm.hidden_select';
}


ddm.select_hidden_options = function(opt_data, opt_ignored) {
  var output = '';
  var selected__soy146 = '';
  if (opt_data.values) {
    var currentValueList152 = opt_data.values;
    var currentValueListLen152 = currentValueList152.length;
    for (var currentValueIndex152 = 0; currentValueIndex152 < currentValueListLen152; currentValueIndex152++) {
      var currentValueData152 = currentValueList152[currentValueIndex152];
      selected__soy146 += (currentValueData152.value == opt_data.option.value) ? 'selected' : '';
    }
  }
  output += '<option dir="' + soy.$$escapeHtmlAttribute(opt_data.dir) + '" ' + soy.$$filterHtmlAttributes(selected__soy146) + ' value="' + soy.$$escapeHtmlAttribute(opt_data.option.value) + '">' + soy.$$escapeHtml(opt_data.option.label) + '</option>';
  return output;
};
if (goog.DEBUG) {
  ddm.select_hidden_options.soyTemplateName = 'ddm.select_hidden_options';
}


ddm.select_options = function(opt_data, opt_ignored) {
  var output = '';
  if (opt_data.options.length > 0) {
    var optionList197 = opt_data.options;
    var optionListLen197 = optionList197.length;
    for (var optionIndex197 = 0; optionIndex197 < optionListLen197; optionIndex197++) {
      var optionData197 = optionList197[optionIndex197];
      var selected__soy166 = '';
      if (opt_data.value) {
        var currentValueList172 = opt_data.value;
        var currentValueListLen172 = currentValueList172.length;
        for (var currentValueIndex172 = 0; currentValueIndex172 < currentValueListLen172; currentValueIndex172++) {
          var currentValueData172 = currentValueList172[currentValueIndex172];
          selected__soy166 += (currentValueData172.value == optionData197.value) ? 'selected' : '';
        }
      }
      output += '<li class="select-option-item ' + ((selected__soy166) ? 'option-selected' : '') + '" data-option-index="' + soy.$$escapeHtmlAttribute(optionIndex197) + '" data-option-selected="' + ((selected__soy166) ? 'true' : '') + '" data-option-value="' + soy.$$escapeHtmlAttribute(optionData197.value) + '">' + ((opt_data.multiple) ? '<input type="checkbox" value="" ' + ((selected__soy166) ? 'checked' : '') + '>' : '') + '<span>' + soy.$$escapeHtml(optionData197.label) + '</span></li>';
    }
  } else {
    output += '<li class="no-results-list"><span>' + soy.$$escapeHtml(opt_data.strings.emptyList) + '</span></li>';
  }
  return output;
};
if (goog.DEBUG) {
  ddm.select_options.soyTemplateName = 'ddm.select_options';
}
