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

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.farsunset.chat.config.properties.FileS3Properties;
import com.farsunset.chat.service.FileStoreService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * 文件存储在阿里云OSS的策略下 文件管理实现
 */
public class S3FileServiceImpl implements FileStoreService {

    private final AmazonS3 s3Client;
    private final FileS3Properties s3Properties;

    public S3FileServiceImpl(FileS3Properties s3Properties) {

        this.s3Properties = s3Properties;

        BasicAWSCredentials credentials = new BasicAWSCredentials(s3Properties.getAccessKeyId(), s3Properties.getSecretKeyId());
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(s3Properties.getRegion())
                .build();
    }

    @Override
    public int[] upload(MultipartFile file, String dir, String key) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            s3Client.putObject(new PutObjectRequest(dir,key,file.getInputStream(),metadata));

            BufferedImage sourceImg = ImageIO.read(file.getInputStream());
            return new int[]{sourceImg.getWidth(),sourceImg.getHeight()};
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new int[]{0,0};
    }

    @Override
    public void upload(File file, String dir, String key) {
        s3Client.putObject(dir,key,file);
    }

    @Override
    public Resource get(String dir, String key) {

        Date expiration = new Date(System.currentTimeMillis() + s3Properties.getUrlDuration());

        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(dir, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(urlRequest);

        return new UrlResource(url);
    }

    @Override
    public void delete(String dir, String key) {

        if (!s3Client.doesObjectExist(dir,key)){
            return;
        }

        s3Client.deleteObject(dir,key);
    }

}
