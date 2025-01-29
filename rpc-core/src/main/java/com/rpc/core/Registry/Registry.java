package com.rpc.core.Registry;

import com.rpc.core.config.RegistryConfig;
import com.rpc.core.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Registry {
    void init(RegistryConfig config);

    void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException, Exception;

    void unregister(ServiceMetaInfo serviceMetaInfo);

    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    void destroy();
}
