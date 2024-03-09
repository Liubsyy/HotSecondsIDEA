## Introduction
HotSeconds is a remote hot deployment plugin for Java, including HotSecondsClient and HotSecondsServer. In theory, it can hot reloading any file (java, xml, html, css, js, etc.), and also supports hot reloading of common frameworks (Spring, MyBatis). It can save a lot of time in packaging->deployment->startup. <br>
Currently supports Java8, Java11 and Java17.

#### Hot-deployment file range
support hot-deployment of all right-clicked files to the server, including java, .class files in jar, xml, html and other files, but path mapping needs to be configured for other files except java.
#### Java hot reloading range
It supports code modification, add fields, add methods, and add classes. It also supports framework context updates such as Spring, SpringMVC , MyBatis, SpringBoot etc.
#### Issue Feedback and Communication
If you have any questions, you can just create Issues, which is convenient for others to see and will not be swiped.



<br>

## HotSecondsServer Installation

### 1. Install the server package ###
[HotSecondsServer.zip Installation Package Download Link](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/install/download_server.md)

#### Java8 ####
【Linux】 Download HotSecondsServer.zip and decompress it locally, then go to [libjvm.so](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/install/libjvm_so.md) to find the corresponding jdk version. No need after downloading Rename it and place it directly under ./lib in the decompressed directory, then upload it to the server (note the directory as $path1), and execute sh install.sh<br>

【Windows & MacOS local hot deployment】 First go to [dcevm](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/install/dcevm_installer.md) to find your JDK version of the jar package (**The versions are consistent Important**), download and run the jar (may require administrator rights or root rights) to install, select Install DCEVM as altjvm, then unzip HotSecondsServer.zip and put it in the root directory of your project (note the directory as $path1) <br>
> Verify whether it is successful: Enter java -XXaltjvm=dcevm -version. If the result comes out, it means that the first step is successful, and everything will be smooth sailing later.

#### Java11 #####
First download [Trava-JDK11](https://github.com/TravaOpenJDK/trava-jdk-11-dcevm/releases) as your JDK startup. This JDK is modified based on OpenJDK11
<br>Then download HotSecondsServer.zip, unzip it and put it on the server (note the directory as $path1)

#### Java17 #####
First download [JBR17](https://github.com/JetBrains/JetBrainsRuntime/releases) as your JDK startup. This JDK is modified based on OpenJDK17
<br>Then download HotSecondsServer.zip, unzip it and put it on the server (note the directory as $path1)

###### If you use OracleJDK and ordinary OpenJDK, you can also start it, but hot update does not support new fields and methods, and other functions are normal ######

### 2. Configure hot-seconds-remote.xml ###
Copy hot-seconds-remote.xml to the resource directory of the code (any directory will do, remember the directory as $path2), and modify configurations such as secret and classloader as needed. <br>
The secret parameter only needs to be unique. You need to ensure that the secrets of the client and the server are consistent. The configuration of the classloader parameters is as follows:
- Common project: AppClassLoader
- Tomcat project: Fill in WebappClassLoader for Tomcat7 and below, fill in ParallelWebappClassLoader for Tomcat8 and above.
- SpringBoot project: LaunchedURLClassLoader (if it is a local main function to start, fill in AppClassLoader)
- Other container projects: You can remotely breakpoint getClass().getClassLoader() on the class that needs to be hot updated to see which classloader it is. Just fill it in. You can also leave a message and I will add the document later.


### 3. Add jvm parameters ###
Add jvm parameters to enable hot deployment agent
* Java8 adds jvm parameters: -XXaltjvm=dcevm -javaagent:$path1/HotSecondsServer.jar=hotconf=$path2/hot-seconds-remote.xml
* Java11 adds jvm parameters: --add-opens java.desktop/com.sun.beans.introspect=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED -javaagent:$path1/HotSecondsServer.jar=hotconf=$path2/hot-seconds-remote.xml
* Java17 adds jvm parameters: --add-opens java.desktop/com.sun.beans.introspect=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED -XX:+AllowEnhancedClassRedefinition -javaagent:$path1 /HotSecondsServer.jar=hotconf=$path2/hot-seconds-remote.xml

Where $path1 is the directory uploaded in step 1, $path2 is the directory uploaded in step 2
###### Friendly reminder: Only add this parameter in the test/sandbox, do not add this parameter online, otherwise the hot deployment agent will not be enabled ######

## HotSecondsClient Installation

**Version requirements: IDEA2020.3 and later**<br><br>

1.Install HotSecondsClient from the plugin marketplace.<br><br>
![](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/img/pluginmarketplace.png)
<br><br>

2.Go to the menu Run->HotSeconds Settings->Settings to add the server to connect to and configure.<br><br>
The 'secret' should match that of the remote server. By filling in the local and remote mapping paths, files in the local directory, including files in subfolders, can be uploaded to the remote server.<br>
![](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/img/hotseconds-setting.png)

<br>
If it is a java or .class file, you don’t need to add configuration, other files need to be filled.
<br>

<br><br>

3.Click the avatar next to Debug to open the HotSeconds plug-in. In the latest version of the UI, you can click Run->HotSeconds Start/Stop to open the HotSeconds plug-in, and right-click to hot-deploy the selected file to the remote server.<br><br>
![](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/img/use.png)
<br>The rule for uploading the directory is the Path mappings configured in step 2. If it is a java/class file, you don’t need to fill it in. Other files need to be filled in. <br>

**When hot-deploying java files, it will be compiled first and then hot-deployed to the remote. If the compilation fails, first build the project as a whole and then hot-deploy (this is the basic operation)**

<br><br>

## Quick Start
- After installation, click the button next to Debug to start connecting to the remote server. In the latest UI of the 2023 version, you can select the Run menu -> HotSeconds Start/Stop (the latest version of the UI recommends setting shortcut keys).

- Open the java file, right-click and select Hot swap this file to remote to hot-deploy the current file, and you can also right-click to select hot-deployment for the files in the project on the left.

- Right click on the java file, you can also select Remote compilation and Hot swap, that is, remote compilation and hot deployment.

- In Run->HotSeconds Settings, you can choose the language, add functions such as IP and other shortcut operations

<br>

### All actions of the plugin
| Action | Discribtion | Location | Keymap |
| ------ | ------ | ------ | ------ |
|HotSeconds switch | After modifying the configuration, you need to turn on the switch again | Avatar button next to Run menu/Debug| HotSeconds Start/Stop |
|Select file for hot deployment | Path mapping needs to be configured for hot deployment to the remote path, if it is a java file, no need to configure | Right click on the file/project menu to select the file | Hot swap this file |
|Remote compilation and hot deployment of files | Upload source code to remote compilation and hot deployment, the remote needs to install jdk (not jre), which solves the problem of inconsistency between local and remote versions | Right click on java files | Remote compilation and Hot swap |
|Hot deployment of the entire jar | Upload to the remote and batch hot deployment of all .classes in the jar |Open any .class in the jar package and right click | Hot deploy the JAR to remote |
|Remote Execution Method | Static no-argument method is required to execute, you can use this method to call other parameters and non-static methods |Right click on the static no-argument method |Run method on remote server |
|Automatic hot deployment | After completing the path mapping, you need to restart HotSecondsClient and the plugin will monitor the local directory file changes and automatically upload to the remote (except for .class files, if it is .class, use the Hot swap this file function) |Run->HotSeconds Settings->Settings |
|Batch hot reloading modified files | Modified files can be hot deployed in batches, including java and resources | HotSecondsClient console->Batch hot deployment modified files | Batch Hot Reloading Modified Files |


<br>

### Plugin Keymap
In Keymap->Plugins->HotSecondsClient, you can customize shortcuts<br>
![](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/img/keymap.png)
<br>For instance, you can set shortcuts for HotSeconds Start/Stop and Hot swap this file.
<br><br>


## About extensions
Every company has its own framework, and there are many new frameworks on the market. This plugin is compatible with everything, and can expand the pre-logic and post-logic of uploading files. <br>
Copy IHotExtHandler.java in HotSecondsServer.zip to your project, implement this interface, and then configure your class name in hot-seconds-remote.xml, so that some logic that needs to refresh the cache and context can be done trigger.<br>

Times are developing, society is progressing, and this plugin is constantly being updated and improved. At the same time, I also hope that everyone can expand the logic of some open source frameworks, and actively share and contribute at the same time. I will also consider writing commonly used extension logic into the core of the plugin. 

<br>

**Here is an extended example: the html of the hot-deployed velocity template should be used as an example**

First create a java file com.liubs.ext.VelocityHtmlCacheClear to implement the IHotExtHandler interface

```
public class VelocityHtmlCacheClear implements IHotExtHandler {
    @Override
    public void preHandle(ClassLoader classLoader, String path, byte[] content) {
    }

    @Override
    public void afterHandle(ClassLoader classLoader, Class<?> classz, String path, byte[] content) {
        if(!path.endsWith(".html")) {
            return;
        }

        try{
            String fileName = path.substring(path.lastIndexOf("/")+1);

            Class<?> runtimeSingleton = Class.forName("org.apache.velocity.runtime.RuntimeSingleton");
            Field riField = runtimeSingleton.getDeclaredField("ri");
            riField.setAccessible(true);
            Object ri =  riField.get(null);
            riField.setAccessible(false);
            Field resourceManagerField = ri.getClass().getDeclaredField("resourceManager");
            resourceManagerField.setAccessible(true);
            Object resourceManager = resourceManagerField.get(ri);
            resourceManagerField.setAccessible(false);
            Field globalCacheField = resourceManager.getClass().getDeclaredField("globalCache");
            globalCacheField.setAccessible(true);
            Object resourceCache = globalCacheField.get(resourceManager);
            globalCacheField.setAccessible(false);

            Method enumerateKeys = resourceCache.getClass().getDeclaredMethod("enumerateKeys");
            enumerateKeys.setAccessible(true);
            Iterator cacheIterator = (Iterator)enumerateKeys.invoke(resourceCache);
            enumerateKeys.setAccessible(false);

            while(cacheIterator.hasNext()) {
                Object cacheKey = cacheIterator.next();

                //clear cache
                if(cacheKey.toString().contains(fileName)) {   
                    cacheIterator.remove();
                }
            }
        }catch (Throwable e) {
           e.printStackTrace();
        }
    }
}
```


Then add classname to dev-ext in hot-seconds-remote.xml
```
    <dev-ext>
        <classname>com.liubs.ext.VelocityHtmlCacheClear</classname>
    </dev-ext>
```

Then redeploy the server (you can also not restart, use this plug-in to hot-deploy VelocityHtmlCacheClear twice, remember twice) <br>
Then modify the mapping path
![](https://github.com/Liubsyy/HotSecondsIDEA/blob/master/img/mapping-set.png)

After re-opening the switch, you can hot-deploy the .html/.css/.js files in the local /src/main/webapp directory to the /opt/web/xx/webapps directory. After uploading, the logic of VelocityHtmlCacheClear will be executed, then Just refresh the cache.


<br>

## Future
+ Constantly update and improve the hot deployment of new frameworks and popular frameworks in the market, make the extension pack a new project
+ Support hot deployment of higher versions of JDK
+ The plug-in adds some practical functions, similar to the function of remote execution function (already implemented), welcome to give suggestions
+ Extensions to other programming languages ​​and IDEs


