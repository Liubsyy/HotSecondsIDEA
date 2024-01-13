package com.liubs.ff.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.liubs.ff.hotdeploy.HotDeployClient;
import com.liubs.ff.hotdeploy.HotDeployBean;
import com.liubs.ff.uitools.NoticeInfo;

public class MyHotDeployAction extends ToggleAction {

    @Override
    public boolean isSelected(AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            HotDeployBean hotDeployBean = HotDeployBean.getInstance(project);
            HotDeployClient hotDeployClient = hotDeployBean.getHotDeployClient();
            // 获取并返回当前状态
            return null != hotDeployClient && hotDeployClient.isOpen();
        }

        return false;
    }

    @Override
    public void setSelected(AnActionEvent e, boolean state) {
        Project project = e.getProject();
        if (project == null) {
            NoticeInfo.warning("当前工程为空，不可选中");
            return;
        }
        HotDeployBean hotDeployBean = HotDeployBean.getInstance(project);
        HotDeployClient hotDeployClient = hotDeployBean.getHotDeployClient();
        if(null == hotDeployClient || !hotDeployClient.isOpen()) {
            NoticeInfo.info("一键远程热部署: 开");
            hotDeployClient = new HotDeployClient();
            hotDeployBean.setHotDeployClient(hotDeployClient);

            hotDeployClient.init(e.getProject().getBasePath());
            if(null != hotDeployClient.getInitError() && !hotDeployClient.getInitError().isEmpty()) {
                NoticeInfo.error(hotDeployClient.getInitError());
            }

        }else {
            hotDeployClient.setOpen(false);
            NoticeInfo.error("一键远程热部署: 关");
            hotDeployBean.setHotDeployClient(null);
        }

    }

}
