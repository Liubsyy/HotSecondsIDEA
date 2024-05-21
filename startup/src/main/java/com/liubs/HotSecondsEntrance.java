package com.liubs;

import org.hotswap.agent.HotswapAgent;
import org.hotswap.agent.logging.AgentLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.net.URL;

public class HotSecondsEntrance {

    static {
        URL url = HotSecondsEntrance.class.getProtectionDomain().getCodeSource().getLocation();
        String osName = System.getProperty("os.name").toLowerCase();
        String bootLib = null;
        if(osName.contains("mac")) {
            bootLib = "hot.dylib";
        }else if(osName.contains("linux")) {
            bootLib = "hot.so";
        }else if(osName.contains("windows")) {
            bootLib = "hot.dll";
        }
        if(null == bootLib) {
            throw new UnsupportedOperationException("不支持当前操作系统");
        }
        String dir = url.getPath();
        File currentFile = new File(url.getPath());
        if(!currentFile.isDirectory()) {
            dir = currentFile.getParent();
        }
        System.load(dir+"/"+bootLib);
    }

    public native static void start0(String args, Instrumentation inst)  throws Exception;

    public static void premain(String args, Instrumentation inst) throws Exception {
        start0(args,inst);
        AgentLogger.getHandler().setPrintStream(
                new PrintStream(new FileOutputStream("hotseconds_agent.log", false)));
        HotswapAgent.premain(args,inst);
    }


}
