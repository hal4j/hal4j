package com.github.hal4j.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.ResourceSupport;

import java.util.List;
import java.util.Map;

@JsonDeserialize(using = ResourceDeserializer.class)
public abstract class ResourceMixin<T> {

    @JsonUnwrapped
    public T model;

    @JsonProperty("_links")
    private Map<String, List<HALLink>> _links;

    @JsonProperty("_embedded")
    private Map<String, List<Object>> _embedded;

}
