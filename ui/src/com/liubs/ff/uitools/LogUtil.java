package com.liubs.ff.uitools;

import com.intellij.notification.*;
import com.intellij.openapi.ui.MessageType;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtil {
    private static NotificationGroup notificationGroup
            = new NotificationGroup("noticeLog", NotificationDisplayType.NONE, true);



    public static void info(String message){
        if(message == null || message.isEmpty()) {
            return;
        }
        Notification notification = notificationGroup.createNotification(message, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }

    public static void info(String message,Object ...param){
        if(message == null || message.isEmpty()) {
            return;
        }
        message = String.format(message,param);
        Notification notification = notificationGroup.createNotification(message, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }

    public static void error(String message){
        if(message == null || message.isEmpty()) {
            return;
        }
        Notification notification = notificationGroup.createNotification(message, MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }
    public static void error(String message,Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        Notification notification = notificationGroup.createNotification(message+":"+stackTrace, MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }

    public static void warning(String message){
        if(message == null || message.isEmpty()) {
            return;
        }
        Notification notification = notificationGroup.createNotification(message, MessageType.WARNING);
        Notifications.Bus.notify(notification);
    }

    public static void auto(String message,boolean enable){
        if(message == null || message.isEmpty()) {
            return;
        }
        Notification notification = notificationGroup.createNotification(message, enable ? MessageType.INFO: MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }
}
