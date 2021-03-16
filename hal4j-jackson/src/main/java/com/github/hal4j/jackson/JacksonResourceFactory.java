package com.github.hal4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.resources.DefaultResourceFactory;
import com.github.hal4j.resources.curie.CurieResolver;

public class JacksonResourceFactory extends DefaultResourceFactory {

    public JacksonResourceFactory(ObjectMapper mapper) {
        super(null, new JacksonBindingContext(mapper));
    }

    public JacksonResourceFactory(CurieResolver resolver, ObjectMapper mapper) {
        super(resolver, new JacksonBindingContext(mapper));
    }

}
