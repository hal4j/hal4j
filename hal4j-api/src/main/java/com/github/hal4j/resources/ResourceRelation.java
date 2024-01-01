package com.github.hal4j.resources;

import java.util.Objects;

public final class ResourceRelation {

    private final String rel;

    private final HALLink link;

    public ResourceRelation(String rel, HALLink link) {
        this.rel = rel;
        this.link = link;
    }

    public static ResourceRelation link(String name, HALLinkBuilder link) {
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
        return rel + " -> " + link;
    }

}
