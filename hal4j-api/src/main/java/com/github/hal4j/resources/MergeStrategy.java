package com.github.hal4j.resources;

public interface MergeStrategy {

    HALLink map(HALLink link);

    Object map(Object object, BindingContext context);

}
