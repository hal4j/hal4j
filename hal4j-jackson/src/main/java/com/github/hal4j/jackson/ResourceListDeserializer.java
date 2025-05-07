package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.ResourceList;
import com.github.hal4j.resources.ResourceViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ResourceListDeserializer extends JsonDeserializer<ResourceList<?>> {

    private JavaType modelType;

    public ResourceListDeserializer() {}

    public ResourceListDeserializer(JavaType modelType) {
        this.modelType = modelType;
    }

    @Override
    public ResourceList<?> deserialize(JsonParser jp,
                                       DeserializationContext ctx) throws IOException, JsonProcessingException {
        ObjectCodec codec = jp.getCodec();
        ObjectNode node = codec.readTree(jp);
        Map<String, List<Object>> embedded = Deserializers.parseAndRemove(codec, node, "_embedded", new TypeReference<>() {});
        Map<String, List<HALLink>> links = Deserializers.parseAndRemove(codec, node, "_links", new TypeReference<>() {});
        ObjectMapper mapper = (ObjectMapper) ctx.findInjectableValue("mapper", null, null);
        return new ResourceList<>(links, embedded, (Class<? extends ResourceViewModel>) modelType.getRawClass(), new JacksonBindingContext(mapper));
    }

}