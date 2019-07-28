package com.github.hal4j.resources.curie;

import com.github.hal4j.uritemplate.URITemplate;

import java.util.HashMap;
import java.util.Map;

public class MapBasedCurieResolver implements CurieResolver {

    private Map<String, URITemplate> mappings = new HashMap<>();

    public MapBasedCurieResolver() {
    }

    public MapBasedCurieResolver(String rel, URITemplate template) {
        this.mappings.put(rel, template);
    }

    public MapBasedCurieResolver add(String rel, URITemplate template) {
        this.mappings.put(rel, template);
        return this;
    }

    public URITemplate resolve(String uri) {
        return mappings.get(uri);
    }

}
