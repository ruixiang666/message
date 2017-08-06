# message

消息推送平台，主要作用是通过websocket协议，向移动app（ios，android，微信服务号应用或者微信小程序）

推送消息和接收客户端推送过来的消息

1.主要特点：

a、使用websocket协议

b、支持android，ios，h5客户端

c.支持ssl传输加盟

2.用maven管理，请确保开发环境已经部署配置好maven环境和jdk（建议用jdk1.7以上版本）

3.依赖 https://github.com/ruixiang666/common

需要下载common工程并在本地并进入工程目录运行mvn install

4.message 是平台核心代码，message.client是message服务的java客户端

message.webapp  是用来启动消息平台的web应用

5.需要在开发环境增加一个配置文件 ${user.home}/config/msg.cfg.properties，其中${user.home}是指用户home目录

database.driver=com.mysql.jdbc.Driver

database.url=jdbc:mysql://127.0.0.1:3306/msgdb?useUnicode=true&amp;characterEncoding=UTF-8

database.user=msg

database.password=********


ws.port=9000

ws.ssl=false

ws.sslKeyPath=/Users/freedog/Downloads/m.orgid.cn/Tomcat/m.orgid.cn.jks

ws.sslKeyPasswd=******

dev.mode=true



uploadFilePath=/Users/freedog/upload

systemInfoPath=/Users/fugui/config/sys.peroperties

6./Users/fugui/config/sys.peroperties  用来标示消息服务器在集群中的名称和地址，名称和地址在集群中不能重复

serverId=A2  #集群服务器id

serverUrl=ws://127.0.0.1:9000 #集群服务器地址

7.ws.ssl=true时，需要配置https证书，证书可以在类似腾讯云之类的平台申请一些免费的https证书，也可以购买，比较贵就是了

8.message 工程运行mvn install 成功后，可以进入message.webapp 运行mvn jetty:run 或者nohup mvn jetty:run & 启动消息服务；

9.使用中需要帮助的话可以加本人qq 726318161 咨询，加qq时请注明 消息平台开源项目技术交流

10.通讯协议
客户端和服务器之间的通讯使用http(https) +json,ws(wss)+json的方式

具体的协议参考

https://github.com/ruixiang666/message/blob/master/protocol.txt











