package com.rpc.core.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class ServiceMetaInfo {
    /**
     * Service Name
     */
    private String serviceName;

    private String serviceHost;

    private String serviceVersion = "1.0";

    private Integer servicePort;

    /**
     * Service Group(to be implemented)
     */
    private String serviceGroup = "default";

    /**
     * get Service key == ServiceName:serviceVersion
     * @return
     */
    public String getServiceKey() {
        return String.format("%s:%s",serviceName,serviceVersion);
    }

    /**
     * get Service Node Key == ServiceKey/serviceHost:servicePort
     * @return
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%s",getServiceKey(),serviceHost,servicePort);
    }

    /**
     * get the complete service address
     * @return
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }
}
