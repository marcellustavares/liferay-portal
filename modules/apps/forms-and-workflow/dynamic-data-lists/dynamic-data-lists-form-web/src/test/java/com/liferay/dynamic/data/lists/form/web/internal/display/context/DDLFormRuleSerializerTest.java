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
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONSerializer;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Marcellus Tavares
 */
public class DDLFormRuleSerializerTest {

	@Test
	public void testDeserializer() {


		DDLFormRule ddlFormRule = new DDLFormRule();

		ddlFormRule.addDDLFormRuleAction(
			new DDLFormRuleAction("show", "T"));

		ddlFormRule.addDDLFormRuleConditions(
			new DDLFormRuleCondition(
				"contains",
				Arrays.asList(
					new DDLFormRuleCondition.Operand("field", "Field1"),
					new DDLFormRuleCondition.Operand("constant", "Joe")
				)));

		JSONFactory jsonFactory = new JSONFactoryImpl();

		JSONSerializer jsonSerializer = jsonFactory.createJSONSerializer();

		System.out.println(jsonSerializer.serializeDeep(Arrays.asList(ddlFormRule)));


	}
}
