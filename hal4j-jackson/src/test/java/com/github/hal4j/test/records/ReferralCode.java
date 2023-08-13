package com.github.hal4j.test.records;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record ReferralCode(String value) {

    @JsonCreator
    public ReferralCode {
        if (value == null) throw new NullPointerException("value");
    }

    @JsonValue
    public String value() {
        return this.value;
    }

}
