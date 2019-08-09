package com.github.hal4j.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.ResourceSupport;

import java.util.List;
import java.util.Map;

@JsonDeserialize(using = ResourcesDeserializer.class)
public abstract class ResourcesMixin {

    @JsonProperty("_links")
    private Map<String, List<HALLink>> _links;

    @JsonProperty("_embedded")
    private Map<String, List<Object>> _embedded;

}
