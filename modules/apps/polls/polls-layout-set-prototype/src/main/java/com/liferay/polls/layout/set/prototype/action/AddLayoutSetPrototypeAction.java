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

package com.liferay.polls.layout.set.prototype.action;

import com.liferay.layout.set.prototype.web.constants.LayoutSetPrototypePortletKeys;
import com.liferay.polls.constants.PollsPortletKeys;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.util.DefaultLayoutPrototypesUtil;
import com.liferay.portal.util.DefaultLayoutSetPrototypesUtil;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 *
 * @author Lino Alves
 *
 */

@Component(immediate = true, service = AddLayoutSetPrototypeAction.class)
public class AddLayoutSetPrototypeAction {

	@Activate
	protected void activate() throws Exception {
		List<Company> companies = _companyLocalService.getCompanies();

		for (Company company : companies) {
			long defaultUserId = _userLocalService.getDefaultUserId(
				company.getCompanyId());

			List<LayoutSetPrototype> layoutSetPrototypes =
				_layoutSetPrototypeLocalService.search(
					company.getCompanyId(), null, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

			addPublicSite(
				company.getCompanyId(), defaultUserId, layoutSetPrototypes);
		}
	}

	protected void addPublicSite(
			long companyId, long defaultUserId,
			List<LayoutSetPrototype> layoutSetPrototypes)
		throws Exception {

		String nameKey = "layout-set-prototype-community-site-title";
		String descriptionKey =
			"layout-set-prototype-community-site-description";

		LayoutSet layoutSet =
			DefaultLayoutSetPrototypesUtil.addLayoutSetPrototype(
				companyId, defaultUserId, nameKey, descriptionKey,
				layoutSetPrototypes,
				AddLayoutSetPrototypeAction.class.getClassLoader());

		if (Validator.isNull(layoutSet)) {
			ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
					"content.Language", LocaleUtil.getDefault(),
					AddLayoutSetPrototypeAction.class.getClassLoader());

			String nameLayout = LanguageUtil.get(resourceBundle, nameKey);

			String descriptionLayout = LanguageUtil.get(
				resourceBundle, descriptionKey);

			for (LayoutSetPrototype layoutSetPrototype : layoutSetPrototypes) {
				String curName = layoutSetPrototype.getName(
					LocaleUtil.getDefault());
				String curDescription = layoutSetPrototype.getDescription(
					LocaleUtil.getDefault());

				if (nameLayout.equals(curName) &&
					descriptionLayout.equals(curDescription)) {

					layoutSet = layoutSetPrototype.getLayoutSet();

					break;
				}
			}
		}

		// Home layout

		Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(
					layoutSet.getGroupId(), true, "/home");

		if (Validator.isNull(layout)) {
			layout = DefaultLayoutPrototypesUtil.addLayout(
				layoutSet, "home", "/home", "2_columns_iii");
		}

		String layoutTypeSettings = layout.getTypeSettings();

		if (Validator.isNull(layoutTypeSettings) ||
			!layoutTypeSettings.contains(PollsPortletKeys.POLLS_DISPLAY)) {

			DefaultLayoutPrototypesUtil.addPortletId(
				layout, PollsPortletKeys.POLLS_DISPLAY, "column-1");
		}
	}

	protected void doRun(long companyId) throws Exception {
		long defaultUserId = _userLocalService.getDefaultUserId(companyId);

		List<LayoutSetPrototype> layoutSetPrototypes =
			_layoutSetPrototypeLocalService.search(
				companyId, null, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		addPublicSite(companyId, defaultUserId, layoutSetPrototypes);
	}

	@Reference(unbind = "-")
	protected void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	@Reference(unbind = "-")
	protected void setLayoutSetPrototypeLocalService(
		LayoutSetPrototypeLocalService layoutSetPrototypeLocalService) {

		_layoutSetPrototypeLocalService = layoutSetPrototypeLocalService;
	}

	@Reference(
		target = "(javax.portlet.name=" + LayoutSetPrototypePortletKeys.LAYOUT_SET_PROTOTYPE + ")",
		unbind = "-"
	)
	protected void setLayoutSetPrototypePortlet(Portlet portlet) {
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private CompanyLocalService _companyLocalService;
	private LayoutSetPrototypeLocalService _layoutSetPrototypeLocalService;
	private UserLocalService _userLocalService;

}