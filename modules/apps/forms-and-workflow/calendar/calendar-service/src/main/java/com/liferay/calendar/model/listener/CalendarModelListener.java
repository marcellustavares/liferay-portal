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

package com.liferay.calendar.model.listener;

import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchException;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adam Brandizzi
 */
@Component(immediate = true, service = ModelListener.class)
public class CalendarModelListener extends BaseModelListener<Calendar> {

	@Override
	public void onAfterUpdate(Calendar calendar)
	throws ModelListenerException {

		List<CalendarBooking> calendarBookings =
			_calendarBookingLocalService.getCalendarBookings(
				calendar.getCalendarId());

		Indexer<CalendarBooking> indexer = _indexerRegistry.getIndexer(
			CalendarBooking.class);

		try {
			indexer.reindex(calendarBookings);
		} catch (SearchException e) {
			new ModelListenerException(e);
		}
	}

	@Reference(unbind = "-")
	private IndexerRegistry _indexerRegistry;
	@Reference(unbind = "-")
	private CalendarBookingLocalService _calendarBookingLocalService;
}