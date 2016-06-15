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

package com.liferay.dynamic.data.lists.form.web.portlet.action;

import com.liferay.dynamic.data.lists.form.web.constants.DDLFormPortletKeys;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.service.DDLRecordSetService;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluator;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DDLFormPortletKeys.DYNAMIC_DATA_LISTS_FORM,
		"mvc.command.name=evaluateRecordSet"
	},
	service = MVCResourceCommand.class
)
public class EvaluateRecordSetMVCResourceCommand
	extends BaseMVCResourceCommand {

	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		long recordSetId = ParamUtil.getLong(resourceRequest, "recordSetId");

		DDLRecordSet recordSet = _ddlRecordSetService.getRecordSet(recordSetId);

		DDMStructure ddmStructure = recordSet.getDDMStructure();

		DDMForm ddmForm = ddmStructure.getDDMForm();

		String serializedDDMFormValues = ParamUtil.getString(
			resourceRequest, "serializedDDMFormValues");

		DDMFormValues ddmFormValues =
			_ddmFormValuesJSONDeserializer.deserialize(
				ddmForm, serializedDDMFormValues);

		DDMFormEvaluationResult ddmFormEvaluationResult =
			_ddmFormEvaluator.evaluate(
				ddmForm, ddmFormValues, resourceRequest.getLocale());

		resourceResponse.setContentType(ContentTypes.APPLICATION_JSON);

		JSONSerializer jsonSerializer = _jsonFactory.createJSONSerializer();

		PortletResponseUtil.write(
			resourceResponse,
			jsonSerializer.serializeDeep(ddmFormEvaluationResult));

		resourceResponse.flushBuffer();
	}

	@Reference
	private DDLRecordSetService _ddlRecordSetService;

	@Reference
	private DDMFormEvaluator _ddmFormEvaluator;

	@Reference
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;

	@Reference
	private JSONFactory _jsonFactory;

}