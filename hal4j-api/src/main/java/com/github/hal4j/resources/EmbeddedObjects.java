package com.github.hal4j.resources;

import java.util.List;
import java.util.Optional;

public interface EmbeddedObjects extends MetadataElements<Object> {
    /**
     * Find an embedded object with the given relation
     * and return the search result as an object of the given type, if any found.
     *
     * @param rel  relation of the searched object
     * @param type the class object used as a metamodel for mapping
     * @param <T>  the type of the expected result
     * @return search result as an Optional
     */
    <T> Optional<T> find(String rel, Class<T> type);

    /**
     * Find an embedded object with the given relation and return the search result
     * as a resource of the given type if any found.
     *
     * @param rel  relation of the searched object
     * @param type the class object used as a metamodel for mapping
     * @param <T>  the type of the expected result
     * @return search result as an Optional
     */
    <T> Optional<Resource<T>> findResource(String rel, Class<T> type);

    /**
     * Find a collection of embedded objects with the given relation and return
     * the search results as objects of the given type, if any found.
     *
     * @param rel  relation of searched objects
     * @param type the class object used as a metamodel for mapping
     * @param <T>  the type of the expected results
     * @return the search result as a List of objects
     */
    <T> List<T> findAll(String rel, Class<T> type);

    /**
     * Find a collection of embedded objects with the given relation and return
     * the search results as resources of the given type
     *
     * @param rel  relation of searched objects
     * @param type the class object used as a metamodel for mapping
     * @param <T>  the type of the expected results
     * @return the search result as a List of objects
     */
    <T> List<Resource<T>> findResources(String rel, Class<T> type);
}
