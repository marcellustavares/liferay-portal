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

import com.liferay.dynamic.data.mapping.model.DDMDataProvider;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.exportimport.staging.permission.StagingPermissionUtil;

/**
 * @author Leonardo Barros
 */
public class DDMDataProviderPermission {

	public static void check(
			PermissionChecker permissionChecker, long dataProviderId,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, dataProviderId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, DDMDataProvider.class.getName(),
				dataProviderId, actionId);
		}
	}

	public static boolean contains(
		PermissionChecker permissionChecker, DDMDataProvider dataProvider,
		String actionId) {

		String portletId = PortletProviderUtil.getPortletId(
			DDMDataProvider.class.getName(), PortletProvider.Action.EDIT);

		Boolean hasPermission = StagingPermissionUtil.hasPermission(
			permissionChecker, dataProvider.getGroupId(),
			DDMDataProvider.class.getName(), dataProvider.getDataProviderId(),
			portletId, actionId);

		if (hasPermission != null) {
			return hasPermission.booleanValue();
		}

		if (permissionChecker.hasOwnerPermission(
				dataProvider.getCompanyId(), DDMDataProvider.class.getName(),
				dataProvider.getDataProviderId(), dataProvider.getUserId(),
				actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			dataProvider.getGroupId(), DDMDataProvider.class.getName(),
			dataProvider.getDataProviderId(), actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long dataProviderId,
			String actionId)
		throws PortalException {

		DDMDataProvider dataProvider =
			DDMDataProviderLocalServiceUtil.getDDMDataProvider(dataProviderId);

		return contains(permissionChecker, dataProvider, actionId);
	}

}