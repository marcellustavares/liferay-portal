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

package com.liferay.dynamic.data.mapping.form.evaluator.rules;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorGraph;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorNode;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.portal.kernel.util.StringPool;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorGraphTest {

	@Test(expected = DDMFormEvaluationException.class)
	public void testLoop() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"isVisible(field0)", DDMFormFieldRuleType.READ_ONLY, "field1",
				StringPool.BLANK);

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"isVisible(field2)", DDMFormFieldRuleType.VISIBILITY, "field0",
				StringPool.BLANK);

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"isReadOnly(field1)", DDMFormFieldRuleType.VISIBILITY, "field2",
				StringPool.BLANK);

		ddmFormRuleEvaluatorNode0.getEdges().add(ddmFormRuleEvaluatorNode1);
		ddmFormRuleEvaluatorNode1.getEdges().add(ddmFormRuleEvaluatorNode2);
		ddmFormRuleEvaluatorNode2.getEdges().add(ddmFormRuleEvaluatorNode0);

		Set<DDMFormRuleEvaluatorNode> nodes = new HashSet<>();
		nodes.add(ddmFormRuleEvaluatorNode0);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			new DDMFormRuleEvaluatorGraph(nodes);

		ddmFormRuleEvaluatorGraph.sort();
	}

	@Test
	public void testSort() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"isReadOnly(field0)", DDMFormFieldRuleType.READ_ONLY, "field1",
				StringPool.BLANK);

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"equals(field0,\"value0\")", DDMFormFieldRuleType.READ_ONLY,
				"field0", StringPool.BLANK);

		ddmFormRuleEvaluatorNode0.getEdges().add(ddmFormRuleEvaluatorNode1);

		Set<DDMFormRuleEvaluatorNode> nodes = new HashSet<>();
		nodes.add(ddmFormRuleEvaluatorNode0);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			new DDMFormRuleEvaluatorGraph(nodes);

		List<DDMFormRuleEvaluatorNode> sortedNodes =
			ddmFormRuleEvaluatorGraph.sort();

		Assert.assertEquals(2, sortedNodes.size());
		Assert.assertTrue(ddmFormRuleEvaluatorNode0.isVisited());
		Assert.assertTrue(ddmFormRuleEvaluatorNode0.isResolved());
		Assert.assertTrue(ddmFormRuleEvaluatorNode1.isVisited());
		Assert.assertTrue(ddmFormRuleEvaluatorNode1.isResolved());
		Assert.assertEquals(ddmFormRuleEvaluatorNode1, sortedNodes.get(0));
		Assert.assertEquals(ddmFormRuleEvaluatorNode0, sortedNodes.get(1));
	}

	@Test
	public void testSort2() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"isVisible(field0)", DDMFormFieldRuleType.READ_ONLY, "field1",
				StringPool.BLANK);

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"isVisible(field2) && equals(field1,\"test\")",
				DDMFormFieldRuleType.VISIBILITY, "field0", StringPool.BLANK);

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"equals(field1,\"test\")", DDMFormFieldRuleType.VISIBILITY,
				"field2", StringPool.BLANK);

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode3 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field1", StringPool.BLANK);

		ddmFormRuleEvaluatorNode0.getEdges().add(ddmFormRuleEvaluatorNode1);
		ddmFormRuleEvaluatorNode1.getEdges().add(ddmFormRuleEvaluatorNode2);
		ddmFormRuleEvaluatorNode1.getEdges().add(ddmFormRuleEvaluatorNode3);
		ddmFormRuleEvaluatorNode2.getEdges().add(ddmFormRuleEvaluatorNode3);

		Set<DDMFormRuleEvaluatorNode> nodes = new HashSet<>();
		nodes.add(ddmFormRuleEvaluatorNode0);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			new DDMFormRuleEvaluatorGraph(nodes);

		List<DDMFormRuleEvaluatorNode> sortedNodes =
			ddmFormRuleEvaluatorGraph.sort();

		Assert.assertEquals(4, sortedNodes.size());
		Assert.assertTrue(ddmFormRuleEvaluatorNode0.isVisited());
		Assert.assertTrue(ddmFormRuleEvaluatorNode0.isResolved());
		Assert.assertTrue(ddmFormRuleEvaluatorNode1.isVisited());
		Assert.assertTrue(ddmFormRuleEvaluatorNode1.isResolved());
		Assert.assertTrue(ddmFormRuleEvaluatorNode2.isVisited());
		Assert.assertTrue(ddmFormRuleEvaluatorNode2.isResolved());
		Assert.assertTrue(ddmFormRuleEvaluatorNode3.isVisited());
		Assert.assertTrue(ddmFormRuleEvaluatorNode3.isResolved());
		Assert.assertEquals(ddmFormRuleEvaluatorNode3, sortedNodes.get(0));
		Assert.assertEquals(ddmFormRuleEvaluatorNode2, sortedNodes.get(1));
		Assert.assertEquals(ddmFormRuleEvaluatorNode1, sortedNodes.get(2));
		Assert.assertEquals(ddmFormRuleEvaluatorNode0, sortedNodes.get(3));
	}

}