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

import com.liferay.portal.kernel.exception.PortalException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorTree {

	public DDMFormRuleEvaluatorTree(
		DDMFormFieldRuleHelper ddmFormFieldRuleHelper) {

		_ddmFormFieldRuleHelper = ddmFormFieldRuleHelper;
	}

	public void addTreeNode(DDMFormRuleEvaluatorTreeNode treeNode)
		throws PortalException {

		String expression = treeNode.getExpression();

		boolean hasDependencies =
			_ddmFormFieldRuleHelper.hasDDMFormFieldDependencies(expression);

		if (!hasDependencies) {
			_rootNode.addChildNode(treeNode);
		}
		else {
			List<DDMFormRuleEvaluatorTreeNode> treeNodes =
				_ddmFormFieldRuleHelper.getTreeNodesForDDMFormFieldDependencies(
					expression);

			DDMFormRuleEvaluatorTreeNode deeperNode = null;
			int deeper = 0;

			for (DDMFormRuleEvaluatorTreeNode dependentTreeNode : treeNodes) {
				DDMFormRuleEvaluatorTreeNode foundTreeNode = findTreeNode(
					_rootNode, dependentTreeNode);

				if (deeperNode == null && foundTreeNode == null) {
					deeperNode = _rootNode;
				}
				else if(foundTreeNode != null) {
					if (foundTreeNode.getDepth() > deeper) {
						deeper = foundTreeNode.getDepth();
						deeperNode = foundTreeNode;
					}
				}
			}

			deeperNode.addChildNode(treeNode);

			List<DDMFormRuleEvaluatorTreeNode> affectedByTreeNodes =
				new ArrayList<>();

			findAffectedByTreeNode(_rootNode, treeNode, affectedByTreeNodes);

			for (DDMFormRuleEvaluatorTreeNode affectedByTreeNode :
					affectedByTreeNodes) {

				if (treeNode.getDepth() >= affectedByTreeNode.getDepth()) {
					treeNode.addChildNode(affectedByTreeNode);
				}
			}
		}
	}

	public void assignPriorities() {
		for (DDMFormRuleEvaluatorTreeNode treeNode : _rootNode.getChildren()) {
			assignPriority(treeNode, 0);
		}
	}

	protected void assignPriority(
		DDMFormRuleEvaluatorTreeNode treeNode, int priority) {

		treeNode.getRule().setPriority(priority);

		for (DDMFormRuleEvaluatorTreeNode childNode : treeNode.getChildren()) {
			assignPriority(childNode, priority + 1);
		}
	}

	protected void findAffectedByTreeNode(
		DDMFormRuleEvaluatorTreeNode treeNode,
		DDMFormRuleEvaluatorTreeNode findTreeNode,
		List<DDMFormRuleEvaluatorTreeNode> foundTreeNodes) {

		if (treeNode.isAffectedBy(findTreeNode)) {
			foundTreeNodes.add(treeNode);
		}

		List<DDMFormRuleEvaluatorTreeNode> children = treeNode.getChildren();

		if (!children.isEmpty()) {
			for (DDMFormRuleEvaluatorTreeNode childNode : children) {
				findAffectedByTreeNode(childNode, findTreeNode, foundTreeNodes);
			}
		}
	}

	protected DDMFormRuleEvaluatorTreeNode findTreeNode(
		DDMFormRuleEvaluatorTreeNode treeNode,
		DDMFormRuleEvaluatorTreeNode findTreeNode) {

		if ((treeNode.getRule() != null) && treeNode.equals(findTreeNode)) {
			return treeNode;
		}

		List<DDMFormRuleEvaluatorTreeNode> children = treeNode.getChildren();

		if (!children.isEmpty()) {
			DDMFormRuleEvaluatorTreeNode foundNode = null;

			for (DDMFormRuleEvaluatorTreeNode childNode : children) {
				foundNode = findTreeNode(childNode, findTreeNode);

				if (foundNode != null) {
					return foundNode;
				}
			}
		}

		return null;
	}

	private final DDMFormFieldRuleHelper _ddmFormFieldRuleHelper;
	private final DDMFormRuleEvaluatorTreeNode _rootNode =
		new DDMFormRuleEvaluatorTreeNode(null);

}