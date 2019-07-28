package com.github.hal4j.spring.cloud.gw;

import com.github.hal4j.jackson.JacksonHALMapper;
import com.github.hal4j.jackson.JacksonResourceFactory;
import com.github.hal4j.resources.GenericResource;
import com.github.hal4j.resources.ResourceFactory;
import com.github.hal4j.uritemplate.URITemplate;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceResponseFilterTest {

    private static final Function<String, String> URI_MAPPER = href -> new URITemplate(href)
            .expand("auth", "api.example.com").toBuilder()
            .scheme("https").defaultPort()
            .path().replace("api", "auth")
            .toString();

    public static final JacksonHALMapper OBJECT_MAPPER = new JacksonHALMapper();
    private static final ResourceFactory FACTORY = new JacksonResourceFactory(OBJECT_MAPPER);

    @Test
    void shouldMapTrivialResourceCorrectly() {
        ResourceResponseFilter filter = new ResourceResponseFilter(FACTORY, OBJECT_MAPPER, URI_MAPPER);
        GenericResource origin = FACTORY.bindGeneric().to("http://{auth}:8480/api/v1/items/1").asResource();
        GenericResource result = filter.map(origin);
        assertEquals("https://api.example.com/auth/v1/items/1", result.self().toString());
    }

    @Test
    void shouldMapEmbeddedObjectsCorrectly() {
        ResourceResponseFilter filter = new ResourceResponseFilter(FACTORY, OBJECT_MAPPER, URI_MAPPER);

        GenericResource item = FACTORY.bindGeneric().to("http://{auth}:8480/api/v1/items/1").asResource();
        GenericResource container = FACTORY.bindGeneric().to("http://{auth}:8480/api/v1/containers/1")
                .embed("auth:item", item)
                .asResource();

        GenericResource result = filter.map(container);
        assertEquals("https://api.example.com/auth/v1/containers/1", result.self().toString());

        assertTrue(result.embedded().include("auth:item"));
        assertEquals(1, result.embedded().count("auth:item"));
        assertEquals("https://api.example.com/auth/v1/items/1",
                result.embedded().find("auth:item", GenericResource.class)
                        .map(GenericResource::self).map(URI::toString).orElse(null));

        OBJECT_MAPPER.printTo(System.out, result);

    }

}
