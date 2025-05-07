package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.DefaultResourceFactory;
import com.github.hal4j.resources.GenericResource;
import com.github.hal4j.test.model.Order;
import com.github.hal4j.test.model.OrderStatus;
import com.github.hal4j.test.records.AccountRecord;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.github.hal4j.jackson.EmbeddedResources.resources;
import static com.github.hal4j.resources.HALLink.REL_SELF;
import static com.github.hal4j.resources.ResourceCollection.REL_ITEMS;
import static com.github.hal4j.resources.curie.TemplateCurieResolver.curie;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

public class EmbeddedResourcesTest {

    protected List<Order> createAttachments() {
        return IntStream.range(0, 10)
                .mapToObj(idx -> new Order(UUID.randomUUID(), OrderStatus.DELIVERED))
                .collect(toList());
    }

    @Test
    public void shouldSerializeAndDeserializeEmbeddedResourcesUnwrappedInContainer() throws JsonProcessingException {
        DefaultResourceFactory factory = new DefaultResourceFactory(curie("http://www.example.com/rel/{ns}/{rel}"));
        String link = "http://www.example.com/api/link";
        String self = "http://www.example.com/accounts/1";

        EmbeddedResources resources = factory.bind(resources()).to(self)
                .link("example:link").to(link)
                .embed("example:orders", createAttachments())
                .build();

        var account = new AccountRecord("Alice", "alice@example.com", resources);

        ObjectMapper mapper = HALObjectMapperFactory.createStrictMapper();
        String json = mapper.writeValueAsString(account);
        var parsed = mapper.readValue(json, AccountRecord.class);
        assertNotNull(parsed);
        assertNotNull(parsed.resources());
        assertTrue(parsed.resources().links().include("example:link"));
    }

    @Test
    public void shouldSupportCollectionOfRecords() throws JsonProcessingException {
        DefaultResourceFactory factory = new DefaultResourceFactory(curie("http://www.example.com/rel/{ns}/{rel}"));
        String link = "http://www.example.com/api/link";
        String self = "http://www.example.com/accounts";

        var accounts = IntStream.range(0, 10)
                .mapToObj(idx -> new AccountRecord("John" + idx,"john" + idx + "@example.com", factory.bind(resources()).to(self + "/" + idx).asResource()))
                .toList();

        var collection = factory.bind(resources()).to(self).embed(REL_ITEMS, accounts).asResource();

        ObjectMapper mapper = HALObjectMapperFactory.createStrictMapper();
        String json = mapper.writeValueAsString(collection);
        var parsed = mapper.readValue(json, GenericResource.class);
        assertNotNull(parsed);
        var parsedCollection = parsed.embedded().findAll(REL_ITEMS, AccountRecord.class);
        assertEquals(10, parsedCollection.size());
        var item = parsedCollection.get(0);
        assertTrue(item.resources().links().include(REL_SELF));

    }

}
