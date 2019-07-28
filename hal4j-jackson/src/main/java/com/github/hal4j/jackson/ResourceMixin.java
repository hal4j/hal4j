package com.github.hal4j.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.hal4j.resources.HALLink;

import java.util.List;
import java.util.Map;

@JsonDeserialize(using = ResourceDeserializer.class)
public abstract class ResourceMixin<T> {

    @JsonUnwrapped
    public T model;

    @JsonGetter("_links")
    public abstract Map<String, List<HALLink>> links();

    @JsonGetter("_embedded")
    public abstract Map<String, List<Object>> attachments();

}
