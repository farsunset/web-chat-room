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

import com.farsunset.cim.sdk.server.model.Message;
import com.farsunset.chat.cim.pusher.GroupMessagePusher;
import com.farsunset.chat.constant.Common;
import com.farsunset.chat.constant.MessageAction;
import com.farsunset.chat.entity.Member;
import com.farsunset.chat.mvc.annotation.PageNumber;
import com.farsunset.chat.mvc.response.ResponseEntity;
import com.farsunset.chat.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @Resource
    private GroupMessagePusher groupMessagePusher;

    @GetMapping(value = "/list")
    public String list(Member member, @PageNumber int currentPage, Model model) {
        Pageable pageable = PageRequest.of(currentPage, Common.PAGE_SIZE, Sort.by(Sort.Order.desc("id")));
        Page<Member> page = memberService.queryPage(member, pageable);
        model.addAttribute("page", page);
        model.addAttribute("member", member);
        return "dashboard/member/manage";
    }

    @PostMapping(value = "/out")
    public @ResponseBody ResponseEntity<Void> out(Member member) {

        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setAction(MessageAction.ACTION_OUT_ROOM);
        message.setSender(String.valueOf(Common.SYSTEM_ID));
        message.setReceiver(String.valueOf(member.getRoomId()));
        message.setContent(member.getUid());
        message.setTimestamp(System.currentTimeMillis());

        groupMessagePusher.push(message,member.getUid());

        memberService.remove(member);

        return ResponseEntity.make();
    }
}
