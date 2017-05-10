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

package com.liferay.dynamic.data.lists.exportimport.staged.model.repository;

import com.liferay.dynamic.data.lists.model.DDLRecordSetVersion;
import com.liferay.dynamic.data.lists.service.DDLRecordSetVersionLocalService;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.staged.model.repository.StagedModelRepository;
import com.liferay.exportimport.staged.model.repository.base.BaseStagedModelRepository;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = {
		"model.class.name=com.liferay.dynamic.data.lists.model.DDLRecordSetVersion"
	},
	service = {
		DDLRecordSetVersionStagedModelRepository.class,
		StagedModelRepository.class
	}
)
public class DDLRecordSetVersionStagedModelRepository
	extends BaseStagedModelRepository<DDLRecordSetVersion> {

	@Override
	public DDLRecordSetVersion addStagedModel(
			PortletDataContext portletDataContext,
			DDLRecordSetVersion recordSetVersion)
		throws PortalException {

		return _ddlRecordSetVersionLocalService.addDDLRecordSetVersion(
			recordSetVersion);
	}

	@Override
	public void deleteStagedModel(DDLRecordSetVersion recordSetVersion)
		throws PortalException {

		_ddlRecordSetVersionLocalService.deleteDDLRecordSetVersion(
			recordSetVersion);
	}

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException {

		DDLRecordSetVersion recordSetVersion = fetchStagedModelByUuidAndGroupId(
			uuid, groupId);

		if (recordSetVersion != null) {
			deleteStagedModel(recordSetVersion);
		}
	}

	@Override
	public void deleteStagedModels(PortletDataContext portletDataContext)
		throws PortalException {

		List<DDLRecordSetVersion> recordSetVersions =
			_ddlRecordSetVersionLocalService.findByG_C(
				portletDataContext.getScopeGroupId(),
				portletDataContext.getCompanyId());

		for (DDLRecordSetVersion recordSetVersion : recordSetVersions) {
			deleteStagedModel(recordSetVersion);
		}
	}

	@Override
	public DDLRecordSetVersion fetchStagedModelByUuidAndGroupId(
		String uuid, long groupId) {

		return _ddlRecordSetVersionLocalService.
			fetchDDLRecordSetVersionByUuidAndGroupId(uuid, groupId);
	}

	@Override
	public List<DDLRecordSetVersion> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		return _ddlRecordSetVersionLocalService.
			getDDLRecordSetVersionsByUuidAndCompanyId(uuid, companyId);
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext) {

		return _ddlRecordSetVersionLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public DDLRecordSetVersion saveStagedModel(
			DDLRecordSetVersion recordSetVersion)
		throws PortalException {

		return _ddlRecordSetVersionLocalService.updateDDLRecordSetVersion(
			recordSetVersion);
	}

	@Override
	public DDLRecordSetVersion updateStagedModel(
			PortletDataContext portletDataContext,
			DDLRecordSetVersion recordSetVersion)
		throws PortalException {

		return _ddlRecordSetVersionLocalService.updateDDLRecordSetVersion(
			recordSetVersion);
	}

	@Reference
	private DDLRecordSetVersionLocalService _ddlRecordSetVersionLocalService;

}