/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
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
package com.farsunset.chat.mvc.controller.api;

import com.farsunset.chat.constant.FileBucket;
import com.farsunset.chat.service.FileStoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/file")
public class FileStoreController  extends ResourceHttpRequestHandler {

	@Resource
	private FileStoreService fileStoreService;

	@PostMapping(value = "/icon")
	public void uploadIcon(MultipartFile file, HttpServletResponse response, @RequestParam String uid) throws IOException {
		if (StringUtils.isEmpty(uid)){
			response.getWriter().print("<script>parent.onLogoUploadFinished(" + HttpStatus.BAD_REQUEST.value() + ");</script>");
			return;
		}
		fileStoreService.upload(file, FileBucket.USER_ICON, uid);
		response.getWriter().print("<script>parent.onLogoUploadFinished(" + HttpStatus.OK.value() + ");</script>");
	}

	@PostMapping(value = "/image")
	public void uploadImage(MultipartFile file, HttpServletResponse response, @RequestParam String key) throws IOException {
		if (StringUtils.isEmpty(key)){
			response.getWriter().print("<script>parent.onImageUploadFinished(" + HttpStatus.BAD_REQUEST.value() + ",0,0);</script>");
			return;
		}
		int[] size = fileStoreService.upload(file, FileBucket.CHAT_SPACE, key);
		response.getWriter().print("<script>parent.onImageUploadFinished(" + HttpStatus.OK.value() + ","+size[0] +"," + size[1] +");</script>");
	}

	/**
	 * 文件下载，支持断点续传
	 */

	@GetMapping(value = "/{bucket}/{key:.+}")
	public void download(@PathVariable String bucket, @PathVariable String key,String name, HttpServletRequest request,
						 HttpServletResponse response) throws IOException, ServletException {

		org.springframework.core.io.Resource resource = fileStoreService.get(bucket, key);

		if (resource == null) {
			response.sendError(HttpStatus.NOT_FOUND.value());
			return;
		}

		/*
		 * 来自OSS的文件地址，重定向到第三方文件地址
		 */
		if (resource instanceof UrlResource) {
			response.sendRedirect(resource.getURL().toString());
			return;
		}

		/*
		 * 来自本机文件，则使用spring mvc 自带的文件下载组件处理
		 */
		if(StringUtils.isNotEmpty(name)) {
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + URLEncoder.encode(name,"UTF-8"));
			response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
		}
		request.setAttribute("resource", resource);
		handleRequest(request, response);
	}

	@Override
	protected org.springframework.core.io.Resource getResource(HttpServletRequest request) {
		return (org.springframework.core.io.Resource) request.getAttribute("resource");
	}

}
