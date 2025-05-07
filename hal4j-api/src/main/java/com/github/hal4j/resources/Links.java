package com.github.hal4j.resources;

import java.util.Optional;
import java.util.function.Predicate;

public interface Links extends MetadataElements<HALLink> {
    /**
     * Checks if there's at least one link with given relation and name
     *
     * @param rel  name of relation
     * @param name name of the link (see {@link HALLink#name})
     * @return <code>true</code> if such link exists, <code>false</code> otherwise.
     */
    boolean include(String rel, String name);

    /**
     * Finds a link with the given name of relation and resolves it to the permanent URI of resource
     *
     * @param rel name of relation
     * @return Optional with the link if such link exists, <code>Optional.empty</code> otherwise.
     */
    Optional<HALLink> resolve(String rel);

    /**
     * Finds any matching link with given relation and name
     *
     * @param rel  name of relation
     * @param name name of the link (see {@link HALLink#name})
     * @return Optional with the link if such link exists, <code>Optional.empty</code> otherwise.
     */
    Optional<HALLink> resolve(String rel, String name);

    /**
     * Finds any link with given relation that matches given condition
     *
     * @param rel       name of relation
     * @param condition the condition to match
     * @return Optional with the link if such link exists, <code>Optional.empty</code> otherwise.
     */
    Optional<HALLink> resolve(String rel, Predicate<HALLink> condition);
}
