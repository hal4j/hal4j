package com.github.hal4j.resources;

import com.github.hal4j.resources.test.model.Any;
import com.github.hal4j.resources.test.model.UserProfile;
import com.github.hal4j.uritemplate.URITemplate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.github.hal4j.resources.curie.CurieResolver.REL_CURIES;
import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurieResolutionTest {

    public static final String CURIE_TEMPLATE = "https://example.com/docs/{ns}/{rel}";

    @Disabled
    @Test
    void shouldIncludeCurieLinksOnceInCollectionsOfResources() {
        DefaultResourceFactory factory = new DefaultResourceFactory(namespace -> new URITemplate(CURIE_TEMPLATE).expandPartial(of("ns", namespace)));

        var items = new ArrayList<UserProfile>();
        items.add(new UserProfile("Alice", "alice@example.com", Any.pictureLink()));
        items.add(new UserProfile("Bob", "bob@example.com", Any.pictureLink()));

        var resource = factory.bindAll(UserProfile.class, items).to("/object")
                .as(u -> factory.bind(u)
                        .linkSelf().to("/user/" + u.uuid())
                        .link("mail:to").to("mailto:" + u.email())
                        .asResource()).asResource();

        assertTrue(resource.links().include(REL_CURIES), "Parent must include CURIE link");

        for (var item : resource.items()) {
            assertFalse(item.links().include(REL_CURIES), "Embedded objects must not include CURIE link");
        }
    }

}
