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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class DDMStructureRestrictionSoap implements Serializable {
	public static DDMStructureRestrictionSoap toSoapModel(
		DDMStructureRestriction model) {
		DDMStructureRestrictionSoap soapModel = new DDMStructureRestrictionSoap();

		soapModel.setStructureRestrictionId(model.getStructureRestrictionId());
		soapModel.setClassNameId(model.getClassNameId());
		soapModel.setClassPK(model.getClassPK());
		soapModel.setStructureId(model.getStructureId());

		return soapModel;
	}

	public static DDMStructureRestrictionSoap[] toSoapModels(
		DDMStructureRestriction[] models) {
		DDMStructureRestrictionSoap[] soapModels = new DDMStructureRestrictionSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static DDMStructureRestrictionSoap[][] toSoapModels(
		DDMStructureRestriction[][] models) {
		DDMStructureRestrictionSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new DDMStructureRestrictionSoap[models.length][models[0].length];
		}
		else {
			soapModels = new DDMStructureRestrictionSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static DDMStructureRestrictionSoap[] toSoapModels(
		List<DDMStructureRestriction> models) {
		List<DDMStructureRestrictionSoap> soapModels = new ArrayList<DDMStructureRestrictionSoap>(models.size());

		for (DDMStructureRestriction model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new DDMStructureRestrictionSoap[soapModels.size()]);
	}

	public DDMStructureRestrictionSoap() {
	}

	public long getPrimaryKey() {
		return _structureRestrictionId;
	}

	public void setPrimaryKey(long pk) {
		setStructureRestrictionId(pk);
	}

	public long getStructureRestrictionId() {
		return _structureRestrictionId;
	}

	public void setStructureRestrictionId(long structureRestrictionId) {
		_structureRestrictionId = structureRestrictionId;
	}

	public long getClassNameId() {
		return _classNameId;
	}

	public void setClassNameId(long classNameId) {
		_classNameId = classNameId;
	}

	public long getClassPK() {
		return _classPK;
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	public long getStructureId() {
		return _structureId;
	}

	public void setStructureId(long structureId) {
		_structureId = structureId;
	}

	private long _structureRestrictionId;
	private long _classNameId;
	private long _classPK;
	private long _structureId;
}