package com.github.hal4j.resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HALLinkTest {

    @Test
    public void shouldReturnTemplateCorrectly() {
        String uriTemplate = "https://www.example.com/users/{id}";
        HALLink link = new HALLink(uriTemplate, true, null, null, null, null, null, null);
        assertEquals(uriTemplate, link.template().toString());
    }

}
