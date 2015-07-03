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

package com.liferay.journal.test.util;

import com.liferay.portal.kernel.locale.test.LocaleTestUtil;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormXSDDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.StorageType;
import com.liferay.portlet.dynamicdatamapping.util.DDMUtil;
import com.liferay.portlet.dynamicdatamapping.util.DDMXMLUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class DDMTestUtil {

	public static DDMStructure addDDMStructure(
			long groupId, long parentStructureId, long classNameId,
			String structureKey, String name, String description,
			DDMForm ddmForm, DDMFormLayout ddmFormLayout, String storageType,
			int type)
		throws Exception {

		return DDMStructureLocalServiceUtil.addStructure(
			TestPropsValues.getUserId(), groupId, parentStructureId,
			classNameId, structureKey, LocaleTestUtil.getDefaultLocaleMap(name),
			LocaleTestUtil.getDefaultLocaleMap(description), ddmForm,
			ddmFormLayout, storageType, type,
			ServiceContextTestUtil.getServiceContext(groupId));
	}

	public static DDMStructure addDDMStructure(
			long groupId, long classNameId, String structureKey, String name,
			DDMForm ddmForm, String storageType, int type)
		throws Exception {

		DDMFormLayout ddmFormLayout = DDMUtil.getDefaultDDMFormLayout(ddmForm);

		return addDDMStructure(
			groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
			classNameId, structureKey, name, StringPool.BLANK, ddmForm,
			ddmFormLayout, storageType, type);
	}

	public static DDMStructure addDDMStructure(
			long groupId, long classNameId, String structureKey, String name,
			String definition, String storageType, int type)
		throws Exception {

		DDMForm ddmForm = toDDMForm(definition);

		return addDDMStructure(
			groupId, classNameId, structureKey, name, ddmForm, storageType,
			type);
	}

	public static DDMStructure addDDMStructure(long groupId, String className)
		throws Exception {

		return addDDMStructure(
			groupId, className, 0, getSampleDDMForm(),
			LocaleUtil.getSiteDefault(),
			ServiceContextTestUtil.getServiceContext());
	}

	public static DDMStructure addDDMStructure(
			long groupId, String className, DDMForm ddmForm)
		throws Exception {

		return addDDMStructure(
			groupId, className, 0, ddmForm, LocaleUtil.getSiteDefault(),
			ServiceContextTestUtil.getServiceContext());
	}

	public static DDMStructure addDDMStructure(
			long groupId, String className, DDMForm ddmForm,
			Locale defaultLocale)
		throws Exception {

		return addDDMStructure(
			groupId, className, 0, ddmForm, defaultLocale,
			ServiceContextTestUtil.getServiceContext());
	}

	public static DDMStructure addDDMStructure(
			long groupId, String className, long parentStructureId,
			DDMForm ddmForm, Locale defaultLocale,
			ServiceContext serviceContext)
		throws Exception {

		Map<Locale, String> nameMap = new HashMap<>();

		nameMap.put(defaultLocale, "Test Structure");

		DDMFormLayout ddmFormLayout = DDMUtil.getDefaultDDMFormLayout(ddmForm);

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return DDMStructureLocalServiceUtil.addStructure(
			TestPropsValues.getUserId(), groupId, parentStructureId,
			PortalUtil.getClassNameId(className), null, nameMap, null, ddmForm,
			ddmFormLayout, StorageType.JSON.toString(),
			DDMStructureConstants.TYPE_DEFAULT, serviceContext);
	}

	public static DDMStructure addDDMStructure(String className)
		throws Exception {

		return addDDMStructure(
			TestPropsValues.getGroupId(), className, 0, getSampleDDMForm(),
			LocaleUtil.getSiteDefault(),
			ServiceContextTestUtil.getServiceContext());
	}

	public static DDMStructure addDDMStructure(
			String className, DDMForm ddmForm)
		throws Exception {

		return addDDMStructure(
			TestPropsValues.getGroupId(), className, ddmForm);
	}

	public static DDMStructure addDDMStructure(
			String className, Locale defaultLocale)
		throws Exception {

		return addDDMStructure(
			TestPropsValues.getGroupId(), className, 0,
			getSampleDDMForm(
				"name", new Locale[] {LocaleUtil.US}, defaultLocale),
			defaultLocale, ServiceContextTestUtil.getServiceContext());
	}

	public static DDMTemplate addDDMTemplate(long structureId)
		throws Exception {

		return addDDMTemplate(
			TestPropsValues.getGroupId(), structureId,
			TemplateConstants.LANG_TYPE_VM, _SAMPLE_VM_TEMPLATE_SCRIPT);
	}

	public static DDMTemplate addDDMTemplate(long groupId, long structureId)
		throws Exception {

		return addDDMTemplate(
			groupId, structureId, TemplateConstants.LANG_TYPE_VM,
			_SAMPLE_VM_TEMPLATE_SCRIPT);
	}

	public static DDMTemplate addDDMTemplate(
			long groupId, long structureId, Locale defaultLocale)
		throws Exception {

		return addDDMTemplate(
			groupId, structureId, TemplateConstants.LANG_TYPE_VM,
			_SAMPLE_VM_TEMPLATE_SCRIPT, defaultLocale);
	}

	public static DDMTemplate addDDMTemplate(
			long groupId, long classNameId, long classPK)
		throws Exception {

		return addDDMTemplate(
			groupId, classNameId, classPK, TemplateConstants.LANG_TYPE_VM,
			_SAMPLE_VM_TEMPLATE_SCRIPT, LocaleUtil.getSiteDefault());
	}

	public static DDMTemplate addDDMTemplate(
			long groupId, long classNameId, long classPK, String language,
			String script, Locale defaultLocale)
		throws Exception {

		Map<Locale, String> nameMap = new HashMap<>();

		nameMap.put(defaultLocale, "Test Template");

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return DDMTemplateLocalServiceUtil.addTemplate(
			TestPropsValues.getUserId(), groupId, classNameId, classPK, 0,
			nameMap, null, DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null,
			language, script, serviceContext);
	}

	public static DDMTemplate addDDMTemplate(
			long groupId, long structureId, String language, String script)
		throws Exception {

		return addDDMTemplate(
			groupId, PortalUtil.getClassNameId(DDMStructure.class), structureId,
			language, script, LocaleUtil.getSiteDefault());
	}

	public static DDMTemplate addDDMTemplate(
			long groupId, long structureId, String language, String script,
			Locale defaultLocale)
		throws Exception {

		return addDDMTemplate(
			groupId, PortalUtil.getClassNameId(DDMStructure.class), structureId,
			language, script, defaultLocale);
	}

	public static DDMForm getSampleDDMForm() {
		return getSampleDDMForm("name");
	}

	public static DDMForm getSampleDDMForm(
		Locale[] availableLocales, Locale defaultLocale) {

		return getSampleDDMForm("name", availableLocales, defaultLocale);
	}

	public static DDMForm getSampleDDMForm(String name) {
		return getSampleDDMForm(
			name, new Locale[] {LocaleUtil.US}, LocaleUtil.US);
	}

	public static DDMForm getSampleDDMForm(
		String name, Locale[] availableLocales, Locale defaultLocale) {

		return getSampleDDMForm(
			name, "string", "text", true, "text", availableLocales,
			defaultLocale);
	}

	public static DDMForm getSampleDDMForm(
		String name, String dataType, String indexType, boolean repeatable,
		String type, Locale[] availableLocales, Locale defaultLocale) {

		DDMForm ddmForm = new DDMForm();

		ddmForm.setAvailableLocales(SetUtil.fromArray(availableLocales));
		ddmForm.setDefaultLocale(defaultLocale);

		DDMFormField ddmFormField = new DDMFormField(name, type);

		ddmFormField.setDataType(dataType);
		ddmFormField.setIndexType(indexType);
		ddmFormField.setLocalizable(true);
		ddmFormField.setRepeatable(repeatable);

		LocalizedValue label = new LocalizedValue(defaultLocale);

		label.addString(defaultLocale, "Field");

		ddmFormField.setLabel(label);

		ddmForm.addDDMFormField(ddmFormField);

		return ddmForm;
	}

	protected static DDMForm toDDMForm(String definition) throws Exception {
		DDMXMLUtil.validateXML(definition);

		return DDMFormXSDDeserializerUtil.deserialize(definition);
	}

	private static final String _SAMPLE_VM_TEMPLATE_SCRIPT = "$name.getData()";

}