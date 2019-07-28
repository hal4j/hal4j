package com.github.hal4j.test.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Immutable
public interface PersonalName {

    @JsonProperty
    String first();

    @JsonProperty
    String last();

    @JsonCreator
    static PersonalName create(String first, String last) {
        return new PersonalName() {
            @Override
            public String first() {
                return first;
            }

            @Override
            public String last() {
                return last;
            }
        };
    }

}
