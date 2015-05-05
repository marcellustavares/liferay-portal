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

package com.liferay.portlet.dynamicdatamapping.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.service.persistence.BasePersistence;

import com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction;

/**
 * The persistence interface for the d d m structure restriction service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portlet.dynamicdatamapping.service.persistence.impl.DDMStructureRestrictionPersistenceImpl
 * @see DDMStructureRestrictionUtil
 * @generated
 */
@ProviderType
public interface DDMStructureRestrictionPersistence extends BasePersistence<DDMStructureRestriction> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link DDMStructureRestrictionUtil} to access the d d m structure restriction persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the d d m structure restrictions where classNameId = &#63; and classPK = &#63;.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @return the matching d d m structure restrictions
	*/
	public java.util.List<DDMStructureRestriction> findByC_C(long classNameId,
		long classPK);

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
	public java.util.List<DDMStructureRestriction> findByC_C(long classNameId,
		long classPK, int start, int end);

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
	public java.util.List<DDMStructureRestriction> findByC_C(long classNameId,
		long classPK, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DDMStructureRestriction> orderByComparator);

	/**
	* Returns the first d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching d d m structure restriction
	* @throws NoSuchStructureRestrictionException if a matching d d m structure restriction could not be found
	*/
	public DDMStructureRestriction findByC_C_First(long classNameId,
		long classPK,
		com.liferay.portal.kernel.util.OrderByComparator<DDMStructureRestriction> orderByComparator)
		throws com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;

	/**
	* Returns the first d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching d d m structure restriction, or <code>null</code> if a matching d d m structure restriction could not be found
	*/
	public DDMStructureRestriction fetchByC_C_First(long classNameId,
		long classPK,
		com.liferay.portal.kernel.util.OrderByComparator<DDMStructureRestriction> orderByComparator);

	/**
	* Returns the last d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching d d m structure restriction
	* @throws NoSuchStructureRestrictionException if a matching d d m structure restriction could not be found
	*/
	public DDMStructureRestriction findByC_C_Last(long classNameId,
		long classPK,
		com.liferay.portal.kernel.util.OrderByComparator<DDMStructureRestriction> orderByComparator)
		throws com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;

	/**
	* Returns the last d d m structure restriction in the ordered set where classNameId = &#63; and classPK = &#63;.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching d d m structure restriction, or <code>null</code> if a matching d d m structure restriction could not be found
	*/
	public DDMStructureRestriction fetchByC_C_Last(long classNameId,
		long classPK,
		com.liferay.portal.kernel.util.OrderByComparator<DDMStructureRestriction> orderByComparator);

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
	public DDMStructureRestriction[] findByC_C_PrevAndNext(
		long structureRestrictionId, long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator<DDMStructureRestriction> orderByComparator)
		throws com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;

	/**
	* Removes all the d d m structure restrictions where classNameId = &#63; and classPK = &#63; from the database.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	*/
	public void removeByC_C(long classNameId, long classPK);

	/**
	* Returns the number of d d m structure restrictions where classNameId = &#63; and classPK = &#63;.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @return the number of matching d d m structure restrictions
	*/
	public int countByC_C(long classNameId, long classPK);

	/**
	* Returns the d d m structure restriction where classNameId = &#63; and classPK = &#63; and structureId = &#63; or throws a {@link NoSuchStructureRestrictionException} if it could not be found.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param structureId the structure ID
	* @return the matching d d m structure restriction
	* @throws NoSuchStructureRestrictionException if a matching d d m structure restriction could not be found
	*/
	public DDMStructureRestriction findByC_C_S(long classNameId, long classPK,
		long structureId)
		throws com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;

	/**
	* Returns the d d m structure restriction where classNameId = &#63; and classPK = &#63; and structureId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param structureId the structure ID
	* @return the matching d d m structure restriction, or <code>null</code> if a matching d d m structure restriction could not be found
	*/
	public DDMStructureRestriction fetchByC_C_S(long classNameId, long classPK,
		long structureId);

	/**
	* Returns the d d m structure restriction where classNameId = &#63; and classPK = &#63; and structureId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param structureId the structure ID
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching d d m structure restriction, or <code>null</code> if a matching d d m structure restriction could not be found
	*/
	public DDMStructureRestriction fetchByC_C_S(long classNameId, long classPK,
		long structureId, boolean retrieveFromCache);

	/**
	* Removes the d d m structure restriction where classNameId = &#63; and classPK = &#63; and structureId = &#63; from the database.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param structureId the structure ID
	* @return the d d m structure restriction that was removed
	*/
	public DDMStructureRestriction removeByC_C_S(long classNameId,
		long classPK, long structureId)
		throws com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;

	/**
	* Returns the number of d d m structure restrictions where classNameId = &#63; and classPK = &#63; and structureId = &#63;.
	*
	* @param classNameId the class name ID
	* @param classPK the class p k
	* @param structureId the structure ID
	* @return the number of matching d d m structure restrictions
	*/
	public int countByC_C_S(long classNameId, long classPK, long structureId);

	/**
	* Caches the d d m structure restriction in the entity cache if it is enabled.
	*
	* @param ddmStructureRestriction the d d m structure restriction
	*/
	public void cacheResult(DDMStructureRestriction ddmStructureRestriction);

	/**
	* Caches the d d m structure restrictions in the entity cache if it is enabled.
	*
	* @param ddmStructureRestrictions the d d m structure restrictions
	*/
	public void cacheResult(
		java.util.List<DDMStructureRestriction> ddmStructureRestrictions);

	/**
	* Creates a new d d m structure restriction with the primary key. Does not add the d d m structure restriction to the database.
	*
	* @param structureRestrictionId the primary key for the new d d m structure restriction
	* @return the new d d m structure restriction
	*/
	public DDMStructureRestriction create(long structureRestrictionId);

	/**
	* Removes the d d m structure restriction with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param structureRestrictionId the primary key of the d d m structure restriction
	* @return the d d m structure restriction that was removed
	* @throws NoSuchStructureRestrictionException if a d d m structure restriction with the primary key could not be found
	*/
	public DDMStructureRestriction remove(long structureRestrictionId)
		throws com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;

	public DDMStructureRestriction updateImpl(
		DDMStructureRestriction ddmStructureRestriction);

	/**
	* Returns the d d m structure restriction with the primary key or throws a {@link NoSuchStructureRestrictionException} if it could not be found.
	*
	* @param structureRestrictionId the primary key of the d d m structure restriction
	* @return the d d m structure restriction
	* @throws NoSuchStructureRestrictionException if a d d m structure restriction with the primary key could not be found
	*/
	public DDMStructureRestriction findByPrimaryKey(long structureRestrictionId)
		throws com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;

	/**
	* Returns the d d m structure restriction with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param structureRestrictionId the primary key of the d d m structure restriction
	* @return the d d m structure restriction, or <code>null</code> if a d d m structure restriction with the primary key could not be found
	*/
	public DDMStructureRestriction fetchByPrimaryKey(
		long structureRestrictionId);

	@Override
	public java.util.Map<java.io.Serializable, DDMStructureRestriction> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the d d m structure restrictions.
	*
	* @return the d d m structure restrictions
	*/
	public java.util.List<DDMStructureRestriction> findAll();

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
	public java.util.List<DDMStructureRestriction> findAll(int start, int end);

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
	public java.util.List<DDMStructureRestriction> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DDMStructureRestriction> orderByComparator);

	/**
	* Removes all the d d m structure restrictions from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of d d m structure restrictions.
	*
	* @return the number of d d m structure restrictions
	*/
	public int countAll();
}