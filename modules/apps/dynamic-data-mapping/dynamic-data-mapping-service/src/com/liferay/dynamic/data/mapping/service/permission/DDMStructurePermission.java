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

package com.liferay.dynamic.data.mapping.service.permission;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.permission.DDMPermissionSupportTracker.DDMStructurePermissionSupportWrapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.exportimport.staging.permission.StagingPermissionUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(immediate = true, service = DDMStructurePermission.class)
public class DDMStructurePermission {

	public static void check(
			PermissionChecker permissionChecker, DDMStructure structure,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, structure, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker,
				getStructureModelResourceName(structure.getClassNameId()),
				structure.getStructureId(), actionId);
		}
	}

	public static void check(
			PermissionChecker permissionChecker, long groupId, long classNameId,
			String structureKey, String actionId)
		throws PortalException {

		DDMStructure structure = DDMStructureLocalServiceUtil.getStructure(
			groupId, classNameId, structureKey, true);

		check(permissionChecker, structure, actionId);
	}

	public static void check(
			PermissionChecker permissionChecker, long structureId,
			String actionId)
		throws PortalException {

		DDMStructure structure = DDMStructureLocalServiceUtil.getStructure(
			structureId);

		check(permissionChecker, structure, actionId);
	}

	public static void checkAddStruturePermission(
			PermissionChecker permissionChecker, long groupId, long classNameId)
		throws PortalException {

		if (!containsAddStruturePermission(
				permissionChecker, groupId, classNameId)) {

			DDMStructurePermissionSupportWrapper
				structurePermissionSupportWrapper =
					_ddmPermissionSupportTracker.
						getDDMStructurePermissionSupportWrapper(classNameId);

			throw new PrincipalException.MustHavePermission(
				permissionChecker,
				structurePermissionSupportWrapper.getPortletResourceName(),
				groupId, structurePermissionSupportWrapper.getAddActionId());
		}
	}

	public static boolean contains(
			PermissionChecker permissionChecker, DDMStructure structure,
			String actionId)
		throws PortalException {

		return contains(permissionChecker, structure, null, actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, DDMStructure structure,
			String portletId, String actionId)
		throws PortalException {

		String structureModelResourceName = getStructureModelResourceName(
			structure.getClassNameId());

		if (Validator.isNotNull(portletId)) {
			Boolean hasPermission = StagingPermissionUtil.hasPermission(
				permissionChecker, structure.getGroupId(),
				structureModelResourceName, structure.getStructureId(),
				portletId, actionId);

			if (hasPermission != null) {
				return hasPermission.booleanValue();
			}
		}

		if (permissionChecker.hasOwnerPermission(
				structure.getCompanyId(), structureModelResourceName,
				structure.getStructureId(), structure.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			structure.getGroupId(), structureModelResourceName,
			structure.getStructureId(), actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long groupId, long classNameId,
			String structureKey, String actionId)
		throws PortalException {

		DDMStructure structure = DDMStructureLocalServiceUtil.getStructure(
			groupId, classNameId, structureKey, true);

		return contains(permissionChecker, structure, actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long structureId,
			String actionId)
		throws PortalException {

		return contains(permissionChecker, structureId, null, actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long structureId,
			String portletId, String actionId)
		throws PortalException {

		DDMStructure structure = DDMStructureLocalServiceUtil.getStructure(
			structureId);

		return contains(permissionChecker, structure, portletId, actionId);
	}

	public static boolean containsAddStruturePermission(
			PermissionChecker permissionChecker, long groupId, long classNameId)
		throws PortalException {

		DDMStructurePermissionSupportWrapper structurePermissionSupportWrapper =
			_ddmPermissionSupportTracker.
				getDDMStructurePermissionSupportWrapper(classNameId);

		return permissionChecker.hasPermission(
			groupId, structurePermissionSupportWrapper.getPortletResourceName(),
			groupId, structurePermissionSupportWrapper.getAddActionId());
	}

	public static String getStructureModelResourceName(long classNameId)
		throws PortalException {

		DDMStructurePermissionSupportWrapper structurePermissionSupportWrapper =
			_ddmPermissionSupportTracker.
				getDDMStructurePermissionSupportWrapper(classNameId);

		return structurePermissionSupportWrapper.getModelResourceName();
	}

	@Reference
	protected void setDDMPermissionSupportTracker(
		DDMPermissionSupportTracker ddmPermissionSupportTracker) {

		_ddmPermissionSupportTracker = ddmPermissionSupportTracker;
	}

	private static DDMPermissionSupportTracker _ddmPermissionSupportTracker;

}