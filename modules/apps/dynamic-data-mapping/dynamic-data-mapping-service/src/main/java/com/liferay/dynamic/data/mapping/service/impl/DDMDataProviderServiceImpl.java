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

package com.liferay.dynamic.data.mapping.service.impl;

import com.liferay.dynamic.data.mapping.constants.DDMActionKeys;
import com.liferay.dynamic.data.mapping.model.DDMDataProvider;
import com.liferay.dynamic.data.mapping.service.base.DDMDataProviderServiceBaseImpl;
import com.liferay.dynamic.data.mapping.service.permission.DDMDataProviderPermission;
import com.liferay.dynamic.data.mapping.service.permission.DDMPermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ServiceContext;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 * @author Leonardo Barros
 */
public class DDMDataProviderServiceImpl extends DDMDataProviderServiceBaseImpl {

	@Override
	public DDMDataProvider addDataProvider(
			long groupId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, String data, String type,
			ServiceContext serviceContext)
		throws PortalException {

		DDMPermission.check(
			getPermissionChecker(), groupId, DDMActionKeys.ADD_DATA_PROVIDER,
			DDMDataProvider.class.getName());

		return ddmDataProviderLocalService.addDataProvider(
			getUserId(), groupId, nameMap, descriptionMap, data, type,
			serviceContext);
	}

	@Override
	public void deleteDataProvider(long dataProviderId) throws PortalException {
		DDMDataProviderPermission.check(
			getPermissionChecker(), dataProviderId, ActionKeys.DELETE);

		ddmDataProviderLocalService.deleteDDMDataProvider(dataProviderId);
	}

	@Override
	public DDMDataProvider getDataProvider(long dataProviderId)
		throws PortalException, PrincipalException {

		DDMDataProviderPermission.check(
			getPermissionChecker(), dataProviderId, ActionKeys.VIEW);

		return ddmDataProviderLocalService.getDDMDataProvider(dataProviderId);
	}

	@Override
	public List<DDMDataProvider> search(
		long companyId, long[] groupIds, String keywords, int start, int end,
		OrderByComparator<DDMDataProvider> orderByComparator) {

		return ddmDataProviderFinder.filterByKeywords(
			companyId, groupIds, keywords, start, end, orderByComparator);
	}

	@Override
	public List<DDMDataProvider> search(
		long companyId, long[] groupIds, String name, String description,
		boolean andOperator, int start, int end,
		OrderByComparator<DDMDataProvider> orderByComparator) {

		return ddmDataProviderFinder.filterFindByC_G_N_D(
			companyId, groupIds, name, description, andOperator, start, end,
			orderByComparator);
	}

	@Override
	public int searchCount(long companyId, long[] groupIds, String keywords) {
		return ddmDataProviderFinder.filterCountByKeywords(
			companyId, groupIds, keywords);
	}

	@Override
	public int searchCount(
		long companyId, long[] groupIds, String name, String description,
		boolean andOperator) {

		return ddmDataProviderFinder.filterCountByC_G_N_D(
			companyId, groupIds, name, description, andOperator);
	}

	@Override
	public DDMDataProvider updateDataProvider(
			long dataProviderId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, String data,
			ServiceContext serviceContext)
		throws PortalException {

		DDMDataProviderPermission.check(
			getPermissionChecker(), dataProviderId, ActionKeys.UPDATE);

		return ddmDataProviderLocalService.updateDataProvider(
			getUserId(), dataProviderId, nameMap, descriptionMap, data,
			serviceContext);
	}

}