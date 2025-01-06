package com.rpc.core.proxy;

import com.rpc.core.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * ServiceProxy Factory Class
 */
public class ServiceProxyFactory {
    /**
     * Proxy Factory of Service
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    /**
     * Mock Proxy Factory of Service
     * @param mockServiceClass
     * @param <T>
     * @return
     */
    public static <T> T getMockProxy(Class<T> mockServiceClass) {
        return (T) Proxy.newProxyInstance(mockServiceClass.getClassLoader(),
                new Class[]{mockServiceClass},
                new MockServiceProxy());
    }
}
