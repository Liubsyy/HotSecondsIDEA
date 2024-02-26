## startup模块
核心热部署源码都在hotswap-core模块，此startup模块仅仅是作为启动器触发调用hotswap-core模块<br>


### 如何打包？
- 1.hotswap-core 模块进行 mvn install
- 2.startup模块 mvn clean install可得到HotSecondsServer.jar覆盖原安装包的jar即可