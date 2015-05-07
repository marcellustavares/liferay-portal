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

package com.liferay.portlet.dynamicdatamapping.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.CacheModel;

import com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing DDMStructureRestriction in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see DDMStructureRestriction
 * @generated
 */
@ProviderType
public class DDMStructureRestrictionCacheModel implements CacheModel<DDMStructureRestriction>,
	Externalizable {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMStructureRestrictionCacheModel)) {
			return false;
		}

		DDMStructureRestrictionCacheModel ddmStructureRestrictionCacheModel = (DDMStructureRestrictionCacheModel)obj;

		if (structureRestrictionId == ddmStructureRestrictionCacheModel.structureRestrictionId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, structureRestrictionId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(9);

		sb.append("{structureRestrictionId=");
		sb.append(structureRestrictionId);
		sb.append(", classNameId=");
		sb.append(classNameId);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append(", structureId=");
		sb.append(structureId);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public DDMStructureRestriction toEntityModel() {
		DDMStructureRestrictionImpl ddmStructureRestrictionImpl = new DDMStructureRestrictionImpl();

		ddmStructureRestrictionImpl.setStructureRestrictionId(structureRestrictionId);
		ddmStructureRestrictionImpl.setClassNameId(classNameId);
		ddmStructureRestrictionImpl.setClassPK(classPK);
		ddmStructureRestrictionImpl.setStructureId(structureId);

		ddmStructureRestrictionImpl.resetOriginalValues();

		return ddmStructureRestrictionImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		structureRestrictionId = objectInput.readLong();
		classNameId = objectInput.readLong();
		classPK = objectInput.readLong();
		structureId = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(structureRestrictionId);
		objectOutput.writeLong(classNameId);
		objectOutput.writeLong(classPK);
		objectOutput.writeLong(structureId);
	}

	public long structureRestrictionId;
	public long classNameId;
	public long classPK;
	public long structureId;
}