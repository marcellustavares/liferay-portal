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

package com.liferay.dynamic.data.mapping.expression.evaluator.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.expression.evaluator.DDMExpressionEvaluator;
import com.liferay.dynamic.data.mapping.service.test.BaseDDMServiceTestCase;
import com.liferay.portal.expression.ExpressionFactory;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Pablo Carvalho
 */
@RunWith(Arquillian.class)
public class DDMExpressionEvaluatorTest extends BaseDDMServiceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testValidFields() throws Exception {
		String serializedDDMFormValues = read(
			"ddm-expression-evaluator-form-values-test-data.json");

		String serializedDDMForm = read(
			"ddm-expression-evaluator-form-test-data.json");

		String languageId = LocaleUtil.toLanguageId(LocaleUtil.US);

		DDMExpressionEvaluator ddmExpressionEvaluator =
			new DDMExpressionEvaluator(
				serializedDDMFormValues, serializedDDMForm, languageId);

		Registry registry = RegistryUtil.getRegistry();

		ExpressionFactory expressionFactory = registry.getService(
			ExpressionFactory.class);

		ddmExpressionEvaluator.setExpressionFactory(expressionFactory);

		String actualResult = ddmExpressionEvaluator.evaluate();

		String expectedResult = read(
			"ddm-expression-evaluator-result-data.json");

		Assert.assertEquals(expectedResult, actualResult);
	}

}