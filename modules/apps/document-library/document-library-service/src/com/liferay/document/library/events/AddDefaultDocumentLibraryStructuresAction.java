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

package com.liferay.document.library.events;

import com.liferay.document.library.listeners.DDMStructureModelListener;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.metadata.RawMetadataProcessorUtil;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntryMetadata;
import com.liferay.portlet.documentlibrary.model.DLFileEntryTypeConstants;
import com.liferay.portlet.documentlibrary.util.RawMetadataProcessor;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormXSDDeserializer;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalService;
import com.liferay.portlet.dynamicdatamapping.storage.StorageType;
import com.liferay.portlet.dynamicdatamapping.util.DDM;
import com.liferay.portlet.dynamicdatamapping.util.DefaultDDMStructureUtil;

import java.io.StringReader;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 * @author Miguel Pastor
 * @author Roberto Díaz
 */
@Component(
	immediate = true, service = AddDefaultDocumentLibraryStructuresAction.class
)
public class AddDefaultDocumentLibraryStructuresAction extends SimpleAction {

	@Override
	public void run(String[] ids) throws ActionException {
		try {
			doRun(GetterUtil.getLong(ids[0]));
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

	@Activate
	protected void activate() throws ActionException {
		Long companyId = CompanyThreadLocal.getCompanyId();

		try {
			List<Company> companies = _companyLocalService.getCompanies();

			for (Company company : companies) {
				CompanyThreadLocal.setCompanyId(company.getCompanyId());

				run(new String[] {String.valueOf(company.getCompanyId())});
			}
		}
		finally {
			CompanyThreadLocal.setCompanyId(companyId);
		}
	}

	protected void addDLFileEntryTypeDDMStructure(
			long userId, long groupId, String ddmStructureName,
			String ddmStructureKey, List<String> ddmStructureFragmentNames)
		throws Exception {

		List<Long> ddmStructureFragmentIds = new ArrayList<>();

		for (String ddmStructureFragmentName : ddmStructureFragmentNames) {
			String ddmStructureFragmentKey = ddmStructureFragmentName;

			DDMStructure ddmStructureFragment =
				_ddmStructureLocalService.fetchStructure(
					groupId,
					PortalUtil.getClassNameId(DLFileEntryMetadata.class),
					ddmStructureFragmentKey);

			if (ddmStructureFragment == null) {
				continue;
			}

			ddmStructureFragmentIds.add(ddmStructureFragment.getStructureId());
		}

		Locale locale = PortalUtil.getSiteDefaultLocale(groupId);

		String definition =
			DefaultDDMStructureUtil.getDynamicDDMStructureDefinition(
				AddDefaultDocumentLibraryStructuresAction.class.
					getClassLoader(),
				"com/liferay/document/library/events/dependencies" +
					"/document-library-structures.xml",
				ddmStructureName, locale);

		DDMStructure ddmStructure = _ddmStructureLocalService.fetchStructure(
			groupId, PortalUtil.getClassNameId(DLFileEntryMetadata.class),
			ddmStructureKey);

		if (ddmStructure != null) {
			return;
		}

		Map<Locale, String> localizationMap = getLocalizationMap(
			ddmStructureName);

		DDMForm ddmForm = _ddmFormXSDDeserializer.deserialize(definition);

		ServiceContext serviceContext = createServiceContext();

		serviceContext.setAttribute(
			"structureFragmentIds",
			ArrayUtil.toLongArray(ddmStructureFragmentIds));

		_ddmStructureLocalService.addStructure(
			userId, groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
			PortalUtil.getClassNameId(DLFileEntryMetadata.class),
			ddmStructureKey, localizationMap, localizationMap, ddmForm,
			_ddm.getDefaultDDMFormLayout(ddmForm), StorageType.JSON.toString(),
			DDMStructureConstants.TYPE_DEFAULT, serviceContext);
	}

	protected void addDLFileEntryTypesDDMStructures(long userId, long groupId)
		throws Exception {

		List<String> ddmStructureFragmentNames = new ArrayList<>();

		addDLFileEntryTypeDDMStructure(
			userId, groupId, DLFileEntryTypeConstants.NAME_CONTRACT,
			DLFileEntryTypeConstants.FILE_ENTRY_TYPE_KEY_CONTRACT,
			ddmStructureFragmentNames);

		ddmStructureFragmentNames.clear();

		ddmStructureFragmentNames.add("Marketing Campaign Theme Metadata");

		addDLFileEntryTypeDDMStructure(
			userId, groupId, DLFileEntryTypeConstants.NAME_MARKETING_BANNER,
			DLFileEntryTypeConstants.FILE_ENTRY_TYPE_KEY_MARKETING_BANNER,
			ddmStructureFragmentNames);

		ddmStructureFragmentNames.clear();

		ddmStructureFragmentNames.add("Learning Module Metadata");

		addDLFileEntryTypeDDMStructure(
			userId, groupId, DLFileEntryTypeConstants.NAME_ONLINE_TRAINING,
			DLFileEntryTypeConstants.FILE_ENTRY_TYPE_KEY_ONLINE_TRAINING,
			ddmStructureFragmentNames);

		ddmStructureFragmentNames.clear();

		ddmStructureFragmentNames.add("Meeting Metadata");

		addDLFileEntryTypeDDMStructure(
			userId, groupId, DLFileEntryTypeConstants.NAME_SALES_PRESENTATION,
			DLFileEntryTypeConstants.FILE_ENTRY_TYPE_KEY_SALES_PRESENTATION,
			ddmStructureFragmentNames);

		if (UpgradeProcessUtil.isCreateIGImageDocumentType()) {
			addDLFileEntryTypeDDMStructure(
				userId, groupId, DLFileEntryTypeConstants.NAME_IG_IMAGE,
				DLFileEntryTypeConstants.FILE_ENTRY_TYPE_KEY_IG_IMAGE,
				ddmStructureFragmentNames);
		}
	}

	protected void addDLRawMetadataStructures(
			long userId, long groupId, ServiceContext serviceContext)
		throws Exception {

		Locale locale = PortalUtil.getSiteDefaultLocale(groupId);

		String xsd = buildDLRawMetadataXML(
			RawMetadataProcessorUtil.getFields(), locale);

		Document document = UnsecureSAXReaderUtil.read(new StringReader(xsd));

		Element rootElement = document.getRootElement();

		List<Element> structureElements = rootElement.elements("structure");

		for (Element structureElement : structureElements) {
			String name = structureElement.elementText("name");
			String description = structureElement.elementText("description");

			Element structureElementRootElement = structureElement.element(
				"root");

			String structureElementRootXML =
				structureElementRootElement.asXML();

			DDMStructure ddmStructure =
				_ddmStructureLocalService.fetchStructure(
					groupId,
					PortalUtil.getClassNameId(RawMetadataProcessor.class),
					name);

			DDMForm ddmForm = _ddmFormXSDDeserializer.deserialize(
				structureElementRootXML);

			if (ddmStructure != null) {
				ddmStructure.setDDMForm(ddmForm);

				_ddmStructureLocalService.updateDDMStructure(ddmStructure);
			}
			else {
				Map<Locale, String> nameMap = new HashMap<>();

				nameMap.put(locale, name);

				Map<Locale, String> descriptionMap = new HashMap<>();

				descriptionMap.put(locale, description);

				DDMFormLayout ddmFormLayout = _ddm.getDefaultDDMFormLayout(
					ddmForm);

				_ddmStructureLocalService.addStructure(
					userId, groupId,
					DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
					PortalUtil.getClassNameId(RawMetadataProcessor.class), name,
					nameMap, descriptionMap, ddmForm, ddmFormLayout,
					StorageType.JSON.toString(),
					DDMStructureConstants.TYPE_DEFAULT, serviceContext);
			}
		}
	}

	protected String buildDLRawMetadataElementXML(Field field, Locale locale) {
		StringBundler sb = new StringBundler(15);

		sb.append("<dynamic-element dataType=\"string\" indexType=\"text\" ");
		sb.append("name=\"");

		Class<?> fieldClass = field.getDeclaringClass();

		sb.append(fieldClass.getSimpleName());
		sb.append(StringPool.UNDERLINE);
		sb.append(field.getName());
		sb.append("\" required=\"false\" showLabel=\"true\" type=\"text\">");
		sb.append("<meta-data locale=\"");
		sb.append(locale);
		sb.append("\">");
		sb.append("<entry name=\"label\"><![CDATA[metadata.");
		sb.append(fieldClass.getSimpleName());
		sb.append(StringPool.PERIOD);
		sb.append(field.getName());
		sb.append("]]></entry><entry name=\"predefinedValue\">");
		sb.append("<![CDATA[]]></entry></meta-data></dynamic-element>");

		return sb.toString();
	}

	protected String buildDLRawMetadataStructureXML(
		String name, Field[] fields, Locale locale) {

		StringBundler sb = new StringBundler(12 + fields.length);

		sb.append("<structure><name><![CDATA[");
		sb.append(name);
		sb.append("]]></name>");
		sb.append("<description><![CDATA[");
		sb.append(name);
		sb.append("]]></description>");
		sb.append("<root available-locales=\"");
		sb.append(locale);
		sb.append("\" default-locale=\"");
		sb.append(locale);
		sb.append("\">");

		for (Field field : fields) {
			sb.append(buildDLRawMetadataElementXML(field, locale));
		}

		sb.append("</root></structure>");

		return sb.toString();
	}

	protected String buildDLRawMetadataXML(
		Map<String, Field[]> fields, Locale locale) {

		StringBundler sb = new StringBundler(2 + fields.size());

		sb.append("<?xml version=\"1.0\"?><root>");

		for (String key : fields.keySet()) {
			sb.append(
				buildDLRawMetadataStructureXML(key, fields.get(key), locale));
		}

		sb.append("</root>");

		return sb.toString();
	}

	protected ServiceContext createServiceContext() {
		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGuestPermissions(true);
		serviceContext.setAddGroupPermissions(true);

		return serviceContext;
	}

	protected void doRun(long companyId) throws Exception {
		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGuestPermissions(true);
		serviceContext.setAddGroupPermissions(true);

		Group group = _groupLocalService.getCompanyGroup(companyId);

		serviceContext.setScopeGroupId(group.getGroupId());

		long defaultUserId = _userLocalService.getDefaultUserId(companyId);

		serviceContext.setAttribute(
			"type", DDMStructureConstants.TYPE_FRAGMENT);
		serviceContext.setUserId(defaultUserId);

		DefaultDDMStructureUtil.addDDMStructures(
			defaultUserId, group.getGroupId(),
			PortalUtil.getClassNameId(DLFileEntryMetadata.class),
			AddDefaultDocumentLibraryStructuresAction.class.getClassLoader(),
			"com/liferay/document/library/events/dependencies" +
				"/document-library-structures.xml",
			serviceContext);

		serviceContext.removeAttribute("type");

		addDLFileEntryTypesDDMStructures(defaultUserId, group.getGroupId());
		addDLRawMetadataStructures(
			defaultUserId, group.getGroupId(), serviceContext);
	}

	protected Map<Locale, String> getLocalizationMap(String content) {
		Map<Locale, String> localizationMap = new HashMap<>();

		Locale defaultLocale = LocaleUtil.getDefault();

		String defaultValue = LanguageUtil.get(defaultLocale, content);

		for (Locale locale : LanguageUtil.getSupportedLocales()) {
			String value = LanguageUtil.get(locale, content);

			if (!locale.equals(defaultLocale) && value.equals(defaultValue)) {
				continue;
			}

			localizationMap.put(locale, value);
		}

		return localizationMap;
	}

	@Reference
	protected void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	@Reference
	protected void setDDM(DDM ddm) {
		_ddm = ddm;
	}

	@Reference
	protected void setDDMFormXSDDeserializer(
		DDMFormXSDDeserializer ddmFormXSDDeserializer) {

		_ddmFormXSDDeserializer = ddmFormXSDDeserializer;
	}

	@Reference
	protected void setDDMStructureLocalService(
		DDMStructureLocalService ddmStructureLocalService) {

		_ddmStructureLocalService = ddmStructureLocalService;
	}

	@Reference
	protected void setDDMStructureModelListener(
		DDMStructureModelListener ddmStructureModelListener) {
	}

	@Reference
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(target = "(original.bean=true)", unbind = "-")
	protected void setServletContext(ServletContext servletContext) {
	}

	@Reference
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private CompanyLocalService _companyLocalService;
	private DDM _ddm;
	private DDMFormXSDDeserializer _ddmFormXSDDeserializer;
	private DDMStructureLocalService _ddmStructureLocalService;
	private GroupLocalService _groupLocalService;
	private UserLocalService _userLocalService;

}