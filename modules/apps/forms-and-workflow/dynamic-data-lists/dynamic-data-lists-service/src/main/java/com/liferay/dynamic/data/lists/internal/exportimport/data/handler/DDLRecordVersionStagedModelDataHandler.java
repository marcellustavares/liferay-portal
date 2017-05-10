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

package com.liferay.dynamic.data.lists.internal.exportimport.data.handler;

import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetVersion;
import com.liferay.dynamic.data.lists.model.DDLRecordVersion;
import com.liferay.dynamic.data.lists.service.DDLRecordSetVersionLocalService;
import com.liferay.exportimport.kernel.lar.ExportImportPathUtil;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.lar.BaseStagedModelDataHandler;
import com.liferay.exportimport.staged.model.repository.StagedModelRepository;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true, service = StagedModelDataHandler.class)
public class DDLRecordVersionStagedModelDataHandler
	extends BaseStagedModelDataHandler<DDLRecordVersion> {

	@Override
	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			DDLRecordVersion recordVersion)
		throws Exception {

		DDLRecordSet recordSet = recordVersion.getRecordSet();

		StagedModelDataHandlerUtil.exportReferenceStagedModel(
			portletDataContext, recordVersion, recordSet,
			PortletDataContext.REFERENCE_TYPE_STRONG);

		DDLRecordSetVersion recordSetVersion =
			_ddlRecordSetVersionLocalService.getRecordSetVersion(
				recordSet.getRecordSetId(),
				recordVersion.getRecordSetVersion());

		StagedModelDataHandlerUtil.exportReferenceStagedModel(
			portletDataContext, recordVersion, recordSetVersion,
			PortletDataContext.REFERENCE_TYPE_STRONG);

		Element recordVersionElement = portletDataContext.getExportDataElement(
			recordVersion);

		portletDataContext.addClassedModel(
			recordVersionElement,
			ExportImportPathUtil.getModelPath(recordVersion), recordVersion);
	}

	@Override
	protected void doImportMissingReference(
			PortletDataContext portletDataContext, String uuid, long groupId,
			long recordVersionId)
		throws Exception {

		DDLRecordVersion existingRecordVersion = fetchMissingReference(
			uuid, groupId);

		Map<Long, Long> recordVersionIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDLRecordVersion.class);

		recordVersionIds.put(
			recordVersionId, existingRecordVersion.getRecordVersionId());
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			DDLRecordVersion recordVersion)
		throws Exception {

		Map<Long, Long> recordSetIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDLRecordSet.class);

		long recordSetId = MapUtil.getLong(
			recordSetIds, recordVersion.getRecordSetId(),
			recordVersion.getRecordSetId());

		Map<Long, Long> recordIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDLRecord.class);

		long recordId = MapUtil.getLong(
			recordIds, recordVersion.getRecordId(),
			recordVersion.getRecordId());

		DDLRecordVersion importedRecordVersion =
			(DDLRecordVersion)recordVersion.clone();

		importedRecordVersion.setGroupId(portletDataContext.getScopeGroupId());
		importedRecordVersion.setRecordId(recordId);
		importedRecordVersion.setRecordSetId(recordSetId);

		DDLRecordVersion existingRecordVersion =
			_stagedModelRepository.fetchStagedModelByUuidAndGroupId(
				recordVersion.getUuid(), portletDataContext.getScopeGroupId());

		if ((existingRecordVersion == null) ||
			!portletDataContext.isDataStrategyMirror()) {

			importedRecordVersion = _stagedModelRepository.addStagedModel(
				portletDataContext, importedRecordVersion);
		}
		else {
			importedRecordVersion = _stagedModelRepository.updateStagedModel(
				portletDataContext, importedRecordVersion);
		}

		portletDataContext.importClassedModel(
			recordVersion, importedRecordVersion);
	}

	@Reference(unbind = "-")
	protected void setDDLRecordSetVersionLocalService(
		DDLRecordSetVersionLocalService ddlRecordSetVersionLocalService) {

		_ddlRecordSetVersionLocalService = ddlRecordSetVersionLocalService;
	}

	@Reference(
		target = "(model.class.name=com.liferay.dynamic.data.lists.model.DDLRecordVersion)",
		unbind = "-"
	)
	protected void setStagedModelRepository(
		StagedModelRepository<DDLRecordVersion> stagedModelRepository) {

		_stagedModelRepository = stagedModelRepository;
	}

	private static final String[] _CLASS_NAMES = {
		DDLRecordVersion.class.getName()
	};

	private DDLRecordSetVersionLocalService _ddlRecordSetVersionLocalService;
	private StagedModelRepository<DDLRecordVersion> _stagedModelRepository;

}