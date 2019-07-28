package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;

import java.util.Collection;

public class DefaultResourceFactory implements ResourceFactory {

    private CurieResolver resolver;

    private BindingContext context;

    public DefaultResourceFactory() {
        this(null, null);
    }

    public DefaultResourceFactory(CurieResolver resolver) {
        this(resolver, null);
    }

    public DefaultResourceFactory(CurieResolver resolver, BindingContext context) {
        this.resolver = resolver != null ? resolver : anyNamespace -> null;
        this.context = context;
    }

    public <T> ResourceBuilder<T> bind(T object) {
        return new ResourceBuilder<>(object, resolver).in(context);
    }

    @Override
    public <T> ResourcesBuilder<T> bindAll(Class<T> elementType, Collection<T> objects) {
        return new ResourcesBuilder<T>(elementType, objects, resolver).in(context);
    }

    public GenericResourceBuilder bindGeneric() {
        return new GenericResourceBuilder(resolver).in(context);
    }

    @Override
    public NavigationResourceBuilder createEntry() {
        return new NavigationResourceBuilder(resolver).in(context);
    }

}
