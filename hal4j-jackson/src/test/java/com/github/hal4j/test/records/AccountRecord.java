package com.github.hal4j.test.records;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.hal4j.jackson.EmbeddedResources;
import com.github.hal4j.resources.ResourceViewModel;

public record AccountRecord(String name,
                            String email,
                            @JsonUnwrapped EmbeddedResources resources) implements ResourceViewModel {
}
