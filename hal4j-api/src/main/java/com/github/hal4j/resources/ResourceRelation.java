package com.github.hal4j.resources;

import java.util.Objects;

public final class ResourceRelation {

    private final String rel;

    private final HALLink link;

    public ResourceRelation(String rel, HALLink link) {
        this.rel = rel;
        this.link = link;
    }

    /**
     * @deprecated will be removed in v2.0 to avoid name conflict with link builders, use {@link #rel(String, HALLinkBuilder)} instead.
     */
    @Deprecated(forRemoval = true)
    public static ResourceRelation link(String name, HALLinkBuilder link) {
        return rel(name, link);
    }

    /**
     * Create new resource relation with given name and link
     * @param name name of the relation
     * @param link the HAL link to the resource
     * @return new ResourceRelation for use in {@link ResourceBuilderSupport#add} method
     */
    public static ResourceRelation rel(String name, HALLinkBuilder link) {
        return new ResourceRelation(name, link.build());
    }

    public String name() {
        return rel;
    }

    public HALLink link() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceRelation that = (ResourceRelation) o;
        return Objects.equals(rel, that.rel) && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rel, link);
    }

    @Override
    public String toString() {
        return rel + " : " + link;
    }

}
