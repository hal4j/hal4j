package com.github.hal4j.resources.curie;


import com.github.hal4j.uritemplate.URITemplate;

import static com.github.hal4j.uritemplate.URIFactory.templateUri;

public class TemplateCurieResolver implements CurieResolver {

    private URITemplate template;

    public static TemplateCurieResolver curie(String templateString) {
        return new TemplateCurieResolver(templateUri(templateString));
    }

    public TemplateCurieResolver(URITemplate template) {
        this.template = template;
    }

    @Override
    public URITemplate resolve(String namespace) {
        return template.expandPartial("ns", namespace);
    }

}
