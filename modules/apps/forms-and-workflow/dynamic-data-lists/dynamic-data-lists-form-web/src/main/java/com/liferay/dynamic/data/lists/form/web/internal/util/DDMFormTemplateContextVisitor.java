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

package com.liferay.dynamic.data.lists.form.web.internal.util;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Marcellus Tavares
 */
public class DDMFormTemplateContextVisitor {

	public DDMFormTemplateContextVisitor(
		Map<String, Object> ddmFormTemplateContext) {

		_ddmFormTemplateContext = ddmFormTemplateContext;
	}

	public void onVisitField(
		Consumer<Map<String, Object>> fieldConsumer) {

		_fieldConsumer = fieldConsumer;
	}

	public void visit() {
		traversePages((List<Map<String, Object>>) _ddmFormTemplateContext.get("pages"));
	}

	protected void traverseColumns(List<Map<String, Object>> columns) {
		for (Map<String, Object> column : columns) {
			traverseFields((List<Map<String, Object>>) column.get("fields"));
		}
	}

	protected void traverseFields(List<Map<String, Object>> fields) {
		for (Map<String, Object> field : fields) {
			_fieldConsumer.accept(field);
		}
	}

	protected void traversePages(List<Map<String, Object>> pages) {
		for (Map<String, Object> page : pages) {
			traverseRows((List<Map<String, Object>>) page.get("rows"));
		}
	}

	protected void traverseRows(List<Map<String, Object>> rows) {
		for (Map<String, Object> row : rows) {
			traverseColumns((List<Map<String, Object>>) row.get("columns"));
		}
	}
	private Map<String, Object> _ddmFormTemplateContext;
	private Consumer<Map<String, Object>> _fieldConsumer =
			new NOPFieldContextConsumer();

	private static class NOPFieldContextConsumer
		implements Consumer<Map<String, Object> > {

		@Override
		public void accept(Map<String, Object> fieldContext) {
		}

	}

}