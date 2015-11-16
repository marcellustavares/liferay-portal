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

package com.liferay.dynamic.data.lists.form.web.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.liferay.dynamic.data.lists.form.web.configuration.DDLFormWebConfigurationUtil;
import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.dynamic.data.lists.util.comparator.DDLRecordIdComparator;
import com.liferay.dynamic.data.lists.util.comparator.DDLRecordModifiedDateComparator;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Leonardo Barros
 */
public class DDLFormAdminPortletUtil {

	public static OrderByComparator<DDLRecord> getRecordOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator<DDLRecord> orderByComparator = null;

		if (orderByCol.equals("modified-date")) {
			orderByComparator = new DDLRecordModifiedDateComparator(orderByAsc);
		}
		else {
			orderByComparator = new DDLRecordIdComparator(orderByAsc);
		}

		return orderByComparator;
	}

	public static void saveThumbnail(
		HttpServletRequest request, long recordSetId) throws Exception {

		DDLRecordSet recordSet = DDLRecordSetLocalServiceUtil.getRecordSet(
			recordSetId);

		String thumbName = String.valueOf(
			recordSetId) + "_" + recordSet.getModifiedDate().getTime();

		String thumbPath =
			DDLFormWebConfigurationUtil.get("thumb.path") + thumbName + ".png";

		File thumbFile = new File(thumbPath);

		if (thumbFile.exists()) {
			return;
		}

		String portalURL = PortalUtil.getPortalURL(request);

		String url =
			portalURL + "/o/ddm-form-renderer-servlet?recordSetId=" + recordSetId;

		ProcessBuilder processBuilder = new ProcessBuilder(
			DDLFormWebConfigurationUtil.get("wkhtmltoimage.path"), "-f", "png",
			"--height", "800", url, thumbPath);

		Process process = processBuilder.start();

		process.waitFor();
	}

}