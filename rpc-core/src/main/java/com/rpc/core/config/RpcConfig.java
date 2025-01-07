package com.rpc.core.config;

import com.rpc.core.serializer.Serializer;
import com.rpc.core.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {
    private String name = "RPC";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8080;

    private String serializer = SerializerKeys.JDK;

    /**
     * mock invoke rpc interface
     */
    private boolean mock = false;
}
