package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.BindingContext;
import com.github.hal4j.resources.GenericResource;
import com.github.hal4j.resources.Resource;

public class JacksonBindingContext implements BindingContext {

    private final ObjectMapper mapper;

    public JacksonBindingContext(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public <T> T bind(Object value, Class<T> targetType) {
        if (targetType.isInstance(value)) {
            return targetType.cast(value);
        } else if (value instanceof Resource) {
            bind(((Resource) value).value(), targetType);
        }
        try {
            JsonNode tree = mapper.valueToTree(value);
            return mapper.treeToValue(tree, targetType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot parse value to " + targetType);
        }
    }

    @Override
    public <T> Resource<T> bindResource(Object value, Class<T> targetType) {
        return bind(value, GenericResource.class).as(targetType);
    }
}
