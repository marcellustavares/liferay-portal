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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.upgrade.AutoBatchPreparedStatementUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcellus Tavares
 */
public class UpgradeDDLFormPortletId
	extends com.liferay.portal.upgrade.util.UpgradePortletId {

	protected void deleteResourcePermissions(
		String oldRootPortletId, String newRootPortletId) {

		try (PreparedStatement ps1 = connection.prepareStatement(
				"select rp1.resourcePermissionId from ResourcePermission rp1 " +
					"inner join ResourcePermission rp2 " +
					"on rp1.companyId = rp2.companyId " +
					"and rp1.scope = rp2.scope " +
					"and rp1.roleId = rp2.roleId " +
					"where rp1.name = '" + oldRootPortletId + "' " +
					"and rp2.name = '" + newRootPortletId + "'");
			PreparedStatement ps2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"delete from ResourcePermission " +
						"where resourcePermissionId = ?");
			ResultSet rs = ps1.executeQuery()) {

			while (rs.next()) {
				long resourcePermissionId = rs.getLong("resourcePermissionId");

				ps2.setLong(1, resourcePermissionId);
				ps2.addBatch();
			}

			ps2.executeBatch();
		}
		catch (SQLException sqle) {
			if (_log.isWarnEnabled()) {
				_log.warn(sqle, sqle);
			}
		}
	}

	@Override
	protected String[][] getRenamePortletIdsArray() {
		return new String[][] {
			new String[] {
				"1_WAR_ddlformportlet",
				DDLPortletKeys.DYNAMIC_DATA_LISTS_DISPLAY
			}
		};
	}

	@Override
	protected void updateInstanceablePortletPreferences(
			String oldRootPortletId, String newRootPortletId)
		throws Exception {

		StringBundler sb = new StringBundler(8);

		sb.append("select portletPreferencesId, portletId, preferences from ");
		sb.append("PortletPreferences where portletId = '");
		sb.append(oldRootPortletId);
		sb.append("' OR portletId like '");
		sb.append(oldRootPortletId);
		sb.append("_INSTANCE_%' OR portletId like '");
		sb.append(oldRootPortletId);
		sb.append("_USER_%_INSTANCE_%'");

		try (PreparedStatement ps = connection.prepareStatement(sb.toString());
			PreparedStatement ps2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update PortletPreferences set portletId = ?, " +
						"preferences = ? where portletPreferencesId = ?");
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				long portletPreferencesId = rs.getLong("portletPreferencesId");
				String portletId = rs.getString("portletId");
				String preferences = rs.getString("preferences");

				String newPortletId = StringUtil.replace(
					portletId, oldRootPortletId, newRootPortletId);

				String newPreferences = StringUtil.replace(
					preferences, "</portlet-preferences>",
					"<preference><name>formView</name><value>true</value>" +
						"</preference></portlet-preferences>");

				ps2.setString(1, newPortletId);
				ps2.setString(2, newPreferences);
				ps2.setLong(3, portletPreferencesId);

				ps2.addBatch();
			}

			ps2.executeBatch();
		}
	}

	@Override
	protected void updatePortlet(
			String oldRootPortletId, String newRootPortletId)
		throws Exception {

		try {
			updateResourcePermission(oldRootPortletId, newRootPortletId, true);

			updateInstanceablePortletPreferences(
				oldRootPortletId, newRootPortletId);

			updateLayouts(oldRootPortletId, newRootPortletId, false);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
	}

	@Override
	protected void updateResourcePermission(
			String oldRootPortletId, String newRootPortletId,
			boolean updateName)
		throws Exception {

		deleteResourcePermissions(oldRootPortletId, newRootPortletId);

		super.updateResourcePermission(
			oldRootPortletId, newRootPortletId, updateName);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeDDLFormPortletId.class);

}