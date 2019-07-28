package com.github.hal4j.spring;

import com.github.hal4j.uritemplate.URIBuilder;

import java.lang.reflect.Method;

public interface MappingDiscoverer {

    URIBuilder applyMapping(Class<?> clazz, URIBuilder builder);

    URIBuilder applyMapping(Method method, URIBuilder builder);

}
