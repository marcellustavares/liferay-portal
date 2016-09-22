/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.lists.form.web.internal.display.context;

import com.liferay.portal.kernel.json.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Marcellus Tavares
 */

public class DDLFormRule {

	public void addDDLFormRuleAction(DDLFormRuleAction ddlFormRuleAction) {
		_ddlFormRuleActions.add(ddlFormRuleAction);
	}

	public void addDDLFormRuleConditions(DDLFormRuleCondition ddlFormRuleCondition) {
		_ddlFormRuleConditions.add(ddlFormRuleCondition);
	}

	@JSON(name = "logical-operator")
	public void setLogicalOperator(String logicalOperator) {
		_logicalOperator = logicalOperator;
	}

	@JSON(name = "logical-operator")
	public String getLogicalOperator() {
		return _logicalOperator;
	}

	@JSON(name = "conditions")
	public void setDDLFormRuleConditions(
		List<DDLFormRuleCondition> ddlFormRuleConditions) {
		_ddlFormRuleConditions = ddlFormRuleConditions;
	}

	@JSON(name = "actions")
	public void setDDLFormRuleActions(
		List<DDLFormRuleAction> ddlFormRuleActions) {
		_ddlFormRuleActions = ddlFormRuleActions;
	}

	@JSON(name = "actions")
	public List<DDLFormRuleAction> getDDLFormRuleActions() {
		return _ddlFormRuleActions;
	}

	@JSON(name = "conditions")
	public List<DDLFormRuleCondition> getDdlFormRuleConditions() {
		return _ddlFormRuleConditions;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDLFormRule)) {
			return false;
		}

		DDLFormRule ddlFormRule = (DDLFormRule)obj;

		if (Objects.equals(_ddlFormRuleActions, ddlFormRule._ddlFormRuleActions) &&
			Objects.equals(_ddlFormRuleConditions, ddlFormRule._ddlFormRuleConditions)) {
			return true;
		}

		return false;
	}


	private List<DDLFormRuleAction> _ddlFormRuleActions = new ArrayList<>();


	private List<DDLFormRuleCondition> _ddlFormRuleConditions = new ArrayList<>();
	private String _logicalOperator = "and";
}
