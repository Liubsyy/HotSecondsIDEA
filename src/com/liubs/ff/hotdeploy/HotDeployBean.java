package com.liubs.ff.hotdeploy;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class HotDeployBean {
    private HotDeployClient hotDeployClient;

    public HotDeployBean() {
    }
    public static HotDeployBean getInstance(@NotNull Project project) {
        return project.getService(HotDeployBean.class);
    }

    public HotDeployClient getHotDeployClient() {
        return hotDeployClient;
    }

    public void setHotDeployClient(HotDeployClient hotDeployClient) {
        this.hotDeployClient = hotDeployClient;
    }
}
