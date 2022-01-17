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
package com.farsunset.chat.service.impl;

import com.farsunset.cim.sdk.server.model.Message;
import com.farsunset.chat.cim.pusher.GroupMessagePusher;
import com.farsunset.chat.entity.Member;
import com.farsunset.chat.repository.MemberRepository;
import com.farsunset.chat.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

	@Resource
	private GroupMessagePusher groupMessagePusher;

	@Resource
	private MemberRepository memberRepository;

	@Override
	public Page<Member> queryPage(Member condition, Pageable page) {
		Specification<Member> specification = (root, criteriaQuery, builder) -> {
			List<Predicate> predicatesList = new ArrayList<>();
			if (condition.getRoomId() != null) {
				predicatesList.add(builder.equal(root.get("roomId").as(String.class), condition.getRoomId()));
			}
			if (StringUtils.isNotBlank(condition.getName())) {
				predicatesList.add(builder.like(root.get("name").as(String.class), "%"+condition.getName()+"%"));
			}
			criteriaQuery.orderBy(builder.desc(root.get("createTime").as(Date.class)));
			criteriaQuery.where(predicatesList.toArray(new Predicate[0]));
			return criteriaQuery.getRestriction();
		};
		return memberRepository.findAll(specification,page);
	}

	@Override
	public List<Member> findList(long roomId) {
		return memberRepository.findList(roomId);
	}

	@Override
	public void add(Member member) {
		member.setCreateTime(new Date());
		memberRepository.delete(member.getRoomId(),member.getUid());
		memberRepository.save(member);
	}

	@Override
	public void remove(Member member) {
		memberRepository.delete(member.getRoomId(),member.getUid());
	}


	public void pushEvent(Member member,String action){
		Message message = new Message();
		message.setId(System.currentTimeMillis());
		message.setAction(action);
		message.setSender(member.getUid());
		message.setReceiver(String.valueOf(member.getRoomId()));
		message.setExtra(member.getName());
		message.setTimestamp(System.currentTimeMillis());
		groupMessagePusher.push(message);
	}
}
