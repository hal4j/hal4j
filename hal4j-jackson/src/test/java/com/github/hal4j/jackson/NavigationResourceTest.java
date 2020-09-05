package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.DefaultResourceFactory;
import com.github.hal4j.resources.NavigationResource;
import com.github.hal4j.resources.Resource;
import com.github.hal4j.resources.ResourceBuilder;
import com.github.hal4j.test.model.Account;
import com.github.hal4j.test.model.Address;
import com.github.hal4j.test.model.Order;
import com.github.hal4j.test.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.github.hal4j.resources.curie.TemplateCurieResolver.curie;
import static com.github.hal4j.test.model.Money.fromString;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NavigationResourceTest {

    protected Account createModel() {
        Address address = new Address("12345", "DE", "Berlin", null, null);
        Account account = new Account("John Doe", "hello@example.com", address, fromString("1 EUR"));
        return account;
    }

    protected List<Order> createAttachments() {
        return IntStream.range(0, 10)
                .mapToObj(idx -> new Order(UUID.randomUUID(), OrderStatus.DELIVERED))
                .collect(toList());
    }

    @Test
    public void shouldCorrectlySerializeAndDeserializeNavigationResource() throws JsonProcessingException {
        DefaultResourceFactory factory = new DefaultResourceFactory(curie("http://www.example.com/rel/{ns}/{rel}"));
        String link = "http://www.example.com/api/link";
        String self = "http://www.example.com/accounts/1";
        ResourceBuilder<Account> builder = factory.bind(createModel())
                .to(self)
                .link("example:link").to(link);
        List<Order> original = createAttachments();
        builder.embed("example:orders", original);
        Resource<Account> resource = builder.build();
        ObjectMapper json = HALObjectMapperFactory.createStrictMapper();
        String first = json.writeValueAsString(resource);
        NavigationResource entry = json.readValue(first, NavigationResource.class);
        assertNotNull(entry);
        assertTrue(entry.links().include("example:link"));
    }
}
