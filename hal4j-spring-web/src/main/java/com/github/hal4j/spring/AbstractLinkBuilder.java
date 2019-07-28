package com.github.hal4j.spring;

import com.github.hal4j.uritemplate.URIBuilder;

import java.lang.reflect.Method;

public abstract class AbstractLinkBuilder {

    protected final HypermediaRequest request;
    private final MappingDiscoverer discoverer;

    protected AbstractLinkBuilder(HypermediaRequest request,
                                  MappingDiscoverer discoverer) {
        this.request = request;
        this.discoverer = discoverer;
    }

    public URIBuilder to(Class<?> controllerClass) {
        return discoverer.applyMapping(controllerClass, link(request));
    }

    protected abstract URIBuilder link(HypermediaRequest request);

    public URIBuilder to(Method method) {
        return discoverer.applyMapping(method, link(request));
    }

}
