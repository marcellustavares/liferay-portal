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

package com.liferay.document.library.listeners;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntryMetadata;
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;
import com.liferay.portlet.documentlibrary.service.DLFileEntryTypeLocalService;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	service = {DDMStructureModelListener.class, ModelListener.class}
)
public class DDMStructureModelListener extends BaseModelListener<DDMStructure> {

	@Override
	public void onAfterCreate(DDMStructure ddmStructure)
		throws ModelListenerException {

		if (!isDLFileEntryTypeDDMStructure(ddmStructure)) {
			return;
		}

		try {
			_dlFileEntryTypeLocalService.addFileEntryType(
				ddmStructure.getUserId(), ddmStructure.getGroupId(),
				ddmStructure.getStructureKey(), ddmStructure.getNameMap(),
				ddmStructure.getDescriptionMap(), ddmStructure.getStructureId(),
				createServiceContext());
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

	@Override
	public void onAfterUpdate(DDMStructure ddmStructure)
		throws ModelListenerException {

		if (!isDLFileEntryTypeDDMStructure(ddmStructure)) {
			return;
		}

		try {
			DLFileEntryType dlFileEntryType = getDLFileEntryType(ddmStructure);

			_dlFileEntryTypeLocalService.updateFileEntryType(
				ddmStructure.getUserId(), dlFileEntryType.getFileEntryTypeId(),
				ddmStructure.getNameMap(), ddmStructure.getDescriptionMap(),
				createServiceContext());
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

	protected ServiceContext createServiceContext() {
		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGuestPermissions(true);
		serviceContext.setAddGroupPermissions(true);

		return serviceContext;
	}

	protected DLFileEntryType getDLFileEntryType(DDMStructure ddmStructure)
		throws PortalException {

		return _dlFileEntryTypeLocalService.getFileEntryType(
			ddmStructure.getGroupId(), ddmStructure.getStructureKey());
	}

	protected boolean isDLFileEntryTypeDDMStructure(DDMStructure ddmStructure) {
		long fileEntryMetadataClassNameId = PortalUtil.getClassNameId(
			DLFileEntryMetadata.class);

		if ((ddmStructure.getClassNameId() == fileEntryMetadataClassNameId) &&
			(ddmStructure.getType() == DDMStructureConstants.TYPE_DEFAULT)) {

			return true;
		}

		return false;
	}

	@Reference
	protected void setDLFileEntryTypeLocalService(
		DLFileEntryTypeLocalService dlFileEntryTypeLocalService) {

		_dlFileEntryTypeLocalService = dlFileEntryTypeLocalService;
	}

	private DLFileEntryTypeLocalService _dlFileEntryTypeLocalService;

}