package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

public class ResourceListBuilder<M, VM extends ResourceViewModel>
        extends ResourceBuilderSupport<ResourceList<VM>, ResourceListBuilder<M, VM>>
        implements Builder<ResourceList<VM>> {

    private final Class<VM> elementType;
    private final Collection<M> elements;
    private final ResourceFactory factory;
    private BiFunction<M, ResourceFactory, VM> binding;

    public ResourceListBuilder(Class<VM> elementType,
                               Collection<M> elements,
                               CurieResolver resolver,
                               ResourceFactory factory) {
        super(resolver);
        this.elementType = elementType;
        this.elements = elements;
        this.factory = factory;
    }

    public ResourceListBuilder<M, VM> as(BiFunction<M, ResourceFactory, VM> bindingFunction) {
        this.binding = bindingFunction;
        return this;
    }

    @Override
    protected ResourceListBuilder<M, VM> _this() {
        return this;
    }

    @Override
    public ResourceList<VM> build() {
        List<VM> model = elements.stream().map(e -> binding.apply(e, factory)).collect(toList());
        return new ResourceList<>(_links, _embedded, elementType, model, context());
    }

}
