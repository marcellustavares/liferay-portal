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
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionVersionServiceUtil;

/**
 * Provides the HTTP utility for the
 * {@link KaleoDefinitionVersionServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * {@link HttpPrincipal} parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDefinitionVersionServiceSoap
 * @see HttpPrincipal
 * @see KaleoDefinitionVersionServiceUtil
 * @generated
 */
@ProviderType
public class KaleoDefinitionVersionServiceHttp {
	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion addKaleoDefinitionVersion(
		HttpPrincipal httpPrincipal, long userId, long groupId,
		java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content, int version,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"addKaleoDefinitionVersion",
					_addKaleoDefinitionVersionParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					groupId, name, titleMap, content, version, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void deleteKaleoDefinitionVersions(
		HttpPrincipal httpPrincipal, java.lang.String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"deleteKaleoDefinitionVersions",
					_deleteKaleoDefinitionVersionsParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(methodKey, name,
					serviceContext);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion getKaleoDefinitionVersion(
		HttpPrincipal httpPrincipal, java.lang.String name, int version,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"getKaleoDefinitionVersion",
					_getKaleoDefinitionVersionParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(methodKey, name,
					version, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getKaleoDefinitionVersions(
		HttpPrincipal httpPrincipal)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"getKaleoDefinitionVersions",
					_getKaleoDefinitionVersionsParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(methodKey);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion getLatestKaleoDefinitionVersion(
		HttpPrincipal httpPrincipal, java.lang.String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"getLatestKaleoDefinitionVersion",
					_getLatestKaleoDefinitionVersionParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(methodKey, name,
					serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		HttpPrincipal httpPrincipal, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"getLatestKaleoDefinitionVersions",
					_getLatestKaleoDefinitionVersionsParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, start, end, orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		HttpPrincipal httpPrincipal, long companyId, java.lang.String keywords,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion> orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"getLatestKaleoDefinitionVersions",
					_getLatestKaleoDefinitionVersionsParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					companyId, keywords, start, end, orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.util.List<com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion>)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion publishKaleoDefinitionVersion(
		HttpPrincipal httpPrincipal, long userId, long groupId,
		java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"publishKaleoDefinitionVersion",
					_publishKaleoDefinitionVersionParameterTypes7);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					groupId, name, titleMap, content, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion updateKaleoDefinitionVersion(
		HttpPrincipal httpPrincipal, long userId, java.lang.String name,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.lang.String content,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		try {
			MethodKey methodKey = new MethodKey(KaleoDefinitionVersionServiceUtil.class,
					"updateKaleoDefinitionVersion",
					_updateKaleoDefinitionVersionParameterTypes8);

			MethodHandler methodHandler = new MethodHandler(methodKey, userId,
					name, titleMap, content, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(KaleoDefinitionVersionServiceHttp.class);
	private static final Class<?>[] _addKaleoDefinitionVersionParameterTypes0 = new Class[] {
			long.class, long.class, java.lang.String.class, java.util.Map.class,
			java.lang.String.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _deleteKaleoDefinitionVersionsParameterTypes1 =
		new Class[] {
			java.lang.String.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _getKaleoDefinitionVersionParameterTypes2 = new Class[] {
			java.lang.String.class, int.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _getKaleoDefinitionVersionsParameterTypes3 = new Class[] {
			
		};
	private static final Class<?>[] _getLatestKaleoDefinitionVersionParameterTypes4 =
		new Class[] {
			java.lang.String.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _getLatestKaleoDefinitionVersionsParameterTypes5 =
		new Class[] {
			long.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _getLatestKaleoDefinitionVersionsParameterTypes6 =
		new Class[] {
			long.class, java.lang.String.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[] _publishKaleoDefinitionVersionParameterTypes7 =
		new Class[] {
			long.class, long.class, java.lang.String.class, java.util.Map.class,
			java.lang.String.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[] _updateKaleoDefinitionVersionParameterTypes8 =
		new Class[] {
			long.class, java.lang.String.class, java.util.Map.class,
			java.lang.String.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
}