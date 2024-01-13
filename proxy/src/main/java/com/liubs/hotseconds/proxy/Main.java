package com.liubs.hotseconds.proxy;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.rmi.AlreadyBoundException;

/**
 * @author Liubsyy
 * @date 2023/11/25 12:35 AM
 **/
public class Main {

    private static String configPath;
    private static String host;
    private static int port;

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        String lib = null;
        if(osName.contains("linux")) {
            lib = "proxyserver.so";
        }else if(osName.contains("mac")) {
            lib = "proxyserver.dylib";
        }else{
            throw new UnsupportedOperationException("不支持当前操作系统");
        }

        URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
        String dir = url.getPath();
        File currentFile = new File(url.getPath());
        if(!currentFile.isDirectory()) {
            dir = currentFile.getParent();
        }

        configPath = dir+"/proxy-config.xml";

        System.load(dir+"/"+lib);
    }


    private static void initConfigFile() throws IOException, SAXException, ParserConfigurationException {
        File configFile = new File(configPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(configFile);
        doc.getDocumentElement().normalize();

        NodeList hostNameList = doc.getElementsByTagName("host");
        if (hostNameList.getLength() > 0) {
            Node node = hostNameList.item(0);
            host = node.getTextContent();
        }

        NodeList portList = doc.getElementsByTagName("port");
        if (portList.getLength() > 0) {
            Node node = portList.item(0);
            port = Integer.parseInt(node.getTextContent());
        }

    }


    public static native void openProxyService(String host, int port) throws IOException, AlreadyBoundException;



    public static boolean isPortInUse(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return false;
        } catch (Exception e) {
            // 端口被占用会抛出异常
            return true;
        }
    }


    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, AlreadyBoundException {
        initConfigFile();
        if(isPortInUse(port)) {
            System.out.printf("端口%d已经被占用\n",port);
            return;
        }

        openProxyService(host,port);
        System.out.printf("代理服务器启动成功, host:%s, port:%d\n",host,port);

    }
}
