package com.github.hal4j.resources;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public interface MetadataElements<T> {

    /**
     * Returns underlying objects "as is", i.e. as a Map with relation keys and lists of objects.
     * May return <code>null</code>.
     *
     * @return the underlying map or <code>null</code>
     */
    Map<String, List<T>> asIs();

    /**
     * Return all items with given relation
     *
     * @param rel name of relation
     * @return list of items or empty list
     */
    List<T> findAll(String rel);

    /**
     * Return all items with the given relation
     *
     * @param rel name of relation as URI
     * @return list of items or empty list
     */
    List<T> findAll(URI rel);

    /**
     * Return any of the items with the given relation
     *
     * @param rel name of relation as URI
     * @return any found item or empty Optional
     */
    Optional<T> find(URI rel);

    /**
     * Return any of the items with the given relation
     *
     * @param rel name of relation
     * @return any found item or empty Optional
     */
    Optional<T> find(String rel);

    /**
     * Checks if any item with the given relation is present
     *
     * @param rel name of relation as URI
     * @return <code>true</code> if such relation exists, <code>false</code> otherwise.
     */
    boolean include(URI rel);

    /**
     * Checks if any item with the given relation is present
     *
     * @param rel name of relation
     * @return <code>true</code> if such relation exists, <code>false</code> otherwise.
     */
    boolean include(String rel);

    /**
     * Count the number of items with the given relation
     *
     * @param rel name of relation
     * @return number of items or 0 if the relation does not exist in this resource.
     */
    int count(String rel);

    /**
     * Count the number of items with the given relation
     *
     * @param rel name of relation as URI
     * @return number of items or 0 if the relation does not exist in this resource.
     */
    int count(URI rel);

    Stream<T> selectAll(String uri);

    Stream<T> selectAll(URI uri);

    /**
     * Returns underlying objects as a Map with relation keys and lists of objects.
     * If underlying map is <code>null</code>, returns empty map.
     *
     * @return the underlying map or empty map
     */
    Map<String, List<T>> all();
}
