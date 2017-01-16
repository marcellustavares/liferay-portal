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

package com.liferay.portal.workflow.kaleo.service.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.dao.orm.custom.sql.CustomSQLUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Junction;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionList;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowDefinitionManagerUtil;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.workflow.kaleo.exception.DuplicateKaleoDefinitionNameException;
import com.liferay.portal.workflow.kaleo.exception.KaleoDefinitionContentException;
import com.liferay.portal.workflow.kaleo.exception.KaleoDefinitionNameException;
import com.liferay.portal.workflow.kaleo.exception.NoSuchDefinitionVersionException;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion;
import com.liferay.portal.workflow.kaleo.service.base.KaleoDefinitionVersionLocalServiceBaseImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Inácio Nery
 */
@ProviderType
public class KaleoDefinitionVersionLocalServiceImpl
	extends KaleoDefinitionVersionLocalServiceBaseImpl {

	@Override
	public KaleoDefinitionVersion addKaleoDefinitionVersion(
			long userId, long groupId, String name,
			Map<Locale, String> titleMap, String content, int version,
			ServiceContext serviceContext)
		throws PortalException {

		// Kaleo definition version

		User user = userLocalService.getUser(userId);
		Date now = new Date();

		validate(user.getCompanyId(), name, version);

		long kaleoDefinitionVersionId = counterLocalService.increment();

		KaleoDefinitionVersion kaleoDefinitionVersion =
			kaleoDefinitionVersionPersistence.create(kaleoDefinitionVersionId);

		kaleoDefinitionVersion.setGroupId(groupId);
		kaleoDefinitionVersion.setCompanyId(user.getCompanyId());
		kaleoDefinitionVersion.setUserId(user.getUserId());
		kaleoDefinitionVersion.setUserName(user.getFullName());
		kaleoDefinitionVersion.setCreateDate(now);
		kaleoDefinitionVersion.setModifiedDate(now);
		kaleoDefinitionVersion.setName(name);
		kaleoDefinitionVersion.setTitleMap(titleMap);
		kaleoDefinitionVersion.setContent(content);
		kaleoDefinitionVersion.setVersion(version);

		kaleoDefinitionVersionPersistence.update(kaleoDefinitionVersion);

		// Resources

		resourceLocalService.addModelResources(
			kaleoDefinitionVersion, serviceContext);

		return kaleoDefinitionVersion;
	}

	@Override
	public KaleoDefinitionVersion deleteKaleoDefinitionVersion(
			KaleoDefinitionVersion kaleoDefinitionVersion)
		throws PortalException {

		// Kaleo definition version

		kaleoDefinitionVersionPersistence.remove(kaleoDefinitionVersion);

		// Resources

		resourceLocalService.deleteResource(
			kaleoDefinitionVersion, ResourceConstants.SCOPE_COMPANY);

		return kaleoDefinitionVersion;
	}

	@Override
	public KaleoDefinitionVersion deleteKaleoDefinitionVersion(
			String name, int version, ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionVersion kaleoDefinitionVersion =
			getKaleoDefinitionVersion(name, version, serviceContext);

		return deleteKaleoDefinitionVersion(kaleoDefinitionVersion);
	}

	@Override
	public void deleteKaleoDefinitionVersions(
			String name, ServiceContext serviceContext)
		throws PortalException {

		List<KaleoDefinitionVersion> kaleoDefinitionVersions =
			kaleoDefinitionVersionPersistence.findByC_N(
				serviceContext.getCompanyId(), name);

		for (KaleoDefinitionVersion kaleoDefinitionVersion :
				kaleoDefinitionVersions) {

			deleteKaleoDefinitionVersion(kaleoDefinitionVersion);
		}
	}

	@Override
	public KaleoDefinitionVersion getKaleoDefinitionVersion(
			String name, int version, ServiceContext serviceContext)
		throws PortalException {

		return kaleoDefinitionVersionPersistence.findByC_N_V(
			serviceContext.getCompanyId(), name, version);
	}

	@Override
	public List<KaleoDefinitionVersion> getKaleoDefinitionVersions(
		String name, int start, int end,
		OrderByComparator<KaleoDefinitionVersion> orderByComparator,
		ServiceContext serviceContext) {

		return kaleoDefinitionVersionPersistence.findByC_N(
			serviceContext.getCompanyId(), name, start, end, orderByComparator);
	}

	@Override
	public int getKaleoDefinitionVersionsCount(
		String name, ServiceContext serviceContext) {

		return kaleoDefinitionVersionPersistence.countByC_N(
			serviceContext.getCompanyId(), name);
	}

	@Override
	public KaleoDefinitionVersion getLatestKaleoDefinitionVersion(
			String name, ServiceContext serviceContext)
		throws PortalException {

		List<KaleoDefinitionVersion> kaleoDefinitionVersions =
			kaleoDefinitionVersionPersistence.findByC_N(
				serviceContext.getCompanyId(), name, 0, 1);

		if (kaleoDefinitionVersions.isEmpty()) {
			throw new NoSuchDefinitionVersionException();
		}

		return kaleoDefinitionVersions.get(0);
	}

	@Override
	public List<KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		long companyId, int start, int end,
		OrderByComparator<KaleoDefinitionVersion> orderByComparator) {

		return getLatestKaleoDefinitionVersions(
			companyId, null, start, end, orderByComparator);
	}

	@Override
	public List<KaleoDefinitionVersion> getLatestKaleoDefinitionVersions(
		long companyId, String keywords, int start, int end,
		OrderByComparator<KaleoDefinitionVersion> orderByComparator) {

		List<Long> kaleoDefinitionVersionIds = getKaleoDefinitionVersionIds(
			companyId, keywords);

		if (kaleoDefinitionVersionIds.isEmpty()) {
			return Collections.emptyList();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			KaleoDefinitionVersion.class, getClassLoader());

		Property property = PropertyFactoryUtil.forName(
			"kaleoDefinitionVersionId");

		dynamicQuery.add(property.in(kaleoDefinitionVersionIds));

		return dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	@Override
	public int getLatestKaleoDefinitionVersionsCount(long companyId) {
		return getLatestKaleoDefinitionVersionsCount(companyId, null);
	}

	@Override
	public int getLatestKaleoDefinitionVersionsCount(
		long companyId, String keywords) {

		List<Long> kaleoDefinitionVersionIds = getKaleoDefinitionVersionIds(
			companyId, keywords);

		if (kaleoDefinitionVersionIds.isEmpty()) {
			return 0;
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			KaleoDefinitionVersion.class, getClassLoader());

		Property property = PropertyFactoryUtil.forName(
			"kaleoDefinitionVersionId");

		dynamicQuery.add(property.in(kaleoDefinitionVersionIds));

		return (int)dynamicQueryCount(dynamicQuery);
	}

	@Override
	public KaleoDefinitionVersion incrementKaleoDefinitionVersion(
			long userId, String name, ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionVersion kaleoDefinitionVersion =
			getLatestKaleoDefinitionVersion(name, serviceContext);

		return addKaleoDefinitionVersion(
			userId, kaleoDefinitionVersion.getGroupId(),
			kaleoDefinitionVersion.getName(),
			kaleoDefinitionVersion.getTitleMap(),
			kaleoDefinitionVersion.getContent(),
			kaleoDefinitionVersion.getVersion() + 1, serviceContext);
	}

	@Override
	public KaleoDefinitionVersion publishKaleoDefinitionVersion(
			long userId, long groupId, String name,
			Map<Locale, String> titleMap, String content,
			ServiceContext serviceContext)
		throws PortalException {

		validate(content);

		WorkflowDefinition workflowDefinition =
			WorkflowDefinitionManagerUtil.deployWorkflowDefinition(
				serviceContext.getCompanyId(), serviceContext.getUserId(),
				_getLocalizedTitleXML(titleMap), content.getBytes());

		int version = workflowDefinition.getVersion();

		KaleoDefinitionVersion kaleoDefinitionVersion =
			addKaleoDefinitionVersion(
				userId, groupId, name, titleMap, content, 1, serviceContext);

		if (version == 1) {
			deleteKaleoDefinitionVersions(name, serviceContext);
		}

		return kaleoDefinitionVersion;
	}

	@Override
	public KaleoDefinitionVersion updateKaleoDefinitionVersion(
			long userId, String name, Map<Locale, String> titleMap,
			String content, ServiceContext serviceContext)
		throws PortalException {

		KaleoDefinitionVersion kaleoDefinitionVersion =
			incrementKaleoDefinitionVersion(userId, name, serviceContext);

		kaleoDefinitionVersion.setTitleMap(titleMap);
		kaleoDefinitionVersion.setContent(content);

		kaleoDefinitionVersionPersistence.update(kaleoDefinitionVersion);

		return kaleoDefinitionVersion;
	}

	protected void addKeywordsCriterion(
		DynamicQuery dynamicQuery, String keywords) {

		if (Validator.isNull(keywords)) {
			return;
		}

		Junction junction = RestrictionsFactoryUtil.disjunction();

		for (String keyword : CustomSQLUtil.keywords(keywords)) {
			junction.add(RestrictionsFactoryUtil.ilike("name", keyword));
			junction.add(RestrictionsFactoryUtil.ilike("title", keyword));
		}

		dynamicQuery.add(junction);
	}

	protected List<Long> getKaleoDefinitionVersionIds(long companyId) {
		return getKaleoDefinitionVersionIds(companyId, null);
	}

	protected List<Long> getKaleoDefinitionVersionIds(
		long companyId, String keywords) {

		List<Long> kaleoDefinitionVersionIds = new ArrayList<>();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			KaleoDefinitionVersion.class, getClassLoader());

		Property companyIdProperty = PropertyFactoryUtil.forName("companyId");

		dynamicQuery.add(companyIdProperty.eq(companyId));

		addKeywordsCriterion(dynamicQuery, keywords);

		ProjectionList projectionList = ProjectionFactoryUtil.projectionList();

		projectionList.add(
			ProjectionFactoryUtil.max("kaleoDefinitionVersionId"));
		projectionList.add(ProjectionFactoryUtil.groupProperty("name"));

		dynamicQuery.setProjection(projectionList);

		List<Object[]> results = dynamicQuery(dynamicQuery);

		for (Object[] result : results) {
			kaleoDefinitionVersionIds.add((Long)result[0]);
		}

		return kaleoDefinitionVersionIds;
	}

	protected void validate(long companyId, String name, int version)
		throws PortalException {

		if (Validator.isNull(name)) {
			throw new KaleoDefinitionNameException();
		}

		if (kaleoDefinitionVersionPersistence.countByC_N_V(
				companyId, name, version) > 0) {

			throw new DuplicateKaleoDefinitionNameException();
		}
	}

	protected void validate(
			long companyId, String name, String content, int version)
		throws PortalException {

		validate(companyId, name, version);
		validate(content);
	}

	protected void validate(String content) throws PortalException {
		try {
			WorkflowDefinitionManagerUtil.validateWorkflowDefinition(
				content.getBytes());
		}
		catch (WorkflowException we) {
			throw new KaleoDefinitionContentException(we);
		}
	}

	private static String _getLocalizedTitleXML(Map<Locale, String> titleMap) {
		String title = StringPool.BLANK;

		if (titleMap == null) {
			return title;
		}

		String defaultLanguageId = LocaleUtil.toLanguageId(
			LocaleUtil.getDefault());

		for (Locale locale : LanguageUtil.getAvailableLocales()) {
			String languageId = LocaleUtil.toLanguageId(locale);

			String localizedTitle = titleMap.get(locale);

			if (Validator.isNotNull(localizedTitle)) {
				title = LocalizationUtil.updateLocalization(
					title, "Title", localizedTitle, languageId,
					defaultLanguageId);
			}
		}

		return title;
	}

}