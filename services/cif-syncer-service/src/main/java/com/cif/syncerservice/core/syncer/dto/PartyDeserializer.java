package com.cif.syncerservice.core.syncer.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;


public class PartyDeserializer<T extends PartyRequest> implements Deserializer<T> {
    private ObjectMapper objectMapper;
    private Class<T> type;

    /**
     * Default constructor needed by Kafka
     */
    public PartyDeserializer() {

    }

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public T deserialize(String ignored, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(bytes, new TypeReference<T>() {
            });
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    protected Class<T> getType() {
        return type;
    }

    @Override
    public void close() {

    }
}
