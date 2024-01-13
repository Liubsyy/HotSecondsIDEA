package com.liubs.ff.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.liubs.ff.hotdeploy.HotDeployBean;
import com.liubs.ff.hotdeploy.HotDeployClient;
import com.liubs.ff.uitools.LogUtil;
import com.liubs.ff.uitools.NoticeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HotDeployPopMenuJar extends AnAction {

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


        filePath = filePath.substring(0,filePath.indexOf("jar!"))+"jar";
        LogUtil.info("热部署选中文件："+filePath);

        //热部署jar
        hotDeployClient.upload(filePath,null,false);

    }



    @Override
    public void update(@NotNull AnActionEvent e) {
        boolean display = false;
        Project project = e.getProject();
        HotDeployClient hotDeployClient = null;
        if (project != null) {
            HotDeployBean hotDeployBean = HotDeployBean.getInstance(project);
            if(null != hotDeployBean) {
                hotDeployClient = hotDeployBean.getHotDeployClient();
                if(null != hotDeployClient &&  hotDeployClient.isOpen()){
                    VirtualFile selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
                    if(null != selectedFile) {
                        String filePath = selectedFile.getPath();

                        if(filePath.contains("jar!")) {
                            display = true;
                        }
                    }
                }
            }
        }

        // 根据SwitchStateToolBar的状态控制HotDeployPopMenu的可见性
        e.getPresentation().setVisible(display);
        if(display) {
            e.getPresentation().setText("HotDeploy to remote "+hotDeployClient.getHotDeployConfig().getRemoteIp());
        }
    }
}
