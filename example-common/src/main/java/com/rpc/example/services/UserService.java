package com.rpc.example.services;

import com.rpc.example.model.User;

/**
 * User service
 */
public interface UserService {
    /**
     * Get User
     *
     * @param user
     * @return
     */
    User getUser(User user);


    /**
     *  Get a Primitive value
     */
    default short getNum() {
        short res=1;
        return res;
    }
}
