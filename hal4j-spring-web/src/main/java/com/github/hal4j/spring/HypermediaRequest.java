package com.github.hal4j.spring;

public interface HypermediaRequest {

    String scheme();

    String host();

    int port();

    HypermediaRequest resolved();

    String pathPrefix();

}
