package com.rpc.core.proxy;

import java.lang.reflect.Proxy;

/**
 * ServiceProxy Factory Class
 */
public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}