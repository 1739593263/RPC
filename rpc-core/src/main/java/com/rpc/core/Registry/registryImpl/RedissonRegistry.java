package com.rpc.core.Registry.registryImpl;

import cn.hutool.json.JSONUtil;
import com.rpc.core.Registry.Registry;
import com.rpc.core.config.RegistryConfig;
import com.rpc.core.model.ServiceMetaInfo;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RedissonRegistry implements Registry {
    private  RedissonClient redisson;

    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig config) {
        // 1. Create config object
        Config redissonConfig = new Config();
        String redisAddress = config.getAddress();
        redissonConfig.useSingleServer().setAddress(redisAddress).setDatabase(3);

        // 2. Create Redisson instance
        // Sync and Async API
        redisson = Redisson.create(redissonConfig);
    }

    // set key-value and timeout
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException, Exception {
        // create key-value pair: key==>register key; value==>serviceMetaInfo
        String register_key = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        String value = JSONUtil.toJsonStr(serviceMetaInfo);
        long timeout = 30;
        // Get the RBucket object
        RBucket<String> bucket = redisson.getBucket(register_key);

        // Attempt to set the value if the key does not exist
        boolean isSet = bucket.trySet(value);

        if (isSet) {
            // If the value was set, set the expiration time
            bucket.expire(timeout, TimeUnit.SECONDS);
            System.out.println("Value set successfully with timeout of " + timeout + " seconds.");
        } else {
            System.out.println("Key already exists. Value not set.");
        }
    }

    // delete the key_value pair by key
    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        String register_key = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();

        // Get the RBucket object
        RBucket<String> bucket = redisson.getBucket(register_key);

        // Delete the key
        boolean isDeleted = bucket.delete();

        if (isDeleted) {
            System.out.println("Key '" + register_key + "' deleted successfully.");
        } else {
            System.out.println("Key '" + register_key + "' does not exist.");
        }
    }

    // get value by key
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        String prefix = ETCD_ROOT_PATH + serviceKey + "/";

        RKeys keys = redisson.getKeys();

        // Get all keys with the specified prefix
        Iterable<String> keySet = keys.getKeysByPattern(prefix + "*");
        List<ServiceMetaInfo> serviceMetaInfoList = new ArrayList<>();
        // Retrieve and print values for each key
        for (String key : keySet) {
            RBucket<String> bucket = redisson.getBucket(key);
            String value = bucket.get();
            ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
            serviceMetaInfoList.add(serviceMetaInfo);
            System.out.println("Key: " + key + ", Value: " + value);
        }
        return serviceMetaInfoList;
    }

    // end the redisson client
    @Override
    public void destroy() {
        redisson.shutdown();
    }
}
