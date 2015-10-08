// This file was automatically generated from date.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.date = function(opt_data, opt_ignored) {
  return '\t<script type="text/javascript">AUI().use( \'node\', \'aui-datepicker\', function(A) {var datePicker = new A.DatePicker({trigger: \'#displayDate\', popover: {zIndex: 1}, on: {selectionChange: function(event) {var selectedDate = event.newSelection[0]; var formattedSelectedDateValue = selectedDate.getDate(); formattedSelectedDateValue = formattedSelectedDateValue + "," + selectedDate.getMonth(); formattedSelectedDateValue = formattedSelectedDateValue + "," + selectedDate.getFullYear(); A.one(\'#selectedDate\').set("value", formattedSelectedDateValue);}}});});<\/script><div class="form-group field-wrapper" data-fieldname="' + soy.$$escapeHtml(opt_data.name) + '"><label class="control-fieldLabel" for="displayDate">' + soy.$$escapeHtml(opt_data.label) + '</label><input class="field form-control" dir="' + soy.$$escapeHtml(opt_data.dir) + '" id="displayDate" name="displayDate" type="text" value="' + soy.$$escapeHtml(opt_data.value) + '"><input id="selectedDate" name="' + soy.$$escapeHtml(opt_data.name) + '" type="hidden" value="">' + soy.$$filterNoAutoescape(opt_data.childElementsHTML) + '</div>';
};
