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
package com.farsunset.chat.config;

import com.farsunset.cim.sdk.server.group.TagSessionGroup;
import com.farsunset.cim.sdk.server.handler.CIMNioSocketAcceptor;
import com.farsunset.cim.sdk.server.handler.CIMRequestHandler;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.farsunset.chat.cim.CIMHandler;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(includeFilters = @ComponentScan.Filter(CIMHandler.class))
public class CIMConfiguration implements CIMRequestHandler, ApplicationListener<ApplicationStartedEvent> {

	@Resource
	private ApplicationContext applicationContext;

	private final Map<String, CIMRequestHandler> handlerMap = new HashMap<>();

	@Bean(destroyMethod = "destroy")
	public CIMNioSocketAcceptor getNioSocketAcceptor(@Value("${cim.websocket.port}") int websocketPort) {

		return new CIMNioSocketAcceptor.Builder()
				.setWebsocketPort(websocketPort)
				.setOuterRequestHandler(this)
				.build();	

	}

	@Bean
	public TagSessionGroup tagSessionGroup() {
		return new TagSessionGroup();
	}

	@Override
	public void process(Channel channel, SentBody body) {

        CIMRequestHandler handler = handlerMap.get(body.getKey());
		
		if (handler != null){
			handler.process(channel, body);
		}

	}

	/**
	 * springboot启动完成之后再启动cim服务的，避免服务正在重启时，客户端会立即开始连接导致意外异常发生.
	 */
	@Override
	public void onApplicationEvent(ApplicationStartedEvent startedEvent) {

		Map<String, CIMRequestHandler> beans =  applicationContext.getBeansOfType(CIMRequestHandler.class);

		for (Map.Entry<String, CIMRequestHandler> entry : beans.entrySet()) {

			CIMRequestHandler handler = entry.getValue();

			CIMHandler annotation = handler.getClass().getAnnotation(CIMHandler.class);

			if (annotation != null){
				handlerMap.put(annotation.key(),handler);
			}
		}

		applicationContext.getBean(CIMNioSocketAcceptor.class).bind();
	}
}