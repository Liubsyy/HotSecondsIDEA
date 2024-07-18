## HotSeconds

[![License](https://img.shields.io/github/license/Liubsyy/HotSecondsIDEA)](./LICENSE)
[![GitHub](https://img.shields.io/github/stars/Liubsyy/HotSecondsIDEA.svg?style=flat&label=Stars)](https://github.com/Liubsyy/HotSecondsIDEA)
[![](https://img.shields.io/jetbrains/plugin/d/21635)](https://plugins.jetbrains.com/plugin/21635-hotsecondsclient)
[![JDK-8](https://img.shields.io/badge/JDK-8-red.svg)]()
[![JDK-11](https://img.shields.io/badge/JDK-11-blue.svg)]()
[![JDK-17](https://img.shields.io/badge/JDK-17-green.svg)]()
[![JDK-21](https://img.shields.io/badge/JDK-21-blue.svg)]()
[![QQ](https://img.shields.io/badge/QQ-173093336-blue.svg)]()


HotSeconds是一款Java远程热部署的插件(当然也支持本地热部署)，分为HotSecondsClient(IDEA热部署插件)和HotSecondsServer(服务端agent)，可热更新java和常用配置文件，也支持常用框架(Spring,MyBatis)的热更新，相对传统打包->部署->启动流程可节省大量时间提高开发效率。<br>
目前支持Java8，Java11，Java17和Java21。

- [English Document](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/install/document.md)

### 插件特性
- 热更新Java类，包括修改代码块，新增字段，新增方法，新增类，以及动态代理类
- 支持框架的热更新，比如Spring, SpringMVC，SpringBoot，MyBatis等
- 支持一些资源文件的上传和热部署
- 支持热部署jar包中的.class和热部署整个jar包
- 支持本地热部署和远程热部署，可本地编译也可远程编译
- 支持获取远程字段值，远程执行方法
- 支持自定义扩展，比如热更新某个文件的刷新逻辑


### 文档链接
- [所有功能介绍](https://github.com/Liubsyy/HotSecondsIDEA/wiki/HotSeconds%E6%89%80%E6%9C%89%E5%8A%9F%E8%83%BD%E4%BB%8B%E7%BB%8D)
- [安装文档](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/install/%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.md)
- [安装文档(萌新版)](https://juejin.cn/post/7325375988853358607)
- [常见问题和解决方案](https://github.com/Liubsyy/HotSecondsIDEA/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98%E5%92%8C%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88)

### 下载链接
 
 - 服务端插件 [HotSecondsServer](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/install/download_server.md)
 - 客户端插件 [HotSecondsClient](https://plugins.jetbrains.com/plugin/21635-hotsecondsclient)
 - 代理服务器 [HotSecondsProxy](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/install/proxyserver.md)

### 插件扩展
本插件是可以支持自定义扩展的，比如热更新某个文件之后执行自定义的逻辑
- [插件扩展步骤](https://github.com/Liubsyy/HotSecondsIDEA/wiki/HotSeconds%E6%89%80%E6%9C%89%E5%8A%9F%E8%83%BD%E4%BB%8B%E7%BB%8D#27-%E6%8F%92%E4%BB%B6%E6%89%A9%E5%B1%95)
- [HotSecondsExtension项目](https://github.com/Liubsyy/HotSecondsExtension)


 ### 使用演示
 按文档安装完之后连接目标端口，右键选择Hot swap this file to remote即可实现单文件热更新，如果是java文件会先编译再热更新。如果需要远程编译可选择Remote compilation and hot swap
 
 <img src="https://github.com/Liubsyy/HotSecondsIDEA/blob/master/img/wiki/hotswapfile.png" width="500" height="306" />
 

批量热更新：可以将修改的文件批量热部署到远程

![](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/img/gif/batchhot.gif)

热更新MyBatis

![](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/img/gif/mybatis1.gif)


<br>

### 问题反馈和交流
可以先看[常见问题和解决方案](https://github.com/Liubsyy/HotSecondsIDEA/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98%E5%92%8C%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88)，如果还有问题直接建Issues即可，同时也欢迎加QQ群 **173093336** 交流。<br>

**如果本插件对你有帮助，请点击 ⭐Star 支持一下吧，后续为大家带来更多的黑科技**


[![Star History Chart](https://api.star-history.com/svg?repos=Liubsyy/HotSecondsIDEA&type=Date)](https://star-history.com/#Liubsyy/HotSecondsIDEA&Date)
