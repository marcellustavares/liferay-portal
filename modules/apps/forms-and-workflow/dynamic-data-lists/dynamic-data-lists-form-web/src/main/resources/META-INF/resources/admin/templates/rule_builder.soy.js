// This file was automatically generated from rule_builder.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ddl.
 * @public
 */

if (typeof ddl == 'undefined') { var ddl = {}; }


ddl.rule_builder = function(opt_data, opt_ignored) {
  return '<div class="form-builder-rule-builder-container"><h1 class="form-builder-section-title text-default">Rules Builder</h1><div class="liferay-ddl-form-rule-rules-list-container"></div><div class="form-builder-rule-builder-add-rule-container"><div class="btn-action-secondary btn-bottom-right dropdown form-builder-rule-builder-add-rule-button"><button class="btn btn-primary form-builder-rule-builder-add-rule-button-icon" type="button">' + soy.$$filterNoAutoescape(opt_data.plusIcon) + '</button></div></div></div>';
};
if (goog.DEBUG) {
  ddl.rule_builder.soyTemplateName = 'ddl.rule_builder';
}


ddl.rule_list = function(opt_data, opt_ignored) {
  var output = '';
  if (opt_data.rules.length > 0) {
    output += '<ul class="ddl-form-body-content form-builder-rule-builder-rules-list tabular-list-group">';
    var ruleList194 = opt_data.rules;
    var ruleListLen194 = ruleList194.length;
    for (var ruleIndex194 = 0; ruleIndex194 < ruleListLen194; ruleIndex194++) {
      var ruleData194 = ruleList194[ruleIndex194];
      output += '<li class="list-group-item"><div class="clamp-horizontal list-group-item-content"><p class="text-default form-builder-rule-builder-rule-description"><b>If </b>';
      var conditionList172 = ruleData194.conditions;
      var conditionListLen172 = conditionList172.length;
      for (var conditionIndex172 = 0; conditionIndex172 < conditionListLen172; conditionIndex172++) {
        var conditionData172 = conditionList172[conditionIndex172];
        output += ddl.condition({operandType: conditionData172.operands[0].type, operandValue: conditionData172.operands[0].label}) + '<b class="text-lowercase"><em> ' + soy.$$escapeHtml(opt_data.strings[conditionData172.operator]) + ' </em></b>' + ddl.condition({operandType: conditionData172.operands[1].type, operandValue: conditionData172.operands[1].label}) + ((! (conditionIndex172 == conditionListLen172 - 1)) ? ', <br /><b> and </b>' : '');
      }
      output += '<br />';
      var actionList180 = ruleData194.actions;
      var actionListLen180 = actionList180.length;
      for (var actionIndex180 = 0; actionIndex180 < actionListLen180; actionIndex180++) {
        var actionData180 = actionList180[actionIndex180];
        output += ddl.action({action: actionData180}) + ((! (actionIndex180 == actionListLen180 - 1)) ? ', <br /><b> and </b>' : '');
      }
      output += '</p></div><div class="list-group-item-field"><div class="card-col-field"><div class="dropdown"><a class="dropdown-toggle icon-monospaced" data-toggle="dropdown" href="#1">' + soy.$$filterNoAutoescape(opt_data.kebab) + '</a><ul class="dropdown-menu dropdown-menu-right"><li class="rule-card-edit" data-card-id="' + soy.$$escapeHtmlAttribute(ruleIndex194) + '"><a href="javascript:;">' + soy.$$escapeHtml(opt_data.strings.edit) + '</a></li><li class="rule-card-delete" data-card-id="' + soy.$$escapeHtmlAttribute(ruleIndex194) + '"><a href="javascript:;">' + soy.$$escapeHtml(opt_data.strings['delete']) + '</a></li></ul></div></div></div></li>';
    }
    output += '</ul>';
  } else {
    output += ddl.empty_list({message: opt_data.strings.emptyListText});
  }
  return output;
};
if (goog.DEBUG) {
  ddl.rule_list.soyTemplateName = 'ddl.rule_list';
}


ddl.empty_list = function(opt_data, opt_ignored) {
  opt_data = opt_data || {};
  return '<div class="main-content-body"><div class="card main-content-card taglib-empty-result-message"><div class="card-row card-row-padded"><div class="taglib-empty-result-message-header-has-plus-btn"></div>' + ((opt_data.message) ? '<div class="text-center text-muted"><p class="text-default">' + soy.$$escapeHtml(opt_data.message) + '</p></div>' : '') + '</div></div></div>';
};
if (goog.DEBUG) {
  ddl.empty_list.soyTemplateName = 'ddl.empty_list';
}


ddl.rule_types = function(opt_data, opt_ignored) {
  return '<ul class="dropdown-menu"><li><a href="javascript:;" data-rule-type="visibility">' + soy.$$escapeHtml(opt_data.strings.showHide) + '</a><a href="javascript:;" data-rule-type="readonly">' + soy.$$escapeHtml(opt_data.strings.enableDisable) + '</a><a href="javascript:;" data-rule-type="require">' + soy.$$escapeHtml(opt_data.strings.require) + '</a></li></ul>';
};
if (goog.DEBUG) {
  ddl.rule_types.soyTemplateName = 'ddl.rule_types';
}


ddl.badge = function(opt_data, opt_ignored) {
  opt_data = opt_data || {};
  return '<span class="badge badge-default badge-sm">' + soy.$$escapeHtml(opt_data.content) + '</span>';
};
if (goog.DEBUG) {
  ddl.badge.soyTemplateName = 'ddl.badge';
}


ddl.condition = function(opt_data, opt_ignored) {
  return '<span>' + soy.$$escapeHtml(opt_data.operandType) + ' </span>' + ddl.badge({content: opt_data.operandValue});
};
if (goog.DEBUG) {
  ddl.condition.soyTemplateName = 'ddl.condition';
}


ddl.action = function(opt_data, opt_ignored) {
  return '<b>' + soy.$$escapeHtml(opt_data.action.action) + '</b> ' + ddl.badge({content: opt_data.action.target});
};
if (goog.DEBUG) {
  ddl.action.soyTemplateName = 'ddl.action';
}
