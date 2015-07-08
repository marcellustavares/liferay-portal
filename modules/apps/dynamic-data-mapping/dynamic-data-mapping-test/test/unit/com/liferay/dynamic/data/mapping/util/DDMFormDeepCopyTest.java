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

package com.liferay.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.BaseDDMTestCase;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormDeepCopyTest extends BaseDDMTestCase {

	@Test
	public void testNestedFields() throws Exception {
		DDMForm ddmForm = createDDMForm();

		DDMFormField nameDDMFormField = new DDMFormField("Name", "textarea");

		ddmForm.addDDMFormField(nameDDMFormField);

		DDMFormField phoneDDMFormField = new DDMFormField("Phone", "text");
		DDMFormField contactDDMFormField = new DDMFormField("Name", "text");

		nameDDMFormField.addNestedDDMFormField(phoneDDMFormField);
		nameDDMFormField.addNestedDDMFormField(contactDDMFormField);

		DDMFormField addressDDMFormField = new DDMFormField("ZipCode", "text");

		contactDDMFormField.addNestedDDMFormField(addressDDMFormField);

		com.liferay.portlet.dynamicdatamapping.DDMForm copiedForm =
			BeanPropertiesUtil.deepCopyProperties(
				com.liferay.portlet.dynamicdatamapping.DDMForm.class, ddmForm);

		Assert.assertEquals(1, copiedForm.getDDMFormFields().size());

		com.liferay.portlet.dynamicdatamapping.DDMFormField formField =
			copiedForm.getDDMFormFields().get(0);

		Assert.assertEquals("Name", formField.getName());
		Assert.assertEquals("textarea", formField.getType());

		Assert.assertEquals(2, formField.getNestedDDMFormFields().size());

		com.liferay.portlet.dynamicdatamapping.DDMFormField nestedFormField =
			formField.getNestedDDMFormFields().get(0);

		Assert.assertEquals("Phone", nestedFormField.getName());
		Assert.assertEquals("text", nestedFormField.getType());

		nestedFormField = formField.getNestedDDMFormFields().get(1);

		Assert.assertEquals("Name", nestedFormField.getName());
		Assert.assertEquals("text", nestedFormField.getType());

		Assert.assertEquals(
			1, formField.getNestedDDMFormFields().get(
				1).getNestedDDMFormFields().size());

		nestedFormField = formField.getNestedDDMFormFields().get(
			1).getNestedDDMFormFields().get(0);

		Assert.assertEquals("ZipCode", nestedFormField.getName());
		Assert.assertEquals("text", nestedFormField.getType());
	}

	@Test
	public void testSomeFieldProperties() throws Exception {
		DDMForm ddmForm = createDDMForm();

		DDMFormField nameDDMFormField = new DDMFormField("Name", "textarea");

		ddmForm.addDDMFormField(nameDDMFormField);

		nameDDMFormField.setFieldNamespace("namespace");
		nameDDMFormField.setIndexType("indexType");

		LocalizedValue value = new LocalizedValue(LocaleUtil.BRAZIL);
		value.addString(LocaleUtil.BRAZIL, "teste");

		nameDDMFormField.setLabel(value);

		nameDDMFormField.setLocalizable(true);
		nameDDMFormField.setMultiple(true);
		nameDDMFormField.setReadOnly(true);
		nameDDMFormField.setRepeatable(true);
		nameDDMFormField.setRequired(true);
		nameDDMFormField.setShowLabel(true);

		value = new LocalizedValue(LocaleUtil.ENGLISH);
		value.addString(LocaleUtil.ENGLISH, "style");

		nameDDMFormField.setStyle(value);

		value = new LocalizedValue(LocaleUtil.FRANCE);
		value.addString(LocaleUtil.FRANCE, "tip");

		nameDDMFormField.setTip(value);

		nameDDMFormField.setVisibilityExpression("expression");

		DDMFormFieldOptions fieldOptions = new DDMFormFieldOptions();
		fieldOptions.setDefaultLocale(LocaleUtil.BRAZIL);
		fieldOptions.addOption("teste");
		fieldOptions.addOptionLabel("teste", LocaleUtil.ENGLISH, "label");

		nameDDMFormField.setDDMFormFieldOptions(fieldOptions);

		com.liferay.portlet.dynamicdatamapping.DDMForm copiedForm =
			BeanPropertiesUtil.deepCopyProperties(
				com.liferay.portlet.dynamicdatamapping.DDMForm.class, ddmForm);

		com.liferay.portlet.dynamicdatamapping.DDMFormField formField =
			copiedForm.getDDMFormFields().get(0);

		Assert.assertEquals("Name", formField.getName());
		Assert.assertEquals("textarea", formField.getType());

		Assert.assertEquals("namespace", formField.getFieldNamespace());
		Assert.assertEquals("indexType", formField.getIndexType());

		com.liferay.portlet.dynamicdatamapping.LocalizedValue lValue =
			formField.getLabel();

		Assert.assertEquals(LocaleUtil.BRAZIL, lValue.getDefaultLocale());
		Assert.assertEquals("teste", lValue.getString(LocaleUtil.BRAZIL));

		Assert.assertTrue(formField.isLocalizable());
		Assert.assertTrue(formField.isMultiple());
		Assert.assertTrue(formField.isReadOnly());
		Assert.assertTrue(formField.isRepeatable());
		Assert.assertTrue(formField.isRequired());
		Assert.assertTrue(formField.isShowLabel());

		lValue = formField.getStyle();

		Assert.assertEquals(LocaleUtil.ENGLISH, lValue.getDefaultLocale());
		Assert.assertEquals("style", lValue.getString(LocaleUtil.ENGLISH));

		lValue = formField.getTip();

		Assert.assertEquals(LocaleUtil.FRANCE, lValue.getDefaultLocale());
		Assert.assertEquals("tip", lValue.getString(LocaleUtil.FRANCE));

		Assert.assertEquals("expression", formField.getVisibilityExpression());

		com.liferay.portlet.dynamicdatamapping.DDMFormFieldOptions ffOptions =
			formField.getDDMFormFieldOptions();

		Assert.assertEquals(LocaleUtil.BRAZIL, ffOptions.getDefaultLocale());

		Map<String, com.liferay.portlet.dynamicdatamapping.LocalizedValue>
			mapOptions = ffOptions.getOptions();

		Assert.assertTrue(mapOptions.containsKey("teste"));

		lValue = mapOptions.get("teste");
		Assert.assertEquals("label", lValue.getString(LocaleUtil.ENGLISH));
	}

	@Test
	public void testWithFields() throws Exception {
		DDMForm ddmForm = createDDMForm();

		DDMFormField nameDDMFormField = new DDMFormField("Name", "textarea");

		DDMFormField phoneDDMFormField = new DDMFormField("Phone", "text");

		ddmForm.addDDMFormField(nameDDMFormField);
		ddmForm.addDDMFormField(phoneDDMFormField);

		com.liferay.portlet.dynamicdatamapping.DDMForm copiedForm =
			BeanPropertiesUtil.deepCopyProperties(
				com.liferay.portlet.dynamicdatamapping.DDMForm.class, ddmForm);

		Assert.assertEquals(2, copiedForm.getDDMFormFields().size());

		com.liferay.portlet.dynamicdatamapping.DDMFormField formField =
			copiedForm.getDDMFormFields().get(0);

		Assert.assertEquals("Name", formField.getName());
		Assert.assertEquals("textarea", formField.getType());

		formField = copiedForm.getDDMFormFields().get(1);

		Assert.assertEquals("Phone", formField.getName());
		Assert.assertEquals("text", formField.getType());
	}

	@Test
	public void testWithoutFields() throws Exception {
		DDMForm ddmForm = createDDMForm();

		com.liferay.portlet.dynamicdatamapping.DDMForm copiedForm =
			BeanPropertiesUtil.deepCopyProperties(
				com.liferay.portlet.dynamicdatamapping.DDMForm.class, ddmForm);

		Assert.assertTrue(!copiedForm.getAvailableLocales().isEmpty());
		Assert.assertEquals(
			copiedForm.getAvailableLocales().toArray()[0], LocaleUtil.US);
	}

}