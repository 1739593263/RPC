package com.rpc.core;

import com.rpc.core.common.RpcConstant;
import com.rpc.core.config.RpcConfig;
import com.rpc.core.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * init rpc structure
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("config init, it is {}",rpcConfig.toString());
    }

    /**
     * init rpc
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // use defined Config if the set config cannot be loaded.
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * get rpc config
     * 双检锁单例模式
     * @return
     */
    public static RpcConfig getConfig(){
        if (rpcConfig==null) {
            synchronized (RpcApplication.class) {
                // only one rpcConfig can be initialized.
                if (rpcConfig==null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
