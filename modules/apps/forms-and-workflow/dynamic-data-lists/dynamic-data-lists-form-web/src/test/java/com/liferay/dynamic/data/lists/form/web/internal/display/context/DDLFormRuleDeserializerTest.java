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

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONDeserializer;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class DDLFormRuleDeserializerTest {

	@Test
	public void testDeserializer() throws JSONException {
		String serializedJSON = "[{\"actions\":[{\"action\":\"show\",\"target\":\"T\"}],\"conditions\":[{\"operands\":[{\"type\":\"field\",\"value\":\"T\"},{\"type\":\"constant\",\"value\":\"ae\"}],\"operator\":\"equals-to\"}],\"logical-operator\":\"OR\"}]";

		JSONFactory jsonFactory = new JSONFactoryImpl();

		JSONArray jsonArray = jsonFactory.createJSONArray(serializedJSON);

		List<DDLFormRule> rules = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONDeserializer<DDLFormRule> jsonDeserializer = jsonFactory.createJSONDeserializer();

			rules.add(jsonDeserializer.deserialize(jsonArray.getJSONObject(i).toString(), DDLFormRule.class));
		}

		DDLFormRule expectedDDLFormRule = new DDLFormRule();

		expectedDDLFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("show", "T"));

		expectedDDLFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"equals-to",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "T"),
					new DDLFormRuleCondition.Operand("constant", "ae")
				)));

		Assert.assertEquals(expectedDDLFormRule, rules.get(0));

	}
}
