package com.github.hal4j.resources;

import java.io.Serializable;
import java.net.URI;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.hal4j.resources.HALLink.REL_SELF;
import static com.github.hal4j.resources.HALLink.HREF_SAME_RESOURCE;
import static com.github.hal4j.resources.HALLink.SAME_RESOURCE;
import static com.github.hal4j.resources.MissingLinkException.missingLink;
import static java.util.Collections.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

/**
 * Core implementation of all resources which defines all HAL contracts and data model.
 *
 */
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
     * @return
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
    public URI self() {
        return links().find(REL_SELF)
                .map(HALLink::uri)
                .orElseThrow(missingLink(REL_SELF));
    }

    /**
     * Returns collection of the links associated with this resource
     * @return non-null collection of the links associated with this resource
     */
    public Links links() {
        return new Links();
    }

    /**
     * Returns collection of the embedded objects included with this resource
     * @return non-null collection  of the embedded objects included with this resource
     */
    public EmbeddedObjects embedded() {
        return new EmbeddedObjects();
    }

    /**
     * Common query operations for links and embedded objects
     * @param <T> type of object (link or embedded)
     */
    public abstract class MetadataElements<T> {

        private final Map<String, List<T>> map;

        private MetadataElements(Map<String, List<T>> map) {
            this.map = map;
        }

        /**
         * Returns underlying objects "as is", i.e. as a Map with relation keys and lists of objects.
         * May return <code>null</code>.
         * @return the underlying map or <code>null</code>
         */
        public Map<String, List<T>> asIs() {
            return this.map;
        }

        /**
         * Return all items with given relation
         * @param rel name of relation
         * @return list of items or empty list
         */
        public List<T> findAll(String rel) {
            if (rel == null) {
                throw new NullPointerException("Relation name cannot be null");
            }
            return ofNullable(map)
                    .map(m -> m.get(rel))
                    .orElse(emptyList());
        }

        /**
         * Return all items with given relation
         * @param rel name of relation as URI
         * @return list of items or empty list
         */
        public List<T> findAll(URI rel) {
            return findAll(rel.toString());
        }

        /**
         * Return any of the items with given relation
         * @param rel name of relation as URI
         * @return any found item or empty Optional
         */
        public Optional<T> find(URI rel) {
            return findAll(rel).stream().findAny();
        }

        /**
         * Return any of the items with given relation
         * @param rel name of relation
         * @return any found item or empty Optional
         */
        public Optional<T> find(String rel) {
            return findAll(rel).stream().findAny();
        }

        /**
         * Checks if any item with given relation is present
         * @param rel name of relation as URI
         * @return <code>true</code> if such relation exists, <code>false</code> otherwise.
         */
        public boolean include(URI rel) {
            return ofNullable(map)
                    .map(m -> m.containsKey(rel.toString()))
                    .orElse(false);
        }

        /**
         * Checks if any item with given relation is present
         * @param rel name of relation
         * @return <code>true</code> if such relation exists, <code>false</code> otherwise.
         */
        public boolean include(String rel) {
            return this.include(URI.create(rel));
        }

        /**
         * Count number of items with given relation
         * @param rel name of relation
         * @return number of items or 0 if relation does not exist in this resource.
         */
        public int count(String rel) {
            return this.findAll(rel).size();
        }

        /**
         * Count number of items with given relation
         * @param rel name of relation as URI
         * @return number of items or 0 if relation does not exist in this resource.
         */
        public int count(URI rel) {
            return this.findAll(rel).size();
        }

        public Stream<T> selectAll(String uri) {
            return this.findAll(uri).stream();
        }

        public Stream<T> selectAll(URI uri) {
            return this.findAll(uri).stream();
        }

        /**
         * Returns underlying objects as a Map with relation keys and lists of objects.
         * If underlying map is <code>null</code>, returns empty map.
         * @return the underlying map or empty map
         */
        public Map<String, List<T>> all() {
            return ofNullable(map).orElse(emptyMap());
        }
    }

    /**
     * Wrapper for the collection of links providing convenience methods for querying them
     */
    public class Links extends MetadataElements<HALLink> {

        Links() {
            super(_links);
        }

        /**
         * Checks if there's at least one link with given relation and name
         * @param rel name of relation
         * @param name name of the link (see {@link HALLink#name})
         * @return <code>true</code> if such link exists, <code>false</code> otherwise.
         */
        public boolean include(String rel, String name) {
            return findAll(rel).stream().anyMatch(link -> name.equals(link.name));
        }

        /**
         * Finds a link with the given name of relation and resolves it to the permanent URI of resource
         * @param rel name of relation
         * @return Optional with the link if such link exists, <code>Optional.empty</code> otherwise.
         */
        public Optional<HALLink> resolve(String rel) {
            return resolve(rel, link -> Objects.equals(null, link.name));
        }

        /**
         * Finds any matching link with given relation and name
         * @param rel name of relation
         * @param name name of the link (see {@link HALLink#name})
         * @return Optional with the link if such link exists, <code>Optional.empty</code> otherwise.
         */
        public Optional<HALLink> resolve(String rel, String name) {
            return resolve(rel, link -> Objects.equals(name, link.name));
        }

        /**
         * Finds any link with given relation that matches given condition
         * @param rel name of relation
         * @param condition the condition to match
         * @return Optional with the link if such link exists, <code>Optional.empty</code> otherwise.
         */
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
                self = findAll(REL_SELF).stream().filter(not(SAME_RESOURCE)).findFirst()
                        .orElseThrow(() -> new IllegalStateException("Self link not found"));
            }
            return Optional.of(link.resolve(self));
        }

    }

    /**
     * Wrapper for the collection of embedded objects providing convenience methods for querying them
     */
    public class EmbeddedObjects extends MetadataElements<Object> {

        EmbeddedObjects() {
            super(_embedded);
        }

        /**
         * Find an embedded object with given relation
         * and return the search result as an object of given type, if any found.
         * @param rel relation of searched object
         * @param type the class object used as a metamodel for mapping
         * @param <T> the type of the expected result
         * @return search result as an Optional
         */
        public <T> Optional<T> find(String rel, Class<T> type) {
            return find(rel).map(item -> context().bind(item, type));
        }

        /**
         * Find an embedded object with given relation and return the search result
         * as a resource of given type if any found.
         * @param rel relation of searched object
         * @param type the class object used as a metamodel for mapping
         * @param <T> the type of the expected result
         * @return search result as an Optional
         */
        public <T> Optional<Resource<T>> findResource(String rel, Class<T> type) {
            return find(rel).map(item -> context().bind(item, GenericResource.class))
                    .map(resource -> resource.as(type));
        }

        /**
         * Find a collection of embedded objects with given relation and return
         * the search results as objects of given type, if any found.
         * @param rel relation of searched objects
         * @param type the class object used as a metamodel for mapping
         * @param <T> the type of the expected results
         * @return search result as a List of objects
         */
        public <T>  List<T> findAll(String rel, Class<T> type) {
            return findAll(rel).stream()
                    .map(item -> context().bind(item, type))
                    .collect(toList());
        }

        /**
         * Find a collection of embedded objects with given relation and return
         * the search results as resources of given type
         * @param rel relation of searched objects
         * @param type the class object used as a metamodel for mapping
         * @param <T> the type of the expected results
         * @return search result as a List of objects
         */
        public <T>  List<Resource<T>> findResources(String rel, Class<T> type) {
            return findAll(rel).stream()
                    .map(item -> context().bind(item, GenericResource.class))
                    .map(resource -> resource.as(type))
                    .collect(toList());
        }

    }

}
