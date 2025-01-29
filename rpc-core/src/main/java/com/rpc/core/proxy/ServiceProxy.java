package com.rpc.core.proxy;

import cn.hutool.core.collection.CollUtil;
import com.rpc.core.Registry.Registry;
import com.rpc.core.Registry.RegistryFactory;
import com.rpc.core.RpcApplication;
import com.rpc.core.common.RpcConstant;
import com.rpc.core.config.RpcConfig;
import com.rpc.core.model.RpcRequestModel;
import com.rpc.core.model.RpcResponseModel;
import com.rpc.core.model.ServiceMetaInfo;
import com.rpc.core.serializer.JdkSerializer;
import com.rpc.core.serializer.Serializer;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.rpc.core.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements InvocationHandler {
    // server address
    private static final String SERVER_ADDR = "http://"+RpcApplication.getConfig().getServerHost()+
            ":"+RpcApplication.getConfig().getServerPort();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // JDK serializer
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());

        // build request
        String serviceName = method.getDeclaringClass().getName();
        RpcRequestModel requestModel = RpcRequestModel.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterType(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // serialize
            byte[] bodyBytes = serializer.serialize(requestModel);

            // retrieve the request from registry
            RpcConfig rpcConfig = RpcApplication.getConfig();
            Registry registry = RegistryFactory
                    .getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry
                    .serviceDiscovery(serviceMetaInfo.getServiceKey());

            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("No valid service found");
            }

            // get the target service method
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                RpcResponseModel rpcResponse = serializer.deserialize(result, RpcResponseModel.class);
                return rpcResponse.getData();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        //        // serialize
//        byte[] requestBody = serializer.serialize(requestModel);
//        try(HttpResponse httpResponse = HttpRequest.post(SERVER_ADDR).body(requestBody).execute()) {
//            // deserialize
//            byte[] bytes = httpResponse.bodyBytes();
//            RpcResponseModel rpcResponse = serializer.deserialize(bytes, RpcResponseModel.class);
//            return rpcResponse.getData();
//        } catch (Exception e) {
////            throw new RuntimeException(e);
//            e.printStackTrace();
//        }
        return null;
    }
}
