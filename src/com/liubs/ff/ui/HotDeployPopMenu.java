package com.liubs.ff.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import com.liubs.ff.hotdeploy.HotDeployClient;
import com.liubs.ff.hotdeploy.HotDeployBean;
import com.liubs.ff.uitools.LogUtil;
import com.liubs.ff.uitools.NoticeInfo;
import com.liubs.ff.util.FilePathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HotDeployPopMenu extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            NoticeInfo.warning("Project为空");
            return;
        }
        HotDeployBean hotDeployBean = HotDeployBean.getInstance(project);
        HotDeployClient hotDeployClient = hotDeployBean.getHotDeployClient();

        if(null == hotDeployClient ||  !hotDeployClient.isOpen()){
            NoticeInfo.warning("请打开一键热部署开关");
            return;
        }

        VirtualFile selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE);


        if(null == selectedFile) {
            NoticeInfo.warning("选中文件为空");
            return;
        }


        String filePath = selectedFile.getPath();


        LogUtil.info("热部署选中文件："+filePath);

        hotDeployClient.upload(filePath,null,false);


    }




    @Override
    public void update(@NotNull AnActionEvent e) {
  
    }

}
