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

package com.liferay.portal.portlet.bridge.soy.internal;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCCommandCache;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.util.HtmlImpl;

import java.net.URL;

import org.junit.Before;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Marcellus Tavares
 */
@PrepareForTest(FrameworkUtil.class)
@RunWith(PowerMockRunner.class)
public class SoyPortletHelperTest {

	@Before
	public void setUpFrameworkUtil() {
		setUpJSONFactoryUtil();

		PowerMockito.spy(FrameworkUtil.class);
	}

	@Before
	public void setUpHtmlUtil() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(new HtmlImpl());
	}

	@Before
	public void setUpJSONFactoryUtil() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

	private Bundle _mockBundleWithoutPackage() {
		Bundle bundle = Mockito.mock(Bundle.class);

		Mockito.when(
			bundle.getEntry("package.json")
		).then(
			new Answer<URL>() {

				public URL answer(InvocationOnMock invocationOnMock) {
					return null;
				}

			}
		);

		return bundle;
	}

	private Bundle _mockBundleWithPackageFile(final String packageFile) {
		Bundle bundle = Mockito.mock(Bundle.class);

		Mockito.when(
			bundle.getEntry(Matchers.endsWith("package.json"))
		).then(
			new Answer<URL>() {

				public URL answer(InvocationOnMock invocationOnMock) {
					return SoyPortletHelperTest.class.getResource(
						"dependencies/" + packageFile);
				}

			}
		);

		return bundle;
	}

	private MVCCommandCache _mockEmptyMVCCommandCache() {
		MVCCommandCache mvcCommandCache = Mockito.mock(MVCCommandCache.class);

		Mockito.when(
			mvcCommandCache.getMVCCommand(Matchers.anyString())
		).then(
			new Answer<MVCCommand>() {

				public MVCCommand answer(InvocationOnMock invocationOnMock)
					throws Throwable {

					return MVCRenderCommand.EMPTY;
				}

			}
		);

		return mvcCommandCache;
	}

	private MVCCommandCache _mockMVCCommandCacheWithSingleCommand(
		Bundle bundle, final String controllerName) {

		MVCCommandCache mvcCommandCache = Mockito.mock(MVCCommandCache.class);

		final MVCRenderCommand mvcRenderCommand = Mockito.mock(
			MVCRenderCommand.class);

		Mockito.when(
			mvcCommandCache.getMVCCommand(controllerName)
		).thenReturn(
			mvcRenderCommand
		);

		Mockito.when(
			FrameworkUtil.getBundle(mvcRenderCommand.getClass())
		).thenReturn(
			bundle
		);

		return mvcCommandCache;
	}

}