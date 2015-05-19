package com.liferay.portlet.dynamicdatamapping.registry;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;

public abstract class BaseDDMFormFieldType implements DDMFormFieldType {

	@Override
	public DDMForm getSettingsDDMForm() throws PortalException {
		return _settingsDDMForm;
	}

	protected void setSerializedSettingsDDMForm(DDMForm settingsDDMForm) {
		_settingsDDMForm = settingsDDMForm;
	}

	private DDMForm _settingsDDMForm;

}
