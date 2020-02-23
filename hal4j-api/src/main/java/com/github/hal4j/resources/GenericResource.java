package com.github.hal4j.resources;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public <T> Resource<T> as(Class<T> clazz) {
        T instance = context().bind(this.model, clazz);
        return new Resource<>(this, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> Resources<T> asCollectionOf(Class<T> clazz) {
        List<Object> attachment = this.embedded().findAll(HALLink.REL_ITEMS);
        List<Resource<T>> resources = attachment.stream()
                .map(object -> context().bind(object, GenericResource.class))
                .map(resource -> resource.as(clazz))
                .collect(Collectors.toList());

        return new Resources<T>(this.links().asIs(), this.embedded().asIs(), (Class) Resource.class, resources, context());
    }

    public <T> Stream<Resource<T>> asStreamOf(Class<T> clazz) {
        return asCollectionOf(clazz).stream();
    }

    public NavigationResource asRoot() {
        return new NavigationResource(this.links().asIs(), this.embedded().asIs());
    }

    public Map<String, ?> get() {
        return this.model;
    }
}
