package com.rpc.core.serializer;

import java.io.IOException;

/**
 * Serializer interface
 */
public interface Serializer {
    /**
     * Serialize
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * Deserialize
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
