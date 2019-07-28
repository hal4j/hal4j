package com.github.hal4j.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.hal4j.resources.HALLink;

import java.util.List;
import java.util.Map;

@JsonDeserialize(using = ResourcesDeserializer.class)
public abstract class ResourcesMixin {

    @JsonGetter("_links")
    public abstract Map<String, List<HALLink>> links();

    @JsonGetter("_embedded")
    public abstract Map<String, List<Object>> attachments();

}
