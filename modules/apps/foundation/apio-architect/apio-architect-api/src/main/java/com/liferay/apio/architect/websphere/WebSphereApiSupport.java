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

package com.liferay.apio.architect.websphere;

import java.io.InputStream;

import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * Add support for some WebSphere versions where the {@code
 * javax.annotation-api.jar} is not correctly provided.
 *
 * @author Carlos Sierra Andrés
 * @review
 */
public class WebSphereApiSupport implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if (_isWebSphere()) {
			Bundle bundle = bundleContext.getBundle();

			URL url = bundle.getResource("javax.annotation-api.jar");

			try (InputStream inputStream = url.openStream()) {
				_bundle = bundleContext.installBundle(
					"javax.annotation-api", inputStream);

				_bundle.start();
			}
			catch (BundleException be) {
				be.printStackTrace();
			}
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		try {
			if (_bundle != null) {
				_bundle.stop();

				_bundle.uninstall();
			}
		}
		catch (BundleException be) {
			be.printStackTrace();
		}
	}

	private boolean _isWebSphere() {
		return true;
	}

	private Bundle _bundle;

}