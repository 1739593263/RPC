package com.rpc.core.server;

import com.rpc.core.Registry.LocalRegistry;
import com.rpc.core.RpcApplication;
import com.rpc.core.model.RpcRequestModel;
import com.rpc.core.model.RpcResponseModel;
import com.rpc.core.serializer.JdkSerializer;
import com.rpc.core.serializer.Serializer;
import com.rpc.core.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Http request processing
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());

        System.out.println("Received Request: "+request.method()+" "+request.uri());

        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequestModel requestModel = null;
            try {
                requestModel = serializer.deserialize(bytes, RpcRequestModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            RpcResponseModel responseModel = new RpcResponseModel();
            if (requestModel==null) {
                responseModel.setMessage("rpcRequest is null");
                doResponse(request, responseModel, serializer);
            }

            try {
                // get the invoked service class by reflection
                Class<?> implClass = LocalRegistry.get(requestModel.getServiceName());
                Method method = implClass.getMethod(requestModel.getMethodName(), requestModel.getParameterType());
                Object result = method.invoke(implClass.newInstance(), requestModel.getArgs());

                // encapsulate the response result
                responseModel.setData(result);
                responseModel.setParameterType(method.getReturnType());
                responseModel.setMessage("OK");
            } catch (Exception e) {
                e.printStackTrace();
                responseModel.setData(e.getMessage());
                responseModel.setException(e);
            }

            doResponse(request, responseModel, serializer);
        });
    }

    /**
     * Response process
     *
     * @param request
     * @param responseModel
     * @param serializer
     */
    public void doResponse(HttpServerRequest request, RpcResponseModel responseModel, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try {
            byte[] bytes = serializer.serialize(responseModel);
            httpServerResponse.end(Buffer.buffer(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
