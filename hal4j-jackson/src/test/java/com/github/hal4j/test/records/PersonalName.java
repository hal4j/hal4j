package com.github.hal4j.test.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record PersonalName(String first,
                           String last,
                           String full,
                           SalutationForm salutation) {
}
