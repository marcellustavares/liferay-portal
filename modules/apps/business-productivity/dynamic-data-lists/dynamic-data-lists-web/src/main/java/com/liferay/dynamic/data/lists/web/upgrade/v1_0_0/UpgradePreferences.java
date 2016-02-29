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

package com.liferay.dynamic.data.lists.web.upgrade.v1_0_0;

import com.liferay.dynamic.data.lists.constants.DDLPortletKeys;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.upgrade.BaseUpgradePortletPreferences;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;

/**
 * @author Leonardo Barros
 */
public class UpgradePreferences extends BaseUpgradePortletPreferences {

	public UpgradePreferences() {
		_preferenceNamesMap.put("showEditFormFirst", "true");
	}

	@Override
	protected String[] getPortletIds() {
		return new String[] {DDLPortletKeys.DYNAMIC_DATA_LISTS_DISPLAY + "%"};
	}

	protected Map<String, String> getPreferenceNamesMap() {
		return _preferenceNamesMap;
	}

	@Override
	protected String upgradePreferences(
			long companyId, long ownerId, int ownerType, long plid,
			String portletId, String xml)
		throws Exception {

		PortletPreferences preferences = PortletPreferencesFactoryUtil.fromXML(
			companyId, ownerId, ownerType, plid, portletId, xml);

		Map<String, String[]> preferencesMap = preferences.getMap();

		Map<String, String> preferenceNamesMap = getPreferenceNamesMap();

		for (String name : preferenceNamesMap.keySet()) {
			String[] values = preferencesMap.get(name);

			if (values == null) {
				preferences.setValues(preferenceNamesMap.get(name), values);
				continue;
			}

			preferences.reset(name);

			preferences.setValues(preferenceNamesMap.get(name), values);
		}

		return PortletPreferencesFactoryUtil.toXML(preferences);
	}

	private final Map<String, String> _preferenceNamesMap = new HashMap<>();

}