package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.NavigationResource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NavigationResourceDeserializer extends JsonDeserializer<NavigationResource> {

    @Override
    public NavigationResource deserialize(JsonParser jp,
                              DeserializationContext ctx) throws IOException {
        ObjectCodec codec = jp.getCodec();
        ObjectNode node = codec.readTree(jp);
        Map<String, List<Object>> embedded = Deserializers.parseAndRemove(codec, node, "_embedded", new TypeReference<Map<String, List<Object>>>(){});
        Map<String, List<HALLink>> links = Deserializers.parseAndRemove(codec, node, "_links", new TypeReference<Map<String, List<HALLink>>>(){});
        ObjectMapper mapper = (ObjectMapper) ctx.findInjectableValue("mapper", null, null);
        return new NavigationResource(links, embedded, new JacksonBindingContext(mapper));
    }

}
