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

package com.liferay.dynamic.data.lists.web.display;

import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordServiceUtil;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
@Component(immediate = true, service = {FormsRendererHelper.class})
public class FormsRendererHelper {

	public String render(
			long recordSetId, long recordId, String portletNamespace,
			Locale locale)
		throws PortalException {

		DDLRecordSet recordSet = DDLRecordSetLocalServiceUtil.getRecordSet(
			recordSetId);

		DDMStructure ddmStructure =
			DDMStructureLocalServiceUtil.getDDMStructure(
				recordSet.getDDMStructureId());

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setPortletNamespace(portletNamespace);
		ddmFormRenderingContext.setLocale(locale);

		if (recordId > 0) {
			DDLRecord record = DDLRecordServiceUtil.getRecord(recordId);

			ddmFormRenderingContext.setDDMFormValues(record.getDDMFormValues());
		}

		return _ddmFormRenderer.render(
			ddmStructure.getDDMForm(), ddmStructure.getDDMFormLayout(),
			ddmFormRenderingContext);
	}

	@Reference
	protected void setDDMFormRenderer(DDMFormRenderer ddmFormRenderer) {
		_ddmFormRenderer = ddmFormRenderer;
	}

	private static DDMFormRenderer _ddmFormRenderer;

}