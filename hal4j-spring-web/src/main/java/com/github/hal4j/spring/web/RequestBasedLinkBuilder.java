package com.github.hal4j.spring.web;

import com.github.hal4j.spring.HypermediaRequest;
import com.github.hal4j.uritemplate.URIBuilder;

public class RequestBasedLinkBuilder extends SpringWebLinkBuilder {

    public static RequestBasedLinkBuilder link() {
        return new RequestBasedLinkBuilder();
    }

    /**
     * Convenience method to create a request-based link to a REST controller with a some additional path segments
     * @param controllerClass the class of the REST controller to bind to
     * @param pathSegments additional path segments used in mapping of specific methods
     * @return a URI builder for a link to the given REST controller
     */
    public static URIBuilder endpoint(Class<?> controllerClass, Object... pathSegments) {
        return new RequestBasedLinkBuilder().to(controllerClass).path().append(pathSegments);
    }

    private RequestBasedLinkBuilder() {
        super();
    }

    @Override
    protected URIBuilder link(HypermediaRequest request) {
        return URIBuilder.uri(request.scheme(), request.host(), request.port()).path().append(request.pathPrefix());
    }
}
