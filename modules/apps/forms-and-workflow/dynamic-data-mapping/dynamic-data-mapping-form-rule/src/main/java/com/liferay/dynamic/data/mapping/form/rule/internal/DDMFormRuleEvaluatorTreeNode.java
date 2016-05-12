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

import com.liferay.dynamic.data.mapping.form.rule.internal.rules.DDMFormFieldBaseRule;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorTreeNode {

	public DDMFormRuleEvaluatorTreeNode(DDMFormFieldBaseRule rule) {
		_rule = rule;
	}

	public void addChildNode(DDMFormRuleEvaluatorTreeNode childNode) {
		if (_parentNode != null) {
			_parentNode._children.remove(childNode);
		}

		childNode._parentNode = this;

		childNode._depth = 0;

		DDMFormRuleEvaluatorTreeNode _currentParent = childNode._parentNode;

		while (_currentParent != null) {
			childNode._depth++;
			_currentParent = _currentParent._parentNode;
		}

		_children.add(childNode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		else if (obj == null) {
			return false;
		}
		else if (getClass() != obj.getClass()) {
			return false;
		}

		DDMFormRuleEvaluatorTreeNode other = (DDMFormRuleEvaluatorTreeNode)obj;

		if (_rule == null) {
			if (other._rule != null) {
				return false;
			}
		}

		if (!_rule.equals(other._rule)) {
			return false;
		}

		return true;
	}

	public List<DDMFormRuleEvaluatorTreeNode> getChildren() {
		return _children;
	}

	public int getDepth() {
		return _depth;
	}

	public String getExpression() {
		if (_rule == null) {
			return StringPool.BLANK;
		}

		return _rule.getExpression();
	}

	public DDMFormFieldBaseRule getRule() {
		return _rule;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_rule == null) ? 0 : _rule.hashCode());
		return result;
	}

	public boolean isAffectedBy(DDMFormRuleEvaluatorTreeNode treeNode) {
		DDMFormFieldBaseRule ddmFormFieldRule = treeNode.getRule();

		return ddmFormFieldRule.isAffectedBy(getExpression());
	}

	private final List<DDMFormRuleEvaluatorTreeNode> _children =
		new ArrayList<>();
	private int _depth;
	private DDMFormRuleEvaluatorTreeNode _parentNode;
	private final DDMFormFieldBaseRule _rule;

}