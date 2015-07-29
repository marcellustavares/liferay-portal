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

package com.liferay.dynamic.data.mapping.expression.evaluator.internal;

import com.liferay.dynamic.data.mapping.expression.evaluator.DDMExpressionEvaluator;
import com.liferay.dynamic.data.mapping.expression.evaluator.DDMFormExpressionEvaluatorResult;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONDeserializer;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pablo Carvalho
 */
@Component(immediate = true)
@Path("/")
public class DDMExpressionEvaluatorRestResource {

	@POST
	@Produces("text/plain")
	public String evaluate(
			@FormParam("serializedDDMForm") String serializedDDMForm,
			@FormParam("serializedDDMFormValues")
				String serializedDDMFormValues,
			@FormParam("languageId") String languageId)
		throws PortalException {

		DDMForm ddmForm = _ddmFormJSONDeserializer.deserialize(
			serializedDDMForm);

		DDMFormValues ddmFormValues =
			_ddmFormValuesJSONDeserializer.deserialize(
				ddmForm, serializedDDMFormValues);

		DDMFormExpressionEvaluatorResult ddmFormExpressionEvaluatorResult =
			_ddmExpressionEvaluator.evaluate(
				ddmForm, ddmFormValues, LocaleUtil.fromLanguageId(languageId));

		JSONSerializer jsonSerializer = _jsonFactory.createJSONSerializer();

		return jsonSerializer.serializeDeep(ddmFormExpressionEvaluatorResult);
	}

	@Reference
	protected void setDDMExpressionEvaluator(
		DDMExpressionEvaluator ddmExpressionEvaluator) {

		_ddmExpressionEvaluator = ddmExpressionEvaluator;
	}

	@Reference
	protected void setDDMFormJSONDeserializer(
		DDMFormJSONDeserializer ddmFormJSONDeserializer) {

		_ddmFormJSONDeserializer = ddmFormJSONDeserializer;
	}

	@Reference
	protected void setDDMFormValuesJSONDeserializer(
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer) {

		_ddmFormValuesJSONDeserializer = ddmFormValuesJSONDeserializer;
	}

	@Reference
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	private DDMExpressionEvaluator _ddmExpressionEvaluator;
	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;
	private JSONFactory _jsonFactory;

}