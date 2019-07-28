package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;

public class NavigationResourceBuilder extends ResourceBuilderSupport<NavigationResource, NavigationResourceBuilder> {

    protected NavigationResourceBuilder(CurieResolver resolver) {
        super(resolver);
    }

    @Override
    protected NavigationResourceBuilder _this() {
        return this;
    }

    @Override
    public NavigationResource build() {
        return new NavigationResource(_links, _embedded, context());
    }
}
