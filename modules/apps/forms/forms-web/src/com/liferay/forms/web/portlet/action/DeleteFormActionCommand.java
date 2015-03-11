package com.liferay.forms.web.portlet.action;

import com.liferay.forms.web.portlet.constants.FormsPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.ActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureServiceUtil;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
@Component(
	immediate = true,
	property = {
		"action.command.name=deleteForm",
		"javax.portlet.name=" + FormsPortletKeys.FORMS
	},
	service = ActionCommand.class
)
public class DeleteFormActionCommand extends TransactionActionCommand {

	@Override
	protected void doTransactionCommand(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		long recordSetId = ParamUtil.getLong(portletRequest, "recordSetId");

		DDLRecordSet ddlRecordSet = DDLRecordSetServiceUtil.getRecordSet(
			recordSetId);

		DDLRecordSetServiceUtil.deleteRecordSet(recordSetId);

		DDMStructureServiceUtil.deleteStructure(
			ddlRecordSet.getDDMStructureId());
	}

}