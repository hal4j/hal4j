package com.github.hal4j.resources;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.hal4j.resources.HALLink.REL_SELF;
import static com.github.hal4j.resources.HALLinkBuilder.alt;
import static com.github.hal4j.resources.HALLinkBuilder.uri;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultResourceFactoryTest {

    @Test
    void shouldBindEmptyObjectToRelativeSelfLinkCorrectly() {
        DefaultResourceFactory factory = new DefaultResourceFactory();
        Resource<Object> resource = factory.bind(new Object()).to("/object").asResource();
        assertEquals("/object", resource.self().toString());
    }

    @Test
    void shouldAlwaysLinkRelationWithoutCondition() {
        DefaultResourceFactory factory = new DefaultResourceFactory();
        Resource<Object> resource = factory.bind(new Object()).to("/object")
                .link("ns:ref").to("/object/ref")
                .asResource();
        assertTrue(resource.links().include("ns:ref"));
        assertTrue(resource.links().find("ns:ref").isPresent());
    }

    @Test
    void shouldLinkRelationConditionally() {
        DefaultResourceFactory factory = new DefaultResourceFactory();
        Resource<Object> resource = factory.bind(new Object()).to("/object")
                .link("ns:ref").when(false).to("/object/ref")
                .asResource();
        assertFalse(resource.links().include("ns:ref"));
    }

    @Test
    void shouldLinkRelationConditionallyWithDeferredVeto() {
        Predicate<URI> never = rel -> false;
        DefaultResourceFactory factory = new DefaultResourceFactory();
        Resource<Object> resource = factory.bind(new Object()).to("/object")
                .link("ns:ref").when(never).to("/object/ref")
                .asResource();
        assertFalse(resource.links().include("ns:ref"));
    }

    @Test
    void shouldLinkRelationConditionallyWithDeferredCondition() {
        Supplier<Boolean> never = () -> false;
        DefaultResourceFactory factory = new DefaultResourceFactory();
        Resource<Object> resource = factory.bind(new Object()).to("/object")
                .link("ns:ref").when(never).to("/object/ref")
                .asResource();
        assertFalse(resource.links().include("ns:ref"));
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
