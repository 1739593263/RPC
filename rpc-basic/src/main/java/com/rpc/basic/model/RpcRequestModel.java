package com.rpc.basic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc request
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequestModel {
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
