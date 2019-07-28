package com.github.hal4j.resources;

public interface BindingContext {

    <T> T bind(Object value, Class<T> targetType);

    <T> Resource<T> bindResource(Object value, Class<T> targetType);

}
