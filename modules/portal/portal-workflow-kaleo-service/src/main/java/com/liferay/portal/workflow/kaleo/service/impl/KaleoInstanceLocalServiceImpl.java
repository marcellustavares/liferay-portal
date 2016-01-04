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

package com.liferay.portal.workflow.kaleo.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.exception.NoSuchInstanceException;
import com.liferay.portal.workflow.kaleo.model.KaleoInstance;
import com.liferay.portal.workflow.kaleo.service.base.KaleoInstanceLocalServiceBaseImpl;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoInstanceQuery;
import com.liferay.portal.workflow.kaleo.util.WorkflowContextUtil;
import com.liferay.portlet.exportimport.staging.StagingUtil;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 * @author Marcellus Tavares
 */
public class KaleoInstanceLocalServiceImpl
	extends KaleoInstanceLocalServiceBaseImpl {

	@Override
	public KaleoInstance addKaleoInstance(
			long kaleoDefinitionId, String kaleoDefinitionName,
			int kaleoDefinitionVersion,
			Map<String, Serializable> workflowContext,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userPersistence.fetchByPrimaryKey(
			serviceContext.getUserId());

		if (user == null) {
			user = userLocalService.getDefaultUser(
				serviceContext.getCompanyId());
		}

		Date now = new Date();

		long kaleoInstanceId = counterLocalService.increment();

		KaleoInstance kaleoInstance = kaleoInstancePersistence.create(
			kaleoInstanceId);

		long groupId = StagingUtil.getLiveGroupId(
			serviceContext.getScopeGroupId());

		kaleoInstance.setGroupId(groupId);

		kaleoInstance.setCompanyId(user.getCompanyId());
		kaleoInstance.setUserId(user.getUserId());
		kaleoInstance.setUserName(user.getFullName());
		kaleoInstance.setCreateDate(now);
		kaleoInstance.setModifiedDate(now);
		kaleoInstance.setKaleoDefinitionId(kaleoDefinitionId);
		kaleoInstance.setKaleoDefinitionName(kaleoDefinitionName);
		kaleoInstance.setKaleoDefinitionVersion(kaleoDefinitionVersion);
		kaleoInstance.setClassName(
			(String)workflowContext.get(
				WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME));

		if (workflowContext.containsKey(
				WorkflowConstants.CONTEXT_ENTRY_CLASS_PK)) {

			kaleoInstance.setClassPK(
				GetterUtil.getLong(
					(String)workflowContext.get(
						WorkflowConstants.CONTEXT_ENTRY_CLASS_PK)));
		}

		kaleoInstance.setCompleted(false);
		kaleoInstance.setWorkflowContext(
			WorkflowContextUtil.convert(workflowContext));

		kaleoInstancePersistence.update(kaleoInstance);

		return kaleoInstance;
	}

	@Override
	public KaleoInstance completeKaleoInstance(long kaleoInstanceId)
		throws PortalException {

		KaleoInstance kaleoInstance = kaleoInstancePersistence.findByPrimaryKey(
			kaleoInstanceId);

		kaleoInstance.setCompleted(true);
		kaleoInstance.setCompletionDate(new Date());

		kaleoInstancePersistence.update(kaleoInstance);

		return kaleoInstance;
	}

	@Override
	public void deleteCompanyKaleoInstances(long companyId) {

		// Kaleo instances

		kaleoInstancePersistence.removeByCompanyId(companyId);

		// Kaleo instance tokens

		kaleoInstanceTokenLocalService.deleteKaleoDefinitionKaleoInstanceTokens(
			companyId);

		// Kaleo logs

		kaleoLogLocalService.deleteKaleoDefinitionKaleoLogs(companyId);

		// Kaleo task instance tokens

		kaleoTaskInstanceTokenLocalService.
			deleteKaleoDefinitionKaleoTaskInstanceTokens(companyId);
	}

	@Override
	public void deleteKaleoDefinitionKaleoInstances(long kaleoDefinitionId) {

		// Kaleo instances

		kaleoInstancePersistence.removeByKaleoDefinitionId(kaleoDefinitionId);

		// Kaleo instance tokens

		kaleoInstanceTokenLocalService.deleteKaleoDefinitionKaleoInstanceTokens(
			kaleoDefinitionId);

		// Kaleo logs

		kaleoLogLocalService.deleteKaleoDefinitionKaleoLogs(kaleoDefinitionId);

		// Kaleo task instance tokens

		kaleoTaskInstanceTokenLocalService.
			deleteKaleoDefinitionKaleoTaskInstanceTokens(kaleoDefinitionId);
	}

	@Override
	public KaleoInstance deleteKaleoInstance(long kaleoInstanceId) {
		KaleoInstance kaleoInstance = null;

		try {
			kaleoInstance = kaleoInstancePersistence.remove(kaleoInstanceId);
		}
		catch (NoSuchInstanceException nsie) {
			return null;
		}

		// Kaleo instance tokens

		kaleoInstanceTokenLocalService.deleteKaleoInstanceKaleoInstanceTokens(
			kaleoInstanceId);

		// Kaleo logs

		kaleoLogLocalService.deleteKaleoInstanceKaleoLogs(kaleoInstanceId);

		// Kaleo task instance tokens

		kaleoTaskInstanceTokenLocalService.
			deleteKaleoInstanceKaleoTaskInstanceTokens(kaleoInstanceId);

		// Kaleo timer instance tokens

		kaleoTimerInstanceTokenLocalService.deleteKaleoTimerInstanceTokens(
			kaleoInstanceId);

		return kaleoInstance;
	}

	@Override
	public List<KaleoInstance> getKaleoInstances(
		Long userId, String assetClassName, Long assetClassPK,
		Boolean completed, int start, int end,
		OrderByComparator<KaleoInstance> orderByComparator,
		ServiceContext serviceContext) {

		KaleoInstanceQuery kaleoInstanceQuery = new KaleoInstanceQuery(
			serviceContext);
		kaleoInstanceQuery.setAssigneeClassName(assetClassName);
		kaleoInstanceQuery.setAssigneeClassPK(assetClassPK);
		kaleoInstanceQuery.setCompleted(completed);
		kaleoInstanceQuery.setEnd(end);
		kaleoInstanceQuery.setOrderByComparator(orderByComparator);
		kaleoInstanceQuery.setStart(start);

		if (Validator.isNotNull(userId)) {
			kaleoInstanceQuery.setUserId(userId);
		}

		return kaleoInstanceFinder.findKaleoInstances(kaleoInstanceQuery);
	}

	@Override
	public List<KaleoInstance> getKaleoInstances(
		Long userId, String[] assetClassNames, Boolean completed, int start,
		int end, OrderByComparator<KaleoInstance> orderByComparator,
		ServiceContext serviceContext) {

		KaleoInstanceQuery kaleoInstanceQuery = new KaleoInstanceQuery(
			serviceContext);
		kaleoInstanceQuery.setAssetTypes(assetClassNames);
		kaleoInstanceQuery.setCompleted(completed);
		kaleoInstanceQuery.setEnd(end);
		kaleoInstanceQuery.setOrderByComparator(orderByComparator);
		kaleoInstanceQuery.setStart(start);

		if (Validator.isNotNull(userId)) {
			kaleoInstanceQuery.setUserId(userId);
		}

		return kaleoInstanceFinder.findKaleoInstances(kaleoInstanceQuery);
	}

	@Override
	public List<KaleoInstance> getKaleoInstances(
		String kaleoDefinitionName, int kaleoDefinitionVersion,
		boolean completed, int start, int end,
		OrderByComparator<KaleoInstance> orderByComparator,
		ServiceContext serviceContext) {

		KaleoInstanceQuery kaleoInstanceQuery = new KaleoInstanceQuery(
			serviceContext);
		kaleoInstanceQuery.setCompleted(completed);
		kaleoInstanceQuery.setEnd(end);
		kaleoInstanceQuery.setKaleoDefinitionName(kaleoDefinitionName);
		kaleoInstanceQuery.setKaleoDefinitionVersion(kaleoDefinitionVersion);
		kaleoInstanceQuery.setOrderByComparator(orderByComparator);
		kaleoInstanceQuery.setStart(start);

		return kaleoInstanceFinder.findKaleoInstances(kaleoInstanceQuery);
	}

	@Override
	public int getKaleoInstancesCount(
		long kaleoDefinitionId, boolean completed) {

		return kaleoInstancePersistence.countByKDI_C(
			kaleoDefinitionId, completed);
	}

	@Override
	public int getKaleoInstancesCount(
		Long userId, String assetClassName, Long assetClassPK,
		Boolean completed, ServiceContext serviceContext) {

		KaleoInstanceQuery kaleoInstanceQuery = new KaleoInstanceQuery(
			serviceContext);
		kaleoInstanceQuery.setAssigneeClassName(assetClassName);
		kaleoInstanceQuery.setAssigneeClassPK(assetClassPK);
		kaleoInstanceQuery.setCompleted(completed);

		if (Validator.isNotNull(userId)) {
			kaleoInstanceQuery.setUserId(userId);
		}

		return kaleoInstanceFinder.countKaleoInstances(kaleoInstanceQuery);
	}

	@Override
	public int getKaleoInstancesCount(
		Long userId, String[] assetClassNames, Boolean completed,
		ServiceContext serviceContext) {

		KaleoInstanceQuery kaleoInstanceQuery = new KaleoInstanceQuery(
			serviceContext);
		kaleoInstanceQuery.setAssetTypes(assetClassNames);
		kaleoInstanceQuery.setCompleted(completed);

		if (Validator.isNotNull(userId)) {
			kaleoInstanceQuery.setUserId(userId);
		}

		return kaleoInstanceFinder.countKaleoInstances(kaleoInstanceQuery);
	}

	@Override
	public int getKaleoInstancesCount(
		String kaleoDefinitionName, int kaleoDefinitionVersion,
		boolean completed, ServiceContext serviceContext) {

		KaleoInstanceQuery kaleoInstanceQuery = new KaleoInstanceQuery(
			serviceContext);
		kaleoInstanceQuery.setCompleted(completed);
		kaleoInstanceQuery.setKaleoDefinitionName(kaleoDefinitionName);
		kaleoInstanceQuery.setKaleoDefinitionVersion(kaleoDefinitionVersion);

		return kaleoInstanceFinder.countKaleoInstances(kaleoInstanceQuery);
	}

	@Override
	public List<KaleoInstance> search(
		Long userId, String assetType, String nodeName,
		String kaleoDefinitionName, Boolean completed, int start, int end,
		OrderByComparator<KaleoInstance> orderByComparator,
		ServiceContext serviceContext) {

		KaleoInstanceQuery kaleoInstanceQuery = new KaleoInstanceQuery(
			serviceContext);
		kaleoInstanceQuery.setAssetTypes(getAssetTypes(assetType));
		kaleoInstanceQuery.setCompleted(completed);
		kaleoInstanceQuery.setEnd(end);
		kaleoInstanceQuery.setNodeName(nodeName);
		kaleoInstanceQuery.setOrderByComparator(orderByComparator);
		kaleoInstanceQuery.setStart(start);
		kaleoInstanceQuery.setKaleoDefinitionName(kaleoDefinitionName);

		if (Validator.isNotNull(userId)) {
			kaleoInstanceQuery.setUserId(userId);
		}

		return kaleoInstanceFinder.findKaleoInstances(kaleoInstanceQuery);
	}

	@Override
	public int searchCount(
		Long userId, String assetType, String nodeName,
		String kaleoDefinitionName, Boolean completed,
		ServiceContext serviceContext) {

		KaleoInstanceQuery kaleoInstanceQuery = new KaleoInstanceQuery(
			serviceContext);
		kaleoInstanceQuery.setAssetTypes(getAssetTypes(assetType));
		kaleoInstanceQuery.setNodeName(nodeName);
		kaleoInstanceQuery.setCompleted(completed);
		kaleoInstanceQuery.setKaleoDefinitionName(kaleoDefinitionName);

		if (Validator.isNotNull(userId)) {
			kaleoInstanceQuery.setUserId(userId);
		}

		return kaleoInstanceFinder.countKaleoInstances(kaleoInstanceQuery);
	}

	@Override
	public KaleoInstance updateKaleoInstance(
			long kaleoInstanceId, Map<String, Serializable> workflowContext,
			ServiceContext serviceContext)
		throws PortalException {

		KaleoInstance kaleoInstance = kaleoInstancePersistence.findByPrimaryKey(
			kaleoInstanceId);

		kaleoInstance.setWorkflowContext(
			WorkflowContextUtil.convert(workflowContext));

		kaleoInstancePersistence.update(kaleoInstance);

		return kaleoInstance;
	}

	protected String[] getAssetTypes(String assetType) {
		if (Validator.isNull(assetType)) {
			return null;
		}

		return new String[] {assetType};
	}

}