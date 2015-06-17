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

package com.liferay.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Juan Fernández
 * @author Leonardo Barros
 */
public class DDMTemplateHelperUtil {

	public static DDMStructure fetchStructure(DDMTemplate ddmTemplate) {
		return getDDMTemplateHelper().fetchStructure(ddmTemplate);
	}

	public static String getAutocompleteJSON(
			HttpServletRequest request, String language)
		throws Exception {

		return getDDMTemplateHelper().getAutocompleteJSON(request, language);
	}

	public static DDMTemplateHelper getDDMTemplateHelper() {
		PortalRuntimePermission.checkGetBeanProperty(
			DDMTemplateHelperUtil.class);

		return _serviceTracker.getService();
	}

	public static boolean isAutocompleteEnabled(String language) {
		return getDDMTemplateHelper().isAutocompleteEnabled(language);
	}

	private static final
		ServiceTracker<DDMTemplateHelper, DDMTemplateHelper> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(DDMTemplateHelperUtil.class);

		_serviceTracker = new ServiceTracker<>(
			bundle.getBundleContext(), DDMTemplateHelper.class, null);

		_serviceTracker.open();
	}

}