package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.hal4j.resources.DefaultResourceFactory;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.Resources;
import com.github.hal4j.test.model.Account;
import com.github.hal4j.test.model.Address;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.hal4j.resources.curie.TemplateCurieResolver.curie;
import static com.github.hal4j.test.model.Money.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourcesTest {

    protected List<Account> createModel() {
        List<Account> items = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Address address = new Address("1043"+ i, "DE", "Berlin",
                    null,
                    null);
            items.add(new Account("John " + i, "hello" + i + "@example.com", address, fromString(i + " EUR")));
        }
        return items;
    }

    @Test
    public void shouldCorrectlySerializeAndDeserializeModel() {
        DefaultResourceFactory factory = new DefaultResourceFactory(curie("http://www.example.com/rel/{ns}/{rel}"));

        String link = "http://www.example.com/api/link";
        String self = "http://www.example.com/accounts";
        Resources<Account> resources = factory.bindAll(Account.class, createModel())
                .to(self)
                .as(account -> {
                    String aself = "http://www.example.com/accounts/" + account.address.postalCode;
                    return factory.bind(account)
                            .to(aself)
                            .build();
                })
                .link("example:link").to(link)
                .build();

        JacksonHALMapper json = new JacksonHALMapper();
        String string = json.serialize(resources);
        Resources<Account> parsed = json.parse(string, new TypeReference<Resources<Account>>() {});
        assertNotNull(parsed.self());
        assertEquals(self, parsed.self().toString());
        assertEquals(link, parsed.links().find("example:link").map(HALLink::href).orElse(null));
        assertEquals(resources.items().size(), parsed.items().size());
    }

}
