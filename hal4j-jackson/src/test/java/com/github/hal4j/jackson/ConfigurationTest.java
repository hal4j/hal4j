package com.github.hal4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.hal4j.test.model.DateExample;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest {

    @Test
    public void shouldCorrectlyFormatDate() throws JsonProcessingException {
        DateExample value = new DateExample(LocalDate.of(2018, 10, 24));
        String string = HALObjectMapperFactory.createStrictMapper().writeValueAsString(value);
        assertEquals("{\"date\":\"2018-10-24\"}", string);
    }

}
