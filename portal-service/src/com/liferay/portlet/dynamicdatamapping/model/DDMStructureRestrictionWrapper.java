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

package com.liferay.portlet.dynamicdatamapping.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link DDMStructureRestriction}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDMStructureRestriction
 * @generated
 */
@ProviderType
public class DDMStructureRestrictionWrapper implements DDMStructureRestriction,
	ModelWrapper<DDMStructureRestriction> {
	public DDMStructureRestrictionWrapper(
		DDMStructureRestriction ddmStructureRestriction) {
		_ddmStructureRestriction = ddmStructureRestriction;
	}

	@Override
	public Class<?> getModelClass() {
		return DDMStructureRestriction.class;
	}

	@Override
	public String getModelClassName() {
		return DDMStructureRestriction.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("structureRestrictionId", getStructureRestrictionId());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("structureId", getStructureId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long structureRestrictionId = (Long)attributes.get(
				"structureRestrictionId");

		if (structureRestrictionId != null) {
			setStructureRestrictionId(structureRestrictionId);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		Long structureId = (Long)attributes.get("structureId");

		if (structureId != null) {
			setStructureId(structureId);
		}
	}

	@Override
	public java.lang.Object clone() {
		return new DDMStructureRestrictionWrapper((DDMStructureRestriction)_ddmStructureRestriction.clone());
	}

	@Override
	public int compareTo(
		com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction ddmStructureRestriction) {
		return _ddmStructureRestriction.compareTo(ddmStructureRestriction);
	}

	/**
	* Returns the fully qualified class name of this d d m structure restriction.
	*
	* @return the fully qualified class name of this d d m structure restriction
	*/
	@Override
	public java.lang.String getClassName() {
		return _ddmStructureRestriction.getClassName();
	}

	/**
	* Returns the class name ID of this d d m structure restriction.
	*
	* @return the class name ID of this d d m structure restriction
	*/
	@Override
	public long getClassNameId() {
		return _ddmStructureRestriction.getClassNameId();
	}

	/**
	* Returns the class p k of this d d m structure restriction.
	*
	* @return the class p k of this d d m structure restriction
	*/
	@Override
	public long getClassPK() {
		return _ddmStructureRestriction.getClassPK();
	}

	@Override
	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _ddmStructureRestriction.getExpandoBridge();
	}

	/**
	* Returns the primary key of this d d m structure restriction.
	*
	* @return the primary key of this d d m structure restriction
	*/
	@Override
	public long getPrimaryKey() {
		return _ddmStructureRestriction.getPrimaryKey();
	}

	@Override
	public java.io.Serializable getPrimaryKeyObj() {
		return _ddmStructureRestriction.getPrimaryKeyObj();
	}

	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructure getStructure()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureRestriction.getStructure();
	}

	/**
	* Returns the structure ID of this d d m structure restriction.
	*
	* @return the structure ID of this d d m structure restriction
	*/
	@Override
	public long getStructureId() {
		return _ddmStructureRestriction.getStructureId();
	}

	/**
	* Returns the structure restriction ID of this d d m structure restriction.
	*
	* @return the structure restriction ID of this d d m structure restriction
	*/
	@Override
	public long getStructureRestrictionId() {
		return _ddmStructureRestriction.getStructureRestrictionId();
	}

	@Override
	public int hashCode() {
		return _ddmStructureRestriction.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _ddmStructureRestriction.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _ddmStructureRestriction.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _ddmStructureRestriction.isNew();
	}

	@Override
	public void persist() {
		_ddmStructureRestriction.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_ddmStructureRestriction.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(java.lang.String className) {
		_ddmStructureRestriction.setClassName(className);
	}

	/**
	* Sets the class name ID of this d d m structure restriction.
	*
	* @param classNameId the class name ID of this d d m structure restriction
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_ddmStructureRestriction.setClassNameId(classNameId);
	}

	/**
	* Sets the class p k of this d d m structure restriction.
	*
	* @param classPK the class p k of this d d m structure restriction
	*/
	@Override
	public void setClassPK(long classPK) {
		_ddmStructureRestriction.setClassPK(classPK);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.model.BaseModel<?> baseModel) {
		_ddmStructureRestriction.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portlet.expando.model.ExpandoBridge expandoBridge) {
		_ddmStructureRestriction.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_ddmStructureRestriction.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public void setNew(boolean n) {
		_ddmStructureRestriction.setNew(n);
	}

	/**
	* Sets the primary key of this d d m structure restriction.
	*
	* @param primaryKey the primary key of this d d m structure restriction
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_ddmStructureRestriction.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
		_ddmStructureRestriction.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the structure ID of this d d m structure restriction.
	*
	* @param structureId the structure ID of this d d m structure restriction
	*/
	@Override
	public void setStructureId(long structureId) {
		_ddmStructureRestriction.setStructureId(structureId);
	}

	/**
	* Sets the structure restriction ID of this d d m structure restriction.
	*
	* @param structureRestrictionId the structure restriction ID of this d d m structure restriction
	*/
	@Override
	public void setStructureRestrictionId(long structureRestrictionId) {
		_ddmStructureRestriction.setStructureRestrictionId(structureRestrictionId);
	}

	@Override
	public com.liferay.portal.model.CacheModel<com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction> toCacheModel() {
		return _ddmStructureRestriction.toCacheModel();
	}

	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction toEscapedModel() {
		return new DDMStructureRestrictionWrapper(_ddmStructureRestriction.toEscapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _ddmStructureRestriction.toString();
	}

	@Override
	public com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction toUnescapedModel() {
		return new DDMStructureRestrictionWrapper(_ddmStructureRestriction.toUnescapedModel());
	}

	@Override
	public java.lang.String toXmlString() {
		return _ddmStructureRestriction.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMStructureRestrictionWrapper)) {
			return false;
		}

		DDMStructureRestrictionWrapper ddmStructureRestrictionWrapper = (DDMStructureRestrictionWrapper)obj;

		if (Validator.equals(_ddmStructureRestriction,
					ddmStructureRestrictionWrapper._ddmStructureRestriction)) {
			return true;
		}

		return false;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedModel}
	 */
	@Deprecated
	public DDMStructureRestriction getWrappedDDMStructureRestriction() {
		return _ddmStructureRestriction;
	}

	@Override
	public DDMStructureRestriction getWrappedModel() {
		return _ddmStructureRestriction;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _ddmStructureRestriction.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _ddmStructureRestriction.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_ddmStructureRestriction.resetOriginalValues();
	}

	private final DDMStructureRestriction _ddmStructureRestriction;
}