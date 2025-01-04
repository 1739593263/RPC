package com.rpc.example.provider;

import com.rpc.basic.Registry.LocalRegistry;
import com.rpc.basic.server.VertxHttpServer;
import com.rpc.example.services.UserService;

public class EasyProviderExample {
    public static void main(String[] args) {
        // register
        LocalRegistry.put(UserService.class.getName(), UserServiceImpl.class);

        // launch the server
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(8080);
    }
}
