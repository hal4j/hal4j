package com.github.hal4j.spring.web;

import com.github.hal4j.resources.curie.CurieResolver;
import com.github.hal4j.uritemplate.URITemplate;

import static com.github.hal4j.spring.web.RequestBasedLinkBuilder.link;

public class RequestBasedCurieResolver implements CurieResolver {

    private final Class<?> curieController;

    public RequestBasedCurieResolver(Class<?> curieController) {
        this.curieController = curieController;
    }

    @Override
    public URITemplate resolve(String namespace) {
        return link().to(curieController).asTemplate().expandPartial(namespace);
    }

}
