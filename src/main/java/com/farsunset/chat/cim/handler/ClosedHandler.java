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
package com.farsunset.chat.cim.handler;

import com.farsunset.cim.sdk.server.constant.ChannelAttr;
import com.farsunset.cim.sdk.server.group.TagSessionGroup;
import com.farsunset.cim.sdk.server.handler.CIMRequestHandler;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.chat.cim.CIMHandler;
import com.farsunset.chat.entity.Member;
import com.farsunset.chat.service.MemberService;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import javax.annotation.Resource;

/**
 * 连接断开时，更新用户相关状态
 */
@CIMHandler(key = "client_closed")
public class ClosedHandler implements CIMRequestHandler {

	@Resource
	private TagSessionGroup tagSessionGroup;

	@Resource
	private MemberService memberService;

	@Override
	public void process(Channel channel, SentBody message) {

		tagSessionGroup.remove(channel);

		String uid = channel.attr(ChannelAttr.UID).get();
		String roomId = channel.attr(ChannelAttr.TAG).get();

		Object name = channel.attr(AttributeKey.valueOf("name")).get();

		if (uid == null || roomId== null || name == null){
			return;
		}

		Member member = new Member();
		member.setName(name.toString());
		member.setRoomId(Long.parseLong(roomId));
		member.setUid(uid);

		memberService.remove(member);
	}

}
