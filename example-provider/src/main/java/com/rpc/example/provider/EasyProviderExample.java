package com.rpc.example.provider;

import com.rpc.basic.server.VertxHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start(8080);
    }
}
