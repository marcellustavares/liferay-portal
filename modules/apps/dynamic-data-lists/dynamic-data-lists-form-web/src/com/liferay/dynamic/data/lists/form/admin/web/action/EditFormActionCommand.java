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

package com.liferay.dynamic.data.lists.form.admin.web.action;

import com.liferay.dynamic.data.lists.form.admin.web.constants.FormsPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.ActionCommand;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetConstants;
import com.liferay.dynamic.data.lists.service.DDLRecordSetServiceUtil;
import com.liferay.portlet.dynamicdatamapping.StructureDefinitionException;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormLayoutJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureServiceUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
@Component(
	immediate = true,
	property = {
		"action.command.name=editForm",
		"javax.portlet.name=" + FormsPortletKeys.FORMS
	},
	service = ActionCommand.class
)
public class EditFormActionCommand extends TransactionActionCommand {

	@Override
	protected void doTransactionCommand(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		DDMStructure ddmStructure = updateDDMStructure(portletRequest);

		updateDDLRecordSet(portletRequest, ddmStructure.getStructureId());
	}

	protected DDMForm getDDMForm(PortletRequest portletRequest)
		throws PortalException {

		try {
			String definition = ParamUtil.getString(
				portletRequest, "definition");

			return DDMFormJSONDeserializerUtil.deserialize(definition);
		}
		catch (PortalException pe) {
			throw new StructureDefinitionException(pe);
		}
	}

	protected DDMFormLayout getDDMFormLayout(PortletRequest portletRequest)
		throws PortalException {

		String layout = ParamUtil.getString(portletRequest, "layout");

		return DDMFormLayoutJSONDeserializerUtil.deserialize(layout);
	}

	protected void updateDDLRecordSet(
			PortletRequest portletRequest, long ddmStructureId)
		throws Exception {

		long groupId = ParamUtil.getLong(portletRequest, "groupId");
		long recordSetId = ParamUtil.getLong(portletRequest, "recordSetId");
		String recordSetKey = ParamUtil.getString(
			portletRequest, "recordSetKey");
		String name = ParamUtil.getString(portletRequest, "name");
		String description = ParamUtil.getString(portletRequest, "description");

		Map<Locale, String> nameMap = new HashMap<>();

		nameMap.put(LocaleUtil.getDefault(), name);

		Map<Locale, String> descriptionMap = new HashMap<>();

		descriptionMap.put(LocaleUtil.getDefault(), description);

		int scope = ParamUtil.getInteger(portletRequest, "scope");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDLRecordSet.class.getName(), portletRequest);

		if (recordSetId > 0) {
			DDLRecordSetServiceUtil.updateRecordSet(
				recordSetId, ddmStructureId, nameMap, descriptionMap,
				DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT, serviceContext);
		}
		else {
			DDLRecordSetServiceUtil.addRecordSet(
				groupId, ddmStructureId, recordSetKey, nameMap, descriptionMap,
				DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT, scope,
				serviceContext);
		}
	}

	protected DDMStructure updateDDMStructure(PortletRequest portletRequest)
		throws Exception {

		long groupId = ParamUtil.getLong(portletRequest, "groupId");
		long structureId = ParamUtil.getLong(portletRequest, "structureId");
		String structureKey = ParamUtil.getString(
			portletRequest, "structureKey");
		String storageType = ParamUtil.getString(portletRequest, "storageType");

		String name = ParamUtil.getString(portletRequest, "name");
		String description = ParamUtil.getString(portletRequest, "description");

		Map<Locale, String> nameMap = new HashMap<>();

		nameMap.put(LocaleUtil.getDefault(), name);

		Map<Locale, String> descriptionMap = new HashMap<>();

		descriptionMap.put(LocaleUtil.getDefault(), description);

		DDMForm ddmForm = getDDMForm(portletRequest);
		DDMFormLayout ddmFormLayout = getDDMFormLayout(portletRequest);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDMStructure.class.getName(), portletRequest);

		DDMStructure ddmStructure = null;

		if (structureId > 0) {
			ddmStructure = DDMStructureServiceUtil.updateStructure(
				structureId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
				nameMap, descriptionMap, ddmForm, ddmFormLayout,
				serviceContext);
		}
		else {
			ddmStructure = DDMStructureServiceUtil.addStructure(
				groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
				PortalUtil.getClassNameId(DDLRecordSet.class), structureKey,
				nameMap, descriptionMap, ddmForm, ddmFormLayout, storageType,
				DDMStructureConstants.TYPE_DEFAULT, serviceContext);
		}

		return ddmStructure;
	}

}