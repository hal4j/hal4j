package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.hal4j.resources.GenericResource;
import com.github.hal4j.resources.HALLink;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GenericResourceDeserializer extends StdDeserializer<GenericResource> {

    public GenericResourceDeserializer() {
        super(GenericResource.class);
    }

    @Override
    public GenericResource deserialize(JsonParser jp,
                                       DeserializationContext ctx) throws IOException {
        ObjectCodec codec = jp.getCodec();
        ObjectNode node = codec.readTree(jp);
        Map<String, List<Object>> embedded = Deserializers.parseAndRemove(codec, node, "_embedded", new TypeReference<Map<String, List<Object>>>(){});
        Map<String, List<HALLink>> links = Deserializers.parseAndRemove(codec, node, "_links", new TypeReference<Map<String, List<HALLink>>>(){});
        Map<String, JsonNode> model = codec.readValue(codec.treeAsTokens(node), new TypeReference<Map<String, JsonNode>>(){});
        ObjectMapper mapper = (ObjectMapper) ctx.findInjectableValue("mapper", null, null);
        return new GenericResource(model, links, embedded, new JacksonBindingContext(mapper));
    }

}
