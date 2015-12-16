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

package com.liferay.dynamic.data.lists.form.web.util;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;

/**
 * @author Bruno Basto
 */
@DDMForm(localization = "content/Language")
public interface DDLRecordSetSettingsForm {

	@DDMFormField(label = "%from-address")
	public String emailFromAddress();

	@DDMFormField(label = "%from-name")
	public String emailFromName();

	@DDMFormField(label = "%subject")
	public String emailSubject();

	@DDMFormField(label = "%to-address")
	public String emailToAddress();

	@DDMFormField
	public String redirectURL();

	@DDMFormField(
		label = "%require-captcha", properties = {"showAsSwitcher=true"},
		type = "checkbox"
	)
	public boolean requireCaptcha();

	@DDMFormField(
		label = "%send-email-notification",
		properties = {"showAsSwitcher=true"}, type = "checkbox"
	)
	public boolean sendEmailNotification();

	@DDMFormField(
		label = "%workflow", properties = {"dataSourceType=manual"},
		type = "select"
	)
	public String workflowDefinition();

}