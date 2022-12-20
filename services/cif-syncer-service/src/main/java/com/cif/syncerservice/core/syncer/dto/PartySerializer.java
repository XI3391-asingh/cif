package com.cif.syncerservice.core.syncer.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class PartySerializer<T> implements Serializer<T> {

    private ObjectMapper mapper;

    @Override
    public void configure(final Map<String, ?> settings, final boolean isKey) {
        mapper = new ObjectMapper();
    }

    @Override
    public byte[] serialize(final String topic, final T object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
