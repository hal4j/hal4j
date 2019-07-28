package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class ResourcesBuilder<T>
        extends ResourceBuilderSupport<Resources<T>, ResourcesBuilder<T>>
        implements Builder<Resources<T>> {

    private final Class<T> elementType;
    private final Collection<T> elements;
    private Function<T, Resource<T>> binding;

    public ResourcesBuilder(Class<T> elementType,
                            Collection<T> elements,
                            CurieResolver resolver) {
        super(resolver);
        this.elementType = elementType;
        this.elements = elements;
    }

    public ResourcesBuilder<T> as(Function<T, Resource<T>> bindingFunction) {
        this.binding = bindingFunction;
        return this;
    }

    @Override
    protected ResourcesBuilder<T> _this() {
        return this;
    }

    @Override
    public Resources<T> build() {
        List<Resource<T>> model = elements.stream().map(binding).collect(toList());
        return new Resources<T>(_links, _embedded, elementType, model, context());
    }

}
