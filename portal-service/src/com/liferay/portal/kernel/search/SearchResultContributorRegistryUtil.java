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

import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.messageboards.model.MBMessage;

/**
 * @author André de Oliveira
 */
public class SearchResultContributorRegistryUtil {

	public static SearchResultContributorRegistry getRegistry() {
		return _registry;
	}

	private static SearchResultContributorRegistry _createRegistry() {
		SearchResultContributorRegistry registry =
			new SearchResultContributorRegistryImpl();

		_registerDLFileEntry(registry);
		_registerJournalArticle(registry);
		_registerMBMessage(registry);

		return registry;
	}

	private static void _registerDLFileEntry(
		SearchResultContributorRegistry registry) {

		registry.register(
			DLFileEntry.class.getName(),
			new SearchResultDLFileEntryContributorFactory());
	}

	private static void _registerJournalArticle(
		SearchResultContributorRegistry registry) {

		registry.register(
			JournalArticle.class.getName(),
			new SearchResultJournalArticleContributorFactory());
	}

	private static void _registerMBMessage(
		SearchResultContributorRegistry registry) {

		registry.register(
			MBMessage.class.getName(),
			new SearchResultMBMessageContributorFactory());
	}

	private static SearchResultContributorRegistry _registry =
		_createRegistry();

}