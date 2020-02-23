package com.github.hal4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.GenericResource;
import com.github.hal4j.resources.ResourceSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class ResourceParser<R extends ResourceSupport> {

    private final ObjectMapper mapper;

    private final Function<GenericResource, R> transformation;

    ResourceParser(ObjectMapper mapper,
                   Function<GenericResource, R> transformation) {
        this.mapper = mapper;
        this.transformation = transformation;
    }

    public R parse(String src) throws IOException {
        return transformation.apply(mapper.readValue(src, GenericResource.class));
    }

    public R parse(InputStream src) throws IOException {
        return transformation.apply(mapper.readValue(src, GenericResource.class));
    }

    public R parse(byte[] src) throws IOException {
        return transformation.apply(mapper.readValue(src, GenericResource.class));
    }

}
