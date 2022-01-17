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
package com.farsunset.chat.mvc.controller.admin;

import com.farsunset.chat.constant.Common;
import com.farsunset.chat.entity.Room;
import com.farsunset.chat.mvc.annotation.PageNumber;
import com.farsunset.chat.mvc.response.ResponseEntity;
import com.farsunset.chat.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin/room")
public class RoomController {

    @Resource
    private RoomService roomService;

    @GetMapping(value = "/list")
    public String list(Room room, @PageNumber int currentPage, Model model) {
        Pageable pageable = PageRequest.of(currentPage, Common.PAGE_SIZE, Sort.by(Sort.Order.desc("id")));
        Page<Room> page = roomService.queryPage(room, pageable);
        model.addAttribute("page", page);
        model.addAttribute("room", room);
        return "dashboard/room/manage";

    }


    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Room> get(@PathVariable long id) {
        return ResponseEntity.ok(roomService.findOne(id));
    }


    @PostMapping(value = "/add")
    public @ResponseBody ResponseEntity<Void> add(Room room) {
        roomService.add(room);
        return ResponseEntity.make();
    }

    @PostMapping(value = "/update")
    public @ResponseBody ResponseEntity<Void> updateName(Room room) {
        roomService.update(room);
        return ResponseEntity.make();
    }
}
