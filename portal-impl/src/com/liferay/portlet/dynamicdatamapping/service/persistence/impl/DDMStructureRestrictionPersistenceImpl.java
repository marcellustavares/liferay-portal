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

package com.liferay.portlet.dynamicdatamapping.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction;
import com.liferay.portlet.dynamicdatamapping.model.impl.DDMStructureRestrictionImpl;
import com.liferay.portlet.dynamicdatamapping.model.impl.DDMStructureRestrictionModelImpl;
import com.liferay.portlet.dynamicdatamapping.service.persistence.DDMStructureRestrictionPersistence;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the d d m structure restriction service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDMStructureRestrictionPersistence
 * @see com.liferay.portlet.dynamicdatamapping.service.persistence.DDMStructureRestrictionUtil
 * @generated
 */
@ProviderType
public class DDMStructureRestrictionPersistenceImpl extends BasePersistenceImpl<DDMStructureRestriction>
	implements DDMStructureRestrictionPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link DDMStructureRestrictionUtil} to access the d d m structure restriction persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = DDMStructureRestrictionImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionModelImpl.FINDER_CACHE_ENABLED,
			DDMStructureRestrictionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionModelImpl.FINDER_CACHE_ENABLED,
			DDMStructureRestrictionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_C_C = new FinderPath(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionModelImpl.FINDER_CACHE_ENABLED,
			DDMStructureRestrictionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByC_C",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_C = new FinderPath(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionModelImpl.FINDER_CACHE_ENABLED,
			DDMStructureRestrictionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByC_C",
			new String[] { Long.class.getName(), Long.class.getName() },
			DDMStructureRestrictionModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			DDMStructureRestrictionModelImpl.CLASSPK_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_C_C = new FinderPath(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns all the d d m structure restrictions where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @return the matching d d m structure restrictions
	 */
	@Override
	public List<DDMStructureRestriction> findByC_C(long classNameId,
		long classPK) {
		return findByC_C(classNameId, classPK, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the d d m structure restrictions where classNameId = &#63; and classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DDMStructureRestrictionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param start the lower bound of the range of d d m structure restrictions
	 * @param end the upper bound of the range of d d m structure restrictions (not inclusive)
	 * @return the range of matching d d m structure restrictions
	 */
	@Override
	public List<DDMStructureRestriction> findByC_C(long classNameId,
		long classPK, int start, int end) {
		return findByC_C(classNameId, classPK, start, end, null);
	}

	/**
	 * Returns an ordered range of all the d d m structure restrictions where classNameId = &#63; and classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DDMStructureRestrictionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param start the lower bound of the range of d d m structure restrictions
	 * @param end the upper bound of the range of d d m structure restrictions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching d d m structure restrictions
	 */
	@Override
	public List<DDMStructureRestriction> findByC_C(long classNameId,
		long classPK, int start, int end,
		OrderByComparator<DDMStructureRestriction> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_C;
			finderArgs = new Object[] { classNameId, classPK };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_C_C;
			finderArgs = new Object[] {
					classNameId, classPK,
					
					start, end, orderByComparator
				};
		}

		List<DDMStructureRestriction> list = (List<DDMStructureRestriction>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DDMStructureRestriction ddmStructureRestriction : list) {
				if ((classNameId != ddmStructureRestriction.getClassNameId()) ||
						(classPK != ddmStructureRestriction.getClassPK())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_DDMSTRUCTURERESTRICTION_WHERE);

			query.append(_FINDER_COLUMN_C_C_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_C_C_CLASSPK_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DDMStructureRestrictionModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				qPos.add(classPK);

				if (!pagination) {
					list = (List<DDMStructureRestriction>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DDMStructureRestriction>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching d d m structure restriction
	 * @throws NoSuchStructureRestrictionException if a matching d d m structure restriction could not be found
	 */
	@Override
	public DDMStructureRestriction findByC_C_First(long classNameId,
		long classPK,
		OrderByComparator<DDMStructureRestriction> orderByComparator)
		throws NoSuchStructureRestrictionException {
		DDMStructureRestriction ddmStructureRestriction = fetchByC_C_First(classNameId,
				classPK, orderByComparator);

		if (ddmStructureRestriction != null) {
			return ddmStructureRestriction;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("classNameId=");
		msg.append(classNameId);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchStructureRestrictionException(msg.toString());
	}

	/**
	 * Returns the first d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching d d m structure restriction, or <code>null</code> if a matching d d m structure restriction could not be found
	 */
	@Override
	public DDMStructureRestriction fetchByC_C_First(long classNameId,
		long classPK,
		OrderByComparator<DDMStructureRestriction> orderByComparator) {
		List<DDMStructureRestriction> list = findByC_C(classNameId, classPK, 0,
				1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching d d m structure restriction
	 * @throws NoSuchStructureRestrictionException if a matching d d m structure restriction could not be found
	 */
	@Override
	public DDMStructureRestriction findByC_C_Last(long classNameId,
		long classPK,
		OrderByComparator<DDMStructureRestriction> orderByComparator)
		throws NoSuchStructureRestrictionException {
		DDMStructureRestriction ddmStructureRestriction = fetchByC_C_Last(classNameId,
				classPK, orderByComparator);

		if (ddmStructureRestriction != null) {
			return ddmStructureRestriction;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("classNameId=");
		msg.append(classNameId);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchStructureRestrictionException(msg.toString());
	}

	/**
	 * Returns the last d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching d d m structure restriction, or <code>null</code> if a matching d d m structure restriction could not be found
	 */
	@Override
	public DDMStructureRestriction fetchByC_C_Last(long classNameId,
		long classPK,
		OrderByComparator<DDMStructureRestriction> orderByComparator) {
		int count = countByC_C(classNameId, classPK);

		if (count == 0) {
			return null;
		}

		List<DDMStructureRestriction> list = findByC_C(classNameId, classPK,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the d d m structure restrictions before and after the current d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param structureRestrictionId the primary key of the current d d m structure restriction
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next d d m structure restriction
	 * @throws NoSuchStructureRestrictionException if a d d m structure restriction with the primary key could not be found
	 */
	@Override
	public DDMStructureRestriction[] findByC_C_PrevAndNext(
		long structureRestrictionId, long classNameId, long classPK,
		OrderByComparator<DDMStructureRestriction> orderByComparator)
		throws NoSuchStructureRestrictionException {
		DDMStructureRestriction ddmStructureRestriction = findByPrimaryKey(structureRestrictionId);

		Session session = null;

		try {
			session = openSession();

			DDMStructureRestriction[] array = new DDMStructureRestrictionImpl[3];

			array[0] = getByC_C_PrevAndNext(session, ddmStructureRestriction,
					classNameId, classPK, orderByComparator, true);

			array[1] = ddmStructureRestriction;

			array[2] = getByC_C_PrevAndNext(session, ddmStructureRestriction,
					classNameId, classPK, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DDMStructureRestriction getByC_C_PrevAndNext(Session session,
		DDMStructureRestriction ddmStructureRestriction, long classNameId,
		long classPK,
		OrderByComparator<DDMStructureRestriction> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DDMSTRUCTURERESTRICTION_WHERE);

		query.append(_FINDER_COLUMN_C_C_CLASSNAMEID_2);

		query.append(_FINDER_COLUMN_C_C_CLASSPK_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DDMStructureRestrictionModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(classNameId);

		qPos.add(classPK);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(ddmStructureRestriction);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DDMStructureRestriction> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the d d m structure restrictions where classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 */
	@Override
	public void removeByC_C(long classNameId, long classPK) {
		for (DDMStructureRestriction ddmStructureRestriction : findByC_C(
				classNameId, classPK, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(ddmStructureRestriction);
		}
	}

	/**
	 * Returns the number of d d m structure restrictions where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @return the number of matching d d m structure restrictions
	 */
	@Override
	public int countByC_C(long classNameId, long classPK) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_C_C;

		Object[] finderArgs = new Object[] { classNameId, classPK };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_DDMSTRUCTURERESTRICTION_WHERE);

			query.append(_FINDER_COLUMN_C_C_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_C_C_CLASSPK_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				qPos.add(classPK);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_C_CLASSNAMEID_2 = "ddmStructureRestriction.classNameId = ? AND ";
	private static final String _FINDER_COLUMN_C_C_CLASSPK_2 = "ddmStructureRestriction.classPK = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_C_C_S = new FinderPath(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionModelImpl.FINDER_CACHE_ENABLED,
			DDMStructureRestrictionImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByC_C_S",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			},
			DDMStructureRestrictionModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			DDMStructureRestrictionModelImpl.CLASSPK_COLUMN_BITMASK |
			DDMStructureRestrictionModelImpl.STRUCTUREID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_C_C_S = new FinderPath(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C_S",
			new String[] {
				Long.class.getName(), Long.class.getName(), Long.class.getName()
			});

	/**
	 * Returns the d d m structure restriction where classNameId = &#63; and classPK = &#63; and structureId = &#63; or throws a {@link NoSuchStructureRestrictionException} if it could not be found.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param structureId the structure ID
	 * @return the matching d d m structure restriction
	 * @throws NoSuchStructureRestrictionException if a matching d d m structure restriction could not be found
	 */
	@Override
	public DDMStructureRestriction findByC_C_S(long classNameId, long classPK,
		long structureId) throws NoSuchStructureRestrictionException {
		DDMStructureRestriction ddmStructureRestriction = fetchByC_C_S(classNameId,
				classPK, structureId);

		if (ddmStructureRestriction == null) {
			StringBundler msg = new StringBundler(8);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("classNameId=");
			msg.append(classNameId);

			msg.append(", classPK=");
			msg.append(classPK);

			msg.append(", structureId=");
			msg.append(structureId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchStructureRestrictionException(msg.toString());
		}

		return ddmStructureRestriction;
	}

	/**
	 * Returns the d d m structure restriction where classNameId = &#63; and classPK = &#63; and structureId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param structureId the structure ID
	 * @return the matching d d m structure restriction, or <code>null</code> if a matching d d m structure restriction could not be found
	 */
	@Override
	public DDMStructureRestriction fetchByC_C_S(long classNameId, long classPK,
		long structureId) {
		return fetchByC_C_S(classNameId, classPK, structureId, true);
	}

	/**
	 * Returns the d d m structure restriction where classNameId = &#63; and classPK = &#63; and structureId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param structureId the structure ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching d d m structure restriction, or <code>null</code> if a matching d d m structure restriction could not be found
	 */
	@Override
	public DDMStructureRestriction fetchByC_C_S(long classNameId, long classPK,
		long structureId, boolean retrieveFromCache) {
		Object[] finderArgs = new Object[] { classNameId, classPK, structureId };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_C_C_S,
					finderArgs, this);
		}

		if (result instanceof DDMStructureRestriction) {
			DDMStructureRestriction ddmStructureRestriction = (DDMStructureRestriction)result;

			if ((classNameId != ddmStructureRestriction.getClassNameId()) ||
					(classPK != ddmStructureRestriction.getClassPK()) ||
					(structureId != ddmStructureRestriction.getStructureId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_SELECT_DDMSTRUCTURERESTRICTION_WHERE);

			query.append(_FINDER_COLUMN_C_C_S_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_C_C_S_CLASSPK_2);

			query.append(_FINDER_COLUMN_C_C_S_STRUCTUREID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				qPos.add(classPK);

				qPos.add(structureId);

				List<DDMStructureRestriction> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_S,
						finderArgs, list);
				}
				else {
					DDMStructureRestriction ddmStructureRestriction = list.get(0);

					result = ddmStructureRestriction;

					cacheResult(ddmStructureRestriction);

					if ((ddmStructureRestriction.getClassNameId() != classNameId) ||
							(ddmStructureRestriction.getClassPK() != classPK) ||
							(ddmStructureRestriction.getStructureId() != structureId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_S,
							finderArgs, ddmStructureRestriction);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_C_C_S,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (DDMStructureRestriction)result;
		}
	}

	/**
	 * Removes the d d m structure restriction where classNameId = &#63; and classPK = &#63; and structureId = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param structureId the structure ID
	 * @return the d d m structure restriction that was removed
	 */
	@Override
	public DDMStructureRestriction removeByC_C_S(long classNameId,
		long classPK, long structureId)
		throws NoSuchStructureRestrictionException {
		DDMStructureRestriction ddmStructureRestriction = findByC_C_S(classNameId,
				classPK, structureId);

		return remove(ddmStructureRestriction);
	}

	/**
	 * Returns the number of d d m structure restrictions where classNameId = &#63; and classPK = &#63; and structureId = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param structureId the structure ID
	 * @return the number of matching d d m structure restrictions
	 */
	@Override
	public int countByC_C_S(long classNameId, long classPK, long structureId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_C_C_S;

		Object[] finderArgs = new Object[] { classNameId, classPK, structureId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_DDMSTRUCTURERESTRICTION_WHERE);

			query.append(_FINDER_COLUMN_C_C_S_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_C_C_S_CLASSPK_2);

			query.append(_FINDER_COLUMN_C_C_S_STRUCTUREID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				qPos.add(classPK);

				qPos.add(structureId);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_C_S_CLASSNAMEID_2 = "ddmStructureRestriction.classNameId = ? AND ";
	private static final String _FINDER_COLUMN_C_C_S_CLASSPK_2 = "ddmStructureRestriction.classPK = ? AND ";
	private static final String _FINDER_COLUMN_C_C_S_STRUCTUREID_2 = "ddmStructureRestriction.structureId = ?";

	public DDMStructureRestrictionPersistenceImpl() {
		setModelClass(DDMStructureRestriction.class);
	}

	/**
	 * Caches the d d m structure restriction in the entity cache if it is enabled.
	 *
	 * @param ddmStructureRestriction the d d m structure restriction
	 */
	@Override
	public void cacheResult(DDMStructureRestriction ddmStructureRestriction) {
		EntityCacheUtil.putResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionImpl.class,
			ddmStructureRestriction.getPrimaryKey(), ddmStructureRestriction);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_S,
			new Object[] {
				ddmStructureRestriction.getClassNameId(),
				ddmStructureRestriction.getClassPK(),
				ddmStructureRestriction.getStructureId()
			}, ddmStructureRestriction);

		ddmStructureRestriction.resetOriginalValues();
	}

	/**
	 * Caches the d d m structure restrictions in the entity cache if it is enabled.
	 *
	 * @param ddmStructureRestrictions the d d m structure restrictions
	 */
	@Override
	public void cacheResult(
		List<DDMStructureRestriction> ddmStructureRestrictions) {
		for (DDMStructureRestriction ddmStructureRestriction : ddmStructureRestrictions) {
			if (EntityCacheUtil.getResult(
						DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
						DDMStructureRestrictionImpl.class,
						ddmStructureRestriction.getPrimaryKey()) == null) {
				cacheResult(ddmStructureRestriction);
			}
			else {
				ddmStructureRestriction.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all d d m structure restrictions.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(DDMStructureRestrictionImpl.class.getName());
		}

		EntityCacheUtil.clearCache(DDMStructureRestrictionImpl.class);

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the d d m structure restriction.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DDMStructureRestriction ddmStructureRestriction) {
		EntityCacheUtil.removeResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionImpl.class,
			ddmStructureRestriction.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(ddmStructureRestriction);
	}

	@Override
	public void clearCache(
		List<DDMStructureRestriction> ddmStructureRestrictions) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (DDMStructureRestriction ddmStructureRestriction : ddmStructureRestrictions) {
			EntityCacheUtil.removeResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
				DDMStructureRestrictionImpl.class,
				ddmStructureRestriction.getPrimaryKey());

			clearUniqueFindersCache(ddmStructureRestriction);
		}
	}

	protected void cacheUniqueFindersCache(
		DDMStructureRestriction ddmStructureRestriction) {
		if (ddmStructureRestriction.isNew()) {
			Object[] args = new Object[] {
					ddmStructureRestriction.getClassNameId(),
					ddmStructureRestriction.getClassPK(),
					ddmStructureRestriction.getStructureId()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_C_C_S, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_S, args,
				ddmStructureRestriction);
		}
		else {
			DDMStructureRestrictionModelImpl ddmStructureRestrictionModelImpl = (DDMStructureRestrictionModelImpl)ddmStructureRestriction;

			if ((ddmStructureRestrictionModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_C_C_S.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						ddmStructureRestriction.getClassNameId(),
						ddmStructureRestriction.getClassPK(),
						ddmStructureRestriction.getStructureId()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_C_C_S, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_S, args,
					ddmStructureRestriction);
			}
		}
	}

	protected void clearUniqueFindersCache(
		DDMStructureRestriction ddmStructureRestriction) {
		DDMStructureRestrictionModelImpl ddmStructureRestrictionModelImpl = (DDMStructureRestrictionModelImpl)ddmStructureRestriction;

		Object[] args = new Object[] {
				ddmStructureRestriction.getClassNameId(),
				ddmStructureRestriction.getClassPK(),
				ddmStructureRestriction.getStructureId()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_C_C_S, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_C_C_S, args);

		if ((ddmStructureRestrictionModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_C_C_S.getColumnBitmask()) != 0) {
			args = new Object[] {
					ddmStructureRestrictionModelImpl.getOriginalClassNameId(),
					ddmStructureRestrictionModelImpl.getOriginalClassPK(),
					ddmStructureRestrictionModelImpl.getOriginalStructureId()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_C_C_S, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_C_C_S, args);
		}
	}

	/**
	 * Creates a new d d m structure restriction with the primary key. Does not add the d d m structure restriction to the database.
	 *
	 * @param structureRestrictionId the primary key for the new d d m structure restriction
	 * @return the new d d m structure restriction
	 */
	@Override
	public DDMStructureRestriction create(long structureRestrictionId) {
		DDMStructureRestriction ddmStructureRestriction = new DDMStructureRestrictionImpl();

		ddmStructureRestriction.setNew(true);
		ddmStructureRestriction.setPrimaryKey(structureRestrictionId);

		return ddmStructureRestriction;
	}

	/**
	 * Removes the d d m structure restriction with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param structureRestrictionId the primary key of the d d m structure restriction
	 * @return the d d m structure restriction that was removed
	 * @throws NoSuchStructureRestrictionException if a d d m structure restriction with the primary key could not be found
	 */
	@Override
	public DDMStructureRestriction remove(long structureRestrictionId)
		throws NoSuchStructureRestrictionException {
		return remove((Serializable)structureRestrictionId);
	}

	/**
	 * Removes the d d m structure restriction with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the d d m structure restriction
	 * @return the d d m structure restriction that was removed
	 * @throws NoSuchStructureRestrictionException if a d d m structure restriction with the primary key could not be found
	 */
	@Override
	public DDMStructureRestriction remove(Serializable primaryKey)
		throws NoSuchStructureRestrictionException {
		Session session = null;

		try {
			session = openSession();

			DDMStructureRestriction ddmStructureRestriction = (DDMStructureRestriction)session.get(DDMStructureRestrictionImpl.class,
					primaryKey);

			if (ddmStructureRestriction == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchStructureRestrictionException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(ddmStructureRestriction);
		}
		catch (NoSuchStructureRestrictionException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected DDMStructureRestriction removeImpl(
		DDMStructureRestriction ddmStructureRestriction) {
		ddmStructureRestriction = toUnwrappedModel(ddmStructureRestriction);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(ddmStructureRestriction)) {
				ddmStructureRestriction = (DDMStructureRestriction)session.get(DDMStructureRestrictionImpl.class,
						ddmStructureRestriction.getPrimaryKeyObj());
			}

			if (ddmStructureRestriction != null) {
				session.delete(ddmStructureRestriction);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (ddmStructureRestriction != null) {
			clearCache(ddmStructureRestriction);
		}

		return ddmStructureRestriction;
	}

	@Override
	public DDMStructureRestriction updateImpl(
		DDMStructureRestriction ddmStructureRestriction) {
		ddmStructureRestriction = toUnwrappedModel(ddmStructureRestriction);

		boolean isNew = ddmStructureRestriction.isNew();

		DDMStructureRestrictionModelImpl ddmStructureRestrictionModelImpl = (DDMStructureRestrictionModelImpl)ddmStructureRestriction;

		Session session = null;

		try {
			session = openSession();

			if (ddmStructureRestriction.isNew()) {
				session.save(ddmStructureRestriction);

				ddmStructureRestriction.setNew(false);
			}
			else {
				session.merge(ddmStructureRestriction);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !DDMStructureRestrictionModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((ddmStructureRestrictionModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_C.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						ddmStructureRestrictionModelImpl.getOriginalClassNameId(),
						ddmStructureRestrictionModelImpl.getOriginalClassPK()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_C_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_C,
					args);

				args = new Object[] {
						ddmStructureRestrictionModelImpl.getClassNameId(),
						ddmStructureRestrictionModelImpl.getClassPK()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_C_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_C_C,
					args);
			}
		}

		EntityCacheUtil.putResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
			DDMStructureRestrictionImpl.class,
			ddmStructureRestriction.getPrimaryKey(), ddmStructureRestriction,
			false);

		clearUniqueFindersCache(ddmStructureRestriction);
		cacheUniqueFindersCache(ddmStructureRestriction);

		ddmStructureRestriction.resetOriginalValues();

		return ddmStructureRestriction;
	}

	protected DDMStructureRestriction toUnwrappedModel(
		DDMStructureRestriction ddmStructureRestriction) {
		if (ddmStructureRestriction instanceof DDMStructureRestrictionImpl) {
			return ddmStructureRestriction;
		}

		DDMStructureRestrictionImpl ddmStructureRestrictionImpl = new DDMStructureRestrictionImpl();

		ddmStructureRestrictionImpl.setNew(ddmStructureRestriction.isNew());
		ddmStructureRestrictionImpl.setPrimaryKey(ddmStructureRestriction.getPrimaryKey());

		ddmStructureRestrictionImpl.setStructureRestrictionId(ddmStructureRestriction.getStructureRestrictionId());
		ddmStructureRestrictionImpl.setClassNameId(ddmStructureRestriction.getClassNameId());
		ddmStructureRestrictionImpl.setClassPK(ddmStructureRestriction.getClassPK());
		ddmStructureRestrictionImpl.setStructureId(ddmStructureRestriction.getStructureId());

		return ddmStructureRestrictionImpl;
	}

	/**
	 * Returns the d d m structure restriction with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the d d m structure restriction
	 * @return the d d m structure restriction
	 * @throws NoSuchStructureRestrictionException if a d d m structure restriction with the primary key could not be found
	 */
	@Override
	public DDMStructureRestriction findByPrimaryKey(Serializable primaryKey)
		throws NoSuchStructureRestrictionException {
		DDMStructureRestriction ddmStructureRestriction = fetchByPrimaryKey(primaryKey);

		if (ddmStructureRestriction == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchStructureRestrictionException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return ddmStructureRestriction;
	}

	/**
	 * Returns the d d m structure restriction with the primary key or throws a {@link NoSuchStructureRestrictionException} if it could not be found.
	 *
	 * @param structureRestrictionId the primary key of the d d m structure restriction
	 * @return the d d m structure restriction
	 * @throws NoSuchStructureRestrictionException if a d d m structure restriction with the primary key could not be found
	 */
	@Override
	public DDMStructureRestriction findByPrimaryKey(long structureRestrictionId)
		throws NoSuchStructureRestrictionException {
		return findByPrimaryKey((Serializable)structureRestrictionId);
	}

	/**
	 * Returns the d d m structure restriction with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the d d m structure restriction
	 * @return the d d m structure restriction, or <code>null</code> if a d d m structure restriction with the primary key could not be found
	 */
	@Override
	public DDMStructureRestriction fetchByPrimaryKey(Serializable primaryKey) {
		DDMStructureRestriction ddmStructureRestriction = (DDMStructureRestriction)EntityCacheUtil.getResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
				DDMStructureRestrictionImpl.class, primaryKey);

		if (ddmStructureRestriction == _nullDDMStructureRestriction) {
			return null;
		}

		if (ddmStructureRestriction == null) {
			Session session = null;

			try {
				session = openSession();

				ddmStructureRestriction = (DDMStructureRestriction)session.get(DDMStructureRestrictionImpl.class,
						primaryKey);

				if (ddmStructureRestriction != null) {
					cacheResult(ddmStructureRestriction);
				}
				else {
					EntityCacheUtil.putResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
						DDMStructureRestrictionImpl.class, primaryKey,
						_nullDDMStructureRestriction);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
					DDMStructureRestrictionImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return ddmStructureRestriction;
	}

	/**
	 * Returns the d d m structure restriction with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param structureRestrictionId the primary key of the d d m structure restriction
	 * @return the d d m structure restriction, or <code>null</code> if a d d m structure restriction with the primary key could not be found
	 */
	@Override
	public DDMStructureRestriction fetchByPrimaryKey(
		long structureRestrictionId) {
		return fetchByPrimaryKey((Serializable)structureRestrictionId);
	}

	@Override
	public Map<Serializable, DDMStructureRestriction> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, DDMStructureRestriction> map = new HashMap<Serializable, DDMStructureRestriction>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			DDMStructureRestriction ddmStructureRestriction = fetchByPrimaryKey(primaryKey);

			if (ddmStructureRestriction != null) {
				map.put(primaryKey, ddmStructureRestriction);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			DDMStructureRestriction ddmStructureRestriction = (DDMStructureRestriction)EntityCacheUtil.getResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
					DDMStructureRestrictionImpl.class, primaryKey);

			if (ddmStructureRestriction == null) {
				if (uncachedPrimaryKeys == null) {
					uncachedPrimaryKeys = new HashSet<Serializable>();
				}

				uncachedPrimaryKeys.add(primaryKey);
			}
			else {
				map.put(primaryKey, ddmStructureRestriction);
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_DDMSTRUCTURERESTRICTION_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append(String.valueOf(primaryKey));

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (DDMStructureRestriction ddmStructureRestriction : (List<DDMStructureRestriction>)q.list()) {
				map.put(ddmStructureRestriction.getPrimaryKeyObj(),
					ddmStructureRestriction);

				cacheResult(ddmStructureRestriction);

				uncachedPrimaryKeys.remove(ddmStructureRestriction.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				EntityCacheUtil.putResult(DDMStructureRestrictionModelImpl.ENTITY_CACHE_ENABLED,
					DDMStructureRestrictionImpl.class, primaryKey,
					_nullDDMStructureRestriction);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the d d m structure restrictions.
	 *
	 * @return the d d m structure restrictions
	 */
	@Override
	public List<DDMStructureRestriction> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the d d m structure restrictions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DDMStructureRestrictionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of d d m structure restrictions
	 * @param end the upper bound of the range of d d m structure restrictions (not inclusive)
	 * @return the range of d d m structure restrictions
	 */
	@Override
	public List<DDMStructureRestriction> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the d d m structure restrictions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DDMStructureRestrictionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of d d m structure restrictions
	 * @param end the upper bound of the range of d d m structure restrictions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of d d m structure restrictions
	 */
	@Override
	public List<DDMStructureRestriction> findAll(int start, int end,
		OrderByComparator<DDMStructureRestriction> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<DDMStructureRestriction> list = (List<DDMStructureRestriction>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_DDMSTRUCTURERESTRICTION);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_DDMSTRUCTURERESTRICTION;

				if (pagination) {
					sql = sql.concat(DDMStructureRestrictionModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<DDMStructureRestriction>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DDMStructureRestriction>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the d d m structure restrictions from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (DDMStructureRestriction ddmStructureRestriction : findAll()) {
			remove(ddmStructureRestriction);
		}
	}

	/**
	 * Returns the number of d d m structure restrictions.
	 *
	 * @return the number of d d m structure restrictions
	 */
	@Override
	public int countAll() {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_DDMSTRUCTURERESTRICTION);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the d d m structure restriction persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		EntityCacheUtil.removeCache(DDMStructureRestrictionImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_DDMSTRUCTURERESTRICTION = "SELECT ddmStructureRestriction FROM DDMStructureRestriction ddmStructureRestriction";
	private static final String _SQL_SELECT_DDMSTRUCTURERESTRICTION_WHERE_PKS_IN =
		"SELECT ddmStructureRestriction FROM DDMStructureRestriction ddmStructureRestriction WHERE structureRestrictionId IN (";
	private static final String _SQL_SELECT_DDMSTRUCTURERESTRICTION_WHERE = "SELECT ddmStructureRestriction FROM DDMStructureRestriction ddmStructureRestriction WHERE ";
	private static final String _SQL_COUNT_DDMSTRUCTURERESTRICTION = "SELECT COUNT(ddmStructureRestriction) FROM DDMStructureRestriction ddmStructureRestriction";
	private static final String _SQL_COUNT_DDMSTRUCTURERESTRICTION_WHERE = "SELECT COUNT(ddmStructureRestriction) FROM DDMStructureRestriction ddmStructureRestriction WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "ddmStructureRestriction.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No DDMStructureRestriction exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No DDMStructureRestriction exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = com.liferay.portal.util.PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE;
	private static final Log _log = LogFactoryUtil.getLog(DDMStructureRestrictionPersistenceImpl.class);
	private static final DDMStructureRestriction _nullDDMStructureRestriction = new DDMStructureRestrictionImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<DDMStructureRestriction> toCacheModel() {
				return _nullDDMStructureRestrictionCacheModel;
			}
		};

	private static final CacheModel<DDMStructureRestriction> _nullDDMStructureRestrictionCacheModel =
		new CacheModel<DDMStructureRestriction>() {
			@Override
			public DDMStructureRestriction toEntityModel() {
				return _nullDDMStructureRestriction;
			}
		};
}