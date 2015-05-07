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

package com.liferay.portlet.dynamicdatamapping.service.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction;
import com.liferay.portlet.dynamicdatamapping.service.base.DDMStructureRestrictionLocalServiceBaseImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julio Camarero
 */
@ProviderType
public class DDMStructureRestrictionLocalServiceImpl
	extends DDMStructureRestrictionLocalServiceBaseImpl {

	@Override
	public DDMStructureRestriction addStructureRestricion(
		String className, long classPK, long structureId) {

		long structureRestrictionId = counterLocalService.increment();

		DDMStructureRestriction structureRestriction =
			ddmStructureRestrictionPersistence.create(structureRestrictionId);

		long classNameId = classNameLocalService.getClassNameId(className);

		structureRestriction.setClassNameId(classNameId);
		structureRestriction.setClassPK(classPK);
		structureRestriction.setStructureId(structureId);

		ddmStructureRestrictionPersistence.update(structureRestriction);

		return structureRestriction;
	}

	@Override
	public DDMStructureRestriction deleteStructureRestriction(
			long structureRestrictionId)
		throws PortalException {

		return deleteDDMStructureRestriction(structureRestrictionId);
	}

	@Override
	public DDMStructureRestriction deleteStructureRestriction(
			String className, long classPk, long ddmStructureId)
		throws PortalException {

		long classNameId = classNameLocalService.getClassNameId(className);

		DDMStructureRestriction ddmStructureRestriction =
			ddmStructureRestrictionPersistence.fetchByC_C_S(
				classNameId, classPk, ddmStructureId);

		return deleteDDMStructureRestriction(
			ddmStructureRestriction.getStructureRestrictionId());
	}

	@Override
	public List<DDMStructureRestriction> getStructureRestrictions(
		String className, long classPK) {

		long classNameId = classNameLocalService.getClassNameId(className);

		return ddmStructureRestrictionPersistence.findByC_C(
			classNameId, classPK);
	}

	@Override
	public List<DDMStructure> getStructures(String className, long classPK)
		throws PortalException {

		List<DDMStructure> structures = new ArrayList<>();

		for (DDMStructureRestriction structureRestriction :
				getStructureRestrictions(className, classPK)) {

			structures.add(structureRestriction.getStructure());
		}

		return structures;
	}

}