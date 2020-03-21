package com.github.hal4j.resources;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Resource containing generic model as a Map with String keys
 */
public final class GenericResource extends ResourceSupport {

    public final Map<String, ?> model;

    public GenericResource(Map<String, ?> model,
                           Map<String, List<HALLink>> _links,
                           Map<String, List<Object>> _embedded,
                           BindingContext ctx) {
        super(_links, _embedded, ctx);
        this.model = model != null ? Collections.unmodifiableMap(model) : null;
    }

    public GenericResource(Map<String, Object> model,
                           Map<String, List<HALLink>> _links,
                           Map<String, List<Object>> _embedded) {
        this(model, _links, _embedded, null);
    }

    public GenericResource(Map<String, List<HALLink>> _links,
                           Map<String, List<Object>> _attachments) {
        this(null, _links, _attachments, null);
    }

    /**
     * Parse the generic model into object of given type and return it
     * as a resource with all the links and attachments.
     * @param clazz Class object used as a metamodel for mapping
     * @param <T> type of the expected object
     * @return this resource as a resource of given type
     */
    public <T> Resource<T> as(Class<T> clazz) {
        T instance = context().bind(this.model, clazz);
        return new Resource<>(this, instance);
    }

    /**
     * Parse the generic model into the collection of objects of given type and return it
     * as a resource with all the links and attachments. Collection items are parsed from
     * the embedded objects with relation "items".
     * @param clazz Class object used as a metamodel for mapping
     * @param <T> type of the expected object
     * @return this resource as a collection resource of given type
     */
    @SuppressWarnings("unchecked")
    public <T> Resources<T> asCollectionOf(Class<T> clazz) {
        List<Object> attachment = this.embedded().findAll(HALLink.REL_ITEMS);
        List<Resource<T>> resources = attachment.stream()
                .map(object -> context().bind(object, GenericResource.class))
                .map(resource -> resource.as(clazz))
                .collect(Collectors.toList());

        return new Resources<T>(this.links().asIs(), this.embedded().asIs(), (Class) Resource.class, resources, context());
    }

    /**
     * Convenience method chaining {@link #asCollectionOf(Class)} and {@link Resources#stream()}.
     * @param clazz Class object used as a metamodel for mapping
     * @param <T> type of the expected object
     * @return stream of resources of given type
     */
    public <T> Stream<Resource<T>> asStreamOf(Class<T> clazz) {
        return asCollectionOf(clazz).stream();
    }

    /**
     * Discard internal model and return this resource as pure navigation.
     * @return this resource as pure navigation.
     */
    public NavigationResource asRoot() {
        return new NavigationResource(this.links().asIs(), this.embedded().asIs());
    }

    /**
     * Returns the model of this resource as a Map with String keys
     * @return the internal model
     */
    public Map<String, ?> get() {
        return this.model;
    }
}
