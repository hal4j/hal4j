package com.github.hal4j.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Resources<T> extends ResourceSupport implements ResourceCollection<T, Resource<T>> {

    private final Class<T> elementType;

    public Resources(Map<String, List<HALLink>> _links,
                     Map<String, List<Object>> _embedded,
                     Class<T> elementType,
                     BindingContext ctx) {
        super(_links, _embedded, ctx);
        this.elementType = elementType;
    }

    Resources(ResourceSupport resource, Class<T> elementType) {
        super(resource);
        this.elementType = elementType;
    }

    public Resources(Map<String, List<HALLink>> links,
                     Map<String, List<Object>> attachments,
                     Class<T> elementType,
                     List<Resource<T>> collection,
                     BindingContext ctx) {
        super(links, merge(attachments, collection), ctx);
        this.elementType = elementType;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, List<Object>> merge(Map<String, List<Object>> attachments, List<?> collection) {
        Map<String, List<Object>> result = attachments != null ? new HashMap<>(attachments) : new HashMap<>();
        result.put(REL_ITEMS, (List) collection);
        return result;
    }

    @Override
    public Class<T> type() {
        return elementType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<Resource<T>> resources() {
        return embedded().selectAll(REL_ITEMS)
                .map(item -> item instanceof Resource
                        ? (Resource<T>) item
                        : context().bindResource(item, type()));
    }

    @Override
    public Stream<T> values() {
        return stream().map(Resource::value);
    }

    public List<Resource<T>> items() {
        return stream().collect(Collectors.toList());
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public Stream<Resource<T>> stream() {
        return resources();
    }

    public void forEachResource(Consumer<? super Resource<T>> consumer) {
        resources().forEach(consumer);
    }

    public void forEach(Consumer<? super T> consumer) {
        values().forEach(consumer);
    }

    @Override
    public String toString() {
        return "Resources('" + self() + "': " + size() + ' ' + type().getSimpleName() + " items)";
    }

}
