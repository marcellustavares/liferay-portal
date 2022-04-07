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

package com.liferay.analytics.message.storage.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;AnalyticsModelRemoveLogger&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsModelRemoveLogger
 * @generated
 */
public class AnalyticsModelRemoveLoggerTable
	extends BaseTable<AnalyticsModelRemoveLoggerTable> {

	public static final AnalyticsModelRemoveLoggerTable INSTANCE =
		new AnalyticsModelRemoveLoggerTable();

	public final Column<AnalyticsModelRemoveLoggerTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<AnalyticsModelRemoveLoggerTable, Long>
		analyticsModelRemoveLoggerId = createColumn(
			"analyticsModelRemoveLoggerId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<AnalyticsModelRemoveLoggerTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<AnalyticsModelRemoveLoggerTable, Long> userId =
		createColumn("userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<AnalyticsModelRemoveLoggerTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<AnalyticsModelRemoveLoggerTable, Date> modifiedDate =
		createColumn(
			"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<AnalyticsModelRemoveLoggerTable, String> className =
		createColumn(
			"className", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<AnalyticsModelRemoveLoggerTable, Long> classPK =
		createColumn("classPK", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);

	private AnalyticsModelRemoveLoggerTable() {
		super(
			"AnalyticsModelRemoveLogger", AnalyticsModelRemoveLoggerTable::new);
	}

}