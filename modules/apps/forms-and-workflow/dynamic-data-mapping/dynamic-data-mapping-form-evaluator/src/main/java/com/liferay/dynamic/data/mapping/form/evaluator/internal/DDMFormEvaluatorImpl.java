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

package com.liferay.dynamic.data.mapping.form.evaluator.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluator;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ResourceBundleLoader;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pablo Carvalho
 */
@Component(immediate = true)
public class DDMFormEvaluatorImpl implements DDMFormEvaluator {

	@Override
	public DDMFormEvaluationResult evaluate(
			DDMForm ddmForm, DDMFormValues ddmFormValues, Locale locale)
		throws DDMFormEvaluationException {

		try {
			DDMFormEvaluatorHelper ddmFormEvaluatorHelper =
				new DDMFormEvaluatorHelper(ddmForm, ddmFormValues, locale);

			ddmFormEvaluatorHelper.setDDMExpressionFactory(
				_ddmExpressionFactory);
			ddmFormEvaluatorHelper.setDDMDataProviderTracker(
				_ddmDataProviderTracker);
			ddmFormEvaluatorHelper.setDDMFormValuesJSONDeserializer(
				_ddmFormValuesJSONDeserializer);
			ddmFormEvaluatorHelper.setResourceBundleLoader(
				_resourceBundleLoader);

			return ddmFormEvaluatorHelper.evaluate();
		}
		catch (PortalException pe) {
			throw new DDMFormEvaluationException(pe);
		}
	}

	@Reference(unbind = "-")
	protected void setDDMDataProviderTracker(
		DDMDataProviderTracker ddmDataProviderTracker) {

		_ddmDataProviderTracker = ddmDataProviderTracker;
	}

	@Reference(unbind = "-")
	protected void setDDMExpressionFactory(
		DDMExpressionFactory ddmExpressionFactory) {

		_ddmExpressionFactory = ddmExpressionFactory;
	}

	@Reference(unbind = "-")
	protected void setDDMFormValuesJSONDeserializer(
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer) {

		_ddmFormValuesJSONDeserializer = ddmFormValuesJSONDeserializer;
	}

	@Reference(
		target = "(bundle.symbolic.name=com.liferay.dynamic.data.mapping.lang)",
		unbind = "-"
	)
	protected void setResourceBundleLoader(
		ResourceBundleLoader resourceBundleLoader) {

		_resourceBundleLoader = resourceBundleLoader;
	}

	private DDMDataProviderTracker _ddmDataProviderTracker;
	private DDMExpressionFactory _ddmExpressionFactory;
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;
	private ResourceBundleLoader _resourceBundleLoader;

}