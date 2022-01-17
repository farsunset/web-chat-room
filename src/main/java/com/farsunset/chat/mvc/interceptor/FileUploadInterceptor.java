/*
 * Copyright 2020-2021 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.chat.mvc.interceptor;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FileUploadInterceptor implements HandlerInterceptor {

	@Value("${spring.servlet.image.max-file-size}")
	private DataSize maxFileSize;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		if (ServletFileUpload.isMultipartContent(request) && request.getContentLength() > maxFileSize.toBytes()) {
			if (request.getRequestURI().contains("icon")){
				response.getWriter().print("<script>parent.onLogoUploadFinished(" + HttpStatus.PAYLOAD_TOO_LARGE.value() + ");</script>");
			}
			if (request.getRequestURI().contains("image")){
				response.getWriter().print("<script>parent.onImageUploadFinished(" + HttpStatus.PAYLOAD_TOO_LARGE.value() + ",0,0);</script>");
			}
			return false;
		}
		return true;
	}
}
