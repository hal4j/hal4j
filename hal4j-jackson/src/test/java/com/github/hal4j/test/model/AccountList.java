package com.github.hal4j.test.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.hal4j.jackson.EmbeddedResources;

public record AccountList(int size, @JsonUnwrapped EmbeddedResources resources) {
}
