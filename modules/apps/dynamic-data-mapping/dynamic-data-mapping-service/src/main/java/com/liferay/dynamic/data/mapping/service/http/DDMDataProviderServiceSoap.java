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

package com.liferay.dynamic.data.mapping.service.http;

import aQute.bnd.annotation.ProviderType;

import com.liferay.dynamic.data.mapping.service.DDMDataProviderServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;

import java.rmi.RemoteException;

import java.util.Locale;
import java.util.Map;

/**
 * Provides the SOAP utility for the
 * {@link DDMDataProviderServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a {@link java.util.List}, that
 * is translated to an array of {@link com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap}.
 * If the method in the service utility returns a
 * {@link com.liferay.dynamic.data.mapping.model.DDMDataProvider}, that is translated to a
 * {@link com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap}. Methods that SOAP cannot
 * safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDMDataProviderServiceHttp
 * @see com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap
 * @see DDMDataProviderServiceUtil
 * @generated
 */
@ProviderType
public class DDMDataProviderServiceSoap {
	public static com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap addDataProvider(
		long groupId, java.lang.String[] nameMapLanguageIds,
		java.lang.String[] nameMapValues,
		java.lang.String[] descriptionMapLanguageIds,
		java.lang.String[] descriptionMapValues, java.lang.String data,
		java.lang.String type,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(nameMapLanguageIds,
					nameMapValues);
			Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(descriptionMapLanguageIds,
					descriptionMapValues);

			com.liferay.dynamic.data.mapping.model.DDMDataProvider returnValue = DDMDataProviderServiceUtil.addDataProvider(groupId,
					nameMap, descriptionMap, data, type, serviceContext);

			return com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static void deleteDataProvider(long dataProviderId)
		throws RemoteException {
		try {
			DDMDataProviderServiceUtil.deleteDataProvider(dataProviderId);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap[] search(
		long companyId, long[] groupIds, java.lang.String keywords, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.dynamic.data.mapping.model.DDMDataProvider> orderByComparator)
		throws RemoteException {
		try {
			java.util.List<com.liferay.dynamic.data.mapping.model.DDMDataProvider> returnValue =
				DDMDataProviderServiceUtil.search(companyId, groupIds,
					keywords, start, end, orderByComparator);

			return com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap.toSoapModels(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap getDataProvider(
		long dataProviderId) throws RemoteException {
		try {
			com.liferay.dynamic.data.mapping.model.DDMDataProvider returnValue = DDMDataProviderServiceUtil.getDataProvider(dataProviderId);

			return com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap[] search(
		long companyId, long[] groupIds, java.lang.String name,
		java.lang.String description, boolean andOperator, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.dynamic.data.mapping.model.DDMDataProvider> orderByComparator)
		throws RemoteException {
		try {
			java.util.List<com.liferay.dynamic.data.mapping.model.DDMDataProvider> returnValue =
				DDMDataProviderServiceUtil.search(companyId, groupIds, name,
					description, andOperator, start, end, orderByComparator);

			return com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap.toSoapModels(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static int searchCount(long companyId, long[] groupIds,
		java.lang.String keywords) throws RemoteException {
		try {
			int returnValue = DDMDataProviderServiceUtil.searchCount(companyId,
					groupIds, keywords);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static int searchCount(long companyId, long[] groupIds,
		java.lang.String name, java.lang.String description, boolean andOperator)
		throws RemoteException {
		try {
			int returnValue = DDMDataProviderServiceUtil.searchCount(companyId,
					groupIds, name, description, andOperator);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap updateDataProvider(
		long dataProviderId, java.lang.String[] nameMapLanguageIds,
		java.lang.String[] nameMapValues,
		java.lang.String[] descriptionMapLanguageIds,
		java.lang.String[] descriptionMapValues, java.lang.String data,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(nameMapLanguageIds,
					nameMapValues);
			Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(descriptionMapLanguageIds,
					descriptionMapValues);

			com.liferay.dynamic.data.mapping.model.DDMDataProvider returnValue = DDMDataProviderServiceUtil.updateDataProvider(dataProviderId,
					nameMap, descriptionMap, data, serviceContext);

			return com.liferay.dynamic.data.mapping.model.DDMDataProviderSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(DDMDataProviderServiceSoap.class);
}