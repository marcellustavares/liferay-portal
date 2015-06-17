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

package com.liferay.dynamic.data.mapping.storage;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.liferay.dynamic.data.mapping.exception.StorageException;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.service.ServiceContext;

/**
 * @author Eduardo Lundgren
 * @author Leonardo Barros
 */
public class StorageEngineUtil {

	public static long create(
			long companyId, long ddmStructureId, DDMFormValues ddmFormValues,
			ServiceContext serviceContext)
		throws StorageException {

		return getStorageEngine().create(
			companyId, ddmStructureId, ddmFormValues, serviceContext);
	}

	public static void deleteByClass(long classPK) throws StorageException {
		getStorageEngine().deleteByClass(classPK);
	}

	public static void deleteByDDMStructure(long ddmStructureId)
		throws StorageException {

		getStorageEngine().deleteByDDMStructure(ddmStructureId);
	}

	public static DDMFormValues getDDMFormValues(long classPK)
		throws StorageException {

		return getStorageEngine().getDDMFormValues(classPK);
	}

	public static StorageEngine getStorageEngine() {
		PortalRuntimePermission.checkGetBeanProperty(StorageEngineUtil.class);

		return _serviceTracker.getService();
	}

	public static void update(
			long classPK, DDMFormValues ddmFormValues,
			ServiceContext serviceContext)
		throws StorageException {

		getStorageEngine().update(classPK, ddmFormValues, serviceContext);
	}

	private static final 
		ServiceTracker<StorageEngine, StorageEngine> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(StorageEngineUtil.class);

		_serviceTracker = new ServiceTracker<>(
			bundle.getBundleContext(), StorageEngine.class, null);

		_serviceTracker.open();
	}

}