package com.rpc.example.model;

import java.io.Serializable;

/**
 * User
 *
 * implements Serializable for serialize network information
 */
public class User implements Serializable {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
