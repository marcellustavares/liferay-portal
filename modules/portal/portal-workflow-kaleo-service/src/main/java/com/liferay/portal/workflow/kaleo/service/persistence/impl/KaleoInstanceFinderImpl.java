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

package com.liferay.portal.workflow.kaleo.service.persistence.impl;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.UserGroupGroupRole;
import com.liferay.portal.service.UserGroupGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.workflow.kaleo.model.KaleoInstance;
import com.liferay.portal.workflow.kaleo.model.impl.KaleoInstanceModelImpl;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoInstanceFinder;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoInstanceQuery;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoInstanceUtil;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Inácio Nery
 */
public class KaleoInstanceFinderImpl
	extends KaleoInstanceFinderBaseImpl implements KaleoInstanceFinder {

	public static final String COUNT_BY_C_KIT_KTAI =
		KaleoInstanceFinder.class.getName() + ".countByC_KIT_KTAI";

	public static final String FIND_BY_C_KIT_KTAI =
		KaleoInstanceFinder.class.getName() + ".findByC_KIT_KTAI";

	@Override
	public int countKaleoInstances(KaleoInstanceQuery kaleoInstanceQuery) {
		Session session = null;

		try {
			session = openSession();

			SQLQuery q = buildKaleoInstanceQuerySQL(
				kaleoInstanceQuery, true, session);

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<KaleoInstance> findKaleoInstances(
		KaleoInstanceQuery kaleoInstanceQuery) {

		Session session = null;

		try {
			session = openSession();

			SQLQuery q = buildKaleoInstanceQuerySQL(
				kaleoInstanceQuery, false, session);

			List<KaleoInstance> kaleoInstances = new ArrayList<>();

			Iterator<Long> itr = (Iterator<Long>)QueryUtil.iterate(
				q, getDialect(), kaleoInstanceQuery.getStart(),
				kaleoInstanceQuery.getEnd());

			while (itr.hasNext()) {
				long kaleoInstanceId = itr.next();

				KaleoInstance kaleoInstance =
					KaleoInstanceUtil.findByPrimaryKey(kaleoInstanceId);

				kaleoInstances.add(kaleoInstance);
			}

			return kaleoInstances;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected boolean appendSearchCriteria(
		KaleoInstanceQuery kaleoInstanceQuery) {

		if (ArrayUtil.isNotEmpty(kaleoInstanceQuery.getAssetTypes())) {
			return true;
		}

		if (Validator.isNotNull(kaleoInstanceQuery.getKaleoDefinitionName())) {
			return true;
		}

		if (Validator.isNotNull(kaleoInstanceQuery.getNodeName())) {
			return true;
		}

		return false;
	}

	protected SQLQuery buildKaleoInstanceQuerySQL(
			KaleoInstanceQuery kaleoInstanceQuery, boolean count,
			Session session)
		throws Exception {

		String sql = null;

		if (count) {
			sql = CustomSQLUtil.get(COUNT_BY_C_KIT_KTAI);
		}
		else {
			sql = CustomSQLUtil.get(FIND_BY_C_KIT_KTAI);
		}

		sql = CustomSQLUtil.appendCriteria(
			sql, getAssigneeClassName(kaleoInstanceQuery));
		sql = CustomSQLUtil.appendCriteria(
			sql, getAssigneeClassPK(kaleoInstanceQuery));
		sql = CustomSQLUtil.appendCriteria(
			sql, getCompleted(kaleoInstanceQuery));
		sql = CustomSQLUtil.appendCriteria(sql, getUserId(kaleoInstanceQuery));

		if (appendSearchCriteria(kaleoInstanceQuery)) {
			sql = CustomSQLUtil.appendCriteria(sql, " AND (");

			if (ArrayUtil.isNotEmpty(kaleoInstanceQuery.getAssetTypes())) {
				sql = CustomSQLUtil.appendCriteria(sql, " (");
				sql = CustomSQLUtil.appendCriteria(
					sql, getAssetTypes(kaleoInstanceQuery, true));
				sql = CustomSQLUtil.appendCriteria(sql, ") ");
			}

			sql = CustomSQLUtil.appendCriteria(
				sql,
				getKaleoDefinitionName(
						kaleoInstanceQuery,
					ArrayUtil.isEmpty(kaleoInstanceQuery.getAssetTypes())));
			sql = CustomSQLUtil.appendCriteria(
				sql,
				getKaleoDefinitionVersion(
					kaleoInstanceQuery,
					ArrayUtil.isEmpty(
						kaleoInstanceQuery.getAssetTypes()) &&
					 (kaleoInstanceQuery.getKaleoDefinitionName() == null)));
			sql = CustomSQLUtil.appendCriteria(
				sql,
				getNodeName(
					kaleoInstanceQuery,
					ArrayUtil.isEmpty(
						kaleoInstanceQuery.getAssetTypes()) &&
					 (kaleoInstanceQuery.getKaleoDefinitionName() == null) &&
					 (Validator.isNull(
						kaleoInstanceQuery.getKaleoDefinitionVersion()))));

			sql = CustomSQLUtil.appendCriteria(sql, ")");

			sql = CustomSQLUtil.replaceAndOperator(sql, false);
		}

		OrderByComparator<KaleoInstance> obc =
			kaleoInstanceQuery.getOrderByComparator();

		if (obc != null) {
			StringBundler sb = new StringBundler(sql);

			appendOrderByComparator(sb, _ORDER_BY_ENTITY_ALIAS, obc);

			sql = sb.toString();
			String sort = "DESC";

			if (obc.isAscending()) {
				sort = "ASC";
			}

			sb.append(", KaleoInstance.className "+ sort);

			sql = sb.toString();

			String[] orderByFields = obc.getOrderByFields();

			sb = new StringBundler(orderByFields.length * 3 + 2);

			sb.append("DISTINCT KaleoInstance.kaleoInstanceId");

			for (String orderByField : orderByFields) {
				sb.append(", ");
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByField);
			}

			sb.append(", KaleoInstance.className ");

			sql = sql.replace(
				"DISTINCT KaleoInstance.kaleoInstanceId", sb.toString());
		}

		SQLQuery q = session.createSynchronizedSQLQuery(sql);

		if (count) {
			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);
		}
		else {
			q.addScalar("KaleoInstanceId", Type.LONG);
		}

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(kaleoInstanceQuery.getCompanyId());

		setAssigneeClassName(qPos, kaleoInstanceQuery);
		setAssigneeClassPK(qPos, kaleoInstanceQuery);
		setCompleted(qPos, kaleoInstanceQuery);
		setUserId(qPos, kaleoInstanceQuery);

		setAssetType(qPos, kaleoInstanceQuery);
		setKaleoDefinitionName(qPos, kaleoInstanceQuery);
		setKaleoDefinitionVersion(qPos, kaleoInstanceQuery);
		setNodeName(qPos, kaleoInstanceQuery);

		return q;
	}

	protected String getAssetTypes(
		KaleoInstanceQuery kaleoInstanceQuery, boolean firstCriteria) {

		String[] assetTypes = kaleoInstanceQuery.getAssetTypes();

		if (ArrayUtil.isEmpty(assetTypes)) {
			return StringPool.BLANK;
		}

		assetTypes = CustomSQLUtil.keywords(kaleoInstanceQuery.getAssetTypes());

		if (ArrayUtil.isEmpty(assetTypes)) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(assetTypes.length * 2 + 1);

		if (!firstCriteria) {
			sb.append(" AND (");
		}
		else {
			sb.append("(");
		}

		for (int i = 0; i < assetTypes.length; i++) {
			sb.append("(lower(KaleoInstance.className) LIKE ?)");

			if ((i + 1) < assetTypes.length) {
				sb.append(" OR ");
			}
			else {
				sb.append(")");
			}
		}

		return sb.toString();
	}

	protected String getAssigneeClassName(
		KaleoInstanceQuery kaleoInstanceQuery) {

		String assigneeClassName = kaleoInstanceQuery.getAssigneeClassName();

		if (Validator.isNull(assigneeClassName)) {
			return StringPool.BLANK;
		}

		return "AND (KaleoAssignmentInstance.assigneeClassName = ?)";
	}

	protected String getAssigneeClassPK(KaleoInstanceQuery kaleoInstanceQuery) {
		Long assigneeClassPK = kaleoInstanceQuery.getAssigneeClassPK();

		if (Validator.isNull(assigneeClassPK)) {
			return StringPool.BLANK;
		}

		return "AND (KaleoAssignmentInstance.assigneeClassPK = ?)";
	}

	protected String getCompleted(KaleoInstanceQuery kaleoInstanceQuery) {
		Boolean completed = kaleoInstanceQuery.isCompleted();

		if (completed == null) {
			return StringPool.BLANK;
		}

		return "AND (KaleoInstance.completed = ?)";
	}

	protected String getKaleoDefinitionName(
		KaleoInstanceQuery kaleoInstanceQuery, boolean firstCriteria) {

		String definitionName = kaleoInstanceQuery.getKaleoDefinitionName();

		if (Validator.isNull(definitionName)) {
			return StringPool.BLANK;
		}

		String[] definitionNames = CustomSQLUtil.keywords(
			definitionName, false);

		if (ArrayUtil.isEmpty(definitionNames)) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(definitionNames.length * 3 + 1);

		if (!firstCriteria) {
			sb.append("[$AND_OR_CONNECTOR$] (");
		}
		else {
			sb.append("(");
		}

		for (int i = 0; i < definitionNames.length; i++) {
			sb.append("(lower(KaleoInstance.kaleoDefinitionName) ");
			sb.append("LIKE lower(?))");

			if ((i + 1) < definitionNames.length) {
				sb.append(" OR ");
			}
			else {
				sb.append(")");
			}
		}

		return sb.toString();
	}

	protected String getKaleoDefinitionVersion(
		KaleoInstanceQuery kaleoInstanceQuery, boolean firstCriteria) {

		Integer definitionVersion =
			kaleoInstanceQuery.getKaleoDefinitionVersion();

		if (Validator.isNull(definitionVersion)) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(4);

		if (!firstCriteria) {
			sb.append("[$AND_OR_CONNECTOR$] (");
		}
		else {
			sb.append("(");
		}

		sb.append("(KaleoInstance.kaleoDefinitionVersion = ?)");

		sb.append(")");

		return sb.toString();
	}

	protected String getNodeName(
		KaleoInstanceQuery kaleoInstanceQuery, boolean firstCriteria) {

		String nodeName = kaleoInstanceQuery.getNodeName();

		if (Validator.isNull(nodeName)) {
			return StringPool.BLANK;
		}

		String[] nodeNames = CustomSQLUtil.keywords(nodeName, false);

		if (ArrayUtil.isEmpty(nodeNames)) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(nodeNames.length * 3 + 1);

		if (!firstCriteria) {
			sb.append("[$AND_OR_CONNECTOR$] (");
		}
		else {
			sb.append("(");
		}

		for (int i = 0; i < nodeNames.length; i++) {
			sb.append("(lower(KaleoInstanceToken.currentKaleoNodeName) ");
			sb.append("LIKE lower(?))");

			if ((i + 1) < nodeNames.length) {
				sb.append(" OR ");
			}
			else {
				sb.append(")");
			}
		}

		return sb.toString();
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return KaleoInstanceModelImpl.TABLE_COLUMNS_MAP;
	}

	protected List<UserGroupGroupRole> getUserGroupGroupRoles(long userId)
		throws Exception {

		List<UserGroupGroupRole> userGroupGroupRoles = new ArrayList<>();

		List<UserGroup> userGroups =
			UserGroupLocalServiceUtil.getUserUserGroups(userId);

		for (UserGroup userGroup : userGroups) {
			userGroupGroupRoles.addAll(
				UserGroupGroupRoleLocalServiceUtil.getUserGroupGroupRoles(
					userGroup.getUserGroupId()));
		}

		return userGroupGroupRoles;
	}

	protected String getUserId(KaleoInstanceQuery kaleoInstanceQuery) {
		Long userId = kaleoInstanceQuery.getUserId();

		if (Validator.isNull(userId)) {
			return StringPool.BLANK;
		}

		return "AND (KaleoInstance.userId = ?)";
	}

	protected void setAssetType(
		QueryPos qPos, KaleoInstanceQuery kaleoInstanceQuery) {

		String[] assetTypes = kaleoInstanceQuery.getAssetTypes();

		if (ArrayUtil.isEmpty(assetTypes)) {
			return;
		}

		assetTypes = CustomSQLUtil.keywords(assetTypes, false);

		qPos.add(assetTypes);
	}

	protected void setAssigneeClassName(
		QueryPos qPos, KaleoInstanceQuery kaleoInstanceQuery) {

		String assigneeClassName = kaleoInstanceQuery.getAssigneeClassName();

		if (Validator.isNull(assigneeClassName)) {
			return;
		}

		qPos.add(assigneeClassName);
	}

	protected void setAssigneeClassPK(
		QueryPos qPos, KaleoInstanceQuery kaleoInstanceQuery) {

		Long assigneeClassPK = kaleoInstanceQuery.getAssigneeClassPK();

		if (assigneeClassPK == null) {
			return;
		}

		qPos.add(assigneeClassPK);
	}

	protected void setCompleted(
		QueryPos qPos, KaleoInstanceQuery kaleoInstanceQuery) {

		Boolean completed = kaleoInstanceQuery.isCompleted();

		if (completed == null) {
			return;
		}

		qPos.add(completed);
	}

	protected void setKaleoDefinitionName(
		QueryPos qPos, KaleoInstanceQuery kaleoInstanceQuery) {

		String definitionName = kaleoInstanceQuery.getKaleoDefinitionName();

		if (Validator.isNull(definitionName)) {
			return;
		}

		String[] definitionNames = CustomSQLUtil.keywords(
			definitionName, false);

		qPos.add(definitionNames);
	}

	protected void setKaleoDefinitionVersion(
		QueryPos qPos, KaleoInstanceQuery kaleoInstanceQuery) {

		Integer definitionVersion =
			kaleoInstanceQuery.getKaleoDefinitionVersion();

		if (Validator.isNull(definitionVersion)) {
			return;
		}

		qPos.add(definitionVersion);
	}

	protected void setNodeName(
		QueryPos qPos, KaleoInstanceQuery kaleoInstanceQuery) {

		String nodeName = kaleoInstanceQuery.getNodeName();

		if (Validator.isNull(nodeName)) {
			return;
		}

		String[] nodeNames = CustomSQLUtil.keywords(nodeName, false);

		qPos.add(nodeNames);
	}

	protected void setUserId(
		QueryPos qPos, KaleoInstanceQuery kaleoInstanceQuery) {

		Long userId = kaleoInstanceQuery.getUserId();

		if (Validator.isNull(userId)) {
			return;
		}

		qPos.add(userId);
	}

	private static final String _ORDER_BY_ENTITY_ALIAS = "KaleoInstance.";

}