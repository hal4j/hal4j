package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.hal4j.resources.GenericResource;

import java.io.IOException;

public class GenericResourceSerializer extends JsonSerializer<GenericResource> {

    @Override
    public void serialize(GenericResource resource,
                          JsonGenerator generator,
                          SerializerProvider provider) throws IOException {
        generator.writeStartObject();
        if (resource.embedded().asIs() != null) {
            generator.writeObjectField("_embedded", resource.embedded().asIs());
        }
        if (resource.get() != null) {
            resource.get().forEach((key, value) -> {
                try {
                    generator.writeObjectField(key, value);
                } catch (IOException e) {
                    throw new IllegalStateException("", e);
                }
            });
        }
        if (resource.links().asIs() != null) {
            generator.writeObjectField("_links", resource.links().asIs());
        }
        generator.writeEndObject();
    }
}
