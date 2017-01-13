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

package com.liferay.portal.workflow.kaleo.service.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.workflow.kaleo.constants.KaleoDefinitionActionKeys;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion;
import com.liferay.portal.workflow.kaleo.service.base.KaleoDefinitionVersionServiceBaseImpl;
import com.liferay.portal.workflow.kaleo.service.permission.KaleoDefinitionPermission;
import com.liferay.portal.workflow.kaleo.service.permission.KaleoDefinitionVersionPermission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Inácio Nery
 */
@ProviderType
public class KaleoDefinitionVersionServiceImpl
	extends KaleoDefinitionVersionServiceBaseImpl {

	@Override
	public KaleoDefinitionVersion addKaleoDefinitionVersion(
			long userId, long groupId, String name,
			Map<Locale, String> titleMap, String content, int version,
			ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionPermission.check(
			getPermissionChecker(), groupId,
			KaleoDefinitionActionKeys.ADD_DRAFT);

		return kaleoDefinitionVersionLocalService.addKaleoDefinitionVersion(
			userId, groupId, name, titleMap, content, version, serviceContext);
	}

	@Override
	public void deleteKaleoDefinitionVersions(
			String name, ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionVersion kaleoDefinitionVersion =
			kaleoDefinitionVersionLocalService.getLatestKaleoDefinitionVersion(
				name, serviceContext);

		KaleoDefinitionVersionPermission.check(
			getPermissionChecker(), kaleoDefinitionVersion, ActionKeys.DELETE);

		kaleoDefinitionVersionLocalService.deleteKaleoDefinitionVersions(
			name, serviceContext);
	}

	@Override
	public KaleoDefinitionVersion getKaleoDefinitionVersion(
			String name, int version, ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionVersion kaleoDefinitionVersion =
			kaleoDefinitionVersionLocalService.getKaleoDefinitionVersion(
				name, version, serviceContext);

		KaleoDefinitionVersionPermission.check(
			getPermissionChecker(), kaleoDefinitionVersion, ActionKeys.VIEW);

		return kaleoDefinitionVersion;
	}

	@Override
	public List<KaleoDefinitionVersion> getKaleoDefinitionVersions()
		throws PortalException {

		List<KaleoDefinitionVersion> kaleoDefinitionVersions =
			kaleoDefinitionVersionLocalService.getKaleoDefinitionVersions(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		return filterKaleoDefinitionVersions(
			kaleoDefinitionVersions, ActionKeys.VIEW);
	}

	@Override
	public KaleoDefinitionVersion getLatestKaleoDefinitionVersion(
			String name, ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionVersion latestKaleoDefinitionVersion =
			kaleoDefinitionVersionLocalService.getLatestKaleoDefinitionVersion(
				name, serviceContext);

		KaleoDefinitionVersionPermission.check(
			getPermissionChecker(), latestKaleoDefinitionVersion,
			ActionKeys.VIEW);

		return latestKaleoDefinitionVersion;
	}

	@Override
	public List<KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
			long companyId, int start, int end,
			OrderByComparator<KaleoDefinitionVersion> orderByComparator)
		throws PortalException {

		List<KaleoDefinitionVersion> latestKaleoDefinitionVersions =
			kaleoDefinitionVersionLocalService.getLatestKaleoDefinitionVersions(
				companyId, start, end, orderByComparator);

		return filterKaleoDefinitionVersions(
			latestKaleoDefinitionVersions, ActionKeys.VIEW);
	}

	@Override
	public List<KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
			long companyId, String keywords, int start, int end,
			OrderByComparator<KaleoDefinitionVersion> orderByComparator)
		throws PortalException {

		List<KaleoDefinitionVersion> latestKaleoDefinitionVersions =
			kaleoDefinitionVersionLocalService.getLatestKaleoDefinitionVersions(
				companyId, keywords, start, end, orderByComparator);

		return filterKaleoDefinitionVersions(
			latestKaleoDefinitionVersions, ActionKeys.VIEW);
	}

	@Override
	public KaleoDefinitionVersion publishKaleoDefinitionVersion(
			long userId, long groupId, String name,
			Map<Locale, String> titleMap, String content,
			ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionPermission.check(
			getPermissionChecker(), groupId, KaleoDefinitionActionKeys.PUBLISH);

		return kaleoDefinitionVersionLocalService.publishKaleoDefinitionVersion(
			userId, groupId, name, titleMap, content, serviceContext);
	}

	@Override
	public KaleoDefinitionVersion updateKaleoDefinitionVersion(
			long userId, String name, Map<Locale, String> titleMap,
			String content, ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionVersion latestKaleoDefinitionVersion =
			getLatestKaleoDefinitionVersion(name, serviceContext);

		KaleoDefinitionVersionPermission.check(
			getPermissionChecker(), latestKaleoDefinitionVersion,
			ActionKeys.UPDATE);

		return kaleoDefinitionVersionLocalService.updateKaleoDefinitionVersion(
			userId, name, titleMap, content, serviceContext);
	}

	protected List<KaleoDefinitionVersion> filterKaleoDefinitionVersions(
			List<KaleoDefinitionVersion> kaleoDefinitionVersions,
			String actionId)
		throws PrincipalException {

		List<KaleoDefinitionVersion> filteredKaleoDefinitionVersions =
			new ArrayList<>();

		for (KaleoDefinitionVersion kaleoDefinitionVersion :
				kaleoDefinitionVersions) {

			if (KaleoDefinitionVersionPermission .contains(
					getPermissionChecker(), kaleoDefinitionVersion, actionId)) {

				filteredKaleoDefinitionVersions.add(kaleoDefinitionVersion);
			}
		}

		return Collections.unmodifiableList(filteredKaleoDefinitionVersions);
	}

}