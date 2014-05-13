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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.messageboards.model.MBMessage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

/**
 * @author André de Oliveira
 */
public class SearchResultContributorRegistryImpl
	implements SearchResultContributorRegistry {

	public SearchResultContributorRegistryImpl() {
		register(
			DLFileEntry.class.getName(),
			new SearchResultDLFileEntryContributorFactory());

		register(
			MBMessage.class.getName(),
			new SearchResultMBMessageContributorFactory());
	}

	@Override
	public SearchResultContributor getInstance(
			String entryClassName, long entryClassPK, Locale locale,
			PortletURL portletURL, PortletRequest portletRequest,
			PortletResponse portletResponse)
		throws PortalException, SystemException {

		SearchResultContributorFactory factory = _registeredFactories.get(
			entryClassName);

		if (factory == null) {
			return null;
		}

		return factory.getInstance(
			entryClassPK, locale, portletURL, portletRequest, portletResponse);
	}

	protected void register(
		String className, SearchResultContributorFactory factory) {

		_registeredFactories.put(className, factory);
	}

	private Map<String, SearchResultContributorFactory> _registeredFactories =
		new HashMap<String, SearchResultContributorFactory>();

}