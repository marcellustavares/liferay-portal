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

package com.liferay.portal.search.elasticsearch;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexSearcher;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.HitsImpl;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch.facet.ElasticsearchFacetFieldCollector;
import com.liferay.portal.search.elasticsearch.facet.FacetProcessorUtil;
import com.liferay.portal.search.elasticsearch.util.DocumentTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.StopWatch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * @author Michael C. Han
 * @author Milen Dyankov
 */
public class ElasticsearchIndexSearcher extends BaseIndexSearcher {

	@Override
	public Hits search(SearchContext searchContext, Query query) {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		/*
		 * Temporary code, will be replaced when problem of search without
		 * results being resolved.
		 *
		 * Now the same query used through java API shown different results than
		 * pure REST API.
		 *
		 * This issues is registered in issues on elasticsearch github.
		 */
		QueryBuilder queryBuilder = nestedQuery("fields", matchAllQuery());

		SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder(
			searchContext, query, queryBuilder);

		SearchRequest searchRequest = searchRequestBuilder.request();

		SearchResponse searchResponse = getSearchResponse(
			searchContext, searchRequest);

		updateFacetCollectors(searchContext, searchResponse);

		Hits hits = processSearchHits(searchResponse, query);

		hits.setStart(stopWatch.getStartTime());

		logSearchDetails(searchContext, stopWatch, queryBuilder, hits);

		return hits;
	}

	public void setElasticsearchConnectionManager(
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		_elasticsearchConnectionManager = elasticsearchConnectionManager;
	}

	public void setMaxResultSize(int maxResultSize) {
		_maxResultSize = maxResultSize;
	}

	protected void addFacets(
		SearchRequestBuilder searchRequestBuilder,
		SearchContext searchContext) {

		Map<String, Facet> facetsMap = searchContext.getFacets();

		for (Facet facet : facetsMap.values()) {
			if (facet.isStatic()) {
				continue;
			}

			FacetProcessorUtil.processFacet(searchRequestBuilder, facet);
		}
	}

	protected void addHighlightedField(
		SearchRequestBuilder searchRequestBuilder, QueryConfig queryConfig,
		String fieldName) {

		searchRequestBuilder.addHighlightedField(
			fieldName, queryConfig.getHighlightFragmentSize(),
			queryConfig.getHighlightSnippetSize());

		String localizedFieldName = DocumentImpl.getLocalizedName(
			queryConfig.getLocale(), fieldName);

		searchRequestBuilder.addHighlightedField(
			localizedFieldName, queryConfig.getHighlightFragmentSize(),
			queryConfig.getHighlightSnippetSize());
	}

	protected void addHighlights(
		SearchRequestBuilder searchRequestBuilder, QueryConfig queryConfig) {

		if (!queryConfig.isHighlightEnabled()) {
			return;
		}

		addHighlightedField(
			searchRequestBuilder, queryConfig, Field.ASSET_CATEGORY_TITLES);
		addHighlightedField(searchRequestBuilder, queryConfig, Field.CONTENT);
		addHighlightedField(
			searchRequestBuilder, queryConfig, Field.DESCRIPTION);
		addHighlightedField(searchRequestBuilder, queryConfig, Field.TITLE);
	}

	protected void addPagination(
		SearchRequestBuilder searchRequestBuilder, int start, int end) {

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS)) {
			searchRequestBuilder.setSize(_maxResultSize);
		}
		else {
			searchRequestBuilder.setFrom(start);
			searchRequestBuilder.setSize(end - start);
		}
	}

	protected void addSelectedFields(
		SearchRequestBuilder searchRequestBuilder, QueryConfig queryConfig) {

		String[] selectedFieldNames = queryConfig.getSelectedFieldNames();

		if (ArrayUtil.isEmpty(selectedFieldNames)) {
			searchRequestBuilder.addField(StringPool.STAR);
		}
		else {
			searchRequestBuilder.addFields(selectedFieldNames);
		}
	}

	protected void addSnippets(
		Document document, Set<String> queryTerms,
		Map<String, HighlightField> highlightFields, String fieldName,
		Locale locale) {

		String snippet = StringPool.BLANK;

		String localizedContentName = DocumentImpl.getLocalizedName(
			locale, fieldName);

		String snippetFieldName = localizedContentName;

		HighlightField highlightField = highlightFields.get(
			localizedContentName);

		if (highlightField == null) {
			highlightField = highlightFields.get(fieldName);

			snippetFieldName = fieldName;
		}

		if (highlightField != null) {
			Text[] texts = highlightField.fragments();

			StringBundler sb = new StringBundler(texts.length * 2);

			for (Text text : texts) {
				sb.append(text);
				sb.append(StringPool.TRIPLE_PERIOD);
			}

			snippet = sb.toString();
		}

		Matcher matcher = _pattern.matcher(snippet);

		while (matcher.find()) {
			queryTerms.add(matcher.group(1));
		}

		snippet = StringUtil.replace(snippet, "<em>", StringPool.BLANK);
		snippet = StringUtil.replace(snippet, "</em>", StringPool.BLANK);

		document.addText(
			Field.SNIPPET.concat(StringPool.UNDERLINE).concat(snippetFieldName),
			snippet);
	}

	protected void addSnippets(
		SearchHit hit, Document document, QueryConfig queryConfig,
		Set<String> queryTerms) {

		if (!queryConfig.isHighlightEnabled()) {
			return;
		}

		Map<String, HighlightField> highlightFields = hit.getHighlightFields();

		if (MapUtil.isEmpty(highlightFields)) {
			return;
		}

		addSnippets(
			document, queryTerms, highlightFields, Field.ASSET_CATEGORY_TITLES,
			queryConfig.getLocale());
		addSnippets(
			document, queryTerms, highlightFields, Field.CONTENT,
			queryConfig.getLocale());
		addSnippets(
			document, queryTerms, highlightFields, Field.DESCRIPTION,
			queryConfig.getLocale());
		addSnippets(
			document, queryTerms, highlightFields, Field.TITLE,
			queryConfig.getLocale());
	}

	protected void addSort(
		SearchRequestBuilder searchRequestBuilder, Sort[] sorts) {

		if ((sorts == null) || (sorts.length == 0)) {
			return;
		}

		Set<String> sortFieldNames = new HashSet<String>();

		for (Sort sort : sorts) {
			if (sort == null) {
				continue;
			}

			String sortFieldName = DocumentImpl.getSortFieldName(
				sort, "_score");

			if (sortFieldNames.contains(sortFieldName)) {
				continue;
			}

			sortFieldNames.add(sortFieldName);

			SortOrder sortOrder = SortOrder.ASC;

			if (sort.isReverse() || sortFieldName.equals("_score")) {
				sortOrder = SortOrder.DESC;
			}

			SortBuilder sortBuilder = null;

			if (sortFieldName.equals("_score")) {
				sortBuilder = new ScoreSortBuilder();
			}
			else {
				FieldSortBuilder fieldSortBuilder = new FieldSortBuilder(
					sortFieldName);

				fieldSortBuilder.ignoreUnmapped(true);

				sortBuilder = fieldSortBuilder;
			}

			sortBuilder.order(sortOrder);

			searchRequestBuilder.addSort(sortBuilder);
		}
	}

	protected SearchRequestBuilder buildSearchRequestBuilder(
		SearchContext searchContext, Query query, QueryBuilder queryBuilder) {

		Client client = _elasticsearchConnectionManager.getClient();

		String companyId = String.valueOf(searchContext.getCompanyId());

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(
			companyId);

		configureSearch(searchContext, query, searchRequestBuilder);

		searchRequestBuilder.setQuery(queryBuilder);

		searchRequestBuilder.setTypes(DocumentTypes.LIFERAY);

		searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);

		return searchRequestBuilder;
	}

	protected void configureSearch(
		SearchContext searchContext, Query query,
		SearchRequestBuilder searchRequestBuilder) {

		addFacets(searchRequestBuilder, searchContext);
		addHighlights(searchRequestBuilder, query.getQueryConfig());
		addPagination(
			searchRequestBuilder, searchContext.getStart(),
			searchContext.getEnd());
		addSelectedFields(searchRequestBuilder, query.getQueryConfig());
		addSort(searchRequestBuilder, searchContext.getSorts());
	}

	protected SearchResponse getSearchResponse(
		SearchContext searchContext, SearchRequest searchRequest) {

		Client client = _elasticsearchConnectionManager.getClient();

		refreshIndex(searchContext);

		ClusterHealthRequest request = Requests.clusterHealthRequest();
		request.waitForStatus(ClusterHealthStatus.GREEN);
		client.admin().cluster().health(request).actionGet();

		ActionFuture<SearchResponse> future = client.search(searchRequest);

		SearchResponse searchResponse = future.actionGet();

		return searchResponse;
	}

	protected void logSearchDetails(
		SearchContext searchContext, StopWatch stopWatch,
		QueryBuilder queryBuilder, Hits hits) {

		if (_log.isInfoEnabled()) {
			stopWatch.stop();

			String msgTemplate =
				"Searching \nIndex: %S \nDocument type: %S \nQuery: \n%S\nHit" +
				"s: %S\ntook %S s with the search engine using %S s.";

			String index = String.valueOf(searchContext.getCompanyId());
			double startTime = (double)stopWatch.getTime() / 1000;

			String msg = String.format(msgTemplate, index,
			DocumentTypes.LIFERAY, queryBuilder.toString(), hits.getLength(),
			startTime, hits.getSearchTime());

			_log.info(msg);
		}
	}

	protected Document processSearchHit(SearchHit hit) {
		Document document = new DocumentImpl();

		Map<String, SearchHitField> searchHitFields = hit.getFields();

		for (Map.Entry<String, SearchHitField> entry :
				searchHitFields.entrySet()) {

			SearchHitField searchHitField = entry.getValue();

			Collection<Object> fieldValues = searchHitField.getValues();

			Field field = new Field(
				entry.getKey(),
				ArrayUtil.toStringArray(
					fieldValues.toArray(new Object[fieldValues.size()])));

			document.add(field);
		}

		return document;
	}

	protected Hits processSearchHits(
		SearchResponse searchResponse, Query query) {

		SearchHits searchHits = searchResponse.getHits();
		QueryConfig queryConfig = query.getQueryConfig();

		Hits hits = new HitsImpl();

		List<Document> documents = new ArrayList<Document>();
		Set<String> queryTerms = new HashSet<String>();
		List<Float> scores = new ArrayList<Float>();

		if (searchHits.totalHits() > 0) {
			SearchHit[] searchHitsArray = searchHits.getHits();

			for (SearchHit searchHit : searchHitsArray) {
				Document document = processSearchHit(searchHit);

				documents.add(document);

				scores.add(searchHit.getScore());

				addSnippets(searchHit, document, queryConfig, queryTerms);
			}
		}

		hits.setDocs(documents.toArray(new Document[documents.size()]));
		hits.setLength((int)searchHits.getTotalHits());
		hits.setQueryTerms(queryTerms.toArray(new String[queryTerms.size()]));
		hits.setScores(scores.toArray(new Float[scores.size()]));
		hits.setQuery(query);

		TimeValue timeValue = searchResponse.getTook();

		hits.setSearchTime((float)timeValue.getSecondsFrac());

		return hits;
	}

	protected void refreshIndex(SearchContext searchContext) {
		Client client = _elasticsearchConnectionManager.getClient();
		String companyId = String.valueOf(searchContext.getCompanyId());

		ListenableActionFuture<RefreshResponse> future =
			client.admin().indices().prepareRefresh(companyId).execute();

		future.actionGet();
	}

	protected void updateFacetCollectors(
		SearchContext searchContext, SearchResponse searchResponse) {

		Map<String, Facet> facetsMap = searchContext.getFacets();

		for (Facet facet : facetsMap.values()) {
			if (facet.isStatic()) {
				continue;
			}

			Facets facets = searchResponse.getFacets();

			org.elasticsearch.search.facet.Facet elasticsearchFacet =
				facets.facet(facet.getFieldName());

			FacetCollector facetCollector =
				new ElasticsearchFacetFieldCollector(elasticsearchFacet);

			facet.setFacetCollector(facetCollector);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		ElasticsearchIndexSearcher.class);

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private int _maxResultSize = 1000;
	private Pattern _pattern = Pattern.compile("<em>(.*?)</em>");

}