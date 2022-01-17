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

import com.farsunset.chat.entity.Room;
import com.farsunset.chat.repository.RoomRepository;
import com.farsunset.chat.service.RoomService;
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
import java.util.Objects;

@Service
public class RoomServiceImpl implements RoomService {

	@Resource
	private RoomRepository roomRepository;

	@Override
	public Page<Room> queryPage(Room condition, Pageable page) {
		Specification<Room> specification = (root, criteriaQuery, builder) -> {
			List<Predicate> predicatesList = new ArrayList<>();
			if (StringUtils.isNotBlank(condition.getName())) {
				predicatesList.add(builder.like(root.get("name").as(String.class), "%"+condition.getName()+"%"));
			}
			criteriaQuery.orderBy(builder.desc(root.get("createTime").as(Date.class)));
			criteriaQuery.where(predicatesList.toArray(new Predicate[0]));
			return criteriaQuery.getRestriction();
		};
		return roomRepository.findAll(specification,page);
	}

	@Override
	public void add(Room room) {
		room.setCreateTime(new Date());
		roomRepository.save(room);
	}

	@Override
	public void update(Room room) {

		Room target = roomRepository.findById(room.getId()).orElse(null);

		if (target == null){
			return;
		}

		target.setName(room.getName());

		target.setPassword(room.getPassword());

		roomRepository.saveAndFlush(target);
	}

	@Override
	public boolean checkPassword(long id, String password) {
		return Objects.equals(password,roomRepository.findPassword(id));
	}

	@Override
	public Room findOne(long id) {
		return roomRepository.findById(id).orElse(null);
	}
}
