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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorGraph {

	public DDMFormRuleEvaluatorGraph(Set<DDMFormRuleEvaluatorNode> nodes) {
		_nodes = nodes;
	}

	public Set<DDMFormRuleEvaluatorNode> getNodes() {
		return _nodes;
	}

	public List<DDMFormRuleEvaluatorNode> sort()
		throws DDMFormEvaluationException {

		List<DDMFormRuleEvaluatorNode> sortedNodes = new ArrayList<>();

		for (DDMFormRuleEvaluatorNode node : _nodes) {
			node.visit(sortedNodes);
		}

		return sortedNodes;
	}

	private final Set<DDMFormRuleEvaluatorNode> _nodes;

}