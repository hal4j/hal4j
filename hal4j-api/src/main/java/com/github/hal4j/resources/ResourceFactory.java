package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;

import java.util.Collection;
import java.util.function.Function;

public interface ResourceFactory {

    <T> ResourceBuilder<T> bind(T object);

    <T extends ResourceSupport, B extends ResourceBuilderSupport<T, B>> B bind(Function<CurieResolver, B> builder);

    <T> ResourcesBuilder<T> bindAll(Class<T> elementType, Collection<T> objects);

    GenericResourceBuilder bindGeneric();

    NavigationResourceBuilder bindEntry();

}
