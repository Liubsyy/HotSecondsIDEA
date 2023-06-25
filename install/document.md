## Introduction
The HotSeconds plugin is mainly used for hot deployment to remote servers after writing the codes, with a response time in a few seconds. It provides one-key operation in all time, help you saving a significant amount of time for the modify->package->deploy cycle. This plugin is divided into HotSecondsClient and HotSecondsServer.<br>
At present, only JDK8 is supported.  In the future will consider supporting java11, java17 and other higher versions according to the demand.<br><br>

#### Hot-deployment file range
support hot-deployment of all right-clicked files to the server, including java, .class files in jar, xml, html and other files, but path mapping needs to be configured for other files except java.
#### Java hot reloading range
It supports code modification, add fields, add methods, and add classes. It also supports framework context updates such as Spring, SpringMVC , MyBatis, SpringBoot etc.
<br><br>

## HotSecondsServer Installation
1.Download HotSecondsServer.zip and decompress it locally([HotSecondsServer.zip Download Here](https://github.com/thanple/HotSecondsIDEA/blob/master/install/download_server.md)). First, check the jdk version of the server (remote and local are not necessarily the same), execute sh download.sh versionnum locally.<br>
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
| Project type | classloader |
| ------ | ------ |
|Common project| AppClassLoader|
|Tomcat project| fill in WebappClassLoader for Tomcat7 and below, fill in ParallelWebappClassLoader for Tomcat8 and above|
|SpringBoot project| LaunchedURLClassLoader|
|Other container projects| Take your 20 years of effort to see which ClassLoader you used to load it, just fill it in, or leave a message and I will add the document later|

<br>
4.Add the JVM parameter -XXaltjvm=dcevm -javaagent:$path1/HotSecondsServer.jar=hotconf=$path2/hot-seconds-remote.xml<br>
Here, $path1 is the directory uploaded in the step 2, and $path2 is the directory uploaded in the step 3.<br><br>

***Friendly reminder: Only add this parameter in the test/sandbox, do not add this parameter online, if you do not add this parameter, the hot deployment agent will not be enabled.***
<br><br>
When all finished , restart your server.

<br><br>

## HotSecondsClient Installation

**Version requirements: IDEA2020.3 and later**<br><br>

1.Install HotSecondsClient from the plugin marketplace.<br><br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/img/pluginmarketplace.png)
<br><br>

2.Go to the menu Run->HotSeconds Settings->Settings to add the server to connect to and configure.<br><br>
The 'secret' should match that of the remote server. By filling in the local and remote mapping paths, files in the local directory, including files in subfolders, can be uploaded to the remote server.<br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/img/hotseconds-setting.png)

<br>
If it is a java or .class file, you don’t need to add configuration, other files need to be filled.
<br>

**After filling in, hot-seconds.xml will be generated locally, this is the local configuration file.**

<br><br>

3.Run->HotSeconds Start/Stop to activate the HotSeconds plugin. Right-click to hot deploy the selected files to the remote server.
<br><br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/img/use.png)
<br>The rules for uploading directories correspond to the Path mappings configured in the second step.<br><br><br>


### All actions of the plugin
| Action | Discribtion | Location | Keymap |
| ------ | ------ | ------ | ------ |
|HotSeconds switch | After modifying the configuration, you need to turn on the switch again | Avatar button next to Run menu/Debug| HotSeconds Start/Stop |
|Select file for hot deployment | Path mapping needs to be configured for hot deployment to the remote path, if it is a java file, no need to configure | Right click on the file/project menu to select the file | Hot swap this file |
|Remote compilation and hot deployment of files | Upload source code to remote compilation and hot deployment, the remote needs to install jdk (not jre), which solves the problem of inconsistency between local and remote versions | Right click on java files | Remote compilation and Hot swap |
|Hot deployment of the entire jar | Upload to the remote and batch hot deployment of all .classes in the jar |Open any .class in the jar package and right click | Hot deploy the JAR to remote |
|Remote Execution Method | Static no-argument method is required to execute, you can use this method to call other parameters and non-static methods |Right click on the static no-argument method |Run method on remote server |



<br>

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
![](https://github.com/thanple/HotSecondsIDEA/blob/master/img/mapping-set.png)

After re-opening the switch, you can hot-deploy the .html/.css/.js files in the local /src/main/webapp directory to the /opt/web/xx/webapps directory. After uploading, the logic of VelocityHtmlCacheClear will be executed, then Just refresh the cache.


<br>

## Future
+ Constantly update and improve the hot deployment of new frameworks and popular frameworks in the market, make the extension pack a new project
+ Support hot deployment of higher versions of JDK, such as Java11, Java17
+ The plug-in adds some practical functions, similar to the function of remote execution function (already implemented), welcome to give suggestions
+ Extensions to other programming languages ​​and IDEs


