package com.github.hal4j.jackson;

import com.github.hal4j.test.model.DateExample;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest {

    @Test
    public void shouldCorrectlyFormatDate() {
        String string = new JacksonHALMapper().serialize(new DateExample(LocalDate.of(2018, 10, 24)));
        assertEquals("{\"date\":\"2018-10-24\"}", string);
    }

}
