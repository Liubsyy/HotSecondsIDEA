### Introduction
The HotSeconds plugin is mainly used for hot deploying code to remote servers, with a response time in seconds. It provides one-click operation throughout the entire process, saving a significant amount of time for the modify->package->deploy cycle. This plugin is divided into HotSecondsClient and HotSecondsServer.<br>
At present, only JDK 1.8 is supported. Support for higher versions will be considered in the future based on demand.

### HotSecondsServer Installation
Upload HotSeconds1.0.zip to the server side, and run 'sh install.sh'<br>
Copy hot-seconds-remote.xml to the resource directory of the code, and modify configurations like 'secret' as needed<br>
Add the JVM parameter -XXaltjvm=dcevm -javaagent:$path1/HotSecondsServer.jar=hotconf=$path2/hot-seconds-remote.xml<br>
Here, $path1 is the directory uploaded in the first step, and $path2 is the directory uploaded in the second step<br>

### HotSecondsClient Installation
1.Download and install HotSecondsClient from the plugin market<br>
2.Go to the menu Run->HotSeconds Settings->Settings to add the server to connect to and configure<br>
The 'secret' should match that of the remote server. By filling in the local and remote mapping paths, files in the local directory, including files in subfolders, can be uploaded to the remote server.<br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/install/hotseconds-setting.png)
<br><br>
3. Run->HotSeconds Start/Stop to activate the HotSeconds plugin. Right-click to hot deploy the selected files to the remote server.<br>
![](https://github.com/thanple/HotSecondsIDEA/blob/master/install/use.png)
<br>The rules for uploading directories correspond to the Path mappings configured in the second step.<br><br>

### Plugin Shortcuts
In Keymap->Plugins->HotSecondsClient, you can customize shortcuts<br>
For instance, you can set shortcuts for HotSeconds Start/Stop and Hot swap this file.
