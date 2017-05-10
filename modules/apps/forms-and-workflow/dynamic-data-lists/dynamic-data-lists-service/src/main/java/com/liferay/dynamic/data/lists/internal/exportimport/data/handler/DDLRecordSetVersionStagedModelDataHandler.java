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

import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetVersion;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
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
public class DDLRecordSetVersionStagedModelDataHandler
	extends BaseStagedModelDataHandler<DDLRecordSetVersion> {

	@Override
	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			DDLRecordSetVersion recordSetVersion)
		throws Exception {

		Element recordSetVersionElement =
			portletDataContext.getExportDataElement(recordSetVersion);

		DDMStructureVersion ddmStructureVersion =
			recordSetVersion.getDDMStructureVersion();

		StagedModelDataHandlerUtil.exportReferenceStagedModel(
			portletDataContext, recordSetVersion, ddmStructureVersion,
			PortletDataContext.REFERENCE_TYPE_STRONG);

		portletDataContext.addClassedModel(
			recordSetVersionElement,
			ExportImportPathUtil.getModelPath(recordSetVersion),
			recordSetVersion);
	}

	@Override
	protected void doImportMissingReference(
			PortletDataContext portletDataContext, String uuid, long groupId,
			long recordSetVersionId)
		throws Exception {

		DDLRecordSetVersion existingRecordSetVersion = fetchMissingReference(
			uuid, groupId);

		Map<Long, Long> recordSetVersionIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDLRecordSetVersion.class);

		recordSetVersionIds.put(
			recordSetVersionId,
			existingRecordSetVersion.getRecordSetVersionId());
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			DDLRecordSetVersion recordSetVersion)
		throws Exception {

		Map<Long, Long> ddmStructureVersionIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDMStructureVersion.class);

		Map<Long, Long> recordSetIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				DDLRecordSet.class);

		DDLRecordSetVersion importedRecordSetVersion =
			(DDLRecordSetVersion)recordSetVersion.clone();

		importedRecordSetVersion.setGroupId(
			portletDataContext.getScopeGroupId());

		DDLRecordSetVersion existingRecordSetVersion =
			_stagedModelRepository.fetchStagedModelByUuidAndGroupId(
				recordSetVersion.getUuid(),
				portletDataContext.getScopeGroupId());

		if ((existingRecordSetVersion == null) ||
			!portletDataContext.isDataStrategyMirror()) {

			importedRecordSetVersion = _stagedModelRepository.addStagedModel(
				portletDataContext, importedRecordSetVersion);
		}
		else {
			importedRecordSetVersion.setRecordSetVersionId(
				existingRecordSetVersion.getRecordSetVersionId());

			long ddmStructureVersionId = ddmStructureVersionIds.get(
				importedRecordSetVersion.getDDMStructureVersionId());

			importedRecordSetVersion.setDDMStructureVersionId(
				ddmStructureVersionId);

			long recordSetId = MapUtil.getLong(
				recordSetIds, importedRecordSetVersion.getRecordSetId(),
				importedRecordSetVersion.getRecordSetId());

			importedRecordSetVersion.setRecordSetId(recordSetId);

			importedRecordSetVersion = _stagedModelRepository.updateStagedModel(
				portletDataContext, importedRecordSetVersion);
		}
	}

	@Reference(
		target = "(model.class.name=com.liferay.dynamic.data.lists.model.DDLRecordSetVersion)",
		unbind = "-"
	)
	protected void setStagedModelRepository(
		StagedModelRepository<DDLRecordSetVersion> stagedModelRepository) {

		_stagedModelRepository = stagedModelRepository;
	}

	private static final String[] _CLASS_NAMES = {
		DDLRecordSetVersion.class.getName()
	};

	private StagedModelRepository<DDLRecordSetVersion> _stagedModelRepository;

}