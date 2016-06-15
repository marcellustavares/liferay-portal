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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.rules;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.portal.kernel.util.HashUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorNode {

	public DDMFormRuleEvaluatorNode(
		String expression, DDMFormFieldRuleType ddmFormFieldRuleType,
		String ddmFormFieldName, String instanceId) {

		_expression = expression;
		_ddmFormFieldRuleType = ddmFormFieldRuleType;
		_ddmFormFieldName = ddmFormFieldName;
		_instanceId = instanceId;
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

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _ddmFormFieldName);

		hash = HashUtil.hash(hash, _instanceId);

		return HashUtil.hash(hash, _ddmFormFieldRuleType);
	}

	public boolean isResolved() {
		return _resolved;
	}

	public boolean isVisited() {
		return _visited;
	}

	public void setExpression(String expression) {
		_expression = expression;
	}

	public void visit(List<DDMFormRuleEvaluatorNode> sortedNodes)
		throws DDMFormEvaluationException {

		if (!_visited) {
			_visited = true;

			for (DDMFormRuleEvaluatorNode edge : getEdges()) {
				edge.visit(sortedNodes);
			}

			sortedNodes.add(this);
			_resolved = true;
		}
		else if(!_resolved) {
			throw new DDMFormEvaluationException("A loop was found");
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