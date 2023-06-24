## Introduction
The HotSeconds plugin is mainly used for hot deployment to remote servers after writing the codes, with a response time in a few seconds. It provides one-key operation in all time, help you saving a significant amount of time for the modify->package->deploy cycle. This plugin is divided into HotSecondsClient and HotSecondsServer.<br>
At present, only JDK8 is supported.  In the future will consider supporting java11, java17 and other higher versions according to the demand.<br><br>

#### Hot-deployment file range
support hot-deployment of all right-clicked files to the server, including java, .class files in jar, xml, html and other files, but path mapping needs to be configured for other files except java.
#### Java hot reloading range
It supports code modification, add fields, add methods, and add classes. It also supports framework context updates such as Spring, SpringMVC , MyBatis, SpringBoot etc.
<br><br>

## HotSecondsServer Installation
1.Download HotSecondsServer.zip and decompress it locally. First, check the jdk version of the server (remote and local are not necessarily the same), execute sh download.sh versionnum locally.<br>
Example: The server enters java -version and displays "1.8.0_181", then versionnum=181, and then executes sh download.sh 181, and the required files will be downloaded at this time. <br><br>
You can also manually go to [libjvm.so](https://github.com/thanple/HotSecondsIDEA/blob/master/install/download_dcevm.md) to find the corresponding version to download, and put it under ./lib in the decompression directory after downloading.

2.Upload the package in step 1 to the server, and run sh install.sh on the server <br>

**Verification is successful: Enter java -XXaltjvm=dcevm -version, if the result comes out, it means that the first 2 steps are successful, and the next step will be smooth.**


3.Copy hot-seconds-remote.xml to the resource directory of the code, and modify configurations like 'secret' , 'classloader' etc as needed<br>

&lt;secret&gt;
```
As long as this is unique, it is necessary to ensure that the secrets of the client and the server are consistent
```

&lt;classloader&gt;
```
Common project: fill in AppClassLoader
Tomcat project: fill in WebappClassLoader for Tomcat7 and below, fill in ParallelWebappClassLoader for Tomcat8 and above
SpringBoot project: LaunchedURLClassLoader
Other container projects: Take your 20 years of effort to see which ClassLoader you used to load it, just fill it in, or leave a message and I will add the document later
```

<br>
4.Add the JVM parameter -XXaltjvm=dcevm -javaagent:$path1/HotSecondsServer.jar=hotconf=$path2/hot-seconds-remote.xml<br>
Here, $path1 is the directory uploaded in the step 2, and $path2 is the directory uploaded in the step 3.<br>
If it is a java or .class file, you don’t need to add configuration, other files need to be filled.<br><br>


[HotSecondsServer.zip Download Here](https://github.com/thanple/HotSecondsIDEA/releases/tag/HotSecondsServer)

<br><br>

## HotSecondsClient Installation

**Version requirements: IDEA2020.3 and later**<br><br>

1.Install HotSecondsClient from the plugin marketplace.<br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/img/pluginmarketplace.png)
<br><br>

2.Go to the menu Run->HotSeconds Settings->Settings to add the server to connect to and configure.<br>
The 'secret' should match that of the remote server. By filling in the local and remote mapping paths, files in the local directory, including files in subfolders, can be uploaded to the remote server.<br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/img/hotseconds-setting.png)
<br><br>

3.Run->HotSeconds Start/Stop to activate the HotSeconds plugin. Right-click to hot deploy the selected files to the remote server.<br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/img/use.png)
<br>The rules for uploading directories correspond to the Path mappings configured in the second step.<br><br>

### Plugin Keymap
In Keymap->Plugins->HotSecondsClient, you can customize shortcuts<br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/img/keymap.png)
<br>For instance, you can set shortcuts for HotSeconds Start/Stop and Hot swap this file.
<br><br>

## About local hot deployment
Of course it is possible, just change the remote ip to 127.0.0.1, but it is a bit like a cannon hitting mosquitoes, so it is not recommended.<br>
If you need to do this, you can omit the step 1 and step 2 in "HotSecondsServer Installation", go to [dcevm](https://github.com/dcevm/dcevm/releases) to find the jar package of your JDK version, download it and execute sudo java -jar DCEVM-light-8u-install.jar is installed, and then perform steps 3 and 4.
<br><br>

## About extensions
Every company has its own framework, and there are many new frameworks on the market. This plugin is compatible with everything, and can expand the pre-logic and post-logic of uploading files. <br>
Copy IHotExtHandler.java in HotSecondsServer.zip to your project, implement this interface, and then configure your class name in hot-seconds-remote.xml, so that some logic that needs to refresh the cache and context can be done trigger.<br>

Times are developing, society is progressing, and this plugin is constantly being updated and improved. At the same time, I also hope that everyone can expand the logic of some open source frameworks, and actively share and contribute at the same time. I will also consider writing commonly used extension logic into the core of the plugin. 

<br>

## Future
+ Constantly update and improve the hot deployment of new frameworks and popular frameworks in the market
+ Support hot deployment of higher versions of JDK, such as Java11, Java17
+ The plug-in adds some practical functions, similar to the function of remote execution function (already implemented), welcome to give suggestions



