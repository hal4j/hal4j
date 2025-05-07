package com.github.hal4j.resources;

import java.util.List;
import java.util.stream.Stream;

public interface ResourceCollection<T, R extends ResourceSupport> extends ResourceObject {

    String REL_ITEMS = "items";

    default int size() {
        return embedded().count(REL_ITEMS);
    }

    Stream<R> resources();

    Stream<T> values();

    Class<T> type();

}
