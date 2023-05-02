package com.liubs.ff.hotdeploy;

import com.liubs.ff.uitools.LogUtil;
import com.liubs.ff.uitools.NoticeInfo;
import com.liubs.ff.util.ThreadPoolUtil;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author Liuzhipeng05
 * @Date: 2023/4/28 12:51 下午
 * Description:
 */
public class HotDeployClient {

    private HotDeployConfig hotDeployConfig;
    private volatile boolean isOpen = false;
    private String initError;

    private volatile Map<String,List<HotDeployConfig.Mapping>> modifiedFiles = new ConcurrentHashMap<>();




    public void init(String ROOTPATH){

        try{
            HotDeployConfig config = HotDeployConfig.initConfig(ROOTPATH,ROOTPATH + "/hot-seconds.xml");
            setHotDeployConfig(config);
            LogUtil.info("初始化hot-seconds.xml:"+hotDeployConfig);
        }catch (Exception e) {
            LogUtil.error("初始化hot-seconds.xml异常",e);
            setInitError("加载hot-seconds.xml异常:"+(e.getMessage() == null ? "":e.getMessage()));
            return;
        }
        isOpen = true;

    }



    public void upload(String fullPath,String outPutPath, boolean isAddClass){
        try{
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try{
                        String result = "success";
                        String substring = fullPath.substring(fullPath.lastIndexOf("/") + 1);
                        NoticeInfo.auto(now()+" 热部署文件 "+substring+",result="+result,"success".equals(result));

                    }catch (Exception e) {
                        LogUtil.error("upload err",e);
                        NoticeInfo.error("热部署文件%s失败:"+fullPath,e.getMessage());
                    }
                }
            };


            if(fullPath.endsWith(".class")) {
                ThreadPoolUtil.runClassDeploy(runnable);
            }else {
                ThreadPoolUtil.runFileDeploy(runnable);
            }

        }catch (Exception e) {
            LogUtil.error("upload err",e);
            NoticeInfo.error("热部署文件%s失败:"+fullPath,e.getMessage());
        }
    }


    public String getInitError() {
        return initError;
    }

    public void setInitError(String initError) {
        this.initError = initError;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public HotDeployConfig getHotDeployConfig() {
        return hotDeployConfig;
    }

    public void setHotDeployConfig(HotDeployConfig hotDeployConfig) {
        this.hotDeployConfig = hotDeployConfig;
    }


    private static final ThreadLocal<SimpleDateFormat> timeFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));
    public static String now(){
        return timeFormatter.get().format(new Date());
    }



    public Map<String, List<HotDeployConfig.Mapping>> getModifiedFiles() {
        return modifiedFiles;
    }

    public void setModifiedFiles(Map<String, List<HotDeployConfig.Mapping>> modifiedFiles) {
        this.modifiedFiles = modifiedFiles;
    }

    public boolean isOpen() {
        return isOpen;
    }



}
