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

package com.liferay.portlet.dynamicdatamapping.service.persistence.test;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.TransactionalTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.dynamicdatamapping.NoSuchStructureRestrictionException;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureRestriction;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureRestrictionLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.persistence.DDMStructureRestrictionPersistence;
import com.liferay.portlet.dynamicdatamapping.service.persistence.DDMStructureRestrictionUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @generated
 */
public class DDMStructureRestrictionPersistenceTest {
	@Rule
	public final AggregateTestRule aggregateTestRule = new AggregateTestRule(new LiferayIntegrationTestRule(),
			PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(Propagation.REQUIRED));

	@Before
	public void setUp() {
		_persistence = DDMStructureRestrictionUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<DDMStructureRestriction> iterator = _ddmStructureRestrictions.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		DDMStructureRestriction ddmStructureRestriction = _persistence.create(pk);

		Assert.assertNotNull(ddmStructureRestriction);

		Assert.assertEquals(ddmStructureRestriction.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		DDMStructureRestriction newDDMStructureRestriction = addDDMStructureRestriction();

		_persistence.remove(newDDMStructureRestriction);

		DDMStructureRestriction existingDDMStructureRestriction = _persistence.fetchByPrimaryKey(newDDMStructureRestriction.getPrimaryKey());

		Assert.assertNull(existingDDMStructureRestriction);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addDDMStructureRestriction();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		DDMStructureRestriction newDDMStructureRestriction = _persistence.create(pk);

		newDDMStructureRestriction.setClassNameId(RandomTestUtil.nextLong());

		newDDMStructureRestriction.setClassPK(RandomTestUtil.nextLong());

		newDDMStructureRestriction.setStructureId(RandomTestUtil.nextLong());

		_ddmStructureRestrictions.add(_persistence.update(
				newDDMStructureRestriction));

		DDMStructureRestriction existingDDMStructureRestriction = _persistence.findByPrimaryKey(newDDMStructureRestriction.getPrimaryKey());

		Assert.assertEquals(existingDDMStructureRestriction.getStructureRestrictionId(),
			newDDMStructureRestriction.getStructureRestrictionId());
		Assert.assertEquals(existingDDMStructureRestriction.getClassNameId(),
			newDDMStructureRestriction.getClassNameId());
		Assert.assertEquals(existingDDMStructureRestriction.getClassPK(),
			newDDMStructureRestriction.getClassPK());
		Assert.assertEquals(existingDDMStructureRestriction.getStructureId(),
			newDDMStructureRestriction.getStructureId());
	}

	@Test
	public void testCountByC_C() throws Exception {
		_persistence.countByC_C(RandomTestUtil.nextLong(),
			RandomTestUtil.nextLong());

		_persistence.countByC_C(0L, 0L);
	}

	@Test
	public void testCountByC_C_S() throws Exception {
		_persistence.countByC_C_S(RandomTestUtil.nextLong(),
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		_persistence.countByC_C_S(0L, 0L, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		DDMStructureRestriction newDDMStructureRestriction = addDDMStructureRestriction();

		DDMStructureRestriction existingDDMStructureRestriction = _persistence.findByPrimaryKey(newDDMStructureRestriction.getPrimaryKey());

		Assert.assertEquals(existingDDMStructureRestriction,
			newDDMStructureRestriction);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchStructureRestrictionException");
		}
		catch (NoSuchStructureRestrictionException nsee) {
		}
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			getOrderByComparator());
	}

	protected OrderByComparator<DDMStructureRestriction> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("DDMStructureRestriction",
			"structureRestrictionId", true, "classNameId", true, "classPK",
			true, "structureId", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		DDMStructureRestriction newDDMStructureRestriction = addDDMStructureRestriction();

		DDMStructureRestriction existingDDMStructureRestriction = _persistence.fetchByPrimaryKey(newDDMStructureRestriction.getPrimaryKey());

		Assert.assertEquals(existingDDMStructureRestriction,
			newDDMStructureRestriction);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		DDMStructureRestriction missingDDMStructureRestriction = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingDDMStructureRestriction);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		DDMStructureRestriction newDDMStructureRestriction1 = addDDMStructureRestriction();
		DDMStructureRestriction newDDMStructureRestriction2 = addDDMStructureRestriction();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newDDMStructureRestriction1.getPrimaryKey());
		primaryKeys.add(newDDMStructureRestriction2.getPrimaryKey());

		Map<Serializable, DDMStructureRestriction> ddmStructureRestrictions = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, ddmStructureRestrictions.size());
		Assert.assertEquals(newDDMStructureRestriction1,
			ddmStructureRestrictions.get(
				newDDMStructureRestriction1.getPrimaryKey()));
		Assert.assertEquals(newDDMStructureRestriction2,
			ddmStructureRestrictions.get(
				newDDMStructureRestriction2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, DDMStructureRestriction> ddmStructureRestrictions = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(ddmStructureRestrictions.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		DDMStructureRestriction newDDMStructureRestriction = addDDMStructureRestriction();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newDDMStructureRestriction.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, DDMStructureRestriction> ddmStructureRestrictions = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, ddmStructureRestrictions.size());
		Assert.assertEquals(newDDMStructureRestriction,
			ddmStructureRestrictions.get(
				newDDMStructureRestriction.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, DDMStructureRestriction> ddmStructureRestrictions = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(ddmStructureRestrictions.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		DDMStructureRestriction newDDMStructureRestriction = addDDMStructureRestriction();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newDDMStructureRestriction.getPrimaryKey());

		Map<Serializable, DDMStructureRestriction> ddmStructureRestrictions = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, ddmStructureRestrictions.size());
		Assert.assertEquals(newDDMStructureRestriction,
			ddmStructureRestrictions.get(
				newDDMStructureRestriction.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = DDMStructureRestrictionLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod() {
				@Override
				public void performAction(Object object) {
					DDMStructureRestriction ddmStructureRestriction = (DDMStructureRestriction)object;

					Assert.assertNotNull(ddmStructureRestriction);

					count.increment();
				}
			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		DDMStructureRestriction newDDMStructureRestriction = addDDMStructureRestriction();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DDMStructureRestriction.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("structureRestrictionId",
				newDDMStructureRestriction.getStructureRestrictionId()));

		List<DDMStructureRestriction> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		DDMStructureRestriction existingDDMStructureRestriction = result.get(0);

		Assert.assertEquals(existingDDMStructureRestriction,
			newDDMStructureRestriction);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DDMStructureRestriction.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("structureRestrictionId",
				RandomTestUtil.nextLong()));

		List<DDMStructureRestriction> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		DDMStructureRestriction newDDMStructureRestriction = addDDMStructureRestriction();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DDMStructureRestriction.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"structureRestrictionId"));

		Object newStructureRestrictionId = newDDMStructureRestriction.getStructureRestrictionId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("structureRestrictionId",
				new Object[] { newStructureRestrictionId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingStructureRestrictionId = result.get(0);

		Assert.assertEquals(existingStructureRestrictionId,
			newStructureRestrictionId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DDMStructureRestriction.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"structureRestrictionId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("structureRestrictionId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		DDMStructureRestriction newDDMStructureRestriction = addDDMStructureRestriction();

		_persistence.clearCache();

		DDMStructureRestriction existingDDMStructureRestriction = _persistence.findByPrimaryKey(newDDMStructureRestriction.getPrimaryKey());

		Assert.assertEquals(existingDDMStructureRestriction.getClassNameId(),
			ReflectionTestUtil.invoke(existingDDMStructureRestriction,
				"getOriginalClassNameId", new Class<?>[0]));
		Assert.assertEquals(existingDDMStructureRestriction.getClassPK(),
			ReflectionTestUtil.invoke(existingDDMStructureRestriction,
				"getOriginalClassPK", new Class<?>[0]));
		Assert.assertEquals(existingDDMStructureRestriction.getStructureId(),
			ReflectionTestUtil.invoke(existingDDMStructureRestriction,
				"getOriginalStructureId", new Class<?>[0]));
	}

	protected DDMStructureRestriction addDDMStructureRestriction()
		throws Exception {
		long pk = RandomTestUtil.nextLong();

		DDMStructureRestriction ddmStructureRestriction = _persistence.create(pk);

		ddmStructureRestriction.setClassNameId(RandomTestUtil.nextLong());

		ddmStructureRestriction.setClassPK(RandomTestUtil.nextLong());

		ddmStructureRestriction.setStructureId(RandomTestUtil.nextLong());

		_ddmStructureRestrictions.add(_persistence.update(
				ddmStructureRestriction));

		return ddmStructureRestriction;
	}

	private List<DDMStructureRestriction> _ddmStructureRestrictions = new ArrayList<DDMStructureRestriction>();
	private DDMStructureRestrictionPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;
}