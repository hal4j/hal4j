package com.github.hal4j.resources;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.hal4j.resources.HALLink.REL_SELF;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

/**
 * This class cannot be deserialized directly by Jackson
 * (there's no support for JsonUnwrapped/JsonCreator combo).
 * @param <T> type of the model wrapped in this resource
 */
public final class Resource<T> extends ResourceSupport {

    public final T model;

    public Resource(T model,
                    Map<String, List<HALLink>> _links,
                    Map<String, List<Object>> _embedded) {
        super(_links, _embedded, null);
        this.model = model;
    }

    public Resource(T model,
                    Map<String, List<HALLink>> _links,
                    Map<String, List<Object>> _embedded,
                    BindingContext ctx) {
        super(_links, _embedded, ctx);
        this.model = model;
    }

    public Resource(T model, URI self) {
        super(singletonMap(REL_SELF, singletonList(HALLink.create(self))), null, null);
        this.model = model;
    }

    Resource(ResourceSupport source, T model) {
        super(source);
        this.model = model;
    }

    public T value() {
        return this.model;
    }

    public T required() {
        return asOptional().orElseThrow(() -> new IllegalStateException("Undefined HAL model"));
    }

    public Optional<T> asOptional() {
        return Optional.ofNullable(model);
    }

}
