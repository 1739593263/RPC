package com.rpc.core.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.core.model.RpcRequestModel;
import com.rpc.core.model.RpcResponseModel;

import java.io.IOException;

/**
 * Json Serializer
 */
public class JsonSerializer implements Serializer{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, type);
        if (obj instanceof RpcResponseModel) {
            handleResponse((RpcResponseModel) obj, type);
        } else if (obj instanceof  RpcRequestModel) {
            handleRequest((RpcRequestModel)obj, type);
        }
        return null;
    }

    private <T> T handleResponse(RpcResponseModel responseModel, Class<T> type) throws IOException {
        byte[] paramBytes = OBJECT_MAPPER.writeValueAsBytes(responseModel);
        responseModel.setData(OBJECT_MAPPER.readValue(paramBytes, responseModel.getParameterType()));
        return type.cast(responseModel);
    }

    private <T> T handleRequest(RpcRequestModel requestModel, Class<T> type) throws IOException {
        Class<?>[] parameterType = requestModel.getParameterType();
        Object[] args = requestModel.getArgs();

        for (int i=0; i< parameterType.length; i++) {
            Class<?> clazz = parameterType[i];
            // if different types case
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argBytes, clazz);
            }
        }
        return type.cast(requestModel);
    }
}
