package com.rpc.core.model;

import com.rpc.core.common.RpcConstant;
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
     * service version
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * parameter type list
     */
    private Class<?>[] parameterType;

    /**
     * parameters list
     */
    private Object[] args;
}
