package com.github.hal4j.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class HALObjectMapperFactory {

    public static ObjectMapper createStrictMapper() {
        ObjectMapper mapper = createCompatibleMapper();
        mapper.enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        return mapper;
    }

    public static ObjectMapper createCompatibleMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
        mapper.setInjectableValues(new InjectableValues.Std().addValue("mapper", mapper));
        return mapper;
    }

}
