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

package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONSerializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMContent;
import com.liferay.portlet.dynamicdatamapping.model.DDMStorageLink;
import com.liferay.portlet.dynamicdatamapping.service.DDMContentLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMStorageLinkLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.query.Condition;
import com.liferay.portlet.dynamicdatamapping.util.DDMUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pablo Carvalho
 */
public class JSONStorageAdapter extends BaseStorageAdapter {

	@Override
	protected long doCreate(
			long companyId, long ddmStructureId, DDMFormValues ddmFormValues,
			ServiceContext serviceContext)
		throws Exception {

		long classNameId = PortalUtil.getClassNameId(
			DDMContent.class.getName());

		String ddmFormValuesJSON = DDMFormValuesJSONSerializerUtil.serialize(
			ddmFormValues);

		DDMContent ddmContent = DDMContentLocalServiceUtil.addJSONContent(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			DDMStorageLink.class.getName(), null, ddmFormValuesJSON,
			serviceContext);

		DDMStorageLinkLocalServiceUtil.addStorageLink(
			classNameId, ddmContent.getPrimaryKey(), ddmStructureId,
			serviceContext);

		return ddmContent.getPrimaryKey();
	}

	@Override
	protected long doCreate(
			long companyId, long ddmStructureId, Fields fields,
			ServiceContext serviceContext)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	protected void doDeleteByClass(long classPK) throws Exception {
		DDMContentLocalServiceUtil.deleteDDMContent(classPK);

		DDMStorageLinkLocalServiceUtil.deleteClassStorageLink(classPK);
	}

	@Override
	protected void doDeleteByDDMStructure(long ddmStructureId)
		throws Exception {

		List<DDMStorageLink> ddmStorageLinks =
			DDMStorageLinkLocalServiceUtil.getStructureStorageLinks(
				ddmStructureId);

		for (DDMStorageLink ddmStorageLink : ddmStorageLinks) {
			DDMContentLocalServiceUtil.deleteDDMContent(
				ddmStorageLink.getClassPK());
		}

		DDMStorageLinkLocalServiceUtil.deleteStructureStorageLinks(
			ddmStructureId);
	}

	@Override
	protected List<DDMFormValues> doGetDDMFormValuesListByClasses(
			long ddmStructureId, long[] classPKs, List<String> fieldNames,
			OrderByComparator<DDMFormValues> orderByComparator)
		throws Exception {

		return _doQuery(
			ddmStructureId, classPKs, fieldNames, orderByComparator);
	}

	@Override
	protected List<DDMFormValues> doGetDDMFormValuesListByDDMStructure(
			long ddmStructureId, List<String> fieldNames,
			OrderByComparator<DDMFormValues> orderByComparator)
		throws Exception {

		return _doQuery(ddmStructureId, fieldNames, orderByComparator);
	}

	@Override
	protected Map<Long, DDMFormValues> doGetDDMFormValuesMapByClasses(
			long ddmStructureId, long[] classPKs, List<String> fieldNames)
		throws Exception {

		return _doQuery(ddmStructureId, classPKs, fieldNames);
	}

	@Override
	protected List<Fields> doGetFieldsListByClasses(
			long ddmStructureId, long[] classPKs, List<String> fieldNames,
			OrderByComparator<Fields> orderByComparator)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	protected List<Fields> doGetFieldsListByDDMStructure(
			long ddmStructureId, List<String> fieldNames,
			OrderByComparator<Fields> orderByComparator)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	protected Map<Long, Fields> doGetFieldsMapByClasses(
			long ddmStructureId, long[] classPKs, List<String> fieldNames)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	protected List<Fields> doQuery(
			long ddmStructureId, List<String> fieldNames, Condition condition,
			OrderByComparator<Fields> orderByComparator)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	protected int doQueryCount(long ddmStructureId, Condition condition)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	protected List<DDMFormValues> doQueryDDMFormValues(
			long ddmStructureId, List<String> fieldNames, Condition condition,
			OrderByComparator<DDMFormValues> orderByComparator)
		throws Exception {

		if (condition != null) {
			throw new UnsupportedOperationException();
		}

		return _doQuery(ddmStructureId, fieldNames, orderByComparator);
	}

	@Override
	protected void doUpdate(
			long classPK, DDMFormValues ddmFormValues, boolean mergeFields,
			ServiceContext serviceContext)
		throws Exception {

		DDMContent ddmContent = DDMContentLocalServiceUtil.getContent(classPK);

		ddmContent.setModifiedDate(serviceContext.getModifiedDate(null));

		if (mergeFields) {
			ddmFormValues = DDMUtil.mergeDDMFormValues(
				ddmFormValues, getDDMFormValues(classPK));
		}

		ddmContent.setData(
			DDMFormValuesJSONSerializerUtil.serialize(ddmFormValues));

		DDMContentLocalServiceUtil.updateContent(
			ddmContent.getPrimaryKey(), ddmContent.getName(),
			ddmContent.getDescription(), ddmContent.getData(), serviceContext);
	}

	@Override
	protected void doUpdate(
			long classPK, Fields fields, boolean mergeFields,
			ServiceContext serviceContext)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	private List<DDMFormValues> _doQuery(
			long ddmStructureId, List<String> fieldNames,
			OrderByComparator<DDMFormValues> orderByComparator)
		throws Exception {

		return _doQuery(
			ddmStructureId, _getStructureClassPKs(ddmStructureId), fieldNames,
			orderByComparator);
	}

	private Map<Long, DDMFormValues> _doQuery(
			long ddmStructureId, long[] classPKs, List<String> fieldNames)
		throws Exception {

		Map<Long, DDMFormValues> ddmFormValuesMap =
			new HashMap<Long, DDMFormValues>();

		List<DDMFormValues> ddmFormValuesList = _doQuery(
			ddmStructureId, classPKs, fieldNames, null);

		for (int i = 0; i < ddmFormValuesList.size(); i++) {
			DDMFormValues ddmFormValues = ddmFormValuesList.get(i);

			ddmFormValuesMap.put(classPKs[i], ddmFormValues);
		}

		return ddmFormValuesMap;
	}

	private List<DDMFormValues> _doQuery(
			long ddmStructureId, long[] classPKs, List<String> fieldNames,
			OrderByComparator<DDMFormValues> orderByComparator)
		throws Exception {

		List<DDMFormValues> fieldsList = new ArrayList<DDMFormValues>();

		for (long classPK : classPKs) {
			DDMContent ddmContent = DDMContentLocalServiceUtil.getContent(
				classPK);

			DDMFormValues fields =
				DDMFormValuesJSONDeserializerUtil.deserialize(
					ddmContent.getData());

			fieldsList.add(fields);
		}

		if (orderByComparator != null) {
			Collections.sort(fieldsList, orderByComparator);
		}

		return fieldsList;
	}

	private long[] _getStructureClassPKs(long ddmStructureId) throws Exception {
		List<Long> classPKs = new ArrayList<Long>();

		List<DDMStorageLink> ddmStorageLinks =
			DDMStorageLinkLocalServiceUtil.getStructureStorageLinks(
				ddmStructureId);

		for (DDMStorageLink ddmStorageLink : ddmStorageLinks) {
			classPKs.add(ddmStorageLink.getClassPK());
		}

		return ArrayUtil.toArray(classPKs.toArray(new Long[classPKs.size()]));
	}

}