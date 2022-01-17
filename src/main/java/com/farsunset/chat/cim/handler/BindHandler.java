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
import com.farsunset.cim.sdk.server.model.ReplyBody;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.chat.cim.CIMHandler;
import com.farsunset.chat.entity.Member;
import com.farsunset.chat.service.MemberService;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;

/**
 * 客户长连接 账户绑定实现
 */
@CIMHandler(key = "client_bind")
public class BindHandler implements CIMRequestHandler {

	@Resource
	private TagSessionGroup tagsessionGroup;

	@Resource
	private MemberService memberService;

	@Override
	public void process(Channel channel, SentBody body) {

		ReplyBody reply = new ReplyBody();
		reply.setKey(body.getKey());
		reply.setCode(HttpStatus.OK.value());
		reply.setTimestamp(System.currentTimeMillis());

		String uid = body.get("uid");

		String name = body.get("name");

		long roomId = body.getLong("roomId");

		Member member = new Member();
		member.setUid(uid);
		member.setName(name);
		member.setRoomId(roomId);
		memberService.add(member);

		channel.attr(ChannelAttr.UID).set(uid);

		channel.attr(AttributeKey.valueOf("name")).set(name);

		channel.attr(ChannelAttr.TAG).set(String.valueOf(roomId));

		tagsessionGroup.add(channel);

		channel.writeAndFlush(reply);
	}
}
