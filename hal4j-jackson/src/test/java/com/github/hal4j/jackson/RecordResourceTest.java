package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.DefaultResourceFactory;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.Resource;
import com.github.hal4j.test.records.Person;
import com.github.hal4j.test.records.PersonalName;
import com.github.hal4j.test.records.ReferralCode;
import com.github.hal4j.test.records.SalutationForm;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static com.github.hal4j.resources.curie.TemplateCurieResolver.curie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RecordResourceTest {

    protected Person createModel() {
        return new Person(UUID.randomUUID(), new PersonalName("John", "Doe", null, SalutationForm.MALE), LocalDate.of(1960, 1, 12), new ReferralCode("TESTCODE"));
    }

    @Test
    public void shouldCorrectlySerializeAndDeserializeModel() throws JsonProcessingException {
        DefaultResourceFactory factory = new DefaultResourceFactory(curie("http://www.example.com/rel/{ns}/{rel}"));

        String link = "http://www.example.com/api/link";
        String self = "http://www.example.com/people/1";
        Resource<Person> resource = factory.bind(createModel())
                .to(self)
                .link("example:link").to(link)
                .build();
        ObjectMapper json = HALObjectMapperFactory.createStrictMapper();
        String string = json.writeValueAsString(resource);
        Resource<Person> parsed = json.readValue(string, new TypeReference<>() {});
        assertNotNull(parsed.self());
        assertEquals(self, parsed.self().toString());
        assertEquals(link, parsed.links().find("example:link").map(HALLink::href).orElse(null));
        assertEquals(resource.value(), parsed.required());
    }


}
