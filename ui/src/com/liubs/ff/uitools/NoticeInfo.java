package com.liubs.ff.uitools;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.MessageType;

public class NoticeInfo {
    private static NotificationGroup notificationGroup
            = new NotificationGroup("HotDeploy", NotificationDisplayType.TOOL_WINDOW, false);

    public static void info(String message){
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
        Notification notification = notificationGroup.createNotification(message, MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }

    public static void error(String message,Object ...param){
        if(message == null || message.isEmpty()) {
            return;
        }
        message = String.format(message,param);
        Notification notification = notificationGroup.createNotification(message, MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }

    public static void warning(String message){
        Notification notification = notificationGroup.createNotification(message, MessageType.WARNING);
        Notifications.Bus.notify(notification);
    }
    public static void warning(String message,Object ...param){
        if(message == null || message.isEmpty()) {
            return;
        }
        message = String.format(message,param);
        Notification notification = notificationGroup.createNotification(message, MessageType.WARNING);
        Notifications.Bus.notify(notification);
    }

    public static void auto(String message,boolean enable){
        Notification notification = notificationGroup.createNotification(message, enable ? MessageType.INFO: MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }


}
