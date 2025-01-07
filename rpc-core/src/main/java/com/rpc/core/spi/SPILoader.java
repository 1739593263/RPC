package com.rpc.core.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.CustomKeyMap;
import com.rpc.core.serializer.Serializer;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SPILoader {
    private static Map<String, Map<String, Class<?>>> loadMap = new ConcurrentHashMap<>();

    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    private static final String RPC_SYSTEM_DIR = "META-INF/rpc/system/";
    private static final String RPC_CUSTOM_DIR = "META-INF/rpc/custom/";

    // scan path
    private static final String[] SCAN_PATH = new String[]{RPC_SYSTEM_DIR, RPC_CUSTOM_DIR};

    // load class list
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    public void loadAll() {
        log.info("load all class");
        for (Class<?> clazz : LOAD_CLASS_LIST) {
            load(clazz);
        }
    }

    /**
     * get the instance of the interface
     * @param tClass
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loadMap.get(tClassName);

        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader doesn't load %s", tClassName));
        }

        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader doesn't include key = %s in the %s", key, tClassName));
        }
//        System.out.println(keyClassMap.size());
        // get the implement instance
        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(String.format("fail to creat the instance of {}", implClassName));
            }
        }
        return (T) instanceCache.get(implClassName);
    }

    /**
     * load a class
     * @param loadClass
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("Load the {} type of SPI", loadClass.getName());

        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir:SCAN_PATH) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());

            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine())!=null) {
                        String[] split = line.split("=");
                        if (split.length > 1) {
                            String key = split[0];
                            String className = split[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    log.error("SPI resources load error {}", e);
//                    e.printStackTrace();
                }
            }
        }
        loadMap.put(loadClass.getName(), keyClassMap);
//        System.out.println(keyClassMap.size());
        return keyClassMap;
    }
}
