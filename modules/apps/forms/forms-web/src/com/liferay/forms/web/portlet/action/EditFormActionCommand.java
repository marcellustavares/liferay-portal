package com.liferay.forms.web.portlet.action;

import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.forms.web.portlet.constants.FormsPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.ActionCommand;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSetConstants;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetServiceUtil;
import com.liferay.portlet.dynamicdatamapping.StructureDefinitionException;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormLayoutJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureServiceUtil;
@Component(
	immediate = true,
	property = {
		"action.command.name=editForm",
		"javax.portlet.name=" + FormsPortletKeys.FORMS
	},
	service = ActionCommand.class
)
public class EditFormActionCommand extends TransactionActionCommand {

	protected void updateDDLRecordSet(
			PortletRequest portletRequest, long ddmStructureId)
		throws Exception {

		long groupId = ParamUtil.getLong(portletRequest, "groupId");
		long recordSetId = ParamUtil.getLong(portletRequest, "recordSetId");
		String recordSetKey = ParamUtil.getString(
			portletRequest, "recordSetKey");
		Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(
			portletRequest, "name");
		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(portletRequest, "description");
		int scope = ParamUtil.getInteger(portletRequest, "scope");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDLRecordSet.class.getName(), portletRequest);

		if (recordSetId > 0) {
			DDLRecordSetServiceUtil.updateRecordSet(
				recordSetId, ddmStructureId, nameMap, descriptionMap,
				DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT, serviceContext);
		}
		else {
			DDLRecordSetServiceUtil.addRecordSet(
				groupId, ddmStructureId, recordSetKey, nameMap, descriptionMap,
				DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT, scope,
				serviceContext);
		}
	}

	protected DDMStructure updateDDMStructure(PortletRequest portletRequest)
		throws Exception {

		long groupId = ParamUtil.getLong(portletRequest, "groupId");
		long structureId = ParamUtil.getLong(portletRequest, "structureId");
		String structureKey = ParamUtil.getString(
			portletRequest, "structureKey");
		Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(
			portletRequest, "name");
		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(portletRequest, "description");
		String storageType = ParamUtil.getString(portletRequest, "storageType");

		DDMForm ddmForm = getDDMForm(portletRequest);
		DDMFormLayout ddmFormLayout = getDDMFormLayout(portletRequest);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDMStructure.class.getName(), portletRequest);

		DDMStructure ddmStructure = null;

		if (structureId > 0) {
			ddmStructure = DDMStructureServiceUtil.updateStructure(
				structureId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
				nameMap, descriptionMap, ddmForm, ddmFormLayout,
				serviceContext);
		}
		else {
			ddmStructure = DDMStructureServiceUtil.addStructure(
				groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
				PortalUtil.getClassNameId(DDLRecordSet.class), structureKey,
				nameMap, descriptionMap, ddmForm, ddmFormLayout, storageType,
				DDMStructureConstants.TYPE_DEFAULT, serviceContext);
		}

		return ddmStructure;
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

	protected DDMFormLayout getDDMFormLayout(PortletRequest portletRequest)
		throws PortalException {

		String layout = ParamUtil.getString(portletRequest, "layout");

		return DDMFormLayoutJSONDeserializerUtil.deserialize(layout);
	}

	@Override
	protected void doTransactionCommand(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		DDMStructure ddmStructure = updateDDMStructure(portletRequest);

		updateDDLRecordSet(portletRequest, ddmStructure.getStructureId());
	}

}