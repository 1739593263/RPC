package com.rpc.core.Registry;

import com.rpc.core.Registry.registryImpl.EtcdRegistry;
import com.rpc.core.spi.SPILoader;

public class RegistryFactory {
    static {
        SPILoader.load(Registry.class);
    }

    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key) {
        return  SPILoader.getInstance(Registry.class, key);
    }
}
