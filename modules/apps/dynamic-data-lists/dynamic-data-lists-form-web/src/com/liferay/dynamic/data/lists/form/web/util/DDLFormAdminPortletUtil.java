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

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import com.liferay.dynamic.data.lists.form.web.configuration.DDLFormWebConfigurationUtil;
import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.util.comparator.DDLRecordIdComparator;
import com.liferay.dynamic.data.lists.util.comparator.DDLRecordModifiedDateComparator;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.Image;

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
	
	public static void saveThumbnail(long recordSetId) throws Exception {
		String url = 
			"http://localhost:8080/o/ddm-form-renderer-servlet?recordSetId=" + recordSetId;
		
		String thumbPath = DDLFormWebConfigurationUtil.get("thumb.path") + recordSetId + ".png";
		
		ProcessBuilder processBuilder = new ProcessBuilder(
			DDLFormWebConfigurationUtil.get("wkhtmltoimage.path"), "-f", "png", "--height", "400", url, thumbPath);
		
		Process process = processBuilder.start();
		
		process.waitFor();
		
//		ImageBag imageBag  = ImageToolUtil.read(
//			FileUtil.getBytes(new File(thumbPath)));
//		
//		RenderedImage renderedImage = ImageToolUtil.scale(imageBag.getRenderedImage(), 500, 400);
//		
//		byte[] bytes = ImageToolUtil.getBytes(renderedImage, "image/png");
//		
//		FileUtil.write(thumbPath, bytes);
	}

}