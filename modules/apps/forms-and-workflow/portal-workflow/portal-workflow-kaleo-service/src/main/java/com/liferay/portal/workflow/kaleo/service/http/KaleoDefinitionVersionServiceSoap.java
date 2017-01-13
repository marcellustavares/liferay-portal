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

package com.liferay.portal.workflow.kaleo.service.http;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionVersionServiceUtil;

import java.rmi.RemoteException;

import java.util.Locale;
import java.util.Map;

/**
 * Provides the SOAP utility for the
 * {@link KaleoDefinitionVersionServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a {@link java.util.List}, that
 * is translated to an array of {@link com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap}.
 * If the method in the service utility returns a
 * {@link com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion}, that is translated to a
 * {@link com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap}. Methods that SOAP cannot
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
 * @see KaleoDefinitionVersionServiceHttp
 * @see com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap
 * @see KaleoDefinitionVersionServiceUtil
 * @generated
 */
@ProviderType
public class KaleoDefinitionVersionServiceSoap {
	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap addKaleoDefinitionVersion(
		long userId, long groupId, java.lang.String name,
		java.lang.String[] titleMapLanguageIds,
		java.lang.String[] titleMapValues, java.lang.String content,
		int version,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(titleMapLanguageIds,
					titleMapValues);

			com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion returnValue =
				KaleoDefinitionVersionServiceUtil.addKaleoDefinitionVersion(userId,
					groupId, name, titleMap, content, version, serviceContext);

			return com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static void deleteKaleoDefinitionVersions(java.lang.String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			KaleoDefinitionVersionServiceUtil.deleteKaleoDefinitionVersions(name,
				serviceContext);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap getKaleoDefinitionVersion(
		java.lang.String name, int version,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion returnValue =
				KaleoDefinitionVersionServiceUtil.getKaleoDefinitionVersion(name,
					version, serviceContext);

			return com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap[] getKaleoDefinitionVersions()
		throws RemoteException {
		try {
			java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> returnValue =
				KaleoDefinitionVersionServiceUtil.getKaleoDefinitionVersions();

			return com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap.toSoapModels(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap getLatestKaleoDefinitionVersion(
		java.lang.String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion returnValue =
				KaleoDefinitionVersionServiceUtil.getLatestKaleoDefinitionVersion(name,
					serviceContext);

			return com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap[] getLatestKaleoDefinitionVersions(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> orderByComparator)
		throws RemoteException {
		try {
			java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> returnValue =
				KaleoDefinitionVersionServiceUtil.getLatestKaleoDefinitionVersions(companyId,
					start, end, orderByComparator);

			return com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap.toSoapModels(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap[] getLatestKaleoDefinitionVersions(
		long companyId, java.lang.String keywords, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> orderByComparator)
		throws RemoteException {
		try {
			java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> returnValue =
				KaleoDefinitionVersionServiceUtil.getLatestKaleoDefinitionVersions(companyId,
					keywords, start, end, orderByComparator);

			return com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap.toSoapModels(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap publishKaleoDefinitionVersion(
		long userId, long groupId, java.lang.String name,
		java.lang.String[] titleMapLanguageIds,
		java.lang.String[] titleMapValues, java.lang.String content,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(titleMapLanguageIds,
					titleMapValues);

			com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion returnValue =
				KaleoDefinitionVersionServiceUtil.publishKaleoDefinitionVersion(userId,
					groupId, name, titleMap, content, serviceContext);

			return com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap updateKaleoDefinitionVersion(
		long userId, java.lang.String name,
		java.lang.String[] titleMapLanguageIds,
		java.lang.String[] titleMapValues, java.lang.String content,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(titleMapLanguageIds,
					titleMapValues);

			com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion returnValue =
				KaleoDefinitionVersionServiceUtil.updateKaleoDefinitionVersion(userId,
					name, titleMap, content, serviceContext);

			return com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersionSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(KaleoDefinitionVersionServiceSoap.class);
}