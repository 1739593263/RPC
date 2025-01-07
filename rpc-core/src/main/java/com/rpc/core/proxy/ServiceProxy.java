package com.rpc.core.proxy;

import com.rpc.core.RpcApplication;
import com.rpc.core.model.RpcRequestModel;
import com.rpc.core.model.RpcResponseModel;
import com.rpc.core.serializer.JdkSerializer;
import com.rpc.core.serializer.Serializer;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.rpc.core.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // JDK serializer
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());

        // build request
        RpcRequestModel requestModel = RpcRequestModel.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterType(method.getParameterTypes())
                .args(args)
                .build();

        // serialize
        byte[] requestBody = serializer.serialize(requestModel);
        try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").body(requestBody).execute()) {
            // deserialize
            byte[] bytes = httpResponse.bodyBytes();
            RpcResponseModel rpcResponse = serializer.deserialize(bytes, RpcResponseModel.class);
            return rpcResponse.getData();
        } catch (Exception e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return null;
    }
}
