package com.github.hal4j.resources.test.examples;

import com.github.hal4j.resources.*;
import com.github.hal4j.resources.test.model.UserProfile;
import com.github.hal4j.uritemplate.URIBuilder;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static com.github.hal4j.resources.HALLink.REL_SELF;
import static com.github.hal4j.resources.HALLinkBuilder.uri;
import static com.github.hal4j.resources.ResourceRelation.link;
import static com.github.hal4j.resources.StandardRelations.*;
import static com.github.hal4j.resources.test.model.UserProfile.someUserProfile;
import static com.github.hal4j.uritemplate.URIBuilder.basedOn;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectOperationsTest {

    private final ResourceFactory factory = new DefaultResourceFactory();

    @Test
    public void shouldRespondWithAvailableOperationsOnResource() {

        String endpoint = "https://api.example.com/users/erikamuster";
        String relPassword = "user:password";

        UserProfile user = someUserProfile();
        Supplier<Boolean> admin = () -> false;
        Supplier<Boolean> owner = () -> true;

        Resource<UserProfile> resource = factory.bind(user).linkSelf().to(endpoint)
                .when(admin).add(DELETE, UPDATE)
                .when(owner).apply(b -> b.add(UPDATE, link(relPassword, uri(basedOn(endpoint).path().join("password")).profile(HTTP_METHOD_PUT))))
                .asResource();

        assertTrue(resource.links().include(relPassword));
        assertTrue(resource.links().findAll(REL_SELF).stream().anyMatch(l -> "update".equals(l.name())));

    }

}
