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

import com.farsunset.chat.config.properties.FileLocalProperties;
import com.farsunset.chat.config.properties.FileS3Properties;
import com.farsunset.chat.service.FileStoreService;
import com.farsunset.chat.service.impl.LocalFileServiceImpl;
import com.farsunset.chat.service.impl.S3FileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class FileConfiguration {

	@Bean("fileStoreService")
	@ConditionalOnProperty(
			name = {"chat.file.local.enable"},
			havingValue = "true"
	)
	FileStoreService localFileStoreService(FileLocalProperties fileLocalProperties) throws IOException {
		return new LocalFileServiceImpl(fileLocalProperties);
	}

	@Bean("fileStoreService")
	@ConditionalOnProperty(
			name = {"chat.file.s3.enable"},
			havingValue = "true"
	)
	FileStoreService s3FileStoreService(FileS3Properties fileS3Properties) {
		return new S3FileServiceImpl(fileS3Properties);
	}

}