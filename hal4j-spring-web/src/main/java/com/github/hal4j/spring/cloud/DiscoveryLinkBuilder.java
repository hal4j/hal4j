package com.github.hal4j.spring.cloud;

import com.github.hal4j.spring.HypermediaRequest;
import com.github.hal4j.spring.web.SpringWebLinkBuilder;
import com.github.hal4j.uritemplate.URIBuilder;

import static com.github.hal4j.resources.cloud.DiscoveryClientLinkResolver.SERVICE_NS;
import static com.github.hal4j.uritemplate.URIBuilder.uri;
import static com.github.hal4j.uritemplate.URITemplateVariable.preEncoded;

public class DiscoveryLinkBuilder extends SpringWebLinkBuilder {

    private final String service;

    public static DiscoveryLinkBuilder link(String service) {
        return new DiscoveryLinkBuilder(service);
    }

    private DiscoveryLinkBuilder(String service) {
        super();
        this.service = service;
    }

    @Override
    protected URIBuilder link(HypermediaRequest request) {
        String host = request.resolved().host();
        URIBuilder builder = uri(request.resolved().scheme(), host, request.resolved().port());
        return builder.server(preEncoded(SERVICE_NS + "." + this.service));
    }
}
