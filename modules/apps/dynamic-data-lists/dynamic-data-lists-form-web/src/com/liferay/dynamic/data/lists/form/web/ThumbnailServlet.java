package com.liferay.dynamic.data.lists.form.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.dynamic.data.lists.form.web.configuration.DDLFormWebConfigurationUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;

@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/ddm-form-thumbnail",
		"osgi.http.whiteboard.servlet.name=Dynamic Data Mapping Form Thumbnail Servlet",
		"osgi.http.whiteboard.servlet.pattern=/ddm-form-thumbnail/*"
	},
	service = Servlet.class
)
public class ThumbnailServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		long recordSetId = ParamUtil.getLong(req, "recordSetId");
		
		String thumbPath = 
			DDLFormWebConfigurationUtil.get("thumb.path") + recordSetId + ".png";
	
		byte[] bytes = FileUtil.getBytes(new File(thumbPath));

		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType(ContentTypes.IMAGE_PNG);
		
		ServletResponseUtil.write(resp, bytes);
	}

}
