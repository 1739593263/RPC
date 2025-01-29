package com.rpc.core.Registry.registryImpl;

import cn.hutool.json.JSONUtil;
import com.rpc.core.Registry.Registry;
import com.rpc.core.config.RegistryConfig;
import com.rpc.core.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry {
    private Client client;
    private KV kvClient;

    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig config) {
        client = Client.builder().endpoints(config.getAddress())
                .connectTimeout(Duration.ofMillis(config.getTimeout()))
                .build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // get leaseClient to set time-out of a key-value
        Lease leaseClient = client.getLeaseClient();
        // create a leaseId of 30 sec
        long leaseId = leaseClient.grant(30).get().getID();

        // create key-value pair: key==>register key; value==>serviceMetaInfo
        String register_key = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(register_key, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // relate key-value to leaseId
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        String register_key = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(register_key, StandardCharsets.UTF_8);
        kvClient.delete(key);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        String prefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // prefix searching
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> kvs = kvClient.get(ByteSequence.from(prefix, StandardCharsets.UTF_8), getOption)
                    .get().getKvs();

            // transfer key-value into the value list
            return kvs.stream().map(kv->{
                String value = kv.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Fail to retrieve service Info");
        }
    }

    @Override
    public void destroy() {
        System.out.println("node outline...");
        if (client!=null) client.close();
        if (kvClient!=null) kvClient.close();
    }
}
