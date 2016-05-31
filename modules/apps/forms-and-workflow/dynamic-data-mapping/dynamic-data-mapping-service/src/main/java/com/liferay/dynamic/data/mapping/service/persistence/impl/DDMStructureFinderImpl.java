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

package com.liferay.dynamic.data.mapping.service.persistence.impl;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.model.impl.DDMStructureImpl;
import com.liferay.dynamic.data.mapping.service.permission.DDMStructurePermission;
import com.liferay.dynamic.data.mapping.service.persistence.DDMStructureFinder;
import com.liferay.portal.dao.orm.custom.sql.CustomSQLUtil;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Iterator;
import java.util.List;

/**
 * @author Eduardo Lundgren
 * @author Connor McKay
 * @author Marcellus Tavares
 */
public class DDMStructureFinderImpl
	extends DDMStructureFinderBaseImpl implements DDMStructureFinder {

	public static final String COUNT_BY_C_G_C_N_D_S_T_S =
		DDMStructureFinder.class.getName() + ".countByC_G_C_N_D_S_T_S";

	public static final String FIND_BY_C_G_C_N_D_S_T_R =
		DDMStructureFinder.class.getName() + ".findByC_G_C_N_D_S_T_S";

	@Override
	public int countByKeywords(
		long companyId, long[] groupIds, long classNameId, String keywords,
		int status) {

		String[] names = null;
		String[] descriptions = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			names = CustomSQLUtil.keywords(keywords);
			descriptions = CustomSQLUtil.keywords(keywords, false);
		}
		else {
			andOperator = true;
		}

		return countByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, null,
			DDMStructureConstants.TYPE_DEFAULT, status, andOperator);
	}

	@Override
	public int countByC_G_C_S(
		long companyId, long[] groupIds, long classNameId, int status) {

		String[] names = CustomSQLUtil.keywords(StringPool.BLANK);
		String[] descriptions = CustomSQLUtil.keywords(StringPool.BLANK, false);

		return countByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, null,
			DDMStructureConstants.TYPE_DEFAULT, status, true);
	}

	@Override
	public int countByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String name,
		String description, String storageType, int type, int status,
		boolean andOperator) {

		String[] names = CustomSQLUtil.keywords(name);
		String[] descriptions = CustomSQLUtil.keywords(description, false);

		return countByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, storageType,
			type, status, andOperator);
	}

	@Override
	public int countByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String[] names,
		String[] descriptions, String storageType, int type, int status,
		boolean andOperator) {

		return doCountByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, storageType,
			type, status, andOperator, false);
	}

	@Override
	public int filterCountByKeywords(
		long companyId, long[] groupIds, long classNameId, String keywords,
		int status) {

		String[] names = null;
		String[] descriptions = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			names = CustomSQLUtil.keywords(keywords);
			descriptions = CustomSQLUtil.keywords(keywords, false);
		}
		else {
			andOperator = true;
		}

		return filterCountByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, null,
			DDMStructureConstants.TYPE_DEFAULT, status, andOperator);
	}

	@Override
	public int filterCountByC_G_C_S(
		long companyId, long[] groupIds, long classNameId, int status) {

		String[] names = CustomSQLUtil.keywords(StringPool.BLANK);
		String[] descriptions = CustomSQLUtil.keywords(StringPool.BLANK, false);

		return filterCountByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, null,
			DDMStructureConstants.TYPE_DEFAULT, status, true);
	}

	@Override
	public int filterCountByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String name,
		String description, String storageType, int type, int status,
		boolean andOperator) {

		String[] names = CustomSQLUtil.keywords(name);
		String[] descriptions = CustomSQLUtil.keywords(description, false);

		return filterCountByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, storageType,
			type, status, andOperator);
	}

	@Override
	public int filterCountByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String[] names,
		String[] descriptions, String storageType, int type, int status,
		boolean andOperator) {

		return doCountByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, storageType,
			type, status, andOperator, true);
	}

	@Override
	public List<DDMStructure> filterFindByKeywords(
		long companyId, long[] groupIds, long classNameId, String keywords,
		int status, int start, int end,
		OrderByComparator<DDMStructure> orderByComparator) {

		String[] names = null;
		String[] descriptions = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			names = CustomSQLUtil.keywords(keywords);
			descriptions = CustomSQLUtil.keywords(keywords, false);
		}
		else {
			andOperator = true;
		}

		return filterFindByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, null,
			DDMStructureConstants.TYPE_DEFAULT, status, andOperator, start, end,
			orderByComparator);
	}

	@Override
	public List<DDMStructure> filterFindByC_G_C_S(
		long companyId, long[] groupIds, long classNameId, int status,
		int start, int end, OrderByComparator<DDMStructure> orderByComparator) {

		String[] names = CustomSQLUtil.keywords(StringPool.BLANK);
		String[] descriptions = CustomSQLUtil.keywords(StringPool.BLANK, false);

		return filterFindByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, null,
			DDMStructureConstants.TYPE_DEFAULT, status, true, start, end,
			orderByComparator);
	}

	@Override
	public List<DDMStructure> filterFindByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String name,
		String description, String storageType, int type, int status,
		boolean andOperator, int start, int end,
		OrderByComparator<DDMStructure> orderByComparator) {

		String[] names = CustomSQLUtil.keywords(name);
		String[] descriptions = CustomSQLUtil.keywords(description, false);

		return filterFindByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, storageType,
			type, status, andOperator, start, end, orderByComparator);
	}

	@Override
	public List<DDMStructure> filterFindByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String[] names,
		String[] descriptions, String storageType, int type, int status,
		boolean andOperator, int start, int end,
		OrderByComparator<DDMStructure> orderByComparator) {

		return doFindByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, storageType,
			type, status, andOperator, start, end, orderByComparator, true);
	}

	@Override
	public List<DDMStructure> findByKeywords(
		long companyId, long[] groupIds, long classNameId, String keywords,
		int status, int start, int end,
		OrderByComparator<DDMStructure> orderByComparator) {

		String[] names = null;
		String[] descriptions = null;
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			names = CustomSQLUtil.keywords(keywords);
			descriptions = CustomSQLUtil.keywords(keywords, false);
		}
		else {
			andOperator = true;
		}

		return findByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, null,
			DDMStructureConstants.TYPE_DEFAULT, status, andOperator, start, end,
			orderByComparator);
	}

	@Override
	public List<DDMStructure> findByC_G_C_S(
		long companyId, long[] groupIds, long classNameId, int status,
		int start, int end, OrderByComparator<DDMStructure> orderByComparator) {

		String[] names = CustomSQLUtil.keywords(StringPool.BLANK);
		String[] descriptions = CustomSQLUtil.keywords(StringPool.BLANK, false);

		return findByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, null,
			DDMStructureConstants.TYPE_DEFAULT, status, true, start, end,
			orderByComparator);
	}

	@Override
	public List<DDMStructure> findByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String name,
		String description, String storageType, int type, int status,
		boolean andOperator, int start, int end,
		OrderByComparator<DDMStructure> orderByComparator) {

		String[] names = CustomSQLUtil.keywords(name);
		String[] descriptions = CustomSQLUtil.keywords(description, false);

		return findByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, storageType,
			type, status, andOperator, start, end, orderByComparator);
	}

	@Override
	public List<DDMStructure> findByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String[] names,
		String[] descriptions, String storageType, int type, int status,
		boolean andOperator, int start, int end,
		OrderByComparator<DDMStructure> orderByComparator) {

		return doFindByC_G_C_N_D_S_T_S(
			companyId, groupIds, classNameId, names, descriptions, storageType,
			type, status, andOperator, start, end, orderByComparator, false);
	}

	protected int doCountByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String[] names,
		String[] descriptions, String storageType, int type, int status,
		boolean andOperator, boolean inlineSQLHelper) {

		names = CustomSQLUtil.keywords(names);
		descriptions = CustomSQLUtil.keywords(descriptions, false);

		Session session = null;

		try {
			session = openSession();

			String sql = null;

			if (ArrayUtil.isEmpty(groupIds)) {
				sql = getSQLQueryByC_G_C_N_D_S_T_S(
					COUNT_BY_C_G_C_N_D_S_T_S, 0, classNameId, names,
					descriptions, status, andOperator, inlineSQLHelper);
			}
			else {
				StringBundler sb = new StringBundler(4 * groupIds.length);

				for (int i = 0; i < groupIds.length; i++) {
					if (i > 0) {
						sb.append(" UNION ");
					}

					sb.append(StringPool.OPEN_PARENTHESIS);

					sql = getSQLQueryByC_G_C_N_D_S_T_S(
						COUNT_BY_C_G_C_N_D_S_T_S, groupIds[i], classNameId,
						names, descriptions, status, andOperator,
						inlineSQLHelper);

					sb.append(sql);

					sb.append(StringPool.CLOSE_PARENTHESIS);
				}

				sql = sb.toString();
			}

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			if (ArrayUtil.isEmpty(groupIds)) {
				getQueryPosByC_G_C_N_D_S_T_S(
					qPos, companyId, null, classNameId, names, descriptions,
					storageType, type, status);
			}
			else {
				for (int i = 0; i < groupIds.length; i++) {
					getQueryPosByC_G_C_N_D_S_T_S(
						qPos, companyId, groupIds[i], classNameId, names,
						descriptions, storageType, type, status);
				}
			}

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

	protected List<DDMStructure> doFindByC_G_C_N_D_S_T_S(
		long companyId, long[] groupIds, long classNameId, String[] names,
		String[] descriptions, String storageType, int type, int status,
		boolean andOperator, int start, int end,
		OrderByComparator<DDMStructure> orderByComparator,
		boolean inlineSQLHelper) {

		names = CustomSQLUtil.keywords(names);
		descriptions = CustomSQLUtil.keywords(descriptions, false);

		Session session = null;

		try {
			session = openSession();

			String sql = null;

			if (ArrayUtil.isEmpty(groupIds)) {
				sql = getSQLQueryByC_G_C_N_D_S_T_S(
					FIND_BY_C_G_C_N_D_S_T_R, 0, classNameId, names,
					descriptions, status, andOperator, inlineSQLHelper);
			}
			else {
				StringBundler sb = new StringBundler(4 * groupIds.length + 1);

				sb.append("SELECT {DDMStructure.*} FROM (");

				for (int i = 0; i < groupIds.length; i++) {
					if (i > 0) {
						sb.append(" UNION ");
					}

					sb.append(StringPool.OPEN_PARENTHESIS);

					sql = getSQLQueryByC_G_C_N_D_S_T_S(
						FIND_BY_C_G_C_N_D_S_T_R, groupIds[i], classNameId,
						names, descriptions, status, andOperator,
						inlineSQLHelper);

					sb.append(sql);

					sb.append(StringPool.CLOSE_PARENTHESIS);
				}

				sb.append(") DDMStructure");

				sql = sb.toString();
			}

			if (orderByComparator != null) {
				sql = CustomSQLUtil.replaceOrderBy(sql, orderByComparator);
			}

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity("DDMStructure", DDMStructureImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			if (ArrayUtil.isEmpty(groupIds)) {
				getQueryPosByC_G_C_N_D_S_T_S(
					qPos, companyId, null, classNameId, names, descriptions,
					storageType, type, status);
			}
			else {
				for (int i = 0; i < groupIds.length; i++) {
					getQueryPosByC_G_C_N_D_S_T_S(
						qPos, companyId, groupIds[i], classNameId, names,
						descriptions, storageType, type, status);
				}
			}

			return (List<DDMStructure>)QueryUtil.list(
				q, getDialect(), start, end);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected String getGroupId(long groupId) {
		if (Validator.isNull(groupId)) {
			return StringPool.BLANK;
		}

		return "DDMStructure.groupId = ? AND";
	}

	protected void getQueryPosByC_G_C_N_D_S_T_S(
		QueryPos qPos, long companyId, Long groupId, long classNameId,
		String[] names, String[] descriptions, String storageType, int type,
		int status) {

		qPos.add(companyId);

		if (groupId != null) {
			qPos.add(groupId);
		}

		qPos.add(classNameId);
		qPos.add(names, 2);
		qPos.add(descriptions, 2);
		qPos.add(storageType);
		qPos.add(storageType);
		qPos.add(type);

		if (status != WorkflowConstants.STATUS_ANY) {
			qPos.add(status);
		}
	}

	protected String getSQLQueryByC_G_C_N_D_S_T_S(
			String id, long groupId, long classNameId, String[] names,
			String[] descriptions, int status, boolean andOperator,
			boolean inlineSQLHelper)
		throws PortalException {

		String sql = CustomSQLUtil.get(getClass(), id);

		if (inlineSQLHelper) {
			sql = InlineSQLHelperUtil.replacePermissionCheck(
				sql,
				DDMStructurePermission.getStructureModelResourceName(
					classNameId),
				"DDMStructure.structureId", groupId);
		}

		sql = StringUtil.replace(sql, "[$GROUP_ID$]", getGroupId(groupId));
		sql = StringUtil.replace(sql, "[$STATUS$]", getStatus(status));
		sql = CustomSQLUtil.replaceKeywords(
			sql, "lower(CAST_TEXT(DDMStructure.name))", StringPool.LIKE, false,
			names);
		sql = CustomSQLUtil.replaceKeywords(
			sql, "DDMStructure.description", StringPool.LIKE, true,
			descriptions);
		sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);

		return sql;
	}

	protected String getStatus(int status) {
		if (status == WorkflowConstants.STATUS_ANY) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(5);

		sb.append("AND EXISTS (SELECT 1 FROM DDMStructureVersion WHERE ");
		sb.append("(DDMStructureVersion.structureId = ");
		sb.append("DDMStructure.structureId) AND ");
		sb.append("(DDMStructureVersion.version = DDMStructure.version) AND ");
		sb.append("(DDMStructureVersion.status = ?))");

		return sb.toString();
	}

}