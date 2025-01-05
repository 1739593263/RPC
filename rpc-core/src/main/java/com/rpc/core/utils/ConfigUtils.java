package com.rpc.core.utils;


import cn.hutool.setting.dialect.Props;
import io.micrometer.common.util.StringUtils;

/**
 * Configure tool class
 */
public class ConfigUtils {
    /**
     * load config object
     * @param tClass
     * @param prefix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix){
        return loadConfig(tClass, prefix, "");
    }

    /**
     * load config object with environment support
     * @param tClass
     * @param prefix
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment){
        StringBuilder configBuilder = new StringBuilder("application");
        if (StringUtils.isNotBlank(environment)) {
            configBuilder.append("-"+environment);
        }
        configBuilder.append(".properties");

        Props props = new Props(configBuilder.toString());
        return props.toBean(tClass, prefix);
    }
}
