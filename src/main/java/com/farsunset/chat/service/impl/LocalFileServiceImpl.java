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

import com.farsunset.chat.config.properties.FileLocalProperties;
import com.farsunset.chat.service.FileStoreService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件存储在本地文件夹的策略下 文件管理实现
 */
public class LocalFileServiceImpl implements FileStoreService {
	private final Logger LOGGER = LoggerFactory.getLogger(LocalFileServiceImpl.class);
	private final File jarFilePath;


	public LocalFileServiceImpl(FileLocalProperties properties) throws IOException {
		ApplicationHome home = new ApplicationHome(getClass());
		if (home.getSource() != null) {
			jarFilePath = home.getSource().getParentFile();
		}else {
			jarFilePath = home.getDir();
		}

		File bucketDir = new File(getBaseBucketPath());

		FileUtils.forceMkdir(bucketDir);

		for (String bucket : properties.getBuckets()) {
			FileUtils.forceMkdir(new File(bucketDir, bucket));
		}

	}


	@Override
	public void delete(String bucket, String key) {
		if (StringUtils.isBlank(bucket)
				|| StringUtils.isBlank(key)) {
			return;
		}

		File targetFile = new File(getBaseBucketPath() + "/" + bucket, key);

		FileUtils.deleteQuietly(targetFile);

	}


	@Override
	public int[] upload(MultipartFile file, String bucket, String key) {

		String path = getBaseBucketPath() + "/" + bucket;
		File desFile = new File(path, key);
		FileUtils.deleteQuietly(desFile);
		try {
			file.transferTo(desFile);
			BufferedImage sourceImg = ImageIO.read(new FileInputStream(desFile));
			return new int[]{sourceImg.getWidth(),sourceImg.getHeight()};
		} catch (Exception e) {
			LOGGER.error("upload local file error",e);
		}

		return new int[]{0,0};
	}

	@Override
	public void upload(File file, String bucket, String key) {
		String path = getBaseBucketPath() + "/" + bucket;
		File desFile = new File(path, key);
		try {
			FileUtils.copyFile(file,desFile);
		} catch (Exception e) {
			LOGGER.error("upload local file error",e);
		}
	}


	@Override
	public Resource get(String bucket, String key) {
		File destFile = new File(getBucket(bucket), key);
		return destFile.exists() ? new FileSystemResource(destFile) : null;
	}

	public String getBaseBucketPath() {
		return new File(jarFilePath, "bucket").getAbsolutePath();
	}


	private File getBucket(String bucket) {
		return  new File(getBaseBucketPath(),bucket);
	}

}
