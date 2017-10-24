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

package com.liferay.portal.analytics.servlet;

import com.liferay.analytics.java.client.AnalyticsEventsMessage;
import com.liferay.portal.analytics.constants.AnalyticsDestinationNames;
import com.liferay.portal.kernel.json.JSONDeserializer;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jeyvison Nascimento
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/",
		"osgi.http.whiteboard.servlet.name=com.liferay.portal.analytics.servlet.AnalyticsServlet",
		"osgi.http.whiteboard.servlet.pattern=/analytics/*"
	},
	service = Servlet.class
)
public class AnalyticsServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws IOException, ServletException {

		int statusCode = HttpServletResponse.SC_OK;

		String payload = _getMessage(req);

		if ((payload != null) && !payload.isEmpty()) {
			JSONDeserializer<AnalyticsEventsMessage> messageDeserializer =
				JSONFactoryUtil.createJSONDeserializer();

			AnalyticsEventsMessage analyticsMessage =
				messageDeserializer.deserialize(
					payload, AnalyticsEventsMessage.class);

			if (analyticsMessage.getEvents().isEmpty()) {
				resp.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"No events in Analytics Message");
				return;
			}

			Message message = new Message();

			message.setPayload(analyticsMessage);

			MessageBusUtil.sendMessage(
				AnalyticsDestinationNames.ANALYTICS, message);

			resp.setStatus(HttpServletResponse.SC_CREATED);

			statusCode = HttpServletResponse.SC_CREATED;
		}

		resp.setStatus(statusCode);
	}

	private String _getMessage(HttpServletRequest request) throws IOException {
		BufferedReader reader = request.getReader();

		return reader.lines().collect(Collectors.joining());
	}

	private static final long serialVersionUID = 1L;

}