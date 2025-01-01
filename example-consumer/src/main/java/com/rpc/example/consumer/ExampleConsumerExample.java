package com.rpc.example.consumer;

import com.rpc.example.model.User;
import com.rpc.example.services.UserService;

public class ExampleConsumerExample {
    public static void main(String[] args) {
        // todo: get the implement userService method by RPC.
        UserService userService = null;
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
