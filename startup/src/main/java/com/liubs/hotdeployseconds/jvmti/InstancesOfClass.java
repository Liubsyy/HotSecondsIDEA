package com.liubs.hotdeployseconds.jvmti;

public class InstancesOfClass {
    public static native Object[] getInstances(Class<?> targetClass);
}