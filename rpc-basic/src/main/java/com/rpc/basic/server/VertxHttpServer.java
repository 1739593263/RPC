package com.rpc.basic.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    @Override
    public void start(int port) {
        Vertx vertx = Vertx.vertx();

        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // listen the port and cope the request
//        server.requestHandler(request -> {
//            // cope the request
//            System.out.println("Received request "+request.method()+" "+request.uri());
//
//            // send http response
//            request.response()
//                    .putHeader("content-type", "text-plain")
//                    .end("Hello from Vert.x http server");
//        });

        // listen the port and handle requests
        server.requestHandler(new HttpServerHandler());

        // launch server and listen the port.
        server.listen(port, result -> {
           if (result.succeeded()) {
               System.out.println("Server is now listening on port "+ port);
           } else {
               System.err.println("Failed to start server: "+result.cause());
           }
        });
    }
}
