package com.rpc.basic.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.rpc.basic.model.RpcRequestModel;
import com.rpc.basic.model.RpcResponseModel;
import com.rpc.basic.serializer.JdkSerializer;
import com.rpc.basic.serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // JDK serializer
        Serializer serializer = new JdkSerializer();

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
