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

package com.liferay.portal.test.rule.callback;

import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;

import java.util.concurrent.CountDownLatch;

import org.junit.runner.Description;

/**
 * @author Shuyang Zhou
 */
public class HotDeployAwaitTestCallback extends BaseTestCallback<Void, Void> {

	public static final HotDeployAwaitTestCallback INSTANCE =
		new HotDeployAwaitTestCallback();

	@Override
	public Void beforeClass(Description description)
		throws InterruptedException {

		Destination destination = MessageBusUtil.getDestination(
			DestinationNames.HOT_DEPLOY);

		final CountDownLatch countDownLatch = new CountDownLatch(1);

		final Message countDownMessage = new Message();

		destination.register(
			new MessageListener() {

				@Override
				public void receive(Message message) {
					if (countDownMessage == message) {
						countDownLatch.countDown();
					}
				}

			});

		destination.send(countDownMessage);

		countDownLatch.await();

		return null;
	}

}