package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ResourceDeserializer extends JsonDeserializer<Resource<?>> {

    private JavaType modelType;

    public ResourceDeserializer() {}

    public ResourceDeserializer(JavaType modelType) {
        this.modelType = modelType;
    }

    @Override
    public Resource<?> deserialize(JsonParser jp,
                              DeserializationContext ctx) throws IOException, JsonProcessingException {
        ObjectCodec codec = jp.getCodec();
        ObjectNode node = codec.readTree(jp);
        Map<String, List<Object>> embedded = Deserializers.parseAndRemove(codec, node, "_embedded", new TypeReference<Map<String, List<Object>>>(){});
        Map<String, List<HALLink>> links = Deserializers.parseAndRemove(codec, node, "_links", new TypeReference<Map<String, List<HALLink>>>(){});
        Object model = codec.readValue(codec.treeAsTokens(node), modelType);
        return new Resource<>(model, links, embedded);
    }

}
