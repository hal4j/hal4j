package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.DefaultResourceFactory;
import com.github.hal4j.resources.ResourceFactory;
import com.github.hal4j.resources.ResourceList;
import com.github.hal4j.test.model.Account;
import com.github.hal4j.test.records.AccountRecord;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static com.github.hal4j.jackson.EmbeddedResources.resources;
import static com.github.hal4j.resources.HALLink.REL_SELF;
import static com.github.hal4j.resources.curie.TemplateCurieResolver.curie;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceListTest {

    private static final String SELF = "http://www.example.com/accounts";

    @Test
    public void shouldSerializeResourceList() throws JsonProcessingException {

        DefaultResourceFactory factory = new DefaultResourceFactory(curie("http://www.example.com/rel/{ns}/{rel}"));
        String link = "http://www.example.com/api/link";

        var accounts = IntStream.range(0, 10)
                .mapToObj(idx -> new Account("John" + idx,"john" + idx + "@example.com", null, null))
                .toList();

        var resource = factory.bind(AccountRecord.class, accounts).as(ResourceListTest::accountResources).to(SELF).asResource();

        ObjectMapper mapper = HALObjectMapperFactory.createStrictMapper();
        String json = mapper.writeValueAsString(resource);
        var parsed = mapper.readValue(json, new TypeReference<ResourceList<AccountRecord>>() {});
        assertNotNull(parsed);
        assertEquals(10, parsed.size());
        var item = parsed.values().toList().get(0);
        assertTrue(item.resources().links().include(REL_SELF));

    }

    private static AccountRecord accountResources(Account account, ResourceFactory hal) {
        return new AccountRecord(account.name, account.email, hal.bind(resources()).to(SELF + "/1").asResource());
    }

}
