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

package com.liferay.dynamic.data.mapping.internal;

import com.liferay.portlet.dynamicdatamapping.DDMTemplate;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.exportimport.lar.StagedModelType;

import java.io.Serializable;

import java.util.Date;

/**
 * @author Marcellus Tavares
 * @author Rafael Praxedes
 */
public class DDMTemplateImpl implements DDMTemplate {

	public DDMTemplateImpl(
		com.liferay.dynamic.data.mapping.model.DDMTemplate ddmTemplate) {

		_ddmTemplate = ddmTemplate;
	}

	@Override
	public Object clone() {
		DDMTemplateImpl ddmTemplateImpl = new DDMTemplateImpl(
			(com.liferay.dynamic.data.mapping.model.DDMTemplate)
				_ddmTemplate.clone());

		return ddmTemplateImpl;
	}

	@Override
	public long getCompanyId() {
		return _ddmTemplate.getCompanyId();
	}

	@Override
	public Date getCreateDate() {
		return _ddmTemplate.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _ddmTemplate.getExpandoBridge();
	}

	@Override
	public long getGroupId() {
		return _ddmTemplate.getGroupId();
	}

	@Override
	public Date getLastPublishDate() {
		return _ddmTemplate.getLastPublishDate();
	}

	@Override
	public Class<?> getModelClass() {
		return _ddmTemplate.getModelClass();
	}

	@Override
	public String getModelClassName() {
		return _ddmTemplate.getModelClassName();
	}

	@Override
	public Date getModifiedDate() {
		return _ddmTemplate.getModifiedDate();
	}

	@Override
	public long getPrimaryKey() {
		return _ddmTemplate.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _ddmTemplate.getPrimaryKeyObj();
	}

	@Override
	public String getScript() {
		return _ddmTemplate.getScript();
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _ddmTemplate.getStagedModelType();
	}

	@Override
	public long getTemplateId() {
		return _ddmTemplate.getTemplateId();
	}

	@Override
	public String getTemplateKey() {
		return _ddmTemplate.getTemplateKey();
	}

	@Override
	public String getType() {
		return _ddmTemplate.getType();
	}

	@Override
	public long getUserId() {
		return _ddmTemplate.getUserId();
	}

	@Override
	public String getUserName() {
		return _ddmTemplate.getUserName();
	}

	@Override
	public String getUserUuid() {
		return _ddmTemplate.getUserUuid();
	}

	@Override
	public String getUuid() {
		return _ddmTemplate.getUuid();
	}

	@Override
	public void setCompanyId(long companyId) {
		_ddmTemplate.setCompanyId(companyId);
	}

	@Override
	public void setCreateDate(Date createDate) {
		_ddmTemplate.setCreateDate(createDate);
	}

	@Override
	public void setGroupId(long groupId) {
		_ddmTemplate.setGroupId(groupId);
	}

	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_ddmTemplate.setLastPublishDate(lastPublishDate);
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_ddmTemplate.setModifiedDate(modifiedDate);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_ddmTemplate.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public void setUserId(long userId) {
		_ddmTemplate.setUserId(userId);
	}

	@Override
	public void setUserName(String userName) {
		_ddmTemplate.setUserName(userName);
	}

	@Override
	public void setUserUuid(String userUuid) {
		_ddmTemplate.setUserUuid(userUuid);
	}

	@Override
	public void setUuid(String uuid) {
		_ddmTemplate.setUuid(uuid);
	}

	private final com.liferay.dynamic.data.mapping.model.DDMTemplate
		_ddmTemplate;

}