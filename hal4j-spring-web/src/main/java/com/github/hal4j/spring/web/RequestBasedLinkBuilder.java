package com.github.hal4j.spring.web;

import com.github.hal4j.spring.HypermediaRequest;
import com.github.hal4j.uritemplate.URIBuilder;

public class RequestBasedLinkBuilder extends SpringWebLinkBuilder {

    public static RequestBasedLinkBuilder link() {
        return new RequestBasedLinkBuilder();
    }

    private RequestBasedLinkBuilder() {
        super();
    }

    @Override
    protected URIBuilder link(HypermediaRequest request) {
        return URIBuilder.uri(request.scheme(), request.host(), request.port()).path().append(request.pathPrefix());
    }
}
