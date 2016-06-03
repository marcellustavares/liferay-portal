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

package com.liferay.dynamic.data.mapping.form.rule.internal;

import com.liferay.dynamic.data.mapping.form.rule.DDMFormRuleEvaluationException;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorNode {

	public DDMFormRuleEvaluatorNode(
		String ddmFormFieldName, String instanceId,
		DDMFormFieldRuleType ddmFormFieldRuleType, String expression) {

		_ddmFormFieldName = ddmFormFieldName;
		_instanceId = instanceId;
		_ddmFormFieldRuleType = ddmFormFieldRuleType;
		_expression = expression;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		DDMFormRuleEvaluatorNode other = (DDMFormRuleEvaluatorNode)obj;

		if (_ddmFormFieldName == null) {
			if (other._ddmFormFieldName != null) {
				return false;
			}
		}
		else if (!_ddmFormFieldName.equals(other._ddmFormFieldName)) {
			return false;
		}

		if (_ddmFormFieldRuleType != other._ddmFormFieldRuleType) {
			return false;
		}

		if (_instanceId == null) {
			if (other._instanceId != null) {
				return false;
			}
		}
		else if (!_instanceId.equals(other._instanceId)) {
			return false;
		}

		return true;
	}

	public String getDDMFormFieldName() {
		return _ddmFormFieldName;
	}

	public DDMFormFieldRuleType getDDMFormFieldRuleType() {
		return _ddmFormFieldRuleType;
	}

	public List<DDMFormRuleEvaluatorNode> getEdges() {
		return _edges;
	}

	public String getExpression() {
		return _expression;
	}

	public String getInstanceId() {
		return _instanceId;
	}
	
	public void setExpression(String expression) {
		_expression = expression;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result +
			((_ddmFormFieldName == null) ? 0 : _ddmFormFieldName.hashCode());
		result = prime * result +
			((_ddmFormFieldRuleType == null) ?
				0 : _ddmFormFieldRuleType.hashCode());
		result = prime * result +
			((_instanceId == null) ? 0 : _instanceId.hashCode());
		return result;
	}

	public boolean isResolved() {
		return _resolved;
	}

	public boolean isVisited() {
		return _visited;
	}

	public void visit(List<DDMFormRuleEvaluatorNode> sortedNodes)
		throws Exception {

		if (!_visited) {
			_visited = true;

			for (DDMFormRuleEvaluatorNode edge : getEdges()) {
				edge.visit(sortedNodes);
			}

			sortedNodes.add(this);
			_resolved = true;
		}
		else if(!_resolved) {
			throw new DDMFormRuleEvaluationException("A loop was found");
		}
	}

	private final String _ddmFormFieldName;
	private final DDMFormFieldRuleType _ddmFormFieldRuleType;
	private final List<DDMFormRuleEvaluatorNode> _edges = new ArrayList<>();
	private String _expression;
	private final String _instanceId;
	private boolean _resolved;
	private boolean _visited;

}