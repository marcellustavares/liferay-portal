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

package com.liferay.dynamic.data.mapping.expression.evaluator;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pablo Carvalho
 */
@Component(immediate = true)
@Path("/")
public class DDMExpressionEvaluatorRestResource {

	@POST
	@Produces("text/plain")
	public String evaluate(
		@FormParam("serializedDDMFormValues") String serializedDDMFormValues,
		@FormParam("serializedDDMForm") String serializedDDMForm,
		@FormParam("languageId") String languageId) throws PortalException {

		DDMExpressionEvaluatorService ddmExpressionEvaluatorService =
			getDDMExpressionEvaluatorService();

		return ddmExpressionEvaluatorService.evaluate(
			serializedDDMFormValues, serializedDDMForm, languageId);
	}

	private DDMExpressionEvaluatorService getDDMExpressionEvaluatorService() {
		Registry registry = RegistryUtil.getRegistry();

		return registry.getService(DDMExpressionEvaluatorService.class);
	}

}