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

package com.liferay.dynamic.data.mapping.web.internal.exportimport.data.handler;

import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalService;
import com.liferay.exportimport.kernel.lar.ExportImportPathUtil;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataException;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = {"javax.portlet.name=" + DDMPortletKeys.DYNAMIC_DATA_MAPPING},
	service = StagedModelDataHandler.class
)
public class DDMStructureVersionStagedModelDataHandler
	extends BaseStagedModelDataHandler<DDMStructureVersion> {

	@Override
	public void deleteStagedModel(DDMStructureVersion structureVersion)
		throws PortalException {

		_ddmStructureVersionLocalService.deleteDDMStructureVersion(
			structureVersion);
	}

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException {

		DDMStructureVersion ddmStructureVersion =
			fetchStagedModelByUuidAndGroupId(uuid, groupId);

		if (ddmStructureVersion != null) {
			deleteStagedModel(ddmStructureVersion);
		}
	}

	@Override
	public DDMStructureVersion fetchStagedModelByUuidAndGroupId(
		String uuid, long groupId) {

		return _ddmStructureVersionLocalService.
			fetchDDMStructureVersionByUuidAndGroupId(uuid, groupId);
	}

	@Override
	public List<DDMStructureVersion> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		return _ddmStructureVersionLocalService.
			getDDMStructureVersionsByUuidAndCompanyId(uuid, companyId);
	}

	@Override
	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	public String getDisplayName(DDMStructureVersion structureVersion) {
		return structureVersion.getNameCurrentValue();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			DDMStructureVersion structureVersion)
		throws Exception {

		Element structureVersionElement =
			portletDataContext.getExportDataElement(structureVersion);

		exportDDMForm(
			portletDataContext, structureVersion, structureVersionElement);

		portletDataContext.addClassedModel(
			structureVersionElement,
			ExportImportPathUtil.getModelPath(structureVersion),
			structureVersion);
	}

	@Override
	protected void doImportMissingReference(
			PortletDataContext portletDataContext, Element referenceElement)
		throws PortletDataException {

		String uuid = referenceElement.attributeValue("uuid");

		Map<Long, Long> groupIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				Group.class);

		long groupId = GetterUtil.getLong(
			referenceElement.attributeValue("group-id"));

		groupId = MapUtil.getLong(groupIds, groupId);

		DDMStructureVersion existingStructureVersion =
			fetchStagedModelByUuidAndGroupId(uuid, groupId);

		Map<Long, Long> structureVersionIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDMStructureVersion.class);

		long structureVersionId = GetterUtil.getLong(
			referenceElement.attributeValue("class-pk"));

		structureVersionIds.put(
			structureVersionId,
			existingStructureVersion.getStructureVersionId());
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			DDMStructureVersion structureVersion)
		throws Exception {

		Element structureVersionElement =
			portletDataContext.getImportDataElement(structureVersion);

		Map<Long, Long> structureIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDMStructure.class);

		Map<Long, Long> structureVersionIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDMStructureVersion.class);

		long parentStructureId = MapUtil.getLong(
			structureIds, structureVersion.getParentStructureId(),
			structureVersion.getParentStructureId());

		long structureId = MapUtil.getLong(
			structureIds, structureVersion.getStructureId(),
			structureVersion.getStructureId());

		long structureVersionId = MapUtil.getLong(
			structureVersionIds, structureVersion.getStructureVersionId(),
			structureVersion.getStructureVersionId());

		DDMForm ddmForm = getImportDDMForm(
			portletDataContext, structureVersionElement);

		DDMStructureVersion importedStructureVersion = null;

		structureVersion.setStructureVersionId(structureVersionId);
		structureVersion.setStructureId(structureId);
		structureVersion.setParentStructureId(parentStructureId);
		structureVersion.setDefinition(
			_ddmFormJSONSerializer.serialize(ddmForm));

		if (portletDataContext.isDataStrategyMirror()) {
			DDMStructureVersion existingStructureVersion =
				fetchStagedModelByUuidAndGroupId(
					structureVersion.getUuid(),
					portletDataContext.getScopeGroupId());

			if (existingStructureVersion == null) {
				importedStructureVersion =
					_ddmStructureVersionLocalService.addDDMStructureVersion(
						structureVersion);
			}
			else {
				importedStructureVersion =
					_ddmStructureVersionLocalService.updateDDMStructureVersion(
						structureVersion);
			}
		}
		else {
			importedStructureVersion =
				_ddmStructureVersionLocalService.addDDMStructureVersion(
					structureVersion);
		}

		portletDataContext.importClassedModel(
			structureVersion, importedStructureVersion);
	}

	protected void exportDDMForm(
		PortletDataContext portletDataContext,
		DDMStructureVersion structureVersion, Element structureElement) {

		String ddmFormPath = ExportImportPathUtil.getModelPath(
			structureVersion, "ddm-form.json");

		structureElement.addAttribute("ddm-form-path", ddmFormPath);

		portletDataContext.addZipEntry(
			ddmFormPath, structureVersion.getDefinition());
	}

	protected DDMForm getImportDDMForm(
			PortletDataContext portletDataContext, Element structureElement)
		throws PortalException {

		String ddmFormPath = structureElement.attributeValue("ddm-form-path");

		String serializedDDMForm = portletDataContext.getZipEntryAsString(
			ddmFormPath);

		return _ddmFormJSONDeserializer.deserialize(serializedDDMForm);
	}

	@Reference(unbind = "-")
	protected void setDDMFormJSONDeserializer(
		DDMFormJSONDeserializer ddmFormJSONDeserializer) {

		_ddmFormJSONDeserializer = ddmFormJSONDeserializer;
	}

	@Reference(unbind = "-")
	protected void setDDMFormJSONSerializer(
		DDMFormJSONSerializer ddmFormJSONSerializer) {

		_ddmFormJSONSerializer = ddmFormJSONSerializer;
	}

	@Reference(unbind = "-")
	protected void setDDMStructureVersionLocalService(
		DDMStructureVersionLocalService ddmStructureVersionLocalService) {

		_ddmStructureVersionLocalService = ddmStructureVersionLocalService;
	}

	private static final String[] _CLASS_NAMES = {
		DDMStructureVersion.class.getName()
	};

	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;
	private DDMFormJSONSerializer _ddmFormJSONSerializer;
	private DDMStructureVersionLocalService _ddmStructureVersionLocalService;

}