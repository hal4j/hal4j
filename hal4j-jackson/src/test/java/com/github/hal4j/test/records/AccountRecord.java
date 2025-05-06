package com.github.hal4j.test.records;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.hal4j.jackson.EmbeddedResources;

public record AccountRecord(String name,
                            int age,
                            @JsonUnwrapped EmbeddedResources resources) {
}
