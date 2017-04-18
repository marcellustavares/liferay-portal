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

package com.liferay.dynamic.data.lists.form.web.internal.display.context;

import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Marcellus Tavares
 */
public class DDLFormBuilderContext {

	public DDLFormBuilderContext(Optional<DDLRecordSet> recordSetOptional) {
		_recordSetOptional = recordSetOptional;
	}

	public Map<String, Object> create() {
		Optional<Map<String, Object>> contextOptional = _recordSetOptional.map(
			this::createStateContext);

		return contextOptional.orElseGet(this::createEmptyStateContext);
	}

	protected Map<String, Object> createEmptyStateContext() {
		Map<String, Object> emptyStateContext = new HashMap<>();

		emptyStateContext.put("pages", new ArrayList<>());
		emptyStateContext.put("rules", new ArrayList<>());

		Map<String, String> successPage = new HashMap<>();

		successPage.put("body", StringPool.BLANK);
		successPage.put("enabled", Boolean.FALSE.toString());
		successPage.put("title", StringPool.BLANK);

		emptyStateContext.put("successPage", successPage);

		return emptyStateContext;
	}

	protected Map<String, Object> createStateContext(DDLRecordSet recordSet) {
		Map<String, Object> stateContext = new HashMap<>();

		return stateContext;
	}

	private final Optional<DDLRecordSet> _recordSetOptional;

}