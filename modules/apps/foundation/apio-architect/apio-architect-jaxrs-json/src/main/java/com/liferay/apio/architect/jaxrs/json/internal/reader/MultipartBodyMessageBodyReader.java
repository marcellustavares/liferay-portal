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

package com.liferay.apio.architect.jaxrs.json.internal.reader;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

import static org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent;

import com.liferay.apio.architect.file.BinaryFile;
import com.liferay.apio.architect.form.Body;

import java.io.IOException;
import java.io.InputStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import org.osgi.service.component.annotations.Component;

/**
 * Reads {@code "multipart/form-data"} as a {@link Body}.
 *
 * @author Alejandro Hernández
 */
@Component(
	immediate = true,
	property = "liferay.apio.architect.message.body.reader=true"
)
@Consumes(MULTIPART_FORM_DATA)
@Provider
public class MultipartBodyMessageBodyReader implements MessageBodyReader<Body> {

	@Override
	public boolean isReadable(
		Class<?> clazz, Type genericType, Annotation[] annotations,
		MediaType mediaType) {

		return true;
	}

	@Override
	public Body readFrom(
			Class<Body> clazz, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream)
		throws IOException {

		if (!isMultipartContent(_httpServletRequest)) {
			throw new BadRequestException(
				"Request body is not a valid multipart form");
		}

		FileItemFactory fileItemFactory = new DiskFileItemFactory();

		ServletFileUpload servletFileUpload = new ServletFileUpload(
			fileItemFactory);

		try {
			List<FileItem> fileItems = servletFileUpload.parseRequest(
				_httpServletRequest);

			Iterator<FileItem> iterator = fileItems.iterator();

			Map<String, String> values = new HashMap<>();
			Map<String, BinaryFile> binaryFiles = new HashMap<>();

			while (iterator.hasNext()) {
				FileItem fileItem = iterator.next();

				String name = fileItem.getFieldName();

				if (fileItem.isFormField()) {
					InputStream stream = fileItem.getInputStream();

					values.put(name, Streams.asString(stream));
				}
				else {
					BinaryFile binaryFile = new BinaryFile(
						fileItem.getInputStream(), fileItem.getSize(),
						fileItem.getContentType());

					binaryFiles.put(name, binaryFile);
				}
			}

			return Body.create(
				key -> Optional.ofNullable(values.get(key)),
				key -> Optional.ofNullable(binaryFiles.get(key)));
		}
		catch (FileUploadException fue) {
			throw new BadRequestException(
				"Request body is not a valid multipart form", fue);
		}
	}

	@Context
	private HttpServletRequest _httpServletRequest;

}