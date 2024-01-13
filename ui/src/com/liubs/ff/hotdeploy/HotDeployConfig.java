package com.liubs.ff.hotdeploy;

import org.jaxen.util.SingletonList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HotDeployConfig {
    private String secret;
    private String remoteIp;
    private int remotePort = 8000;
    private int timeout;
    private boolean autoHotDeploy;
    private List<Mapping> mappings = new ArrayList<>();


    public static HotDeployConfig initConfig(String ROOTPATH,String path) throws Exception {

        File configFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(configFile);
        doc.getDocumentElement().normalize();

        HotDeployConfig config = new HotDeployConfig();

        // 解析 secret
        NodeList secretNodeList = doc.getElementsByTagName("secret");
        if (secretNodeList.getLength() > 0) {
            Node secretNode = secretNodeList.item(0);
            config.setSecret(secretNode.getTextContent());
        }

        // 解析 remote_ip
        NodeList remoteIpNodeList = doc.getElementsByTagName("remote_ip");
        if (remoteIpNodeList.getLength() > 0) {
            Node remoteIpNode = remoteIpNodeList.item(0);
            config.setRemoteIp(remoteIpNode.getTextContent());
        }

        // 解析 remote_port
        NodeList remotePortNodeList = doc.getElementsByTagName("remote_port");
        if (remotePortNodeList.getLength() > 0) {
            Node remotePortNode = remotePortNodeList.item(0);
            config.setRemotePort(Integer.parseInt(remotePortNode.getTextContent()));
        }
        NodeList timeoutNodeList = doc.getElementsByTagName("timeout");
        if (timeoutNodeList.getLength() > 0) {
            Node timeoutNode = timeoutNodeList.item(0);
            config.setTimeout(Integer.parseInt(timeoutNode.getTextContent()));
        }

        if(null == config.getRemoteIp() || config.getRemoteIp().trim().isEmpty()) {
            throw new RuntimeException("ip为空");
        }
        if(config.getRemotePort()<0) {
            throw new RuntimeException("port为空");
        }

        // 解析 local.auto_hotdeploy
        NodeList autoHotDeployNodeList = doc.getElementsByTagName("auto_hotdeploy");
        if (autoHotDeployNodeList.getLength() > 0) {
            Node autoHotDeployNode = autoHotDeployNodeList.item(0);
            config.setAutoHotDeploy(Boolean.parseBoolean(autoHotDeployNode.getTextContent()));
        }

        // 解析 local.mappings
        NodeList mappingsNodeList = doc.getElementsByTagName("mappings");
        if (mappingsNodeList.getLength() > 0) {
            Node mappingsNode = mappingsNodeList.item(0);
            NodeList mappingNodeList = mappingsNode.getChildNodes();
            for (int i = 0; i < mappingNodeList.getLength(); i++) {
                Node mappingNode = mappingNodeList.item(i);
                if (mappingNode.getNodeType() == Node.ELEMENT_NODE && mappingNode.getNodeName().equals("mapping")) {
                    Element mappingElement = (Element) mappingNode;
                    HotDeployConfig.Mapping mapping = new HotDeployConfig.Mapping();
                    // 解析 file_type
                    NodeList fileTypeNodeList = mappingElement.getElementsByTagName("file_type");
                    if (fileTypeNodeList.getLength() > 0) {
                        Node fileTypeNode = fileTypeNodeList.item(0);
                        List<String> fileType = new ArrayList<>();
                        if(fileTypeNode.getTextContent() != null) {
                            for(String s : fileTypeNode.getTextContent().split(",")){
                                fileType.add(s.trim());
                            }
                        }
                        mapping.setFileType(fileType);
                    }
                    // 解析 path
                    NodeList pathNodeList = mappingElement.getElementsByTagName("path");
                    if (pathNodeList.getLength() > 0) {
                        Node pathNode = pathNodeList.item(0);
                        if(!pathNode.getTextContent().isEmpty()) {
                            String realPath = pathNode.getTextContent().trim().replace("$rootPath$",ROOTPATH);
                            mapping.setPath(realPath);
                        }
                    }
                    // 解析 remote_path
                    NodeList remotePathNodeList = mappingElement.getElementsByTagName("remote-path");
                    if (remotePathNodeList.getLength() > 0) {
                        Node remotePathNode = remotePathNodeList.item(0);
                        mapping.setRemotePath(remotePathNode.getTextContent());
                    }
                    config.getMappings().add(mapping);
                }
            }
        }

        return config;
    }

    @Override
    public String toString() {
        return "HotDeployConfig{" +
                "secret='" + secret + '\'' +
                ", remoteIp='" + remoteIp + '\'' +
                ", remotePort=" + remotePort +
                ", timeout=" + timeout +
                ", autoHotDeploy=" + autoHotDeploy +
                ", mappings=" + mappings +
                '}';
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret != null ? secret.trim() : null;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp != null ? remoteIp.trim() : null;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isAutoHotDeploy() {
        return autoHotDeploy;
    }

    public void setAutoHotDeploy(boolean autoHotDeploy) {
        this.autoHotDeploy = autoHotDeploy;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<Mapping> mappings) {
        this.mappings = mappings;
    }

    public List<HotDeployConfig.Mapping> getMappingConfig(String filePath) {
        if(null == mappings) {
            return Collections.EMPTY_LIST;
        }
        return  getMappings().stream()
                .filter(c->c.matchPath(filePath))
                .filter(c->c.matchFileType(filePath))
                .collect(Collectors.toList());
    }


    static class Mapping {
        private List<String> fileType;
        private String path;
        private String remotePath;

        @Override
        public String toString() {
            return "Mapping{" +
                    "fileType=" + fileType +
                    ", path='" + path + '\'' +
                    ", remotePath='" + remotePath + '\'' +
                    '}';
        }

        public List<String> getFileType() {
            return fileType;
        }

        public void setFileType(List<String> fileType) {
            this.fileType = fileType;
        }

        public boolean matchPath(String path) {
            return null != path && path.contains(this.path);
        }

        public boolean matchFileType(String fileName) {
            for(String endWithStr : fileType) {
                if(fileName.endsWith(endWithStr)) {
                    return true;
                }
            }
            return false;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getRemotePath() {
            return remotePath;
        }

        public void setRemotePath(String remotePath) {
            this.remotePath = remotePath != null ? remotePath.trim() : null;
        }
    }
}


