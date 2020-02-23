package com.github.hal4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.GenericResource;
import com.github.hal4j.resources.NavigationResource;
import com.github.hal4j.resources.Resource;
import com.github.hal4j.resources.Resources;

public class Parsers {

    public static ParserBuilder map(ObjectMapper json) {
        return new ParserBuilder(json);
    }

    private static class ParserBuilder {
        private final ObjectMapper mapper;

        private ParserBuilder(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        public ResourceParser<GenericResource> asIs() {
            return new ResourceParser<>(mapper, resource -> resource);
        }

        public ResourceParser<NavigationResource> asRoot() {
            return new ResourceParser<>(mapper, GenericResource::asRoot);
        }

        public <T> ResourceParser<Resource<T>> toResource(Class<T> type) {
            return new ResourceParser<>(mapper, resource -> resource.as(type));
        }

        public <T> ResourceParser<Resources<T>> toCollectionOf(Class<T> type) {
            return new ResourceParser<>(mapper, resource -> resource.asCollectionOf(type));
        }

    }

}
