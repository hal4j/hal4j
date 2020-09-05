package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.HALLink;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HALLinkSerializationTest {

    @Test
    public void shouldCorrectlySerializeAndDeserializeLink() throws JsonProcessingException {
        HALLink link = HALLink.create(URI.create("http://www.example.com"));
        ObjectMapper json = HALObjectMapperFactory.createStrictMapper();
        String string = json.writeValueAsString(link);
        HALLink parsed = json.readValue(string, HALLink.class);
        assertEquals(link.href, parsed.href);
        assertEquals(link.templated, parsed.templated);
    }

}
