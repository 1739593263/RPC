package com.rpc.example.consumer;

import com.rpc.basic.proxy.ServiceProxyFactory;
import com.rpc.core.config.RpcConfig;
import com.rpc.core.utils.ConfigUtils;
import com.rpc.example.model.User;
import com.rpc.example.services.UserService;

public class ExampleConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);

        // get the proxy class of userService
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("Kun");
        User newUser = userService.getUser(user);
        if (newUser!=null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("No User found");
        }
    }
}
