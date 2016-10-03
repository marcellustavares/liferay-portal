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

package com.liferay.dynamic.data.lists.internal.search.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.lists.helper.DDLRecordSetTestHelper;
import com.liferay.dynamic.data.lists.helper.DDLRecordTestHelper;
import com.liferay.dynamic.data.lists.internal.search.DDLRecordIndexer;
import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.test.IdempotentRetryAssert;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.SearchContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.permission.SimplePermissionChecker;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Inácio Nery
 */
@RunWith(Arquillian.class)
@Sync
public class DDLRecordIndexerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		setUpPermissionThreadLocal();
		setUpPrincipalThreadLocal();

		_group = GroupTestUtil.addGroup();
		_user = UserTestUtil.addUser();

		setUpSearchContext(_group, _user);
	}

	@After
	public void tearDown() {
		PermissionThreadLocal.setPermissionChecker(_originalPermissionChecker);

		PrincipalThreadLocal.setName(_originalName);
	}

	@Test
	public void testSearch() throws Exception {
		setUpSearchContext(_group, TestPropsValues.getUser());

		String title = RandomTestUtil.randomString();

		addDDLRecord(title);

		assertSearchHitsLength(title, 1);
	}

	@Test
	public void testSearchNotAdmin() throws Exception {
		setUpSearchContext(_group, _user);

		String title = RandomTestUtil.randomString();

		addDDLRecord(title);

		assertSearchHitsLength(title, 1);
	}

	protected static SearchContext getSearchContext(Group group, User user)
		throws Exception {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext(
			group.getGroupId());

		searchContext.setUserId(user.getUserId());

		return searchContext;
	}

	protected void addDDLRecord(String title) throws Exception {
		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), DDLRecordSet.class.getName());

		DDLRecordSetTestHelper ddlRecordSetTestHelper =
			new DDLRecordSetTestHelper(_group);

		DDLRecordSet recordSet = ddlRecordSetTestHelper.addRecordSet(
			ddmStructure);

		DDLRecordTestHelper ddlRecordTestHelper = new DDLRecordTestHelper(
			_group, recordSet);

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmStructure.getDDMForm(),
			DDMFormValuesTestUtil.createAvailableLocales(LocaleUtil.US),
			LocaleUtil.US);

		DDMFormFieldValue nameDDMFormFieldValue =
			DDMFormValuesTestUtil.createLocalizedDDMFormFieldValue(
				"name", title);

		ddmFormValues.addDDMFormFieldValue(nameDDMFormFieldValue);

		ddlRecordTestHelper.addRecord(
			ddmFormValues, WorkflowConstants.ACTION_PUBLISH);
	}

	protected void assertSearchHitsLength(
			final String keywords, final int expectedLength)
		throws Exception {

		IdempotentRetryAssert.retryAssert(
			3, TimeUnit.SECONDS,
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					_searchContext.setKeywords(
						StringUtil.toLowerCase(keywords));

					Indexer<DDLRecord> indexer = new DDLRecordIndexer();

					Hits hits = indexer.search(_searchContext);

					Assert.assertEquals(expectedLength, hits.getLength());

					return null;
				}

			});
	}

	protected void setUpPermissionThreadLocal() throws Exception {
		_originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		PermissionThreadLocal.setPermissionChecker(
			new SimplePermissionChecker() {
				{
					init(TestPropsValues.getUser());
				}

				@Override
				public boolean hasOwnerPermission(
					long companyId, String name, String primKey, long ownerId,
					String actionId) {

					return true;
				}

			});
	}

	protected void setUpPrincipalThreadLocal() throws Exception {
		_originalName = PrincipalThreadLocal.getName();

		PrincipalThreadLocal.setName(TestPropsValues.getUserId());
	}

	protected void setUpSearchContext(Group group, User user) throws Exception {
		_searchContext = getSearchContext(_group, _user);
	}

	@DeleteAfterTestRun
	private Group _group;

	private String _originalName;
	private PermissionChecker _originalPermissionChecker;
	private SearchContext _searchContext;

	@DeleteAfterTestRun
	private User _user;

}