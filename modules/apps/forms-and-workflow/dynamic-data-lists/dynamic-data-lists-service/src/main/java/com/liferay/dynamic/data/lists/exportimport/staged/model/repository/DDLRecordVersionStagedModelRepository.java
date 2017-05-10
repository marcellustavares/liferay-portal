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

import com.liferay.dynamic.data.lists.model.DDLRecordVersion;
import com.liferay.dynamic.data.lists.service.DDLRecordVersionLocalService;
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
		"model.class.name=com.liferay.dynamic.data.lists.model.DDLRecordVersion"
	},
	service = {
		DDLRecordVersionStagedModelRepository.class, StagedModelRepository.class
	}
)
public class DDLRecordVersionStagedModelRepository
	extends BaseStagedModelRepository<DDLRecordVersion> {

	@Override
	public DDLRecordVersion addStagedModel(
			PortletDataContext portletDataContext,
			DDLRecordVersion recordVersion)
		throws PortalException {

		return _ddlRecordVersionLocalService.addDDLRecordVersion(recordVersion);
	}

	@Override
	public void deleteStagedModel(DDLRecordVersion recordVersion)
		throws PortalException {

		_ddlRecordVersionLocalService.deleteDDLRecordVersion(recordVersion);
	}

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException {

		DDLRecordVersion recordVersion = fetchStagedModelByUuidAndGroupId(
			uuid, groupId);

		if (recordVersion != null) {
			deleteStagedModel(recordVersion);
		}
	}

	@Override
	public void deleteStagedModels(PortletDataContext portletDataContext)
		throws PortalException {

		List<DDLRecordVersion> recordVersions =
			_ddlRecordVersionLocalService.findByG_C(
				portletDataContext.getScopeGroupId(),
				portletDataContext.getCompanyId());

		for (DDLRecordVersion recordVersion : recordVersions) {
			deleteStagedModel(recordVersion);
		}
	}

	@Override
	public DDLRecordVersion fetchStagedModelByUuidAndGroupId(
		String uuid, long groupId) {

		return _ddlRecordVersionLocalService.
			fetchDDLRecordVersionByUuidAndGroupId(uuid, groupId);
	}

	@Override
	public List<DDLRecordVersion> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		return _ddlRecordVersionLocalService.
			getDDLRecordVersionsByUuidAndCompanyId(uuid, companyId);
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext) {

		return _ddlRecordVersionLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public DDLRecordVersion saveStagedModel(DDLRecordVersion recordVersion)
		throws PortalException {

		return _ddlRecordVersionLocalService.updateDDLRecordVersion(
			recordVersion);
	}

	@Override
	public DDLRecordVersion updateStagedModel(
			PortletDataContext portletDataContext,
			DDLRecordVersion recordVersion)
		throws PortalException {

		return _ddlRecordVersionLocalService.updateDDLRecordVersion(
			recordVersion);
	}

	@Reference
	private DDLRecordVersionLocalService _ddlRecordVersionLocalService;

}