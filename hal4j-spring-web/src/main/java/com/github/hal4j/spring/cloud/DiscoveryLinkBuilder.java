package com.github.hal4j.spring.cloud;

import com.github.hal4j.spring.HypermediaRequest;
import com.github.hal4j.spring.web.SpringWebLinkBuilder;
import com.github.hal4j.uritemplate.URIBuilder;

import static com.github.hal4j.resources.cloud.DiscoveryClientLinkResolver.SERVICE_NS;
import static com.github.hal4j.uritemplate.URIBuilder.uri;
import static com.github.hal4j.uritemplate.URITemplateVariable.preEncoded;

public class DiscoveryLinkBuilder extends SpringWebLinkBuilder {

    private final String service;
    
    private boolean allowLocal;

    public static DiscoveryLinkBuilder link(String service) {
        return new DiscoveryLinkBuilder(service);
    }

    private DiscoveryLinkBuilder(String service) {
        super();
        this.service = service;
    }

    public DiscoveryLinkBuilder allowLocal() {
        this.allowLocal = true;
        return this;
    }
    
    @Override
    protected URIBuilder link(HypermediaRequest request) {
        String host = request.resolved().host();
        URIBuilder builder = uri(request.resolved().scheme(), host, request.resolved().port());
        return "localhost".equals(host) && allowLocal ? builder : builder.server(preEncoded(SERVICE_NS + "." + this.service));
    }
}
