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

package com.liferay.portlet.dynamic.data.mapping.registry.test;

import java.util.Set;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portlet.dynamic.data.mapping.registry.test.bundle.ddmformfieldtyperegistryimpl.TestDDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistry;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistryUtil;

/**
 * @author Peter Fellwock
 */
@RunWith(Arquillian.class)
public class DDMFormFieldTypeRegistryImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.ddmformfieldtyperegistryimpl"));

	@Test
	public void testGetDDMFormFieldType() {
		String className = TestDDMFormFieldType.class.getName();

		DDMFormFieldType dDMFormFieldType =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldType(className);

		Class<?> clazz = dDMFormFieldType.getClass();

		Assert.assertEquals(className, clazz.getName());
	}

	@Test
	public void testGetDDMFormFieldTypeNames() {
		Set<String> dDMFormFieldTypeNames =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldTypeNames();

		for (String dDMFormFieldTypeName : dDMFormFieldTypeNames) {
			if (dDMFormFieldTypeName.equals(
					TestDDMFormFieldType.class.getName())) {

				return;
			}
		}

		Assert.fail();
	}

	@Test
	public void testInstanceGetDDMFormFieldType() {
		DDMFormFieldTypeRegistry dDMFormFieldTypeRegistry =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldTypeRegistry();

		DDMFormFieldType dDMFormFieldType =
			dDMFormFieldTypeRegistry.getDDMFormFieldType(
				TestDDMFormFieldType.class.getName());

		Class<?> clazz = dDMFormFieldType.getClass();

		Assert.assertEquals(
			TestDDMFormFieldType.class.getName(), clazz.getName());
	}

	@Test
	public void testInstanceGetDDMFormFieldTypeNames() {
		DDMFormFieldTypeRegistry dDMFormFieldTypeRegistry =
			DDMFormFieldTypeRegistryUtil.getDDMFormFieldTypeRegistry();

		Set<String> dDMFormFieldTypeNames =
			dDMFormFieldTypeRegistry.getDDMFormFieldTypeNames();

		for (String dDMFormFieldTypeName : dDMFormFieldTypeNames) {
			if (dDMFormFieldTypeName.equals(
					TestDDMFormFieldType.class.getName())) {

				return;
			}
		}

		Assert.fail();
	}

}