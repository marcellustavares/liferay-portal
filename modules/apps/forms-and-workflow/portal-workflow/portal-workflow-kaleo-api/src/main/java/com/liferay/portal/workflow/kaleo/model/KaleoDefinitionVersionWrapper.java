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

package com.liferay.portal.workflow.kaleo.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link KaleoDefinitionVersion}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDefinitionVersion
 * @generated
 */
@ProviderType
public class KaleoDefinitionVersionWrapper implements KaleoDefinitionVersion,
	ModelWrapper<KaleoDefinitionVersion> {
	public KaleoDefinitionVersionWrapper(
		KaleoDefinitionVersion kaleoDefinitionVersion) {
		_kaleoDefinitionVersion = kaleoDefinitionVersion;
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoDefinitionVersion.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoDefinitionVersion.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("kaleoDefinitionVersionId", getKaleoDefinitionVersionId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("name", getName());
		attributes.put("title", getTitle());
		attributes.put("content", getContent());
		attributes.put("version", getVersion());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long kaleoDefinitionVersionId = (Long)attributes.get(
				"kaleoDefinitionVersionId");

		if (kaleoDefinitionVersionId != null) {
			setKaleoDefinitionVersionId(kaleoDefinitionVersionId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String title = (String)attributes.get("title");

		if (title != null) {
			setTitle(title);
		}

		String content = (String)attributes.get("content");

		if (content != null) {
			setContent(content);
		}

		Integer version = (Integer)attributes.get("version");

		if (version != null) {
			setVersion(version);
		}
	}

	@Override
	public KaleoDefinitionVersion toEscapedModel() {
		return new KaleoDefinitionVersionWrapper(_kaleoDefinitionVersion.toEscapedModel());
	}

	@Override
	public KaleoDefinitionVersion toUnescapedModel() {
		return new KaleoDefinitionVersionWrapper(_kaleoDefinitionVersion.toUnescapedModel());
	}

	@Override
	public boolean isCachedModel() {
		return _kaleoDefinitionVersion.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _kaleoDefinitionVersion.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _kaleoDefinitionVersion.isNew();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _kaleoDefinitionVersion.getExpandoBridge();
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<KaleoDefinitionVersion> toCacheModel() {
		return _kaleoDefinitionVersion.toCacheModel();
	}

	@Override
	public int compareTo(KaleoDefinitionVersion kaleoDefinitionVersion) {
		return _kaleoDefinitionVersion.compareTo(kaleoDefinitionVersion);
	}

	/**
	* Returns the version of this kaleo definition version.
	*
	* @return the version of this kaleo definition version
	*/
	@Override
	public int getVersion() {
		return _kaleoDefinitionVersion.getVersion();
	}

	@Override
	public int hashCode() {
		return _kaleoDefinitionVersion.hashCode();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoDefinitionVersion.getPrimaryKeyObj();
	}

	@Override
	public java.lang.Object clone() {
		return new KaleoDefinitionVersionWrapper((KaleoDefinitionVersion)_kaleoDefinitionVersion.clone());
	}

	/**
	* Returns the content of this kaleo definition version.
	*
	* @return the content of this kaleo definition version
	*/
	@Override
	public java.lang.String getContent() {
		return _kaleoDefinitionVersion.getContent();
	}

	@Override
	public java.lang.String getDefaultLanguageId() {
		return _kaleoDefinitionVersion.getDefaultLanguageId();
	}

	/**
	* Returns the name of this kaleo definition version.
	*
	* @return the name of this kaleo definition version
	*/
	@Override
	public java.lang.String getName() {
		return _kaleoDefinitionVersion.getName();
	}

	/**
	* Returns the title of this kaleo definition version.
	*
	* @return the title of this kaleo definition version
	*/
	@Override
	public java.lang.String getTitle() {
		return _kaleoDefinitionVersion.getTitle();
	}

	/**
	* Returns the localized title of this kaleo definition version in the language. Uses the default language if no localization exists for the requested language.
	*
	* @param languageId the ID of the language
	* @return the localized title of this kaleo definition version
	*/
	@Override
	public java.lang.String getTitle(java.lang.String languageId) {
		return _kaleoDefinitionVersion.getTitle(languageId);
	}

	/**
	* Returns the localized title of this kaleo definition version in the language, optionally using the default language if no localization exists for the requested language.
	*
	* @param languageId the ID of the language
	* @param useDefault whether to use the default language if no localization exists for the requested language
	* @return the localized title of this kaleo definition version
	*/
	@Override
	public java.lang.String getTitle(java.lang.String languageId,
		boolean useDefault) {
		return _kaleoDefinitionVersion.getTitle(languageId, useDefault);
	}

	/**
	* Returns the localized title of this kaleo definition version in the language. Uses the default language if no localization exists for the requested language.
	*
	* @param locale the locale of the language
	* @return the localized title of this kaleo definition version
	*/
	@Override
	public java.lang.String getTitle(java.util.Locale locale) {
		return _kaleoDefinitionVersion.getTitle(locale);
	}

	/**
	* Returns the localized title of this kaleo definition version in the language, optionally using the default language if no localization exists for the requested language.
	*
	* @param locale the local of the language
	* @param useDefault whether to use the default language if no localization exists for the requested language
	* @return the localized title of this kaleo definition version. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	*/
	@Override
	public java.lang.String getTitle(java.util.Locale locale, boolean useDefault) {
		return _kaleoDefinitionVersion.getTitle(locale, useDefault);
	}

	@Override
	public java.lang.String getTitleCurrentLanguageId() {
		return _kaleoDefinitionVersion.getTitleCurrentLanguageId();
	}

	@Override
	public java.lang.String getTitleCurrentValue() {
		return _kaleoDefinitionVersion.getTitleCurrentValue();
	}

	/**
	* Returns the user name of this kaleo definition version.
	*
	* @return the user name of this kaleo definition version
	*/
	@Override
	public java.lang.String getUserName() {
		return _kaleoDefinitionVersion.getUserName();
	}

	/**
	* Returns the user uuid of this kaleo definition version.
	*
	* @return the user uuid of this kaleo definition version
	*/
	@Override
	public java.lang.String getUserUuid() {
		return _kaleoDefinitionVersion.getUserUuid();
	}

	@Override
	public java.lang.String toString() {
		return _kaleoDefinitionVersion.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _kaleoDefinitionVersion.toXmlString();
	}

	@Override
	public java.lang.String[] getAvailableLanguageIds() {
		return _kaleoDefinitionVersion.getAvailableLanguageIds();
	}

	/**
	* Returns the create date of this kaleo definition version.
	*
	* @return the create date of this kaleo definition version
	*/
	@Override
	public Date getCreateDate() {
		return _kaleoDefinitionVersion.getCreateDate();
	}

	/**
	* Returns the modified date of this kaleo definition version.
	*
	* @return the modified date of this kaleo definition version
	*/
	@Override
	public Date getModifiedDate() {
		return _kaleoDefinitionVersion.getModifiedDate();
	}

	/**
	* Returns a map of the locales and localized titles of this kaleo definition version.
	*
	* @return the locales and localized titles of this kaleo definition version
	*/
	@Override
	public Map<java.util.Locale, java.lang.String> getTitleMap() {
		return _kaleoDefinitionVersion.getTitleMap();
	}

	/**
	* Returns the company ID of this kaleo definition version.
	*
	* @return the company ID of this kaleo definition version
	*/
	@Override
	public long getCompanyId() {
		return _kaleoDefinitionVersion.getCompanyId();
	}

	/**
	* Returns the group ID of this kaleo definition version.
	*
	* @return the group ID of this kaleo definition version
	*/
	@Override
	public long getGroupId() {
		return _kaleoDefinitionVersion.getGroupId();
	}

	/**
	* Returns the kaleo definition version ID of this kaleo definition version.
	*
	* @return the kaleo definition version ID of this kaleo definition version
	*/
	@Override
	public long getKaleoDefinitionVersionId() {
		return _kaleoDefinitionVersion.getKaleoDefinitionVersionId();
	}

	/**
	* Returns the primary key of this kaleo definition version.
	*
	* @return the primary key of this kaleo definition version
	*/
	@Override
	public long getPrimaryKey() {
		return _kaleoDefinitionVersion.getPrimaryKey();
	}

	/**
	* Returns the user ID of this kaleo definition version.
	*
	* @return the user ID of this kaleo definition version
	*/
	@Override
	public long getUserId() {
		return _kaleoDefinitionVersion.getUserId();
	}

	@Override
	public void persist() {
		_kaleoDefinitionVersion.persist();
	}

	@Override
	public void prepareLocalizedFieldsForImport()
		throws com.liferay.portal.kernel.exception.LocaleException {
		_kaleoDefinitionVersion.prepareLocalizedFieldsForImport();
	}

	@Override
	public void prepareLocalizedFieldsForImport(
		java.util.Locale defaultImportLocale)
		throws com.liferay.portal.kernel.exception.LocaleException {
		_kaleoDefinitionVersion.prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_kaleoDefinitionVersion.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this kaleo definition version.
	*
	* @param companyId the company ID of this kaleo definition version
	*/
	@Override
	public void setCompanyId(long companyId) {
		_kaleoDefinitionVersion.setCompanyId(companyId);
	}

	/**
	* Sets the content of this kaleo definition version.
	*
	* @param content the content of this kaleo definition version
	*/
	@Override
	public void setContent(java.lang.String content) {
		_kaleoDefinitionVersion.setContent(content);
	}

	/**
	* Sets the create date of this kaleo definition version.
	*
	* @param createDate the create date of this kaleo definition version
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_kaleoDefinitionVersion.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_kaleoDefinitionVersion.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_kaleoDefinitionVersion.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_kaleoDefinitionVersion.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this kaleo definition version.
	*
	* @param groupId the group ID of this kaleo definition version
	*/
	@Override
	public void setGroupId(long groupId) {
		_kaleoDefinitionVersion.setGroupId(groupId);
	}

	/**
	* Sets the kaleo definition version ID of this kaleo definition version.
	*
	* @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo definition version
	*/
	@Override
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		_kaleoDefinitionVersion.setKaleoDefinitionVersionId(kaleoDefinitionVersionId);
	}

	/**
	* Sets the modified date of this kaleo definition version.
	*
	* @param modifiedDate the modified date of this kaleo definition version
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_kaleoDefinitionVersion.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this kaleo definition version.
	*
	* @param name the name of this kaleo definition version
	*/
	@Override
	public void setName(java.lang.String name) {
		_kaleoDefinitionVersion.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_kaleoDefinitionVersion.setNew(n);
	}

	/**
	* Sets the primary key of this kaleo definition version.
	*
	* @param primaryKey the primary key of this kaleo definition version
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_kaleoDefinitionVersion.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_kaleoDefinitionVersion.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the title of this kaleo definition version.
	*
	* @param title the title of this kaleo definition version
	*/
	@Override
	public void setTitle(java.lang.String title) {
		_kaleoDefinitionVersion.setTitle(title);
	}

	/**
	* Sets the localized title of this kaleo definition version in the language.
	*
	* @param title the localized title of this kaleo definition version
	* @param locale the locale of the language
	*/
	@Override
	public void setTitle(java.lang.String title, java.util.Locale locale) {
		_kaleoDefinitionVersion.setTitle(title, locale);
	}

	/**
	* Sets the localized title of this kaleo definition version in the language, and sets the default locale.
	*
	* @param title the localized title of this kaleo definition version
	* @param locale the locale of the language
	* @param defaultLocale the default locale
	*/
	@Override
	public void setTitle(java.lang.String title, java.util.Locale locale,
		java.util.Locale defaultLocale) {
		_kaleoDefinitionVersion.setTitle(title, locale, defaultLocale);
	}

	@Override
	public void setTitleCurrentLanguageId(java.lang.String languageId) {
		_kaleoDefinitionVersion.setTitleCurrentLanguageId(languageId);
	}

	/**
	* Sets the localized titles of this kaleo definition version from the map of locales and localized titles.
	*
	* @param titleMap the locales and localized titles of this kaleo definition version
	*/
	@Override
	public void setTitleMap(Map<java.util.Locale, java.lang.String> titleMap) {
		_kaleoDefinitionVersion.setTitleMap(titleMap);
	}

	/**
	* Sets the localized titles of this kaleo definition version from the map of locales and localized titles, and sets the default locale.
	*
	* @param titleMap the locales and localized titles of this kaleo definition version
	* @param defaultLocale the default locale
	*/
	@Override
	public void setTitleMap(Map<java.util.Locale, java.lang.String> titleMap,
		java.util.Locale defaultLocale) {
		_kaleoDefinitionVersion.setTitleMap(titleMap, defaultLocale);
	}

	/**
	* Sets the user ID of this kaleo definition version.
	*
	* @param userId the user ID of this kaleo definition version
	*/
	@Override
	public void setUserId(long userId) {
		_kaleoDefinitionVersion.setUserId(userId);
	}

	/**
	* Sets the user name of this kaleo definition version.
	*
	* @param userName the user name of this kaleo definition version
	*/
	@Override
	public void setUserName(java.lang.String userName) {
		_kaleoDefinitionVersion.setUserName(userName);
	}

	/**
	* Sets the user uuid of this kaleo definition version.
	*
	* @param userUuid the user uuid of this kaleo definition version
	*/
	@Override
	public void setUserUuid(java.lang.String userUuid) {
		_kaleoDefinitionVersion.setUserUuid(userUuid);
	}

	/**
	* Sets the version of this kaleo definition version.
	*
	* @param version the version of this kaleo definition version
	*/
	@Override
	public void setVersion(int version) {
		_kaleoDefinitionVersion.setVersion(version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof KaleoDefinitionVersionWrapper)) {
			return false;
		}

		KaleoDefinitionVersionWrapper kaleoDefinitionVersionWrapper = (KaleoDefinitionVersionWrapper)obj;

		if (Objects.equals(_kaleoDefinitionVersion,
					kaleoDefinitionVersionWrapper._kaleoDefinitionVersion)) {
			return true;
		}

		return false;
	}

	@Override
	public KaleoDefinitionVersion getWrappedModel() {
		return _kaleoDefinitionVersion;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _kaleoDefinitionVersion.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _kaleoDefinitionVersion.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_kaleoDefinitionVersion.resetOriginalValues();
	}

	private final KaleoDefinitionVersion _kaleoDefinitionVersion;
}