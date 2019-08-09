package com.github.hal4j.spring.cloud;

import com.github.hal4j.spring.HypermediaRequest;
import com.github.hal4j.spring.web.SpringWebLinkBuilder;
import com.github.hal4j.uritemplate.URIBuilder;

import java.util.Set;
import java.util.stream.Stream;

import static com.github.hal4j.resources.cloud.DiscoveryClientLinkResolver.SERVICE_NS;
import static com.github.hal4j.uritemplate.URIBuilder.uri;
import static com.github.hal4j.uritemplate.URITemplateVariable.template;
import static java.util.stream.Collectors.toSet;

public class DiscoveryLinkBuilder extends SpringWebLinkBuilder {

    private static final Set<String> LOCALHOSTS = Stream.of("localhost", "127.0.0.1").collect(toSet());

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
        return LOCALHOSTS.contains(host)
                ? builder
                : builder.server(template(SERVICE_NS + "." + this.service));
    }
}
