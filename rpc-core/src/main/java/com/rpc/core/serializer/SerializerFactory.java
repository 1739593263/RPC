package com.rpc.core.serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {
    private final static Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<>(){{
       put(SerializerKeys.JDK, new JdkSerializer());
       put(SerializerKeys.JSON, new JsonSerializer());
       put(SerializerKeys.HESSIAN, new HessianSerializer());
       put(SerializerKeys.KRYO, new KryoSerializer());
    }};

    private final static Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get(SerializerKeys.JDK);

    public final static Serializer getInstance(String key) {
        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
    }
}
