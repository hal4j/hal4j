package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;

import java.util.Collection;
import java.util.function.Function;

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

    @Override
    public <T> ResourceBuilder<T> bind(T object) {
        return new ResourceBuilder<>(object, resolver).in(context);
    }

    @Override
    public <T extends ResourceSupport, B extends ResourceBuilderSupport<T, B>> B bind(Function<CurieResolver, B> builder) {
        return builder.apply(resolver).in(context);
    }

    @Override
    public <T> ResourcesBuilder<T> bindAll(Class<T> elementType, Collection<T> objects) {
        return new ResourcesBuilder<T>(elementType, objects, resolver).in(context);
    }

    @Override
    public GenericResourceBuilder bindGeneric() {
        return new GenericResourceBuilder(resolver).in(context);
    }

    @Override
    public NavigationResourceBuilder bindEntry() {
        return new NavigationResourceBuilder(resolver).in(context);
    }

}
