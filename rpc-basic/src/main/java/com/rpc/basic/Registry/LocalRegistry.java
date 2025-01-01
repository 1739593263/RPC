package com.rpc.basic.Registry;

import java.util.HashMap;
import java.util.Map;

public class LocalRegistry {
    /**
     * Registry info repository
     */
    private static final Map<String, Class<?>> map = new HashMap<>();

    /**
     * put Registry Info
     *
     * @param serviceName
     * @param implClass
     */
    public static void put(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * get Registry Info
     *
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * remove Registry Info
     *
     * @param serviceName
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }

}
