package com.rpc.example.provider;

import com.rpc.basic.Registry.LocalRegistry;
import com.rpc.basic.server.HttpServer;
import com.rpc.basic.server.VertxHttpServer;
import com.rpc.core.RpcApplication;
import com.rpc.example.services.UserService;

public class EasyProviderExample {
    public static void main(String[] args) {
        // init RPC structure
        RpcApplication.init();

        // register
        LocalRegistry.put(UserService.class.getName(), UserServiceImpl.class);

        // launch the server
        HttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(RpcApplication.getConfig().getServerPort());
    }
}
