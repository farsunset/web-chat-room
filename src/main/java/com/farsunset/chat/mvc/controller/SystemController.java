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
package com.farsunset.chat.mvc.controller;

import com.farsunset.chat.entity.Manager;
import com.farsunset.chat.mvc.response.ResponseEntity;
import com.farsunset.chat.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class SystemController {

	@Resource
	private ManagerService managerService;

	@PostMapping(value = "/login.do")
	public ResponseEntity login(String password, String account, HttpServletRequest request) {

		Manager target = managerService.queryByAccount(account);
		if (target != null && DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)).equals(target.getPassword())) {
			request.getSession().setAttribute("manager", target);
			return ResponseEntity.make();
		} else {
			return ResponseEntity.make(HttpStatus.FORBIDDEN.value());
		}
	}

	@GetMapping(value = "/logout.do")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.sendRedirect("/");
	}

	@PostMapping(value = "/password")
	public ResponseEntity password(HttpServletRequest request) {

		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		Manager manager = (Manager) request.getSession().getAttribute("manager");

		if (!manager.getPassword().equals(DigestUtils.md5DigestAsHex(oldPassword.getBytes(StandardCharsets.UTF_8)))) {
			return ResponseEntity.make(HttpStatus.FORBIDDEN.value());
		}

		manager.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8)));
		managerService.updatePassword(manager);
		return ResponseEntity.make();
	}

}
