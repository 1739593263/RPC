package com.rpc.basic.model;

import com.rpc.basic.serializer.Serializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * rpc request
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequestModel implements Serializable {
    /**
     * service name
     */
    private String serviceName;

    /**
     * method name
     */
    private String methodName;

    /**
     * parameter type list
     */
    private Class<?>[] parameterType;

    /**
     * parameters list
     */
    private Object[] args;
}
