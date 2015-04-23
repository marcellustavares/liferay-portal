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

package com.liferay.dynamic.data.lists.web.action;

import com.liferay.dynamic.data.lists.web.constants.FormsPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.ActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureServiceUtil;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
@Component(
	immediate = true,
	property = {
		"action.command.name=deleteForm",
		"javax.portlet.name=" + FormsPortletKeys.FORMS
	},
	service = ActionCommand.class
)
public class DeleteRecordActionCommand extends TransactionActionCommand {

	@Override
	protected void doTransactionCommand(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		long recordSetId = ParamUtil.getLong(portletRequest, "recordSetId");

		DDLRecordSet ddlRecordSet = DDLRecordSetServiceUtil.getRecordSet(
			recordSetId);

		DDLRecordSetServiceUtil.deleteRecordSet(recordSetId);

		DDMStructureServiceUtil.deleteStructure(
			ddlRecordSet.getDDMStructureId());
	}

}