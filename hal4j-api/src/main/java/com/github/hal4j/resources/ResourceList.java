package com.github.hal4j.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ResourceList<T extends ResourceViewModel> extends ResourceSupport implements ResourceCollection<T, ResourceSupport> {

    private final Class<T> elementType;

    public ResourceList(Map<String, List<HALLink>> _links,
                        Map<String, List<Object>> _embedded,
                        Class<T> elementType,
                        BindingContext ctx) {
        super(_links, _embedded, ctx);
        this.elementType = elementType;
    }

    public ResourceList(Map<String, List<HALLink>> links,
                        Map<String, List<Object>> attachments,
                        Class<T> elementType,
                        List<T> collection,
                        BindingContext ctx) {
        super(links, merge(attachments, collection), ctx);
        this.elementType = elementType;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
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
    public Stream<T> values() {
        return embedded().findAll(REL_ITEMS, elementType).stream();
    }

    @Override
    public Stream<ResourceSupport> resources() {
        return values().map(ResourceViewModel::resources);
    }

    @Override
    public String toString() {
        return "Resources('" + self() + "': " + size() + ' ' + type().getSimpleName() + " items)";
    }

}
