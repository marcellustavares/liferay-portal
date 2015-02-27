package com.liferay.forms.web.portlet.action;

import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.ActionCommand;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatamapping.StructureDefinitionException;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormLayoutJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;

public class EditFormActionCommand implements ActionCommand {

	public boolean processCommand(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws PortletException {
		
		long userId = PortalUtil.getUserId(portletRequest);

		long groupId = ParamUtil.getLong(portletRequest, "groupId");
		String structureKey = ParamUtil.getString(
			portletRequest, "structureKey");
		Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(
			portletRequest, "name");
		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(portletRequest, "description");
		String storageType = ParamUtil.getString(portletRequest, "storageType");

		try {
			DDMForm ddmForm = getDDMForm(portletRequest);
			DDMFormLayout ddmFormLayout = getDDMLayout(portletRequest);
			
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				DDMStructure.class.getName(), portletRequest);

			DDMStructureLocalServiceUtil.addStructure(
				userId, groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
				PortalUtil.getClassNameId(DDLRecordSet.class), structureKey,
				nameMap, descriptionMap, ddmForm, ddmFormLayout, storageType,
				DDMStructureConstants.TYPE_DEFAULT, serviceContext);
		}
		catch (PortalException e) {
			e.printStackTrace();
		}

		return false;
	}

	protected DDMForm getDDMForm(PortletRequest portletRequest)
		throws PortalException {

		try {
			String definition = ParamUtil.getString(
				portletRequest, "definition");

			return DDMFormJSONDeserializerUtil.deserialize(definition);
		}
		catch (PortalException pe) {
			throw new StructureDefinitionException(pe);
		}
	}

	protected DDMFormLayout getDDMLayout(PortletRequest portletRequest)
		throws PortalException {

		String layout = ParamUtil.getString(portletRequest, "layout");

		return DDMFormLayoutJSONDeserializerUtil.deserialize(layout);
	}

}
