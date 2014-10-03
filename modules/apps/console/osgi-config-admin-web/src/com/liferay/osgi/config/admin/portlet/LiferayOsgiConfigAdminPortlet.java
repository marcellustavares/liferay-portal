
package com.liferay.osgi.config.admin.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.MetaTypeService;

@Component(immediate = true, service = Portlet.class, property = {
	"com.liferay.portlet.display-category=category.hidden",
	"com.liferay.portlet.instanceable=false",
	"javax.portlet.init-param.template-path=/",
	"javax.portlet.init-param.view-template=/view.jsp",
	"javax.portlet.resource-bundle=content.Language",
	"javax.portlet.security-role-ref=power-user,user",
	"com.liferay.portlet.control-panel-entry-category=configuration",
	"com.liferay.portlet.control-panel-entry-weight=11"

})
public class LiferayOsgiConfigAdminPortlet extends MVCPortlet {

	@Activate
	public void activate(BundleContext context) {
		_context = context;
	}

	@Override
	public void doView(
		RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		Bundle[] bundles = _context.getBundles();
		//TODO build a static class that will tear this MetatypeInfo
		super.doView(renderRequest, renderResponse);
	}

	@Reference
	protected void setConfigAdminService(ConfigurationAdmin configurationAdmin) {
		_configurationAdmin = configurationAdmin;
	}

	@Reference
	protected void setMetaTypeService(MetaTypeService metaTypeService) {
		_metaTypeService = metaTypeService;
	}

	private Log _log = LogFactoryUtil.getLog(LiferayOsgiConfigAdminPortlet.class);
	private ConfigurationAdmin _configurationAdmin;
	private BundleContext _context;
	private MetaTypeService _metaTypeService;

}
