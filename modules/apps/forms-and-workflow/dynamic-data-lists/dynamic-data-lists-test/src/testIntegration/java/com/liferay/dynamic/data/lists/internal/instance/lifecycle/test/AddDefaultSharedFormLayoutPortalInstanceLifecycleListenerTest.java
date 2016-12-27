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

package com.liferay.dynamic.data.lists.internal.instance.lifecycle.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adam Brandizzi
 */
@RunWith(Arquillian.class)
@Sync
public class AddDefaultSharedFormLayoutPortalInstanceLifecycleListenerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		setUpCompany();

		setUpPortalInstanceLifecycleListener();
	}

	@Test
	public void testCompanyRegistrationCreateGroup() throws Exception {
		_portalInstanceLifecycleListener.portalInstanceRegistered(_company);

		Group group = GroupLocalServiceUtil.fetchFriendlyURLGroup(
			_company.getCompanyId(), "/forms");

		Assert.assertNotNull(group);
	}

	@Test
	public void testCompanyRegistrationCreateRestrictedLayout()
		throws Exception {

		_portalInstanceLifecycleListener.portalInstanceRegistered(_company);

		Group group = GroupLocalServiceUtil.fetchFriendlyURLGroup(
			_company.getCompanyId(), "/forms");

		Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(
			group.getGroupId(), true, "/shared");

		Assert.assertNotNull(layout);
	}

	@Test
	public void testCompanyRegistrationCreateSharedLayout() throws Exception {
		_portalInstanceLifecycleListener.portalInstanceRegistered(_company);

		Group group = GroupLocalServiceUtil.fetchFriendlyURLGroup(
			_company.getCompanyId(), "/forms");

		Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(
			group.getGroupId(), false, "/shared");

		Assert.assertNotNull(layout);
	}

	protected void setUpCompany() throws Exception {
		_company = CompanyTestUtil.addCompany();
	}

	protected void setUpPortalInstanceLifecycleListener() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		Collection<PortalInstanceLifecycleListener> listeners =
			registry.getServices(PortalInstanceLifecycleListener.class, null);

		for (PortalInstanceLifecycleListener listener : listeners) {
			String className = listener.getClass().getName();

			if (className.contains("AddDefaultSharedFormLayout")) {
				_portalInstanceLifecycleListener = listener;
			}
		}
	}

	@DeleteAfterTestRun
	private Company _company;

	private PortalInstanceLifecycleListener _portalInstanceLifecycleListener;

}