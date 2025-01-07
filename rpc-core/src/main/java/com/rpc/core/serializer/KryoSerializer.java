package com.rpc.core.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo Serializer
 */
public class KryoSerializer implements Serializer{
    // because Kryo is not thread security, using threadLocal to guarantee only one thread.
    private static final ThreadLocal<Kryo> kryo_thread = ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        return kryo;
    });


    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo_thread.get().writeObject(output, byteArrayOutputStream);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        T res = kryo_thread.get().readObject(input, type);
        input.close();
        return res;
    }
}
