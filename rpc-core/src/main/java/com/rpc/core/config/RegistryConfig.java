package com.rpc.core.config;

import lombok.Data;

/**
 * Registry Center Configuration
 */
@Data
public class RegistryConfig {
    /**
     * Registry category
     */
    private String registry = "redisson";

    /**
     * Registry address
     */
    private String address = "redis://localhost:6379";

    private String username;

    private String password;

    /**
     * timeout(ms)
     */
    private Long timeout = 10000L;
}
