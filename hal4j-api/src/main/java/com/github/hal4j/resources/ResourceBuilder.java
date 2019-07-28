package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;

public class ResourceBuilder<T>
        extends ResourceBuilderSupport<Resource<T>, ResourceBuilder<T>>
        implements Builder<Resource<T>> {

    private final T model;

    public ResourceBuilder(T model, CurieResolver resolver) {
        super(resolver);
        this.model = model;
    }

    @Override
    protected ResourceBuilder<T> _this() {
        return this;
    }

    @Override
    public Resource<T> build() {
        return new Resource<T>(model, _links, _embedded, context());
    }

}
