/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.lists.form.web.internal.display.context;

import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class DDLFormRuleToDDMFormRuleConverterTest {

	@Test
	public void testShowRuleWithNumericCondition() {
		DDMFormRule expectedDDMFormRule = new DDMFormRule(
			"TRUE",
			Arrays.asList("setVisible('Field2', getValue('Field1') > 2)"));

		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("show", "Field2"));
		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"greater-than",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "2")
				)));

		DDLFormRuleToDDMFormRuleRuleConverter ddlFormRuleToDDMFormRuleRuleConverter =
			new DDLFormRuleToDDMFormRuleRuleConverter(ddlFormRule);

		List<DDMFormRule> actualDDMFormRules  =
			ddlFormRuleToDDMFormRuleRuleConverter.convert();

		Assert.assertEquals(expectedDDMFormRule, actualDDMFormRules.get(0));
	}

	@Test
	public void testShowRuleWithStringCondition1() {
		DDMFormRule expectedDDMFormRule = new DDMFormRule(
			"TRUE",
			Arrays.asList("setVisible('Field2', contains(getValue('Field1'), 'Joe'))"));

		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("show", "Field2"));
		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"contains",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "Joe")
				)));

		DDLFormRuleToDDMFormRuleRuleConverter ddlFormRuleToDDMFormRuleRuleConverter =
			new DDLFormRuleToDDMFormRuleRuleConverter(ddlFormRule);

		List<DDMFormRule> actualDDMFormRules  =
			ddlFormRuleToDDMFormRuleRuleConverter.convert();

		Assert.assertEquals(expectedDDMFormRule, actualDDMFormRules.get(0));

	}

	@Test
	public void testShowRuleWithStringCondition2() {
		DDMFormRule expectedDDMFormRule = new DDMFormRule(
			"TRUE",
			Arrays.asList("setVisible('Field2', isEmailAddress(getValue('Field1')))"));

		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("show", "Field2"));
		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"isEmailAddress",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1")
				)));

		DDLFormRuleToDDMFormRuleRuleConverter ddlFormRuleToDDMFormRuleRuleConverter =
			new DDLFormRuleToDDMFormRuleRuleConverter(ddlFormRule);

		List<DDMFormRule> actualDDMFormRules  =
			ddlFormRuleToDDMFormRuleRuleConverter.convert();

		Assert.assertEquals(expectedDDMFormRule, actualDDMFormRules.get(0));

	}

	@Test
	public void testShowRuleWithLogicalOperator1() {
		DDMFormRule expectedDDMFormRule = new DDMFormRule(
			"TRUE",
			Arrays.asList("setVisible('Field2', getValue('Field1') > 2 or getValue('Field1') < 4 or getValue('Field1') < 6)"));

		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("show", "Field2"));

		ddlFormRule.setLogicalOperator("or");

		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"greater-than",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "2")
				)));
		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"less-than",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "4")
				)));
		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"less-than",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "6")
				)));

		DDLFormRuleToDDMFormRuleRuleConverter ddlFormRuleToDDMFormRuleRuleConverter =
			new DDLFormRuleToDDMFormRuleRuleConverter(ddlFormRule);

		List<DDMFormRule> actualDDMFormRules  =
			ddlFormRuleToDDMFormRuleRuleConverter.convert();

		Assert.assertEquals(expectedDDMFormRule, actualDDMFormRules.get(0));

	}

	@Test
	public void testShowRuleWithLogicalOperator2() {
		DDMFormRule expectedDDMFormRule = new DDMFormRule(
			"TRUE",
			Arrays.asList("setVisible('Field2', contains(getValue('Field1'), 'Joe') and contains(getValue('Field1'), 'Blog'))"));

		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("show", "Field2"));

		ddlFormRule.setLogicalOperator("and");

		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"contains",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "Joe")
				)));
		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"contains",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "Blog")
				)));

		DDLFormRuleToDDMFormRuleRuleConverter ddlFormRuleToDDMFormRuleRuleConverter =
			new DDLFormRuleToDDMFormRuleRuleConverter(ddlFormRule);

		List<DDMFormRule> actualDDMFormRules  =
			ddlFormRuleToDDMFormRuleRuleConverter.convert();

		Assert.assertEquals(expectedDDMFormRule, actualDDMFormRules.get(0));

	}

	@Test
	public void testCalculateRule1() {
		DDMFormRule expectedDDMFormRule = new DDMFormRule(
			"contains(getValue('Field1'), 'Joe')",
			Arrays.asList("setValue('Field2', 2 + 3)"));

		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("calculate", "Field2", "2 + 3"));

		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"contains",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "Joe")
				)));

		DDLFormRuleToDDMFormRuleRuleConverter ddlFormRuleToDDMFormRuleRuleConverter =
			new DDLFormRuleToDDMFormRuleRuleConverter(ddlFormRule);

		List<DDMFormRule> actualDDMFormRules  =
			ddlFormRuleToDDMFormRuleRuleConverter.convert();

		Assert.assertEquals(expectedDDMFormRule, actualDDMFormRules.get(0));

	}

	@Test
	public void testCalculateRule2() {
		DDMFormRule expectedDDMFormRule = new DDMFormRule(
			"contains(getValue('Field1'), 'Joe')",
			Arrays.asList("setValue('Field2', 2 + getValue('Field3'))"));

		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("calculate", "Field2", "2 + 'Field3'"));

		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"contains",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "Joe")
				)));

		DDLFormRuleToDDMFormRuleRuleConverter ddlFormRuleToDDMFormRuleRuleConverter =
			new DDLFormRuleToDDMFormRuleRuleConverter(ddlFormRule);

		List<DDMFormRule> actualDDMFormRules  =
			ddlFormRuleToDDMFormRuleRuleConverter.convert();

		Assert.assertEquals(expectedDDMFormRule, actualDDMFormRules.get(0));

	}

	@Test
	public void testBooleanAndCalculateRule() {
		DDMFormRule expectedDDMFormRule1 = new DDMFormRule(
			"TRUE",
			Arrays.asList("setVisible('Field4', contains(getValue('Field1'), 'Joe'))"));

		DDMFormRule expectedDDMFormRule2 = new DDMFormRule(
			"contains(getValue('Field1'), 'Joe')",
			Arrays.asList("setValue('Field2', 2 + getValue('Field3'))"));

		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("calculate", "Field2", "2 + 'Field3'"));
		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("show", "Field4"));

		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"contains",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "Joe")
				)));

		DDLFormRuleToDDMFormRuleRuleConverter ddlFormRuleToDDMFormRuleRuleConverter =
			new DDLFormRuleToDDMFormRuleRuleConverter(ddlFormRule);

		List<DDMFormRule> actualDDMFormRules  =
			ddlFormRuleToDDMFormRuleRuleConverter.convert();

		Assert.assertEquals(expectedDDMFormRule1, actualDDMFormRules.get(0));
		Assert.assertEquals(expectedDDMFormRule2, actualDDMFormRules.get(1));
	}

}
