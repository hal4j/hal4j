package com.github.hal4j.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.hal4j.resources.HALLink;

import java.util.List;
import java.util.Map;

@JsonDeserialize(using = NavigationResourceDeserializer.class)
public abstract class NavigationResourceMixin {

    @JsonProperty("_links")
    private Map<String, List<HALLink>> _links;

    @JsonProperty("_embedded")
    private Map<String, List<Object>> _embedded;

}
