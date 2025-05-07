package com.github.hal4j.resources;

import java.io.Serializable;
import java.net.URI;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.hal4j.resources.HALLink.*;
import static java.util.Collections.*;
import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;

/**
 * Core implementation of all resources which defines all HAL contracts and data model.
 *
 */
public abstract class ResourceSupport implements Serializable, ResourceObject {

    private final BindingContext context;

    private final URI self;

    private final Map<String, List<HALLink>> _links;

    private final Map<String, List<Object>> _embedded;

    protected ResourceSupport(ResourceSupport resource) {
        this(resource._links, resource._embedded, resource.context);
    }

    protected ResourceSupport(Map<String, List<HALLink>> _links,
                    Map<String, List<Object>> _embedded,
                    BindingContext context) {
        this._links = _links != null && !_links.isEmpty() ? clone(_links) : null;
        HALLink self = null;
        if (this._links != null) {
            List<HALLink> all = this._links.get(REL_SELF);
            if (all != null) {
                for (HALLink link : all) {
                    if (link.name == null) {
                        self = link;
                        break;
                    } else if (self == null) {
                        self = link;
                    }
                }
            }
        }
        this.self = self != null ? self.uri() : null;
        this._embedded = _embedded != null && ! _embedded.isEmpty() ? clone(_embedded) : null;
        this.context = context;
    }

    private static <T> Map<String, List<T>> clone(Map<String, List<T>> map) {
        Map<String, List<T>> result = new HashMap<>();
        map.forEach((rel, list) -> result.put(rel, unmodifiableList(new ArrayList<>(list))));
        return Collections.unmodifiableMap(result);
    }

    /**
     * Returns binding context used to construct this resource object
     * @return the binding context or <code>null</code> if not used/set.
     */
    public BindingContext context() {
        return context;
    }

    /**
     * Checks if this resource equals given object. Two resources are considered equal if their <code>self</code> links are equal.
     * @param that object to check for equality
     * @return <code>true</code> if <code>that</code> object is a resource and it has the same <code>self</code> link.
     * @see HALLink#REL_SELF
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof ResourceSupport)) return false;
        ResourceSupport thatResource = (ResourceSupport) that;
        URI uri;
        try {
            uri = self();
        } catch (MissingLinkException e) {
            return false;
        }
        return Objects.equals(uri, thatResource.self());
    }

    /**
     * Returns hash code of this resource defined as hash code of the <code>self</code> link
     * @return hash code of this object
     */
    @Override
    public int hashCode() {
        try {
            return Objects.hash(self());
        } catch (MissingLinkException e) {
            return 0;
        }
    }

    /**
     * Returns URI of the link with the rel <code>self</code>
     * @return the <code>self</code> link
     * @throws MissingLinkException if resource does not contain <code>self</code> link
     */
    @Override
    public URI self() {
        if (this.self == null) {
            throw new MissingLinkException(REL_SELF);
        }
        return this.self;
    }

    /**
     * Returns collection of the links associated with this resource
     * @return non-null collection of the links associated with this resource
     */
    @Override
    public Links links() {
        return new LinksImpl();
    }

    /**
     * Returns collection of the embedded objects included with this resource
     * @return non-null collection  of the embedded objects included with this resource
     */
    @Override
    public EmbeddedObjects embedded() {
        return new EmbeddedObjectsImpl();
    }

    /**
     * Common query operations for links and embedded objects
     * @param <T> type of object (link or embedded)
     */
    public static abstract class MetadataElementMap<T> implements MetadataElements<T> {

        private final Map<String, List<T>> map;

        private MetadataElementMap(Map<String, List<T>> map) {
            this.map = map;
        }

        @Override
        public Map<String, List<T>> asIs() {
            return this.map;
        }

        @Override
        public List<T> findAll(String rel) {
            if (rel == null) {
                throw new NullPointerException("Relation name cannot be null");
            }
            return ofNullable(map)
                    .map(m -> m.get(rel))
                    .orElse(emptyList());
        }

        @Override
        public List<T> findAll(URI rel) {
            return findAll(rel.toString());
        }

        @Override
        public Optional<T> find(URI rel) {
            return findAll(rel).stream().findAny();
        }

        @Override
        public Optional<T> find(String rel) {
            return findAll(rel).stream().findAny();
        }

        @Override
        public boolean include(URI rel) {
            return ofNullable(map)
                    .map(m -> m.containsKey(rel.toString()))
                    .orElse(false);
        }

        @Override
        public boolean include(String rel) {
            return this.include(URI.create(rel));
        }

        @Override
        public int count(String rel) {
            return this.findAll(rel).size();
        }

        @Override
        public int count(URI rel) {
            return this.findAll(rel).size();
        }

        @Override
        public Stream<T> selectAll(String uri) {
            return this.findAll(uri).stream();
        }

        @Override
        public Stream<T> selectAll(URI uri) {
            return this.findAll(uri).stream();
        }

        @Override
        public Map<String, List<T>> all() {
            return ofNullable(map).orElse(emptyMap());
        }
    }

    /**
     * Wrapper for the collection of links providing convenience methods for querying them
     */
    public class LinksImpl extends MetadataElementMap<HALLink> implements Links {

        LinksImpl() {
            super(_links);
        }

        @Override
        public boolean include(String rel, String name) {
            return findAll(rel).stream().anyMatch(link -> name.equals(link.name));
        }

        @Override
        public Optional<HALLink> resolve(String rel) {
            return resolve(rel, link -> Objects.equals(null, link.name));
        }

        @Override
        public Optional<HALLink> resolve(String rel, String name) {
            return resolve(rel, link -> Objects.equals(name, link.name));
        }

        @Override
        public Optional<HALLink> resolve(String rel, Predicate<HALLink> condition) {
            List<HALLink> links = findAll(rel);
            if (links.isEmpty()) {
                return empty();
            }
            HALLink link = null;
            HALLink self = null;
            for (int i = 0; i < links.size(); i++) {
                HALLink l = links.get(i);
                if (condition.test(l)) {
                    if (!HREF_SAME_RESOURCE.equals(l.href)) {
                        return of(l);
                    }
                    link = l;
                }
                if (l.name == null && !HREF_SAME_RESOURCE.equals(l.href)) {
                    self = l;
                }
                if (link != null && self != null) {
                    break;
                }
            }
            if (link == null) return empty();
            if (self == null) {
                self = findAll(REL_SELF).stream().filter(value -> !SAME_RESOURCE.test(value)).findFirst()
                        .orElseThrow(() -> new IllegalStateException("Self link not found"));
            }
            return Optional.of(link.resolve(self));
        }

    }

    /**
     * Wrapper for the collection of embedded objects providing convenience methods for querying them
     */
    public class EmbeddedObjectsImpl extends MetadataElementMap<Object> implements EmbeddedObjects {

        EmbeddedObjectsImpl() {
            super(_embedded);
        }

        @Override
        public <T> Optional<T> find(String rel, Class<T> type) {
            return find(rel).map(item -> context().bind(item, type));
        }

        @Override
        public <T> Optional<Resource<T>> findResource(String rel, Class<T> type) {
            return find(rel).map(item -> context().bind(item, GenericResource.class))
                    .map(resource -> resource.as(type));
        }

        @Override
        public <T>  List<T> findAll(String rel, Class<T> type) {
            return findAll(rel).stream()
                    .map(item -> context().bind(item, type))
                    .toList();
        }

        @Override
        public <T>  List<Resource<T>> findResources(String rel, Class<T> type) {
            return findAll(rel).stream()
                    .map(item -> context().bind(item, GenericResource.class))
                    .map(resource -> resource.as(type))
                    .toList();
        }

    }

    @Override
    public String toString() {
        return "ResourceObject('" + self() + "')";
    }

}
