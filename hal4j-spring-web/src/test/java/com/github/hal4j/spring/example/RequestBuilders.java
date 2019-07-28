package com.github.hal4j.spring.example;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class RequestBuilders {

    private RequestBuilders() {}

    public static MockHttpServletRequest givenRequested(MockHttpServletRequestBuilder requested, String ctx) {
        MockServletContext context = new MockServletContext(ctx);
        MockHttpServletRequest request = requested.buildRequest(context);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        return request;
    }


}
