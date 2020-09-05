package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.DefaultResourceFactory;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.Resource;
import com.github.hal4j.test.model.Account;
import com.github.hal4j.test.model.Address;
import org.junit.jupiter.api.Test;

import static com.github.hal4j.resources.curie.TemplateCurieResolver.curie;
import static com.github.hal4j.test.model.Money.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourceTest {

    protected Account createModel() {
        Address address = new Address("12345", "DE", "Berlin", null, null);
        return new Account("John Doe", "hello@example.com", address, fromString("1 EUR"));
    }

    @Test
    public void shouldCorrectlySerializeAndDeserializeModel() throws JsonProcessingException {
        DefaultResourceFactory factory = new DefaultResourceFactory(curie("http://www.example.com/rel/{ns}/{rel}"));

        String link = "http://www.example.com/api/link";
        String self = "http://www.example.com/accounts/1";
        Resource<Account> resource = factory.bind(createModel())
                .to(self)
                .link("example:link").to(link)
                .build();
        ObjectMapper json = HALObjectMapperFactory.createStrictMapper();
        String string = json.writeValueAsString(resource);
        Resource<Account> parsed = json.readValue(string, new TypeReference<Resource<Account>>() {});
        assertNotNull(parsed.self());
        assertEquals(self, parsed.self().toString());
        assertEquals(link, parsed.links().find("example:link").map(HALLink::href).orElse(null));
        assertEquals(resource.value().name, parsed.required().name);
    }

}
