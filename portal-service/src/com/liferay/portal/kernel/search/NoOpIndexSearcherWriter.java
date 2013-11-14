/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

import com.liferay.portal.kernel.util.StringPool;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class NoOpIndexSearcherWriter implements IndexSearcher, IndexWriter {

	@Override
	public void addDocument(SearchContext searchContext, Document document)
		throws SearchException {}

	@Override
	public void addDocuments(
			SearchContext searchContext, Collection<Document> documents)
		throws SearchException {}

	@Override
	public void clearQuerySuggestionDictionaryIndexes(
			SearchContext searchContext)
		throws SearchException {}

	@Override
	public void clearSpellCheckerDictionaryIndexes(SearchContext searchContext)
		throws SearchException {}

	@Override
	public void deleteDocument(SearchContext searchContext, String uid)
		throws SearchException {
	}

	@Override
	public void deleteDocuments(
			SearchContext searchContext, Collection<String> uids)
		throws SearchException {
	}

	@Override
	public void deletePortletDocuments(
			SearchContext searchContext, String portletId)
		throws SearchException {
	}

	@Override
	public void indexKeyword(
			SearchContext searchContext, float weight, String keywordType)
		throws SearchException {}

	@Override
	public void indexQuerySuggestionDictionaries(SearchContext searchContext)
		throws SearchException {}

	@Override
	public void indexQuerySuggestionDictionary(SearchContext searchContext)
		throws SearchException {}

	@Override
	public void indexSpellCheckerDictionaries(SearchContext searchContext)
		throws SearchException {}

	@Override
	public void indexSpellCheckerDictionary(SearchContext searchContext)
		throws SearchException {}

	@Override
	public Hits search(SearchContext searchContext, Query query)
		throws SearchException {

		return null;
	}

	public Hits search(
			String searchEngineId, long companyId, Query query, Sort[] sort,
			int start, int end)
		throws SearchException {

		return null;
	}

	@Override
	public String spellCheckKeywords(SearchContext searchContext)
		throws SearchException {

		return StringPool.BLANK;
	}

	@Override
	public Map<String, List<String>> spellCheckKeywords(
			SearchContext searchContext, int max)
		throws SearchException {

		return Collections.emptyMap();
	}

	@Override
	public String[] suggestKeywordQueries(SearchContext searchContext, int max)
		throws SearchException {

		return new String[0];
	}

	@Override
	public void updateDocument(SearchContext searchContext, Document document)
		throws SearchException {
	}

	@Override
	public void updateDocuments(
			SearchContext searchContext, Collection<Document> documents)
		throws SearchException {
	}

}