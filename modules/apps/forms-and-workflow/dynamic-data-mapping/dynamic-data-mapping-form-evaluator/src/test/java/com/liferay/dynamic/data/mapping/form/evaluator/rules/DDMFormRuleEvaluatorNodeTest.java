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
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorNode;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorNodeTest {

	@Test
	public void testEquals() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"1 + 2", DDMFormFieldRuleType.READ_ONLY, "field0",
				"field0_instanceId");

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"2 * 3", DDMFormFieldRuleType.READ_ONLY, "field0",
				"field0_instanceId");

		Assert.assertTrue(
			ddmFormRuleEvaluatorNode1.equals(ddmFormRuleEvaluatorNode2));
	}

	@Test(expected = DDMFormEvaluationException.class)
	public void testLoop() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"isVisible(field0)", DDMFormFieldRuleType.READ_ONLY, "field1",
				StringPool.BLANK);

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"isReadOnly(field1)", DDMFormFieldRuleType.VISIBILITY, "field0",
				StringPool.BLANK);

		ddmFormRuleEvaluatorNode0.getEdges().add(ddmFormRuleEvaluatorNode1);
		ddmFormRuleEvaluatorNode1.getEdges().add(ddmFormRuleEvaluatorNode0);

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>();

		ddmFormRuleEvaluatorNode0.visit(nodes);
	}

	@Test
	public void testNotEquals() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"1 + 2", DDMFormFieldRuleType.VALIDATION, "field0",
				"field0_instanceId");

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"2 * 3", DDMFormFieldRuleType.READ_ONLY, "field0",
				"field0_instanceId");

		Assert.assertFalse(
			ddmFormRuleEvaluatorNode1.equals(ddmFormRuleEvaluatorNode2));
	}

	@Test
	public void testNotEquals2() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"1 + 2", DDMFormFieldRuleType.READ_ONLY, "field0",
				"field0_instanceId");

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"2 * 3", DDMFormFieldRuleType.READ_ONLY, "field1",
				"field0_instanceId");

		Assert.assertFalse(
			ddmFormRuleEvaluatorNode1.equals(ddmFormRuleEvaluatorNode2));
	}

	@Test
	public void testNotEquals3() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"1 + 2", DDMFormFieldRuleType.READ_ONLY, "field0",
				"field0_instanceId");

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode2 =
			new DDMFormRuleEvaluatorNode(
				"2 * 3", DDMFormFieldRuleType.READ_ONLY, "field0",
				"field1_instanceId");

		Assert.assertFalse(
			ddmFormRuleEvaluatorNode1.equals(ddmFormRuleEvaluatorNode2));
	}

	@Test
	public void testVisit() throws Exception {
		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode0 =
			new DDMFormRuleEvaluatorNode(
				"between(field0,1,5)", DDMFormFieldRuleType.READ_ONLY, "field1",
				StringPool.BLANK);

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode1 =
			new DDMFormRuleEvaluatorNode(
				"", DDMFormFieldRuleType.VALUE, "field0", StringPool.BLANK);

		ddmFormRuleEvaluatorNode0.getEdges().add(ddmFormRuleEvaluatorNode1);

		List<DDMFormRuleEvaluatorNode> nodes = new ArrayList<>();

		ddmFormRuleEvaluatorNode0.visit(nodes);

		Assert.assertEquals(2, nodes.size());
		Assert.assertTrue(ddmFormRuleEvaluatorNode0.isVisited());
		Assert.assertTrue(ddmFormRuleEvaluatorNode0.isResolved());
		Assert.assertTrue(ddmFormRuleEvaluatorNode1.isVisited());
		Assert.assertTrue(ddmFormRuleEvaluatorNode1.isResolved());
	}

}