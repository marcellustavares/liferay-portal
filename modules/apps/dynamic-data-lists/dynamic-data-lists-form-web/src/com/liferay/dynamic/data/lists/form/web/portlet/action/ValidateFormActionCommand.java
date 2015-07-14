package com.liferay.dynamic.data.lists.form.web.portlet.action;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.util.PortalUtil;

public class ValidateFormActionCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		String definition = "";

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(resourceRequest);

		HttpServletResponse httpServletResponse =
			PortalUtil.getHttpServletResponse(resourceResponse);

		ServletResponseUtil.sendFile(
			httpServletRequest, httpServletResponse, null,
			definition.getBytes(), ContentTypes.TEXT_XML_UTF8);
	}

}
