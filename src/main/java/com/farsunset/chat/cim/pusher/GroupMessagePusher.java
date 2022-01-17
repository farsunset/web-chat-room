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
package com.farsunset.chat.cim.pusher;

import com.farsunset.cim.sdk.server.constant.ChannelAttr;
import com.farsunset.cim.sdk.server.group.TagSessionGroup;
import com.farsunset.cim.sdk.server.model.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 推送群消息
 */
@Component
public class GroupMessagePusher {

	@Resource
	private TagSessionGroup tagSessionGroup;

	public void push(final Message message) {
		String roomId  = message.getReceiver();
		tagSessionGroup.write(roomId,message , channel -> !Objects.equals(message.getSender(),channel.attr(ChannelAttr.UID).get()));
	}

	public void push(final Message message,String uid) {
		String roomId  = message.getReceiver();
		tagSessionGroup.write(roomId,message , channel -> Objects.equals(uid,channel.attr(ChannelAttr.UID).get()));
	}

}
