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

package com.liferay.dynamic.data.mapping.expression.internal;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunctionTracker;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory.ServiceWrapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true)
public class DDMExpressionFunctionTrackerImpl
	implements DDMExpressionFunctionTracker {

	@Override
	public Set<String> getFunctionNames() {
		return _ddmExpressionFunctionServiceTrackerMap.keySet();
	}

	@Override
	public Map<String, Object> getFunctionProperties(String name) {
		ServiceWrapper<DDMExpressionFunction>
			ddmExpressionFunctionServiceWrapper =
				_ddmExpressionFunctionServiceTrackerMap.getService(name);

		if (ddmExpressionFunctionServiceWrapper == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No function registered with name " + name);
			}

			return null;
		}

		return ddmExpressionFunctionServiceWrapper.getProperties();
	}

	@Override
	public Map<String, DDMExpressionFunction> getFunctions() {
		Map<String, DDMExpressionFunction> ddmExpressionFunctionsMap =
			new HashMap<>();

		List<ServiceWrapper<DDMExpressionFunction>>
			ddmExpressionFunctionServiceWrappers = ListUtil.fromCollection(
				_ddmExpressionFunctionServiceTrackerMap.values());

		Map<String, Object> functionPropertiesMap = null;

		for (ServiceWrapper<DDMExpressionFunction>
				ddmExpressionFunctionServiceWrapper :
					ddmExpressionFunctionServiceWrappers) {

			functionPropertiesMap =
				ddmExpressionFunctionServiceWrapper.getProperties();

			if (functionPropertiesMap.containsKey(
					"ddm.form.evaluator.function.name")) {

				ddmExpressionFunctionsMap.put(
					functionPropertiesMap.get(
						"ddm.form.evaluator.function.name").toString(),
					ddmExpressionFunctionServiceWrapper.getService());
			}
		}

		return ddmExpressionFunctionsMap;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_ddmExpressionFunctionServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, DDMExpressionFunction.class,
				"ddm.form.evaluator.function.name",
				ServiceTrackerCustomizerFactory.
					<DDMExpressionFunction>serviceWrapper(bundleContext));
	}

	@Deactivate
	protected void deactivate() {
		_ddmExpressionFunctionServiceTrackerMap.close();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMExpressionFunctionTrackerImpl.class);

	private ServiceTrackerMap<String, ServiceWrapper<DDMExpressionFunction>>
		_ddmExpressionFunctionServiceTrackerMap;

}