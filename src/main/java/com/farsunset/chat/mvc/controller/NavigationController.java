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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NavigationController {

	@GetMapping(value = "/")
	public ModelAndView index(ModelAndView model) {
		model.setViewName("app/index");
		return model;
	}

	@GetMapping(value = "/dashboard")
	public ModelAndView dashboard(ModelAndView model) {
		model.setViewName("dashboard/login");
		return model;
	}

	@GetMapping(value = "/admin/index")
	public ModelAndView adminIndex(ModelAndView model) {
		model.setViewName("dashboard/index");
		return model;
	}


	@GetMapping(value = "/app")
	public ModelAndView appIndex(ModelAndView model) {
		model.setViewName("app/index");
		return model;
	}

	@GetMapping(value = "/profile")
	public ModelAndView appProfile(ModelAndView model) {
		model.setViewName("app/profile");
		return model;
	}

	@GetMapping(value = "/home")
	public ModelAndView appHome(ModelAndView model) {
		model.setViewName("app/home");
		return model;
	}

	@GetMapping(value = "/room")
	public ModelAndView appRoom(ModelAndView model, @RequestParam long id) {
		model.setViewName("app/room");
		model.addObject("roomId", id);
		return model;
	}
}
