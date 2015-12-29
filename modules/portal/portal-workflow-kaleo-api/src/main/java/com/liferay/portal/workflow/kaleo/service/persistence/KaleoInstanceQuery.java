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

package com.liferay.portal.workflow.kaleo.service.persistence;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.model.KaleoInstance;

/**
 * @author Inácio Nery
 */
public class KaleoInstanceQuery {

	public KaleoInstanceQuery(ServiceContext serviceContext) {
		_serviceContext = serviceContext;
		_companyId = serviceContext.getCompanyId();
		_userId = serviceContext.getUserId();
	}

	public String[] getAssetTypes() {
		return _assetTypes;
	}

	public String getAssigneeClassName() {
		return _assigneeClassName;
	}

	public Long getAssigneeClassPK() {
		return _assigneeClassPK;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public int getEnd() {
		return _end;
	}

	public String getKaleoDefinitionName() {
		return _kaleoDefinitionName;
	}

	public Integer getKaleoDefinitionVersion() {
		return _kaleoDefinitionVersion;
	}

	public String getNodeName() {
		return _nodeName;
	}

	public OrderByComparator<KaleoInstance> getOrderByComparator() {
		return _orderByComparator;
	}

	public ServiceContext getServiceContext() {
		return _serviceContext;
	}

	public int getStart() {
		return _start;
	}

	public long getUserId() {
		return _userId;
	}

	public Boolean isCompleted() {
		return _completed;
	}

	public void setAssetTypes(String[] assetTypes) {
		_assetTypes = assetTypes;
	}

	public void setAssigneeClassName(String assigneeClassName) {
		_assigneeClassName = assigneeClassName;
	}

	public void setAssigneeClassPK(Long assigneeClassPK) {
		_assigneeClassPK = assigneeClassPK;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public void setCompleted(Boolean completed) {
		_completed = completed;
	}

	public void setEnd(int end) {
		_end = end;
	}

	public void setKaleoDefinitionName(String kaleoDefinitionName) {
		_kaleoDefinitionName = kaleoDefinitionName;
	}

	public void setKaleoDefinitionVersion(Integer kaleoDefinitionVersion) {
		_kaleoDefinitionVersion = kaleoDefinitionVersion;
	}

	public void setNodeName(String nodeName) {
		_nodeName = nodeName;
	}

	public void setOrderByComparator(
		OrderByComparator<KaleoInstance> orderByComparator) {

		_orderByComparator = orderByComparator;
	}

	public void setServiceContext(ServiceContext serviceContext) {
		_serviceContext = serviceContext;
	}

	public void setStart(int start) {
		_start = start;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	private String[] _assetTypes;
	private String _assigneeClassName;
	private Long _assigneeClassPK;
	private long _companyId;
	private Boolean _completed;
	private int _end = QueryUtil.ALL_POS;
	private String _kaleoDefinitionName;
	private Integer _kaleoDefinitionVersion;
	private String _nodeName;
	private OrderByComparator<KaleoInstance> _orderByComparator;
	private ServiceContext _serviceContext;
	private int _start = QueryUtil.ALL_POS;
	private long _userId;

}