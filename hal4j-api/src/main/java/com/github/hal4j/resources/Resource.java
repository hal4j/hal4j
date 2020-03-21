package com.github.hal4j.resources;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.hal4j.resources.HALLink.REL_SELF;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

/**
 * Immutable wrapper of an object of given type (resource model) including the HAL model metadata (links and embedded objects).
 *
 * Note that this class cannot be deserialized directly by Jackson
 * (there's no support for JsonUnwrapped/JsonCreator combination).
 * @param <T> type of the model wrapped in this resource
 */
public final class Resource<T> extends ResourceSupport {

    public final T model;

    /**
     * Create a resource with given model, links and embedded objects. Any of them can be <code>null</code>.
     * @param model the resource model
     * @param _links the HAL links
     * @param _embedded the embedded objects.
     */
    public Resource(T model,
                    Map<String, List<HALLink>> _links,
                    Map<String, List<Object>> _embedded) {
        super(_links, _embedded, null);
        this.model = model;
    }

    /**
     * Create a resource with given model, links and embedded objects. Any of them can be <code>null</code>.
     * The binding context can be used to transform embedded objects into typed resources.
     * @param model the resource model
     * @param _links the HAL links
     * @param _embedded the embedded objects.
     * @param ctx the binding context
     * @see BindingContext
     */
    public Resource(T model,
                    Map<String, List<HALLink>> _links,
                    Map<String, List<Object>> _embedded,
                    BindingContext ctx) {
        super(_links, _embedded, ctx);
        this.model = model;
    }

    /**
     * Create a simple resource with given model and <code>self</code> link.
     * @param model the resource model
     * @param self the permalink to this object (link with rel <code>self</code>)
     */
    public Resource(T model, URI self) {
        super(singletonMap(REL_SELF, singletonList(HALLink.create(self))), null, null);
        this.model = model;
    }

    /**
     * Create a resource with given model and copy of the links and embedded objects taken from given source.
     * @param source original resource object from which the links and objects will be taken.
     * @param model the resource model
     */
    Resource(ResourceSupport source, T model) {
        super(source);
        this.model = model;
    }

    /**
     * Returns the value of resource model as is even if it is <code>null</code>
     * @return the resource model or <code>null</code>
     */
    public T value() {
        return this.model;
    }

    /**
     * Returns the domain model of the resource
     * @return the resource model
     * @throws IllegalStateException if resource model is <code>null</code>
     */
    public T required() {
        return asOptional().orElseThrow(() -> new IllegalStateException("Undefined HAL model"));
    }

    /**
     * Returns the domain model of the resource in an optional object
     * @return an optional object containing resource model
     */
    public Optional<T> asOptional() {
        return Optional.ofNullable(model);
    }

}
