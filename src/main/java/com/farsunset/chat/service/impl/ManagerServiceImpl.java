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

import com.farsunset.chat.entity.Manager;
import com.farsunset.chat.repository.ManagerRepository;
import com.farsunset.chat.service.ManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Service
public class ManagerServiceImpl implements ManagerService {
	@Resource
	private ManagerRepository managerRepository;
	@Value("${sys.manager.account}")
	private String account;
	@Value("${sys.manager.password}")
	private String password;
	@Value("${sys.manager.name}")
	private String name;

	@PostConstruct
	public void init() {
		long count = managerRepository.countByAccount(account);
		if (count == 0) {

			Manager defManger = new Manager();
			defManger.setAccount(account);
			defManger.setName(name);
			defManger.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
			managerRepository.save(defManger);
		}
	}

	@Override
	public Manager queryByAccount(String account) {
		return managerRepository.findById(account).orElse(null);
	}

	@Override
	public void updatePassword(Manager manager) {
		managerRepository.updatePassword(manager.getAccount(), manager.getPassword());
	}
}
