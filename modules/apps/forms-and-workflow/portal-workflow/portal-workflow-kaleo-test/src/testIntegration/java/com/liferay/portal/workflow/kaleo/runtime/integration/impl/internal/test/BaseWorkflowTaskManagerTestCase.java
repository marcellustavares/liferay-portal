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

package com.liferay.portal.workflow.kaleo.runtime.integration.impl.internal.test;

import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordConstants;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetConstants;
import com.liferay.dynamic.data.lists.service.DDLRecordLocalServiceUtil;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestHelper;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserNotificationDeliveryConstants;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.UserNotificationEventLocalServiceUtil;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowInstance;
import com.liferay.portal.kernel.workflow.WorkflowInstanceManagerUtil;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;

/**
 * @author Inácio Nery
 */
public class BaseWorkflowTaskManagerTestCase {

	@Before
	public void setUp() throws Exception {
		group = GroupTestUtil.addGroup();

		serviceContext = ServiceContextTestUtil.getServiceContext(
			group.getGroupId());

		setUpUsers();
	}

	protected void activeSingleApproverWorkflow(String className, long classPK)
		throws PortalException {

		WorkflowDefinitionLinkLocalServiceUtil.updateWorkflowDefinitionLink(
			adminUser.getUserId(), TestPropsValues.getCompanyId(),
			group.getGroupId(), className, classPK, 0, "Single Approver@1");
	}

	protected BlogsEntry addBlogsEntry() throws PortalException {
		BlogsEntry blogsEntry = BlogsEntryLocalServiceUtil.addEntry(
			adminUser.getUserId(), StringUtil.randomString(),
			StringUtil.randomString(), new Date(), serviceContext);

		_blogsEntries.add(blogsEntry);

		return blogsEntry;
	}

	protected DDLRecord addRecord(DDLRecordSet recordSet)
		throws PortalException {

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm("TextField1");

		DDMFormValues ddmFormValues = createDDMFormValues(ddmForm);

		DDLRecord record = DDLRecordLocalServiceUtil.addRecord(
			adminUser.getUserId(), group.getGroupId(),
			recordSet.getRecordSetId(),
			DDLRecordConstants.DISPLAY_INDEX_DEFAULT, ddmFormValues,
			serviceContext);

		_records.add(record);

		return record;
	}

	protected DDLRecordSet addRecordSet() throws Exception, PortalException {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm("TextField1");

		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(DDLRecordSet.class), group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			ddmForm, StorageType.JSON.toString());

		Map<Locale, String> nameMap = new HashMap<>();

		nameMap.put(LocaleUtil.US, RandomTestUtil.randomString());

		DDLRecordSet recordSet = DDLRecordSetLocalServiceUtil.addRecordSet(
			adminUser.getUserId(), group.getGroupId(),
			ddmStructure.getStructureId(), null, nameMap, null,
			DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT,
			DDLRecordSetConstants.SCOPE_DYNAMIC_DATA_LISTS, serviceContext);

		_recordSets.add(recordSet);

		return recordSet;
	}

	protected void approveWorkflowTask(User user) throws Exception {
		WorkflowTask workflowTask = getWorkflowTask();

		PermissionChecker userPermissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionThreadLocal.setPermissionChecker(userPermissionChecker);

		WorkflowTaskManagerUtil.completeWorkflowTask(
			group.getCompanyId(), user.getUserId(),
			workflowTask.getWorkflowTaskId(), "approve", StringPool.BLANK,
			null);
	}

	protected void assignWorkflowTaskToUser(User user, User assigneeUser)
		throws Exception {

		WorkflowTask workflowTask = getWorkflowTask();

		PermissionChecker userPermissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionThreadLocal.setPermissionChecker(userPermissionChecker);

		WorkflowTaskManagerUtil.assignWorkflowTaskToUser(
			group.getCompanyId(), user.getUserId(),
			workflowTask.getWorkflowTaskId(), assigneeUser.getUserId(),
			StringPool.BLANK, null, null);
	}

	protected void checkUserNotificationEventsByUsers(User... users) {
		for (User user : users) {
			List<UserNotificationEvent> userNotificationEvents =
				UserNotificationEventLocalServiceUtil.
					getArchivedUserNotificationEvents(
						user.getUserId(),
						UserNotificationDeliveryConstants.TYPE_WEBSITE, false);

			_userNotificationEvents.addAll(userNotificationEvents);

			Assert.assertEquals(1, userNotificationEvents.size());

			UserNotificationEvent userNotificationEvent =
				userNotificationEvents.get(0);

			userNotificationEvent.setArchived(true);

			UserNotificationEventLocalServiceUtil.updateUserNotificationEvent(
				userNotificationEvent);
		}
	}

	protected void checkWorkflowInstance(String className, long classPK)
		throws WorkflowException {

		List<WorkflowInstance> workflowInstances =
			WorkflowInstanceManagerUtil.getWorkflowInstances(
				adminUser.getCompanyId(), adminUser.getUserId(), className,
				classPK, true, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Assert.assertEquals(1, workflowInstances.size());
	}

	protected DDMFormValues createDDMFormValues(DDMForm ddmForm) {
		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createLocalizedDDMFormFieldValue(
				"TextField1", StringPool.BLANK);

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		return ddmFormValues;
	}

	protected User createUser(String roleName) throws Exception {
		User user = UserTestUtil.addUser(group.getGroupId());

		Role role = RoleLocalServiceUtil.getRole(
			TestPropsValues.getCompanyId(), roleName);

		UserLocalServiceUtil.addRoleUser(role.getRoleId(), user);

		long[] userIds = {user.getUserId()};

		UserGroupRoleLocalServiceUtil.addUserGroupRoles(
			userIds, group.getGroupId(), role.getRoleId());

		return user;
	}

	protected void deactiveWorkflow(String className, long classPK)
		throws PortalException {

		WorkflowDefinitionLinkLocalServiceUtil.updateWorkflowDefinitionLink(
			adminUser.getUserId(), TestPropsValues.getCompanyId(),
			group.getGroupId(), className, classPK, 0, null);
	}

	protected WorkflowTask getWorkflowTask() throws WorkflowException {
		List<WorkflowTask> workflowTasks =
			WorkflowTaskManagerUtil.getWorkflowTasksBySubmittingUser(
				adminUser.getCompanyId(), adminUser.getUserId(), false,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Assert.assertEquals(1, workflowTasks.size());

		return workflowTasks.get(0);
	}

	protected void setUpUsers() throws Exception {
		adminUser = createUser(RoleConstants.ADMINISTRATOR);

		portalContentReviewerUser = createUser(
			RoleConstants.PORTAL_CONTENT_REVIEWER);

		siteAdminUser = createUser(RoleConstants.SITE_ADMINISTRATOR);
	}

	@DeleteAfterTestRun
	protected User adminUser;

	@DeleteAfterTestRun
	protected Group group;

	@DeleteAfterTestRun
	protected User portalContentReviewerUser;

	protected ServiceContext serviceContext;

	@DeleteAfterTestRun
	protected User siteAdminUser;

	@DeleteAfterTestRun
	private final List<BlogsEntry> _blogsEntries = new ArrayList<>();

	@DeleteAfterTestRun
	private final List<DDLRecord> _records = new ArrayList<>();

	@DeleteAfterTestRun
	private final List<DDLRecordSet> _recordSets = new ArrayList<>();

	@DeleteAfterTestRun
	private final List<UserNotificationEvent> _userNotificationEvents =
		new ArrayList<>();

}