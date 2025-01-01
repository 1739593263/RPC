package com.rpc.basic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC Response
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponseModel implements Serializable {
    /**
     * return data
     */
    private Object data;

    /**
     * return data type
     */
    private Class<?> parameterType;

    /**
     * response message (code)
     */
    private String message;

    /**
     * exception information
     */
    private Exception exception;
}
