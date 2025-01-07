package com.rpc.core.serializer;

import com.rpc.core.spi.SPILoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Serializer bean Factory
 */
public class SerializerFactory {
//    private final static Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<>(){{
//       put(SerializerKeys.JDK, new JdkSerializer());
//       put(SerializerKeys.JSON, new JsonSerializer());
//       put(SerializerKeys.HESSIAN, new HessianSerializer());
//       put(SerializerKeys.KRYO, new KryoSerializer());
//    }};
//
//    private final static Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get(SerializerKeys.JDK);
//
//    public final static Serializer getInstance(String key) {
//        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
//    }

    static {
        SPILoader.load(Serializer.class);
    }

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getInstance(String key) {
        return SPILoader.getInstance(Serializer.class, key);
    }
}
