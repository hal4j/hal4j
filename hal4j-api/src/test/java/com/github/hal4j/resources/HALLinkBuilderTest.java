package com.github.hal4j.resources;


import org.junit.jupiter.api.Test;

import java.net.URI;

import static com.github.hal4j.resources.HALLinkBuilder.uri;
import static org.junit.jupiter.api.Assertions.*;

public class HALLinkBuilderTest {

    public static final String SOME_VALID_URI = "http://www.example.com";
    private static final String SOME_TITLE = "Link Title (all \"chars\" allowed, e.g. $!@%?)";
    private static final String SOME_NAME = "a:b-c";
    private static final String SOME_VALID_TEMPLATE = "http://www.example.com/path{?query}";

    @Test
    public void shouldCreateSimpleLinkCorrectly() {
        String uriString = "http://www.example.com";
        HALLink link = uri(uriString).build();
        assertEquals(uriString, link.href);
        assertFalse(link.templated);
        assertFalse(link.isTemplated());
        assertEquals(URI.create(uriString), link.uri());
        assertNull(link.name);
        assertNull(link.title);
        assertNull(link.type);
        assertNull(link.hreflang);
        assertNull(link.profile);
        assertNull(link.deprecation);
    }

    @Test
    public void shouldDetectTemplateCorrectly() {
        HALLink link = uri(SOME_VALID_TEMPLATE).build();
        assertTrue(link.templated);
        assertTrue(link.isTemplated());
    }

    @Test
    public void shouldSetTitleCorrectly() {
        HALLink link = uri(SOME_VALID_URI).title(SOME_TITLE).build();
        assertEquals(SOME_TITLE, link.title);
        assertEquals(SOME_TITLE, link.title());
    }

    @Test
    public void shouldSetNameCorrectly() {
        HALLink link = uri(SOME_VALID_URI).name(SOME_NAME).build();
        assertEquals(SOME_NAME, link.name);
        assertEquals(SOME_NAME, link.name());
    }

    @Test
    public void shouldValidateName() {
        assertThrows(IllegalArgumentException.class, () -> uri(SOME_VALID_URI).name(":::::::::").build());

    }

}
