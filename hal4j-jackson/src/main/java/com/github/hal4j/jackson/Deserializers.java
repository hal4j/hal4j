package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

class Deserializers {

    static <T> T parseAndRemove(ObjectCodec codec,
                                 ObjectNode node,
                                 String name,
                                 TypeReference<T> reference) throws IOException {
        JsonNode child = node.get(name);
        if (child == null) return null;
        T result = codec.readValue(codec.treeAsTokens(child), reference);
        node.remove(name);
        return result;
    }

}
