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

package com.liferay.dynamic.data.mapping.data.provider.web.portlet.action;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderSettings;
import com.liferay.dynamic.data.mapping.data.provider.web.constants.DDMDataProviderPortletKeys;
import com.liferay.dynamic.data.mapping.form.values.factory.DDMFormValuesFactory;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMDataProvider;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderService;
import com.liferay.osgi.service.tracker.map.ServiceTrackerCustomizerFactory;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DDMDataProviderPortletKeys.DYNAMIC_DATA_MAPPING_DATA_PROVIDER,
		"mvc.command.name=updateDataProvider"
	},
	service = MVCActionCommand.class
)
public class UpdateDataProviderMVCActionCommand
	extends AddDataProviderMVCActionCommand {

	@Activate
	protected void activate(final BundleContext bundleContext)
		throws InvalidSyntaxException {

		ddmDataProvidersMap = ServiceTrackerMapFactory.singleValueMap(
			bundleContext, DDMDataProviderSettings.class,
			"ddm.data.provider.name",
			ServiceTrackerCustomizerFactory.<DDMDataProviderSettings>
				serviceWrapper(bundleContext));
		ddmDataProvidersMap.open();
	}

	@Deactivate
	protected void deactivate() {
		ddmDataProvidersMap.close();
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long dataProviderId = ParamUtil.getLong(
			actionRequest, "dataProviderId");

		String name = ParamUtil.getString(actionRequest, "name");

		String definition = getDataProviderDefinition(
			actionRequest, actionResponse);

		String description = ParamUtil.getString(actionRequest, "description");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDMDataProvider.class.getName(), actionRequest);

		getDDMDataProviderService().updateDataProvider(
			dataProviderId, getLocalizedMap(themeDisplay.getLocale(), name),
			getLocalizedMap(themeDisplay.getLocale(), description), definition,
			serviceContext);
	}

	@Reference(unbind = "-")
	protected void setDDMDataProviderService(
		DDMDataProviderService ddmDataProviderService) {

		super.setDDMDataProviderService(ddmDataProviderService);
	}

	@Reference
	protected void setDDMFormValuesFactory(
		DDMFormValuesFactory ddmFormValuesFactory) {

		super.setDDMFormValuesFactory(ddmFormValuesFactory);
	}

	@Reference
	protected void setDDMFormValuesJSONSerializer(
		DDMFormValuesJSONSerializer ddmFormValuesJSONSerializer) {

		super.setDDMFormValuesJSONSerializer(ddmFormValuesJSONSerializer);
	}

}