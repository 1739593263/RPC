package com.rpc.core.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock Service Proxy
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodReturnType = method.getReturnType();
        log.info("method invoke {}", method.getName());
//        System.out.println("mock invoked");
        return getDefaultResult(methodReturnType);
    }

    /**
     * get the primitive return value
     * @param type
     * @return
     */
    public Object getDefaultResult(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return true;
            } else if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            } else if (type == short.class) {
                return (short)0;
            }
        }

        return null;
    }
}
