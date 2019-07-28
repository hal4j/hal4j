package com.github.hal4j.resources;

import java.io.Serializable;
import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

import static com.github.hal4j.resources.HALLink.REL_SELF;
import static com.github.hal4j.resources.HALLink.SAME_RESOURCE;
import static com.github.hal4j.resources.MissingLinkException.missingLink;
import static java.util.Collections.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public abstract class ResourceSupport implements Serializable {

    private final BindingContext context;

    private final Map<String, List<HALLink>> _links;

    private final Map<String, List<Object>> _embedded;

    ResourceSupport(ResourceSupport resource) {
        this(resource._links, resource._embedded, resource.context);
    }

    ResourceSupport(Map<String, List<HALLink>> _links,
                    Map<String, List<Object>> _embedded,
                    BindingContext context) {
        this._links = _links != null && !_links.isEmpty() ? clone(_links) : null;
        this._embedded = _embedded != null && ! _embedded.isEmpty() ? clone(_embedded) : null;
        this.context = context;
    }

    private static <T> Map<String, List<T>> clone(Map<String, List<T>> map) {
        Map<String, List<T>> result = new HashMap<>();
        map.forEach((rel, list) -> result.put(rel, unmodifiableList(new ArrayList<>(list))));
        return Collections.unmodifiableMap(result);
    }

    public BindingContext context() {
        return context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceSupport that = (ResourceSupport) o;
        return Objects.equals(self(), that.self());
    }

    @Override
    public int hashCode() {
        return Objects.hash(self());
    }

    public URI self() {
        return links().find(REL_SELF)
                .map(HALLink::uri)
                .orElseThrow(missingLink(REL_SELF));
    }

    public Links links() {
        return new Links();
    }

    public EmbeddedObjects embedded() {
        return new EmbeddedObjects();
    }

    public class MetadataElements<T> {

        private final Map<String, List<T>> map;

        private MetadataElements(Map<String, List<T>> map) {
            this.map = map;
        }

        public Map<String, List<T>> asIs() {
            return this.map;
        }

        public List<T> findAll(String rel) {
            return ofNullable(map)
                    .map(m -> m.get(rel))
                    .orElse(emptyList());
        }

        public List<T> findAll(URI rel) {
            return findAll(rel.toString());
        }

        public Optional<T> find(URI rel) {
            return findAll(rel).stream().findAny();
        }

        public Optional<T> find(String rel) {
            return findAll(rel).stream().findAny();
        }

        public boolean include(URI uri) {
            return ofNullable(map)
                    .map(m -> m.containsKey(uri.toString()))
                    .orElse(false);
        }

        public boolean include(String uri) {
            return this.include(URI.create(uri));
        }

        public int count(String uri) {
            return this.findAll(uri).size();
        }

        public int count(URI uri) {
            return this.findAll(uri).size();
        }

        public Stream<T> selectAll(String uri) {
            return this.findAll(uri).stream();
        }

        public Stream<T> selectAll(URI uri) {
            return this.findAll(uri).stream();
        }

        public Map<String, List<T>> all() {
            return ofNullable(map).orElse(emptyMap());
        }
    }

    public class Links extends MetadataElements<HALLink> {
        public Links() {
            super(_links);
        }

        public boolean include(String rel, String name) {
            return findAll(rel).stream().anyMatch(link -> name.equals(link.name));
        }

        public Optional<HALLink> select(String rel, String name) {
            return findAll(rel).stream().filter(link -> name.equals(link.name)).findAny();
        }

        @Override
        public Optional<HALLink> find(String rel) {
            return findAll(rel).stream().filter(link -> !SAME_RESOURCE.equals(link.href)).findAny();
        }
    }

    public class EmbeddedObjects extends MetadataElements<Object> {

        public EmbeddedObjects() {
            super(_embedded);
        }

        public <T> Optional<T> find(String rel, Class<T> type) {
            return find(rel).map(item -> context().bind(item, type));
        }

        public <T>  List<T> findAll(String rel, Class<T> type) {
            return findAll(rel).stream()
                    .map(item -> context().bind(item, type))
                    .collect(toList());
        }

    }

}
