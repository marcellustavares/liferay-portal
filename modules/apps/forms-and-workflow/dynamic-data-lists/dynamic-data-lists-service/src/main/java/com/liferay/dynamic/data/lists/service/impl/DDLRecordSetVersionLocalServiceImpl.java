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

package com.liferay.dynamic.data.lists.service.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.dynamic.data.lists.exception.NoSuchRecordSetVersionException;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetVersion;
import com.liferay.dynamic.data.lists.service.base.DDLRecordSetVersionLocalServiceBaseImpl;
import com.liferay.dynamic.data.lists.util.comparator.DDLRecordSetVersionVersionComparator;
import com.liferay.dynamic.data.mapping.exception.NoSuchStructureVersionException;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides the local service for accessing, adding, deleting, and updating
 * dynamic data list (DDL) record set versions.
 *
 * @author Leonardo Barros
 */
@ProviderType
public class DDLRecordSetVersionLocalServiceImpl
	extends DDLRecordSetVersionLocalServiceBaseImpl {

	@Override
	public void deleteByRecordSetId(long recordSetId) throws PortalException {
		DDLRecordSet ddlRecordSet = null;

		List<DDLRecordSetVersion> ddlRecordSetVersions =
			ddlRecordSetVersionPersistence.findByRecordSetId(recordSetId);

		Set<Long> ddmStructureVersionIds = new HashSet<>();

		//Record set versions

		for (DDLRecordSetVersion ddlRecordSetVersion : ddlRecordSetVersions) {
			if (ddlRecordSet == null) {
				ddlRecordSet = ddlRecordSetVersion.getDDLRecordSet();
			}

			ddmStructureVersionIds.add(
				ddlRecordSetVersion.getDDMStructureVersionId());

			ddlRecordSetVersionPersistence.remove(ddlRecordSetVersion);
		}

		//Structure versions

		for (Long ddmStructureVersionId : ddmStructureVersionIds) {
			ddmStructureVersionLocalService.deleteDDMStructureVersion(
				ddmStructureVersionId);
		}

		DDMStructure ddmStructure = ddlRecordSet.getDDMStructure();

		try {
			ddmStructureVersionLocalService.getLatestStructureVersion(
				ddmStructure.getStructureId());
		}
		catch (NoSuchStructureVersionException nssve) {
			ddmStructureLocalService.deleteDDMStructure(ddmStructure);
		}
	}

	@Override
	public DDLRecordSetVersion getDDLRecordSetVersion(
			long recordSetId, String version)
		throws PortalException {

		return ddlRecordSetVersionPersistence.findByRS_V(recordSetId, version);
	}

	@Override
	public List<DDLRecordSetVersion> getDDLRecordSetVersions(long recordSetId) {
		return ddlRecordSetVersionPersistence.findByRecordSetId(recordSetId);
	}

	@Override
	public List<DDLRecordSetVersion> getDDLRecordSetVersions(
		long recordSetId, int start, int end,
		OrderByComparator<DDLRecordSetVersion> orderByComparator) {

		return ddlRecordSetVersionPersistence.findByRecordSetId(
			recordSetId, start, end, orderByComparator);
	}

	@Override
	public int getDDLRecordSetVersionsCount(long recordSetId) {
		return ddlRecordSetVersionPersistence.countByRecordSetId(recordSetId);
	}

	@Override
	public DDLRecordSetVersion getDLLRecordSetVersion(long recordSetVersionId)
		throws PortalException {

		return ddlRecordSetVersionPersistence.findByPrimaryKey(
			recordSetVersionId);
	}

	@Override
	public DDLRecordSetVersion getLatestRecordSetVersion(long recordSetId)
		throws PortalException {

		List<DDLRecordSetVersion> recordSetVersions =
			ddlRecordSetVersionPersistence.findByRecordSetId(recordSetId);

		if (recordSetVersions.isEmpty()) {
			throw new NoSuchRecordSetVersionException(
				"No record ser versions found for record set ID " +
					recordSetId);
		}

		recordSetVersions = ListUtil.copy(recordSetVersions);

		Collections.sort(
			recordSetVersions, new DDLRecordSetVersionVersionComparator());

		return recordSetVersions.get(0);
	}

	@ServiceReference(type = DDMStructureLocalService.class)
	protected DDMStructureLocalService ddmStructureLocalService;

	@ServiceReference(type = DDMStructureVersionLocalService.class)
	protected DDMStructureVersionLocalService ddmStructureVersionLocalService;

}