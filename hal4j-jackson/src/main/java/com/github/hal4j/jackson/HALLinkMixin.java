package com.github.hal4j.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public abstract class HALLinkMixin {

    @JsonCreator
    public HALLinkMixin(@JsonProperty("href") String href,
                        @JsonProperty("templated") boolean templated,
                        @JsonProperty("title") String title,
                        @JsonProperty("name") URI name,
                        @JsonProperty("type") String type,
                        @JsonProperty("hreflang") String hreflang,
                        @JsonProperty("profile") URI profile,
                        @JsonProperty("deprecation") URI deprecation) {
    }

}
