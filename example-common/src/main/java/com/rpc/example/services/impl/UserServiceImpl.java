package com.rpc.example.services.impl;

import com.rpc.example.model.User;
import com.rpc.example.services.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("User name: "+user.getName());
        return user;
    }
}
