// This file was automatically generated from rule_builder.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddl.
 * @public
 */

if (typeof ddl == 'undefined') { var ddl = {}; }


ddl.rule_builder = function(opt_data, opt_ignored) {
  return '<div class="form-builder-rule-builder-container"><div class="form-builder-rule-builder-rules-list"></div><div class="form-builder-rule-builder-add-rule-container"><div class="btn-action-secondary btn-bottom-right form-builder-rule-builder-add-rule-button dropdown"><button class="btn btn-primary form-builder-rule-builder-add-rule-button-icon" type="button">' + soy.$$filterNoAutoescape(opt_data.plusIcon) + '</button></div></div></div>';
};
if (goog.DEBUG) {
  ddl.rule_builder.soyTemplateName = 'ddl.rule_builder';
}


ddl.rule_list = function(opt_data, opt_ignored) {
  var output = '';
  var ruleList145 = opt_data.rules;
  var ruleListLen145 = ruleList145.length;
  if (ruleListLen145 > 0) {
    for (var ruleIndex145 = 0; ruleIndex145 < ruleListLen145; ruleIndex145++) {
      var ruleData145 = ruleList145[ruleIndex145];
      output += '<div class="card card-horizontal card-rule"><div class="card-row card-row-padded"><div class="card-col-content card-col-gutters"><h4>' + soy.$$escapeHtml(ruleData145.type) + '</h4><p>' + soy.$$escapeHtml(ruleData145.description) + '</p></div><div class="card-col-field"><div class="dropdown"><a class="dropdown-toggle icon-monospaced" data-toggle="dropdown" href="#1">' + soy.$$filterNoAutoescape(opt_data.kebab) + '</a><ul class="dropdown-menu dropdown-menu-right"><li class="rule-card-edit" data-card-id="' + soy.$$escapeHtmlAttribute(ruleIndex145) + '"><a href="javascript:;">Edit</a></li><li class="rule-card-delete" data-card-id="' + soy.$$escapeHtmlAttribute(ruleIndex145) + '"><a href="javascript:;">Delete</a></li></ul></div></div></div></div>';
    }
  } else {
    output += soy.$$escapeHtml(opt_data.strings.emptyListText);
  }
  return output;
};
if (goog.DEBUG) {
  ddl.rule_list.soyTemplateName = 'ddl.rule_list';
}


ddl.rule_types = function(opt_data, opt_ignored) {
  return '<ul class="dropdown-menu"><li><a href="javascript:;" data-rule-type="visibility">' + soy.$$escapeHtml(opt_data.strings.showHide) + '</a><a href="javascript:;" data-rule-type="readonly">' + soy.$$escapeHtml(opt_data.strings.enableDisable) + '</a><a href="javascript:;" data-rule-type="require">' + soy.$$escapeHtml(opt_data.strings.require) + '</a></li></ul>';
};
if (goog.DEBUG) {
  ddl.rule_types.soyTemplateName = 'ddl.rule_types';
}
