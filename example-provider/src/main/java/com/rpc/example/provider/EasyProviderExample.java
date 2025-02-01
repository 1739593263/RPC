package com.rpc.example.provider;

import com.rpc.core.Registry.LocalRegistry;
import com.rpc.core.Registry.Registry;
import com.rpc.core.Registry.RegistryFactory;
import com.rpc.core.config.RegistryConfig;
import com.rpc.core.config.RpcConfig;
import com.rpc.core.model.ServiceMetaInfo;
import com.rpc.core.server.HttpServer;
import com.rpc.core.server.VertxHttpServer;
import com.rpc.core.RpcApplication;
import com.rpc.example.model.User;
import com.rpc.example.services.UserService;

public class EasyProviderExample {
    public static void main(String[] args) {
        // init RPC structure
        RpcApplication.init();

        // register
        String serviceName = UserService.class.getName();
        LocalRegistry.put(serviceName, UserServiceImpl.class);

        // register into the defined registry
        RpcConfig rpcConfig = new RpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory
                .getInstance(registryConfig.getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // launch the server
        HttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(RpcApplication.getConfig().getServerPort());
    }
}
