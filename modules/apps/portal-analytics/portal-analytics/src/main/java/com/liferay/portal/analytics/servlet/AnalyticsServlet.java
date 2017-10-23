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

		BufferedReader reader = req.getReader();

		String payload = reader.lines().collect(Collectors.joining());

		if ((payload != null) && !payload.isEmpty()) {
			JSONDeserializer<AnalyticsEventsMessage> messageDeserializer =
				JSONFactoryUtil.createJSONDeserializer();

			AnalyticsEventsMessage analyticsMessage =
				messageDeserializer.deserialize(
					payload, AnalyticsEventsMessage.class);

			Message message = new Message();

			message.setPayload(analyticsMessage);

			MessageBusUtil.sendMessage(
				AnalyticsDestinationNames.ANALYTICS, message);
		}

		resp.setStatus(200);
	}

	private static final long serialVersionUID = 1L;

}