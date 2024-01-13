package com.liubs.ff.util;

import com.liubs.ff.uitools.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FilePathUtil {

    public static List<String> findAllClassFromJar(String classNameFullPath) {

        List<String> innerClass = new ArrayList<>();
        int jarSeparatorIndex = classNameFullPath.indexOf(".jar!");
        String jarPath = classNameFullPath.substring(0, jarSeparatorIndex + 4);
        String classPath = classNameFullPath.substring(jarSeparatorIndex + 6).replace(".class", "").replace("/", ".");

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(new File(jarPath));
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String entryClassName = entry.getName().replace(".class", "").replace("/", ".");
                    if (entryClassName.startsWith(classPath) ) {
                        innerClass.add(jarPath + "!/" + entry.getName());
                    }
                }
            }
        } catch (IOException e) {
            LogUtil.error("获取jar内部类错误",e);
        }finally {
            if(jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        innerClass.sort(Comparator.comparingInt(String::length));
        return innerClass;
    }

}
