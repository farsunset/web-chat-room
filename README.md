
## 1、[CIM](https://gitee.com/farsunset/cim)

## 2、[相关产品](http://farsunset.com)

---

## 项目介绍

该项目是基于cim开发的一款web匿名聊天室，支持发送表情、图片、文字聊天，供学习使用


<div align="center">
   <img src="https://staticres.oss-cn-hangzhou.aliyuncs.com/chat-room/chat_window.png" width="45%"  />
   <img src="https://staticres.oss-cn-hangzhou.aliyuncs.com/chat-room/room_members.png" width="45%"  />
</div>

   
---


   
---
## [收费产品介绍](http://farsunset.com)

#### 和信
和信是基于CIM组件开发的一整套完整的产品,面向所有人开放注册的试用场景。具有丰富的功能，聊天、群组、好友列表、黑名单、公众号、朋友圈等功能。不依赖任何第三方服务，可以私有化部署。

#### 侣信
侣信是基于CIM组件开发的一整套完整的产品,面向中小企业和者各类团队组织内部交流使用工具。具有丰富的功能，聊天、群组、部门组织、公众号、内部朋友圈等功能。不依赖任何第三方服务，可以私有化部署。


<div align="center">
   <img src="http://staticres.oss-cn-hangzhou.aliyuncs.com/hoxin/group_video_calling.jpg" width="24%"  />
   <img src="http://staticres.oss-cn-hangzhou.aliyuncs.com/hoxin/single_chatting_light.jpg" width="24%"  />
   <img src="http://staticres.oss-cn-hangzhou.aliyuncs.com/hoxin/single_chatting_dark.jpg" width="24%"  />
   <img src="http://staticres.oss-cn-hangzhou.aliyuncs.com/hoxin/moment_timeline_light.jpg" width="24%"  />
</div>

---  

## 工作流程

服务器启动完成后

#### 1.登录控制台页面[http://127.0.0.1:8080/dashboard](http://127.0.0.1:8080/dashboard)

默认账号：system

默认密码：system

![image](https://staticres.oss-cn-hangzhou.aliyuncs.com/chat-room/login.png)



#### 2.创建聊天室

由管理员创建聊天室，给出聊天室号码和密码

![image](https://staticres.oss-cn-hangzhou.aliyuncs.com/chat-room/dashboard.png)

#### 3.Web端设置名字和头像[http://127.0.0.1:8080](http://127.0.0.1:8080)
![image](https://staticres.oss-cn-hangzhou.aliyuncs.com/chat-room/set_profile.png)

#### 4.进入聊天室[http://127.0.0.1:8080/home](http://127.0.0.1:8080/home)

房间号码和密码由管理员创建

![image](https://staticres.oss-cn-hangzhou.aliyuncs.com/chat-room/join_room.png)

#### 5.开始聊天
![image](https://staticres.oss-cn-hangzhou.aliyuncs.com/chat-room/chat_window.png)

#### 6.查看成员
![image](https://staticres.oss-cn-hangzhou.aliyuncs.com/chat-room/room_members.png)

-------------------------------------------------------------------------------------------


## [技术栈]

 springboot、netty、websocket、protobuf、bootstrap、freemaker、mysql

-------------------------------------------------------------------------------------------


## [其他]

1、消息存储

   聊天消息没有持久化，离开页面将会丢失记录

2、图片存储

   支持本地存储和S3存储服务。参见application.properties

3、数据库相关

   数据库配置参见application.properties。创建数据：chat_room_db ,服务端启动会自动创建表