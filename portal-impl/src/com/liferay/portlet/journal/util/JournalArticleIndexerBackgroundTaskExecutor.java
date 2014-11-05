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

package com.liferay.portlet.journal.util;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.lar.backgroundtask.BaseStagingBackgroundTaskExecutor;
import com.liferay.portal.model.BackgroundTask;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Vilmos Papp
 */
public class JournalArticleIndexerBackgroundTaskExecutor
	extends BaseStagingBackgroundTaskExecutor {

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask)
		throws Exception {

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		if (_log.isInfoEnabled()) {
			_log.info(
				"Start DDMStructure based article indexing in background.");
		}

		Map<String, Serializable> taskContextMap =
			backgroundTask.getTaskContextMap();

		String className = MapUtil.getString(taskContextMap, "className");
		String ddmStructureIds = MapUtil.getString(
			taskContextMap, "ddmStructureIds");

		Indexer indexer = IndexerRegistryUtil.getIndexer(className);

		List<String> structureIds = ListUtil.fromString(ddmStructureIds);
		List<Long> ddmStructureIdsList = new ArrayList<Long>(
			structureIds.size());

		for (String structureId : structureIds) {
			ddmStructureIdsList.add(Long.parseLong(structureId));
		}

		indexer.reindexDDMStructures(ddmStructureIdsList);

		if (_log.isInfoEnabled()) {
			_log.info(
				"Finished DDMStructure based article indexing in " +
				stopWatch.getTime() + " ms");
		}

		return BackgroundTaskResult.SUCCESS;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleIndexerBackgroundTaskExecutor.class);

}