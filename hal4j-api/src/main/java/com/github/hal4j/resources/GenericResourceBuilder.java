package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;

import java.util.HashMap;
import java.util.Map;

public class GenericResourceBuilder
        extends ResourceBuilderSupport<GenericResource, GenericResourceBuilder>
        implements Builder<GenericResource> {

    private Map<String, Object> model = new HashMap<>();

    public GenericResourceBuilder(CurieResolver resolver) {
        super(resolver);
    }

    @Override
    protected GenericResourceBuilder _this() {
        return this;
    }

    public GenericResourceBuilder add(String key, Object value) {
        this.model.put(key, value);
        return this;
    }

    @Override
    public GenericResource build() {
        return new GenericResource(model.isEmpty() ? null : model, _links, _embedded, context());
    }

    public GenericResourceBuilder from(GenericResource resource, MergeStrategy strategy) {
        if (resource.model != null) {
            this.model.putAll(resource.model);
        }
        return merge(resource, strategy);
    }
}
