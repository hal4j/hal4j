package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

/**
 * @deprecated use HALObjectMapperFactory instead
 */
@Deprecated
public class JacksonHALMapper {

    private ObjectMapper mapper;

    public JacksonHALMapper() {
        mapper = HALObjectMapperFactory.createStrictMapper();
    }

    public <T> T parse(String string, Class<T> type) {
        try {
            return mapper.readValue(string, type);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot parse string", e);
        }
    }

    public <T> T parse(String string, TypeReference<T> type) {
        try {
            return mapper.readValue(string, type);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot parse string", e);
        }
    }

    public String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Object cannot be serialized", e);
        }
    }

    public void printTo(Writer writer, Object object) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, object);
        } catch (IOException e) {
            throw new IllegalArgumentException("Object cannot be serialized", e);
        }
    }

    public void printTo(PrintStream stream, Object object) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(stream, object);
        } catch (IOException e) {
            throw new IllegalArgumentException("Object cannot be serialized", e);
        }
    }

    public ObjectMapper mapper() {
        return mapper;
    }
}
