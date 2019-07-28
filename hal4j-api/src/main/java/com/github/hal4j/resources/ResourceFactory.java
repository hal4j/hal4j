package com.github.hal4j.resources;

import java.util.Collection;

public interface ResourceFactory {

    <T> ResourceBuilder<T> bind(T object);

    <T> ResourcesBuilder<T> bindAll(Class<T> elementType, Collection<T> objects);

    GenericResourceBuilder bindGeneric();

    NavigationResourceBuilder createEntry();

}
