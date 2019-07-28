package com.github.hal4j.resources;

import org.junit.jupiter.api.Test;

import static com.github.hal4j.resources.HALLink.REL_SELF;
import static com.github.hal4j.resources.HALLinkBuilder.alt;
import static com.github.hal4j.resources.HALLinkBuilder.uri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultResourceFactoryTest {

    @Test
    void shouldBindEmptyObjectToRelativeSelfLinkCorrectly() {
        DefaultResourceFactory factory = new DefaultResourceFactory();
        Resource<Object> resource = factory.bind(new Object()).to("/object").asResource();
        assertEquals("/object", resource.self().toString());
    }

    @Test
    void shouldBindEmptyObjectToRelativeSelfLinkWithSupportedOperationsCorrectly() {
        DefaultResourceFactory factory = new DefaultResourceFactory();
        Resource<Object> resource = factory.bind(new Object())
                .linkSelf().toAll(
                        uri("/object"),
                        alt("update").href("."),
                        alt("delete").href(".")
                ).asResource();
        assertEquals("/object", resource.self().toString());
        assertTrue(resource.links().include(REL_SELF, "update"));
        assertFalse(resource.links().include(REL_SELF, "modify"));
    }

}
