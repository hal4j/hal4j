package com.github.hal4j.spring.web;

import com.github.hal4j.spring.AbstractLinkBuilder;
import com.github.hal4j.spring.HypermediaRequest;
import com.github.hal4j.spring.MappingDiscoverer;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class SpringWebLinkBuilder extends AbstractLinkBuilder {

    private static final MappingDiscoverer DISCOVERER = new SpringWebRequestMappingDiscoverer();

    static {
        try {
            Class.forName("org.springframework.web.bind.annotation.RequestMapping");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Classpath error: no supported implementation of request mapping found", e);
        }
    }

    protected SpringWebLinkBuilder() {
        super(currentRequest(), DISCOVERER);
    }

    private static HypermediaRequest currentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            return new HttpServletRequestWrapper(((ServletRequestAttributes) attributes).getRequest());
        } else {
            throw new IllegalStateException("getCurrentRequest() called outside of servlet request handler");
        }
    }
}
