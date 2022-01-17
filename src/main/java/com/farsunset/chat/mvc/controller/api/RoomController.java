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

import com.farsunset.chat.constant.MessageAction;
import com.farsunset.chat.entity.Member;
import com.farsunset.chat.entity.Room;
import com.farsunset.chat.mvc.response.ResponseEntity;
import com.farsunset.chat.service.MemberService;
import com.farsunset.chat.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("apiRoomController")
@RequestMapping("/api/room")
public class RoomController {

    @Resource
    private MemberService memberService;

    @Resource
    private RoomService roomService;

    @PostMapping(value = "/join")
    public @ResponseBody ResponseEntity<Void> join(@RequestParam long roomId,
                                                   @RequestParam String name,
                                                   @RequestParam String uid,
                                                   @RequestParam String password) {
        if (!roomService.checkPassword(roomId,password)){
            return ResponseEntity.make(HttpStatus.FORBIDDEN);
        }

        Member member = new Member();
        member.setUid(uid);
        member.setName(name);
        member.setRoomId(roomId);

        memberService.pushEvent(member, MessageAction.ACTION_JOIN_ROOM);

        return ResponseEntity.make();
    }

    @PostMapping(value = "/leave")
    public @ResponseBody ResponseEntity<Void> leave(@RequestParam long roomId,
                                                    @RequestParam String name,
                                                   @RequestParam String uid) {
        Member member = new Member();
        member.setUid(uid);
        member.setName(name);
        member.setRoomId(roomId);
        memberService.remove(member);

        memberService.pushEvent(member, MessageAction.ACTION_LEAVE_ROOM);

        return ResponseEntity.make();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Room> get(@PathVariable long id) {
        return ResponseEntity.ok(roomService.findOne(id));
    }
}
