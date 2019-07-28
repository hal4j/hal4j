package com.github.hal4j.test.model;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalNameTest {

    @Test
    public void shouldSerializeAndDeserializeImmutableInterface() throws IOException {
        PersonalName name = PersonalName.create("Alice", "Doe");

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        AnnotationIntrospector ai = mapper.getDeserializationConfig().getAnnotationIntrospector();
        mapper.setAnnotationIntrospector(AnnotationIntrospector.pair(new AnnotationIntrospector() {
            @Override
            public Object findDeserializer(Annotated am) {
                if (of(am.getRawType().getInterfaces()).anyMatch(iface -> iface.isAnnotationPresent(Immutable.class))) {
                    return new ImmutableObjectDeserializer();
                }
                return null;
            }

            @Override
            public Version version() {
                return Version.unknownVersion();
            }
        }, ai));

        String s = mapper.writeValueAsString(name);
        PersonalName p = mapper.readValue(s, PersonalName.class);
        assertEquals(name.first(), p.first());

    }

    static <T> Optional<T> ifPresent(Supplier<T> supplier) {
        return ofNullable(supplier.get());
    }

}
