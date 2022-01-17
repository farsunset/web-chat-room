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

import com.farsunset.cim.sdk.server.model.Message;
import com.farsunset.chat.cim.pusher.GroupMessagePusher;
import com.farsunset.chat.constant.MessageAction;
import com.farsunset.chat.mvc.response.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Resource
    private GroupMessagePusher groupMessagePusher;

    @PostMapping(value = "/send")
    public @ResponseBody ResponseEntity<Long> send(@RequestParam long roomId,
                                                   @RequestParam String name,
                                                   @RequestParam String uid,
                                                   @RequestParam String icon,
                                                   @RequestParam String format,
                                                   @RequestParam String content) {

        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setAction(MessageAction.ACTION_CHAT);
        message.setSender(uid);
        message.setContent(content);
        message.setReceiver(String.valueOf(roomId));
        message.setFormat(format);
        message.setExtra(name);
        message.setTitle(String.valueOf(icon));
        message.setTimestamp(System.currentTimeMillis());

        groupMessagePusher.push(message);

        return ResponseEntity.ok(message.getTimestamp());
    }

}
