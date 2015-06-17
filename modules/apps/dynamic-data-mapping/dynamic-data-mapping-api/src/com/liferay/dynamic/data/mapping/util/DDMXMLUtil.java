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
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.XPath;

import java.util.List;
import java.util.Locale;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Bruno Basto
 * @author Brian Wing Shun Chan
 * @author Leonardo Barros
 */
public class DDMXMLUtil {

	public static String formatXML(Document document) {
		return getDDMXML().formatXML(document);
	}

	public static String formatXML(String xml) {
		return getDDMXML().formatXML(xml);
	}

	public static DDMXML getDDMXML() {
		PortalRuntimePermission.checkGetBeanProperty(DDMXMLUtil.class);

		return _serviceTracker.getService();
	}

	public static Fields getFields(DDMStructure structure, String xml)
		throws PortalException {

		return getDDMXML().getFields(structure, xml);
	}

	public static Fields getFields(
			DDMStructure structure, XPath xPath, String xml,
			List<String> fieldNames)
		throws PortalException {

		return getDDMXML().getFields(structure, xPath, xml, fieldNames);
	}

	public static String getXML(Document document, Fields fields) {
		return getDDMXML().getXML(document, fields);
	}

	public static String getXML(Fields fields) {
		return getDDMXML().getXML(fields);
	}

	public static String updateXMLDefaultLocale(
		String xml, Locale contentDefaultLocale,
		Locale contentNewDefaultLocale) {

		return getDDMXML().updateXMLDefaultLocale(
			xml, contentDefaultLocale, contentNewDefaultLocale);
	}

	public static String validateXML(String xml) throws PortalException {
		return getDDMXML().validateXML(xml);
	}

	private static final ServiceTracker<DDMXML, DDMXML> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(DDMXMLUtil.class);

		_serviceTracker = new ServiceTracker<>(
			bundle.getBundleContext(), DDMXML.class, null);

		_serviceTracker.open();
	}

}