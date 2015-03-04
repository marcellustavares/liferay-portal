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

package com.liferay.portlet.dynamicdatamapping.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.liferay.portal.bean.BeanPropertiesImpl;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.registry.BaseDDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistry;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeRegistryUtil;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueAccessor;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueRendererAccessor;
import com.liferay.portlet.dynamicdatamapping.registry.DataTypeDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.IndexTypeDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.LabelDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.NameDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.OptionsDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.PredefinedValueDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.RepeatableDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.RequiredDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.ShowLabelDDMFormFieldTypeSetting;
import com.liferay.portlet.dynamicdatamapping.registry.TipDDMFormFieldTypeSetting;

/**
 * @author Marcellus Tavares
 */
@PrepareForTest({LocaleUtil.class})
public class DDMFormJSONDeserializerTest
	extends BaseDDMFormDeserializerTestCase {

	@Before
	public void setUp() {
		setUpBeanPropertiesUtil();
		setUpDDMFormFieldTypeRegistryUtil();
		setUpDDMFormJSONDeserializerUtil();
		setUpLocaleUtil();
		setUpJSONFactoryUtil();
	}
	
	protected void setUpBeanPropertiesUtil() {
		BeanPropertiesUtil beanPropertiesUtil = new BeanPropertiesUtil();

		beanPropertiesUtil.setBeanProperties(new BeanPropertiesImpl());
	}
	
	protected void setUpDDMFormFieldTypeRegistryUtil() {
		List<DDMFormFieldTypeSetting> requiredSettings = new ArrayList<>();

		requiredSettings.add(new DataTypeDDMFormFieldTypeSetting());
		requiredSettings.add(new IndexTypeDDMFormFieldTypeSetting());
		requiredSettings.add(new PredefinedValueDDMFormFieldTypeSetting());
		requiredSettings.add(new RepeatableDDMFormFieldTypeSetting());
		requiredSettings.add(new NameDDMFormFieldTypeSetting());
		requiredSettings.add(new LabelDDMFormFieldTypeSetting());
		requiredSettings.add(new TipDDMFormFieldTypeSetting());
		requiredSettings.add(new RequiredDDMFormFieldTypeSetting());
		requiredSettings.add(new ShowLabelDDMFormFieldTypeSetting());
		requiredSettings.add(new OptionsDDMFormFieldTypeSetting());

		when(
			_ddmFormFieldType.getRequiredSettings()
		).thenReturn(
			requiredSettings
		);
		
		when(
			_ddmFormFieldTypeRegistry.getDDMFormFieldType(Matchers.anyString())
		).thenReturn(
			_ddmFormFieldType
		);
		
		DDMFormFieldTypeRegistryUtil ddmFormFieldTypeRegistryUtil =
			new DDMFormFieldTypeRegistryUtil();
		
		ddmFormFieldTypeRegistryUtil.setDDMFormFieldTypeRegistry(
			_ddmFormFieldTypeRegistry);
	}

	@Override
	protected DDMForm deserialize(String serializedDDMForm)
		throws PortalException {

		return DDMFormJSONDeserializerUtil.deserialize(serializedDDMForm);
	}

	@Override
	protected String getDeserializerType() {
		return "json";
	}

	@Override
	protected String getTestFileExtension() {
		return ".json";
	}

	protected void setUpDDMFormJSONDeserializerUtil() {
		DDMFormJSONDeserializerUtil ddmFormJSONDeserializerUtil =
			new DDMFormJSONDeserializerUtil();

		ddmFormJSONDeserializerUtil.setDDMFormJSONDeserializer(
			new DDMFormJSONDeserializerImpl());
	}
	
	@Mock
	private BaseDDMFormFieldType _ddmFormFieldType;
	
	@Mock
	private DDMFormFieldTypeRegistry _ddmFormFieldTypeRegistry;
}
